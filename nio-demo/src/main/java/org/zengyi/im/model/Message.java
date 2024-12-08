package org.zengyi.im.model;

public class Message {

    private String name;

    private String target;

    private int commandType;

    private int messageType;

    private String content;

    public Message() {
    }

    public Message(String name, String target, int commandType, int messageType, String content) {
        this.name = name;
        this.target = target;
        this.commandType = commandType;
        this.messageType = messageType;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getCommandType() {
        return commandType;
    }

    public void setCommandType(int commandType) {
        this.commandType = commandType;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String message() {
        return name + ": " + content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "name='" + name + '\'' +
                ", target='" + target + '\'' +
                ", commandType=" + commandType +
                ", messageType=" + messageType +
                ", content='" + content + '\'' +
                '}';
    }
}
