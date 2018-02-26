package cn.bjzfgcjs.idefense.core.web;

import cn.bjzfgcjs.idefense.common.utils.RegexUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yine on 2016/12/30.
 */
public class WebRequest {
    private static final Logger logger = LoggerFactory.getLogger(WebRequest.class);

    /**
     * 获取请求IP
     *
     * @param request
     * @param useInternalIp
     * @return
     */
    public static String getRequestIp(HttpServletRequest request, boolean useInternalIp) {
        try {
            String header1 = request.getHeader("X-Forwarded-For");
            String header2 = request.getHeader("Proxy-Client-IP");
            String header3 = request.getHeader("WL-Proxy-Client-IP");

            String header = null;
            if (StringUtils.isNotBlank(header1) && !"unknown".equalsIgnoreCase(header1)) {
                header = header1;
            } else if (StringUtils.isNotBlank(header2) && !"unknown".equalsIgnoreCase(header2)) {
                header = header2;
            } else if (StringUtils.isNotBlank(header3) && !"unknown".equalsIgnoreCase(header3)) {
                header = header3;
            }

            String realIp = null;
            if (StringUtils.isNotBlank(header)) {
                String[] ips = header.split(",");
                for (String ip : ips) {
                    // 过滤2g/3g网关添加的内网ip
                    if (!RegexUtil.isInternalIp(ip)) {
                        realIp = ip;
                        break;
                    }
                }

                // 只有内网ip并且应用允许的情况下才取内网ip
                if (realIp == null && useInternalIp) {
                    realIp = ips[0];
                }
            }

            if (StringUtils.isBlank(realIp)) {
                realIp = request.getRemoteAddr();
            }

            return realIp.trim();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 获取请求IP(过滤内网IP)
     *
     * @param request
     * @return
     */
    public static String getRequestIp(HttpServletRequest request) {
        return getRequestIp(request, false);
    }
}
