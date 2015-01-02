
package com.restaurantroulette.app.lewisrhine.restaurantroulette.yelpapi;

import java.io.Serializable;
import java.util.List;

public class Businesses implements Serializable{
   	private List categories;
   	private String display_phone;
   	private Double distance;
   	private String id;
   	private String image_url;
   	private boolean is_claimed;
   	private boolean is_closed;
   	private Location location;
   	private String mobile_url;
   	private String name;
   	private String phone;
   	private Number rating;
   	private String rating_img_url;
   	private String rating_img_url_large;
   	private String rating_img_url_small;
   	private Number review_count;
   	private String snippet_image_url;
   	private String snippet_text;
   	private String url;


    public String getCategoryReadable(){
        //Here I am cutting out the first category so it is nice and human readable.
        String categoryReadable = categories.get(0).toString();
        int start = categoryReadable.indexOf("[");
        int end = categoryReadable.lastIndexOf(",");
        return categoryReadable.substring((start + 1), (end));
    }

    public List getCategories(){
		return this.categories;
	}
	public void setCategories(List categories){
		this.categories = categories;
	}
 	public String getDisplay_phone(){
		return this.display_phone;
	}
	public void setDisplay_phone(String display_phone){
		this.display_phone = display_phone;
	}

    public Double getDistance(){
		return this.distance;
	}

    public Double getDistanceMiles(){
        //Yelp gives meters, so first they have to be converted to miles
        //Double meters = //Double.parseDouble(business.getString("distance"));
        Double miles = distance * 0.00062137119;
        //then rounded up, and return.
        return Math.round(miles * 10.0) / 10.0;

    }

	public void setDistance(Double distance){
		this.distance = distance;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
 	public String getImage_url(){
		return this.image_url;
	}
	public void setImage_url(String image_url){
		this.image_url = image_url;
	}
 	public boolean getIs_claimed(){
		return this.is_claimed;
	}
	public void setIs_claimed(boolean is_claimed){
		this.is_claimed = is_claimed;
	}
 	public boolean getIs_closed(){
		return this.is_closed;
	}
	public void setIs_closed(boolean is_closed){
		this.is_closed = is_closed;
	}
 	public Location getLocation(){
		return this.location;
	}
	public void setLocation(Location location){
		this.location = location;
	}
 	public String getMobile_url(){
		return this.mobile_url;
	}
	public void setMobile_url(String mobile_url){
		this.mobile_url = mobile_url;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
 	public String getPhone(){
		return this.phone;
	}
	public void setPhone(String phone){
		this.phone = phone;
	}
 	public Number getRating(){
		return this.rating;
	}
	public void setRating(Number rating){
		this.rating = rating;
	}
 	public String getRating_img_url(){
		return this.rating_img_url;
	}
	public void setRating_img_url(String rating_img_url){
		this.rating_img_url = rating_img_url;
	}
 	public String getRating_img_url_large(){
		return this.rating_img_url_large;
	}
	public void setRating_img_url_large(String rating_img_url_large){
		this.rating_img_url_large = rating_img_url_large;
	}
 	public String getRating_img_url_small(){
		return this.rating_img_url_small;
	}
	public void setRating_img_url_small(String rating_img_url_small){
		this.rating_img_url_small = rating_img_url_small;
	}
 	public Number getReview_count(){
		return this.review_count;
	}
	public void setReview_count(Number review_count){
		this.review_count = review_count;
	}
 	public String getSnippet_image_url(){
		return this.snippet_image_url;
	}
	public void setSnippet_image_url(String snippet_image_url){
		this.snippet_image_url = snippet_image_url;
	}
 	public String getSnippet_text(){
		return this.snippet_text;
	}
	public void setSnippet_text(String snippet_text){
		this.snippet_text = snippet_text;
	}
 	public String getUrl(){
		return this.url;
	}
	public void setUrl(String url){
		this.url = url;
	}
}
