package com.askerlve.query.core;

import com.askerlve.query.core.convertor.PageConvertor;
import com.askerlve.query.core.entity.Aggregate;
import com.askerlve.query.core.query.Page;
import com.askerlve.query.core.query.PageQuery;
import com.askerlve.query.core.query.Query;
import com.askerlve.query.core.query.QueryUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * BaseRepository
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class BaseRepository<T extends Aggregate<ID>, ID extends Serializable, S extends IService<T>>
        implements Repository<T, ID> {
    @Autowired
    private S delegateRepo;

    @Autowired
    private PageConvertor pageConvertor;

    @Override
    public T find(ID id) {
        return delegateRepo.getById(id);
    }

    @Override
    public void remove(T aggregate) {
        delegateRepo.removeById(aggregate.getId());
    }

    @Override
    public void removeById(ID id) {
        delegateRepo.removeById(id);
    }

    @Override
    public void removeBatch(@NotNull Collection<T> aggregates) {
        Set<ID> ids = aggregates.stream().map(T::getId).collect(Collectors.toSet());
        delegateRepo.removeByIds(ids);
    }

    @Override
    public void save(T aggregate) {
        delegateRepo.save(aggregate);
    }

    @Override
    public void saveBatch(@NotNull Collection<T> aggregates) {
        delegateRepo.saveBatch(aggregates);
    }

    @Override
    public void update(T aggregate) {
        delegateRepo.updateById(aggregate);
    }

    @Override
    public void updateBatch(@NotNull Collection<T> aggregates) {
        delegateRepo.updateBatchById(aggregates);
    }

    @Override
    public boolean updateByCondition(T aggregate, Query query) {
        QueryWrapper<T> wrapper = QueryUtil.buildQueryWrapper(query, delegateRepo.getEntityClass());
        return delegateRepo.update(aggregate, wrapper);
    }

    @Override
    public Page<T> page(PageQuery pageQuery) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> iPage = toMybatisPage(pageQuery);

        QueryWrapper<T> wrapper = QueryUtil.buildQueryWrapper(pageQuery, delegateRepo.getEntityClass());
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> iPageData = delegateRepo.page(iPage, wrapper);

        return pageConvertor.toPage(iPageData);
    }

    @Override
    public List<T> list(Query query) {
        QueryWrapper<T> wrapper = QueryUtil.buildQueryWrapper(query, delegateRepo.getEntityClass());
        return delegateRepo.list(wrapper);
    }

    @Override
    public T find(Query query) {
        QueryWrapper<T> wrapper = QueryUtil.buildQueryWrapper(query, delegateRepo.getEntityClass());
        return delegateRepo.getOne(wrapper);
    }

    @Override
    public int count(Query query) {
        QueryWrapper<T> wrapper = QueryUtil.buildQueryWrapper(query, delegateRepo.getEntityClass());
        return delegateRepo.count(wrapper);
    }

    @Override
    public int delete(Query query) {
        QueryWrapper<T> wrapper = QueryUtil.buildQueryWrapper(query, null);

        return delegateRepo.getBaseMapper().delete(wrapper);
    }

    protected com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> toMybatisPage(PageQuery pageQuery) {
        return new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageQuery.getCurrent(),
                pageQuery.getSize(), pageQuery.isNeedTotalCount());
    }

    public PageConvertor getPageConvertor() {
        return pageConvertor;
    }

    public S getDelegateRepo() {
        return delegateRepo;
    }
}
