package com.app.checklist.model.db;

import android.content.Context;

import com.app.checklist.model.Values;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ValuesRepository {

	private DatabaseHelper databaseHelper;
	private Dao<Values, Integer> cityDao;
	private static ValuesRepository ventasRepository = null;


	private ValuesRepository(Context ctx)
	{
		try {
			DatabaseManager dbManager = new DatabaseManager();
			databaseHelper = dbManager.getHelper(ctx);
			cityDao = databaseHelper.getValuesDao();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static ValuesRepository getInstance(Context context){
		if(ventasRepository == null)
			ventasRepository = new ValuesRepository(context);
		return ventasRepository;
	}

	public int create(Values dao)
	{
		try {
			return cityDao.create(dao);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public int update(Values dao)
	{
		try {
			return cityDao.update(dao);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public int delete(Values dao)
	{
		try {
			return cityDao.delete(dao);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Values> getAll()
	{
		try {
			
			return cityDao.queryForAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	
	public List<Values> queryForEq(String field, Object val)
	{
		try {
			
			return cityDao.queryForEq(field, val);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

    public Values queryForId(int id)
    {
        try {

            return cityDao.queryForId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

	public RawRowMapper<Values> getMapper(){
		return new RawRowMapper<Values>() {
			@Override
			public Values mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
				Values vnt= new Values(columnNames,resultColumns);
				return vnt;
			}
		};
	}


	public List<Values> getEdited()
	{
		List<Values> result = new ArrayList<>();
		try {
			String queryS= "select * from data where edited=1 and sync=0";
			GenericRawResults<Values> rawResults = cityDao.queryRaw(queryS,getMapper());
			for (Values vnt : rawResults) {
				result.add(vnt);
			}
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	public List<Values> getSync()
	{
		List<Values> result = new ArrayList<>();
		try {
			String queryS= "select * from data where edited=1 and sync=1";
			GenericRawResults<Values> rawResults = cityDao.queryRaw(queryS,getMapper());
			for (Values vnt : rawResults) {
				result.add(vnt);
			}
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	public List<Values> buscar(String buscar)
	{
		List<Values> result = new ArrayList<>();
		try {
			String queryS= "select * from data where field1 like '%"+buscar+"%' or field2 like '%"+buscar+"%' or field3 like '%"+buscar+"%' or field4 like '%"+buscar+"%' or field5 like '%"+buscar+"%' or field6 like '%"+buscar+"%' or field7 like '%"+buscar+"%' or field8 like '%"+buscar+"%' or field9 like '%"+buscar+"%' or field10 like '%"+buscar+"%' or field11 like '%"+buscar+"%' or field12 like '%"+buscar+"%' or field13 like '%"+buscar+"%' or field14 like '%"+buscar+"%' or field15 like '%"+buscar+"%'";
			GenericRawResults<Values> rawResults = cityDao.queryRaw(queryS,getMapper());
			for (Values vnt : rawResults) {
				result.add(vnt);
			}
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}



    public void releaseHelper(){
		
	}


}