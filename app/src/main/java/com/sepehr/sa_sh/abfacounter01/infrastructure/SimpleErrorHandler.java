package com.sepehr.sa_sh.abfacounter01.infrastructure;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;

/**
 * Created by saeid on 3/12/2017.
 */
public class SimpleErrorHandler {
    static String errorMessage;
    private SimpleErrorHandler() {
    }

    public static String getErrorMessage(Throwable throwable){
        if(throwable instanceof IOException){
            errorMessage="خطای دریافت/ارسال اطلاعات به/از سرور";
        }
        else if(throwable instanceof SocketTimeoutException){
            errorMessage="خطا حین اتصال به سرور ، لطفا دوباره امتحان نمایید و در صورت تکر" +
                        "ار تنظیمات شبکه خود را بررسی فرمایید";
        }
        else if(throwable instanceof InterruptedIOException){
            errorMessage="قطع شدن اتصال به سرور حین ارسال/ دریافت اطلاعات";
        }
        else {
            errorMessage="جهت اشکال یابی بهتر"+" "+ throwable.getMessage();
        }
        return errorMessage;
    }

    public static String getErrorMessage(int httpResponseCode){
        String errorMessage="";
        if(httpResponseCode==500){
            errorMessage="خطای سرور";
        }
        else if(httpResponseCode==400){
            errorMessage="در حال حاضر اطلاعات بارگیری جدیدی برای شما وجود ندارد";
        }
        else if(httpResponseCode==404){
            errorMessage="خطای آدرس";
        }
        else if(httpResponseCode==401){
            errorMessage="از ثبت شدن دستگاه در سامانه قرائت اطمینان حاصل کنید ";
        }
        else if(httpResponseCode==405){
            errorMessage="همکار گرامی لطفا نسخه به روز شده اپلیکیشن را از منوی تنظیمات  بخش به روز رسانی دریافت نمایید";
        }
        else if(httpResponseCode==406){
            errorMessage="همکار گرامی لطفا چهت دریافت آخرین نسخه اپلیکیشن با راهبر تماس حاصل فرمایید";
        }
        return  errorMessage;
    }
}
