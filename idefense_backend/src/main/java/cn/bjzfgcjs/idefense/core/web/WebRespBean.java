package cn.bjzfgcjs.idefense.core.web;

/**
 * Created by yine on 2016/12/30.
 */
public class WebRespBean {
    // 响应码
    private int code;
    // 错误描述
    private String errmsg;
    // 可变业务包体
    private Object res;

    public WebRespBean() {
    }

    public WebRespBean(int code, String errmsg, Object res) {
        this.code = code;
        this.errmsg = errmsg;
        this.res = res;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Object getRes() {
        return res;
    }

    public void setRes(Object res) {
        this.res = res;
    }
}
