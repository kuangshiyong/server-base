package cn.bjzfgcjs.idefense.common.utils;

import java.util.UUID;

public class MiscUtil {

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void sleep(Long ms) {
        try { Thread.sleep(ms); } catch (Exception e){e.printStackTrace();}
    }
}
