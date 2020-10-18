package com.gios.freshngreen.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;

import com.gios.freshngreen.R;
import com.gios.freshngreen.adapter.PriceDialogAdapter;
import com.gios.freshngreen.databinding.AreaDialogBinding;
import com.gios.freshngreen.responseModel.product.PriceDetail;
import com.gios.freshngreen.responseModel.product.ProductDetail;
import com.gios.freshngreen.responseModel.product.ProductList;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class PriceDialogForDetails extends DialogFragment implements PriceDialogAdapter.DialogInterface {
    private AreaDialogBinding binding;
    private Context context;
    private PriceDialogForDetails.SelectPriceInterface mInterface;
    private ProductDetail mProductDetail;

    public PriceDialogForDetails(Context context, PriceDialogForDetails.SelectPriceInterface mInterface, ProductDetail mProductDetail) {
        this.mInterface = mInterface;
        this.mProductDetail = mProductDetail;
        this.context = context;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        binding = AreaDialogBinding.inflate(getLayoutInflater());

        binding.dialogLabel.setText("Select Package");

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        PriceDialogAdapter adapter = new PriceDialogAdapter(context,mProductDetail.getPriceDetails(), this);
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
    public void dialogCallback(int pricePosition, PriceDetail mPriceDetail) {
        mInterface.selectPriceCallback(mPriceDetail);
        dismiss();
    }

    public interface SelectPriceInterface {
        void selectPriceCallback(PriceDetail mPriceDetail);
    }

}