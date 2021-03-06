
package com.restaurantroulette.app.lewisrhine.restaurantroulette.yelpapi;

import java.io.Serializable;
import java.util.List;

public class Location implements Serializable{
   	private List address;
   	private String city;
   	private Coordinate coordinate;
   	private String country_code;
   	private String cross_streets;
   	private List display_address;
   	private Number geo_accuracy;
   	private String postal_code;
   	private String state_code;

 	public List getAddress(){
		return this.address;
	}
	public void setAddress(List address){
		this.address = address;
	}
 	public String getCity(){
		return this.city;
	}
	public void setCity(String city){
		this.city = city;
	}
 	public Coordinate getCoordinate(){
		return this.coordinate;
	}
	public void setCoordinate(Coordinate coordinate){
		this.coordinate = coordinate;
	}
 	public String getCountry_code(){
		return this.country_code;
	}
	public void setCountry_code(String country_code){
		this.country_code = country_code;
	}
 	public String getCross_streets(){
		return this.cross_streets;
	}
	public void setCross_streets(String cross_streets){
		this.cross_streets = cross_streets;
	}
 	public List getDisplay_address(){
		return this.display_address;
	}
	public void setDisplay_address(List display_address){
		this.display_address = display_address;
	}
 	public Number getGeo_accuracy(){
		return this.geo_accuracy;
	}
	public void setGeo_accuracy(Number geo_accuracy){
		this.geo_accuracy = geo_accuracy;
	}
 	public String getPostal_code(){
		return this.postal_code;
	}
	public void setPostal_code(String postal_code){
		this.postal_code = postal_code;
	}
 	public String getState_code(){
		return this.state_code;
	}
	public void setState_code(String state_code){
		this.state_code = state_code;
	}
}
