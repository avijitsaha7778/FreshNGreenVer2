package com.gios.freshngreen.adapter;

import android.content.Context;

import com.gios.freshngreen.responseModel.home.BannerList;

import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class BannerSliderAdapter extends SliderAdapter {
    private List<BannerList> bannerImgList;

    public BannerSliderAdapter(Context context, List<BannerList> bannerImgList) {
        this.bannerImgList = bannerImgList;
    }

    @Override
    public int getItemCount() {
        return bannerImgList.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        imageSlideViewHolder.bindImageSlide(bannerImgList.get(position).getImage());
    }
}
