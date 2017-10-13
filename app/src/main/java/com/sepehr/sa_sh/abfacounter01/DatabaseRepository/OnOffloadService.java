package com.sepehr.sa_sh.abfacounter01.DatabaseRepository;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.orm.SugarDb;
import com.orm.SugarRecord;
import com.sepehr.sa_sh.abfacounter01.constants.ReadingListType;
import com.sepehr.sa_sh.abfacounter01.models.OffloadState;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * Created by saeid on 3/11/2017.
 */
public class OnOffloadService implements IOnOffloadService {
    List<OnOffLoadModel> _onOfLoadList;
    SugarDb sugarDb;
    Context appContext;
    SQLiteDatabase database;
    //

    public OnOffloadService(Context appContext) {
        this.appContext=appContext;
        this.sugarDb=new SugarDb(appContext);
        database = sugarDb.getDB();
    }

    //
    public List<OnOffLoadModel> getReadingList(int type){
        if(type<ReadingListType.ALAL_HESAB_ALL.getValue() &&
                type>ReadingListType.NORMAL.getValue()){
            return getReadingListSpecificCode(type);
        }
        if(type==ReadingListType.ALAL_HESAB_ALL.getValue()){
            return getReadingListAlalHesab();
        }
        if(type==ReadingListType.UNREAD.getValue()){
            return getReadingListUnread();
        }
        if(type==ReadingListType.NORMAL.getValue()) {
            return getReadingListNormal();
        }
        else {
            throw new UnsupportedOperationException();
        }
    }
    private List<OnOffLoadModel> getReadingListNormal() {
        String query="SELECT C.* FROM ON_OFF_LOAD_MODEL C " +
                "JOIN READING_CONFIG_MODEL R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                "  WHERE  R.IS_ACTIVE=1  ORDER BY C.QERAAT_CODE";
        _onOfLoadList =OnOffLoadModel.findWithQuery(OnOffLoadModel.class,
                query);
        return _onOfLoadList;
    }
    private List<OnOffLoadModel> getReadingListAlalHesab(int configIndex){
        String query="SELECT C.* FROM ON_OFF_LOAD_MODEL C " +
                " JOIN COUNTER_STATE_VALUE_KEY_MODEL S ON C.COUNTER_STATE_CODE=S.MAINCODE  " +
                "JOIN READING_CONFIG_MODEL R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                "  WHERE S.ISMANE=0 AND R._INDEX="+configIndex;
        _onOfLoadList =OnOffLoadModel.findWithQuery(OnOffLoadModel.class,
                query);
        return _onOfLoadList;
    }
    private List<OnOffLoadModel> getReadingListAlalHesab(){
        String query="SELECT C.* FROM ON_OFF_LOAD_MODEL C " +
                " JOIN COUNTER_STATE_VALUE_KEY_MODEL S ON C.COUNTER_STATE_CODE=S.MAINCODE  " +
                "JOIN READING_CONFIG_MODEL R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                "                WHERE S.ISMANE=1 AND R.IS_ACTIVE=1" +
                "  ORDER BY C.QERAAT_CODE";
        _onOfLoadList =OnOffLoadModel.findWithQuery(OnOffLoadModel.class,
                query);
        return _onOfLoadList;
    }
    private List<OnOffLoadModel> getReadingListUnread(){
        String query="SELECT C.* FROM ON_OFF_LOAD_MODEL C " +
                "JOIN READING_CONFIG_MODEL R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                "  WHERE C.COUNTER_STATE_CODE IS NULL AND R.IS_ACTIVE=1" +
                "  ORDER BY C.QERAAT_CODE";
        _onOfLoadList =OnOffLoadModel.findWithQuery(OnOffLoadModel.class,
                query);
        return _onOfLoadList;
    }
    private List<OnOffLoadModel> getReadingListSpecificCode(int counterStateCode) {
        _onOfLoadList = OnOffLoadModel.find(OnOffLoadModel.class,
                "COUNTER_STATE_CODE=?", counterStateCode + "");
        String query="SELECT C.* FROM ON_OFF_LOAD_MODEL C " +
                "JOIN READING_CONFIG_MODEL R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                "  WHERE C.COUNTER_STATE_CODE ="+counterStateCode+" AND R.IS_ACTIVE=1 " +
                "  ORDER BY C.QERAAT_CODE";
        _onOfLoadList =OnOffLoadModel.findWithQuery(OnOffLoadModel.class,query);
        return _onOfLoadList;
    }
    //
    public List<OnOffLoadModel> getNeedToBeOffloadedOverall(int configIndex){
        String query="SELECT C.* FROM ON_OFF_LOAD_MODEL C " +
                "JOIN READING_CONFIG_MODEL R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                " WHERE C.OFF_LOAD_STATE<>2 AND R._INDEX="+configIndex;
        _onOfLoadList =OnOffLoadModel.findWithQuery(OnOffLoadModel.class,
                query);
        return _onOfLoadList;
    }
    //

