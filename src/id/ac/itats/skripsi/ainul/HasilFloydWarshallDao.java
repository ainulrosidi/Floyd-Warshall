package id.ac.itats.skripsi.ainul;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import id.ac.itats.skripsi.ainul.HasilFloydWarshall;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table Hasil_Floyd_Warshall.
*/
public class HasilFloydWarshallDao extends AbstractDao<HasilFloydWarshall, Long> {

    public static final String TABLENAME = "Hasil_Floyd_Warshall";

    /**
     * Properties of entity HasilFloydWarshall.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property IdMatriksjarakInisialisasi = new Property(0, Long.class, "idMatriksjarakInisialisasi", true, "Id_Matriks_jarak_inisialisasi");
        public final static Property IdNodeAsal = new Property(1, String.class, "idNodeAsal", false, "Id_Node_Asal");
        public final static Property IdNodeTujuan = new Property(2, String.class, "idNodeTujuan", false, "Id_Node_Tujuan");
        public final static Property Jarak = new Property(3, String.class, "jarak", false, "Jarak");
        public final static Property Path = new Property(4, String.class, "path", false, "Path");
        public final static Property WayId = new Property(5, String.class, "wayId", false, "Way_Id");
    };


    public HasilFloydWarshallDao(DaoConfig config) {
        super(config);
    }
    
    public HasilFloydWarshallDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'Hasil_Floyd_Warshall' (" + //
                "'Id_Matriks_jarak_inisialisasi' INTEGER PRIMARY KEY ," + // 0: idMatriksjarakInisialisasi
                "'Id_Node_Asal' TEXT," + // 1: idNodeAsal
                "'Id_Node_Tujuan' TEXT," + // 2: idNodeTujuan
                "'Jarak' TEXT," + // 3: jarak
                "'Path' TEXT," + // 4: path
                "'Way_Id' TEXT);"); // 5: wayId
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_Hasil_Floyd_Warshall_Id_Matriks_jarak_inisialisasi ON Hasil_Floyd_Warshall" +
                " (Id_Matriks_jarak_inisialisasi);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'Hasil_Floyd_Warshall'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, HasilFloydWarshall entity) {
        stmt.clearBindings();
 
        Long idMatriksjarakInisialisasi = entity.getIdMatriksjarakInisialisasi();
        if (idMatriksjarakInisialisasi != null) {
            stmt.bindLong(1, idMatriksjarakInisialisasi);
        }
 
        String idNodeAsal = entity.getIdNodeAsal();
        if (idNodeAsal != null) {
            stmt.bindString(2, idNodeAsal);
        }
 
        String idNodeTujuan = entity.getIdNodeTujuan();
        if (idNodeTujuan != null) {
            stmt.bindString(3, idNodeTujuan);
        }
 
        String jarak = entity.getJarak();
        if (jarak != null) {
            stmt.bindString(4, jarak);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(5, path);
        }
 
        String wayId = entity.getWayId();
        if (wayId != null) {
            stmt.bindString(6, wayId);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public HasilFloydWarshall readEntity(Cursor cursor, int offset) {
        HasilFloydWarshall entity = new HasilFloydWarshall( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // idMatriksjarakInisialisasi
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // idNodeAsal
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // idNodeTujuan
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // jarak
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // path
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // wayId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, HasilFloydWarshall entity, int offset) {
        entity.setIdMatriksjarakInisialisasi(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIdNodeAsal(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIdNodeTujuan(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setJarak(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPath(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setWayId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(HasilFloydWarshall entity, long rowId) {
        entity.setIdMatriksjarakInisialisasi(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(HasilFloydWarshall entity) {
        if(entity != null) {
            return entity.getIdMatriksjarakInisialisasi();
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