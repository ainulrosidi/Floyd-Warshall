package com.example.wrmapz;

import id.ac.itats.skripsi.ainul.NodeMaster;
import id.ac.itats.skripsi.databuilder.GraphAdapter;
import id.ac.itats.skripsi.routingengine.ainul.RoutingEngine;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.mapsforge.android.AndroidUtils;
import org.mapsforge.android.maps.DebugSettings;
import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapScaleBar;
import org.mapsforge.android.maps.MapScaleBar.TextField;
import org.mapsforge.android.maps.MapViewPosition;
import org.mapsforge.android.maps.Projection;
import org.mapsforge.android.maps.mapgenerator.TileCache;
import org.mapsforge.android.maps.overlay.Circle;
import org.mapsforge.android.maps.overlay.ListOverlay;
import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.android.maps.overlay.PolygonalChain;
import org.mapsforge.android.maps.overlay.Polyline;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.map.reader.header.FileOpenResult;
import org.mapsforge.map.reader.header.MapFileInfo;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap.CompressFormat;
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
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wrmapz.controller.FileUtils;
import com.example.wrmapz.controller.Haversine;
import com.example.wrmapz.controller.InfoView;
import com.example.wrmapz.controller.KonversiKoordinat;
import com.example.wrmapz.controller.LatLongUtil;
import com.example.wrmapz.controller.ScreenshotCapturer;
import com.example.wrmapz.controller.SeekBarChangeListener;
import com.example.wrmapz.controller.filefilter.FilterByFileExtension;
import com.example.wrmapz.controller.filefilter.ValidMapFile;
import com.example.wrmapz.controller.filefilter.ValidRenderTheme;
import com.example.wrmapz.controller.filepicker.FilePicker;
import com.example.wrmapz.controller.preferences.EditPreferences;
import com.example.wrmapz.model.Koordinat;
import com.example.wrmapz.view.PetaForm;

public class PetaActivity extends MapActivity implements
		android.view.View.OnClickListener, android.view.View.OnTouchListener {

	// The default number of tiles in the file system cache.
	public static final int FILE_SYSTEM_CACHE_SIZE_DEFAULT = 250;
	// The maximum number of tiles in the file system cache.
	public static final int FILE_SYSTEM_CACHE_SIZE_MAX = 500;
	// The default move speed factor of the map.
	public static final int MOVE_SPEED_DEFAULT = 10;
	// The maximum move speed factor of the map.
	public static final int MOVE_SPEED_MAX = 30;
	private PetaForm petaForm;
	private ListOverlay listOverlay = new ListOverlay();
	private List<OverlayItem> overlayItems = listOverlay.getOverlayItems();
	ListOverlay listOverlay1 = new ListOverlay();
	List<OverlayItem> overlayItems1 = listOverlay1.getOverlayItems();
	ListOverlay listOverlay2 = new ListOverlay();
	List<OverlayItem> overlayItems2 = listOverlay2.getOverlayItems();
	ListOverlay listOverlay3 = new ListOverlay();
	List<OverlayItem> overlayItems3 = listOverlay3.getOverlayItems();
	ListOverlay listOverlay4 = new ListOverlay();
	List<OverlayItem> overlayItems4 = listOverlay4.getOverlayItems();
	ListOverlay listOverlay5 = new ListOverlay();
	List<OverlayItem> overlayItems5 = listOverlay5.getOverlayItems();
	ListOverlay listOverlay6 = new ListOverlay();
	List<OverlayItem> overlayItems6 = listOverlay6.getOverlayItems();
	ListOverlay listOverlayMapMatching = new ListOverlay();	
	List<OverlayItem> overlayItemsMapMatching = listOverlayMapMatching.getOverlayItems();
	
	ListOverlay listOverlayBatasPeta = new ListOverlay();	
	List<OverlayItem> overlayItemsBatasPeta = listOverlayBatasPeta.getOverlayItems();
	
	private static final int DIALOG_ENTER_COORDINATES = 0;
	private static final int DIALOG_INFO_MAP_FILE = 1;
	private static final int DIALOG_LOCATION_PROVIDER_DISABLED = 2;
	private static final int DIALOG_ENTER_NODE_ASAL_TUJUAN = 3;
	private static final int INFORMASI_LOKASIKU = 4;
	private static final int SHOW_RUTE = 5;
	private ArrayList<Koordinat> koordinat = new ArrayList<Koordinat>();
	private Koordinat koordinat1;		
	private ArrayList<Koordinat> koordinat2 = new ArrayList<Koordinat>();
	private Koordinat koordinat3;		
	private static final int SELECT_MAP_FILE = 0;
	private static final int SELECT_RENDER_THEME_FILE = 1;
	private File MAP_FILE = new	 File(Environment.getExternalStorageDirectory().getPath(),"/routing_engine/maps/surabaya_new.map");
	private LocationManager locationManager;
	GPSLocationListener locationListener = new GPSLocationListener();
	private Location location;
	@SuppressWarnings("unused")
	private int klik = 1;
	private static final FileFilter FILE_FILTER_EXTENSION_MAP = new FilterByFileExtension(
			".map");
	private static final FileFilter FILE_FILTER_EXTENSION_XML = new FilterByFileExtension(
			".xml");
	private ValidMapFile validMapFile = new ValidMapFile();
	private ScreenshotCapturer screenshotCapturer;
	private KonversiKoordinat konversiKoordinat = new KonversiKoordinat();
	private WakeLock wakeLock;
	// private static final String BUNDLE_CENTER_AT_FIRST_FIX =
	// "centerAtFirstF
	private static final String BUNDLE_SHOW_MY_LOCATION = "showMyLocation";
	private static final String BUNDLE_SNAP_TO_LOCATION = "snapToLocation";
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1 / 1000; // in
																				// Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in
																	// Milliseconds

	protected static final String TAG = "TestActivity";
	private final GraphAdapter graphAdapter = new GraphAdapter();
	public String nodeAsal = "270391759";
	public String nodeTujuan = "1685014141";	
	private Long idNodeAsal2 = (long) 0;
	private Long idNodeTujuan2 = (long)0;
	private String nodeTempatkuBerdiri = "";
	private String nodeTujuanku = "";
	private Double latitudeMatchingTujuan = 0.0;
	private Double longitudeMatchingTujuan = 0.0;
	private String rute = "";
	private double longitudeKitaBerdiri = 0;
	private double latitudeKitaBerdiri = 0;
	private List<NodeMaster> nodeMasterUntukMemoryList = new ArrayList<NodeMaster>();	
	private NodeMaster nodeMasterDiisiKeMemory;
//	private List<Matriks_jarak_predecessor_inisialisasi_master> jarakAntarNodeUntukMemoryList = new ArrayList<Matriks_jarak_predecessor_inisialisasi_master>();
//	private Matriks_jarak_predecessor_inisialisasi_master jarakAntarNodeDiisiKeMemory;
	private int jumlahTitik;	
	private int jumlahNode=592;
	private FileOpenResult fileOpenResult;
//	private int jumlahNode=259;

	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.peta_form);		
		copyMap();
		//JUMLAH NODE YANG TERDAFTAR
		graphAdapter.setJumlahNodeMaster(jumlahNode);	
		createDatabasePertamaKali();		
		setPetaForm(new PetaForm(this));
		 fileOpenResult = petaForm.getMapView().setMapFile(MAP_FILE);	 
		 
		 if (!fileOpenResult.isSuccess()) {
			 startMapFilePicker();
//			 Toast.makeText(this,
//		 fileOpenResult.getErrorMessage(),Toast.LENGTH_LONG).show();
//		 finish();
		 
		 }
//		 if (this.petaForm.getMapView().getMapFile() == null) {
//		 startMapFilePicker();
//		 }
		petaForm.getMapView().setClickable(true);
		petaForm.getMapView().setLongClickable(true);
		petaForm.getMapView().setBuiltInZoomControls(true);
		petaForm.getMapView().setFocusable(true);
		petaForm.getMapView().setFocusableInTouchMode(true);
		petaForm.getMapView().isInEditMode();
		petaForm.getToggleButtonsnapToLocationView().setOnClickListener(this);
		petaForm.getButtonUndo().setOnClickListener(this);
		petaForm.getRekam().setOnClickListener(this);
		petaForm.getMapView().setOnTouchListener((OnTouchListener) this);
		this.screenshotCapturer = new ScreenshotCapturer(this);
		this.screenshotCapturer.start();
		// ---use the LocationManager class to obtain GPS locations---
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// cek apakah setting Use GPS Satellites sudah aktif apa belum
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
			// 0, 0, locationListener);
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES,
					MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
					new GPSLocationListener());
		} else {
			tampilkanAlertGPS();

		}
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		this.wakeLock = powerManager.newWakeLock(
				PowerManager.SCREEN_DIM_WAKE_LOCK, "AMV");
		MapScaleBar mapScaleBar = this.petaForm.getMapView().getMapScaleBar();
		mapScaleBar.setText(TextField.KILOMETER,
				getString(R.string.unit_symbol_kilometer));
		mapScaleBar.setText(TextField.METER,
				getString(R.string.unit_symbol_meter));

		if (savedInstanceState != null
				&& savedInstanceState.getBoolean(BUNDLE_SHOW_MY_LOCATION)) {
			// enableShowMyLocation(savedInstanceState.getBoolean(BUNDLE_CENTER_AT_FIRST_FIX));
			if (savedInstanceState.getBoolean(BUNDLE_SNAP_TO_LOCATION)) {
				// enableSnapToLocation(false);
			}
		}
		insertNodeMasterToMemoryAndroid();
