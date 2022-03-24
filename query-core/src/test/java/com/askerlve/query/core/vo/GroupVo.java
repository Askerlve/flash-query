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
        @Group(name = "g1", type = GroupType.or),
        @Group(name = "g2", type = GroupType.and, groupName = "g1")
})
public class GroupVo extends PageQuery {
    @LIKE(field = "a")
    private String a;
    @EQ(field = "b", groupName = "g1")
    private String b;
    @EQ(field = "c", groupName = "g1")
    private Integer c;
    @EQ(field = "d")
    private Integer d;
    @EQ(field = "e", groupName = "g2")
    private Integer e;
    @EQ(field = "f", groupName = "g2")
    private Integer f;
}
