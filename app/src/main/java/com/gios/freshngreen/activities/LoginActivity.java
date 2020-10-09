package com.gios.freshngreen.activities;

import android.os.Bundle;

import com.gios.freshngreen.R;
import com.gios.freshngreen.databinding.ActivityLoginBinding;
import com.gios.freshngreen.fragments.login.LoginFragment;
import com.gios.freshngreen.fragments.login.RegisterFragment;

import androidx.appcompat.app.AppCompatActivity;

import static com.gios.freshngreen.utils.Constants.LOGIN;
import static com.gios.freshngreen.utils.Constants.TYPE;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            String type = getIntent().getStringExtra(TYPE);
            if (type != null && type.equalsIgnoreCase(LOGIN)) {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new LoginFragment()).commit();
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new RegisterFragment()).commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