//		insertJarakAntarNodeToMemoryAndroid();
	}

	 
	private void createDatabasePertamaKali() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				graphAdapter.createDatabase();				
				graphAdapter.open();
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
			}

		}.execute();		
	}
	
	private void mapMatchingDanFloydWarshall() {
		jumlahTitik = 1;
		showToastOnUiThread("Please wait to find Floyd-Warshall rute");
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {		
				if(koordinat.size()>1){	
					jumlahTitik=2;
					System.out.println("Latitude1 "+koordinat.get(0).getLatitude()
							+" Longitude1 "+koordinat.get(0).getLongitude());
					System.out.println("Latitude2 "+koordinat.get(koordinat.size()-1).getLatitude()
							+" Longitude2 "+koordinat.get(koordinat.size()-1).getLongitude());				
					System.out.println("Jumlah Titik "+koordinat.size());				
					mapMatchingTempatkuBerdiri(koordinat.get(0).getLatitude(),	koordinat.get(0).getLongitude());			
					mapMatchingTujuan(latitudeMatchingTujuan, longitudeMatchingTujuan);
					overlayItems5.clear();
					overlayItems6.clear();
					overlayItems3.clear();
					koordinat.clear();		
					if ((idNodeAsal2!=0)&&(idNodeTujuan2!=0)) {				
						floydWarshall_proses(idNodeAsal2, idNodeTujuan2);					
//						algoritmaFloyd_Warshall(idNodeAsal2, idNodeTujuan2);

					} 
				}	
				
				else{
				if (getLocation()!= null) {
					mapMatchingTempatkuBerdiri(getLocation().getLatitude(),
							getLocation().getLongitude());
				}		
				if(koordinat.size()==1){
						System.out.println("Jumlah Titik "+koordinat.size());
					mapMatchingTujuan(latitudeMatchingTujuan, longitudeMatchingTujuan);
				}
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				String jarakUntukDitampilkan=graphAdapter.getJarak();
				if(jumlahTitik==2){					
					if(graphAdapter.getJarak()==null){						
						jarakUntukDitampilkan="0";
					}
					if(graphAdapter.getJarak()==""){						
						jarakUntukDitampilkan="0";
					}
				showToastOnUiThread("Floyd-Warshall Finish");	
				getPetaForm().getLocationManager2().setText("Node Asal: " 
						 + nodeTempatkuBerdiri+" Node Tujuan: "
						 +nodeTujuanku+" Jarak Meter : "+String.valueOf(
//								 new BigDecimal((Double.parseDouble(jarakUntukDitampilkan)))
								 new BigDecimal((Double.parseDouble(jarakUntukDitampilkan)))
						 .setScale(3,
									RoundingMode.HALF_EVEN)));
				
				getPetaForm().getTextViewKeterangan().setText("Koordinat Asal :"+latitudeKitaBerdiri+" , "+longitudeKitaBerdiri
						+" Koordinat Tujuan :"+latitudeMatchingTujuan+" , "+longitudeMatchingTujuan);
				petaForm.getMapView().getOverlayController().redrawOverlays();
			}
			}
		}.execute();
		
		
	}
