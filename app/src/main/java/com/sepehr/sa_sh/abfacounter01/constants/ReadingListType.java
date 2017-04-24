package com.sepehr.sa_sh.abfacounter01.constants;

/**
 * Created by saeid on 3/11/2017.
 */
public enum ReadingListType {
    NORMAL(0),
    SPECIFIC_COUNTER_STATE_CODE(1),
    ALAL_HESAB_ALL(255),
    UNREAD(256);

    private final int value;
    ReadingListType(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
