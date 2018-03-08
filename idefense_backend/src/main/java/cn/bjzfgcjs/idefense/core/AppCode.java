package cn.bjzfgcjs.idefense.core;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * author: 钟生辉
 */
public enum AppCode {
    /**
     * wire [0,800)
     */
    OK(0, "OK"), // 成功

    /**
     * base [800,1000)
     */
    UNKNOWN(800, "UNKNOWN"), // 未知错误
    NO_AUTH(801, "NO_AUTH"), // 没有权限
    DB_ERROR(802, "DB_ERROR"), // 数据库错误
    BAD_REQUEST(803, "BAD_REQUEST"), // 非法参数
    FORBIDDEN(804, "FORBIDDEN"), // 禁止操作
    OP_CANCEL(805, "OP_CANCEL"), // 操作被取消
    FREQUENTLY(806, "FREQUENTLY"), // 频繁
    INVALID(807, "INVALID"), // 失效
    OBJECT_NOTEXIST(808, "OBJECT_NOTEXIST"), // 目标不存在
    OBJECT_EXIST(809, "OBJECT_EXIST"), // 目标存在
    NOT_MODIFIED(810, "NOT_MODIFIED"), // 没有更新
    NOT_READY(811, "NOT_READY"), // 未就绪
    EXPIRED(812, "EXPIRED"), // 过期
    NOT_DONE(813, "NOT_DONE"), // 未完成
    DONE(814, "DONE"), // 已完成
    NOT_ENOUGH(815, "NOT_ENOUGH"), // 不足
    EXCESS(816, "EXCESS"), // 过量
    DUPLICATE(817, "DUPLICATE"), // 重复
    SERVICE_BUSY(818, "TOO_BUSY"), // 服务忙
    SERVICE_DISABLE(819, "SERVICE_DISABLE"), // 服务不可用
    SERVICE_NULL(820, "SERVICE_NULL"), // 服务不存在
    WRONG_VERSION(821, "WRONG_VERSION"), // 版本错误
    WRONG_NUMBER(822, "WRONG_NUMBER"), // 号码错误
    WRONG_PRODUCT(823, "WRONG_PRODUCT"), // 非法客户端类型

    // 设备通用代码：[1000, 1100)
    DEV_OK(1000, "DEV_OK"),        // 设备正常
    DEV_BUSY(1001, "DEV_BUSY"),    // 设备忙
    DEV_NOT_INIT(1002, "DEV_NOT_INIT"),  // 设备没初始化操作资源


    // 第三方厂商的设备错误码 都要转为 标准错误码

    /**
     * dev: audio card [1100,2000) IP音频卡
     */
    // 网络配置接口方面
    AUDIO_NP_SUCC(1100, "AUDIO_NP_SUCC"),
    AUDIO_NP_EINET(1101, "AUDIO_NP_EINET"),     // 网卡错误
    AUDIO_NP_ERESP(1102, "AUDIO_NP_ERESP"),     // 返回数据错误
    AUDIO_NP_NONEXIST(1103, "AUDIO_DEV_NONEXIST"), // 设备失联
    AUDIO_NP_EWRITE(1104, "AUDIO_NP_EWRITE"),      // 写配置错误
    AUDIO_NP_EAUTH(1105, "AUDIO_NP_EAUTH"),        // 密码验证错误
    AUDIO_NP_EMEM(1106, "AUDIO_NP_EMEM"),          // 内存不足
    AUDIO_NP_EPARAM(1107, "AUDIO_NP_EPARAM"),      // 参数错误

    // 播放接口
    AUDIO_OP_OK(1200,  "AUDIO_OP_OK"),          // 操作成功
    AUDIO_OP_EPARAM(1201, "AUDIO_OP_EPARAM"),   // 操作参数错误
    AUDIO_OP_ERROR(1201, "AUDIO_OP_ERROR"),     // 执行错误
    AUDIO_OP_ESOCKET(1202, "AUDIO_OP_ESOCKET"), // socket操作错误
    AUDIO_OP_ECODEC(1203, "AUDIO_OP_ECODEC"),   // 解码器初始化失败
    AUDIO_OP_EFILE(1204, "AUDIO_OP_FILE"),      // 音频加载失败
    AUDIO_OP_EFORMAT(1205, "AUDIO_OP_EFORMAT"), // 音频类型错误, 支持mp3、wav
    AUDIO_OP_BAD_BITRATE(1206, "AUDIO_OP_BAD_BITRATE"),
    AUDIO_OP_DECODE_BAN(1207, "AUDIO_OP_DECODE_BAN"); // 禁用解码功能，而播放此文件需要解码

    /**
     * dev: camera [2000,5000)  摄像机
     */


    /**
     * dev: terrence  [3000,4000) 云台
     */

    /**
     * dev:radar [4000,5000)  雷达
     */

    private int code;
    private String message;

    AppCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private static final Map<Integer, AppCode> lookup = new HashMap<>();

    static {
        for (AppCode o : EnumSet.allOf(AppCode.class)) {
            lookup.put(o.getCode(), o);
        }
    }

    public static AppCode lookup(int code) {
        return lookup.get(code);
    }
}
