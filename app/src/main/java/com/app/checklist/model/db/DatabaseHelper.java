package com.app.checklist.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.app.checklist.model.Entity;
import com.app.checklist.model.Items;
import com.app.checklist.model.Values;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    public static final String DATABASE_NAME = "form.sqlite";
    // path to store the database
    public static String DB_PATH = "";//Environment.getExternalStorageDirectory().getAbsolutePath() + "/chova/";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // Database object
    private SQLiteDatabase db;

    private Context context;

    // the DAO object we use to access the SimpleData table
    private Dao<Entity, Integer> entityDao = null;
    public Dao<Entity, Integer> getEntityDao() throws SQLException {
        if (entityDao == null) {
            entityDao = this.getDao(Entity.class);
        }
        return entityDao;
    }

    private Dao<Values, Integer> valuesDao = null;
    public Dao<Values, Integer> getValuesDao() throws SQLException {
        if (valuesDao == null) {
            valuesDao = this.getDao(Values.class);
        }
        return valuesDao;
    }

    private Dao<Items, Integer> itemsDao = null;
    public Dao<Items, Integer> getItemsDao() throws SQLException {
        if (itemsDao == null) {
            itemsDao = this.getDao(Items.class);
        }
        return itemsDao;
    }



    public DatabaseHelper(Context context) {
        super(context, DB_PATH, null, DATABASE_VERSION);
    }
    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */


    

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
//    public Dao<Pantalla, Integer> getProductDao() throws SQLException {
//        if (productDao == null) {
//            productDao = this.getDao(Pantalla.class);
//        }
//        return productDao;
//    }

   
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
//
//    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
//        try {
//            //TableUtils.createTable(connectionSource, Alojamientos.class);
//           // TableUtils.createTable(connectionSource, Pantalla.class);
//    
//           // TableUtils.createTable(connectionSource, WishItem.class);
//        } catch (SQLException e) {
//            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

    }


    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        if(db != null && db.isOpen()) { db.close(); }

        super.close();
        //preguntasDao = null;
       // productDao = null;
    }


}