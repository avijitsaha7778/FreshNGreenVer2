package com.gios.freshngreen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;


import com.gios.freshngreen.R;
import com.gios.freshngreen.utils.SharedPref;

import androidx.appcompat.app.AppCompatActivity;

import static com.gios.freshngreen.utils.Constants.LOGIN;
import static com.gios.freshngreen.utils.Constants.TYPE;

public class SplashScreenActivity extends AppCompatActivity {

    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        startApp();
    }



    private void startApp() {
        sharedPref = new SharedPref(SplashScreenActivity.this);
        new Handler().postDelayed(() -> {
            if(sharedPref != null && !sharedPref.getUserId().isEmpty() && !sharedPref.getMobile().isEmpty()){
                startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                finish();
            }else {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class).putExtra(TYPE, LOGIN));
                finish();
            }
        }, 3000);
    }
}
