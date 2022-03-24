package com.askerlve.query.core.query;

import com.askerlve.query.core.query.annotation.Group;
import com.askerlve.query.core.query.annotation.Groups;
import com.askerlve.query.core.query.enums.GroupType;
import com.askerlve.query.core.query.fields.QueryField;
import com.askerlve.query.core.query.fields.QueryFields;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * QueryUtil
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class QueryUtil {
    private static final Logger LOG = LoggerFactory.getLogger(QueryUtil.class);

    private QueryUtil() {
    }

    /**
     * 构建QueryWrapper
     *
     * @param param 参数
     * @param <D> 参数类型  泛型
     * @param <T> 返回类型
     * @return QueryWrapper
     */
    public static <D extends Query, T> QueryWrapper<T> buildQueryWrapper(D param, Class<T> entityCls) {
        if (param == null) {
            return Wrappers.emptyWrapper();
        }

        QueryWrapper<T> queryWrapper = Wrappers.query();
        Class<?> paramClass = param.getClass();

        GroupNode root = new GroupNode(null, null);
        Map<String, GroupNode> nodes = new HashMap<>();

        // 获取所有@Group注解
        Groups groupsAnnotation = paramClass.getAnnotation(Groups.class);
        if (groupsAnnotation != null) {
            Map<String, GroupNode> nodeMap = Arrays.stream(groupsAnnotation.groups())
                    .collect(Collectors.toMap(Group::name, g -> new GroupNode(g.name(), g.type())));
            nodes.putAll(nodeMap);

            // 构建条件树: 条件组部分
            for (Group group : groupsAnnotation.groups()) {
                String parentName = group.groupName();
                String name = group.name();
                GroupNode node = nodes.get(name);

                if (parentName.isEmpty()) {
                    root.addChild(node);
                } else {
                    GroupNode parentNode = nodes.get(parentName);
                    if (parentNode == null) {
                        throw new RuntimeException("not found group " + parentName);
                    }

                    parentNode.addChild(node);
                }
            }
        }

        // 构建条件树: 条件部分
        List<Field> fields = ReflectionKit.getFieldList(paramClass);
        for (Field field : fields) {
            QueryField queryField = QueryFields.of(field);
            if (queryField != null) {
                String groupName = queryField.getGroupName();
                if (groupName.isEmpty() && !nodes.containsKey(groupName)) {
                    // 无条件组的条件，默认and连接
                    GroupNode defaultGroup = new GroupNode("", GroupType.and);
                    root.addChild(defaultGroup);
                    nodes.put(groupName, defaultGroup);
                }
                GroupNode node = nodes.get(groupName);
                if (node == null) {
                    throw new RuntimeException("not found group " + groupName);
                }

                node.addChild(new FieldNode(field.getName(), queryField));
            }
        }

        // 编译条件树为wrapper
        LOG.info("condition tree:\n{}", root);
        translate(root, queryWrapper, param, root.getType());

        // 排序
        List<OrderItem> orders = param.getOrders();
        if (orders != null && !orders.isEmpty()) {
            Map<String, String> field2ColumnNames = new HashMap<>();
            if (entityCls != null) {
                List<Field> allFields = TableInfoHelper.getAllFields(entityCls);
                for (Field field : allFields) {
                    TableField tableField = field.getAnnotation(TableField.class);
                    if (tableField != null && tableField.exist()) {
                        String column = tableField.value();
                        field2ColumnNames.put(field.getName(), column);
                    }
                }
            }

            for (OrderItem order : orders) {
                String orderBy = order.getOrderBy();
                orderBy = field2ColumnNames.getOrDefault(orderBy, orderBy);
                String orderDirection = order.getOrderDirection();
                if (OrderItem.DESC.equals(orderDirection)) {
                    queryWrapper.orderByDesc(orderBy);
                } else {
                    queryWrapper.orderByAsc(orderBy);
                }
            }
        }

        return queryWrapper;
    }

    private static <D, T> void translate(Node node, QueryWrapper<T> queryWrapper, D param, GroupType groupType) {
        if (node instanceof GroupNode) {
            GroupNode groupNode = (GroupNode) node;
            GroupType type = groupNode.getType();
            List<Node> children = groupNode.getChildren();
            if (!children.isEmpty()) {
                if (groupType == GroupType.or) {
                    AtomicBoolean emptyChildCondition = new AtomicBoolean(true);
                    queryWrapper.or(i -> {
                        for (Node child : children) {
                            translate(child, i, param, type);
                        }

                        if (!i.isEmptyOfWhere()) {
                            emptyChildCondition.set(false);
                        }
                    });

                    if (emptyChildCondition.get()) {
                        queryWrapper.getExpression().getNormal()
                                .remove(queryWrapper.getExpression().getNormal().size() - 1);
                    }
                } else {
                    AtomicBoolean emptyChildCondition = new AtomicBoolean(true);
                    queryWrapper.and(i -> {
                        for (Node child : children) {
                            translate(child, i, param, type);
                        }

                        if (!i.isEmptyOfWhere()) {
                            emptyChildCondition.set(false);
                        }
                    });

                    if (emptyChildCondition.get()) {
                        queryWrapper.getExpression().getNormal()
                                .remove(queryWrapper.getExpression().getNormal().size() - 1);
                    }
                }
            }
        } else if (node instanceof FieldNode) {
            FieldNode fieldNode = (FieldNode) node;
            QueryField field = fieldNode.getField();
            if (groupType == GroupType.or) {
                queryWrapper = queryWrapper.or();
            }
            field.buildCondition(queryWrapper, param);
        } else {
            throw new RuntimeException("unsupport node type: " + node.getClass());
        }
    }

    interface Node {
        String getName();

        String toString(String indent);
    }

    static class FieldNode implements Node {
        private String name;
        private QueryField field;

        public FieldNode(String name, QueryField field) {
            this.name = name;
            this.field = field;
        }

        @Override
        public String getName() {
            return name;
        }

        public QueryField getField() {
            return field;
        }

        @Override
        public String toString() {
            return toString("");
        }

        @Override
        public String toString(String indent) {
            return indent + name;
        }
    }

    static class GroupNode implements Node {
        private String name;
        private GroupType type;
        private List<Node> children = new ArrayList<>();

        protected GroupNode(String name, GroupType type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public String getName() {
            return name;
        }

        public GroupType getType() {
            return type;
        }

        public List<Node> getChildren() {
            return children;
        }

        public void addChild(Node node) {
            children.add(node);
        }

        @Override
        public String toString() {
            return toString("");
        }

        @Override
        public String toString(String indent) {
            StringBuilder sb = new StringBuilder();
            sb.append(indent).append(name).append(",").append(type).append("\n");
            String childIndent = indent.isEmpty() ? "  " : indent + "  ";
            for (int i = 0; i < children.size(); i++) {
                sb.append(children.get(i).toString(childIndent));
                if (i != children.size() - 1) {
                    sb.append("\n");
                }
            }

            return sb.toString();
        }
    }
}
