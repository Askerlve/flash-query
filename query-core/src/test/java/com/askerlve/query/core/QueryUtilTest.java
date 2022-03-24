package com.askerlve.query.core;

import com.askerlve.query.core.query.OrderItem;
import com.askerlve.query.core.query.QueryUtil;
import com.askerlve.query.core.vo.AndVo;
import com.askerlve.query.core.vo.ExtendVo;
import com.askerlve.query.core.vo.GroupVo;
import com.askerlve.query.core.vo.OrVo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class QueryUtilTest {
    @Test
    public void testGroupConditions() {
        GroupVo vo = new GroupVo();
        vo.setA("a");
        vo.setB("b");
        vo.setC(1);
        vo.setD(2);
        vo.setE(3);
        vo.setF(4);
        vo.setSize(10L);
        vo.setCurrent(1L);

        QueryWrapper<Object> wrapper = QueryUtil.buildQueryWrapper(vo, null);
        assertThat(wrapper.getSqlSegment().replaceAll("#\\{[^\\}]+\\}", "?")
                , equalTo("((((e = ? AND f = ?) OR b = ? OR c = ?) AND (a LIKE ? AND d = ?)))"));
    }

    @Test
    public void testEmptyConditions() {
        GroupVo vo = new GroupVo();

        QueryWrapper<Object> wrapper = QueryUtil.buildQueryWrapper(vo, null);
        assertThat(wrapper.isEmptyOfWhere(), equalTo(true));
    }

    @Test
    public void testOrConditions() {
        OrVo vo = new OrVo();
        vo.setA("a");
        vo.setB("b");
        vo.setC(1);
        vo.setD(2);

        QueryWrapper<Object> wrapper = QueryUtil.buildQueryWrapper(vo, null);
        assertThat(wrapper.getSqlSegment().replaceAll("#\\{[^\\}]+\\}", "?")
                , equalTo("(((a LIKE ? OR b = ? OR c = ? OR d = ?)))"));
    }

    @Test
    public void testAndConditions() {
        AndVo vo = new AndVo();
        vo.setA("a");
        vo.setB("b");
        vo.setC(1);
        vo.setD(2);

        QueryWrapper<Object> wrapper = QueryUtil.buildQueryWrapper(vo, null);
        assertThat(wrapper.getSqlSegment().replaceAll("#\\{[^\\}]+\\}", "?")
                , equalTo("(((a LIKE ? AND b = ? AND c = ? AND d = ?)))"));
    }

    @Test
    public void testOrderBy() {
        GroupVo vo = new GroupVo();
        vo.setA("a");
        vo.setB("b");
        vo.setC(1);
        vo.setD(2);
        vo.setE(3);
        vo.setF(4);
        vo.setSize(10L);
        vo.setCurrent(1L);
        vo.getOrders().add(new OrderItem("g", OrderItem.ASC));

        QueryWrapper<Foo> wrapper = QueryUtil.buildQueryWrapper(vo, Foo.class);
        assertThat(wrapper.getSqlSegment().replaceAll("#\\{[^\\}]+\\}", "?")
                , equalTo("((((e = ? AND f = ?) OR b = ? OR c = ?) AND (a LIKE ? AND d = ?))) ORDER BY x ASC"));
    }

    @Test
    public void testMultiOrderBy() {
        GroupVo vo = new GroupVo();
        vo.setA("a");
        vo.setB("b");
        vo.setC(1);
        vo.setD(2);
        vo.setE(3);
        vo.setF(4);
        vo.setSize(10L);
        vo.setCurrent(1L);
        vo.getOrders().add(new OrderItem("g", OrderItem.ASC));
        vo.getOrders().add(new OrderItem("h", OrderItem.DESC));

        QueryWrapper<Foo> wrapper = QueryUtil.buildQueryWrapper(vo, Foo.class);
        assertThat(wrapper.getSqlSegment().replaceAll("#\\{[^\\}]+\\}", "?")
                , equalTo("((((e = ? AND f = ?) OR b = ? OR c = ?) AND (a LIKE ? AND d = ?))) ORDER BY x ASC,h DESC"));
    }

    @Test
    public void testExtendQuery() {
        ExtendVo vo = new ExtendVo();
        vo.setA("a");
        vo.setB("b");
        vo.setC(1);
        vo.setD(2);
        vo.setE(3);
        vo.setSize(10L);
        vo.setCurrent(1L);

        QueryWrapper<Foo> wrapper = QueryUtil.buildQueryWrapper(vo, Foo.class);
        assertThat(wrapper.getSqlSegment().replaceAll("#\\{[^\\}]+\\}", "?")
                , equalTo("(((e = ? AND a LIKE ? AND b = ? AND c = ? AND d = ?)))"));
    }

    @Data
    static class Foo {
        @TableField("x")
        private String g;
        @TableField("h")
        private String h;
    }
}
