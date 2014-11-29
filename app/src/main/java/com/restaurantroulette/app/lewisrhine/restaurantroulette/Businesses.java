package com.restaurantroulette.app.lewisrhine.restaurantroulette;

import java.io.Serializable;


// Business object that we build from data we get form the yelp API
public class Businesses implements Serializable {
    private String name;
    private String id;
    private String distance;
    private String categories;
    private String image_url;
    private Double rating;
    private String rating_image_url;
    private Double longitude;
    private Double latitude;



    public Businesses() {
    }


    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getRating_image_url() {
        return rating_image_url;
    }

    public void setRating_image_url(String rating_image_url) {
        this.rating_image_url = rating_image_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
