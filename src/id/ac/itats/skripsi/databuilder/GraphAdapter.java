package id.ac.itats.skripsi.databuilder;

import id.ac.itats.skripsi.ainul.DaoMaster;
import id.ac.itats.skripsi.ainul.DaoSession;
import id.ac.itats.skripsi.ainul.HasilFloydWarshall;
import id.ac.itats.skripsi.ainul.HasilFloydWarshallDao;
import id.ac.itats.skripsi.ainul.NodeMaster;
import id.ac.itats.skripsi.ainul.NodeMasterDao;
import id.ac.itats.skripsi.ainul.SatuArah;
import id.ac.itats.skripsi.ainul.SatuArahDao;
import id.ac.itats.skripsi.routingengine.ainul.RoutingEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GraphAdapter {
	protected static final String TAG = "GraphAdapter";

	private SQLiteDatabase mDb;
	private DataBaseHelper mDbHelper;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
//	private HasilFloydWarshallbDao hasilFloydWarshallDao;
	private HasilFloydWarshallDao hasilFloydWarshallDao_2;
	private NodeMasterDao nodeMasterDao;
	private SatuArahDao satuArahDao;
//	private Matriks_jarak_predecessor_inisialisasi_masterDao matriks_jarak_predecessor_inisialisasi_masterDao;
	private List<String> hasilAkhir = new ArrayList<String>();
	private List<Long> hasilAkhir_2 = new ArrayList<Long>();
//	private List<String> namaJalan = new ArrayList<String>();
	private List<NodeMaster> shortestPath = new ArrayList<NodeMaster>();
	private List<NodeMaster> shortestPath_2 = new ArrayList<NodeMaster>();
	private List<NodeMaster> nodeMaster = new ArrayList<NodeMaster>();
	private List<SatuArah> satuArah = new ArrayList<SatuArah>();
	private List<NodeMaster> idNodeAsal = new ArrayList<NodeMaster>();
	private List<NodeMaster> idNodeTujuan = new ArrayList<NodeMaster>();
	private String jarak;
	private String namaJalan_2;
//	private List<Matriks_jarak_predecessor_inisialisasi_master> matriks_jarak_predecessor_inisialisasi_master = new ArrayList<Matriks_jarak_predecessor_inisialisasi_master>();
	private String jarakTerpendek="";
	private int jumlahNodeMaster;

	

	public GraphAdapter() {
		mDbHelper = new DataBaseHelper(RoutingEngine.getAppContext());
	}

	public GraphAdapter createDatabase() throws SQLException {
		try {
			mDbHelper.createDataBase();
		} catch (IOException mIOException) {
			Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
			throw new Error("Unable to create database");

		}
		return this;
	}

	public GraphAdapter open() throws SQLException {
		try {
			mDbHelper.openDataBase();
			mDbHelper.close();
			mDb = mDbHelper.getReadableDatabase();
			daoMaster = new DaoMaster(mDb);
			daoSession = daoMaster.newSession();
//			hasilFloydWarshallDao = daoSession.getHasilFloydWarshallbDao();
			hasilFloydWarshallDao_2 = daoSession.getHasilFloydWarshallDao();
			nodeMasterDao = daoSession.getNodeMasterDao();
			satuArahDao = daoSession.getSatuArahDao();
//			matriks_jarak_predecessor_inisialisasi_masterDao = daoSession.getMatriks_jarak_predecessor_inisialisasi_masterDao();

		} catch (SQLException mSQLException) {
			Log.e(TAG, "open >>" + mSQLException.toString());
			throw mSQLException;
		}
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	// XXX GRAPHQUERY
//	public HasilFloydWarshallb getHasil(String asal, String tujuan) {
//		HasilFloydWarshallb result = hasilFloydWarshallDao
//				.queryBuilder()
//				.where(HasilFloydWarshallbDao.Properties.IdNodeAsal.eq(asal),
//						HasilFloydWarshallbDao.Properties.IdNodeTujuan
//								.eq(tujuan)).list().get(0);
//
//		return result;
//	}
	
	// XXX GRAPHQUERY
		public HasilFloydWarshall getHasil2(Long asal, Long tujuan) {
			HasilFloydWarshall result = hasilFloydWarshallDao_2
					.queryBuilder()
					.where(HasilFloydWarshallDao.Properties.IdNodeAsal.eq(String.valueOf(asal)),
							HasilFloydWarshallDao.Properties.IdNodeTujuan
									.eq(String.valueOf(tujuan))).list().get(0);

			return result;
		}

//	public void menampilanRuteDanJarak(String asal, String tujuan) {
//		hasilAkhir.clear();
//		shortestPath.clear();
//		adding(tujuan);
//
//		try {
//			String path1 = getHasil(asal, tujuan).getPath(); // pasti entok
//			jarakTerpendek = getHasil(asal, tujuan).getJarak(); // pasti entok
//			setJarak(jarakTerpendek);
//			System.out.println(" Tujuan " + tujuan+" asal " + asal);			
//			System.out.println(" jarak Terpendek "+jarakTerpendek);			
//			do {
//				String k = path1;
//				tujuan = k;
////				System.out.println("hasil " + k);
//				adding(k);
//					
//				HasilFloydWarshallb result = getHasil(asal, tujuan);
//				path1 = result.getPath();
//				 System.out.println("path "+ path1);
//				if(path1!=null){
//					adding(path1);
//				}
//			} while (asal != tujuan);
//			adding(asal);
//			
//		} catch (Exception e) {
////			System.out.println("Gak ada jalan");
//		}
//	}

	public List<NodeMaster> getShortestPath() {			
			for (String i : hasilAkhir){								
				shortestPath.add(nodeMasterDao.queryBuilder()
						.where(NodeMasterDao.Properties.IdNodePeta.eq(i))
						.list().get(0));			
			}

		return shortestPath;
	}
	public List<SatuArah> getSatuArah() {			
		for (SatuArah i : satuArah){								
			satuArah.add(satuArahDao.queryBuilder()
					.where(SatuArahDao.Properties.WayId.eq(i))
					.list().get(0));	
			setNamaJalan_2(satuArah.get(0).getNamaJalan());
		}

	return satuArah;
}
	
	public List<NodeMaster> getShortestPath_2() {			
		for (Long i : hasilAkhir_2){								
			shortestPath_2.add(nodeMasterDao.queryBuilder()
					.where(NodeMasterDao.Properties.IdNode.eq(i))
					.list().get(0));			
		}		
	return shortestPath_2;
}
	
	
	public List<NodeMaster> getNodeMaster() {	
		nodeMaster.clear();		
		for (int i=1;i<(getJumlahNodeMaster()+1);i++){			
		nodeMaster.add(nodeMasterDao.queryBuilder()
					.where(NodeMasterDao.Properties.IdNode.ge(i)).list().get(0));
//		System.out.println("Node Master : "+(i+1)+". "+nodeMasterDao.queryBuilder()
//				.where(NodeMasterDao.Properties.IdNode.ge(i)).list().get(0).toString());					
		}		
	return nodeMaster;
}
	
	public List<NodeMaster> mencariIdNodeAsal(String idNodepeta) {							
		idNodeAsal.add(nodeMasterDao.queryBuilder()
					.where(NodeMasterDao.Properties.IdNodePeta.eq(idNodepeta)).list().get(0));		
		
	return idNodeAsal;
}
	
	public List<NodeMaster> mencariIdNodeTujuan(String idNodepeta) {							
		idNodeTujuan.add(nodeMasterDao.queryBuilder()
					.where(NodeMasterDao.Properties.IdNodePeta.eq(idNodepeta)).list().get(0));		
		
	return idNodeTujuan;
}

	
//	private void adding(String id){
//		if (!hasilAkhir.contains(id))
//		 hasilAkhir.add(id);
//	}
	
	private void adding_2(Long id){
		if (!hasilAkhir_2.contains(id))
		 hasilAkhir_2.add(id);
	}
//	private void addingNamaJalan(String wayId){
//		if (!namaJalan.contains(wayId))
//			namaJalan.add(wayId);
//	}
	
	
	public void menampilanRuteDanJarak_2(Long asal, Long tujuan) {
		hasilAkhir_2.clear();
		shortestPath_2.clear();		
		adding_2(asal);
		adding_2(tujuan);
		try {
			Long path1 = Long.valueOf(getHasil2(asal, tujuan).getPath()); // pasti entok

			jarakTerpendek = getHasil2(asal, tujuan).getJarak(); // pasti entok
			if(jarakTerpendek.equals(null)){
				setJarak("0");
			}
			else{
				setJarak(jarakTerpendek);
			}
			
			System.out.println(" Tujuan " + tujuan+" , asal " + asal+" , path "+path1);			
			System.out.println(" jarak Terpendek Meter "+jarakTerpendek);
			
			do {
				Long k = path1;
				tujuan = k;
//				System.out.println("hasil " + k);
				adding_2(k);	
				path1=null;				
				HasilFloydWarshall result = getHasil2(asal, tujuan);
				path1 = Long.valueOf(result.getPath());
				 System.out.println("path "+ path1);	
//				 String wayId=result.getWayId();
				 System.out.println(" Way Id "+result.getWayId());
				if(path1!=null){				
						if(hasilAkhir_2.contains(path1)==true){
							System.out.println("path Sudah ada");
							break;
						}
						else{
							adding_2(path1);
							
						}
//						if(namaJalan.contains(wayId)==true){
//							System.out.println("nama Jalan Sudah ada");							
//						}
//						else{							
//							addingNamaJalan(wayId);
//							System.out.println(" Way Id "+result.getWayId());	
//						}					
				}				
				 if(path1==null){
		        	   System.out.println("Stop Gak ada jalan");
		       			 break;
		       		 }
			} while (asal != tujuan);			
			
		} catch (Exception e) {
//			System.out.println("Gak ada jalan");
		}
	}
	
	public String getJarak() {
		return jarak;
	}

	public void setJarak(String jarak) {
		this.jarak = jarak;
	}

	public int getJumlahNodeMaster() {
		return jumlahNodeMaster;
	}

	public void setJumlahNodeMaster(int jumlahNodeMaster) {
		this.jumlahNodeMaster = jumlahNodeMaster;
	}
	public String getNamaJalan_2() {
		return namaJalan_2;
	}

	public void setNamaJalan_2(String namaJalan_2) {
		this.namaJalan_2 = namaJalan_2;
	}
	
//	public List<Matriks_jarak_predecessor_inisialisasi_master> getMatriks_jarak_predecessor_inisialisasi_master() {	
//		nodeMaster.clear();		
//		for (int i=1;i<(1463+1);i++){			
//			matriks_jarak_predecessor_inisialisasi_master.add(matriks_jarak_predecessor_inisialisasi_masterDao.queryBuilder()
//					.where(Matriks_jarak_predecessor_inisialisasi_masterDao.Properties.IdMatriksjarakInisialisasi.ge(i)).list().get(0));
//							
//		}		
//	return matriks_jarak_predecessor_inisialisasi_master;
//}

}
