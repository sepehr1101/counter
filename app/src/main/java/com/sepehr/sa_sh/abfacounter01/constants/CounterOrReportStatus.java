package com.sepehr.sa_sh.abfacounter01.constants;

/**
 * Created by saeid on 3/11/2017.
 */
public enum CounterOrReportStatus {
    NORMAL(1),
    SHOULD_OPEN_SERIAL_BOX(2),
    SHOULD_OPEN_AHAD_BOX(3),
    SHOULD_OPEN_KARBARI_BOX(4),
    COULD_NUMBER_BE_LESS_THAN_PRE(5);

    private final int value;
    CounterOrReportStatus(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
