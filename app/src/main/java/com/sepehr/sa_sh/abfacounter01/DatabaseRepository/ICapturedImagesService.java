package com.sepehr.sa_sh.abfacounter01.DatabaseRepository;

import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CapturedImageModel;

import java.util.List;

public interface ICapturedImagesService {
    List<CapturedImageModel> getCapturedImages(int status,String trackNumberString);
}
