package com.example.childcare;

public class ChildModle {

    String name;

    String id;
    String imageUrl;
    ChildLocation mLocation;

    public ChildModle() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ChildLocation getLocation() {
        return mLocation;
    }

    public void setLocation(ChildLocation location) {
        mLocation = location;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
