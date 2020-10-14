package com.gios.freshngreen.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gios.freshngreen.R;
import com.gios.freshngreen.responseModel.order.OrderList;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private List<OrderList> orderList;
    private OrderHistoryAdapter.Interface mInterface;
    private Context context;


    public OrderHistoryAdapter(Context context, List<OrderList> orderList, OrderHistoryAdapter.Interface mInterface) {
        this.orderList = orderList;
        this.mInterface = mInterface;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout item;
        private TextView orderId;
        private TextView date;
        private TextView time;
        private TextView amount;
        private TextView paymentMode;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            orderId = itemView.findViewById(R.id.orderIdLabel);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            amount = itemView.findViewById(R.id.amount);
            paymentMode = itemView.findViewById(R.id.paymentMode);
        }
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item, parent, false);
        return new OrderHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, int position) {
        OrderList mOrderList = orderList.get(position);

        try {
            if (mOrderList != null && mOrderList.getOrderId() != null) {
                holder.orderId.setText(mOrderList.getOrderId());
            }

            if (mOrderList != null && mOrderList.getOrderTime() != null) {
                holder.date.setText(formatDate(mOrderList.getOrderTime(),"yyyy-MM-dd hh:mm:ss","EEEE, dd MMMM yyyy"));  //2020-10-08 09:10:30"
            }

            if (mOrderList != null && mOrderList.getOrderTime() != null) {
                holder.time.setText(formatDate(mOrderList.getOrderTime(),"yyyy-MM-dd hh:mm:ss","hh:mm a"));
            }

            if (mOrderList != null && mOrderList.getOrderTime() != null) {
                holder.amount.setText(String.format("%s %s", context.getResources().getString(R.string.rs), mOrderList.getNettotal()));
            }

            if (mOrderList != null && mOrderList.getPaymentMethod() != null) {
                holder.paymentMode.setText(mOrderList.getPaymentMethod());
            }

            holder.item.setOnClickListener(v -> {
                mInterface.onClickItem(mOrderList);
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public interface Interface {
        void onClickItem(OrderList mOrderList);
    }

    private String formatDate(String date, String sInputFormat, String sOutputFormat) {
        Date newDate = null;
        String returnDate = "";
        SimpleDateFormat inputFormat = new SimpleDateFormat(sInputFormat);
        SimpleDateFormat outputFormat = new SimpleDateFormat(sOutputFormat);
        try {
            newDate = inputFormat.parse(date);
            returnDate = outputFormat.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

}