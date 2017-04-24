package com.sepehr.sa_sh.abfacounter01;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

/**
 * Created by Sepehr on 3/3/2017.
 */
public class CounterSerialFragment extends DialogFragment {
    String bill_id;
    Button dismiss,ok;
    EditText counterSerial;
    IToastAndAlertBuilder toastAndAlertBuilder;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);//right to left
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.fragment_counter_serial, container, false);
        initialize(rootView);
        return rootView;
    }

    private void initialize(View rootView){
        toastAndAlertBuilder=new ToastAndAlertBuilder(getContext());
        bill_id=((DisplayViewPager)getActivity()).getBill_id();
        counterSerial=(EditText)rootView.findViewById(R.id.counterSerial);
        ok=(Button)rootView.findViewById(R.id.ok);
        dismiss=(Button)rootView.findViewById(R.id.dismiss);
        buttonsClickListeners();
    }

    private void buttonsClickListeners(){
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
    }

    private void confirm(){
        OnOffLoadModel model = OnOffLoadModel.find(OnOffLoadModel.class,
                "BILL_ID= ? ", bill_id).get(0);
        model.possibleCounterSerial=counterSerial.getText().toString();
        model.save();
        toastAndAlertBuilder.makeSimpleToast("اطلاعات ذخیره شد");
    }

    @Override
    public  void onResume(){
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }
}
