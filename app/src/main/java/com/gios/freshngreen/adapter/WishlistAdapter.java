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
import com.gios.freshngreen.dialogs.PriceDialog;
import com.gios.freshngreen.dialogs.PriceDialogWishlist;
import com.gios.freshngreen.responseModel.product.PriceDetail;
import com.gios.freshngreen.responseModel.product.ProductList;
import com.gios.freshngreen.responseModel.wishlist.WishlistDetail;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> implements PriceDialogWishlist.SelectPriceInterface {
    private List<WishlistDetail> ProductListList;
    private WishlistAdapter.Interface mInterface;
    private Context context;


    public WishlistAdapter(Context context, List<WishlistDetail> ProductListList, WishlistAdapter.Interface mInterface) {
        this.ProductListList = ProductListList;
        this.mInterface = mInterface;
        this.context = context;
    }

    @Override
    public void selectPriceCallback(List<WishlistDetail> list, PriceDetail mPriceDetail, String productId) {
        ProductListList = list;
        notifyDataSetChanged();
        mInterface.onUpdateWishlist(mPriceDetail, productId);
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
        private ConstraintLayout unitLayout;
        private ImageView productImg;
        private ImageView add;
        private ImageView remove;
        private ImageView dropdownArrow;
        private ImageView deleteIcon;
        private int cartQuantityValue = 0;
        private int priceArrayItemPos = 0;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            retailPrice = itemView.findViewById(R.id.productPrice);
            actualPrice = itemView.findViewById(R.id.sellingPrice);
            discount = itemView.findViewById(R.id.discount);
            unit = itemView.findViewById(R.id.unit);
            addToCart = itemView.findViewById(R.id.addToCart);
            cartQuantityLayout = itemView.findViewById(R.id.cartQuantityLayout);
            unitLayout = itemView.findViewById(R.id.unitLayout);
            cartQuantity = itemView.findViewById(R.id.cartQuantity);
            productImg = itemView.findViewById(R.id.productImg);
            dropdownArrow = itemView.findViewById(R.id.dropdownArrow);
            remove = itemView.findViewById(R.id.remove);
            add = itemView.findViewById(R.id.add);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
            outOfStock = itemView.findViewById(R.id.outOfStock);
        }
    }

    @NonNull
    @Override
    public WishlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_adapter_item, parent, false);
        return new WishlistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.ViewHolder holder, int position) {
        WishlistDetail mProductList = ProductListList.get(position);

        try {
            if (mProductList != null && mProductList.getProductName() != null) {
                holder.productName.setText(mProductList.getProductName());
            }

            for (int i = 0; i < mProductList.getPriceDetails().size(); i++) {
                if (mProductList.getPriceDetails().get(i).isDefaultPrice()) {
                    holder.priceArrayItemPos = i;
                }
            }

            if (mProductList != null && mProductList.getPriceDetails().size() > 1) {
                holder.dropdownArrow.setVisibility(View.VISIBLE);
                holder.unitLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.weight_background));

                if (mProductList.getPriceDetails().get(holder.priceArrayItemPos).getActualPrice() != null) {
                    holder.actualPrice.setText(String.format("%s%s", context.getResources().getString(R.string.rs),
                            mProductList.getPriceDetails().get(holder.priceArrayItemPos).getActualPrice()));
                }

                if (mProductList.getPriceDetails().get(holder.priceArrayItemPos).getWeight() != null) {
//                    holder.unit.setText(String.format("%s/ Unit", mProductList.getPriceDetails().get(holder.priceArrayItemPos).getWeight()));
                    holder.unit.setText(String.format("%s / %s", context.getResources().getString(R.string.rs) + " " + mProductList.getPriceDetails().get(holder.priceArrayItemPos).getActualPrice(),
                            mProductList.getPriceDetails().get(holder.priceArrayItemPos).getWeight()));
                }

                if (mProductList.getPriceDetails().get(holder.priceArrayItemPos).getRetailPrice() != null && !mProductList.getPriceDetails().get(holder.priceArrayItemPos).getRetailPrice().isEmpty()) {
                    holder.retailPrice.setText(String.format("%s%s", context.getResources().getString(R.string.rs),
                            mProductList.getPriceDetails().get(holder.priceArrayItemPos).getRetailPrice()));
                    holder.retailPrice.setPaintFlags(holder.retailPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.retailPrice.setText("");
                }

                if (mProductList.getPriceDetails().get(holder.priceArrayItemPos).getActualPrice() != null &&
                        mProductList.getPriceDetails().get(holder.priceArrayItemPos).getRetailPrice() != null
                        && !mProductList.getPriceDetails().get(holder.priceArrayItemPos).getRetailPrice().isEmpty()) {
                    holder.discount.setText(String.format("%s%% Off",
                            String.valueOf(calculateDiscount((int) Double.parseDouble(mProductList.getPriceDetails().get(holder.priceArrayItemPos).getRetailPrice()),
                                    (int) Double.parseDouble(mProductList.getPriceDetails().get(holder.priceArrayItemPos).getActualPrice())))));
                } else {
                    holder.discount.setText("");
                }
            } else {
                holder.dropdownArrow.setVisibility(View.GONE);
                holder.unitLayout.setBackground(null);

                if (mProductList.getPriceDetails().get(holder.priceArrayItemPos).getActualPrice() != null) {
                    holder.actualPrice.setText(String.format("%s%s", context.getResources().getString(R.string.rs),
                            mProductList.getPriceDetails().get(holder.priceArrayItemPos).getActualPrice()));
                }

                if (mProductList.getPriceDetails().get(holder.priceArrayItemPos).getWeight() != null) {
                    holder.unit.setText(String.format("%s", mProductList.getPriceDetails().get(holder.priceArrayItemPos).getWeight()));
                }

                if (mProductList.getPriceDetails().get(holder.priceArrayItemPos).getRetailPrice() != null && !mProductList.getPriceDetails().get(holder.priceArrayItemPos).getRetailPrice().isEmpty()) {
                    holder.retailPrice.setText(String.format("%s%s", context.getResources().getString(R.string.rs),
                            mProductList.getPriceDetails().get(holder.priceArrayItemPos).getRetailPrice()));
                    holder.retailPrice.setPaintFlags(holder.retailPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.retailPrice.setText("");
                }

                if (mProductList.getPriceDetails().get(holder.priceArrayItemPos).getActualPrice() != null &&
                        mProductList.getPriceDetails().get(holder.priceArrayItemPos).getRetailPrice() != null
                        && !mProductList.getPriceDetails().get(holder.priceArrayItemPos).getRetailPrice().isEmpty()) {
                    holder.discount.setText(String.format("%s%% Off",
                            String.valueOf(calculateDiscount((int) Double.parseDouble(mProductList.getPriceDetails().get(holder.priceArrayItemPos).getRetailPrice()),
                                    (int) Double.parseDouble(mProductList.getPriceDetails().get(holder.priceArrayItemPos).getActualPrice())))));
                } else {
                    holder.discount.setText("");
                }
            }

            if (mProductList != null && mProductList.getQuantity() != null && Integer.parseInt(mProductList.getQuantity()) == 0) {
                holder.outOfStock.setVisibility(View.VISIBLE);
                holder.addToCart.setVisibility(View.GONE);
                holder.cartQuantityLayout.setVisibility(View.GONE);
            } else {
                if (mProductList != null && mProductList.getItemAddToCart() != null && Integer.parseInt(mProductList.getItemAddToCart()) >= 1) {
                    holder.cartQuantityLayout.setVisibility(View.VISIBLE);
                    holder.outOfStock.setVisibility(View.GONE);
                    holder.addToCart.setVisibility(View.GONE);
                    holder.cartQuantityValue = Integer.parseInt(mProductList.getItemAddToCart());
                    holder.cartQuantity.setText(String.valueOf(holder.cartQuantityValue));
                } else {
                    holder.cartQuantityLayout.setVisibility(View.GONE);
                    holder.outOfStock.setVisibility(View.GONE);
                    holder.addToCart.setVisibility(View.VISIBLE);
                }
            }

            if (mProductList.getImage() != null && !mProductList.getImage().isEmpty()) {
                Picasso.get().load(mProductList.getImage()).into(holder.productImg);
            }



            holder.unitLayout.setOnClickListener(v -> {
                if (mProductList.getPriceDetails().size() > 1) {
                    PriceDialogWishlist mPriceDialog = new PriceDialogWishlist(context, this, ProductListList, position);
                    mPriceDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), TAG);
                }
            });

            holder.addToCart.setOnClickListener(v -> {
                mInterface.onMoveToCart(mProductList);
                /*holder.cartQuantityLayout.setVisibility(View.VISIBLE);
                holder.addToCart.setVisibility(View.GONE);
                holder.cartQuantityValue ++;
                holder.cartQuantity.setText(String.valueOf(holder.cartQuantityValue ));*/
            });

            holder.add.setOnClickListener(v -> {
                if (holder.cartQuantityValue < Integer.parseInt(mProductList.getQuantity())) {
                    holder.cartQuantityValue++;
                    holder.cartQuantity.setText(String.valueOf(holder.cartQuantityValue));
                    mInterface.onUpdateCart(mProductList, String.valueOf(holder.cartQuantityValue));
                    mProductList.setItemAddToCart(String.valueOf(holder.cartQuantityValue));
                }
            });

            holder.remove.setOnClickListener(v -> {
                if (holder.cartQuantityValue > 1) {
                    holder.cartQuantityValue--;
                    holder.cartQuantity.setText(String.valueOf(holder.cartQuantityValue));
                    mInterface.onUpdateCart(mProductList, String.valueOf(holder.cartQuantityValue));
                    mProductList.setItemAddToCart(String.valueOf(holder.cartQuantityValue));
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

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ProductListList.size();
    }

    public interface Interface {
        void onProductListClick(WishlistDetail mWishlistDetail);

        void onRemoveWishlist(WishlistDetail mWishlistDetail);

        void onUpdateWishlist(PriceDetail mPriceDetail, String productId);

        void onMoveToCart(WishlistDetail mWishlistDetail);

        void onUpdateCart(WishlistDetail mWishlistDetail, String itemQuantity);
    }

    private int calculateDiscount(float retailPrice, float actualPrice) {
        int discount = 0;
        if (retailPrice > 0) {
            try {

                float res1 = retailPrice / actualPrice;
                float res2 = 100 / res1;
                discount = (int) ((Integer) 100 - res2);
                if (discount < 1) {
                    discount = 0;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return discount;
    }
}