//	private void algoritmaFloyd_Warshall( 
//			final Long nodeAsal5, 
//			final Long nodeTujuan5) {
//		
//		showToastOnUiThread("Please Wait while doing algoritmaFloyd_Warshall");
//		new AsyncTask<Void, Void, Void>() {
//			@Override
//			protected Void doInBackground(Void... params) {	
//
//				return null;
//			}
//			protected void onPostExecute(Void result) {
//				System.out.println("Start doing algoritmaFloyd_Warshall ");
//				for (int k = 1; k < (jumlahNode+1); k++) {
//					System.out.println("k "+k);
//					for (int i = 1; i < (jumlahNode+1); i++) {
//						for (int j = 1; j < (jumlahNode+1); j++) {
//							
//							for(int l=0;l<jarakAntarNodeUntukMemoryList.size(); l++){						
//								if((jarakAntarNodeUntukMemoryList.get(l).getIdNodeAsal().equals(i))&&(jarakAntarNodeUntukMemoryList.get(l).getIdNodeTujuan().equals(k))){
//								jarak_ik=Double.parseDouble(jarakAntarNodeUntukMemoryList.get(l).getJarak());					
//							}
//								if((jarakAntarNodeUntukMemoryList.get(l).getIdNodeAsal().equals(i))&&(jarakAntarNodeUntukMemoryList.get(l).getIdNodeTujuan().equals(j))){
//								jarak_ij=Double.parseDouble(jarakAntarNodeUntukMemoryList.get(l).getJarak());
//							}							
//								if((jarakAntarNodeUntukMemoryList.get(l).getIdNodeAsal().equals(k))&&(jarakAntarNodeUntukMemoryList.get(l).getIdNodeTujuan().equals(j))){							
//								jarak_kj=Double.parseDouble(jarakAntarNodeUntukMemoryList.get(l).getJarak());								
//								path_kj=Integer.parseInt(jarakAntarNodeUntukMemoryList.get(l).getPath());
//								way_Id=jarakAntarNodeUntukMemoryList.get(l).getWayId();
//								
//							}
//						}
//							/*
//							 * If i and j are different nodes and if the paths between i
//							 * and k and between k and j exist, do
//							 */
//							if ((jarak_ik * jarak_ij != 0)
//									&& (i != j)) {
//								/*
//								 * See if you can't get a shorter path between i and j
//								 * by interspacing k somewhere along the current path
//								 */
//								if((i==Integer.parseInt(nodeAsal5.toString()))||(j==Integer.parseInt(nodeTujuan5.toString()))){
//								if (((jarak_ik + jarak_kj) < jarak_ij)|| (jarak_ij == 0)) {
//									
//										
////									System.out.println(" jarak_bobot_graph "+(i+1)+"->"+(j+1)+" "+ jarak_bobot_graph[i][j]+" > "+" jarak_bobot_graph "+(i+1)+"->"+(k+1)+" "+ jarak_bobot_graph[i][k]+" ditambah jarak_bobot_graph "+(k+1)+"->"+(j+1)+" "+ jarak_bobot_graph[k][j]);
////										if((jarak_bobot_graph[i][j] == 0)){
////											if((i==hasilAsal)){
//										System.out.println(" jarak_bobot_graph titik "+(i+1)
//											+" -> "+(j+1)+" ="+ "tak hingga"+" lebih besar dari ");
//									System.out.println(		
//											" jarak_bobot_graph titik("+(i+1)+" -> "+(k+1)+") = "
//											+ jarak_ik
//													+" + ("
//											+(k+1)+" -> "+(j+1)+") = "+ jarak_kj+" --> "+
//													( jarak_ik+jarak_kj)
//											);
//									
//									jarak_ij = jarak_ik
//											+ jarak_kj;
//									path_ij = path_kj;
//									System.out
//											.println(" dan titik yang dilewati sebelum titik "
//													+ (j + 1)
//													+ " = titik "
//													+ path_ij);
//									
//								
////										}
//								}
//							}}
//							jarak_ik=0;
//							jarak_ij=0;
//							jarak_kj=0;
//							path_ij=0;
//							path_kj=0;	
//							way_Id="";
//						}
//					}
//				}		
//				System.out.println("Finish algoritmaFloyd_Warshall  ");
//				showToastOnUiThread("Finish algoritmaFloyd_Warshall");
//				}	
//			
//		}.execute();
//	}
	private void bersihkanPenanda() {
		overlayItems1.clear();
		overlayItems2.clear();
		overlayItems3.clear();
		overlayItems4.clear();
		overlayItems5.clear();
		overlayItems6.clear();
		overlayItemsBatasPeta.clear();
		overlayItems.clear();	
		overlayItemsMapMatching.clear();
		koordinat.clear();
		koordinat2.clear();		
		petaForm.reset();
		petaForm.getMapView().getOverlays().remove(listOverlay);
		getPetaForm().getMapView().getOverlays().remove(listOverlay1);
		getPetaForm().getMapView().getOverlays().remove(listOverlay2);
		getPetaForm().getMapView().getOverlays().remove(listOverlay3);
		getPetaForm().getMapView().getOverlays().remove(listOverlay4);
		getPetaForm().getMapView().getOverlays().remove(listOverlay5);
		getPetaForm().getMapView().getOverlays().remove(listOverlay6);
		getPetaForm().getMapView().getOverlays().remove(listOverlayBatasPeta);
		getPetaForm().getMapView().getOverlays().remove(listOverlayMapMatching);
		petaForm.getMapView().getOverlayController().redrawOverlays();// methode
													// hasil
		
		rute="";
		nodeTempatkuBerdiri="";
		nodeTujuanku="";
		graphAdapter.setJarak("");			
		latitudeMatchingTujuan = 0.0;
		longitudeMatchingTujuan = 0.0;	
		latitudeKitaBerdiri = 0.0;
		longitudeKitaBerdiri= 0.0;
		idNodeAsal2 = (long) 0;
		idNodeTujuan2 = (long)0;
		getPetaForm().getLocationManager1().setText("");
		getPetaForm().getLocationManager2().setText("");			
		
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
			}
		}.execute();		
	}
	
	private void floydWarshall_proses( 
			final Long nodeAsal5, 
			final Long nodeTujuan5) {

		Double latitudeKuBerdiri = null;
		Double longitudeKuBerdiri= null;
		Double latitudeKuTujuan = null;
		Double longitudeKuTujuan= null;
		
//				graphAdapter.createDatabase();
//				graphAdapter.open();
				graphAdapter.menampilanRuteDanJarak_2(nodeAsal5, nodeTujuan5);
				System.out.println("path floydWarshall : ");
				System.out.println("Jarak : "+graphAdapter.getJarak());					
//				System.out.println("Nama Jalan "+graphAdapter.getNamaJalan_2());
//				for (SatuArah satuArah : graphAdapter.getSatuArah()) {
//					System.out.println("Nama Jalan :"+satuArah.getNamaJalan()); 
//				}
				for (NodeMaster nodeMaster : graphAdapter.getShortestPath_2()) {					
					System.out.println(nodeMaster.getIdNode() 
							+ "->"+nodeMaster.getIdNodePeta() + " -> "
							+ nodeMaster.getLatitude() + " , "
							+ nodeMaster.getLongitude());						

					if (nodeMaster.getIdNodePeta().equals(nodeTempatkuBerdiri)) {
						GeoPoint mapCenter1 = new GeoPoint(
								Double.parseDouble(nodeMaster.getLatitude()),
								Double.parseDouble(nodeMaster.getLongitude()));
						Marker marker2 = createMarker(R.drawable.marker_green2,
								mapCenter1);
						overlayItems5.add(marker2);
						petaForm.getMapView().getOverlays().add(listOverlay5);
						
						latitudeKuBerdiri=Double.parseDouble(nodeMaster.getLatitude());
						longitudeKuBerdiri=Double.parseDouble(nodeMaster.getLongitude());
					} 
					
					else if (nodeMaster.getIdNodePeta().equals(nodeTujuanku)) {
						GeoPoint mapCenter2 = new GeoPoint(
								Double.parseDouble(nodeMaster.getLatitude()),
								Double.parseDouble(nodeMaster.getLongitude()));
						Marker marker3 = createMarker(R.drawable.marker_red1,
								mapCenter2);
						overlayItems5.add(marker3);
						petaForm.getMapView().getOverlays().add(listOverlay5);
						rute=rute+nodeMaster.getIdNodePeta()+"<-";
						 latitudeKuTujuan = Double.parseDouble(nodeMaster.getLatitude());
						 longitudeKuTujuan= Double.parseDouble(nodeMaster.getLongitude());
													
							koordinat1 = new Koordinat();
							koordinat1.setLatitude(Double.parseDouble(nodeMaster
									.getLatitude()));
							koordinat1.setLongitude(Double.parseDouble(nodeMaster
									.getLongitude()));
							koordinat.add(koordinat1);	
						
						
					} else {
//						GeoPoint mapCenter = new GeoPoint(
//								Double.parseDouble(nodeMaster.getLatitude()),
//								Double.parseDouble(nodeMaster.getLongitude()));
//						Marker marker1 = createMarker(R.drawable.marker_blue3,
//								mapCenter);
//						overlayItems5.add(marker1);
//						petaForm.getMapView().getOverlays().add(listOverlay5);
						rute=rute+nodeMaster.getIdNodePeta()+"<-";						
						koordinat1 = new Koordinat();
						koordinat1.setLatitude(Double.parseDouble(nodeMaster
								.getLatitude()));
						koordinat1.setLongitude(Double.parseDouble(nodeMaster
								.getLongitude()));
						koordinat.add(koordinat1);	
						}
				}
				
				koordinat1 = new Koordinat();
				koordinat1.setLatitude(latitudeKuBerdiri);
				koordinat1.setLongitude(longitudeKuBerdiri);
				koordinat.add(koordinat1);	
				
				for (int a = 1; a < koordinat.size(); a++) {
					overlayItems6.add(createPolyline1(new GeoPoint(koordinat
							.get((a - 1)).getLatitude(), koordinat.get((a - 1))
							.getLongitude()), new GeoPoint(koordinat.get(a)
							.getLatitude(), koordinat.get(a).getLongitude())));
					petaForm.getMapView().getOverlays().add(listOverlay6);
					petaForm.getMapView().getOverlayController()
							.redrawOverlays();// methode untuk mempercepat
												// menampilkan hasil
				}
		
				rute=rute+"<-"+nodeTempatkuBerdiri;
				System.out.println("Rute "+rute);
//				System.out.println("Runing Time "+(SelesaiDetik-mulaiDetik)+" Detik");
//				System.out.println("Start: "+mulai+" Finish = "+selesai);
//				showToastOnUiThread("Floyd_Warshall Finish");
				
//				getPetaForm().getLocationManager3().setText("Node Asal: " 
//						 + nodeTempatkuBerdiri+" Node Tujuan: "
//						 +nodeTujuanku+" Jarak Meter : "+graphAdapter.getJarak());
//				getPetaForm().getLocationManager2().setText("Koordinat Asal :"+latitudeKuBerdiri+","+longitudeKuBerdiri
//						+"Koordinat Tujuan :"+latitudeKuTujuan+","+longitudeKuTujuan);
//				getPetaForm().getTextViewKeterangan().setText("Rute :"+rute);						 

	}
	
	private void floydWarshallMenu(){
		showToastOnUiThread("Please wait to find Floyd-Warshall rute");
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {		
				overlayItems5.clear();
				overlayItems6.clear();
				overlayItems3.clear();
				koordinat.clear();			
				if ((idNodeAsal2!=0)&&(idNodeTujuan2!=0)) {				
					floydWarshall_proses(idNodeAsal2, idNodeTujuan2);
				} 
				else if(idNodeTujuan2!=0){
					showToastOnUiThread("Touch atau klik dahulu tujuan");
				}
				else if(idNodeAsal2==0){
					showToastOnUiThread("Touch atau klik dahulu Asal");
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				showToastOnUiThread("Floyd-Warshall Finish");
				String jarakUntukDitampilkan=graphAdapter.getJarak();								
				if(graphAdapter.getJarak()==null){					
					jarakUntukDitampilkan="0";
				}
				if(graphAdapter.getJarak()==""){					
					jarakUntukDitampilkan="0";
				}
				getPetaForm().getLocationManager2().setText("Node Asal: " 
						 + nodeTempatkuBerdiri+" Node Tujuan: "
						 +nodeTujuanku+" Jarak Meter : "+String.valueOf(
								 new BigDecimal((Double.parseDouble(jarakUntukDitampilkan)))
						 .setScale(3,
									RoundingMode.HALF_EVEN)));
				
				getPetaForm().getTextViewKeterangan().setText("Koordinat Asal :"+latitudeKitaBerdiri+" , "+longitudeKitaBerdiri
						+" Koordinat Tujuan :"+latitudeMatchingTujuan+" , "+longitudeMatchingTujuan);
				petaForm.getMapView().getOverlayController().redrawOverlays();
			}

		}.execute();
		
			
	}
	
	private void showMyLocation(){
		if ((getLatitudeKitaBerdiri() != 0)
				&& (getLongitudeKitaBerdiri()) != 0) {
			// Toast.makeText(this,
			// "Anda memilih Show My Location",Toast.LENGTH_LONG).show();
			GeoPoint geoPoint = new GeoPoint(getLatitudeKitaBerdiri(),
					getLongitudeKitaBerdiri());
			Marker marker1 = createMarker(R.drawable.marker_orange,
					geoPoint);
			overlayItems.removeAll(overlayItems);
			overlayItems.add(marker1);
			overlayItems.add(createCircle1(new GeoPoint(
					getLatitudeKitaBerdiri(), getLongitudeKitaBerdiri())));
			petaForm.getMapView().getOverlays().add(listOverlay);
			MapPosition newMapPosition = new MapPosition(geoPoint,
					(byte) 16);
			PetaActivity.this.petaForm.getMapView().getMapViewPosition()
					.setMapPosition(newMapPosition);
		} else {
			showToastOnUiThread("Menunggu Posisi Dari GPS");
		}
	}
	
	private void lastKnownLoaction(){
		if ((getLatitudeKitaBerdiri() != 0)
				&& (getLongitudeKitaBerdiri()) != 0) {
			GeoPoint geoPoint = new GeoPoint(getLatitudeKitaBerdiri(),
					getLongitudeKitaBerdiri());
			Marker marker1 = createMarker(R.drawable.marker_orange,
					geoPoint);
			overlayItems.removeAll(overlayItems);
			overlayItems.add(marker1);
			overlayItems.add(createCircle1(new GeoPoint(
					getLatitudeKitaBerdiri(), getLongitudeKitaBerdiri())));
			petaForm.getMapView().getOverlays().add(listOverlay);
			MapPosition newMapPosition = new MapPosition(geoPoint,
					(byte) 16);
			PetaActivity.this.petaForm.getMapView().getMapViewPosition()
					.setMapPosition(newMapPosition);
		}
	}
	
	private void insertNodeMasterToMemoryAndroid() {
		showToastOnUiThread("Please Wait while start Insert Node Master To Memory");
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {			
				return null;
			}
			protected void onPostExecute(Void result) {
				System.out.println("Start Insert Node To Memory ");
				
				for (NodeMaster nodeMaster : graphAdapter.getNodeMaster()) {
					nodeMasterDiisiKeMemory = new NodeMaster();
					nodeMasterDiisiKeMemory.setLatitude(nodeMaster.getLatitude());
					nodeMasterDiisiKeMemory.setLongitude(nodeMaster.getLongitude());
					nodeMasterDiisiKeMemory.setIdNodePeta(nodeMaster.getIdNodePeta());
					nodeMasterDiisiKeMemory.setIdNode(nodeMaster.getIdNode());
					nodeMasterUntukMemoryList.add(nodeMasterDiisiKeMemory);
//					System.out.println("Id Node  "+nodeMaster.getIdNode()+
//							" IdNodePeta "+nodeMaster.getIdNodePeta());
				}		
				System.out.println("Finish Insert Node Master To Memory  ");
				showToastOnUiThread("Finish Insert Node To Memory");
				}	
			
		}.execute();
		
	}

