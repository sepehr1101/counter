package com.sepehr.sa_sh.abfacounter01.models.sqlLiteTables;
import com.orm.SugarRecord;
import com.sepehr.sa_sh.abfacounter01.models.InterConnection.HighLowViewModel;

/**
 * Created by saeid on 3/9/2017.
 */
public class HighLowModel extends SugarRecord{
    int LOW_CONST_BOUND_MASKOONI;

    int LOW_PERCENT_BOUND_MASKOONI;

    int HIGH_CONST_BOUND_MASKOONI;

    int HIGH_PERCENT_BOUND_MASKOONI;

    int LOW_CONST_BOUND_SAXT;

    int LOW_PERCENT_BOUND_SAXT;

    int HIGH_CONST_BOUND_SAXT;

    int HIGH_PERCENT_BOUND_SAXT;

    int LOW_CONST_ZARFIAT_BOUND;

    int LOW_PERCENT_ZARFIAT_BOUND;

    int HIGH_CONST_ZARFIAT_BOUND;

    int HIGH_PERCENT_ZARFIAT_BOUND;

    int LOW_PERCENT_RATE_BOUND_NON_MASKOONI;
    int HIGH_PERCENT_RATE_BOUND_NON_MASKOONI;

    public HighLowModel() {
    }

    public HighLowModel(HighLowViewModel highLowViewModel) {
        this.setLOW_CONST_BOUND_MASKOONI(highLowViewModel.getLowConstBoundMaskooni());
        this.setLOW_CONST_BOUND_SAXT(highLowViewModel.getLowConstBoundSaxt());
        this.setLOW_CONST_ZARFIAT_BOUND(highLowViewModel.getLowConstZarfiatBound());

        this.setHIGH_CONST_BOUND_MASKOONI(highLowViewModel.getHighConstBoundMaskooni());
        this.setHIGH_CONST_BOUND_SAXT(highLowViewModel.getHighConstBoundSaxt());
        this.setHIGH_CONST_ZARFIAT_BOUND(highLowViewModel.getHighConstZarfiatBound());

        this.setLOW_PERCENT_BOUND_MASKOONI(highLowViewModel.getLowPercentBoundMaskooni());
        this.setLOW_PERCENT_BOUND_SAXT(highLowViewModel.getLowPercentBoundSaxt());
        this.setLOW_PERCENT_RATE_BOUND_NON_MASKOONI(highLowViewModel.getLowPercentRateBoundNonMaskooni());

        this.setHIGH_PERCENT_BOUND_MASKOONI(highLowViewModel.getHighPercentBoundMaskooni());
        this.setHIGH_PERCENT_BOUND_SAXT(highLowViewModel.getHighPercentBoundSaxt());
        this.setHIGH_PERCENT_RATE_BOUND_NON_MASKOONI(highLowViewModel.getHighPercentRateBoundNonMaskooni());

        this.setLOW_PERCENT_ZARFIAT_BOUND(highLowViewModel.getLowPercentZarfiatBound());
        this.setHIGH_PERCENT_ZARFIAT_BOUND(highLowViewModel.getHighPercentZarfiatBound());
    }

    public int getLOW_CONST_BOUND_MASKOONI() {
        return LOW_CONST_BOUND_MASKOONI;
    }

    public void setLOW_CONST_BOUND_MASKOONI(int LOW_CONST_BOUND_MASKOONI) {
        this.LOW_CONST_BOUND_MASKOONI = LOW_CONST_BOUND_MASKOONI;
    }

    public int getLOW_PERCENT_BOUND_MASKOONI() {
        return LOW_PERCENT_BOUND_MASKOONI;
    }

    public void setLOW_PERCENT_BOUND_MASKOONI(int LOW_PERCENT_BOUND_MASKOONI) {
        this.LOW_PERCENT_BOUND_MASKOONI = LOW_PERCENT_BOUND_MASKOONI;
    }

    public int getHIGH_CONST_BOUND_MASKOONI() {
        return HIGH_CONST_BOUND_MASKOONI;
    }

    public void setHIGH_CONST_BOUND_MASKOONI(int HIGH_CONST_BOUND_MASKOONI) {
        this.HIGH_CONST_BOUND_MASKOONI = HIGH_CONST_BOUND_MASKOONI;
    }

    public int getHIGH_PERCENT_BOUND_MASKOONI() {
        return HIGH_PERCENT_BOUND_MASKOONI;
    }

