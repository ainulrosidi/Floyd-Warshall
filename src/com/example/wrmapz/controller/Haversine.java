package com.example.wrmapz.controller;

public class Haversine {
	
	public double haversine1(double lat1, double lng1, double lat2, double lng2) {
//	    double earthRadius = 3958.75;
	    double earthRadius = 6378.1;///KM
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);
	    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)* Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;
	    System.out.println("dLat Math.toRadians(lat2-lat1) "+dLat);
	    System.out.println("dLng Math.toRadians(lng2-lng1) "+dLng);
	    System.out.println("sindLat Math.sin(dLat / 2) "+sindLat);
	    System.out.println("sindLng  Math.sin(dLng / 2) "+sindLng);
	    System.out.println("a Math.pow(sindLat, 2) + Math.pow(sindLng, 2)* Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) "+a);
	    System.out.println("c 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)) "+c);
	    System.out.println("dist earthRadius * c "+dist);
	    
//	    double  meterConversion = 1609.344;
	    return dist;
//	    return (dist*meterConversion);
	    }

}
