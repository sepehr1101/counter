package com.sepehr.sa_sh.abfacounter01.DatabaseRepository;

import com.sepehr.sa_sh.abfacounter01.CounterReadingReport;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by saeid on 4/16/2017.
 */
public interface ICounterReportService {
    List<CounterReadingReport> get(BigDecimal trackNumber);
    List<CounterReadingReport> getNeedToBeOffloadedOverall(BigDecimal trackNumber);
}
