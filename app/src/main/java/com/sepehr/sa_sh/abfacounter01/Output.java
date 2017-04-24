package com.sepehr.sa_sh.abfacounter01;

import com.sepehr.sa_sh.abfacounter01.models.InterConnection.OnOffLoadViewModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saeid on 9/9/2016.
 */
public class Output {
    List<CounterReadingReport> myReports;
    List<OnOffLoadModel> myWorks_old;
    List<OnOffLoadViewModel> myWorks;
    //

    public Output(List<CounterReadingReport> myReports, List<OnOffLoadModel> myWorks) {
        this.myReports = myReports;
        this.myWorks_old = null;
        this.myWorks=setMyWorks(myWorks);
    }

    public List<OnOffLoadViewModel> setMyWorks(List<OnOffLoadModel> onOffLoadModels){
        myWorks=new ArrayList<>();
        for (OnOffLoadModel onOffLoadModel:onOffLoadModels) {
            OnOffLoadViewModel myWork=new OnOffLoadViewModel(onOffLoadModel);
            myWorks.add(myWork);
        }
        return myWorks;
    }

}
