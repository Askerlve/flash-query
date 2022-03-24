package com.askerlve.query.demo.web.dto;

import com.askerlve.query.core.query.PageQuery;
import com.askerlve.query.core.query.annotation.EQ;
import com.askerlve.query.core.query.annotation.GT;
import com.askerlve.query.core.query.annotation.LIKE;
import com.askerlve.query.core.query.enums.SqlLike;
import lombok.Data;

/**
 * TestDTO
 *
 * @author Askerlve
 * @date 2022/03/23 20:44
 */
@Data
public class TestListDTO extends PageQuery {

    @EQ(field = "id")
    private Long id;

    @LIKE(field = "name", type = SqlLike.LEFT)
    private String name;

    @GT(field = "age")
    private Integer age;

}
