package com.sepehr.sa_sh.abfacounter01.Logic;

import android.content.Context;
import android.util.Log;

import com.sepehr.sa_sh.abfacounter01.Crypto;
import com.sepehr.sa_sh.abfacounter01.IAbfaService;
import com.sepehr.sa_sh.abfacounter01.TokenRequestModel;
import com.sepehr.sa_sh.abfacounter01.TokenResponseModel;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.SimpleErrorHandler;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by _1101 on 4/26/2017.
 */

public class LoginLogic {
    Context mAppContext;
    IToastAndAlertBuilder mToastAndAlertBuilder;

  /*  public LoginLogic(Context appContext,IToastAndAlertBuilder toastAndAlertBuilder) {
        this.mAppContext = appContext;
        this.mToastAndAlertBuilder=toastAndAlertBuilder;
    }

    public TokenResponseModel getToken(final String username, final String password, boolean isLocal){

        IAbfaService abfaService = IAbfaService.retrofit.create(IAbfaService.class);
        final TokenRequestModel tokenRequestModel=new TokenRequestModel(username,password);
        Call<TokenResponseModel> call=abfaService.getToken(tokenRequestModel.getUsername()
                ,tokenRequestModel.getPassword(),
                tokenRequestModel.getGrant_type());
        call.enqueue(new Callback<TokenResponseModel>() {
            @Override
            public void onResponse(Call<TokenResponseModel> call,
                                   retrofit2.Response<TokenResponseModel> response) {
                TokenResponseModel tokenResponseModel  = response.body();
                int responseCode = response.code();
                //
                //region_______________________ response implicit error___________________
                if (responseCode != 200) {
                    tokenResponseModel.setErrorMessage("کد کاربری یا گذر واژه معتبر نمیباشد یا چنین کاربری ثبت نشده است.لطفا دوباره امتحان فرمایید");
                    tokenResponseModel.setSucceeded(false);
                    tokenResponseModelToReturn=tokenResponseModel;
                }
                //endregion
            }

            @Override
            public void onFailure(Call<TokenResponseModel> call, Throwable t) {
                mToastAndAlertBuilder.makeSimpleAlert(SimpleErrorHandler.getErrorMessage(t));
            }
        });
        return
    }*/
}
