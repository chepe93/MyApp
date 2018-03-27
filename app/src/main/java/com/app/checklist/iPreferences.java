package com.app.checklist;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;


public class iPreferences {

	public static final String PREFERENCES_ID="PREFERENCES_ID";
	public static final int PREFERENCES_Mode = Activity.MODE_PRIVATE;
	public static final String Database_Init="Database_Init";


	public static final String USER="USER";
	public static final String EMAIL="EMAIL";
	public static final String NAME="NAME";
	public static final String Address="Address";
	public static final String PASSOWRD="PASSOWRD";
	public static final String PHONE="PHONE";
    public static final String REMEMBER="REMEMBER";

	public static final String LAT="LAT";
	public static final String LNG="LNG";
	public static final String LocationTime="LocationTime";

	public static final String LogOut="LogOut";
	public static final String Server="Server";

	public static final String ItemsData="ItemsData";
	public static final String ItemsSchema="ItemsSchema";
	public static final String ItemsList="ItemsList";
	public static final String ItemsListEsp="ItemsListEsp";
	public static final String ItemsForm="ItemsForm";
	public static final String ItemsFormEsp="ItemsFormEsp";



	public static iPreferences instance;
	public Context mContext;
	public SharedPreferences myPreferences;
	
	private iPreferences()
	{
		
	}
	
	public void setContext(Context Context)
	{
		mContext=Context;
		myPreferences =mContext.getSharedPreferences(PREFERENCES_ID, PREFERENCES_Mode);
		
	}
	
	
	public static iPreferences Get_Instance()
	{
		if(instance==null)
		{
				instance=new iPreferences();
		}
		return instance;
	}
	
	public void setLong(String key, long l)
	{
	SharedPreferences.Editor editor = myPreferences
		.edit();
	editor.putLong(key, l);
	editor.commit();
	}
	public void setString(String key, String s)
	{
	SharedPreferences.Editor editor = myPreferences
		.edit();
	editor.putString(key, s);
	editor.commit();
	}
	public void setInt(String key, int i)
	{
	SharedPreferences.Editor editor = myPreferences
		.edit();
	editor.putInt(key, i);
	editor.commit();
	}
    public void setDouble(String key, double i)
    {
        String val=i+"";
        setString(key,val);
    }


	
	public long getLong(String key)
	{
		return this.myPreferences.getLong(key, 0);		
	}
	public String getString(String key)
	{
		return this.myPreferences.getString(key, "");		
	}
	public int getInt(String key){
		if(this.myPreferences.contains(key))
			return this.myPreferences.getInt(key, 0);
		return 0;
	}


    public double getDouble(String key){
        if(this.myPreferences.contains(key)) {
            String val = getString(key);
            try {
                double d = Double.parseDouble(val);
                return d;
            }
            catch (Exception e){

            }

            return this.myPreferences.getInt(key, 0);
        }
        return 0;
    }
	
	
	public boolean getBoolean(String key){
		if(this.myPreferences.contains(key))
			return this.myPreferences.getBoolean(key, false);
		return false;
	}
	
	public void setBoolean(String key, Boolean i){
	SharedPreferences.Editor editor = myPreferences
		.edit();
	editor.putBoolean(key, i);
	editor.commit();
	}
	
	
	public String getEMAIL()
	{
		return getString(EMAIL);
	}
	public void setEMAIL(String val)
	{
		setString(EMAIL, val);
	}
	
	public String getName()
	{
		return getString(NAME);
	}
	public void setName(String val)
	{
		setString(NAME, val);
	}

	public String getAddress()
	{
		return getString(Address);
	}
	public void setAddress(String val)
	{
		setString(Address, val);
	}
	public String getUSER()
	{
		return getString(USER);
	}
	public void setUSER(String val)
	{
		setString(USER, val);
	}

	public String getPASSOWRD()
	{
		return getString(PASSOWRD);
	}
	public void setPASSOWRD(String val)
	{
		setString(PASSOWRD, val);
	}

	public double getLNG()
	{
		return getDouble(LNG);
	}
	public void setLNG(double val)
	{
		setDouble(LNG, val);
	}

	public double getLAT()
	{
		return getDouble(LAT);
	}
	public void setLAT(double val)
	{
		setDouble(LAT, val);
	}

	public long getLocationTime()
	{
		return getLong(LocationTime);
	}
	public void setLocationTime(long val)
	{
		setLong(LocationTime, val);
	}
	
	public String getPHONE()
	{
		return getString(PHONE);
	}
	public void setPHONE(String val)
	{
		setString(PHONE, val);
	}



	public String getServer()
	{
		return getString(Server);
	}
	public void setServer(String val)
	{
		setString(Server, val);
	}


    public boolean getREMEMBER()
    {
        return getBoolean(REMEMBER);
    }
    public void setREMEMBER(boolean val)	{
        setBoolean(REMEMBER, val);
    }


	public void setItemsData(String val){
		setString(ItemsData, val);
	}
	public JSONArray getItemsData(){

		try {
			String res = getString(ItemsData);
			if(res == null || res.length()<2){
				return new JSONArray("[]");
			}
			JSONArray ja = new JSONArray(res);
			return  ja;
		}catch ( Exception e){
		}
		try {
			return new JSONArray("[]");
		}catch (Exception e){

		}

		return  null;

	}

	public String getItemsSchema()
	{
		return getString(ItemsSchema);
	}
	public void setItemsSchema(String val)
	{
		setString(ItemsSchema, val);
	}

	public String getItemsList()
	{
		return getString(ItemsList);
	}
	public void setItemsList(String val)
	{
		setString(ItemsList, val);
	}

	public String getItemsListEsp()
	{
		return getString(ItemsListEsp);
	}
	public void setItemsListEsp(String val)
	{
		setString(ItemsListEsp, val);
	}

	public String getItemsForm()
	{
		return getString(ItemsForm);
	}
	public void setItemsForm(String val)
	{
		setString(ItemsForm, val);
	}

	public String getItemsFormEsp()
	{
		return getString(ItemsFormEsp);
	}
	public void setItemsFormEsp(String val)
	{
		setString(ItemsFormEsp, val);
	}

}
