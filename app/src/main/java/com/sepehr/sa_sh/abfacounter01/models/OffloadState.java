package com.sepehr.sa_sh.abfacounter01.models;

/**
 * Created by saeid on 4/16/2017.
 */
public final class OffloadState {
    private OffloadState() {
    }

    public static Integer IMPORTED=0;
    public static Integer ABOUT_TO_SEND=1;
    public static Integer SENT_SUCCESSFULLY=2;
}
