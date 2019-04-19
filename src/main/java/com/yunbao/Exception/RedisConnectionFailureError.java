package com.yunbao.Exception;

public class RedisConnectionFailureError extends Exception{

    public RedisConnectionFailureError() {
        super();
    }

    public RedisConnectionFailureError(String message) {
        super(message);
    }

}
