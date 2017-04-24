package com.sepehr.sa_sh.abfacounter01;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sepehr.sa_sh.abfacounter01.DatabaseRepository.ReadingConfigService;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.models.ReadingConfigListItemModel;

import java.util.List;

/**
 * Created by saeid on 3/31/2017.
 */
public class ReadingConfigFragment  extends Fragment implements AdapterView.OnItemClickListener{

    ReadingConfigAdaptor readingConfigAdaptor = null;
    View rootView;
    IToastAndAlertBuilder toastAndAlertBuilder;

    public ReadingConfigFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_reading_config, container, false);
        final Typeface face = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/BZar.ttf");
        initialize(rootView);
        displayListView(rootView);
        return rootView;
    }

    //
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //displayListView(rootView);
    }

    @Override
    public void onItemClick(AdapterView<?> parent,View view,int position,long id) {
    }
    //
    private void initialize(View rootView){
        toastAndAlertBuilder=new ToastAndAlertBuilder(getContext());
    }
    //
    private void displayListView(View rootView) {
        ReadingConfigService manager=new ReadingConfigService();
        List<ReadingConfigListItemModel> readingConfigs = manager.get();
        LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        readingConfigAdaptor = new ReadingConfigAdaptor(getContext(),
                R.layout.reading_config_list_item, readingConfigs,vi,getFragmentManager());
        ListView listView = (ListView) rootView.findViewById(R.id.readingConfigList);
        // Assign adapter to ListView
        listView.setAdapter(readingConfigAdaptor);
    }
}
