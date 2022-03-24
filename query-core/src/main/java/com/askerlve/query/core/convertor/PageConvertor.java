package com.askerlve.query.core.convertor;

import com.askerlve.query.core.query.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Component;

/**
 * PageConvertor
 *
 * @author asker_lve
 * @date 2021/8/17 14:34
 */
@Component
public class PageConvertor {
    public <T> Page<T> toPage(IPage<T> pageData) {
        if (pageData == null) {
            return null;
        }

        Page<T> target = new Page<>();
        target.setRecords(pageData.getRecords());
        target.setTotal(pageData.getTotal());
        target.setSize(pageData.getSize());
        target.setCurrent(pageData.getCurrent());
        target.setPages(pageData.getPages());
        return target;
    }
}
