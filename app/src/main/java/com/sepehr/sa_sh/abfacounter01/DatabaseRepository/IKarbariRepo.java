package com.sepehr.sa_sh.abfacounter01.DatabaseRepository;

import java.util.List;

/**
 * Created by saeid on 3/13/2017.
 */
public interface IKarbariRepo {
    String getKarbariTitle(int karbariCode);
    List<String> getKarbariGroupTitles();
    List<String> getFilterdKarbaries(int karbariGroupId);
    boolean HasVibrate(int karbariCode);
}
