package com.example.myeventbus.bus;

public class Subscription {
    private SubscribeMethod subscribeMethod;
    private Object object;//调用者

    public Subscription(SubscribeMethod subscribeMethod, Object object) {
        this.subscribeMethod = subscribeMethod;
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public SubscribeMethod getSubscribeMethod() {
        return subscribeMethod;
    }


    public void setObject(Object object) {
        this.object = object;
    }

    public void setSubscribeMethod(SubscribeMethod subscribeMethod) {
        this.subscribeMethod = subscribeMethod;
    }
}
