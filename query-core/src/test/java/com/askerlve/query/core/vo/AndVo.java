package com.askerlve.query.core.vo;

import com.askerlve.query.core.query.PageQuery;
import com.askerlve.query.core.query.annotation.EQ;
import com.askerlve.query.core.query.annotation.LIKE;
import lombok.Data;

@Data
public class AndVo extends PageQuery {
    @LIKE(field = "a")
    private String a;
    @EQ(field = "b")
    private String b;
    @EQ(field = "c")
    private Integer c;
    @EQ(field = "d")
    private Integer d;
}
