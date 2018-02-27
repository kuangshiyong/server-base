package cn.bjzfgcjs.idefense.core;

import cn.bjzfgcjs.idefense.device.bean.HikHandlerBean;
import cn.bjzfgcjs.idefense.device.bean.SvHandlerBean;

import java.util.concurrent.ConcurrentHashMap;

public final class DataCache {
    private static final ConcurrentHashMap<String, HikHandlerBean> hikCache = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, SvHandlerBean> svCache = new ConcurrentHashMap<>();

    public static HikHandlerBean getHikHandler(String deviceId) {
        return hikCache.get(deviceId);
    }

    public static  HikHandlerBean setHikHandler(String deviceId, HikHandlerBean hikHandlerBean) {
        return hikCache.put(deviceId, hikHandlerBean);
    }

    public static SvHandlerBean setSvHandlerBean(String deviceId, SvHandlerBean svHandlerBean) {
        return svCache.put(deviceId, svHandlerBean);
    }

    public static SvHandlerBean getSvHandlerBean(String deviceId, SvHandlerBean svHandlerBean) {
        return svCache.get(deviceId);
    }
}
