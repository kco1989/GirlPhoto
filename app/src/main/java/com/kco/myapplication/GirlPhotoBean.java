package com.kco.myapplication;

/**
 * Created by 666666 on 2018/3/9.
 */
public class GirlPhotoBean {
    private String url;
    private String imageUrl;
    private String name;

    public GirlPhotoBean(String url, String imageUrl, String name) {
        this.url = url;
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public GirlPhotoBean setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public GirlPhotoBean setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getName() {
        return name;
    }

    public GirlPhotoBean setName(String name) {
        this.name = name;
        return this;
    }
}
