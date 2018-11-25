package com.example.daniel.meetkai_test.Interfaces;

public interface ResultReceiverCallback<T> {
    void onSuccess(T data);
    void onError(Exception exception);
}
