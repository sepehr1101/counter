package com.sepehr.sa_sh.abfacounter01;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.sepehr.sa_sh.abfacounter01.constants.CompanyNames;

/**
 * Created by saeid on 2/16/2017.
 */
public class DifferentCompanyManager {
    private DifferentCompanyManager() {
    }
    //
    public static CompanyNames getCompanyNameEnum(int companyCode){
        switch (companyCode){
            case 1:
               return CompanyNames.ZONE1;
            case 2:
                return CompanyNames.ZONE2;
            case 3:
                return CompanyNames.ZONE3;
            case 4:
                return CompanyNames.ZONE4;
            case 5:
                return CompanyNames.ZONE5;
            case 6:
                return CompanyNames.ZONE6;
            case 7:
                return CompanyNames.TSW;
            case 8:
                return CompanyNames.TE;
            case 9:
                return CompanyNames.TSE;
            case 10:
                return CompanyNames.TOWNS_WEST;
            case 11:
                return CompanyNames.ESF;
            default:
                throw new UnsupportedOperationException();

        }
    }
    //
    public static String getCompanyName(CompanyNames companyName){
        switch (companyName){
            case ZONE1:
                return "آبقا منطقه یک";
            case ZONE2:
                return "آبفا منطقه2";
            case ZONE3:
                return "آبفا منطقه سه";
            case ZONE4:
                return "آبفا منطقه چهار";
            case ZONE5:
                return "آبفامنطقه پنج";
            case ZONE6:
                return "آبقا منطقه شش";
            case TE:
                return "آبفاشرق";
            case TSW:
                return "آبفا جنوب غربی";
            case TSE:
                return "آبفا جنوب شرقی";
            case TOWNS_WEST:
                return "آبفا شهرک های غرب";
            case  ESF:
                return "آبفا استان اصفهان";
            default:
                throw new UnsupportedOperationException();
        }
    }
    //
    public static String getBaseUrl(CompanyNames companyNames){
        switch (companyNames){
            case ZONE1:
                return "http://217.146.220.33:50011/";
            case ZONE2:
                return "http://212.16.75.194:8080/";
            case ZONE3:
                return "http://212.16.69.36:90/";
            case ZONE4:
                return "http://91.98.248.36:8081/";
            case ZONE5:
                return "http://80.69.252.151/";
            case ZONE6:
                return "http://85.133.190.220:4121/";
            case TSW:
                return "http://81.90.148.25/";
            case TE:
                return "http://185.12.60.135/";
            case TSE:
                return "http://5.160.85.228:9098/";
            case TOWNS_WEST:
                return "http://217.66.195.75/";
            case ESF:
                return "http://172.18.12.122/";
            default:
                throw new UnsupportedOperationException();
        }
    }
    //
    public static String getLocalBaseUrl(CompanyNames companyNames){
        switch (companyNames){
            case ZONE1:
                return "http://172.21.0.16/";
            case ZONE2:
                return "http://172.22.4.71/";
            case ZONE3:
                return "http://172.23.0.113/";
            case ZONE4:
                return "http://172.24.13.23/";
            case ZONE5:
                return  "http://172.25.0.72/";
            case ZONE6:
                return "http://172.26.0.32/";
            case TSW:
                return "http://81.90.148.25/";
            case TE:
                return "http://172.31.0.25/";
            case TSE:
                return "http://172.28.5.40/";
            case TOWNS_WEST:
                return "http://172.28.5.40/";
            case ESF:
                return "http://172.18.12.122/";
            default:
                throw new UnsupportedOperationException();
        }
    }
    //
    public static String getEmailTail(CompanyNames companyNames){
        switch (companyNames){
            case ZONE1:
                return "@zone1.ir";
            case ZONE2:
                return "@zone2.ir";
            case ZONE3:
                return "@zone3.ir";
            case ZONE4:
                return "@zone4.ir";
            case ZONE5:
                return "@zone5.ir";
            case ZONE6:
                return "@zone6.ir";
            case TSW:
                return "@swt.ir";
            case TE:
                return "@te.ir";
            case TSE:
                return "@tse.ir";
            case TOWNS_WEST:
                return "@ttw.ir";
            case ESF:
                return "@esf.ir";
            default:
                throw new UnsupportedOperationException();
        }
    }
    //
    public static String getCameraUploadUrl(CompanyNames companyNames){
        switch (companyNames){
            case ZONE1:
                return "http://217.146.220.33:50011/Api/api/CRMobileOffLoad/UploadImage";
            case ZONE2:
                return "http://212.16.75.194:8080/Api/api/CRMobileOffLoad/UploadImage";
            case ZONE3:
                return "http://212.16.69.36:90/Api/api/CRMobileOffLoad/UploadImage";
            case ZONE4:
                return "http://91.98.248.36:8081/Api/api/CRMobileOffLoad/UploadImage";
            case ZONE5:
                return "http://80.69.252.151/Api/api/CRMobileOffLoad/UploadImage";
            case ZONE6:
                return "http://85.133.190.220:4121/Api/api/CRMobileOffLoad/UploadImage";
            case TSW:
                return "http://81.90.148.25/Api/api/CRMobileOffLoad/UploadImage";
            case TE:
                return "http://185.12.60.135/Api/api/CRMobileOffLoad/UploadImage";
            case TSE:
                return "http://5.160.85.228:9098/Api/api/CRMobileOffLoad/UploadImage";
            case TOWNS_WEST:
                return "http://217.66.195.75/Api/api/CRMobileOffLoad/UploadImage";
            case ESF:
                return "http://172.18.12.122/Api/api/CRMobileOffLoad/UploadImage";
            default:
                throw new UnsupportedOperationException();
        }
    }
    //
    public static int getSplashRecourceId(CompanyNames companyNames){
        switch (companyNames){
            case ESF:
                return R.drawable.logo_teh;
            default:
                return R.drawable.logo_teh;
        }
    }
    //
    public static int getStartupImageRecourceId(CompanyNames companyName){
        switch (companyName){
            case ESF:
                return R.drawable.startup_esf;
            default:
                return R.drawable._teh1;
        }
    }
    //
    public static CompanyNames getActiveCompanyName(){
        return CompanyNames.TSW;
    }
}
