package com.shrewd.poppydealers.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CustomModal{
    List<BedSizeModal> accesSizes;
    List<BedSizeModal>bedSizeModals;
    List<BedSizeModal>Incheslist;

    public CustomModal(List<BedSizeModal> accesSizes, List<BedSizeModal> bedSizeModals, List<BedSizeModal> incheslist) {
        this.accesSizes = accesSizes;
        this.bedSizeModals = bedSizeModals;
        Incheslist = incheslist;
    }




    public List<BedSizeModal> getAccesSizes() {
        return accesSizes;
    }

    public void setAccesSizes(List<BedSizeModal> accesSizes) {
        this.accesSizes = accesSizes;
    }

    public List<BedSizeModal> getBedSizeModals() {
        return bedSizeModals;
    }

    public void setBedSizeModals(List<BedSizeModal> bedSizeModals) {
        this.bedSizeModals = bedSizeModals;
    }

    public List<BedSizeModal> getIncheslist() {
        return Incheslist;
    }

    public void setIncheslist(List<BedSizeModal> incheslist) {
        Incheslist = incheslist;
    }


}
