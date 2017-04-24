package com.sepehr.sa_sh.abfacounter01.constants;

/**
 * Created by saeid on 3/5/2017.
 */
public enum ImageScale {
    AS_IS(1),
    HALF_SMALL(2),
    SMALL(3),
    X_SMALL(4),
    XX_SMALL(8),
    TINY(16),
    SUPER_TINY(32);

    private final int value;
    ImageScale(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
