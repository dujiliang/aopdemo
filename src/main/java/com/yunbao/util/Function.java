package com.yunbao.util;

public interface Function<E, T> {
    public T callback(E e);
}