    public void setHIGH_PERCENT_BOUND_MASKOONI(int HIGH_PERCENT_BOUND_MASKOONI) {
        this.HIGH_PERCENT_BOUND_MASKOONI = HIGH_PERCENT_BOUND_MASKOONI;
    }

    public int getLOW_CONST_BOUND_SAXT() {
        return LOW_CONST_BOUND_SAXT;
    }

    public void setLOW_CONST_BOUND_SAXT(int LOW_CONST_BOUND_SAXT) {
        this.LOW_CONST_BOUND_SAXT = LOW_CONST_BOUND_SAXT;
    }

    public int getLOW_PERCENT_BOUND_SAXT() {
        return LOW_PERCENT_BOUND_SAXT;
    }

    public void setLOW_PERCENT_BOUND_SAXT(int LOW_PERCENT_BOUND_SAXT) {
        this.LOW_PERCENT_BOUND_SAXT = LOW_PERCENT_BOUND_SAXT;
    }

    public int getHIGH_CONST_BOUND_SAXT() {
        return HIGH_CONST_BOUND_SAXT;
    }

    public void setHIGH_CONST_BOUND_SAXT(int HIGH_CONST_BOUND_SAXT) {
        this.HIGH_CONST_BOUND_SAXT = HIGH_CONST_BOUND_SAXT;
    }

    public int getHIGH_PERCENT_BOUND_SAXT() {
        return HIGH_PERCENT_BOUND_SAXT;
    }

    public void setHIGH_PERCENT_BOUND_SAXT(int HIGH_PERCENT_BOUND_SAXT) {
        this.HIGH_PERCENT_BOUND_SAXT = HIGH_PERCENT_BOUND_SAXT;
    }

    public int getLOW_CONST_ZARFIAT_BOUND() {
        return LOW_CONST_ZARFIAT_BOUND;
    }

    public void setLOW_CONST_ZARFIAT_BOUND(int LOW_CONST_ZARFIAT_BOUND) {
        this.LOW_CONST_ZARFIAT_BOUND = LOW_CONST_ZARFIAT_BOUND;
    }

    public int getLOW_PERCENT_ZARFIAT_BOUND() {
        return LOW_PERCENT_ZARFIAT_BOUND;
    }

    public void setLOW_PERCENT_ZARFIAT_BOUND(int LOW_PERCENT_ZARFIAT_BOUND) {
        this.LOW_PERCENT_ZARFIAT_BOUND = LOW_PERCENT_ZARFIAT_BOUND;
    }

    public int getHIGH_CONST_ZARFIAT_BOUND() {
        return HIGH_CONST_ZARFIAT_BOUND;
    }

    public void setHIGH_CONST_ZARFIAT_BOUND(int HIGH_CONST_ZARFIAT_BOUND) {
        this.HIGH_CONST_ZARFIAT_BOUND = HIGH_CONST_ZARFIAT_BOUND;
    }

    public int getHIGH_PERCENT_ZARFIAT_BOUND() {
        return HIGH_PERCENT_ZARFIAT_BOUND;
    }

    public void setHIGH_PERCENT_ZARFIAT_BOUND(int HIGH_PERCENT_ZARFIAT_BOUND) {
        this.HIGH_PERCENT_ZARFIAT_BOUND = HIGH_PERCENT_ZARFIAT_BOUND;
    }

    public int getLOW_PERCENT_RATE_BOUND_NON_MASKOONI() {
        return LOW_PERCENT_RATE_BOUND_NON_MASKOONI;
    }

    public void setLOW_PERCENT_RATE_BOUND_NON_MASKOONI(int LOW_PERCENT_RATE_BOUND_NON_MASKOONI) {
        this.LOW_PERCENT_RATE_BOUND_NON_MASKOONI = LOW_PERCENT_RATE_BOUND_NON_MASKOONI;
    }

    public int getHIGH_PERCENT_RATE_BOUND_NON_MASKOONI() {
        return HIGH_PERCENT_RATE_BOUND_NON_MASKOONI;
    }

    public void setHIGH_PERCENT_RATE_BOUND_NON_MASKOONI(int HIGH_PERCENT_RATE_BOUND_NON_MASKOONI) {
        this.HIGH_PERCENT_RATE_BOUND_NON_MASKOONI = HIGH_PERCENT_RATE_BOUND_NON_MASKOONI;
    }
}
