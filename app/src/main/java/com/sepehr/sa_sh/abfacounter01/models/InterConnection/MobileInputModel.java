package com.sepehr.sa_sh.abfacounter01.models.InterConnection;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by saeid on 2/18/2017.
 */
public class MobileInputModel {
    @SerializedName("MyWorks")
    public List<OnOffLoadViewModel> myWorks;
    //public List<LoadModel> myWorks;

    @SerializedName("ReadingConfigs")
    public List<ReadingConfigViewModel> readingConfigs;

    @SerializedName("HighLowInfo")
    public HighLowViewModel highLowViewModel;

    @SerializedName("CounterStateValueKeys")
    public List<CounterStateValueKeyViewModel> counterStateValueKeys;

    @SerializedName("ReportValueKeys")
    public List<CounterReportValueKeyViewModel> reportValueKeys;

    @SerializedName("KarbariInfos")
    public List<KarbariViewModel> karbariInfos;
}
