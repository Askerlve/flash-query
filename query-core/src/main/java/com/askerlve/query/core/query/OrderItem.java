package com.askerlve.query.core.query;

/**
 * OrderItem
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class OrderItem {
    public static final String ASC = "ASC";
    public static final String DESC = "DESC";

    /**
     * 排序字段
     */
    private String orderBy;
    /**
     * 排序方向
     */
    private String orderDirection;

    public OrderItem() {

    }

    public OrderItem(String orderBy, String orderDirection) {
        this.orderBy = orderBy;
        this.orderDirection = orderDirection;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }
}
