package com.gios.freshngreen.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.gios.freshngreen.R;
import com.gios.freshngreen.activities.LoginActivity;
import com.gios.freshngreen.utils.SharedPref;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.gios.freshngreen.utils.Constants.LOGIN;
import static com.gios.freshngreen.utils.Constants.TYPE;

public class LogoutDialog extends DialogFragment {
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.DialogTheme);
        builder.setMessage(R.string.logout_message)
                .setPositiveButton("Yes", (dialog, id) -> {
                    logOut(requireContext(), requireActivity());
                })
                .setNegativeButton("No", (dialog, id) -> {
                });
        return builder.create();
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException ignored) {

        }
    }

    public static void logOut(Context context, Activity activity) {
        SharedPref sharedPref = new SharedPref(context);
        sharedPref.clearAllPrefs(context);
        activity.startActivity(new Intent(activity, LoginActivity.class).putExtra(TYPE, LOGIN));
        activity.finishAffinity();
    }
}