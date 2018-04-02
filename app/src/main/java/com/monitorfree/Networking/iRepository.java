package com.monitorfree.Networking;


import com.monitorfree.RequestModel.RootLogin;
import com.monitorfree.RequestModel.RootMonitorList;
import com.monitorfree.RequestModel.RootMonitorStatus;
import com.monitorfree.RequestModel.RootRegister;
import com.monitorfree.RequestModel.Status;
import com.monitorfree.UserModel.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;

import retrofit2.http.POST;

/**
 * Created by jassi on 24-08-2016.
 */

public interface iRepository {

    @GET("contacts/")
    Call<User> getContacts();

    @FormUrlEncoded
    @POST("/user/register/")
    Call<RootLogin> signUp(@Field("fullName") String fullName, @Field("password") String password, @Field("email") String email,
                              @Field("key") String key, @Field("sig") String sig, @Field("accountType") String accountType, @Field("externalUserId") String externalUserId, @Field("externalPhotoUrl") String externalPhotoUrl);

    @FormUrlEncoded
    @POST("/user/auth/")
    Call<RootLogin> login(@Field("password") String password, @Field("email") String email,
                          @Field("key") String key, @Field("sig") String sig);

    @FormUrlEncoded
    @POST("/user/delete/")
    Call<Status> accountDelete(@Field("key") String key, @Field("sig") String sig, @Field("hash") String hash);

    @FormUrlEncoded
    @POST("/monitor/add/")
    Call<RootRegister> addMonitor(@Field("startDate") String startDate, @Field("name") String name,
                                  @Field("address") String address, @Field("interval") String interval, @Field("type") String type, @Field("port") String port, @Field("keywords") String keywords,
                                  @Field("mobileDateTime") String mobileDateTime, @Field("sig") String sig, @Field("key") String key, @Field("hash") String hash);

    @FormUrlEncoded
    @POST("/monitor/list/")
    Call<RootMonitorList> getMonitorList(@Field("sig") String sig, @Field("key") String key, @Field("hash") String hash);

    @FormUrlEncoded
    @POST("/user/forgetpass/")
    Call<RootRegister> forgotPassword(@Field("email") String email,
                                      @Field("key") String key, @Field("sig") String sig);

    @FormUrlEncoded
    @POST("/status/add/")
    Call<RootRegister> sendStatus(@Field("sig") String sig, @Field("key") String key, @Field("hash") String hash, @Field("monitorId") String monitorId, @Field("status") String status, @Field("mobileDateTime") String mobileDateTime, @Field("deviceName") String deviceName, @Field("ipAddress") String ipAddress);


    @FormUrlEncoded
    @POST("/status/list/")
    Call<RootMonitorStatus> getStatusList(@Field("sig") String sig, @Field("key") String key, @Field("hash") String hash, @Field("monitorId") String monitorId, @Field("timeFrame") String timeFrame);

    @FormUrlEncoded
    @POST("/status/get/")
    Call<RootMonitorStatus> getMonitorStatus(@Field("sig") String sig, @Field("key") String key, @Field("hash") String hash, @Field("monitorId") String monitorId);

    @FormUrlEncoded
    @POST("/monitor/delete/")
    Call<RootRegister> sendMonitorDelete(@Field("sig") String sig, @Field("key") String key, @Field("hash") String hash, @Field("monitorId") String monitorId);

    @FormUrlEncoded
    @POST("/monitor/pause/")
    Call<RootRegister> sendMonitorPause(@Field("sig") String sig, @Field("key") String key, @Field("hash") String hash, @Field("monitorId") String monitorId);


