package com.askerlve.query.demo.repository.impl;

import com.askerlve.query.core.BaseRepository;
import com.askerlve.query.demo.entity.Test;
import com.askerlve.query.demo.mapper.TestMapper;
import com.askerlve.query.demo.repository.ITestRepository;
import org.springframework.stereotype.Service;

/**
 * TestRepositoryImpl
 *
 * @author Askerlve
 * @date 2022/03/23 20:37
 */
@Service
public class TestRepositoryImpl extends BaseRepository<Test, Long, TestMapper>
        implements ITestRepository {
}
