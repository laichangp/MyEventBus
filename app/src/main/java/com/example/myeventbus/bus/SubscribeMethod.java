package com.example.myeventbus.bus;

import java.lang.reflect.Method;

public class SubscribeMethod {
    String lable;
    Method method;
    Class[] argumentType;

    public SubscribeMethod(String lable, Method method, Class[] argumentType) {
        this.lable = lable;
        this.method = method;
        this.argumentType = argumentType;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class[] getArgumentType() {
        return argumentType;
    }

    public void setArgumentType(Class[] argumentType) {
        this.argumentType = argumentType;
    }
}