//	private void insertJarakAntarNodeToMemoryAndroid() {
//		showToastOnUiThread("Please Wait while start insertJarakAntarNodeToMemoryAndroid To Memory");
//		new AsyncTask<Void, Void, Void>() {
//			@Override
//			protected Void doInBackground(Void... params) {			
//				return null;
//			}
//			protected void onPostExecute(Void result) {
//				System.out.println("Start insertJarakAntarNodeToMemoryAndroid To Memory ");				
//				for (Matriks_jarak_predecessor_inisialisasi_master jarakAntarNode2 : graphAdapter.getMatriks_jarak_predecessor_inisialisasi_master()) {
//					jarakAntarNodeDiisiKeMemory = new Matriks_jarak_predecessor_inisialisasi_master();
//					jarakAntarNodeDiisiKeMemory.setIdMatriksjarakInisialisasi(jarakAntarNode2.getIdMatriksjarakInisialisasi());
//					jarakAntarNodeDiisiKeMemory.setIdNodeAsal(jarakAntarNode2.getIdNodeAsal());
//					jarakAntarNodeDiisiKeMemory.setIdNodeTujuan(jarakAntarNode2.getIdNodeTujuan());
//					jarakAntarNodeDiisiKeMemory.setJarak(jarakAntarNode2.getJarak());
//					jarakAntarNodeDiisiKeMemory.setPath(jarakAntarNode2.getPath());
//					jarakAntarNodeDiisiKeMemory.setWayId(jarakAntarNode2.getWayId());
//					jarakAntarNodeUntukMemoryList.add(jarakAntarNodeDiisiKeMemory);					
//					System.out.println("Id "+jarakAntarNode2.getIdMatriksjarakInisialisasi()+
//							" Asal "+jarakAntarNode2.getIdNodeAsal()+
//							" Tujuan "+jarakAntarNode2.getIdNodeTujuan()+
//							" Jarak "+jarakAntarNode2.getJarak()+
//							" Path "+jarakAntarNode2.getPath());
//				}		
//				System.out.println("Finish insertJarakAntarNodeToMemoryAndroid To Memory  ");
//				showToastOnUiThread("Finish insertJarakAntarNodeToMemoryAndroid To Memory");
//				}	
//			
//		}.execute();
//		
//	}

		private void batasPeta() {		
			
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {	
					GeoPoint kiriAtas = new GeoPoint(
							Double.parseDouble("-7.2779"),
							Double.parseDouble("112.7596"));
					Marker marker2 = createMarker(R.drawable.marker_yellow1,
							kiriAtas);
					overlayItemsBatasPeta.add(marker2);
					petaForm.getMapView().getOverlays().add(listOverlayBatasPeta);

					koordinat3 = new Koordinat();
					koordinat3.setLatitude(Double.parseDouble(("-7.2779")));
					koordinat3.setLongitude(Double.parseDouble("112.7596"));
					koordinat2.add(koordinat3);	
					
					GeoPoint KananAtas = new GeoPoint(
							Double.parseDouble("-7.3054"),
							Double.parseDouble("112.7596"));
					
					Marker marker3 = createMarker(R.drawable.marker_yellow1,
							KananAtas);
					overlayItemsBatasPeta.add(marker3);
					petaForm.getMapView().getOverlays().add(listOverlayBatasPeta);

					koordinat3 = new Koordinat();
					koordinat3.setLatitude(Double.parseDouble("-7.3054"));
					koordinat3.setLongitude(Double.parseDouble("112.7596"));
					koordinat2.add(koordinat3);	
					
					GeoPoint kiriBawah = new GeoPoint(
							Double.parseDouble("-7.3054"),
							Double.parseDouble("112.7916"));
					Marker marker4 = createMarker(R.drawable.marker_yellow1,
							kiriBawah);
					overlayItemsBatasPeta.add(marker4);
					petaForm.getMapView().getOverlays().add(listOverlayBatasPeta);

					koordinat3 = new Koordinat();
					koordinat3.setLatitude(Double.parseDouble("-7.3054"));
					koordinat3.setLongitude(Double.parseDouble("112.7916"));
					koordinat2.add(koordinat3);	
					
					GeoPoint kananBawah = new GeoPoint(
							Double.parseDouble("-7.2779"),
							Double.parseDouble("112.7916"));
					
					Marker marker5 = createMarker(R.drawable.marker_yellow1,
							kananBawah);
					overlayItemsBatasPeta.add(marker5);
					petaForm.getMapView().getOverlays().add(listOverlayBatasPeta);

					koordinat3 = new Koordinat();
					koordinat3.setLatitude(Double.parseDouble("-7.2779"));
					koordinat3.setLongitude(Double.parseDouble("112.7916"));
					koordinat2.add(koordinat3);
					
					for (int a = 1; a < koordinat2.size(); a++) {
						overlayItemsBatasPeta.add(createPolyline(new GeoPoint(koordinat2
								.get((a - 1)).getLatitude(), koordinat2.get((a - 1))
								.getLongitude()), new GeoPoint(koordinat2.get(a)
								.getLatitude(), koordinat2.get(a).getLongitude())));
						petaForm.getMapView().getOverlays().add(listOverlayBatasPeta);
						
					}	
					overlayItemsBatasPeta.add(createPolyline(
							new GeoPoint(koordinat2.get(koordinat2.size()-1).getLatitude(), koordinat2.get(koordinat2.size()-1).getLongitude()), 
							new GeoPoint(koordinat2.get(0).getLatitude(), koordinat2.get(0).getLongitude())));
					
					petaForm.getMapView().getOverlayController().redrawOverlays();// methode untuk mempercepat
//										 menampilkan hasil
					return null;
				}
				@Override
				protected void onPostExecute(Void result) {							
						} 
			}.execute();
		}
		

	
		private void mapMatchingTempatkuBerdiri(final Double gpsLatitude,
				final Double gpsLongitude) {
				double minJarak = Double.MAX_VALUE;
				double jarak = 0;
				String latitude = "" ;
				String longitude = "";
				String minNode = "";
					System.out.println("MapMatching Tempatku Berdiri : ");
//					int n=0;
					for (int a=0;a<nodeMasterUntukMemoryList.size();a++) {						
//						 System.out.println(++n+". "+nodeMasterUntukMemoryList.get(a).getIdNodePeta()
//						 +" -> " + nodeMasterUntukMemoryList.get(a).getLatitude() + " , "+
//						 nodeMasterUntukMemoryList.get(a).getLongitude());						
						jarak = LatLongUtil.distance(gpsLatitude, gpsLongitude,
								Double.parseDouble(nodeMasterUntukMemoryList.get(a).getLatitude()),
								Double.parseDouble(nodeMasterUntukMemoryList.get(a).getLongitude()));
						if (jarak < minJarak) {
							minJarak = jarak;
							minNode =nodeMasterUntukMemoryList.get(a).getIdNodePeta();
							latitude = nodeMasterUntukMemoryList.get(a).getLatitude();
							longitude = nodeMasterUntukMemoryList.get(a).getLongitude();
							latitudeKitaBerdiri=Double.parseDouble(nodeMasterUntukMemoryList.get(a).getLatitude());
							longitudeKitaBerdiri=Double.parseDouble(nodeMasterUntukMemoryList.get(a).getLongitude());
							
							idNodeAsal2=nodeMasterUntukMemoryList.get(a).getIdNode();						
							nodeTempatkuBerdiri = nodeMasterUntukMemoryList.get(a).getIdNodePeta();
//							System.out.println("minimum Jarak tempatku Berdiri  = " + minJarak+" idNodeAsal2 "+idNodeAsal2+" Node : "+minNode
//									+ " , Latitude " + latitude + " , Longitude "
//									+ longitude);	
						}

					}
					System.out.println("minimum Jarak tempatku Berdiri = " + minJarak+" idNodeAsal2 "+idNodeAsal2+" Node : "+minNode
							+ " , Latitude " + latitude + " , Longitude "
							+ longitude);				

					GeoPoint mapCenter = new GeoPoint(Double.parseDouble(latitude),
							Double.parseDouble(longitude));
					Marker marker = createMarker(R.drawable.marker_green2, mapCenter);
					overlayItemsMapMatching.add(marker);
					petaForm.getMapView().getOverlays().add(listOverlayMapMatching);
					getPetaForm().getMapView().getOverlayController()
							.redrawOverlays();// methode untuk mempercepat
												// menampilkan hasil
					showToastOnUiThread("MapMatching Tempatku Berdiri Finish");
		}
		


	private void mapMatchingTujuan(final Double gpsLatitude,
			final Double gpsLongitude) {
			double minJarak = Double.MAX_VALUE;
			double jarak = 0;
			String latitude = "";
			String longitude = "";
			String minNode = "";
//				int n = 0;
				System.out.println("MapMatching Tujuan : ");
				for (int a=0;a<nodeMasterUntukMemoryList.size();a++) {					
//					 System.out.println(++n+". "+nodeMasterUntukMemoryList.get(a).getIdNodePeta()
//							 +" -> " + nodeMasterUntukMemoryList.get(a).getLatitude() + " , "+
//							 nodeMasterUntukMemoryList.get(a).getLongitude());					 
					 jarak = LatLongUtil.distance(gpsLatitude, gpsLongitude,
								Double.parseDouble(nodeMasterUntukMemoryList.get(a).getLatitude()),
								Double.parseDouble(nodeMasterUntukMemoryList.get(a).getLongitude()));
						if (jarak < minJarak) {
							minJarak = jarak;
							minNode =nodeMasterUntukMemoryList.get(a).getIdNodePeta();
							latitude = nodeMasterUntukMemoryList.get(a).getLatitude();
							longitude = nodeMasterUntukMemoryList.get(a).getLongitude();
							latitudeMatchingTujuan=Double.parseDouble(nodeMasterUntukMemoryList.get(a).getLatitude());
							longitudeMatchingTujuan=Double.parseDouble(nodeMasterUntukMemoryList.get(a).getLongitude());
							idNodeTujuan2=nodeMasterUntukMemoryList.get(a).getIdNode();						
							nodeTujuanku = nodeMasterUntukMemoryList.get(a).getIdNodePeta();
//							System.out.println("minimum Jarak Tujuan = " + minJarak+" idNodeTujuan2 "+idNodeTujuan2+" Node : "+minNode
//									+ " , Latitude " + latitude + " , Longitude "
//									+ longitude);							
					}
				}
				
				System.out.println("minimum Jarak Tujuan = " + minJarak+" idNodeTujuan2 "+idNodeTujuan2+" Node : "+minNode
						+ " , Latitude " + latitude + " , Longitude "
						+ longitude);

				GeoPoint mapCenter = new GeoPoint(Double.parseDouble(latitude),
						Double.parseDouble(longitude));
				Marker marker = createMarker(R.drawable.marker_red1,
						mapCenter);
				overlayItemsMapMatching.add(marker);
				petaForm.getMapView().getOverlays().add(listOverlayMapMatching);
				getPetaForm().getMapView().getOverlayController()
						.redrawOverlays();// methode untuk mempercepat
											// menampilkan hasil
				showToastOnUiThread("MapMatching Tujuan Finish");

	}
	

	private void konversiIdNodePetaToIdNodeAsal( final String idNodePeta) {
		System.out.println("konversi IdNode Peta To IdNode Asal : ");
		for (int a=0;a<nodeMasterUntukMemoryList.size();a++) {
			if(nodeMasterUntukMemoryList.get(a).getIdNodePeta().equals(idNodePeta)){
				System.out.println("Id Node Asal "+nodeMasterUntukMemoryList.get(a).getIdNode());
				idNodeAsal2=nodeMasterUntukMemoryList.get(a).getIdNode();
				nodeTempatkuBerdiri=nodeMasterUntukMemoryList.get(a).getIdNodePeta();
				latitudeKitaBerdiri=Double.parseDouble(nodeMasterUntukMemoryList.get(a).getLatitude());
				longitudeKitaBerdiri=Double.parseDouble(nodeMasterUntukMemoryList.get(a).getLongitude());
			}
			
		}
		
	}

	private void konversiIdNodePetaToIdNodeTujuan( final String idNodePeta) {
		System.out.println("konversi IdNode Peta To IdNode Tujuan : ");	
		for (int a=0;a<nodeMasterUntukMemoryList.size();a++) {
			if(nodeMasterUntukMemoryList.get(a).getIdNodePeta().equals(idNodePeta)){
				System.out.println("Id Node Tujuan "+nodeMasterUntukMemoryList.get(a).getIdNode());
				idNodeTujuan2=nodeMasterUntukMemoryList.get(a).getIdNode();
				nodeTujuanku=nodeMasterUntukMemoryList.get(a).getIdNodePeta();
				latitudeMatchingTujuan=Double.parseDouble(nodeMasterUntukMemoryList.get(a).getLatitude());
				longitudeMatchingTujuan=Double.parseDouble(nodeMasterUntukMemoryList.get(a).getLongitude());
			}
			}

		
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == petaForm.getToggleButtonsnapToLocationView()) {
			// showToastOnUiThread("Anda klik toggle button");
			if (petaForm.getMapView().isClickable() == true) {
				showToastOnUiThread("Peta tidak dapat di geser");
				petaForm.getMapView().setClickable(false);
				getPetaForm().getToggleButtonsnapToLocationView()
						.setBackgroundResource(R.drawable.btn_snap_pressed);	
				batasPeta();
				
			} else if (petaForm.getMapView().isClickable() == false) {
				showToastOnUiThread("Peta dapat di geser");
				petaForm.getMapView().setClickable(true);
				getPetaForm().getToggleButtonsnapToLocationView()
						.setBackgroundResource(R.drawable.btn_snap_normal);
			}
		}
		
		 if( v== petaForm.getButtonUndo()){
			 if(koordinat.size()==1){				 
				 koordinat.remove(0);
				 overlayItems3.remove(0); 
				 System.out.println("Ukuran Koordinat yg dihapus "+koordinat.size());
				 getPetaForm().getMapView().getOverlayController().redrawOverlays();
				 getPetaForm().getLocationManager2().setText("");				 
					 klik= koordinat.size();				
				 }		
			 
			 if(koordinat.size()>1){
			 koordinat.remove(koordinat.size()-1);
			 overlayItems3.remove(overlayItems3.size()-1);
			 System.out.println("Ukuran Koordinat yg dihapus "+koordinat.size());
			 getPetaForm().getMapView().getOverlayController().redrawOverlays();
			 getPetaForm().getLocationManager2().setText("");
			 klik= koordinat.size();
			 }
		 }
		 
		 if( v== petaForm.getRekam()){
				if (getLocation() != null) {
					GeoPoint mapCenter = new GeoPoint(getLocation().getLatitude(),
							getLocation().getLongitude());
					koordinat1 = new Koordinat();
					koordinat1.setLatitude(getLocation().getLatitude());
					koordinat1.setLongitude(getLocation().getLongitude());
						koordinat.add(koordinat1);
						Marker marker1 = createMarker(R.drawable.marker_blue1, mapCenter);
						overlayItems3.add(marker1);
						getPetaForm().getMapView().getOverlays().add(listOverlay3);								
					getPetaForm().getMapView().getOverlayController().redrawOverlays();// methode
				}
		 }		 
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		// TODO Auto-generated method stub
		boolean sentuh = true;
		if (petaForm.getMapView().isClickable() == false) {
			// showToastOnUiThread("Peta dapat di touch titik");
			Projection projection = getPetaForm().getMapView().getProjection();
			GeoPoint geoPoint = projection.fromPixels((int) event.getX(),			(int) event.getY());		
			
			latitudeMatchingTujuan = geoPoint.latitude;
			longitudeMatchingTujuan = geoPoint.longitude;
			koordinat1 = new Koordinat();
			koordinat1.setLatitude(geoPoint.latitude);
			koordinat1.setLongitude(geoPoint.longitude);			
			int sudah_ada=0;
			for(int a=0;a<koordinat.size();a++){
				if(koordinat.get(a).getLatitude()==koordinat1.getLatitude()
						&&(koordinat.get(a).getLongitude()==koordinat1.getLongitude())){
					sudah_ada=1;
				}
			}
			if(sudah_ada==0){
				koordinat.add(koordinat1);
				Marker marker1 = createMarker(R.drawable.marker_blue1, geoPoint);
				overlayItems3.add(marker1);
				getPetaForm().getMapView().getOverlays().add(listOverlay3);
				getPetaForm().getLocationManager2().setText(
						" Koordinat yang di klik ke " + (koordinat.size()) + " latitude  "
								+ geoPoint.latitude + " longitude  "
								+ geoPoint.longitude);
				System.out.println("Jumlah klik "+koordinat.size());
			}			
			getPetaForm().getMapView().getOverlayController().redrawOverlays();// methode
														// hasil
			klik = koordinat.size();
			sentuh = true;			
		} else if (petaForm.getMapView().isClickable() == true) {
			// showToastOnUiThread("Peta tidak dapat di touch titik");
			sentuh = false;
		}
		return sentuh;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.info).setVisible(true);
		menu.findItem(R.id.screenshot).setVisible(true);
		menu.findItem(R.id.menu_position).setVisible(true);
		menu.findItem(R.id.menu_mapfile).setVisible(true);
		menu.findItem(R.id.action_settings).setVisible(true);
		menu.findItem(R.id.menu_render_theme).setVisible(true);
		menu.findItem(R.id.menu_preferences).setVisible(true);
		return true;
	}

	/**
	 * Uses the UI thread to display the given text message as toast
	 * notification.
	 * 
	 * @param text
	 *            the text message to display
	 */
	public void showToastOnUiThread(final String text) {
		if (AndroidUtils.currentThreadIsUiThread()) {
			Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
			toast.show();
		} else {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast toast = Toast.makeText(PetaActivity.this, text,
							Toast.LENGTH_LONG);
					toast.show();
				}
			});
		}
	}

	public PetaForm getPetaForm() {
		return petaForm;
	}

	public void setPetaForm(PetaForm petaForm) {
		this.petaForm = petaForm;
	}

	private Circle createCircle1(GeoPoint geoPoint) {
		Paint paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintFill.setStyle(Paint.Style.FILL);
		paintFill.setColor(Color.BLUE);
		paintFill.setAlpha(16);
		Paint paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintStroke.setStyle(Paint.Style.STROKE);
		paintStroke.setColor(Color.BLUE);
		paintStroke.setAlpha(128);
		paintStroke.setStrokeWidth(3);
		return new Circle(geoPoint, 200, paintFill, paintStroke);
	}

	private Polyline createPolyline1(GeoPoint geoPoint, GeoPoint geoPoint1) {
		List<GeoPoint> geoPoints = Arrays.asList(geoPoint, geoPoint1);
		PolygonalChain polygonalChain = new PolygonalChain(geoPoints);
		Paint paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintStroke.setStyle(Paint.Style.STROKE);
		paintStroke.setColor(Color.BLACK);
		paintStroke.setAlpha(128);
		paintStroke.setStrokeWidth(5);
		paintStroke
				.setPathEffect(new DashPathEffect(new float[] { 25, 15 }, 0));
		return new Polyline(polygonalChain, paintStroke);
	}
	private Polyline createPolyline(GeoPoint geoPoint, GeoPoint geoPoint1) {
		List<GeoPoint> geoPoints = Arrays.asList(geoPoint, geoPoint1);
		PolygonalChain polygonalChain = new PolygonalChain(geoPoints);
		Paint paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintStroke.setStyle(Paint.Style.STROKE);		
		paintStroke.setColor(Color.GREEN);
		paintStroke.setAlpha(128);
		paintStroke.setStrokeWidth(5);
		paintStroke
				.setPathEffect(new DashPathEffect(new float[] { 25, 15 }, 0));
		return new Polyline(polygonalChain, paintStroke);
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
				 getPetaForm().getLocationManager1().setText(
				 "Lokasiku Lat: " + loc.getLatitude()+
				 konversiKoordinat.konversiKoordinatLatitude(loc)+
				 " Long: " +loc.getLongitude()+
				 konversiKoordinat.konversiKoordinatLongitude(loc)+")"
				 );
				 getPetaForm().getLocationManager3().setText("Ketinggian diatas permukaan laut M:"
				 +String.valueOf(new
				 BigDecimal(loc.getAltitude()).setScale(5,RoundingMode.HALF_EVEN))
				 +" Keakuratan Meter:"+loc.getAccuracy()
				 +" Privider GPS:"+loc.getProvider()
				 );

				setLatitudeKitaBerdiri(loc.getLatitude());
				setLongitudeKitaBerdiri(loc.getLongitude());
				setLocation(loc);
				GeoPoint mapCenter = new GeoPoint(loc.getLatitude(),
						loc.getLongitude());
				Marker marker1 = createMarker(R.drawable.marker_green,
						mapCenter);
				Marker marker2 = createMarker(
						R.drawable.ic_maps_indicator_current_position_anim1,
						mapCenter);				
				overlayItems.removeAll(overlayItems);
				overlayItems.add(marker1);
				overlayItems.add(marker2);
				getPetaForm().getMapView().getOverlays().add(listOverlay);
				getPetaForm().getMapView().getOverlayController().redrawOverlays();// methode untuk mempercepat
											// menampilkan hasil

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
			// showToastOnUiThread("Location status changed");
			// getPetaForm().getLocationManager3().append(" Status changed");
		}

	}

	private void tampilkanAlertGPS() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage(
						"GPS belum diaktifkan. Apakah anda akan mengakstifkan konfigurasi GPS?")
				.setCancelable(false)
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

	private void konfirmasiMenghitung() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
				.setMessage(
						"Apakah anda yakin akan menghitung jarak "
								+ (koordinat.size()) + " titik tersebut")
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						menampilkanJarak();
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

	public double getLongitudeKitaBerdiri() {
		return longitudeKitaBerdiri;
	}

	public void setLongitudeKitaBerdiri(double longitudeKitaBerdiri) {
		this.longitudeKitaBerdiri = longitudeKitaBerdiri;
	}

	public double getLatitudeKitaBerdiri() {
		return latitudeKitaBerdiri;
	}

	public void setLatitudeKitaBerdiri(double latitudeKitaBerdiri) {
		this.latitudeKitaBerdiri = latitudeKitaBerdiri;
	}

	@SuppressWarnings("deprecation")
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent setting = new Intent(
					android.provider.Settings.ACTION_SETTINGS);
			startActivity(setting);
			return true;
		
		case R.id.map_matching:
			mapMatchingDanFloydWarshall();
			return true;			
