package com.app.checklist.model.db;

import android.content.Context;

import com.app.checklist.model.Entity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;


public class EntityRepository {

	private DatabaseHelper databaseHelper;
	private Dao<Entity, Integer> cityDao;
	private static EntityRepository ventasRepository = null;


	private EntityRepository(Context ctx)
	{
		try {
			DatabaseManager dbManager = new DatabaseManager();
			databaseHelper = dbManager.getHelper(ctx);
			cityDao = databaseHelper.getEntityDao();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static EntityRepository getInstance(Context context){
		if(ventasRepository == null)
			ventasRepository = new EntityRepository(context);
		return ventasRepository;
	}

	public int create(Entity dao)
	{
		try {
			return cityDao.create(dao);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public int update(Entity dao)
	{
		try {
			return cityDao.update(dao);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public int delete(Entity dao)
	{
		try {
			return cityDao.delete(dao);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Entity> getAll()
	{
		try {
			
			return cityDao.queryForAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	
	public List<Entity> queryForEq(String field, Object val)
	{
		try {
			
			return cityDao.queryForEq(field, val);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

    public Entity queryForId(int id)
    {
        try {

            return cityDao.queryForId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }







    public void releaseHelper(){
		
	}


}