package com.sepehr.sa_sh.abfacounter01;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.CounterReportService;
import com.sepehr.sa_sh.abfacounter01.constants.CounterOrReportStatus;
import com.sepehr.sa_sh.abfacounter01.models.ReportCheckboxModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by saeid on 3/10/2017.
 */
public class ListViewCheckboxReportAdaptor extends ArrayAdapter<ReportCheckboxModel>{
    public ArrayList<ReportCheckboxModel> reportCheckboxes;
    private Context appContext;
    private LayoutInflater inflater;
    private CounterReportService counterReportService;
    BigDecimal trackNumber;
    FragmentManager fragmentManager;
    public ListViewCheckboxReportAdaptor(Context context, int textViewResourceId,
                           List<ReportCheckboxModel> reportCheckboxes,LayoutInflater inflater,
                                         String billId,BigDecimal trackNumber,FragmentManager fragmentManager) {
        super(context, textViewResourceId, reportCheckboxes);
        this.reportCheckboxes = new ArrayList<ReportCheckboxModel>();
        this.reportCheckboxes.addAll(reportCheckboxes);
        this.appContext=context;
        this.inflater=inflater;
        counterReportService =new CounterReportService(billId);
        this.trackNumber=trackNumber;
        this.fragmentManager=fragmentManager;
    }

    private class ViewHolder {
        TextView code;
        CheckBox name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder ;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.report_info, null);

            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.reportCode);
            holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
            convertView.setTag(holder);
            holder.name.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    ReportCheckboxModel reportCheckboxModel = (ReportCheckboxModel) cb.getTag();
                    String selectedOrRemoved=cb.isChecked()?"انتخاب":"حذف";
                    Toast.makeText(appContext,
                              selectedOrRemoved+":"+ cb.getText(),
                            Toast.LENGTH_LONG).show();
                    reportCheckboxModel.setIsChecked(cb.isChecked());
                    if(cb.isChecked()){
                        CounterOrReportStatus reportStatus=
                                counterReportService.saveReport(reportCheckboxModel, 0,trackNumber);
                        postSaveReportAction(reportStatus);
                    }
                    else {
                        counterReportService.removeReport(reportCheckboxModel.getReportCode());
                    }
                }
            });
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        ReportCheckboxModel reportCheckboxModel = reportCheckboxes.get(position);
        holder.code.setText(" (" + reportCheckboxModel.getReportCode() + ")");
        holder.name.setText(reportCheckboxModel.getTITLE());
        holder.name.setChecked(reportCheckboxModel.isChecked());
        holder.name.setTag(reportCheckboxModel);
        holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        return convertView;

    }
    //
    private void postSaveReportAction(CounterOrReportStatus counterOrReportStatus){
        switch (counterOrReportStatus){
            case SHOULD_OPEN_SERIAL_BOX:
                CounterSerialFragment dialogFragment=new CounterSerialFragment();
                dialogFragment.show(fragmentManager, "serial");
                return;
            case SHOULD_OPEN_AHAD_BOX:
                TaqikAhadFragment dialogFragmentAhad=new TaqikAhadFragment();
                dialogFragmentAhad.show(fragmentManager, "ahad");
                return;
            case SHOULD_OPEN_KARBARI_BOX:
                TaqirKarbariFragment dialogFragmentKarbari=new TaqirKarbariFragment();
                dialogFragmentKarbari.show(fragmentManager,"karbari");
            default:
                return;
        }
    }
}
