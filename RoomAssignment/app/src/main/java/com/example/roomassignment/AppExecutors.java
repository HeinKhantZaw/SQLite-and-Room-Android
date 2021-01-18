package com.example.roomassignment;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// to run db queries in another thread

public class AppExecutors {

    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor diskIO;

    private AppExecutors(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor()); //creates an Executor that uses a single worker thread ( only one thread to process db query )
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }
}

