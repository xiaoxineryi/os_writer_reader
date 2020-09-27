package com.kaito.OS.Main;

import com.kaito.OS.Charactor.Reader;
import com.kaito.OS.Charactor.Writer;
import com.kaito.OS.MySystem.MySystem;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Reader reader = new Reader();
        Reader reader1 = new Reader();
        Writer writer = new Writer();
        Writer writer1 = new Writer();
        reader.start();
        reader1.start();
        System.out.println("当前正在执行线程为"+ MySystem.getTempThreads());
        Thread.sleep(500);
        writer1.start();
        writer.start();
        Writer writer2 = new Writer();
        writer2.start();

        Reader reader2 = new Reader();
        reader2.start();
    }
}
