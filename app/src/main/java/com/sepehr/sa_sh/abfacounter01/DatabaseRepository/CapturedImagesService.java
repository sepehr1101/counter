package com.sepehr.sa_sh.abfacounter01.DatabaseRepository;

import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CapturedImageModel;
import java.util.List;

public class CapturedImagesService implements ICapturedImagesService {
    public CapturedImagesService() {
    }

    @Override
    public List<CapturedImageModel> getCapturedImages(int status,String trackNumberString) {
        String query="SELECT * FROM CAPTURED_IMAGE_MODEL where TRACK_NUMBER="+trackNumberString+" AND STATUS="+status;
        List<CapturedImageModel> capturedImages= CapturedImageModel.findWithQuery(CapturedImageModel.class,query);
        return capturedImages;
    }
}
