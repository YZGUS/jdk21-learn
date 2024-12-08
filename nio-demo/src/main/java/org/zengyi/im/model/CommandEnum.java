package org.zengyi.im.model;

public enum CommandEnum {

    CONNECT(1000),
    CHAT(1001),
    JOIN_GROUP(1002),
    DISCONNECT(1003),
    ERROR(-1);

    private final int code;

    CommandEnum(int code) {
        this.code = code;
    }

    public static CommandEnum codeOf(int code) {
        for (CommandEnum command : CommandEnum.values()) {
            if (command.code == code) {
                return command;
            }
        }
        return ERROR;
    }
}
