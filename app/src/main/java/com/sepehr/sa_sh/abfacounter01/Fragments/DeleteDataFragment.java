package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.orm.SugarTransactionHelper;
import com.sepehr.sa_sh.abfacounter01.Crypto;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ISharedPreferenceManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.IToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.infrastructure.SharedPreferenceManager;
import com.sepehr.sa_sh.abfacounter01.infrastructure.ToastAndAlertBuilder;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CounterReportValueKeyModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.CounterStateValueKeyModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.HighLowModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.KarbariModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.ReadingConfigModel;

/**
 * Created by saeid on 5/25/2017.
 */

public class DeleteDataFragment extends Fragment {
    EditText passwordEditText;
    Button deleteButton;
    ProgressBar deleteProgressBar;

    ISharedPreferenceManager sharedPreferenceManager;
    IToastAndAlertBuilder toastAndAlertBuilder;
    String password;

    public DeleteDataFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_delete_all_data, container, false);
        initialize(rootView);
        return rootView;
    }

    private void initialize(View rootView){
        passwordEditText=(EditText)rootView.findViewById(R.id.deletePass);
        deleteButton=(Button)rootView.findViewById(R.id.deleteButton);
        deleteProgressBar=(ProgressBar)rootView.findViewById(R.id.progressBarDelete);
        sharedPreferenceManager=new SharedPreferenceManager(getContext());
        toastAndAlertBuilder=new ToastAndAlertBuilder(getContext());
        toastAndAlertBuilder.makeSimpleAlert("همکار گرامی در صورت تایید تمام اطلاعات دستگاه قرائت شما حذف خواهد شد");
        deleteClickListener();
    }

    private void deleteClickListener(){
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password=passwordEditText.getText().toString();
                if(!isPasswordCorrect(password)){
                    toastAndAlertBuilder.makeSimpleAlert("پسوورد صحیح نمیباشد لطفا دوباره امتحان نمایید");
                }else {
                    doDelete();
                    toastAndAlertBuilder.makeSimpleAlert("اطلاعات با موفقیت حذف شد");
                }
            }
        });
    }

    private boolean isPasswordCorrect(String password){
        if(password==null || password.length()<1){
            return false;
        }
        String prefPassword= Crypto.decrypt(sharedPreferenceManager.getString("password"));
        if(prefPassword.equals(password)){
            return true;
        }
        return false;
    }

    private void doDelete(){
        SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
            @Override
            public void manipulateInTransaction() {
                CounterStateValueKeyModel.deleteAll(CounterStateValueKeyModel.class);
                CounterReportValueKeyModel.deleteAll(CounterReportValueKeyModel.class);
                KarbariModel.deleteAll(KarbariModel.class);
                HighLowModel.deleteAll(HighLowModel.class);
                OnOffLoadModel.deleteAll(OnOffLoadModel.class);
                ReadingConfigModel.deleteAll(ReadingConfigModel.class);
            }
        });
    }
}
