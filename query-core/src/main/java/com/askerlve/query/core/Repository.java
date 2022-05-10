package com.askerlve.query.core;

import com.askerlve.query.core.entity.Aggregate;
import com.askerlve.query.core.query.Page;
import com.askerlve.query.core.query.PageQuery;
import com.askerlve.query.core.query.Query;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * Repository
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public interface Repository<T extends Aggregate<ID>, ID extends Serializable> extends IService<T> {

    boolean updateByCondition(T aggregate, Query query);

    Page<T> page(PageQuery pageQuery);

    List<T> list(Query query);

    T find(Query query);

    int count(Query query);

    int delete(Query query);
}
