package com.sepehr.sa_sh.abfacounter01.DatabaseRepository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.orm.SugarDb;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

/**
 * Created by saeid on 3/12/2017.
 */
public class StatisticsRepo implements IStatisticsRepo{
    SugarDb sugarDb;
    Context appContext;
    SQLiteDatabase database;

    public StatisticsRepo(Context appContext) {
        this.appContext=appContext;
        this.sugarDb=new SugarDb(appContext);
        database = sugarDb.getDB();
    }

    public long getAlalHesabSize(int configIndex) {
        long count = 0;
        String rawQuery ="SELECT COUNT(*) FROM ON_OFF_LOAD_MODEL C " +
                "JOIN COUNTER_STATE_VALUE_KEY_MODEL S ON C.COUNTER_STATE_CODE=S.MAINCODE " +
                "JOIN READING_CONFIG_MODEL R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                " WHERE S.ISMANE=1 AND R._INDEX="+configIndex;
        SQLiteStatement query = database.compileStatement(rawQuery);
        try {
            count = query.simpleQueryForLong();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            query.close();
        }
        return count;
    }
    //
    public long getAlalHesabSize() {
        long count = 0;
        String rawQuery ="SELECT COUNT(*) FROM ON_OFF_LOAD_MODEL C " +
                "JOIN COUNTER_STATE_VALUE_KEY_MODEL S ON C.COUNTER_STATE_CODE=S.MAINCODE " +
                "JOIN READING_CONFIG_MODEL R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                " WHERE S.ISMANE=1 AND R.IS_ACTIVE=1";
        SQLiteStatement query = database.compileStatement(rawQuery);
        try {
            count = query.simpleQueryForLong();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            query.close();
        }
        return count;
    }
    //
    public long getKolSize(int configIndex){
        long tedadKolSize=0;
        String rawQuery="SELECT COUNT(*) FROM ON_OFF_LOAD_MODEL C            " +
                "JOIN READING_CONFIG_MODEL R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                "                WHERE  R.IS_ACTIVE=1 AND R._INDEX="+configIndex;
        SQLiteStatement query = database.compileStatement(rawQuery);
        try {
            tedadKolSize = query.simpleQueryForLong();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            query.close();
        }
        return tedadKolSize;
    }
    //
    public long getKolSize(){
        long tedadKolSize=0;
        String rawQuery="SELECT COUNT(*) FROM ON_OFF_LOAD_MODEL C            " +
                "JOIN READING_CONFIG_MODEL R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                "                WHERE  R.IS_ACTIVE=1 ";
        SQLiteStatement query = database.compileStatement(rawQuery);
        try {
            tedadKolSize = query.simpleQueryForLong();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            query.close();
        }
        return tedadKolSize;
    }
    //
    public long getUnreadSize(int configIndex){
        long unreadSize=0;
        String rawQuery="SELECT COUNT(*) FROM ON_OFF_LOAD_MODEL C " +
                "JOIN READING_CONFIG_MODEL R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                "WHERE C.COUNTER_STATE_CODE IS NULL AND R.IS_ACTIVE=1 AND R._INDEX="+configIndex;
        SQLiteStatement query = database.compileStatement(rawQuery);
        try {
            unreadSize = query.simpleQueryForLong();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            query.close();
        }
        return unreadSize;
    }
    //
    public long getUnreadSize(){
        long unreadSize=0;
        String rawQuery="SELECT COUNT(*) FROM ON_OFF_LOAD_MODEL C " +
                "JOIN READING_CONFIG_MODEL R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                "WHERE C.COUNTER_STATE_CODE IS NULL AND R.IS_ACTIVE=1";
        SQLiteStatement query = database.compileStatement(rawQuery);
        try {
            unreadSize = query.simpleQueryForLong();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            query.close();
        }
        return unreadSize;
    }
}
