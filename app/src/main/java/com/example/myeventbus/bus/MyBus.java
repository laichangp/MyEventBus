package com.example.myeventbus.bus;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyBus {
    //类的对应所有订阅者
    private Map<Class, List<SubscribeMethod>> methodmap = new HashMap<>();
    //标签的对应所有订阅者和调用者
    private Map<String, List<Subscription>> subscriptionmap = new HashMap<>();
    //类的对应标签集合
    private Map<Class, List<String>> resultmap = new HashMap<>();


    /**
     * 1.注册
     * 2.先拿到所有被注解（订阅）的方法
     * 3.订阅
     * 4.反注册
     */

    private static volatile MyBus instance;

    public static MyBus getInstance() {
        if (null == instance) {
            synchronized (MyBus.class) {
                if (null == instance) {
                    instance = new MyBus();
                }
            }
        }
        return instance;
    }

    /**
     * 订阅
     * @param object
     */
    public void regesiter(Object object) {
        if(resultmap.containsKey(object.getClass())){
            return;
        }
        //取消订阅时用
        List<String> lables = resultmap.get(object.getClass());
        if (lables == null) {
            lables = new ArrayList<>();
            resultmap.put(object.getClass(), lables);
        }
        //找到所有的订阅者，一个标签对应一个订阅者，包含重复订阅者
        List<SubscribeMethod> subscribeMethods = findSubscribeMethod(object);
        for (SubscribeMethod subscribeMethod : subscribeMethods) {
            String lable = subscribeMethod.getLable();
            if (!lables.contains(lable)) {
                lables.add(lable);
            }
            //一个标签对应所有Subscription对象,subscriptions里的都是相同的标签
            List<Subscription> subscriptions = subscriptionmap.get(lable);
            if (subscriptions == null) {
                subscriptions = new ArrayList<>();
                subscriptionmap.put(lable, subscriptions);
            }
            subscriptions.add(new Subscription(subscribeMethod, object));
        }
    }

    /**
     * 拿到订阅的方法
     * @param object 上下文
     * @return
     */
    public List<SubscribeMethod> findSubscribeMethod(Object object) {
        Class<?> subscribeClass = object.getClass();
        List<SubscribeMethod> subscribeMethods = methodmap.get(subscribeClass);
        if (subscribeMethods == null) {
            subscribeMethods = new ArrayList<>();
            //拿到里面的所有方法
            Method[] declaredMethods = subscribeClass.getDeclaredMethods();
            for (Method method : declaredMethods) {
                //拿到注解
                Subscribe subscribe = method.getAnnotation(Subscribe.class);
                if (subscribe != null) {
                    String[] values = subscribe.value();
                    //拿到方法的参数类型集合
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    for (String lable : values) {
                        method.setAccessible(true);
                        //一个标签就存储一个subscribeMethod
                        subscribeMethods.add(new SubscribeMethod(lable, method, parameterTypes));
                    }
                }
            }
            methodmap.put(subscribeClass, subscribeMethods);
        }
        return subscribeMethods;
    }

    /**
     * 发送消息给订阅者
     *
     * @param lable  标签
     * @param params
     */
    public void post(String lable, Object... params) {
        List<Subscription> subscriptions = subscriptionmap.get(lable);
        if (null == subscriptions) {
            return;
        }
        for (Subscription subscription :
                subscriptions) {
            SubscribeMethod subscribeMethod = subscription.getSubscribeMethod();
            //调用者
            Object object = subscription.getObject();
            //方法
            Method method = subscribeMethod.getMethod();
            //参数类型数组
            Class[] argumentType = subscribeMethod.getArgumentType();
            Object[] realparams = new Object[argumentType.length];
            if (params != null) {
                for (int i = 0; i < argumentType.length; i++) {
                    //如果长度在params范围之内并且类型会对应则存储,否则存null
                    if (i < params.length && argumentType[i].isInstance(params[i])) {
                        realparams[i] = params[i];
                    } else {
                        realparams[i] = null;
                    }
                }
            }
            try {
                method.invoke(object, realparams);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取消订阅
     */
    public void unregister(Object object) {
        List<String> lables = resultmap.get(object.getClass());
        if (lables != null) {
            for (String lable : lables) {
                List<Subscription> subscriptions = subscriptionmap.get(lable);
                Iterator<Subscription> iterator = subscriptions.iterator();
                while (iterator.hasNext()){
                    Subscription subscription = iterator.next();
                    Object object1 = subscription.getObject();
                    if(object1==object){
                        //建议使用ArrayList 的iterator 的remove，foreach循环remove会报错
                        iterator.remove();
                    }
                }
            }
           if(resultmap.containsKey(object.getClass())){
               resultmap.remove(object.getClass());
           }
        }
    }

    /**
     * 遍历
     */
    public void traverse() {
        Iterator<Map.Entry<String, List<Subscription>>> iterator = subscriptionmap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<Subscription>> next = iterator.next();
            List<Subscription> value = next.getValue();
            String key = next.getKey();
            Log.e("=====","标签"+key+"的size为："+value.size());
        }
    }
}