//		case R.id.show_rute:
//			showDialog(SHOW_RUTE);
//			return true;
		case R.id.batas_peta:
			batasPeta();
			return true;
		case R.id.floyd_warshall:
			floydWarshallMenu();
			return true;			
//		case R.id.contoh:
//			floydWarshall_contoh();
//			return true;
		case R.id.tarik_garis:

			if (koordinat.size() > 1) {
				konfirmasiMenghitung();
			} else {
				onClick(petaForm.getMapView());
				showToastOnUiThread("Touch atau klik dahulu minimal 2 Titik di peta");
			}
			return true;
		case R.id.map_file_centre:
			MapFileInfo mapFileInfo = petaForm.getMapView().getMapDatabase()
					.getMapFileInfo();
			petaForm.getMapView().getMapViewPosition()
					.setCenter(mapFileInfo.boundingBox.getCenterPoint());
			return true;
		case R.id.bersihkan_penanda:
			bersihkanPenanda();
			return true;

		case R.id.show_mylocation:
			showMyLocation();
			return true;

		case R.id.last_known_location:
lastKnownLoaction();
			return true;
		case R.id.map_file_properties:
			extracted();
			return true;
		case R.id.menu_mapfile:
			startMapFilePicker();
			return true;
		case R.id.menu_screenshot_jpeg:
			this.screenshotCapturer.captureScreenshot(CompressFormat.JPEG);
			return true;
		case R.id.menu_screenshot_png:
			this.screenshotCapturer.captureScreenshot(CompressFormat.PNG);
			return true;
		case R.id.enter_coordinate:
			showDialog(DIALOG_ENTER_COORDINATES);
			return true;
		case R.id.enter_node:
			showDialog(DIALOG_ENTER_NODE_ASAL_TUJUAN);
			return true;
