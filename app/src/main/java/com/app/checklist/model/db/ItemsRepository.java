package com.app.checklist.model.db;

import android.content.Context;

import com.app.checklist.model.Items;
import com.app.checklist.model.Values;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ItemsRepository {

	private DatabaseHelper databaseHelper;
	private Dao<Items, Integer> cityDao;
	private static ItemsRepository ventasRepository = null;


	private ItemsRepository(Context ctx)
	{
		try {
			DatabaseManager dbManager = new DatabaseManager();
			databaseHelper = dbManager.getHelper(ctx);
			cityDao = databaseHelper.getItemsDao();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static ItemsRepository getInstance(Context context){
		if(ventasRepository == null)
			ventasRepository = new ItemsRepository(context);
		return ventasRepository;
	}

	public int create(Items dao)
	{
		try {
			return cityDao.create(dao);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public int update(Items dao)
	{
		try {
			return cityDao.update(dao);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public int delete(Items dao)
	{
		try {
			return cityDao.delete(dao);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Items> getAll(String guia)
	{
		try {
			
//			return cityDao.queryForAll();
			return cityDao.queryForEq("guia", guia);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean deleteAll(String guia)
	{
		try {
			String queryS= "delete from items where guia = '"+guia+"'";
			cityDao.queryRaw(queryS,getMapper());
			return true;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	
	public List<Items> queryForEq(String field, Object val)
	{
		try {
			
			return cityDao.queryForEq(field, val);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

    public Items queryForId(int id)
    {
        try {

            return cityDao.queryForId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

	public RawRowMapper<Items> getMapper(){
		return new RawRowMapper<Items>() {
			@Override
			public Items mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
				Items vnt= new Items(columnNames,resultColumns);
				return vnt;
			}
		};
	}


	public List<Items> getEdited()
	{
		List<Items> result = new ArrayList<>();
		try {
			String queryS= "select * from items where edited=1 and sync=0";
			GenericRawResults<Items> rawResults = cityDao.queryRaw(queryS,getMapper());
			for (Items vnt : rawResults) {
				result.add(vnt);
			}
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

    public Items getItem(String guia,  String articulo)
    {
        List<Items> result = new ArrayList<>();
        try {
            String queryS= "select * from items where guia = '"+guia+"' and articulo = '"+articulo+"'";
            GenericRawResults<Items> rawResults = cityDao.queryRaw(queryS,getMapper());
            for (Items vnt : rawResults) {
                result.add(vnt);
            }
            if(result.size()>0)
                return result.get(0);
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

	public List<Items> buscar(String guia,  String buscar)
	{
		List<Items> result = new ArrayList<>();
		try {
			String queryS= "select * from items where guia = '"+guia+"' and (field1 like '%"+buscar+"%' or field2 like '%"+buscar+"%' or field3 like '%"+buscar+"%' or field4 like '%"+buscar+"%' or field5 like '%"+buscar+"%' or field6 like '%"+buscar+"%' or field7 like '%"+buscar+"%' or field8 like '%"+buscar+"%' or field9 like '%"+buscar+"%' or field10 like '%"+buscar+"%' or field11 like '%"+buscar+"%' or field12 like '%"+buscar+"%' or field13 like '%"+buscar+"%' or field14 like '%"+buscar+"%' or field15 like '%"+buscar+"%')";
			GenericRawResults<Items> rawResults = cityDao.queryRaw(queryS,getMapper());
			for (Items vnt : rawResults) {
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