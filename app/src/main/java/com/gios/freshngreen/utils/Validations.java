package com.gios.freshngreen.utils;

import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class Validations {

    public static boolean checkEmailValidates(String username, TextInputLayout emailTextInputLayout) {
        boolean isError = false;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (username.isEmpty()) {
            isError = true;
            emailTextInputLayout.setErrorEnabled(true);
            emailTextInputLayout.setError("Please enter email");
        } else if (!pattern.matcher(username).matches()) {
            isError = true;
            emailTextInputLayout.setErrorEnabled(true);
            emailTextInputLayout.setError("Please enter a valid email address");
        }
        return isError;
    }

    public static boolean isNameValidates(String name, TextInputLayout mTextInputLayout) {
        boolean isError = false;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (name.isEmpty()) {
            isError = true;
            mTextInputLayout.setErrorEnabled(true);
            mTextInputLayout.setError("Please enter name");
        }
        return isError;
    }

    public static boolean isEmailValidates(String email) {
        boolean isError = false;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (email.isEmpty()) {
            isError = true;
        } else if (!pattern.matcher(email).matches()) {
            isError = true;
        }
        return isError;
    }
    public static boolean isNameValidates(String name) {
        boolean isError = false;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (name.isEmpty()) {
            isError = true;
        }
        return isError;
    }


    public static boolean checkPasswordValidates(String username, String password, TextInputLayout passwordTextInputLayout) {
        boolean isError = false;
        if (password.isEmpty()) {
            isError = true;
            passwordTextInputLayout.setError("Please enter password");
        } else if (!passwordValidate(password, username, passwordTextInputLayout)) {
            isError = true;
        }
        return isError;
    }

    // if you want to know which requirement was not met
    public static boolean passwordValidate(String pass, String email, TextInputLayout passwordTextInputLayout) {
        if (pass.length() < 8) {
            passwordTextInputLayout.setError("Password must have at least 8 characters");
            return false;
        }
        if (!pass.matches(".*\\d.*")) {
            passwordTextInputLayout.setError("Password must include one number");
            return false;
        }

        if (!pass.matches(".*[a-z].*")) {
            passwordTextInputLayout.setError("Password must include one lowercase letter");
            return false;
        }
        if (!pass.matches(".*[A-Z].*")) {
            passwordTextInputLayout.setError("Password must include one uppercase letter");
            return false;
        }
        if (!pass.matches(".*[!@#$%^&*+=?-].*")) {
            passwordTextInputLayout.setError("Password must include one special character [!@#$%^&*+=?]");
            return false;
        }

        if (containsPartOf(pass, email)) {
            passwordTextInputLayout.setError("Password contains username, please make it unique");
            return false;
        }
        return true;
    }

    public static boolean containsPartOf(String pass, String username) {
        int requiredMin = 4;
        for (int i = 0; (i + requiredMin) < username.length(); i++) {
            if (pass.contains(username.substring(i, i + requiredMin))) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkConfirmPasswordValidates(String confirmPassword, String password, TextInputLayout confirmPasswordTextInputLayout) {
        boolean isError = false;
        if (confirmPassword.isEmpty()) {
            isError = true;
            confirmPasswordTextInputLayout.setError("Please enter password");
        } else if (!confirmPassword.equals(password)) {
            confirmPasswordTextInputLayout.setError("Password doesn't match to confirm password");
            isError = true;
        }
        return isError;
    }

    public static boolean checkPhoneValidates(String phone, TextInputLayout textInputLayout) {
        boolean isError = false;
        if (phone.isEmpty()) {
            isError = true;
            textInputLayout.setError("Please enter phone number");
        }else if (phone.length() != 10) {
            isError = true;
            textInputLayout.setError("Please enter a valid mobile number");
        }
        return isError;
    }
}
