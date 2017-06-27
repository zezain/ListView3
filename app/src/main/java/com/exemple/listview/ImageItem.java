package com.exemple.listview;

import android.graphics.Bitmap;

//Classe inspirée de http://stacktips.com/tutorials/android/android-gridview-example-building-image-gallery-in-android#9-download-complete-example

public class ImageItem {
    private Bitmap image;
    private String title;

    public ImageItem(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
