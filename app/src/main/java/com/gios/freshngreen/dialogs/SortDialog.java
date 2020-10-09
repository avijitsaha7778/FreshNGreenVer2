package com.gios.freshngreen.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.gios.freshngreen.R;
import com.gios.freshngreen.adapter.ProductListAdapter;
import com.gios.freshngreen.databinding.FragmentProductListBinding;
import com.gios.freshngreen.fragments.product.ProductListFragment;
import com.gios.freshngreen.responseModel.product.ProductList;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SortDialog extends BottomSheetDialogFragment {

    private SortDialog.Interface mInterface;
    private FragmentProductListBinding binding;
    private String sortVal;

    public SortDialog(SortDialog.Interface mInterface, FragmentProductListBinding binding, String sortVal) {
        this.mInterface = mInterface;
        this.binding = binding;
        this.sortVal = sortVal;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.sort_dialog, null);
        dialog.setContentView(contentView);

        ConstraintLayout priceLowToHighLayout = contentView.findViewById(R.id.priceLowToHighLayout);
        ConstraintLayout priceHighToLowLayout = contentView.findViewById(R.id.priceHighToLowLayout);
        ConstraintLayout nameAToZLayout = contentView.findViewById(R.id.nameAToZLayout);
        ConstraintLayout nameZToALayout = contentView.findViewById(R.id.nameZToALayout);

        RadioButton radioLowToHigh = contentView.findViewById(R.id.radioLowToHigh);
        RadioButton radioHighToLow = contentView.findViewById(R.id.radioHighToLow);
        RadioButton radioAToZ = contentView.findViewById(R.id.radioAToZ);
        RadioButton radioZToA = contentView.findViewById(R.id.radioZToA);

        switch (sortVal){
            case "priceLowToHigh":
                radioLowToHigh.setChecked(true);
                radioHighToLow.setChecked(false);
                radioAToZ.setChecked(false);
                radioZToA.setChecked(false);
                break;
            case "priceHighToLow":
                radioLowToHigh.setChecked(false);
                radioHighToLow.setChecked(true);
                radioAToZ.setChecked(false);
                radioZToA.setChecked(false);
                break;
            case "aToZ":
                radioLowToHigh.setChecked(false);
                radioHighToLow.setChecked(false);
                radioAToZ.setChecked(true);
                radioZToA.setChecked(false);
                break;
            case "zToA":
                radioLowToHigh.setChecked(false);
                radioHighToLow.setChecked(false);
                radioAToZ.setChecked(false);
                radioZToA.setChecked(true);
                break;
        }

        priceLowToHighLayout.setOnClickListener(v -> {
            mInterface.onSortClick(binding,"priceLowToHigh");
            dismiss();
        });

        priceHighToLowLayout.setOnClickListener(v -> {
            mInterface.onSortClick(binding,"priceHighToLow");
            dismiss();
        });

        nameAToZLayout.setOnClickListener(v -> {
            mInterface.onSortClick(binding,"aToZ");
            dismiss();
        });

        nameZToALayout.setOnClickListener(v -> {
            mInterface.onSortClick(binding,"zToA");
            dismiss();
        });
    }

    public interface Interface {
        void onSortClick(FragmentProductListBinding binding, String sortType);
        void onSortCancelClick(FragmentProductListBinding binding);
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
        mInterface.onSortCancelClick(binding);
    }

}