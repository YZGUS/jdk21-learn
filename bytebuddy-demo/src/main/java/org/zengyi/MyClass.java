package org.zengyi;

public class MyClass {

    private final int code = 0;

    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String print() {
        return code + " : " + message;
    }

    @Override
    public String toString() {
        return "MyClass{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
