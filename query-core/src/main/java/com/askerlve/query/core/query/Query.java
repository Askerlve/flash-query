package com.askerlve.query.core.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Query
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class Query {
    private List<OrderItem> orders = new ArrayList<>(1);

    public List<OrderItem> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderItem> orders) {
        this.orders = orders;
    }
}
