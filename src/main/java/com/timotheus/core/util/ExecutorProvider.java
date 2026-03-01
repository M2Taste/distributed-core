package com.timotheus.core.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorProvider {

    private final ExecutorService executorService;

    public ExecutorProvider() {
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public ExecutorService getExecutor() {
        return executorService;
    }

    public void shutdown() {
        executorService.shutdown();
    }
}