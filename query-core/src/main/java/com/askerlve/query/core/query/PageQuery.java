package com.askerlve.query.core.query;

/**
 * PageQuery
 *
 * @author asker_lve
 * @date 2021/4/21 17:36
 */
public class PageQuery extends Query {
    private long size = 10;
    private long current = 1;
    private boolean needTotalCount = true;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public boolean isNeedTotalCount() {
        return needTotalCount;
    }

    public void setNeedTotalCount(boolean needTotalCount) {
        this.needTotalCount = needTotalCount;
    }
}
