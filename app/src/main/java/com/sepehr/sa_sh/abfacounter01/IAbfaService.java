package com.sepehr.sa_sh.abfacounter01;


import com.sepehr.sa_sh.abfacounter01.models.ChangePasswordModel;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.MobileInputModel;
import com.sepehr.sa_sh.abfacounter01.models.SpecialLoadModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.QeireMojazModel;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.Call;

/**
 * Created by sa-sh on 7/23/2016.
 */
public interface IAbfaService {

    @GET("Api/api/CRMobileLoad")
    Call<MobileInputModel> loadData(
            @Header("Authorization") String token,
            @Query("userCode") int userCode,
            @Query("deviceId") String deviceId,
            @Query("currentVersion") int currentVersionCode);

    @POST("Api/api/CRMobileLoad")
    Call<MobileInputModel> reload(
            @Header("Authorization") String token,
            @Query("userCode") int userCode,
            @Query("deviceId") String deviceId,
            @Body Collection<BigDecimal> trackNumbers);

    @POST("Api/api/CRMobileOffLoad")
    Call<Integer> sendCounterReadingInfo(@Header("Authorization") String token,
                                         @Body Output output,
                                         @Query("deviceId") String deviceId,
                                         @Query("userCode") int userCode,
                                         @Query("isOverall") Boolean isOverall,
                                         @Query("trackNumber")BigDecimal trackNumber);

    @POST("AuthApp/Account/ChangePassword")
    Call<String> changePassword(@Header("Authorization") String token,
                                 @Body ChangePasswordModel changePasswordModel);

    @POST("Api/api/CRMobileLoad")
    Call<String> validateMyWorks(@Header("Authorization") String token,
            @Query("userCode") int userCode,
            @Query("deviceId") String deviceId,
            @Query("receivedRecordsCount") int myWorksCount);

    @GET("Api/api/CRSpecialLoad")
    Call<List<SpecialLoadModel>> getMySpecialWorks(@Header("Authorization") String token,
                                             @Query("dbf") String deviceId);

    @POST("Api/api/QeireMojaz")
    Call<String> sendQeireMojaz(@Header("Authorization") String token,
                                @Body QeireMojazModel qeireMojazModel,
                                         @Query("deviceId") String deviceId,
                                         @Query("userCode") int userCode);

   /* @POST("AuthApp/oauth/token")
    Call<String> getToken(@Field("username") TokenRequestModel tokenRequestModel);*/
   @FormUrlEncoded
   @POST("AuthApp/oauth/token")
   Call<TokenResponseModel> getToken(@Field("username") String username,
                                     @Field("password") String password,
                                     @Field("grant_type") String grant_type);

    @GET("Api/api/Apk")
    Call<ResponseBody> updateApp(@Header("Authorization") String token,
                                 @Query("currentVersion") int currentVersion);
}
//