    /**
     * get all OnOffloadModel list of a route(readingConfig) ,
     * including active or inActive
     * @param trackNumber
     * @return List<OnOffLoadModel>
     */
    public List<OnOffLoadModel> get(BigDecimal trackNumber){
        String query="SELECT C.* FROM ON_OFF_LOAD_MODEL C " +
                "JOIN READING_CONFIG_MODEL R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                " WHERE R.TRACK_NUMBER="+trackNumber;
        _onOfLoadList =OnOffLoadModel.findWithQuery(OnOffLoadModel.class,
                query);
        return _onOfLoadList;
    }

    /**
     * get OnOffloadModel list in specific 'offloadState' of a route(readingConfig) ,
     * including active or inActive
     * @param configIndex
     * @param offloadState
     * @return List<OnOffLoadModel>
     */
    public List<OnOffLoadModel> get(int configIndex,Integer offloadState){
        String query="SELECT C.* FROM ON_OFF_LOAD_MODEL C " +
                "JOIN READING_CONFIG_MODEL R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                " WHERE C.OFF_LOAD_STATE="+ offloadState.toString() +" AND R._INDEX="+configIndex;
        _onOfLoadList =OnOffLoadModel.findWithQuery(OnOffLoadModel.class,
                query);
        return _onOfLoadList;
    }

    /**
     * <h1>get list by 'offloadState'</h1>
     * <p>int offloadState is of type 'OffloadState' constant only class
     * in models package</p>
     * @param offloadState
     * @return List<OnOffLoadModel></>
     */
    public List<OnOffLoadModel> get(int offloadState){
      /* String query="SELECT C.* FROM ON_OFF_LOAD_MODEL C "+
            "WHERE C.OFF_LOAD_STATE="+offloadState+" ";*/
        String query="SELECT C.* FROM ON_OFF_LOAD_MODEL C "+
                "JOIN READING_CONFIG_MODEL R ON C.TRACK_NUMBER=R.TRACK_NUMBER "+
                "WHERE C.OFF_LOAD_STATE="+offloadState+" "+"AND R.IS_ACTIVE=1";
    /*    final List<OnOffLoadModel> onOffLoadModelList=OnOffLoadModel
                .find(OnOffLoadModel.class, "OFF_LOAD_STATE= ? ", offloadState+"");*/
        final List<OnOffLoadModel> onOffLoadModelList=OnOffLoadModel
                .findWithQuery(OnOffLoadModel.class,query);
        return onOffLoadModelList;
    }

    public void changeOffloadState(Collection<OnOffLoadModel> onOffloadList, int offloadState){
        for (OnOffLoadModel onOffLoad : onOffloadList) {
            onOffLoad.offLoadState = offloadState;
            //onOffLoad.save();
            Log.i("save done ",offloadState+"");
        }
        SugarRecord.saveInTx(onOffloadList);
    }
}
