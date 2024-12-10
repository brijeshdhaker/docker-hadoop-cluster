package org.examples.utils;

import java.util.concurrent.Callable;

public class ThrowWrappers {

    public static void run(ThrowableRunnable runnable){
        try{
            runnable.run();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static <V> V call(Callable<V> callable){
        try{
            return callable.call();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <V> V call(Callable<V> callable, String message){
        try{
            return callable.call();
        } catch(Exception e) {
            throw new RuntimeException(message, e);
        }
    }


    @FunctionalInterface
    public interface ThrowableRunnable{
        void run() throws  Exception;
    }
}
