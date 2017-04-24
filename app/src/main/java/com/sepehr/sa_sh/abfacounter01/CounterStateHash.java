package com.sepehr.sa_sh.abfacounter01;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by saeid on 10/11/2016.
 */
public final class CounterStateHash {
    //first -> counterStateCode (spinner) , second -> counterStateRealCode(biiling system)
    private static HashMap<BigDecimal, Integer> counterHashMap = new HashMap<BigDecimal, Integer>();
    private static  HashMap<Integer,BigDecimal> counterInverseHashMap=new
            HashMap<Integer,BigDecimal>();

    private static void generateInverse() {
        counterInverseHashMap.put(0, new BigDecimal(0));
        counterInverseHashMap.put(1, new BigDecimal(1));
        counterInverseHashMap.put(4, new BigDecimal(2));
        counterInverseHashMap.put(6, new BigDecimal(3));
        counterInverseHashMap.put(9, new BigDecimal(4));
        counterInverseHashMap.put(10,new BigDecimal(5));
        counterInverseHashMap.put(5, new BigDecimal(6));
        counterInverseHashMap.put(2,new BigDecimal(7));
    }

    private static void generate(){
        counterHashMap.put(new BigDecimal(0),0);//adi
        counterHashMap.put(new BigDecimal(1),1);//nabud
        counterHashMap.put(new BigDecimal(2),4);//jaie na monaseb
        counterHashMap.put(new BigDecimal(3),6);//momaneat
        counterHashMap.put(new BigDecimal(4),9);//az kar oftade
        counterHashMap.put(new BigDecimal(5),10);//faqed
        counterHashMap.put(new BigDecimal(6),5);//pushide
        counterHashMap.put(new BigDecimal(7),2);//maxroobe
    }
    //
    private CounterStateHash() {
        generate();
        generateInverse();
    }
    //
    public static HashMap getHMap(){
        generate();
        return counterHashMap;
    }
    //
    public  static HashMap getHMapInverse(){
        generateInverse();
        return counterInverseHashMap;
    }
}
