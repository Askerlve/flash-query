package com.askerlve.query.core.entity;

import java.io.Serializable;

/**
 * Identifiable
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public interface Identifiable<ID extends Serializable> {
    ID getId();
}
