package com.askerlve.query.core.vo;

import com.askerlve.query.core.query.annotation.EQ;
import lombok.Data;

@Data
public class ExtendVo extends AndVo{
    @EQ(field = "e")
    private Integer e;
}
