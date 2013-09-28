package id.ac.itats.skripsi.routingengine.ainul;

import id.ac.itats.skripsi.ainul.NodeMaster;
import id.ac.itats.skripsi.databuilder.GraphAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mapsforge.android.AndroidUtils;
import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.overlay.ListOverlay;
import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.android.maps.overlay.PolygonalChain;
import org.mapsforge.android.maps.overlay.Polyline;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.map.reader.header.FileOpenResult;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.widget.Toast;

import com.example.wrmapz.R;

public class TestActivity extends MapActivity {
	protected static final String TAG = "TestActivity";
	private final GraphAdapter graphAdapter = new GraphAdapter();
	
	private String hasil1="";
	private File MAP_FILE = new File(Environment.getExternalStorageDirectory().getPath(), "surabaya.map");
	private ListOverlay listOverlay = new ListOverlay();
	private List<OverlayItem> overlayItems = listOverlay.getOverlayItems();
	private MapView mapView;
	private LocationManager locationManager;
	ListOverlay listOverlay4 = new ListOverlay();
	List<OverlayItem> overlayItems4 = listOverlay4.getOverlayItems();
	ListOverlay listOverlay2 = new ListOverlay();
	List<OverlayItem> overlayItems2 = listOverlay2.getOverlayItems();
	private  ArrayList<Koordinat> koordinat = new ArrayList<Koordinat>();
	private Koordinat koordinat1;	
	
//	private static final String BUNDLE_SHOW_MY_LOCATION = "showMyLocation";
//	private static final String BUNDLE_SNAP_TO_LOCATION = "snapToLocation";
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1/1000; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
	
	public String nodeAsal="270391759";
	public String nodeTujuan="1685014141";
	
//	public String nodeAsal="1685011993";
//	public String nodeTujuan="1685012114";
	
//	private TextView locationManager1, locationManager2, locationManager3;
//	private ToggleButton toggleButtonsnapToLocationView;
//	GPSLocationListener locationListener = new GPSLocationListener();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.peta_form);
//		setContentView(R.layout.peta_form);
//		locationManager1 = (TextView) findViewById(R.id.locationManager);
//		locationManager3 = (TextView) findViewById(R.id.locationManager2);
//		locationManager2 = (TextView) findViewById(R.id.locationManager1);
//		toggleButtonsnapToLocationView=(ToggleButton) findViewById(R.id.toggleButtonSnapToLocationView);

//		mapView = (MapView) findViewById(R.id.mapView);

		floydWarshall();

		mapView = new MapView(this);
		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);
		
		FileOpenResult fileOpenResult = mapView.setMapFile(MAP_FILE);
		if (!fileOpenResult.isSuccess()) {
			Toast.makeText(this, fileOpenResult.getErrorMessage(), Toast.LENGTH_LONG).show();
			finish();
		}
		
		// ---use the LocationManager class to obtain GPS locations---
	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						
			 locationManager.requestLocationUpdates(
		                LocationManager.GPS_PROVIDER, 
		                MINIMUM_TIME_BETWEEN_UPDATES, 
		                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
		                new GPSLocationListener()
		        );
		} else {
			tampilkanAlertGPS();	
			
		}
		
		setContentView(mapView);
