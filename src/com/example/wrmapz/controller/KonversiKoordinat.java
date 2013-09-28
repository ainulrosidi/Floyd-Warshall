package com.example.wrmapz.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.location.Location;

public class KonversiKoordinat {
	
	public String konversiKoordinatLatitude(Location location){
		int derajatLatitude=0;
		int minuteLatitude=0;
		double secondLatitude=0;
		double latitude1=0;
		String posisiLatitude="";
		
				
		if(location.getLatitude()<0){
			derajatLatitude=(int) location.getLatitude()*-1;
			double derajatLatitude1= location.getLatitude()%-1;
			minuteLatitude=(int)(derajatLatitude1 *-60);
			double minuteLatitude1=(derajatLatitude1*60) % -1;				
			secondLatitude= ( minuteLatitude1*-60);					
			latitude1 = Double.parseDouble(String.valueOf(new BigDecimal(secondLatitude).setScale(5,RoundingMode.HALF_EVEN)));
			posisiLatitude="South";
		}
		else if(location.getLatitude()>0){
			derajatLatitude=(int) location.getLatitude()*1;
			double derajatLatitude1= location.getLatitude()%1;
			minuteLatitude=(int)(derajatLatitude1 *60);
			double minuteLatitude1=(derajatLatitude1*60)%1;				
			secondLatitude=( minuteLatitude1*60);					
			latitude1 = Double.parseDouble(String.valueOf(new BigDecimal(secondLatitude).setScale(5,RoundingMode.HALF_EVEN)));
			posisiLatitude="North";
		}
		
		return "(d:"+String.valueOf(derajatLatitude)+" m:"+String.valueOf(minuteLatitude)+" s:"+String.valueOf(latitude1)+" "+posisiLatitude+")";
	
	}
	
	public String konversiKoordinatLongitude(Location location){
		
		
		int derajatLongitude=0;
		int minuteLongitude=0;
		double secondLongitude=0;
		double longitude1=0;
		String posisiLongitude="";
		
		
			
		if(location.getLongitude()<0){
			derajatLongitude=(int) location.getLongitude()*-1;
			double derajatLongitude1= location.getLongitude()%-1;
			minuteLongitude=(int)(derajatLongitude1 *-60);
			double minuteLongitude1=(derajatLongitude1*60) % -1;				
			secondLongitude=( minuteLongitude1*-60);					
			longitude1 = Double.parseDouble(String.valueOf(new BigDecimal(secondLongitude).setScale(5,RoundingMode.HALF_EVEN)));
			posisiLongitude="West";
		}
		else if(location.getLongitude()>0){
			derajatLongitude=(int) location.getLongitude()*1;
			double derajatLongitude1= location.getLongitude()%1;
			minuteLongitude=(int)(derajatLongitude1 *60);
			double minuteLongitude1=(derajatLongitude1*60) % 1;				
			secondLongitude=( minuteLongitude1*60);					
			longitude1 = Double.parseDouble(String.valueOf(new BigDecimal(secondLongitude).setScale(5,RoundingMode.HALF_EVEN)));
			posisiLongitude="East";

		}
		
		return "(d:"+String.valueOf(derajatLongitude)+" m:"+String.valueOf(minuteLongitude)+" s:"+String.valueOf(longitude1)+" "+posisiLongitude+")";
	}


}
