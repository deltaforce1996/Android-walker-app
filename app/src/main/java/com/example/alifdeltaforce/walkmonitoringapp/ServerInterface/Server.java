package com.example.alifdeltaforce.walkmonitoringapp.ServerInterface;

import com.example.alifdeltaforce.walkmonitoringapp.Model.Complate;
import com.example.alifdeltaforce.walkmonitoringapp.Model.Level;
import com.example.alifdeltaforce.walkmonitoringapp.Model.Report;
import com.example.alifdeltaforce.walkmonitoringapp.Model.Templatm;
import com.example.alifdeltaforce.walkmonitoringapp.Model.User;



import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Alif Delta Force on 7/4/2018.
 */

public interface Server {

    @FormUrlEncoded
    @POST("new_userRegister.php")
    Call<User> create_new_User(@Field("userName") String userName,
                               @Field("userEmail") String userEmail,
                               @Field("userPass") String userPass,
                               @Field("userAge") Integer userAge,
                               @Field("userGender") String userGender
    );

    @FormUrlEncoded
    @POST("user_Login.php")
    Call<User> check_login(@Field("userEmail") String userEmail,
                           @Field("userPass") String userPass
    );

    @GET("auto_Login.php")
    Call<User>GetUserAutoLogin();

    @GET("user_detail.php")
    Call<User>GetUserDataDetail(@Query("userEmail") String userEmail);

    @FormUrlEncoded
    @POST("user_detail_fix.php")
    Call<User>GetUserDataDetail_fix(@Field("userEmail") String userEmail);

    @FormUrlEncoded
    @POST("delete_user.php")
    Call<User>deleteUser(@Field("email_user") String userEmail);

    @FormUrlEncoded
    @POST("user_edit.php")
    Call<User>EditUser(@Field("eName_user") String name,
                       @Field("e_Age") Integer age,
                       @Field("e_picc") String uri_txt,
                       @Field("userEmail") String email
    );

    @FormUrlEncoded
    @POST("GetDetailDataUser.php")
    Call<Level> GETDATAUSERFORFSET(@Field("email") String txt);

    @GET("TemplatemLevel.php")
    Call<Templatm> getTemPlatm();

    @FormUrlEncoded
    @POST("UpdateLevel.php")
    Call<Complate> UpdateLevel(@Field("LEVEL") int txt,
                               @Field("EMAIL") String email,
                               @Field("STEP") int step,
                               @Field("TIME") int tim,
                               @Field("DISTANCE") int dis);

    @FormUrlEncoded
    @POST("GetReportGraph.php")
    Call<Report> GetPePort(@Field("email") String email);

    @FormUrlEncoded
    @POST("UpdateStep.php")
    Call<Complate> RealTimeUpdate(@Field("LEVEL") int txt,
                                  @Field("EMAIL") String email,
                                  @Field("STEP") int step,
                                  @Field("TIME") int tim,
                                  @Field("DISTANCE") int dis);

}
