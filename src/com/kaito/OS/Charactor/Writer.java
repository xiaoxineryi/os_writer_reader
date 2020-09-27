package com.kaito.OS.Charactor;

import com.kaito.OS.MySystem.MySystem;

public class Writer extends Thread{

    @Override
    public void run() {

        MySystem.startWrite(this);
        try {
            System.out.println("我是写者"+this.getName()+"正在写");
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MySystem.endWrite(this);
    }
}
