package com.aihuishou.c2b.service.common.config.web.servlet;

/**
 * Http Method Invalid
 *
 * @author jiashuai.xie
 * @since 2018/12/25 20:06 1.0.0.RELEASE
 */
public class HttpMethodNotSupportedException extends RuntimeException {

    public HttpMethodNotSupportedException(String message) {
        super(message);
    }

}
