package com.shrewd.poppydealers.views.activity.intro;

public class ScreenItem {
    String Title, Description, imageUrl;
    int ScreenImg;


    public ScreenItem(String title, String description, int screenImg) {
        Title = title;
        Description = description;
        ScreenImg = screenImg;
    }

    public ScreenItem(String image) {
        imageUrl = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getScreenImg() {
        return ScreenImg;
    }

    public void setScreenImg(int screenImg) {
        ScreenImg = screenImg;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
