package com.kaito.OS.MySystem;

import com.kaito.OS.Charactor.Reader;
import com.kaito.OS.Charactor.Writer;
import com.kaito.OS.Semaphore.MySemaphore;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MySystem {
    static MySemaphore operate = new MySemaphore(1);
    static MySemaphore reader_queue_mutex = new MySemaphore(1);
    static MySemaphore writer_queue_mutex = new MySemaphore(1);
    static int reading_count = 0;
    static int write_count = 0;
    static MySemaphore reading_count_mutex = new MySemaphore(1);
    static MySemaphore reader_mutex = new MySemaphore(1);
    static MySemaphore writer_mutex = new MySemaphore(1);
    static List<Writer> writers_queue = new LinkedList<>();
    static List<Reader> readers_queue = new LinkedList<>();
    static List<Thread> temp_threads = new ArrayList<>();
    static int number = 0;

    static  void addWriterQueue(Writer writer){
        writer_queue_mutex.P();
        write_count +=1;
        writers_queue.add(writer);
        writer_queue_mutex.V();
    }
     static void removeWriterQueue(Writer writer) {
        writer_queue_mutex.P();
        writers_queue.remove(writer);
        temp_threads.add(writer);
        writer_queue_mutex.V();
    }
    static void addReaderQueue(Reader reader){
        reader_queue_mutex.P();
        readers_queue.add(reader);
        reader_queue_mutex.V();
    }
    private static void removeReaderQueue(Reader reader) {
        reader_queue_mutex.P();
        readers_queue.remove(reader);
        temp_threads.add(reader);
        reader_queue_mutex.V();
    }

    public static List getTempThreads(){
        return temp_threads;
    }
    public static void startWrite(Writer writer) {
        // 添加进写者队列
        addWriterQueue(writer);
        if(write_count>1||reading_count>0){
            try {
                synchronized (writer_mutex){
                    writer_mutex.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 表示移出写者队列，记录为当前正在执行进程
        removeWriterQueue(writer);
    }

    public static void endWrite(Writer writer) {
        writer_queue_mutex.P();
        write_count -=1;
        writer_queue_mutex.V();
        // 如果还有等待的写者 那么就唤醒
        if(write_count>0){
            synchronized (writer_mutex){
                writer_mutex.notify();
            }
        }else if(readers_queue.size()>0){
            // 如果有等待的读者 那么就唤醒一个读者
            synchronized (reader_mutex){
                reader_mutex.notify();
            }
        }
        temp_threads.remove(writer);
    }

    public static void startRead(Reader reader) {
        addReaderQueue(reader);
        if(write_count>0){
            try {
                synchronized (reader_mutex){
                    reader_mutex.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        reading_count_mutex.P();
        reading_count +=1;
        reading_count_mutex.V();
        synchronized (reader_mutex){
            reader_mutex.notify();
        }
        removeReaderQueue(reader);
    }


    public static void endRead(Reader reader) {
        reading_count_mutex.P();
        reading_count -=1;
        reading_count_mutex.V();
        if (reading_count == 0){
            if (write_count>0){
                synchronized (writer_mutex){
                    writer_mutex.notify();
                }
            }
        }
        temp_threads.remove(reader);
    }
}
