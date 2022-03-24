package com.askerlve.query.demo.service.impl;

import com.askerlve.query.demo.entity.Test;
import com.askerlve.query.demo.mapper.TestMapper;
import com.askerlve.query.demo.service.ITestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * TestServiceImpl
 *
 * @author Askerlve
 * @date 2022/03/22 20:55
 */
@Repository
public class TestServiceImpl extends ServiceImpl<TestMapper, Test>
        implements ITestService {
}
