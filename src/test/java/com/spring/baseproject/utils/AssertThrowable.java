package com.spring.baseproject.utils;

import org.junit.Assert;

public class AssertThrowable {
    public static void assertThrows(Callable callable, OnThrowable onThrowable){
        try {
            callable.call();
            Assert.fail();
        } catch (Throwable t) {
            if (onThrowable != null) {
                onThrowable.onThrowable(t);
            }
        }
    }

    public interface Callable {
        void call() throws Throwable;
    }

    public interface OnThrowable {
        void onThrowable(Throwable throwable);
    }
}
