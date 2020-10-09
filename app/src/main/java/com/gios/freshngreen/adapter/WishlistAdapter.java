package com.gios.freshngreen.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gios.freshngreen.R;
import com.gios.freshngreen.responseModel.wishlist.WishlistDetail;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder>{
    private List<WishlistDetail> ProductListList;
    private WishlistAdapter.Interface mInterface;
    private Context context;



    public WishlistAdapter(Context context, List<WishlistDetail> ProductListList, WishlistAdapter.Interface mInterface) {
        this.ProductListList = ProductListList;
        this. mInterface = mInterface;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView retailPrice;
        private TextView actualPrice;
        private TextView discount;
        private TextView unit;
        private TextView cartQuantity;
        private TextView outOfStock;
        private Button addToCart;
        private ConstraintLayout cartQuantityLayout;
        private ImageView productImg;
        private ImageView add;
        private ImageView remove;
        private ImageView deleteIcon;
        private int cartQuantityValue = 0;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            retailPrice = itemView.findViewById(R.id.productPrice);
            actualPrice = itemView.findViewById(R.id.sellingPrice);
            discount = itemView.findViewById(R.id.discount);
            unit = itemView.findViewById(R.id.unit);
            addToCart = itemView.findViewById(R.id.addToCart);
            cartQuantityLayout = itemView.findViewById(R.id.cartQuantityLayout);
            cartQuantity = itemView.findViewById(R.id.cartQuantity);
            productImg = itemView.findViewById(R.id.productImg);
            remove = itemView.findViewById(R.id.remove);
            add = itemView.findViewById(R.id.add);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
            outOfStock = itemView.findViewById(R.id.outOfStock);
        }
    }

    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_adapter_item,parent,false);
        return new WishlistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {
        WishlistDetail mProductList = ProductListList.get(position);

        try {
            if(mProductList != null && mProductList.getProductName() != null) {
                holder.productName.setText(mProductList.getProductName());
            }

            if(mProductList != null && mProductList.getActualPrice() != null) {
                holder.actualPrice.setText(String.format("%s%s", context.getResources().getString(R.string.rs), mProductList.getActualPrice()));
            }

            if(mProductList != null && mProductList.getWeight() != null) {
                holder.unit.setText(String.format("%s/ Unit", mProductList.getWeight()));
            }

            if(mProductList != null && mProductList.getRetailPrice() != null && !mProductList.getRetailPrice().isEmpty()) {
                holder.retailPrice.setText(String.format("%s%s", context.getResources().getString(R.string.rs), mProductList.getRetailPrice()));
                holder.retailPrice.setPaintFlags(holder.retailPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                holder.retailPrice.setText("");
            }

            if(mProductList != null && mProductList.getActualPrice() != null && mProductList.getRetailPrice() != null && !mProductList.getRetailPrice().isEmpty()) {
                holder.discount.setText(String.format("%s%% Off", String.valueOf(calculateDiscount((int) Double.parseDouble(mProductList.getRetailPrice()),
                        (int) Double.parseDouble(mProductList.getActualPrice())))));
            }else {
                holder.discount.setText("");
            }

            if(mProductList != null && mProductList.getQuantity() != null && Integer.parseInt(mProductList.getQuantity()) == 0) {
                holder.outOfStock.setVisibility(View.VISIBLE);
                holder.addToCart.setVisibility(View.GONE);
            }else {
                if(mProductList != null && mProductList.getItemAddToCart() != null && Integer.parseInt(mProductList.getItemAddToCart()) >= 1) {
                    holder.cartQuantityLayout.setVisibility(View.VISIBLE);
                    holder.addToCart.setVisibility(View.GONE);
                    holder.cartQuantityValue = Integer.parseInt(mProductList.getItemAddToCart());
                    holder.cartQuantity.setText(String.valueOf(holder.cartQuantityValue ));
                }else {
                    holder.cartQuantityLayout.setVisibility(View.GONE);
                    holder.outOfStock.setVisibility(View.GONE);
                    holder.addToCart.setVisibility(View.VISIBLE);
                }
            }

            if(mProductList.getImage() != null && !mProductList.getImage().isEmpty()) {
                Picasso.get().load(mProductList.getImage()).into(holder.productImg);
            }

            holder.addToCart.setOnClickListener(v -> {
                mInterface.onMoveToCart(mProductList);
                /*holder.cartQuantityLayout.setVisibility(View.VISIBLE);
                holder.addToCart.setVisibility(View.GONE);
                holder.cartQuantityValue ++;
                holder.cartQuantity.setText(String.valueOf(holder.cartQuantityValue ));*/
            });

            holder.add.setOnClickListener(v -> {
                if(holder.cartQuantityValue < Integer.parseInt(mProductList.getQuantity())) {
                    holder.cartQuantityValue++;
                    holder.cartQuantity.setText(String.valueOf(holder.cartQuantityValue));
                    mInterface.onUpdateCart(mProductList,String.valueOf(holder.cartQuantityValue));
                }
            });

            holder.remove.setOnClickListener(v -> {
                if (holder.cartQuantityValue > 1) {
                    holder.cartQuantityValue--;
                    holder.cartQuantity.setText(String.valueOf(holder.cartQuantityValue));
                    mInterface.onUpdateCart(mProductList,String.valueOf(holder.cartQuantityValue));
                }
               /* if(holder.cartQuantityValue <= 1){
                    holder.cartQuantityLayout.setVisibility(View.GONE);
                    holder.addToCart.setVisibility(View.VISIBLE);
                    holder.cartQuantityValue = 0;
                }else {
                    holder.cartQuantityValue--;
                    holder.cartQuantity.setText(String.valueOf(holder.cartQuantityValue));
                }*/
            });

            holder.deleteIcon.setOnClickListener(v -> {
                mInterface.onRemoveWishlist(mProductList);
            });


            holder.itemView.setOnClickListener(v -> {
                mInterface.onProductListClick(mProductList);
            });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ProductListList.size();
    }

    public interface Interface{
        void onProductListClick(WishlistDetail mWishlistDetail);
        void onRemoveWishlist(WishlistDetail mWishlistDetail);
        void onMoveToCart(WishlistDetail mWishlistDetail);
        void onUpdateCart(WishlistDetail mWishlistDetail ,String itemQuantity);
    }

    private int calculateDiscount(float retailPrice, float actualPrice){
        int discount = 0;
        try{

            float  res1 = retailPrice / actualPrice;
            float  res2 = 100/res1;
            discount = (int) ((Integer)100-res2);

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return discount;
    }
}