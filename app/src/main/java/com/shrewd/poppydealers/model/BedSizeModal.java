package com.shrewd.poppydealers.model;

public class BedSizeModal {
    String Tittle;
    int Image;

    public BedSizeModal(String tittle, int image) {
        Tittle = tittle;
        Image = image;
    }

    public String getTittle() {
        return Tittle;
    }

    public void setTittle(String tittle) {
        Tittle = tittle;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}
