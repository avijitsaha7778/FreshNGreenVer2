package com.gios.freshngreen.genericClasses;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

public class ApiObserver<T> implements Observer<DataWrapper<T>> {
    private ChangeListener<T> changeListener;

    public ApiObserver(ChangeListener<T> changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public void onChanged(@Nullable DataWrapper<T> tDataWrapper) {
        if (tDataWrapper != null)
            if (tDataWrapper.getApiException() != null)
                changeListener.onFail(tDataWrapper.getApiException());
            else if (tDataWrapper.getErrorMessage() != null && !tDataWrapper.getErrorMessage().isEmpty())
                changeListener.onErrorMessage(tDataWrapper.getErrorMessage());
            else
                changeListener.onSuccess(tDataWrapper.getData());
    }


    public interface ChangeListener<T> {
        void onSuccess(T response);

        void onErrorMessage(String message);

        void onFail(Exception exception);
    }
}