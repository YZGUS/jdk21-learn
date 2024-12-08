package org.zengyi.im.model;

public enum MessageEnum {

    SINGLE(1),
    GROUP(2),
    ERROR(-1);

    private final int code;

    MessageEnum(int code) {
        this.code = code;
    }

    public static MessageEnum codeOf(int code) {
        for (MessageEnum message : MessageEnum.values()) {
            if (message.code == code) {
                return message;
            }
        }
        return ERROR;
    }
}
