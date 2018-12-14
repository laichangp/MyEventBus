package com.example.myeventbus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myeventbus.bus.MyBus;
import com.example.myeventbus.bus.Subscribe;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //订阅
        MyBus.getInstance().regesiter(this);
    }

    @Subscribe({"1"})
    public void test1(String msg,String msg1){
        Log.e("main==test===","msg："+msg+"  msg1："+msg1);
    }
    @Subscribe({"1","2"})
    public void test2(String msg,Integer msg1){
        Log.e("main==test===","msg："+msg+"  msg1："+msg1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消订阅
        MyBus.getInstance().unregister(this);
    }

    public void skip(View view) {
        startActivity(new Intent(this,Main2Activity.class));
    }

    public void traverse(View view) {
        MyBus.getInstance().traverse();
    }
}
