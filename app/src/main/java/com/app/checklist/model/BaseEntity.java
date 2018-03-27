package com.app.checklist.model;


import com.app.checklist.AppState;
import com.app.checklist.model.annotations.JsonMapping;
import com.j256.ormlite.field.DatabaseField;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;


public class BaseEntity {


    public BaseEntity() {

    }



    public HashMap<String,String> fromRawQuery(String[] columnNames, String[] resultColumns) {
        try {

            HashMap<String,String> columsMaps = new HashMap<>();
            for (int i = 0; i < columnNames.length && i< resultColumns.length; i++) {
                columsMaps.put(columnNames[i],resultColumns[i]);
            }

            for (int i = 0; i < getClass().getFields().length; i++) {
                Field f = getClass().getFields()[i];
                DatabaseField mapping = f.getAnnotation(DatabaseField.class);
                if(mapping !=null) {
                    String key=f.getName();
                    if(columsMaps.containsKey(key)) {
                        String val=columsMaps.get(key);
                        if (val != null) {
                            Class<?> t= f.getType();
                            if(t.equals(String.class)){
                                if(!val.equals("null"))
                                    f.set(this,val);
                                continue;
                            }
                            if(t.equals(Integer.class) || t.equals(Integer.TYPE)){
                                f.set(this, AppState.strToInteger(val));
                                continue;
                            }
                            if(t.equals(Boolean.class) || t.equals(Boolean.TYPE)){
                                f.set(this,AppState.strToBoolean(val));
                                continue;
                            }
                            if(t.equals(Double.class) || t.equals(Double.TYPE)){
                                f.set(this,AppState.strToDouble(val));
                                continue;
                            }
                            if(t.equals(Long.class) || t.equals(Long.TYPE)){
                                f.set(this,AppState.strToLong(val));
                                continue;
                            }
                        }

                    }
                }


            }

            return columsMaps;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public void fromJson(String jsonS) {
        try {
            JSONObject json = new JSONObject(jsonS);
            for (int i = 0; i < getClass().getFields().length; i++) {
                try {
                    Field f = getClass().getFields()[i];
                    JsonMapping mapping = f.getAnnotation(JsonMapping.class);
                    if (mapping != null) {
                        String key = mapping.importKey();
                        if (key == null || key.length() == 0) {
                            key = f.getName();
                        }
                        if (json.has(key)) {
                            Class<?> t = f.getType();
                            if (t.equals(String.class)) {
                                f.set(this, json.getString(key));
                                continue;
                            }
                            if (t.equals(Integer.class) || t.equals(Integer.TYPE)) {
                                f.set(this, json.getInt(key));
                                continue;
                            }
                            if (t.equals(Boolean.class) || t.equals(Boolean.TYPE)) {
                                f.set(this, json.getBoolean(key));
                                continue;
                            }
                            if (t.equals(Double.class) || t.equals(Double.TYPE)) {
                                f.set(this, json.getDouble(key));
                                continue;
                            }
                            if (t.equals(Long.class) || t.equals(Long.TYPE)) {
                                f.set(this, json.getLong(key));
                                continue;
                            }

                            try {

                                Method method = t.getMethod("fromJson", String.class);
                                if (method != null) {
                                    Object obj = t.newInstance();
                                    JSONObject jsonObject = json.getJSONObject(key);
                                    method.invoke(obj, jsonObject.toString());
                                    f.set(this, obj);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public JSONObject toJson(){
        JSONObject json=null;
        try {
            json = new JSONObject();
            for (int i = 0; i < getClass().getFields().length; i++) {
                Field f = getClass().getFields()[i];
                JsonMapping mapping = f.getAnnotation(JsonMapping.class);
                if(mapping!=null) {
                    String key=mapping.exportKey();
                    if(key==null || key.length()==0) {
                        key=f.getName();
                    }
                    Class<?> t= f.getType();
                    if(t.isPrimitive())
                    {
                        json.put(key,f.get(this));
                    }
                    else {
                        Object val = f.get(this);
                        if(val!=null) {
                            if (t.equals(String.class)|| t.equals(Integer.class) || t.equals(Boolean.class) || t.equals(Double.class) || t.equals(Long.class)) {
                                json.put(key, f.get(this));
                                continue;
                            }
                            try {
                                Method method = t.getMethod("toJson",null);
                                if(method!=null)
                                {
                                    json.put(key,method.invoke(val));
                                }
                            }catch (Exception e){}

                        }
                    }
                }


            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return json;


    }

    public String getString(JSONObject json, String key)
    {
        try {
            if(json.has(key))
                return json.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getInt(JSONObject json, String key)
    {
        try {
            if(json.has(key))
                return json.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long getLong(JSONObject json, String key)
    {
        try {
            if(json.has(key))
                return json.getLong(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getDouble(JSONObject json, String key)
    {
        try {
            if(json.has(key))
                return json.getDouble(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean getBoolean(JSONObject json, String key)
    {
        try {
            if(json.has(key))
                return json.getBoolean(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }





}
