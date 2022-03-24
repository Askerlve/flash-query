package com.askerlve.query.core.vo;

import com.askerlve.query.core.query.PageQuery;
import com.askerlve.query.core.query.annotation.EQ;
import com.askerlve.query.core.query.annotation.Group;
import com.askerlve.query.core.query.annotation.Groups;
import com.askerlve.query.core.query.annotation.LIKE;
import com.askerlve.query.core.query.enums.GroupType;
import lombok.Data;

@Data
@Groups(groups = {
        @Group(name = "g1", type = GroupType.or)
})
public class OrVo extends PageQuery {
    @LIKE(field = "a", groupName = "g1")
    private String a;
    @EQ(field = "b", groupName = "g1")
    private String b;
    @EQ(field = "c", groupName = "g1")
    private Integer c;
    @EQ(field = "d", groupName = "g1")
    private Integer d;
}
