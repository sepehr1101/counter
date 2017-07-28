package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;

/**
 * Created by saeid on 4/6/2017.
 */
public class ContactUsFragment extends DialogFragment {
    View rootView;
    Button exit,call;
    IToastAndAlertBuilder toastAndAlertBuilder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        rootView = inflater.inflate(R.layout.fragment_contact_us, container, false);
        initialize(rootView);
        return rootView;
    }

    @Override
    public  void onResume(){
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    private void initialize(View rootView){
        toastAndAlertBuilder=new ToastAndAlertBuilder(getContext());
        exit=(Button)rootView.findViewById(R.id.exit);
        call=(Button)rootView.findViewById(R.id.call);
        exitClick();
        callClick();
    }

    private void exitClick(){
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void callClick(){
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent=new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:09357366741"));
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Log.e("call permission :","Denied");
                    toastAndAlertBuilder.makeSimpleToast("لطفا دسترسی تماس را به برنامه اضافه نمایید");
                    return;
                }
                startActivity(callIntent);

            }
        });
    }
}
