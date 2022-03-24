package com.askerlve.query.core.interceptor;

/**
 * UpdateFlagHolder
 *
 * @author asker_lve
 * @date 2021/8/17 14:03
 */
public class UpdateFlagHolder {

    private UpdateFlagHolder() {
    }

    private static ThreadLocal<Boolean> likeQueryUpdateFlagHolder = new ThreadLocal<>();

    public static void put(Boolean updateFlag) {
        likeQueryUpdateFlagHolder.set(updateFlag);
    }

    public static Boolean get() {
        Boolean updateFlag = likeQueryUpdateFlagHolder.get();
        return updateFlag;
    }

    public static void clear() {
        likeQueryUpdateFlagHolder.remove();
    }

}
