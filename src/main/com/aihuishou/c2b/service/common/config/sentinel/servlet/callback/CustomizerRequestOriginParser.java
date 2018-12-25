package com.aihuishou.c2b.service.common.config.sentinel.servlet.callback;

import com.alibaba.csp.sentinel.adapter.servlet.callback.RequestOriginParser;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * {@link RequestOriginParser} Implement
 *
 * <p>
 *     will read request head {@link x-forwarded-for } if return null will read RemoteAddr
 * </p>
 *
 * @author jiashuai.xie
 * @since 2018/12/14 11:03
 */
public class CustomizerRequestOriginParser implements RequestOriginParser {

    @Override
    public String parseOrigin(HttpServletRequest request) {

        String ipAddress = getIpAddress(request);
        if (!StringUtils.hasText(ipAddress)) {
            ipAddress = "";
        }

        return ipAddress;

    }

    private String getIpAddress(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            return ip.split(",")[0];
        } else {
            return ip;
        }
    }
}