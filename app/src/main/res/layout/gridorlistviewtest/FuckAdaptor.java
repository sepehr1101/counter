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

import java.util.ArrayList;

/**
 * Created by saeid on 3/10/2017.
 */
public class FuckAdaptor extends ArrayAdapter<Country>{
    private ArrayList<Country> countryList;
    private LayoutInflater inflater;
    private Context mContext;

    public FuckAdaptor(Context context, int textViewResourceId,
                           ArrayList<Country> countryList,LayoutInflater inflater) {
        super(context, textViewResourceId, countryList);
        this.countryList = new ArrayList<Country>();
        this.countryList.addAll(countryList);
        this.inflater=inflater;
        this.mContext=context;
    }

    private class ViewHolder {
        TextView code;
        CheckBox name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater vi =inflater;
            convertView = vi.inflate(R.layout.country_info, null);

            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
            convertView.setTag(holder);

            holder.name.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    Country country = (Country) cb.getTag();
                    Toast.makeText(mContext,
                            "Clicked on Checkbox: " + cb.getText() +
                                    " is " + cb.isChecked(),
                            Toast.LENGTH_LONG).show();
                    country.setSelected(cb.isChecked());
                }
            });
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Country country = countryList.get(position);
        holder.code.setText(" (" +  country.getCode() + ")");
        holder.name.setText(country.getName());
        holder.name.setChecked(country.isSelected());
        holder.name.setTag(country);

        return convertView;

    }

}
