package com.sepehr.sa_sh.abfacounter01;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sepehr.sa_sh.abfacounter01.constants.ReadingListType;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.OnOffLoadModel;

/**
 * Created by sa-sh on 8/12/2016.
 */
public class UnReadMoshtaraksFragment extends Fragment {
    TextView tedadKolText,tedadKolVal,unreadText,unreadVal;
    public UnReadMoshtaraksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_unread_moshtaraks, container, false);
        final Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/BZar.ttf");

        tedadKolText=(TextView)rootView.findViewById(R.id.tedadKolText);
        tedadKolVal=(TextView) rootView.findViewById(R.id.tedadKolVal);
        unreadText=(TextView) rootView.findViewById(R.id.tedadUnreadText);
        unreadVal=(TextView) rootView.findViewById(R.id.tedadUnreadVal);

        tedadKolVal.setTypeface(face);
        tedadKolText.setTypeface(face);
        unreadVal.setTypeface(face);
        unreadText.setTypeface(face);

        long unreadsSize= OnOffLoadModel.count(OnOffLoadModel.class,
                "COUNTER_STATE_CODE IS NULL", null);
        unreadVal.setText(unreadsSize+"");
        long tedadKolSize=((MyWorksReportActivity)getActivity()).getTedadKolSize();
        tedadKolVal.setText(tedadKolSize+"");
        showUnreads(rootView);
        return rootView;
    }
    //
    private void showUnreads(View rootView){
        final Button showUnreads=(Button)rootView.findViewById(R.id.showUnreads);
        showUnreads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDisplayViewpager = new Intent(getContext(), DisplayViewPager.class);
                intentDisplayViewpager.putExtra("s", ReadingListType.UNREAD.getValue());
                startActivity(intentDisplayViewpager);
            }
        });
    }
}
