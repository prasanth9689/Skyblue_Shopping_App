package com.skyblue.shop.retrofit;

import com.skyblue.shop.model.OrderCancel;
import com.skyblue.shop.model.Login;
import com.skyblue.shop.model.Registration;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {
    @FormUrlEncoded
    @POST("/prasanth/online_shopping/register.php")
    Call<Registration> createNewRegister(@Field("name") String name,
                                         @Field("mobile") String mobile ,
                                         @Field("password") String password,
                                         @Field("mobile_number_only") String mobile_number_only,
                                         @Field("area") String area,
                                         @Field("landmark") String landmark,
                                         @Field("pin_code") String pin_code,
                                         @Field("city") String city,
                                         @Field("state") String state,
                                         @Field("date") String date,
                                         @Field("time") String time,
                                         @Field("time_zone") String time_zone,
                                         @Field("date_time_zone") String date_time_zone,
                                         @Field("token") String token);

    @FormUrlEncoded
    @POST("/prasanth/online_shopping/login.php")
    Call<Login> login(@Field("mobile") String name,
                     @Field("password") String password,
                     @Field("token") String token);

    @FormUrlEncoded
    @POST("/prasanth/online_shopping/cancel_order.php")
    Call<OrderCancel> cancelOrder(@Field("product_id") String product_id,
                                  @Field("user_id") String user_id,
                                  @Field("canceled_date") String canceled_date,
                                  @Field("canceled_time") String canceled_time,
                                  @Field("canceled_time_2") String canceled_time_2);
}