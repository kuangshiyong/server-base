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
    private Object result;

    public WebRespBean() {
    }

    public WebRespBean(int code, String errmsg, Object result) {
        this.code = code;
        this.errmsg = errmsg;
        this.result = result;
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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
