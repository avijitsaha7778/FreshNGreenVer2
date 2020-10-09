package com.gios.freshngreen.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;

import com.gios.freshngreen.R;
import com.gios.freshngreen.adapter.PincodeDialogAdapter;
import com.gios.freshngreen.adapter.PincodeDialogAdapter;
import com.gios.freshngreen.databinding.AreaDialogBinding;
import com.gios.freshngreen.databinding.PincodeDialogBinding;
import com.gios.freshngreen.responseModel.profile.PinList;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class PincodeDialog extends DialogFragment implements PincodeDialogAdapter.DialogInterface {
    private PincodeDialogBinding binding;
    private PincodeDialog.SelectPincodeInterface mInterface;
    private List<PinList> areaLists;

    public PincodeDialog(PincodeDialog.SelectPincodeInterface mInterface, List<PinList> areaLists) {
        this.mInterface = mInterface;
        this.areaLists = areaLists;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        binding = PincodeDialogBinding.inflate(getLayoutInflater());

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        PincodeDialogAdapter adapter = new PincodeDialogAdapter(areaLists, this);
        binding.recyclerView.setAdapter(adapter);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity());
        dialogBuilder.setView(binding.getRoot());
        AlertDialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setElevation(R.dimen._10sdp);

        return dialog;
    }

    @Override
    public void dialogCallback(PinList list) {
        mInterface.selectPincodeCallback(list);
        dismiss();
    }

    public interface SelectPincodeInterface {
        void selectPincodeCallback(PinList list);
    }

}