package com.sepehr.sa_sh.abfacounter01;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.CounterReportService;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.models.ReportCheckboxModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sa-sh on 7/25/2016.
 */
public class CounterReportFragment extends DialogFragment
implements AdapterView.OnItemClickListener{
    ListViewCheckboxReportAdaptor listViewAdaptor = null;
    Button findSelectedAndExit;
    String bill_id;
    BigDecimal trackNumber;
    View rootView;
    IToastAndAlertBuilder toastAndAlertBuilder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        rootView = inflater.inflate(R.layout.fragment_counter_report, container, false);
        initialize(rootView);
        return rootView;
    }
    //
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        displayListView(rootView);
    }

    @Override
    public void onItemClick(AdapterView<?> parent,View view,int position,long id) {
        dismiss();
       /* Toast.makeText(getActivity(), listitems[position], Toast.LENGTH_SHORT)
                .show();*/
    }
    //
    private void initialize(View rootView){
        bill_id=((DisplayViewPager)getActivity()).getBill_id();
        trackNumber=((DisplayViewPager)getActivity()).getCurrentTrackNumber();
        toastAndAlertBuilder=new ToastAndAlertBuilder(getContext());
        findSelectedAndExit=(Button)rootView.findViewById(R.id.findSelectedAndExit);
        findSelectedButtonClickListener();
    }
    //
    private void displayListView(View rootView) {
        CounterReportService manager=new CounterReportService();
        List<ReportCheckboxModel> reportCheckboxModels = manager.getReportCheckboxes(bill_id);
        LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        listViewAdaptor = new ListViewCheckboxReportAdaptor(getContext(),
                R.layout.report_info, reportCheckboxModels,vi,bill_id,trackNumber,getFragmentManager());
        ListView listView = (ListView) rootView.findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(listViewAdaptor);
    }
    //
    @Override
    public  void onResume(){
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }
    //
    private void findSelectedButtonClickListener() {
        findSelectedAndExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer responseText = new StringBuffer();
                responseText.append("موارد زیر ذخیره شده اند...\n");
                ArrayList<ReportCheckboxModel> countryList = listViewAdaptor.reportCheckboxes;
                int selectedCount=0;
                for (int i = 0; i < countryList.size(); i++) {
                    ReportCheckboxModel reportCheckboxModel = countryList.get(i);
                    if (reportCheckboxModel.isChecked()) {
                        responseText.append("\n" + reportCheckboxModel.getTITLE());
                        selectedCount++;
                    }
                }
                if(selectedCount==0){
                    responseText.setLength(selectedCount);
                    responseText.append("گزارشی ذخیره نشده");
                }
                Toast.makeText(getContext(),
                        responseText, Toast.LENGTH_LONG).show();
                dismiss();
            }
        });

    }
}
