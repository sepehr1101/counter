package com.sepehr.sa_sh.abfacounter01.DatabaseRepository;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.sepehr.sa_sh.abfacounter01.constants.ReadingListType;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CounterReportValueKeyModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CounterStateValueKeyModel;
import java.util.List;

/**
 * Created by sepehr on 3/9/2017.
 */
public class CounterStateService implements ICounterStateService {
    private  String[] counterStates;
    CounterStateValueKeyModel counterStateValueKeyModel;
    List<CounterReportValueKeyModel> selectedReports;
    int clientSelectedItem;

    private CounterStateService() {
    }
    //
    public CounterStateService(String selectedItemText,
                               List<CounterReportValueKeyModel> selectedReports) {
        this.clientSelectedItem = clientSelectedItem;
        this.selectedReports=selectedReports;
        counterStateValueKeyModel=CounterStateValueKeyModel.find(CounterStateValueKeyModel.class,
                        "TITLE=?",selectedItemText+"").get(0);
    }
    //

    public CounterStateService(int clientSelectedItem) {
        this.clientSelectedItem = clientSelectedItem;
        counterStateValueKeyModel=CounterStateValueKeyModel.find(CounterStateValueKeyModel.class,
                "CLIENTORDER=?",clientSelectedItem+"").get(0);
    }
    public CounterStateService(boolean newOverload){

    }
    //
    public static int getRealState(String selectedItemText){
        CounterStateValueKeyModel  counterStateValueKeyModel=CounterStateValueKeyModel
                .find(CounterStateValueKeyModel.class,
                "TITLE=?",selectedItemText+"").get(0);
         return counterStateValueKeyModel.getMAIN_CODE();
    }
    //
    public static String[] getCounterStateTitles(){
        List<CounterStateValueKeyModel> counterStateValueKeys=
                Select.from(CounterStateValueKeyModel.class).orderBy("CLIENTORDER").list();
        String[] counterStates=new String[counterStateValueKeys.size()];
        int i=0;
        for(CounterStateValueKeyModel counterStateModel:counterStateValueKeys){
            counterStates[i]=counterStateModel.getTITLE();
            ++i;
        }
        return counterStates;
    }
    //
    public static String[] getManeTitles(){
        List<CounterStateValueKeyModel> counterStateValueKeys=
                Select.from(CounterStateValueKeyModel.class)
                        .where(Condition.prop("ISMANE").eq(1))
                        .orderBy("CLIENTORDER").list();
        String[] counterStates=new String[counterStateValueKeys.size()+2];
        counterStates[0]="انتخاب نمایید";
        counterStates[1]="همه موانع";
        int i=2;
        for(CounterStateValueKeyModel counterStateModel:counterStateValueKeys){
            counterStates[i]=counterStateModel.getTITLE();
            ++i;
        }
        return counterStates;
    }
    //
    public static int getManeCode(int clientSelectedManeItem){
        if(clientSelectedManeItem==1){
            return ReadingListType.ALAL_HESAB_ALL.getValue();
        }
       List<CounterStateValueKeyModel> stateValueKeys=
                Select.from(CounterStateValueKeyModel.class)
                        .where(Condition.prop("ISMANE").eq(1))
                        .orderBy("CLIENTORDER").list();
        CounterStateValueKeyModel counterStateValueKey= stateValueKeys.get(clientSelectedManeItem-2);

        return counterStateValueKey.getMAIN_CODE();
    }
    //
    public boolean canEnterNumber(){
        return counterStateValueKeyModel.CanEnterNumber();
    }
    //
    public boolean shouldIEnterNumber(){
         return counterStateValueKeyModel.isSHOULD_ENTER_NUMBER()==true;
    }
    //
    private boolean canNumberBeLessThanPreFromState(){
        return counterStateValueKeyModel.CanBeNumberLessThanPre();
    }
    //
    public boolean shouldIOpenBodyBox(){
        return counterStateValueKeyModel.IsTavizi();
    }
    //
    public boolean canNumberBeLessThanPre() {
        boolean canNumberBeLessThanPreFromState = canNumberBeLessThanPreFromState();
        boolean canNumberBeLessThanPreFromReports = false;
        for (CounterReportValueKeyModel savedReport : selectedReports) {
            if (savedReport.isCAN_NUMBER_BE_LESS_THAN_PRE()) {
                canNumberBeLessThanPreFromReports = true;
                break;
            }
        }
        return (canNumberBeLessThanPreFromState || canNumberBeLessThanPreFromReports);
    }
    //-----------------------------------------------------------------------------------------------
    private CounterStateValueKeyModel get(int mainCode){
       CounterStateValueKeyModel counterStateValueKeyModel=CounterStateValueKeyModel.find(CounterStateValueKeyModel.class,
                "MAINCODE=?",mainCode+"").get(0);
        return counterStateValueKeyModel;
    }

    public boolean shouldIEnterNumber(int mainCode){
        CounterStateValueKeyModel counterStateValueKeyModel=get(mainCode);
        return counterStateValueKeyModel.isSHOULD_ENTER_NUMBER()==true;
    }
    public boolean canIEnterNumber(int mainCode){
        CounterStateValueKeyModel counterStateValueKeyModel=get(mainCode);
        return counterStateValueKeyModel.CanEnterNumber()==true;
    }
}
