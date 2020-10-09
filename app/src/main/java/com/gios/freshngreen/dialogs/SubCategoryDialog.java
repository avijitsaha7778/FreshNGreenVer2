package com.gios.freshngreen.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;

import com.gios.freshngreen.R;
import com.gios.freshngreen.adapter.SubCategoriesDialogAdapter;
import com.gios.freshngreen.databinding.SubCategoryDialogViewBinding;
import com.gios.freshngreen.responseModel.home.Category;
import com.gios.freshngreen.responseModel.home.Subcategory;
import com.gios.freshngreen.utils.SpacesItemDecoration;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

public class SubCategoryDialog extends DialogFragment implements SubCategoriesDialogAdapter.SubCategoriesDialogAdapterInterface {
    private SubCategoryDialogViewBinding binding;
    private SubCategoryDialog.SubCategorySelectionInterface mInterface;
    private List<Subcategory> subCategoryList;


    public SubCategoryDialog(SubCategoryDialog.SubCategorySelectionInterface mInterface, List<Subcategory> subCategoryList) {
        this.mInterface = mInterface;
        this.subCategoryList = subCategoryList;
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


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        binding = SubCategoryDialogViewBinding.inflate(getLayoutInflater());

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.category_grid_spacing);
        binding.recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        SubCategoriesDialogAdapter adapter = new SubCategoriesDialogAdapter(subCategoryList, this);
        binding.recyclerView.setAdapter(adapter);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity());
        dialogBuilder.setView(binding.getRoot());
        AlertDialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setElevation(R.dimen._10sdp);

        // Do all the stuff to initialize your custom view
        return dialog;
    }

    @Override
    public void onSubCategoryClick(Subcategory mSubcategory) {
        mInterface.onSubCategorySelection(mSubcategory);
        dismiss();
    }

    public interface SubCategorySelectionInterface {
        void onSubCategorySelection(Subcategory mCountryModel);
    }
}