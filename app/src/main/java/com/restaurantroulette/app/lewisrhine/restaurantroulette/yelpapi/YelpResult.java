
package com.restaurantroulette.app.lewisrhine.restaurantroulette.yelpapi;

import java.util.ArrayList;
import java.util.List;

public class YelpResult{
   	private ArrayList<Businesses> businesses;
   	private Region region;
   	private Number total;

 	public ArrayList<Businesses> getBusinesses(){
		return this.businesses;
	}
	public void setBusinesses(ArrayList<Businesses> businesses){
		this.businesses = businesses;
	}
 	public Region getRegion(){
		return this.region;
	}
	public void setRegion(Region region){
		this.region = region;
	}
 	public Number getTotal(){
		return this.total;
	}
	public void setTotal(Number total){
		this.total = total;
	}
}
