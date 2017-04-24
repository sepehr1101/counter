package layout.gridorlistviewtest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by saeid on 3/10/2017.
 */
public class ListViewCheckboxReportAdaptor extends ArrayAdapter<ReportCheckboxModel>{
    private List<ReportCheckboxModel> reportCheckboxes;
    private Context appContext;
    private LayoutInflater inflater;
    public ListViewCheckboxReportAdaptor(Context context, int textViewResourceId,
                                         List<ReportCheckboxModel> reportCheckboxes, LayoutInflater inflater) {
        super(context, textViewResourceId, reportCheckboxes);
        this.reportCheckboxes=reportCheckboxes;
        this.appContext=context;
        this.inflater=inflater;
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
            convertView = inflater.inflate(R.layout.country_info, null);

            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
            convertView.setTag(holder);

            holder.name.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    ReportCheckboxModel reportCheckboxModel = (ReportCheckboxModel) cb.getTag();
                    Toast.makeText(appContext,
                            "Clicked on Checkbox: " + cb.getText() +
                                    " is " + cb.isChecked(),
                            Toast.LENGTH_LONG).show();
                    reportCheckboxModel.setIsChecked(cb.isChecked());
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

        return convertView;

    }

}
