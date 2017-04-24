package com.sepehr.sa_sh.abfacounter01.constants;

/**
 * Created by saeid on 3/2/2017.
 */
public enum MediaType {
    IMAGE(1),
    VIDEO(2);

    private final int value;
    MediaType(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
