package cn.bjzfgcjs.idefense.interceptor;

import cn.bjzfgcjs.idefense.core.web.HWebLogBean;
import cn.bjzfgcjs.idefense.core.web.WebRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author heyueling
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        HWebLogBean.before();
        HWebLogBean.setPath(request.getRequestURL().toString());
        HWebLogBean.setParams(request.getParameterMap());
        HWebLogBean.setIp(WebRequest.getRequestIp(request));
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        HWebLogBean.after();
    }
}
