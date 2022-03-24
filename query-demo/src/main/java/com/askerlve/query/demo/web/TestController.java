package com.askerlve.query.demo.web;

import com.askerlve.query.core.query.Page;
import com.askerlve.query.demo.entity.Test;
import com.askerlve.query.demo.repository.ITestRepository;
import com.askerlve.query.demo.web.dto.TestListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 *
 * @author Askerlve
 * @date 2022/03/23 20:42
 */
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final ITestRepository testRepository;

    @PostMapping("/list")
    public Page<Test> list(@RequestBody TestListDTO listDTO) {
        return testRepository.page(listDTO);
    }

}
