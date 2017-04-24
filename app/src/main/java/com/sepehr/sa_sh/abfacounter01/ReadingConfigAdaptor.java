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

import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ReadingConfigService;
import com.sepehr.sa_sh.abfacounter01.models.ReadingConfigListItemModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by saeid on 3/31/2017.
 */
public class ReadingConfigAdaptor  extends ArrayAdapter<ReadingConfigListItemModel> {

    public ArrayList<ReadingConfigListItemModel> reportCheckboxes;
    private Context appContext;
    private LayoutInflater inflater;
    FragmentManager fragmentManager;
    ReadingConfigService configManager;

    public ReadingConfigAdaptor(Context context,
                                int textViewResourceId,
                                List<ReadingConfigListItemModel> reportCheckboxes,
                                LayoutInflater inflater,
                                FragmentManager fragmentManager) {
        super(context, textViewResourceId, reportCheckboxes);
        this.reportCheckboxes = new ArrayList<ReadingConfigListItemModel>();
        this.reportCheckboxes.addAll(reportCheckboxes);
        this.appContext=context;
        this.inflater=inflater;
        this.fragmentManager=fragmentManager;
        this.configManager=new ReadingConfigService();
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
                    ReadingConfigListItemModel readingConfigListItemModel = (ReadingConfigListItemModel) cb.getTag();
                    String selectedOrRemoved=cb.isChecked()?"انتخاب":"حذف";
                    Toast.makeText(appContext,
                            selectedOrRemoved + ":" + cb.getText(),
                            Toast.LENGTH_LONG).show();
                    readingConfigListItemModel.setIsChecked(cb.isChecked());
                    configManager.set(readingConfigListItemModel.getItemId(),readingConfigListItemModel.isChecked());
                }
            });
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        ReadingConfigListItemModel readingConfigListItemModel = reportCheckboxes.get(position);
        holder.code.setText(" (" + readingConfigListItemModel.getItemId() + ")");
        holder.name.setText(readingConfigListItemModel.getTitle());
        holder.name.setChecked(readingConfigListItemModel.isChecked());
        holder.name.setTag(readingConfigListItemModel);
        holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        return convertView;

    }
    //
}
