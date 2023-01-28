package com.skyblue.shop.retrofit;

import com.skyblue.shop.model.Registration;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {
    @FormUrlEncoded
    @POST("/api/values/UserRegister")
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
}