package com.gios.freshngreen.genericClasses;

public class DataWrapper<T> {
    public Exception getApiException() {
        return apiException;
    }

    public void setApiException(Exception apiException) {
        this.apiException = apiException;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private Exception apiException;
    private T data;
    private String errorMessage;
}