package com.gios.freshngreen.genericClasses;

import android.content.Context;

import com.gios.freshngreen.R;

import androidx.fragment.app.FragmentActivity;
import retrofit2.Response;


public class ApiErrorHandler<R> {
    public String handleError(Response<R> response, Context context, FragmentActivity activity) {
        if (response.code() == 401) {
            // TODO handle errors
        }/* else if (response.code() == 400) {
            return handle400Exception(context, response.errorBody());
        }*/
        return context.getString(com.gios.freshngreen.R.string.something_went_wrong);
    }
}
