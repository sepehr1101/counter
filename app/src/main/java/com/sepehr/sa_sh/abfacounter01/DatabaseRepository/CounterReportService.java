package com.sepehr.sa_sh.abfacounter01.DatabaseRepository;

import com.orm.SugarContext;
import com.orm.SugarDb;
import com.orm.SugarRecord;
import com.orm.util.SugarConfig;
import com.orm.util.SugarCursor;
import com.sepehr.sa_sh.abfacounter01.CounterReadingReport;
import com.sepehr.sa_sh.abfacounter01.constants.CounterOrReportStatus;
import com.sepehr.sa_sh.abfacounter01.models.OffloadState;
import com.sepehr.sa_sh.abfacounter01.models.ReportCheckboxModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CounterReportValueKeyModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by saeid on 3/10/2017.
 */
public class CounterReportService implements ICounterReportService{
    CounterReportValueKeyModel reportValueKeyModel;
    List<CounterReadingReport> counterReadingReports;
    List<CounterReportValueKeyModel> counterReportValueKeys;
    List<ReportCheckboxModel> reportCheckboxes=new ArrayList<>();
    String billId;

    public CounterReportService() {
    }

    public CounterReportService(String billId) {
        this.billId = billId;
    }

    private CounterReportValueKeyModel getReportValueKeyModel(int reportCode){
        reportValueKeyModel=CounterReportValueKeyModel
                .find(CounterReportValueKeyModel.class,"CODE=?",reportCode+"").get(0);
        return reportValueKeyModel;
    }
    //
    private List<CounterReportValueKeyModel> getCounterReportValueKeys(){
        counterReportValueKeys= CounterReportValueKeyModel
                .listAll(CounterReportValueKeyModel.class);
        return counterReportValueKeys;
    }
    //
    private List<CounterReadingReport> getCounterReadingReports(String billId){
        counterReadingReports=CounterReadingReport.find(CounterReadingReport.class,
                "BILL_ID=?", billId);
        return  counterReadingReports;
    }
    //
    public List<ReportCheckboxModel> getReportCheckboxes(String billId){
        counterReadingReports=getCounterReadingReports(billId);
        counterReportValueKeys=getCounterReportValueKeys();
        int i=0;
        for (CounterReportValueKeyModel reportValueKey:counterReportValueKeys) {
            ReportCheckboxModel reportCheckbox=new
                    ReportCheckboxModel(reportValueKey.getCODE(),reportValueKey.getTITLE(),false);
            reportCheckboxes.add(reportCheckbox);
            for (CounterReadingReport savedReport:counterReadingReports) {
                if(reportValueKey.getCODE()==savedReport.getReportCode()){
                    reportCheckbox.setIsChecked(true);
                    reportCheckboxes.get(i).setIsChecked(true);
                }
            }
            ++i;
        }
        return reportCheckboxes;
    }
    //
    public List<CounterReportValueKeyModel> getCounterReadingSelectedReports(String billId){
        counterReadingReports=getCounterReadingReports(billId);
        counterReportValueKeys=getCounterReportValueKeys();
        List<CounterReportValueKeyModel> selectedReports=new ArrayList<>();
        for (CounterReportValueKeyModel reportValueKey:counterReportValueKeys) {
            for (CounterReadingReport savedReport:counterReadingReports) {
                if(reportValueKey.getCODE()==savedReport.getReportCode()){
                    selectedReports.add(reportValueKey);
                }
            }
        }
        return selectedReports;
    }
    //
    public CounterOrReportStatus saveReport(ReportCheckboxModel reportCheckboxModel,int userCode,BigDecimal trackNumber){
        CounterReportValueKeyModel reportValueKey=getReportValueKeyModel(reportCheckboxModel.getReportCode());
        CounterReadingReport counterReadingReport=
                new CounterReadingReport(billId,"",reportCheckboxModel.getReportCode(),
                        reportCheckboxModel.getTITLE(),userCode,1,trackNumber);
        counterReadingReport.save();
        if(reportValueKey.IS_AHAD()){
            return CounterOrReportStatus.SHOULD_OPEN_AHAD_BOX;
        }
        if(reportValueKey.IS_KARBARI()){
            return CounterOrReportStatus.SHOULD_OPEN_KARBARI_BOX;
        }
        if(reportValueKey.IS_TAVIZI()){
            return CounterOrReportStatus.SHOULD_OPEN_SERIAL_BOX;
        }
        return CounterOrReportStatus.NORMAL;
    }
    //
    public void removeReport(int reportCode){
        String whereQuery="BILL_ID=? AND REPORT_CODE= ? ";
        CounterReadingReport counterReadingReport=
                CounterReadingReport
                        .find(CounterReadingReport.class,whereQuery, billId,reportCode+"").get(0);
        counterReadingReport.delete();
    }
    //
    public List<CounterReadingReport> getNeedToBeOffloadedOverall(BigDecimal trackNumber){
        String query="SELECT * FROM COUNTER_READING_REPORT C " +
                "JOIN READING_CONFIG_MODEL R " +
                "ON C.TRACK_NUMBER = R.TRACK_NUMBER " +
                "WHERE C.OFF_LOAD_STATE<>2 AND R.TRACK_NUMBER="+trackNumber;
        List<CounterReadingReport> readingReports=
                CounterReadingReport.findWithQuery(CounterReadingReport.class, query);
        return readingReports;
    }

    public List<CounterReadingReport> get(BigDecimal trackNumber){
        String query="SELECT * FROM COUNTER_READING_REPORT C " +
                "JOIN READING_CONFIG_MODEL R " +
                "ON C.TRACK_NUMBER = R.TRACK_NUMBER " +
                "WHERE C.OFF_LOAD_STATE<>2 AND R.TRACK_NUMBER="+trackNumber;
        List<CounterReadingReport> readingReports=
                CounterReadingReport.findWithQuery(CounterReadingReport.class, query);
        return readingReports;
    }

    public List<CounterReadingReport> get(int offloadState){
        String query="SELECT C.* FROM COUNTER_READING_REPORT C "+
                "JOIN READING_CONFIG_MODEL R ON C.TRACK_NUMBER=R.TRACK_NUMBER "+
                "WHERE C.OFF_LOAD_STATE="+offloadState+" "+"AND R.IS_ACTIVE=1";
      /*  final  List<CounterReadingReport> reportList=CounterReadingReport
                .find(CounterReadingReport.class,"OFF_LOAD_STATE= ? ",offloadState+"");*/
        final  List<CounterReadingReport> reportList=CounterReadingReport
                .findWithQuery(CounterReadingReport.class,query);
        return reportList;
    }

    public void changeOffloadState(Collection<CounterReadingReport> reports, int offloadState){
        for (CounterReadingReport report : reports) {
            report.setOffLoadState(offloadState);
        }
        SugarRecord.saveInTx(reports);
    }
}
