package com.move.ndksocket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainAct extends AppCompatActivity {

    static {
        System.loadLibrary("test");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
    }

    public void startListener(View v) {
        new Thread(){
            @Override
            public void run() {
                System.out.println("server() = " + server());
            }
        }.start();
    }

    public void udpStartListener(View v) {
        new Thread(){
            @Override
            public void run() {
                System.out.println("udpServer() = " + udpServer());
            }
        }.start();
    }

    public void connect(View v) {
        new Thread(){
            @Override
            public void run() {
                System.out.println("client() = " + client());
            }
        }.start();
    }

    public void send(View v) {
        System.out.println("send() = " + send());
    }


    public void udpSend(View v) {
        System.out.println("udpSend() = " + udpSend());
    }

    public static native String server();

    public static native String client();

    public static native int send();

    public static native String udpServer();

    public static native String udpSend();

}
