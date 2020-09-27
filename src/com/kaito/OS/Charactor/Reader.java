package com.kaito.OS.Charactor;

import com.kaito.OS.MySystem.MySystem;

public class Reader extends Thread{
    @Override
    public void run() {
        MySystem.startRead(this);
        try {
            System.out.println("我是读者"+this.getName()+"正在读");
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MySystem.endRead(this);
    }
}
