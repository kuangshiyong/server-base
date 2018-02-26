package cn.bjzfgcjs.idefense.core;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author heyueling
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

    /**
     * user [1000,2000)
     */
    USER_UNAUTHENTICATED(1002, "unauthenticated"),
    USER_NOT_FOUND(1003, "User not found."),
    USER_NOT_MODIFIED(1004, "Not Modified."),
    USER_NOT_BOUND(1005, "User not bound."),
    AUTH_TOO_FREQUENCY(1006, "Auth request too frequency."),
    AUTH_LIMITED(1007, "Auth limited."),
    AUTH_USER_EXISTS(1008, "Can't create user, already exists."),
    AUTH_CODE_INVALID(1009, "Auth code invalid"),
    AUTH_SESSION_NOT_MATCH(1010, "Session not match"),      // 被踢下线

    /**
     * family [2000,3000)
     */
    FAMILY_INVITE_LIMIT(2000, "family invite limit"),               //用户家庭上限（该用户已拥有20个家庭圈，无法邀请）
    FAMILY_CREATE_LIMIT(2001, "family create limit"),               //用户创建家庭上限
    USER_PERMISSION_LIMIT(2002, "user have no permission"),         //用户没有权限操作家庭
    FAMILY_MEMBER_LIMIT(2003, "family member limit"),               //家庭用户上限（包括邀请中用户）
    USER_ALREADY_EXISTS(2004, "user exist in the family"),          //该用户已被邀请过
    USER_FAMILY_EMPTY(2005, "user do not have family"),             //用户目前没有家庭圈
    FAMILY_NOT_FIND(2006, "family not find"),                       //家庭圈没有找到
    MODE_FAMILY_INFO_FAIL(2007, "mode family info fail"),           //家庭编辑失败
    MODE_FAMILY_MARK_DAY_FAIL(2008, "mode family mark day fail"),   //纪念日修改失败
    DEL_FAMILY_MARK_DAY_FAIL(2009, "del family mark day fail"),     //纪念日删除失败
    DEL_FAMILY_MEMBER_FAIL(2010, "del family member fail"),         //删除成员失败
    MARK_DAY_CREATE_LIMIT(2011, "mark day create limit"),           //纪念日达到上限
    CREATOR_LEAVE_LIMIT(2012, "creator leave limit"),               //家庭owner不予许离开家庭（保护逻辑）
    CREATOR_DELETE_LIMIT(2013, "creator delete limit"),             //家庭owner不能自删（保护逻辑）
    PHONE_ALREADY_EXISTS(2014, "phone exist in the family"),        //该手机已被邀请过
    PHONE_INVITE_LIMIT(2015, "phone invite limit"),                 //该手机被邀请的家庭圈达到上限
    PHONE_AREA_LIMIT(2016, "phone area limit"),                     //国际号码暂时不能加入家庭圈
    MEMBER_NOT_IN_FAMILY(2017, "member not in family"),             //用户不在该家庭中
    FAMILY_MARK_DAY_ALREADY_DEAL(2018, "family mark day already deal"),     //纪念日已删除
    DEL_OWN_LIMIT(2019, "del myself limit"),                         //不能删自己
    FAMILY_MEMBER_ALREADY_DEAL(2020, "family member already deal"),  //成员已删除

    /**
     * feed [3000,4000)
     */
    FEED_NOT_EXIST(3000, "feed not exist or deleted."),
    RESOURCE_NOT_EXIST(3001, "resource not exist or deleted."),
    COMMENT_NOT_EXIST(3002, "comment not exist or deleted."),
    REPLYCOMM_NOT_EXIST(3003, "to reply comment not exist or deleted."),
    SHOOT_TIME_NULL(3004, "feed shoot time need."),
    FEED_DEL_DENIED(3005, "FEED_DEL_DENIED"), // 拒绝删除他人动态
    GET_SHARE_ID_FAIL(3006, "get share feedId failed."),

    /**
     * web [10000,20000)
     */
    // 福气值活动 [10100 - 10120]
    ACTIVE_GOODFORTUNE_END(10100, "goodfortune active end."); // 枚举结束点

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
