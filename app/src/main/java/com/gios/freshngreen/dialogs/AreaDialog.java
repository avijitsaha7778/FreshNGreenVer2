package com.gios.freshngreen.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;

import com.gios.freshngreen.R;
import com.gios.freshngreen.adapter.AreaDialogAdapter;
import com.gios.freshngreen.databinding.AreaDialogBinding;
import com.gios.freshngreen.responseModel.profile.AreaList;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class AreaDialog extends DialogFragment implements AreaDialogAdapter.DialogInterface {
    private AreaDialogBinding binding;
    private SelectAreaInterface mInterface;
    private List<AreaList> areaLists;

    public AreaDialog(SelectAreaInterface mInterface, List<AreaList> areaLists) {
        this.mInterface = mInterface;
        this.areaLists = areaLists;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        binding = AreaDialogBinding.inflate(getLayoutInflater());

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        AreaDialogAdapter adapter = new AreaDialogAdapter(areaLists, this);
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
    public void dialogCallback(AreaList list) {
        mInterface.selectAreaCallback(list);
        dismiss();
    }

    public interface SelectAreaInterface {
        void selectAreaCallback(AreaList list);
    }

}