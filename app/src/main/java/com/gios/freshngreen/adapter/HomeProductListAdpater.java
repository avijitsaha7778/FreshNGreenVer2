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
import com.gios.freshngreen.responseModel.product.ProductList;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class HomeProductListAdpater extends RecyclerView.Adapter<HomeProductListAdpater.ViewHolder>{
    private List<ProductList> ProductListList;
    private HomeProductListAdpater.Interface mInterface;
    private Context context;
    private Button viewAllBtn;
    private String categoryId;



    public HomeProductListAdpater(Context context, List<ProductList> ProductListList, HomeProductListAdpater.Interface mInterface,Button viewAllBtn,
                                  String categoryId) {
        this.ProductListList = ProductListList;
        this. mInterface = mInterface;
        this.context = context;
        this.viewAllBtn = viewAllBtn;
        this.categoryId = categoryId;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView retailPrice;
        private TextView actualPrice;
        private TextView discount;
        private TextView unit;
        private TextView outOfStock;
        private TextView cartQuantity;
        private Button addToCart;
        private ConstraintLayout cartQuantityLayout;
        private ImageView productImg;
        private ImageView add;
        private ImageView remove;
        private SparkButton wishListIcon;
        private int cartQuantityValue = 0;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            retailPrice = itemView.findViewById(R.id.productPrice);
            actualPrice = itemView.findViewById(R.id.sellingPrice);
            discount = itemView.findViewById(R.id.discount);
            unit = itemView.findViewById(R.id.unit);
            outOfStock = itemView.findViewById(R.id.outOfStock);
            addToCart = itemView.findViewById(R.id.addToCart);
            cartQuantityLayout = itemView.findViewById(R.id.cartQuantityLayout);
            cartQuantity = itemView.findViewById(R.id.cartQuantity);
            productImg = itemView.findViewById(R.id.productImg);
            remove = itemView.findViewById(R.id.remove);
            add = itemView.findViewById(R.id.add);
            wishListIcon = itemView.findViewById(R.id.wishListIcon);

        }
    }

    @NonNull
    @Override
    public HomeProductListAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_adapter_item,parent,false);
        return new HomeProductListAdpater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeProductListAdpater.ViewHolder holder, int position) {
        ProductList mProductList = ProductListList.get(position);

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

            if(mProductList != null && mProductList.getWishlistFlag() != null && mProductList.getWishlistFlag()) {
                holder.wishListIcon.setChecked(true);
            }else {
                holder.wishListIcon.setChecked(false);
            }



            if(mProductList.getImage() != null && !mProductList.getImage().isEmpty()) {
                Picasso.get().load(mProductList.getImage()).into(holder.productImg);
            }

            holder.addToCart.setOnClickListener(v -> {
                holder.cartQuantityLayout.setVisibility(View.VISIBLE);
                holder.addToCart.setVisibility(View.GONE);
                holder.cartQuantityValue ++;
                holder.cartQuantity.setText(String.valueOf(holder.cartQuantityValue ));
                mInterface.onAddToCart(mProductList);
            });

            holder.add.setOnClickListener(v -> {
                if(holder.cartQuantityValue < Integer.parseInt(mProductList.getQuantity())) {
                    holder.cartQuantityValue++;
                    holder.cartQuantity.setText(String.valueOf(holder.cartQuantityValue));
                    mInterface.onUpdateCart(mProductList,String.valueOf(holder.cartQuantityValue));
                }
            });

            holder.remove.setOnClickListener(v -> {
                if(holder.cartQuantityValue <= 1){
                    holder.cartQuantityLayout.setVisibility(View.GONE);
                    holder.addToCart.setVisibility(View.VISIBLE);
                    holder.cartQuantityValue = 0;
                }else {
                    holder.cartQuantityValue--;
                    holder.cartQuantity.setText(String.valueOf(holder.cartQuantityValue));
                }
                mInterface.onUpdateCart(mProductList,String.valueOf(holder.cartQuantityValue));
            });

            holder.wishListIcon.setEventListener(new SparkEventListener(){
                @Override
                public void onEvent(ImageView button, boolean buttonState) {
                    if (buttonState) {
                        // Button is active
                        mInterface.onAddWishlist(mProductList);
                    } else {
                        // Button is inactive
                        mInterface.onRemoveWishlist(mProductList);
                    }
                }
                @Override
                public void onEventAnimationEnd(ImageView button, boolean buttonState) {
                }
                @Override
                public void onEventAnimationStart(ImageView button, boolean buttonState) {
                }
            });

            holder.itemView.setOnClickListener(v -> {
                mInterface.onProductListClick(mProductList);
            });

            viewAllBtn.setOnClickListener(v -> {
                mInterface.onViewAllClick(categoryId);
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
        void onProductListClick(ProductList mProductList);
        void onViewAllClick(String categoryId);
        void onAddWishlist(ProductList mProductList);
        void onRemoveWishlist(ProductList mProductList);
        void onAddToCart(ProductList mProductList);
        void onUpdateCart(ProductList mProductList ,String itemQuantity);
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