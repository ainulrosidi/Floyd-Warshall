package id.ac.itats.skripsi.ainul;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import id.ac.itats.skripsi.ainul.SatuArah;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table Satu_Arah.
*/
public class SatuArahDao extends AbstractDao<SatuArah, Long> {

    public static final String TABLENAME = "Satu_Arah";

    /**
     * Properties of entity SatuArah.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property IdSatuArah = new Property(0, Long.class, "idSatuArah", true, "Id_Satu_Arah");
        public final static Property WayId = new Property(1, String.class, "wayId", false, "Way_Id");
        public final static Property SatuArah = new Property(2, String.class, "satuArah", false, "Satu_Arah");
        public final static Property NamaJalan = new Property(3, String.class, "namaJalan", false, "Nama_Jalan");
    };


    public SatuArahDao(DaoConfig config) {
        super(config);
    }
    
    public SatuArahDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'Satu_Arah' (" + //
                "'Id_Satu_Arah' INTEGER PRIMARY KEY ," + // 0: idSatuArah
                "'Way_Id' TEXT," + // 1: wayId
                "'Satu_Arah' TEXT," + // 2: satuArah
                "'Nama_Jalan' TEXT);"); // 3: namaJalan
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_Satu_Arah_Id_Satu_Arah ON Satu_Arah" +
                " (Id_Satu_Arah);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'Satu_Arah'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SatuArah entity) {
        stmt.clearBindings();
 
        Long idSatuArah = entity.getIdSatuArah();
        if (idSatuArah != null) {
            stmt.bindLong(1, idSatuArah);
        }
 
        String wayId = entity.getWayId();
        if (wayId != null) {
            stmt.bindString(2, wayId);
        }
 
        String satuArah = entity.getSatuArah();
        if (satuArah != null) {
            stmt.bindString(3, satuArah);
        }
 
        String namaJalan = entity.getNamaJalan();
        if (namaJalan != null) {
            stmt.bindString(4, namaJalan);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SatuArah readEntity(Cursor cursor, int offset) {
        SatuArah entity = new SatuArah( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // idSatuArah
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // wayId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // satuArah
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // namaJalan
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SatuArah entity, int offset) {
        entity.setIdSatuArah(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setWayId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSatuArah(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNamaJalan(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SatuArah entity, long rowId) {
        entity.setIdSatuArah(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SatuArah entity) {
        if(entity != null) {
            return entity.getIdSatuArah();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
