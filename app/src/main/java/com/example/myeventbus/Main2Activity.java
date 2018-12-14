package com.example.myeventbus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myeventbus.bus.MyBus;
import com.example.myeventbus.bus.Subscribe;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        MyBus.getInstance().regesiter(this);
        //发布事件
        MyBus.getInstance().post("1","1",2);
    }

    @Subscribe("1")
    public void test(String msg,String msg1,String msg2){
        Log.e("sencod==test===","msg:"+msg+" msg1:"+msg1+" msg2:"+msg2);
    }

    public void skip(View view) {
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyBus.getInstance().unregister(this);
    }

    public void traverse(View view) {
        MyBus.getInstance().traverse();
    }
}
