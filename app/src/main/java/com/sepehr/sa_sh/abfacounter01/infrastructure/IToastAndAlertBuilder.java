package com.sepehr.sa_sh.abfacounter01.infrastructure;

/**
 * Created by saeid on 3/2/2017.
 */
public interface IToastAndAlertBuilder {
    void makeSimpleAlert(String message);
    void makeSimpleAlert(String message,String title);

    void  makeSimpleToast(String message);
}