//		rute();
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// XXX TESTTEST
	private void floydWarshall() {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				graphAdapter.createDatabase();
				graphAdapter.open();
//				graphAdapter.menampilanRuteDanJarak(nodeAsal , nodeTujuan);								
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				System.out.println("PATH : ");
				for(NodeMaster nodeMaster : graphAdapter.getShortestPath()){
					System.out.println(nodeMaster.getIdNodePeta() +" -> " + nodeMaster.getLatitude() + " , "+ nodeMaster.getLongitude());
					hasil1=hasil1+nodeMaster.getIdNodePeta() +" -> " + nodeMaster.getLatitude() + " , "+ nodeMaster.getLongitude()+"\n";

					if(nodeMaster.getIdNodePeta().equals(nodeAsal))
					{
						GeoPoint mapCenter1 = new GeoPoint(Double.parseDouble(nodeMaster.getLatitude()),Double.parseDouble(nodeMaster.getLongitude()));
						Marker marker2 = createMarker(R.drawable.marker_green,mapCenter1);
						overlayItems.add(marker2);				
						mapView.getOverlays().add(listOverlay);
					}
					else if(nodeMaster.getIdNodePeta().equals(nodeTujuan)){
						GeoPoint mapCenter2 = new GeoPoint(Double.parseDouble(nodeMaster.getLatitude()),Double.parseDouble(nodeMaster.getLongitude()));
						Marker marker3 = createMarker(R.drawable.marker_yellow,mapCenter2);
						overlayItems.add(marker3);				
						mapView.getOverlays().add(listOverlay);
					}
					else{
						GeoPoint mapCenter = new GeoPoint(Double.parseDouble(nodeMaster.getLatitude()),Double.parseDouble(nodeMaster.getLongitude()));
						Marker marker1 = createMarker(R.drawable.marker_blue1,mapCenter);
						overlayItems.add(marker1);				
						mapView.getOverlays().add(listOverlay);
					}
					koordinat1 = new Koordinat();
				    koordinat1.setLatitude(Double.parseDouble(nodeMaster.getLatitude()));
				    koordinat1.setLongitude(Double.parseDouble(nodeMaster.getLongitude()));
				    koordinat.add(koordinat1);	

				}
				
				for (int a=1;a<koordinat.size();a++){					
					overlayItems4.add(createPolyline1(
							new GeoPoint(koordinat.get((a-1)).getLatitude(), koordinat.get((a-1)).getLongitude()), 
							new GeoPoint(koordinat.get(a).getLatitude(), koordinat.get(a).getLongitude())));
					mapView.getOverlays().add(listOverlay4);
					mapView.getOverlayController().redrawOverlays();//methode untuk mempercepat menampilkan hasil
				}
			}
			
		}.execute();
	}
	private Marker createMarker(int resourceIdentifier, GeoPoint geoPoint) {
		Drawable drawable = getResources().getDrawable(resourceIdentifier);		
		return new Marker(geoPoint, Marker.boundCenterBottom(drawable));
	}
	
	private class GPSLocationListener implements LocationListener {
		// Menampilkan Update Lokasi GPS terkini
		@Override
		public void onLocationChanged(Location loc) {
			if (loc != null) {
				GeoPoint mapCenter = new GeoPoint(loc.getLatitude(),loc.getLongitude());	
				Marker marker1 = createMarker(R.drawable.marker_green,mapCenter);
				Marker marker2 = createMarker(R.drawable.ic_maps_indicator_current_position_anim1, mapCenter);
				overlayItems2.removeAll(overlayItems2);
				overlayItems2.add(marker1);
				overlayItems2.add(marker2);
				mapView.getOverlays().add(listOverlay2);
				mapView.getOverlayController().redrawOverlays();//methode untuk mempercepat menampilkan hasil
				
//				locationManager1.setText("Lokasiku Lat: " + loc.getLatitude()+ " Long: " +loc.getLongitude()+ ")");				
//				locationManager3.setText("Ketinggian diatas permukaan laut M:"
//								+String.valueOf(new BigDecimal(loc.getAltitude()).setScale(5,RoundingMode.HALF_EVEN))
//								+" Keakuratan Meter:"+loc.getAccuracy()
//								+" Privider GPS:"+loc.getProvider()
//								);
			}
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			showToastOnUiThread("GPS Provider status Disable");
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			showToastOnUiThread("GPS Provider status Enable");		
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
//			showToastOnUiThread("Location status changed");
		}

}
	private Polyline createPolyline1(GeoPoint geoPoint, GeoPoint geoPoint1) {
		List<GeoPoint> geoPoints = Arrays.asList(geoPoint, geoPoint1);
		PolygonalChain polygonalChain = new PolygonalChain(geoPoints);
		Paint paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintStroke.setStyle(Paint.Style.STROKE);
//		paintStroke.setColor(Color.MAGENTA);
		paintStroke.setColor(Color.BLACK);
		paintStroke.setAlpha(128);
		paintStroke.setStrokeWidth(5);
		paintStroke.setPathEffect(new DashPathEffect(new float[] { 25, 15 }, 0));
		return new Polyline(polygonalChain, paintStroke);
	}

	private void tampilkanAlertGPS() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage("GPS belum diaktifkan. Apakah anda akan mengakstifkan konfigurasi GPS?").setCancelable(false)
				.setPositiveButton("Pilih Settings untuk mengaktifkan GPS",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(callGPSSettingIntent);
							}
						});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
	
	public void showToastOnUiThread(final String text) {
		if (AndroidUtils.currentThreadIsUiThread()) {
			Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
			toast.show();
		} else {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast toast = Toast.makeText(TestActivity.this, text,Toast.LENGTH_LONG);
					toast.show();
				}
			});
		}
	}
	
	public void rute(){
		GeoPoint mapCenter = new GeoPoint(Double.parseDouble("-7.2878182"),
				Double.parseDouble("112.7709266"));
		Marker marker = createMarker(R.drawable.marker_green,mapCenter);
		overlayItems.add(marker);				
		mapView.getOverlays().add(listOverlay);
		
		koordinat1 = new Koordinat();
	    koordinat1.setLatitude(Double.parseDouble("-7.2878182"));
	    koordinat1.setLongitude(Double.parseDouble("112.7709266"));
	    koordinat.add(koordinat1);		    
		
		GeoPoint mapCenter1 = new GeoPoint(Double.parseDouble("-7.2866954"),
				Double.parseDouble("112.7710961"));
		Marker marker1 = createMarker(R.drawable.marker_blue1,mapCenter1);
		overlayItems.add(marker1);				
		mapView.getOverlays().add(listOverlay);
		
		koordinat1 = new Koordinat();
	    koordinat1.setLatitude(Double.parseDouble("-7.2866954"));
	    koordinat1.setLongitude(Double.parseDouble("112.7710961"));
	    koordinat.add(koordinat1);
		
		GeoPoint mapCenter2 = new GeoPoint(Double.parseDouble("-7.283902"),
				Double.parseDouble("112.771513"));
		Marker marker2 = createMarker(R.drawable.marker_blue1,mapCenter2);
		overlayItems.add(marker2);				
		mapView.getOverlays().add(listOverlay);
		
		koordinat1 = new Koordinat();
	    koordinat1.setLatitude(Double.parseDouble("-7.283902"));
	    koordinat1.setLongitude(Double.parseDouble("112.771513"));
	    koordinat.add(koordinat1);
	    
		GeoPoint mapCenter3 = new GeoPoint(Double.parseDouble("-7.2819292"),
				Double.parseDouble("112.7718749"));
		Marker marker3 = createMarker(R.drawable.marker_blue1,mapCenter3);
		overlayItems.add(marker3);				
		mapView.getOverlays().add(listOverlay);
		
		koordinat1 = new Koordinat();
	    koordinat1.setLatitude(Double.parseDouble("-7.2819292"));
	    koordinat1.setLongitude(Double.parseDouble("112.7718749"));
	    koordinat.add(koordinat1);
	    
		GeoPoint mapCenter4 = new GeoPoint(Double.parseDouble("-7.2813307"),
				Double.parseDouble("112.7718967"));
		Marker marker4 = createMarker(R.drawable.marker_blue1,mapCenter4);
		overlayItems.add(marker4);				
		mapView.getOverlays().add(listOverlay);
		
		koordinat1 = new Koordinat();
	    koordinat1.setLatitude(Double.parseDouble("-7.2813307"));
	    koordinat1.setLongitude(Double.parseDouble("112.7718967"));
	    koordinat.add(koordinat1);
	    
		GeoPoint mapCenter5 = new GeoPoint(Double.parseDouble("-7.2805617"),
				Double.parseDouble("112.772022"));
		Marker marker5 = createMarker(R.drawable.marker_blue1,mapCenter5);
		overlayItems.add(marker5);				
		mapView.getOverlays().add(listOverlay);
		
		koordinat1 = new Koordinat();
	    koordinat1.setLatitude(Double.parseDouble("-7.2805617"));
	    koordinat1.setLongitude(Double.parseDouble("112.772022"));
	    koordinat.add(koordinat1);
	    
		GeoPoint mapCenter6 = new GeoPoint(Double.parseDouble("-7.2802241"),
				Double.parseDouble("112.7687946"));
		Marker marker6 = createMarker(R.drawable.marker_blue1,mapCenter6);
		overlayItems.add(marker6);				
		mapView.getOverlays().add(listOverlay);
		
		koordinat1 = new Koordinat();
	    koordinat1.setLatitude(Double.parseDouble("-7.2802241"));
	    koordinat1.setLongitude(Double.parseDouble("112.7687946"));
	    koordinat.add(koordinat1);
		
		GeoPoint mapCenter7 = new GeoPoint(Double.parseDouble("-7.2799872"),
				Double.parseDouble("112.7665459"));
		Marker marker7 = createMarker(R.drawable.marker_blue1,mapCenter7);
		overlayItems.add(marker7);				
		mapView.getOverlays().add(listOverlay);
		
		koordinat1 = new Koordinat();
	    koordinat1.setLatitude(Double.parseDouble("-7.2799872"));
	    koordinat1.setLongitude(Double.parseDouble("112.7665459"));
	    koordinat.add(koordinat1);
	    
	    GeoPoint mapCenter8 = new GeoPoint(Double.parseDouble("-7.2794226"),
				Double.parseDouble("112.7622392"));
		Marker marker8 = createMarker(R.drawable.marker_yellow,mapCenter8);
		overlayItems.add(marker8);				
		mapView.getOverlays().add(listOverlay);
		
		koordinat1 = new Koordinat();
	    koordinat1.setLatitude(Double.parseDouble("-7.2794226"));
	    koordinat1.setLongitude(Double.parseDouble("112.7622392"));
	    koordinat.add(koordinat1);	    
	    
	    for (int a=1;a<koordinat.size();a++){					
			overlayItems4.add(createPolyline1(
					new GeoPoint(koordinat.get((a-1)).getLatitude(), koordinat.get((a-1)).getLongitude()), 
					new GeoPoint(koordinat.get(a).getLatitude(), koordinat.get(a).getLongitude())));
			mapView.getOverlays().add(listOverlay4);
			mapView.getOverlayController().redrawOverlays();//methode untuk mempercepat menampilkan hasil
		}

	}
}
