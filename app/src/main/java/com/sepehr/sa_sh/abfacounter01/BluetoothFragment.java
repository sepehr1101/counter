package com.sepehr.sa_sh.abfacounter01;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by saeid on 10/20/2016.
 */
public class BluetoothFragment extends Fragment {
    public BluetoothFragment() {
    }
    Button turnOnBtn,turnOffBtn,displayListBtn;
    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    ListView listView;
    LinearLayout listViewWrapper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        initialize(rootView);
        setListeners();
        return rootView;
    }
    //
    private void initialize(View rootView){
        turnOffBtn = (Button)rootView.findViewById(R.id.turnOff);
        turnOnBtn=(Button)rootView.findViewById(R.id.turnOnBtn);
        displayListBtn=(Button)rootView.findViewById(R.id.displayListBtn);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        listView = (ListView)rootView.findViewById(R.id.paired_device_list);
        listViewWrapper=(LinearLayout)rootView.findViewById(R.id.listViewWrapper);
    }
    //
    private void setListeners(){
        turnOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visible(view);
            }
        });
        //
        turnOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOff(view);
            }
        });
        //
        displayListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayList(view);
            }
        });
    }
    //
    public void turnOn(View v){
        if (!bluetoothAdapter.isEnabled()) {
            Intent on = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(on, 0);
            Toast.makeText(getContext(), "روشن", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getContext(),"در حال حاضر روشن", Toast.LENGTH_LONG).show();
        }
    }

    public void turnOff(View v){
        bluetoothAdapter.disable();
        Toast.makeText(getContext(),"خاموش شد" ,Toast.LENGTH_LONG).show();
    }

    public  void visible(View v){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }

    public void displayList(View v){
        if(listViewWrapper.getVisibility()!=View.VISIBLE){
            listViewWrapper.setVisibility(View.VISIBLE);
        }
        pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList list = new ArrayList();

        for(BluetoothDevice bt : pairedDevices) {
            list.add(bt.getName());
        }
        if(pairedDevices.size()<1){
            Toast.makeText(getContext(),"موردی پیدا نشد",Toast.LENGTH_SHORT).show();
        }


        final ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }
}