    @GET("/")
    Call<ResponseBody> checkType1();


//    @POST("api2/completePrfile")
//    Call<User> completeProfile(@Header("Authorization") String authorization, @Body User userLogin);
//
//    @POST("/api2/login")
//    Call<User> loginUser(@Body User userLogin);
//


//    @POST("api2/community")
//    Call<ModelCummunity> addCommunity(@Header("Authorization") String authorization, @Body ModelCummunity modelCummunity);
//
//    @POST("api2/m.facebook")
//    Call<User> facebookLogin(@Body User book);
//
//    @POST("api2/m.google")
//    Call<User> googleLogin(@Body User book);
//
//    @Multipart
//    @POST("api2/upload")
//    Call<User> uploadBook(@Header("Authorization") String authorization, @Part MultipartBody.Part filePart);
//
//    @GET("api2/community")
//    Call<RootModel> search(@Header("Authorization") String authorization, @Query("searchkeyword") String searchkeyword);
//
//    @GET("api2/joinCommunity/{cum_id}")
//    Call<RootModelJoinCommunity> joinCum(@Header("Authorization") String authorization, @Path("cum_id") String cum_id);
//
//    @GET("api2/communityRoom/{cum_id}")
//    Call<RootModel> getRooms(@Header("Authorization") String authorization, @Path("cum_id") String cum_id);
//
//    @POST("api2/communityRoom/{cum_id}")
//    Call<RootModel> createRoom(@Header("Authorization") String authorization, @Body ModelRoom modelRoom, @Path("cum_id") String cum_id);
//
//    @GET("api2/communityMembers/{cum_id}")
//    Call<ModelCommunityMembers> getCommunityMembers(@Header("Authorization") String authorization, @Path("cum_id") String cum_id);
//
//    @POST("api2/addServiceProvider")
//    Call<RootModel> addServiceProvider(@Header("Authorization") String authorization, @Body ModelUserInfo modelProfession);
//
//    @GET("api2/getuserdetail")
//    Call<RootUserInfo> getUserDetail(@Header("Authorization") String authorization);
//
//    @GET("api2/joinRoom/{room_id}")
//    Call<RootModel> joinRoom(@Header("Authorization") String authorization, @Path("room_id") String room_id);
//
//    @GET("/api2/communityPosts/{cum_id}")
//    Call<RootPost> getPosts(@Header("Authorization") String authorization, @Path("cum_id") String room_id);
//
//    @POST("api2/updatePrfile")
//    Call<RootModel> updateProfile(@Header("Authorization") String authorization, @Body ModelUserInfo user);
//
//    @GET("/api2/roomMembers/{room_id}")
//    Call<RootModelGroupMember> getRoomMembers(@Header("Authorization") String authorization, @Path("room_id") String room_id);
//
//    @GET("/api2/myCommunities")
//    Call<RootMyCommunities> getMyCommunities(@Header("Authorization") String authorization);
//
//    @POST("/api2/communityPost/{cum_id}")
//    Call<RootModel> addPost(@Header("Authorization") String authorization, @Body ModelPost modelPost, @Path("cum_id") String cum_id);
//
//
//    @GET("api2/getCommunityProfession/{cum_id}")
//    Call<ModelCommunityMembers> getDirectory(@Header("Authorization") String authorization, @Path("cum_id") String cum_id);
//
//
//    @FormUrlEncoded
//    @POST("api2/getmemberdetail")
//    Call<RootUserInfo> getOtherUserInfo(@Header("Authorization") String authorization, @Field("userId") String userId);
//
//
//    @GET("/api2/getServiceProvider/{cum_id}")
//    Call<ModelCommunityMembers> getServiceProviders(@Header("Authorization") String authorization, @Path("cum_id") String cumId, @Query("profesion") String profesion);
//
//
//    @GET("api2/communityPostLike/{post_id}")
//    Call<RootModel> likePost(@Header("Authorization") String authorization, @Path("post_id") String post_id);
//
//    @GET("api2/communityPostComment/{post_id}")
//    Call<RootComment> getComments(@Header("Authorization") String authorization, @Path("post_id") String post_id);
//
//
//    @FormUrlEncoded
//    @POST("api2/communityPostComment/{post_id}")
//    Call<RootComment> postComment(@Header("Authorization") String authorization, @Path("post_id") String post_id, @Field("comment") String comment);


}