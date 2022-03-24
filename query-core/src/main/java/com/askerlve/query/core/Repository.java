package com.askerlve.query.core;

import com.askerlve.query.core.entity.Aggregate;
import com.askerlve.query.core.query.Page;
import com.askerlve.query.core.query.PageQuery;
import com.askerlve.query.core.query.Query;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Repository
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public interface Repository<T extends Aggregate<ID>, ID extends Serializable> {
    T find(@NotNull ID id);

    boolean updateByCondition(T aggregate, Query query);

    Page<T> page(PageQuery pageQuery);

    List<T> list(Query query);

    T find(Query query);

    int count(Query query);

    void remove(@NotNull T aggregate);

    void removeById(@NotNull ID id);

    void removeBatch(@NotNull Collection<T> aggregates);

    void save(@NotNull T aggregate);

    void saveBatch(@NotNull Collection<T> aggregates);

    void update(T aggregate);

    void updateBatch(@NotNull Collection<T> aggregates);

    int delete(Query query);
}
