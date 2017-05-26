package com.sepehr.sa_sh.abfacounter01.DatabaseRepository;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.sepehr.sa_sh.abfacounter01.models.ReadingConfigListItemModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.ReadingConfigModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by saeid on 3/31/2017.
 */
public class ReadingConfigService implements IReadingConfigService{
    public ReadingConfigService() {
    }

    public List<ReadingConfigListItemModel> get(){
        List<ReadingConfigModel> readingConfigs=ReadingConfigModel
                .listAll(ReadingConfigModel.class);
        List<ReadingConfigListItemModel> items=new ArrayList<>();
        for (ReadingConfigModel readingConfig:readingConfigs) {
            ReadingConfigListItemModel item=new ReadingConfigListItemModel(readingConfig);
            items.add(item);
        }
        return items;
    }

    public List<String> getListNumbers(){
        List<ReadingConfigModel> readingConfigs=ReadingConfigModel
                .listAll(ReadingConfigModel.class,"_INDEX");
        List<String> items=new ArrayList<>();
        items.add("انتخاب فرمایید");
        for (ReadingConfigModel readingConfig:readingConfigs) {
            String item=readingConfig.getTrackNumber().toBigInteger().toString();
            items.add(item);
        }
        return items;
    }

    public ReadingConfigModel get(int _index){
        ReadingConfigModel readingConfig= Select.from(ReadingConfigModel.class)
                .where(Condition.prop("_INDEX").eq(_index+""))
                .first();
        return readingConfig;
    }

    public ReadingConfigModel get(String trackNumberString){
        ReadingConfigModel readingConfig= Select.from(ReadingConfigModel.class)
                .where(Condition.prop("TRACK_NUMBER").eq(trackNumberString+".0"))
                .first();
        return readingConfig;
    }
    public ReadingConfigModel get(BigDecimal trackNumber){
        ReadingConfigModel readingConfig= Select.from(ReadingConfigModel.class)
                .where(Condition.prop("TRACK_NUMBER").eq(trackNumber.toString()))
                .first();
        return readingConfig;
    }

    public void set(int _index,boolean isActive){
        ReadingConfigModel readingConfig= get(_index);
        readingConfig.setIsActive(isActive);
        readingConfig.save();
    }

    public int getMaxIndex(){
        List<ReadingConfigModel> readingConfigs= ReadingConfigModel
                .find(ReadingConfigModel.class, null, null, null, "ID DESC", "1");
        if(readingConfigs!=null && readingConfigs.size()>0){
            return readingConfigs.get(0).get_index()+1;
        }
        return 1;
    }

    public Collection<BigDecimal> getReadTrackNumbers(){
        Collection<BigDecimal> trackNumbers=new ArrayList<>();
        Collection<ReadingConfigModel> readingConfigs=ReadingConfigModel.listAll(ReadingConfigModel.class);
        for (ReadingConfigModel readingConfig:readingConfigs) {
            trackNumbers.add(readingConfig.getTrackNumber());
        }
        return trackNumbers;
    }
}
