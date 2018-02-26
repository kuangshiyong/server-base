package cn.bjzfgcjs.idefense.core.web;

import cn.bjzfgcjs.idefense.common.utils.GsonTool;
import cn.bjzfgcjs.idefense.core.AppCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * HTTP+JSON通用序列化返回方法
 * Created by yine on 2016/12/29.
 */
public class WebResponse {
    private static final Logger logger = LoggerFactory.getLogger(WebResponse.class);

    // ctrl层组装业务响应调用
    public static Object write(Object obj) {
        return write(obj, AppCode.OK);
    }

    public static Object write(Object obj, Integer code, String errmsg) {
        return new WebRespBean(code, errmsg, obj);
    }

    public static WebRespBean write(Object obj, AppCode appCode) {
        return new WebRespBean(appCode.getCode(), appCode.getMessage(), obj);
    }

    // AOP 组装真正响应调用
    public static ResponseEntity<String> response(Object obj, HttpStatus httpStatus) {
        return response(obj, httpStatus, true);
    }

    public static ResponseEntity<String> response(
            Object obj, HttpStatus httpStatus, boolean print) {

        HWebLogBean.setCode(httpStatus.value());
        if (print) HWebLogBean.setResponse(obj);

        String res = GsonTool.toJson(obj);
        return new ResponseEntity<>(res, httpStatus);
    }
}
