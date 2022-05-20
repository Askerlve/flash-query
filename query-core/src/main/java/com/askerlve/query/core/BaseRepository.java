package com.askerlve.query.core;

import com.askerlve.query.core.convertor.PageConvertor;
import com.askerlve.query.core.entity.Aggregate;
import com.askerlve.query.core.query.Page;
import com.askerlve.query.core.query.PageQuery;
import com.askerlve.query.core.query.Query;
import com.askerlve.query.core.query.QueryUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * BaseRepository
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class BaseRepository<T extends Aggregate<ID>, ID extends Serializable, S extends BaseMapper<T>> extends ServiceImpl<S, T>
        implements Repository<T, ID> {

    @Autowired
    private PageConvertor pageConvertor;

    @Override
    public boolean update(T aggregate, Query query) {
        QueryWrapper<T> wrapper = QueryUtil.buildQueryWrapper(query, this.getEntityClass());
        return this.update(aggregate, wrapper);
    }

    @Override
    public Page<T> page(PageQuery pageQuery) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> iPage = toMybatisPage(pageQuery);

        QueryWrapper<T> wrapper = QueryUtil.buildQueryWrapper(pageQuery, this.getEntityClass());
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> iPageData = this.page(iPage, wrapper);

        return pageConvertor.toPage(iPageData);
    }

    @Override
    public List<T> list(Query query) {
        QueryWrapper<T> wrapper = QueryUtil.buildQueryWrapper(query, this.getEntityClass());
        return this.list(wrapper);
    }

    @Override
    public T find(Query query) {
        QueryWrapper<T> wrapper = QueryUtil.buildQueryWrapper(query, this.getEntityClass());
        return this.getOne(wrapper);
    }

    @Override
    public int count(Query query) {
        QueryWrapper<T> wrapper = QueryUtil.buildQueryWrapper(query, this.getEntityClass());
        return this.count(wrapper);
    }

    @Override
    public int delete(Query query) {
        QueryWrapper<T> wrapper = QueryUtil.buildQueryWrapper(query, null);
        return this.getBaseMapper().delete(wrapper);
    }

    protected com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> toMybatisPage(PageQuery pageQuery) {
        return new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageQuery.getCurrent(),
                pageQuery.getSize(), pageQuery.isNeedTotalCount());
    }

    public PageConvertor getPageConvertor() {
        return pageConvertor;
    }
}
