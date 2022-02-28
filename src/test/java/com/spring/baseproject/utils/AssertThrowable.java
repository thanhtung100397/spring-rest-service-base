package com.spring.baseproject.utils;

import static org.junit.jupiter.api.Assertions.fail;

public class AssertThrowable {
    public static void assertThrows(Callable callable, OnThrowable onThrowable){
        try {
            callable.call();
            fail();
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
