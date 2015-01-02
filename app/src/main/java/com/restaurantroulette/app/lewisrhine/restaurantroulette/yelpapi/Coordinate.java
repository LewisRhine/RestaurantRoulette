
package com.restaurantroulette.app.lewisrhine.restaurantroulette.yelpapi;

import java.io.Serializable;

public class Coordinate implements Serializable{
   	private Number latitude;
   	private Number longitude;

 	public Number getLatitude(){
		return this.latitude;
	}
	public void setLatitude(Number latitude){
		this.latitude = latitude;
	}
 	public Number getLongitude(){
		return this.longitude;
	}
	public void setLongitude(Number longitude){
		this.longitude = longitude;
	}
}
