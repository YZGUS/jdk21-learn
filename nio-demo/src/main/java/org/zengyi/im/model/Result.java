package org.zengyi.im.model;

import com.alibaba.fastjson2.JSON;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.zengyi.handel.selector.Server;

import java.io.Serializable;

public class Result {

    // 注意成员变量的 get 和 set 方法用于序列化
    private int code;

    private String message;

    public Result(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static TextWebSocketFrame success(String message) {
        return new TextWebSocketFrame(JSON.toJSONString(new Result(message, 200)));

    }

    public static TextWebSocketFrame error(String message) {
        return new TextWebSocketFrame(JSON.toJSONString(new Result(message, 500)));
    }
}
