package com.aihuishou.c2b.service.common.config.sentinel;

/**
 * when trigger sentinel flow control,will throw FlowControlException
 *
 * @author jiashuai.xie
 */
public class FlowControlException extends RuntimeException {

    public FlowControlException(){

    }

    public FlowControlException(String message){
        super(message);
    }

    public FlowControlException(String message, Throwable cause) {
        super(message, cause);
    }

}
