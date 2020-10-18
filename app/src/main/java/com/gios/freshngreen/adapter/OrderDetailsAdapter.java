package com.gios.freshngreen.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gios.freshngreen.R;
import com.gios.freshngreen.responseModel.order.OrderItemList;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    private List<OrderItemList> orderList;
    private Context context;


    public OrderDetailsAdapter(Context context, List<OrderItemList> orderList) {
        this.orderList = orderList;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView sellingPrice;
        private TextView productPrice;
        private TextView discount;
        private TextView unit;
        private TextView quantity;
        private ImageView productImg;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            sellingPrice = itemView.findViewById(R.id.sellingPrice);
            productPrice = itemView.findViewById(R.id.productPrice);
            discount = itemView.findViewById(R.id.discount);
            unit = itemView.findViewById(R.id.unit);
            quantity = itemView.findViewById(R.id.quantity);
            productImg = itemView.findViewById(R.id.productImg);
        }
    }

    @NonNull
    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_item, parent, false);
        return new OrderDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.ViewHolder holder, int position) {
        OrderItemList mOrderList = orderList.get(position);

        try {
            if (mOrderList != null && mOrderList.getProductName() != null) {
                holder.productName.setText(mOrderList.getProductName());
            }


            if (mOrderList != null && mOrderList.getPrice() != null) {
                holder.sellingPrice.setText(String.format("%s%s", context.getResources().getString(R.string.rs), mOrderList.getPrice()));
            }

            if (mOrderList != null && mOrderList.getProductWeight() != null) {
                holder.unit.setText(String.format("%s", mOrderList.getProductWeight()));
            }

            if (mOrderList != null && mOrderList.getQuantity() != null) {
                holder.quantity.setText(String.format("Quantity - %s", mOrderList.getQuantity()));
            }

            if (mOrderList != null && mOrderList.getRetailPrice() != null && !mOrderList.getRetailPrice().isEmpty()) {
                holder.productPrice.setText(String.format("%s%s", context.getResources().getString(R.string.rs), mOrderList.getRetailPrice()));
                holder.productPrice.setPaintFlags(holder.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.productPrice.setText("");
            }

            if (mOrderList != null && mOrderList.getPrice() != null && mOrderList.getRetailPrice() != null && !mOrderList.getRetailPrice().isEmpty()) {
                holder.discount.setText(String.format("%s%% Off", String.valueOf(calculateDiscount((int) Double.parseDouble(mOrderList.getRetailPrice()),
                        (int) Double.parseDouble(mOrderList.getPrice())))));
            } else {
                holder.discount.setText("");
            }

            if(mOrderList.getImage() != null && !mOrderList.getImage().isEmpty()) {
                Picasso.get().load(mOrderList.getImage()).into(holder.productImg);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private int calculateDiscount(float retailPrice, float actualPrice) {
        int discount = 0;
        if(retailPrice > 0) {
            try {

                float res1 = retailPrice / actualPrice;
                float res2 = 100 / res1;
                discount = (int) ((Integer) 100 - res2);
                if(discount <1){
                    discount = 0;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return discount;
    }

}