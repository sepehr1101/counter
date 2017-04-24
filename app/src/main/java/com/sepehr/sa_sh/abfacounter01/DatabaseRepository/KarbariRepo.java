package com.sepehr.sa_sh.abfacounter01.DatabaseRepository;

import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.KarbariGroup;
import com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables.KarbariModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by saeid on 3/13/2017.
 */
public class KarbariRepo implements IKarbariRepo{
    List<String> karbariGroupTitles;
    List<String> karbariTitles;

    public KarbariRepo() {
        karbariGroupTitles=new ArrayList<>();
        karbariTitles=new ArrayList<>();
    }

    public List<String> getKarbariGroupTitles(){
        List<KarbariGroup> karbariGroups=KarbariGroup.listAll(KarbariGroup.class);
        karbariGroupTitles.add("انتخاب کنید");
        for (KarbariGroup k:karbariGroups) {
            karbariGroupTitles.add(k.getGroupTitle());
        }
        return karbariGroupTitles;
    }
    //
    public List<String> getFilterdKarbaries(int karbariGroupId){
        List<KarbariModel> karbariesFilterd= KarbariModel
                .find(KarbariModel.class, "GROUP2= ? ", karbariGroupId + "");
        for(KarbariModel k:karbariesFilterd){
            karbariTitles.add(k.getTITLE());
        }
        return karbariTitles;
    }
    //
    public String getKarbariTitle(int karbariCode){
        String karbariTitle = KarbariModel.find(KarbariModel.class, "CODE= ? ", karbariCode + "")
                .get(0).getTITLE();
        return karbariTitle;
    }
    //
    public boolean HasVibrate(int karbariCode){
        KarbariModel karbariInfo= KarbariModel.find(KarbariModel.class,
                "CODE= ? ", karbariCode + "")
                .get(0);
        return karbariInfo.isHAS_VIBRATE();
    }
}
