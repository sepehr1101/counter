package com.sepehr.sa_sh.abfacounter01.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.rey.material.widget.Spinner;
import com.sepehr.sa_sh.abfacounter01.DisplayViewPager;
import com.sepehr.sa_sh.abfacounter01.R;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by sa-sh on 8/2/2016.
 */
public class SearchFragment extends DialogFragment {

    String[] items = new String[]{"اشتراک","شماره پرونده","شماره بدنه","شناسه ","شماره صفحه","نام"};
    Spinner spinnerSearch;
    EditText searchEditText;
    AutoCompleteTextView searchAutoComplete;
    ProgressBar progressBar;
    Button buttonSearch,exitSearch;
    boolean isNormalList;//if(alal hesab || unread) then false
    Collection<OnOffLoadModel> _list;
    ArrayAdapter<String> adapter2;
    List<String> autoAdaptor;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        initialize(rootView);
        return rootView;
    }
    //
    private void dismissDialog(){

        exitSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    //
    private void initialize(View rootView){
        spinnerSearch=(Spinner) rootView.findViewById(R.id.spinnerSearch);
        searchEditText=(EditText)rootView.findViewById(R.id.searchText);
        searchAutoComplete=(AutoCompleteTextView)rootView.findViewById(R.id.searchAutoComplete);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar5);
        buttonSearch  = (Button) rootView.findViewById(R.id.buttonSearch);
        exitSearch  = (Button) rootView.findViewById(R.id.dismissSearch);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_custom_item, items);
        adapter.setDropDownViewResource(R.layout.spinner_custom_item);
        spinnerSearch.setAdapter(adapter);

        spinnerOnSelectedItem();
        autoCompleteOnTextChange();

        progressBar.setScaleY(7f);
        setViewpagerPosition(rootView);
        searchAutoComplete.setAdapter(adapter2);
        _list=((DisplayViewPager)getActivity()).getTheList();
        isNormalList=false;
        dismissDialog();
    }
    //
    private void autoCompleteOnTextChange(){
        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                autoAdaptor=searchInDB(charSequence+"");
                adapter2 = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, autoAdaptor);
                adapter2.notifyDataSetChanged();
                searchAutoComplete.setAdapter(adapter2);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    //
    private void spinnerOnSelectedItem(){
        spinnerSearch.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner spinner, View view, int i, long l) {
                if(i==5){
                   /* searchEditText.setVisibility(View.GONE);
                    searchAutoComplete.setVisibility(View.VISIBLE);*/
                    Snackbar.make(getView(), "همکار گرامی بدلیل تغییرات زیرساختی نرم افزار ، جستجو با نام در نسخه بعد در دسترس خواهد بود",
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else{
                    searchEditText.setVisibility(View.VISIBLE);
                    searchAutoComplete.setVisibility(View.GONE);
                }
            }
        });
    }
    //
    @Override
    public  void onResume(){

        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);//right to left
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }
    //
    private void setViewpagerPosition(final View rootView){
        try {
            buttonSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String searchText=searchEditText.getText().toString().trim();
                    String autoText=searchAutoComplete.getText().toString().trim();
                    int spinnerSearchSelectedItemPosition=spinnerSearch.getSelectedItemPosition();

                    if(TextUtils.isEmpty(searchText)&&spinnerSearchSelectedItemPosition!=5){
                        //ToDo force user to change value
                        Snackbar.make(getView(), "لطفا در بخش ورود اطلاعات مقداری وارد کنید", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    }
                    if(TextUtils.isEmpty(autoText)&& spinnerSearchSelectedItemPosition==5){
                        //ToDo force user to change value
                        Snackbar.make(getView(), "لطفا در بخش ورود اطلاعات مقداری وارد کنید", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    }
                    if(!TextUtils.isDigitsOnly(searchText) && spinnerSearchSelectedItemPosition!=5){
                        Snackbar.make(getView(), "لطفا فقط کاراکتر های عددی وارد کنید", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    }
                    BackGroundSearch task=new BackGroundSearch();
                    task.initialize(_list, progressBar, spinnerSearchSelectedItemPosition);
                    if(spinnerSearchSelectedItemPosition==5){
                        searchText=autoText;
                    }
                    task.execute(searchText);
                }
            });
        }
        catch (Exception e){
            Log.e("error:",e.getCause()+" msg:"+e.getMessage());
        }
    }
    //
    private int getIndexOf(Collection<OnOffLoadModel> list,String billId){
        int i=0;
        for (OnOffLoadModel counterReading:list ) {
            if( counterReading.billId.trim().equals(billId.trim())){
                return i;
            }
            i++;
        }
        Log.e("search by bill_id", " not found");
        return -1;
    }
    //
    private int getIndexOf(Collection<OnOffLoadModel> list,String eshterak,boolean isEshterak){
        int i=0;
        for (OnOffLoadModel counterReading:list ) {
            if( counterReading.eshterak.trim().contains(eshterak.trim())){
                return i;
            }
            i++;
        }
        Log.e("search by eshterak", " not found");
        return -1;
    }
    //
    private int searchByRadif(Collection<OnOffLoadModel> list,String radif){
        int i=0;
        BigInteger radifInt=new BigInteger(radif);
        for (OnOffLoadModel listItem:list ) {
            if( listItem.getRadif().toBigInteger().equals(radifInt)){
                return i;
            }
            i++;
        }
        Log.e("search by radif", " not found");
        return -1;
    }
    //
    private int searchByCounterSerial(Collection<OnOffLoadModel> list,String counterSerial){
        int i=0;
        for (OnOffLoadModel listItem:list ) {
            if( listItem.counterSerial.trim().contains(counterSerial.trim())){
                return i;
            }
            i++;
        }
        Log.e("search by counterSerial", " not found");
        return -1;
    }
    //
    private List<String> searchInDB(String nameAndFamily){
        String query="SELECT  * "+
        "FROM ON_OFF_LOAD_MODEL "+
        "WHERE (NAME||\' \'||FAMILY) LIKE \'%"+nameAndFamily+"%\'";
        //String query="SELECT * FROM COUNTER_READING_MODEL01 WHERE FAMILY LIKE?";
        List<OnOffLoadModel> searchResultObjects=OnOffLoadModel.findWithQuery(OnOffLoadModel.class,query,null);
        List<String> searchResult=new ArrayList<>();
        for (OnOffLoadModel result:searchResultObjects) {
            searchResult.add(result.name+" "+result.family);
        }
        return searchResult;
    }
    //
    private class BackGroundSearch extends AsyncTask<String, Integer, Integer> {
        ProgressBar progressBar;
        Integer spinnerPosition;
        Collection<OnOffLoadModel> _list;
        int position=-1;
        List<OnOffLoadModel> myWorks;

        public void initialize(Collection<OnOffLoadModel> list,ProgressBar progressBar,Integer spinnerPosition){
            this._list=list;
            this.progressBar = progressBar;
            this.spinnerPosition=spinnerPosition;
        }

        @Override
        protected Integer doInBackground(String... params) {
            //region search in db directly
           if(isNormalList) {
               switch (spinnerPosition) {
                   case 0:
                       String eshterak = params[0];
                       myWorks = OnOffLoadModel
                               .find(OnOffLoadModel.class, "Eshterak= ? ", eshterak);
                       if (myWorks != null && myWorks.size() > 0) {
                           position = myWorks.get(0)._index.intValue();
                           Log.e("search position:", position + "");
                       }
                       break;
                   case 1:

                       break;
                   case 2:

                       break;
                   case 3:
                       String bill_id = params[0];
                       myWorks = OnOffLoadModel
                               .find(OnOffLoadModel.class, "BILL_ID= ? ", bill_id);
                       if (myWorks != null && myWorks.size() > 0) {
                           position = myWorks.get(0)._index.intValue();
                           Log.e("search position:", position + "");
                       }
                       break;
                   case 4:
                       String _position = params[0];
                       myWorks = OnOffLoadModel
                               .find(OnOffLoadModel.class, "_POSITION= ? ", _position);
                       if (myWorks != null && myWorks.size() > 0) {
                           position = myWorks.get(0)._index.intValue() + 1;
                           Log.e("search position:", position + "");
                       }
                       break;
                   case 5:
                   String nameAndFamily=params[0];
                       myWorks = OnOffLoadModel
                               .find(OnOffLoadModel.class, "(NAME||\' \'||FAMILY) LIKE\'%"+nameAndFamily+"%\'", null);
                       if (myWorks != null && myWorks.size() > 0) {
                           position = myWorks.get(0)._index.intValue() + 1;
                           Log.e("search position:", position + "");
                       }
               }
           }
           //endregion

            else{//listIsNormal=false (alalhesab ,unread)
               switch (spinnerPosition){
                   case 0:
                       String eshterak = params[0];
                       position= getIndexOf(this._list,eshterak,true);
                       break;
                   case 1:
                       String radif=params[0];
                       position=searchByRadif(this._list,radif);
                       break;
                   case 2:
                        String counterSerial=params[0];
                       position=searchByCounterSerial(this._list,counterSerial);
                       break;
                   case 3:
                       String bill_id = params[0];
                       position=getIndexOf(this._list,bill_id);
                       break;
                   case 4:
                        try{
                            int viewpagerPosition=new Integer(params[0]).intValue();
                            position=viewpagerPosition-1;
                        }
                        catch(Exception e){
                            position=-1;
                        }
                       break;
               }
            }
            return position;
        }

        @Override
        protected void onPostExecute(Integer position) {
            progressBar.setVisibility(View.GONE);
            if(position>=0){
                ((DisplayViewPager) getActivity()).setViewPagerCurrentItem(position);
                dismiss();
            }
            else {
                Snackbar.make(getView(), "چنین موردی پیدا نشد ، لطفا دوباره امتحان فرمایید", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}