//		case R.id.informasi_lokasi:
//			showDialog(INFORMASI_LOKASIKU);
//			return true;
		case R.id.about:
			startActivity(new Intent(this, InfoView.class));
			return true;
		case R.id.menu_preferences:
			startActivity(new Intent(this, EditPreferences.class));
			return true;
		case R.id.menu_render_theme:
			return true;
		case R.id.menu_render_theme_osmarender:
			this.petaForm.getMapView().setRenderTheme(InternalRenderTheme.OSMARENDER);
			return true;
		case R.id.menu_render_theme_select_file:
			startRenderThemePicker();
			return true;
		default:
			return false;
		}
	}

	public void menampilkanJarak() {
		double jarak3 = 0;
//		double jarak4= 0;
		Haversine haversine = new Haversine();
//		LatLongUtil latAziz = new LatLongUtil();
		if (koordinat.size() > 1) {
			for (int a = 1; a < koordinat.size(); a++) {
				overlayItems4.add(createPolyline1(
						new GeoPoint(koordinat.get((a - 1)).getLatitude(),
								koordinat.get((a - 1)).getLongitude()),
						new GeoPoint(koordinat.get(a).getLatitude(), koordinat
								.get(a).getLongitude())));
				petaForm.getMapView().getOverlays().add(listOverlay4);
				
				jarak3 = jarak3
						+ haversine.haversine1(koordinat.get((a - 1))
								.getLatitude(), koordinat.get((a - 1))
								.getLongitude(),
								koordinat.get(a).getLatitude(), koordinat
										.get(a).getLongitude());

			}
//			if (koordinat.size() > 3) {
//				jarak3 = jarak3
//						+ haversine.haversine1(
//								koordinat.get((koordinat.size() - 1))
//										.getLatitude(),
//								koordinat.get((koordinat.size() - 1))
//										.getLongitude(), koordinat.get(0)
//										.getLatitude(), koordinat.get(0)
//										.getLongitude());
//				overlayItems4.add(createPolyline1(
//						new GeoPoint(koordinat.get(koordinat.size() - 1)
//								.getLatitude(), koordinat.get(
//								koordinat.size() - 1).getLongitude()),
//						new GeoPoint(koordinat.get(0).getLatitude(), koordinat
//								.get(0).getLongitude())));
//			}
			
			petaForm.getLocationManager2().setText(
					"jarak Haversine "
							+ (koordinat.size() / 2)
							+ " Titik = "
							+ String.valueOf(new BigDecimal((jarak3 * 1000))
									.setScale(3, RoundingMode.HALF_EVEN))
							+ " Meter. atau "
							+ String.valueOf(new BigDecimal(jarak3).setScale(3,
									RoundingMode.HALF_EVEN)) + " KiloMeter ");
			petaForm.getMapView().getOverlayController().redrawOverlays();// methode
																// hasil
		}
	}

	@SuppressWarnings("deprecation")
	private void extracted() {
		showDialog(DIALOG_INFO_MAP_FILE);
	}
	private void startMapFilePicker() {
		FilePicker.setFileDisplayFilter(FILE_FILTER_EXTENSION_MAP);
		FilePicker.setFileSelectFilter(validMapFile);
		startActivityForResult(new Intent(this, FilePicker.class),SELECT_MAP_FILE);
	}

	/**
	 * Sets all file filters and starts the FilePicker to select an XML file.
	 */
	private void startRenderThemePicker() {
		FilePicker.setFileDisplayFilter(FILE_FILTER_EXTENSION_XML);
		FilePicker.setFileSelectFilter(new ValidRenderTheme());
		startActivityForResult(new Intent(this, FilePicker.class),SELECT_RENDER_THEME_FILE);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		if (id == DIALOG_ENTER_COORDINATES) {
			builder.setIcon(android.R.drawable.ic_menu_mylocation);
			builder.setTitle(R.string.menu_position_enter_coordinates);
			LayoutInflater factory = LayoutInflater.from(this);
			final View view = factory.inflate(
					R.layout.dialog_enter_coordinates, null);
			builder.setView(view);
			builder.setPositiveButton(R.string.go_to_position,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// disable GPS follow mode if it is enabled
							// disableSnapToLocation(true);
							// set the map center and zoom level
							EditText latitudeView = (EditText) view
									.findViewById(R.id.latitude);
							EditText longitudeView = (EditText) view
									.findViewById(R.id.longitude);
							double latitude = Double.parseDouble(latitudeView
									.getText().toString());
							double longitude = Double.parseDouble(longitudeView
									.getText().toString());
							GeoPoint geoPoint = new GeoPoint(latitude,
									longitude);
							SeekBar zoomLevelView = (SeekBar) view
									.findViewById(R.id.zoomLevel);
							MapPosition newMapPosition = new MapPosition(
									geoPoint, (byte) zoomLevelView
											.getProgress());
							petaForm.getMapView().getMapViewPosition()
									.setMapPosition(newMapPosition);
							Marker marker1 = createMarker(
									R.drawable.marker_blue4, geoPoint);
//							overlayItems2.removeAll(overlayItems2);
							overlayItems2.add(marker1);
							overlayItems2.add(createCircle1(new GeoPoint(
									latitude, longitude)));
							petaForm.getMapView().getOverlays()
									.add(listOverlay2);
							getPetaForm().getMapView().getOverlayController()
									.redrawOverlays();// methode untuk
														// mempercepat
														// menampilkan hasil
							koordinat1 = new Koordinat();
							koordinat1.setLatitude(latitude);
							koordinat1.setLongitude(longitude);
							koordinat.add(koordinat1);
							
						}
					});
			builder.setNegativeButton(R.string.cancel, null);
			return builder.create();

		} else if (id == DIALOG_ENTER_NODE_ASAL_TUJUAN) {
			overlayItems5.clear();
			overlayItems6.clear();
			overlayItems3.clear();
			koordinat.clear();
			
			builder.setIcon(android.R.drawable.ic_menu_mylocation);
			builder.setTitle(R.string.menu_position_enter_node_asal_tujuan);
			LayoutInflater factory = LayoutInflater.from(this);
			final View view = factory.inflate(
					R.layout.dialog_enter_node_asal_tujuan, null);
			builder.setView(view);			
			builder.setPositiveButton(R.string.show_rute,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {							 
							new AsyncTask<Void, Void, Void>() {
								@Override
								protected Void doInBackground(Void... params) {
									EditText nodeAsal3 = (EditText) view
											.findViewById(R.id.latitude);
									EditText nodeTujuan3 = (EditText) view
											.findViewById(R.id.longitude);						
									
									konversiIdNodePetaToIdNodeAsal(nodeAsal3.getText().toString());
									konversiIdNodePetaToIdNodeTujuan(nodeTujuan3.getText().toString());					

									floydWarshall_proses(idNodeAsal2,idNodeTujuan2);
									return null;
								}
								protected void onPostExecute(Void result) {	
									showToastOnUiThread("Floyd-Warshall Finish");
									getPetaForm().getLocationManager3().setText("Node Asal: " 
											 + nodeTempatkuBerdiri+" Node Tujuan: "
											 +nodeTujuanku+" Jarak Meter : "+String.valueOf(new BigDecimal((Double.parseDouble(graphAdapter.getJarak()))).setScale(3,
														RoundingMode.HALF_EVEN)));									
									getPetaForm().getLocationManager2().setText("Koordinat Asal :"+latitudeKitaBerdiri+" , "+longitudeKitaBerdiri
											+" Koordinat Tujuan :"+latitudeMatchingTujuan+" , "+longitudeMatchingTujuan);
									petaForm.getMapView().getOverlayController().redrawOverlays();
									}
								
							}.execute();
													
						}
					});
			builder.setNegativeButton(R.string.cancel, null);
			return builder.create();

		}

		else if (id == INFORMASI_LOKASIKU) {
			if (getLocation() != null) {
				builder.setIcon(android.R.drawable.ic_menu_mylocation);
				builder.setTitle(R.string.informasi_lokasi);
				LayoutInflater factory = LayoutInflater.from(this);
				final View view = factory.inflate(R.layout.informasi_lokasiku,
						null);
				builder.setView(view);
				TextView latitude = (TextView) view.findViewById(R.id.latitude);
				TextView longitude = (TextView) view
						.findViewById(R.id.longitude);
				TextView ketinggian = (TextView) view
						.findViewById(R.id.ketinggian);
				TextView keakuratan = (TextView) view
						.findViewById(R.id.keakuratan);
				TextView providerGps = (TextView) view
						.findViewById(R.id.provider_gps);
				latitude.setText("Latitude: "
						+ getLocation().getLatitude()
						+ " "
						+ konversiKoordinat
								.konversiKoordinatLatitude(getLocation()));
				longitude.setText("Longitude: "
						+ getLocation().getLongitude()
						+ " "
						+ konversiKoordinat
								.konversiKoordinatLongitude(getLocation())
						+ ")");
				ketinggian.setText("Ketinggian diatas permukaan laut Meter : "
						+ String.valueOf(new BigDecimal(getLocation()
								.getAltitude()).setScale(5,
								RoundingMode.HALF_EVEN)));
				keakuratan.setText("Keakuratan Meter :"
						+ getLocation().getAccuracy());
				providerGps.setText("Provider GPS :"
						+ getLocation().getProvider());

				builder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				builder.setNegativeButton(R.string.cancel, null);
			} else {
				showToastOnUiThread("Menunggu Posisi Dari GPS");
			}
			return builder.create();
		}
		else if (id == SHOW_RUTE) {
				builder.setIcon(android.R.drawable.ic_menu_mylocation);
				builder.setTitle(R.string.floyd_warshall);
				LayoutInflater factory = LayoutInflater.from(this);
				final View view = factory.inflate(R.layout.hasil_floyd_warshall,
						null);
				builder.setView(view);
				TextView titikAsal = (TextView) view.findViewById(R.id.titik_asal);
				TextView titikTujuan = (TextView) view
						.findViewById(R.id.titik_tujuan);
				TextView jarak = (TextView) view
						.findViewById(R.id.jarak);
				TextView rute1 = (TextView) view
						.findViewById(R.id.rute);
				TextView rute2 = (TextView) view
						.findViewById(R.id.rute1);
				titikAsal.setText("Node Asal: "+nodeTempatkuBerdiri);
				titikTujuan.setText("Node Tujuan: "+nodeTujuanku);				
				jarak.setText("Jarak Meter: "+graphAdapter.getJarak());
				rute1.setText("Rute :"+rute);
				rute2.setText("");				
				builder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				builder.setNegativeButton(R.string.cancel, null);

			return builder.create();

		}
		else if (id == DIALOG_LOCATION_PROVIDER_DISABLED) {
			builder.setIcon(android.R.drawable.ic_menu_info_details);
			builder.setTitle(R.string.error);
			builder.setMessage(R.string.no_location_provider_available);
			builder.setPositiveButton(R.string.ok, null);
			return builder.create();
		} else if (id == DIALOG_INFO_MAP_FILE) {
			builder.setIcon(android.R.drawable.ic_menu_info_details);
			builder.setTitle(R.string.menu_info_map_file);
			LayoutInflater factory = LayoutInflater.from(this);
			builder.setView(factory
					.inflate(R.layout.dialog_info_map_file, null));
			builder.setPositiveButton(R.string.ok, null);
			builder.setInverseBackgroundForced(true);

			return builder.create();
		} else {
			// do dialog will be created
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	protected void onPrepareDialog(int id, final Dialog dialog) {
		if (id == DIALOG_ENTER_COORDINATES) {
			// latitude
			EditText editText = (EditText) dialog.findViewById(R.id.latitude);
			MapViewPosition mapViewPosition = this.petaForm.getMapView()
					.getMapViewPosition();
			GeoPoint mapCenter = mapViewPosition.getCenter();
			editText.setText(Double.toString(mapCenter.latitude));

			// longitude
			editText = (EditText) dialog.findViewById(R.id.longitude);
			editText.setText(Double.toString(mapCenter.longitude));

			// zoom level
			SeekBar zoomlevel = (SeekBar) dialog.findViewById(R.id.zoomLevel);
			zoomlevel.setMax(this.petaForm.getMapView().getDatabaseRenderer()
					.getZoomLevelMax());
			zoomlevel.setProgress(mapViewPosition.getZoomLevel());
			// zoom level value
			final TextView textView = (TextView) dialog
					.findViewById(R.id.zoomlevelValue);
			textView.setText(String.valueOf(zoomlevel.getProgress()));
			zoomlevel.setOnSeekBarChangeListener(new SeekBarChangeListener(
					textView));
		} else if (id == DIALOG_INFO_MAP_FILE) {
			MapFileInfo mapFileInfo = this.petaForm.getMapView()
					.getMapDatabase().getMapFileInfo();

			// map file name
			TextView textView = (TextView) dialog
					.findViewById(R.id.infoMapFileViewName);
			textView.setText(this.petaForm.getMapView().getMapFile()
					.getAbsolutePath());

			// map file size
			textView = (TextView) dialog.findViewById(R.id.infoMapFileViewSize);
			textView.setText(FileUtils.formatFileSize(mapFileInfo.fileSize,
					getResources()));

			// map file version
			textView = (TextView) dialog
					.findViewById(R.id.infoMapFileViewVersion);
			textView.setText(String.valueOf(mapFileInfo.fileVersion));

			// map file debug
			textView = (TextView) dialog
					.findViewById(R.id.infoMapFileViewDebug);
			if (mapFileInfo.debugFile) {
				textView.setText(R.string.info_map_file_debug_yes);
			} else {
				textView.setText(R.string.info_map_file_debug_no);
			}

			// map file date
			textView = (TextView) dialog.findViewById(R.id.infoMapFileViewDate);
			Date date = new Date(mapFileInfo.mapDate);
			textView.setText(DateFormat.getDateTimeInstance().format(date));

			// map file area
			textView = (TextView) dialog.findViewById(R.id.infoMapFileViewArea);
			BoundingBox boundingBox = mapFileInfo.boundingBox;
			textView.setText(boundingBox.minLatitude + ", "
					+ boundingBox.minLongitude + " � \n"
					+ boundingBox.maxLatitude + ", " + boundingBox.maxLongitude);

			// map file start position
			textView = (TextView) dialog
					.findViewById(R.id.infoMapFileViewStartPosition);
			GeoPoint startPosition = mapFileInfo.startPosition;
			if (startPosition == null) {
				textView.setText(null);
			} else {
				textView.setText(startPosition.latitude + ", "
						+ startPosition.longitude);
			}

			// map file start zoom level
			textView = (TextView) dialog
					.findViewById(R.id.infoMapFileViewStartZoomLevel);
			Byte startZoomLevel = mapFileInfo.startZoomLevel;
			if (startZoomLevel == null) {
				textView.setText(null);
			} else {
				textView.setText(startZoomLevel.toString());
			}
			// map file language preference
			textView = (TextView) dialog
					.findViewById(R.id.infoMapFileViewLanguagePreference);
			textView.setText(mapFileInfo.languagePreference);

			// map file comment text
			textView = (TextView) dialog
					.findViewById(R.id.infoMapFileViewComment);
			textView.setText(mapFileInfo.comment);

			// map file created by text
			textView = (TextView) dialog
					.findViewById(R.id.infoMapFileViewCreatedBy);
			textView.setText(mapFileInfo.createdBy);
		} else {
			super.onPrepareDialog(id, dialog);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == SELECT_MAP_FILE) {
			if (resultCode == RESULT_OK) {
				if (intent != null
						&& intent.getStringExtra(FilePicker.SELECTED_FILE) != null) {
					this.petaForm.getMapView().setMapFile(
							new File(intent
									.getStringExtra(FilePicker.SELECTED_FILE)));
				}
			} else if (resultCode == RESULT_CANCELED
					&& this.petaForm.getMapView().getMapFile() == null) {
				finish();
			}
		} else if (requestCode == SELECT_RENDER_THEME_FILE
				&& resultCode == RESULT_OK && intent != null
				&& intent.getStringExtra(FilePicker.SELECTED_FILE) != null) {
			try {
				this.petaForm.getMapView().setRenderTheme(
						new File(intent
								.getStringExtra(FilePicker.SELECTED_FILE)));
			} catch (FileNotFoundException e) {
				showToastOnUiThread(e.getLocalizedMessage());
			}
		}
	}

	@SuppressLint("Wakelock")
	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		MapScaleBar mapScaleBar = this.petaForm.getMapView().getMapScaleBar();
		mapScaleBar.setShowMapScaleBar(sharedPreferences.getBoolean(
				"showScaleBar", false));
		String scaleBarUnitDefault = getString(R.string.preferences_scale_bar_unit_default);
		String scaleBarUnit = sharedPreferences.getString("scaleBarUnit",
				scaleBarUnitDefault);
		mapScaleBar.setImperialUnits(scaleBarUnit.equals("imperial"));
		try {
			String textScaleDefault = getString(R.string.preferences_text_scale_default);
			this.petaForm.getMapView().setTextScale(
					Float.parseFloat(sharedPreferences.getString("textScale",
							textScaleDefault)));
		} catch (NumberFormatException e) {
			this.petaForm.getMapView().setTextScale(1);
		}

		if (sharedPreferences.getBoolean("fullscreen", false)) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		} else {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
		if (sharedPreferences.getBoolean("wakeLock", false)
				&& !this.wakeLock.isHeld()) {
			this.wakeLock.acquire();
		}

		boolean persistent = sharedPreferences.getBoolean("cachePersistence",
				false);
		int capacity = Math.min(sharedPreferences.getInt("cacheSize",
				FILE_SYSTEM_CACHE_SIZE_DEFAULT), FILE_SYSTEM_CACHE_SIZE_MAX);
		TileCache fileSystemTileCache = this.petaForm.getMapView()
				.getFileSystemTileCache();
		fileSystemTileCache.setPersistent(persistent);
		fileSystemTileCache.setCapacity(capacity);
		float moveSpeedFactor = Math.min(
				sharedPreferences.getInt("moveSpeed", MOVE_SPEED_DEFAULT),
				MOVE_SPEED_MAX) / 10f;
		this.petaForm.getMapView().getMapMover()
				.setMoveSpeedFactor(moveSpeedFactor);
		this.petaForm
				.getMapView()
				.getFpsCounter()
				.setFpsCounter(
						sharedPreferences.getBoolean("showFpsCounter", false));

		boolean drawTileFrames = sharedPreferences.getBoolean("drawTileFrames",
				false);
		boolean drawTileCoordinates = sharedPreferences.getBoolean(
				"drawTileCoordinates", false);
		boolean highlightWaterTiles = sharedPreferences.getBoolean(
				"highlightWaterTiles", false);
		DebugSettings debugSettings = new DebugSettings(drawTileCoordinates,
				drawTileFrames, highlightWaterTiles);
		this.petaForm.getMapView().setDebugSettings(debugSettings);
		if (this.petaForm.getMapView().getMapFile() == null) {
			startMapFilePicker();
		}
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	private static void copyMap(){
		String name = "surabaya_new.map";
		String path = Environment.getExternalStorageDirectory()+ "/routing_engine/maps/";		
		
		File dir = new File(path);
		File file = new File(path + name);
		
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		if(!file.exists()){			
			try {
				
				InputStream mInput = RoutingEngine.getAppContext().getAssets().open(name);
				String outFileName = path + name;
				OutputStream mOutput = new FileOutputStream(outFileName);
				byte[] mBuffer = new byte[1024];
				int mLength;
				while ((mLength = mInput.read(mBuffer)) > 0) {
					mOutput.write(mBuffer, 0, mLength);
				}
				mOutput.flush();
				mOutput.close();
				mInput.close();
				
				Log.i(TAG, "copy succes..");
			} catch(IOException e) {
				throw new Error(e);
			}
		}	
	}
}
