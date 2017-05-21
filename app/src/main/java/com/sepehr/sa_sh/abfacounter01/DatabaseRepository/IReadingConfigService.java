package com.sepehr.sa_sh.abfacounter01.DatabaseRepository;

import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.ReadingConfigModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by saeid on 4/16/2017.
 */
public interface IReadingConfigService {
    ReadingConfigModel get(int _index);
    ReadingConfigModel get(String trackNumberString);
    ReadingConfigModel get(BigDecimal trackNumber);

    List<String> getListNumbers();
}
