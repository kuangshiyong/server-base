package cn.bjzfgcjs.idefense.core;

import cn.bjzfgcjs.idefense.device.bean.HikHandlerBean;

import java.util.concurrent.ConcurrentHashMap;

public final class DataCache {
    private static final ConcurrentHashMap<String, HikHandlerBean> hikCache = new ConcurrentHashMap<>();

    public static HikHandlerBean getHikHandler(String deviceId) {
        return hikCache.get(deviceId);
    }

    public static  HikHandlerBean setHikHandler(String deviceId, HikHandlerBean hikHandlerBean) {
        return hikCache.put(deviceId, hikHandlerBean);
    }





}
