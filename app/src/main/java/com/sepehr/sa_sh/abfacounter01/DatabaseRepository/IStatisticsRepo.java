package com.sepehr.sa_sh.abfacounter01.DatabaseRepository;

/**
 * Created by saeid on 3/12/2017.
 */
public interface IStatisticsRepo {
    long getAlalHesabSize(int configIndex);
    long getAlalHesabSize();
    long getKolSize(int configIndex);
    long getKolSize();
    long getUnreadSize(int configIndex);
    long getUnreadSize();
}
