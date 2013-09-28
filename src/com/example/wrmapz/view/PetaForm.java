package com.example.wrmapz.view;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;

import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.wrmapz.R;

public class PetaForm {
	
	private TextView locationManager1, locationManager2, locationManager3,
	textViewKeterangan;


	private ToggleButton toggleButtonsnapToLocationView;
	private Button buttonUndo, rekam;

	public Button getRekam() {
		return rekam;
	}

	public void setRekam(Button rekam) {
		this.rekam = rekam;
	}

	private MapView mapView;		
	private MapActivity mapactivity;

	public PetaForm(MapActivity mapactivity) {
		super();
		this.setMapactivity(mapactivity);
		setLocationManager1((TextView) mapactivity.findViewById(R.id.locationManager));
		setTextViewKeterangan((TextView) mapactivity.findViewById(R.id.TextViewKeterangan));
		setLocationManager2((TextView) mapactivity.findViewById(R.id.locationManager1));
		setLocationManager3((TextView) mapactivity.findViewById(R.id.locationManager2));
		setToggleButtonsnapToLocationView((ToggleButton) mapactivity.findViewById(R.id.toggleButtonSnapToLocationView));
		setButtonUndo((Button) mapactivity.findViewById(R.id.button_undo));
		setRekam((Button) mapactivity.findViewById(R.id.button_rekam));
		setMapView((MapView) mapactivity.findViewById(R.id.mapView));
		reset();
	}

	public MapActivity getMapactivity() {
		return mapactivity;
	}

	public void setMapactivity(MapActivity mapactivity) {
		this.mapactivity = mapactivity;
	}

	public void reset(){
		getLocationManager1().setText("");
		getLocationManager2().setText("");
		getLocationManager3().setText("");
		getTextViewKeterangan().setText("");
	}

	public TextView getLocationManager1() {
		return locationManager1;
	}

	public void setLocationManager1(TextView locationManager1) {
		this.locationManager1 = locationManager1;
	}

	public TextView getLocationManager2() {
		return locationManager2;
	}

	public void setLocationManager2(TextView locationManager2) {
		this.locationManager2 = locationManager2;
	}

	public TextView getLocationManager3() {
		return locationManager3;
	}

	public void setLocationManager3(TextView locationManager3) {
		this.locationManager3 = locationManager3;
	}

	public ToggleButton getToggleButtonsnapToLocationView() {
		return toggleButtonsnapToLocationView;
	}

	public void setToggleButtonsnapToLocationView(
			ToggleButton toggleButtonsnapToLocationView) {
		this.toggleButtonsnapToLocationView = toggleButtonsnapToLocationView;
	}

	public MapView getMapView() {
		return mapView;
	}

	public void setMapView(MapView mapView) {
		this.mapView = mapView;
	}

	public Button getButtonUndo() {
		return buttonUndo;
	}

	public void setButtonUndo(Button buttonUndo) {
		this.buttonUndo = buttonUndo;
	}
	public TextView getTextViewKeterangan() {
		return textViewKeterangan;
	}

	public void setTextViewKeterangan(TextView textViewKeterangan) {
		this.textViewKeterangan = textViewKeterangan;
	}

}
