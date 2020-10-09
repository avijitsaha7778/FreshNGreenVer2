package com.gios.freshngreen.interfaces;

import com.gios.freshngreen.responseModel.cart.AddCartModel;
import com.gios.freshngreen.responseModel.cart.GetCartModel;
import com.gios.freshngreen.responseModel.cart.RemoveCartModel;
import com.gios.freshngreen.responseModel.contactus.ContactUsModel;
import com.gios.freshngreen.responseModel.home.BannerModel;
import com.gios.freshngreen.responseModel.home.CategoriesModel;
import com.gios.freshngreen.responseModel.home.HomeProductListModel;
import com.gios.freshngreen.responseModel.home.SearchModel;
import com.gios.freshngreen.responseModel.login.ForgotPasswordModel;
import com.gios.freshngreen.responseModel.login.LoginModel;
import com.gios.freshngreen.responseModel.login.ResendOtpModel;
import com.gios.freshngreen.responseModel.login.SignupModel;
import com.gios.freshngreen.responseModel.login.VerifyOtpForgotPasswordModel;
import com.gios.freshngreen.responseModel.login.VerifyOtpModel;
import com.gios.freshngreen.responseModel.login.VerifyPasswordModel;
import com.gios.freshngreen.responseModel.order.OrderHistoryModel;
import com.gios.freshngreen.responseModel.order.PlaceOrderModel;
import com.gios.freshngreen.responseModel.order.UpdateAddressModel;
import com.gios.freshngreen.responseModel.profile.GetAreaListModel;
import com.gios.freshngreen.responseModel.wishlist.AddWishlistModel;
import com.gios.freshngreen.responseModel.product.ProductDetailsModel;
import com.gios.freshngreen.responseModel.product.ProductModel;
import com.gios.freshngreen.responseModel.product.RelatedProductModel;
import com.gios.freshngreen.responseModel.profile.GetProfileModel;
import com.gios.freshngreen.responseModel.profile.UpdateProfileModel;
import com.gios.freshngreen.responseModel.wishlist.GetWishlistModel;
import com.gios.freshngreen.responseModel.wishlist.RemoveWishlistModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

import static com.gios.freshngreen.utils.Urls.URL_ADD_CART;
import static com.gios.freshngreen.utils.Urls.URL_ADD_WISHLIST;
import static com.gios.freshngreen.utils.Urls.URL_AREA_LIST;
import static com.gios.freshngreen.utils.Urls.URL_BANNER;
import static com.gios.freshngreen.utils.Urls.URL_CATEGORIES;
import static com.gios.freshngreen.utils.Urls.URL_CHANGE_PASSWORD;
import static com.gios.freshngreen.utils.Urls.URL_CONTACT_US;
import static com.gios.freshngreen.utils.Urls.URL_CREATE_CUSTOMER;
import static com.gios.freshngreen.utils.Urls.URL_FORGOT_PASSWORD;
import static com.gios.freshngreen.utils.Urls.URL_GET_CART;
import static com.gios.freshngreen.utils.Urls.URL_GET_PROFILE;
import static com.gios.freshngreen.utils.Urls.URL_GET_WISHLIST;
import static com.gios.freshngreen.utils.Urls.URL_HOME_PRODUCT_LIST;
import static com.gios.freshngreen.utils.Urls.URL_LOGIN;
import static com.gios.freshngreen.utils.Urls.URL_ORDER_HISTORY;
import static com.gios.freshngreen.utils.Urls.URL_PLACE_ORDER;
import static com.gios.freshngreen.utils.Urls.URL_PRODUCT_DETAILS;
import static com.gios.freshngreen.utils.Urls.URL_PRODUCT_LIST;
import static com.gios.freshngreen.utils.Urls.URL_PRODUCT_SEARCH;
import static com.gios.freshngreen.utils.Urls.URL_RELATED_PRODUCT;
import static com.gios.freshngreen.utils.Urls.URL_REMOVE_CART;
import static com.gios.freshngreen.utils.Urls.URL_REMOVE_WISHLIST;
import static com.gios.freshngreen.utils.Urls.URL_RESEND_OTP;
import static com.gios.freshngreen.utils.Urls.URL_UPDATE_ADDRESS;
import static com.gios.freshngreen.utils.Urls.URL_UPDATE_PROFILE;
import static com.gios.freshngreen.utils.Urls.URL_VERIFY_OTP;
import static com.gios.freshngreen.utils.Urls.URL_VERIFY_OTP_CHANGE_PASSWORD;

public interface ApiInterface {


    @Multipart
    @POST(URL_CREATE_CUSTOMER)
    Call<SignupModel> signUp(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_VERIFY_OTP)
    Call<VerifyOtpModel> verifyOtp(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_CHANGE_PASSWORD)
    Call<VerifyPasswordModel> changePassword(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_RESEND_OTP)
    Call<ResendOtpModel> resendOtp(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_LOGIN)
    Call<LoginModel> login(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_FORGOT_PASSWORD)
    Call<ForgotPasswordModel> forgotPassword(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_VERIFY_OTP_CHANGE_PASSWORD)
    Call<VerifyOtpForgotPasswordModel> forgotPasswordVerifyOtp(@PartMap Map<String, RequestBody> partMap);


    @Multipart
    @POST(URL_CATEGORIES)
    Call<CategoriesModel> categories(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_BANNER)
    Call<BannerModel> banner(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_PRODUCT_LIST)
    Call<ProductModel> productList(@PartMap Map<String, RequestBody> partMap);


    @Multipart
    @POST(URL_HOME_PRODUCT_LIST)
    Call<HomeProductListModel> homeProductList(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_PRODUCT_DETAILS)
    Call<ProductDetailsModel> productDetails(@PartMap Map<String, RequestBody> partMap);


    @Multipart
    @POST(URL_RELATED_PRODUCT)
    Call<RelatedProductModel> relatedProduct(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_PRODUCT_SEARCH)
    Call<SearchModel> searchProduct(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_GET_PROFILE)
    Call<GetProfileModel> getProfileDetails(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_UPDATE_PROFILE)
    Call<UpdateProfileModel> updateProfile(@PartMap Map<String, RequestBody> partMap,
     @Part MultipartBody.Part file);

    @Multipart
    @POST(URL_ADD_WISHLIST)
    Call<AddWishlistModel> addWistlist(@PartMap Map<String, RequestBody> partMap);


    @Multipart
    @POST(URL_REMOVE_WISHLIST)
    Call<RemoveWishlistModel> removeWistlist(@PartMap Map<String, RequestBody> partMap);


    @Multipart
    @POST(URL_GET_WISHLIST)
    Call<GetWishlistModel> getWistlist(@PartMap Map<String, RequestBody> partMap);


    @Multipart
    @POST(URL_ADD_CART)
    Call<AddCartModel> addToCart(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_GET_CART)
    Call<GetCartModel> getCart(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_REMOVE_CART)
    Call<RemoveCartModel> removeCart(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_ORDER_HISTORY)
    Call<OrderHistoryModel> orderHistory(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_CONTACT_US)
    Call<ContactUsModel> contactUs(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_AREA_LIST)
    Call<GetAreaListModel> areaList(@PartMap Map<String, RequestBody> partMap);


    @Multipart
    @POST(URL_UPDATE_ADDRESS)
    Call<UpdateAddressModel> updateAddress(@PartMap Map<String, RequestBody> partMap);

    @Multipart
    @POST(URL_PLACE_ORDER)
    Call<PlaceOrderModel> placeOrder(@PartMap Map<String, RequestBody> partMap);


}
