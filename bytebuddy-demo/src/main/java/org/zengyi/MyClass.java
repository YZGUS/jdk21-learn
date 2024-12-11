package org.zengyi;

public class MyClass {

    private final int code = 0;

    private String message;

    public MyClass() {
        System.out.println("MyClass");
    }

    public MyClass(String message) {
        this.message = message;
    }

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

    public String printAge(int age) {
        return "age : " + age;
    }


    public static String sayWhat(String msg) {
        System.out.println("testStatic: " + msg);
        return "testStatic" + msg;
    }


    @Override
    public String toString() {
        return "MyClass{" + "code=" + code + ", message='" + message + '\'' + '}';
    }
}
