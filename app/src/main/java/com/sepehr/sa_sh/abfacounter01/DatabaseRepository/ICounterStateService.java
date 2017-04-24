package com.sepehr.sa_sh.abfacounter01.DatabaseRepository;

/**
 * Created by saeid on 3/9/2017.
 */
public interface ICounterStateService {
    boolean canEnterNumber();
    boolean canNumberBeLessThanPre();
    boolean shouldIOpenBodyBox();
    //
    boolean shouldIEnterNumber(int mainCode);
    boolean canIEnterNumber(int mainCode);
}
