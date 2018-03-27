package com.app.checklist.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;

@DatabaseTable(tableName = "data")
public class Values extends BaseEntity{

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String entity;
    @DatabaseField
    public String field1;
    @DatabaseField
    public String field2;
    @DatabaseField
    public String field3;
    @DatabaseField
    public String field4;
    @DatabaseField
    public String field5;
    @DatabaseField
    public String field6;
    @DatabaseField
    public String field7;
    @DatabaseField
    public String field8;
    @DatabaseField
    public String field9;
    @DatabaseField
    public String field10;
    @DatabaseField
    public String field11;
    @DatabaseField
    public String field12;
    @DatabaseField
    public String field13;
    @DatabaseField
    public String field14;
    @DatabaseField
    public String field15;
    @DatabaseField
    public String field16;
    @DatabaseField
    public String field17;
    @DatabaseField
    public String field18;
    @DatabaseField
    public String field19;

    @DatabaseField
    public String field20;
    @DatabaseField
    public String field21;
    @DatabaseField
    public String field22;
    @DatabaseField
    public String field23;
    @DatabaseField
    public String field24;
    @DatabaseField
    public String field25;
    @DatabaseField
    public String field26;
    @DatabaseField
    public String field27;
    @DatabaseField
    public String field28;
    @DatabaseField
    public String field29;

    @DatabaseField
    public String field30;
    @DatabaseField
    public String field31;
    @DatabaseField
    public String field32;
    @DatabaseField
    public String field33;
    @DatabaseField
    public String field34;
    @DatabaseField
    public String field35;
    @DatabaseField
    public String field36;
    @DatabaseField
    public String field37;
    @DatabaseField
    public String field38;
    @DatabaseField
    public String field39;

    @DatabaseField
    public String field40;
    @DatabaseField
    public String field41;
    @DatabaseField
    public String field42;
    @DatabaseField
    public String field43;
    @DatabaseField
    public String field44;
    @DatabaseField
    public String field45;
    @DatabaseField
    public String field46;
    @DatabaseField
    public String field47;
    @DatabaseField
    public String field48;
    @DatabaseField
    public String field49;
    @DatabaseField
    public String field50;

    @DatabaseField
    public long lastUpdate;
    @DatabaseField
    public boolean edited;
    @DatabaseField
    public boolean sync;


    public Values(){
        this.id = 0;
        this.entity = "";
        this.field1 = "";
        this.field2 = "";
        this.field3 = "";
        this.field4 = "";
        this.field5 = "";
        this.field6 = "";
        this.field7 = "";
        this.field8 = "";
        this.field9 = "";
        this.field10 = "";
        this.field11 = "";
        this.field12 = "";
        this.field13 = "";
        this.field14 = "";
        this.field15 = "";
        this.field16 = "";
        this.field17 = "";
        this.field18 = "";
        this.field19 = "";

        this.field20 = "";
        this.field21 = "";
        this.field22 = "";
        this.field23 = "";
        this.field24 = "";
        this.field25 = "";
        this.field26 = "";
        this.field27 = "";
        this.field28 = "";
        this.field29 = "";

        this.field30 = "";
        this.field31 = "";
        this.field32 = "";
        this.field33 = "";
        this.field34 = "";
        this.field35 = "";
        this.field36 = "";
        this.field37 = "";
        this.field38 = "";
        this.field39 = "";

        this.field40 = "";
        this.field41 = "";
        this.field42 = "";
        this.field43 = "";
        this.field44 = "";
        this.field45 = "";
        this.field46 = "";
        this.field47 = "";
        this.field48 = "";
        this.field49 = "";
        this.field50 = "";

        this.lastUpdate = 0;
        this.edited = false;
        this.sync = false;
    }

    public Values(String jsonValues,String jsonSchema, String entity) {
        this();
        try{
            JSONObject values = new JSONObject(jsonValues);
            JSONObject schema = new JSONObject(jsonSchema);
            JSONObject db = schema.getJSONObject("in");

            this.entity = entity;
            for (int i = 1; i < 51; i++) {
                if(db.has("field"+i)){
                    if(values.has(db.getString("field"+i))){
                        getClass().getField("field"+i).set(this,values.getString(db.getString("field"+i)));
                    }
                }
            }

        }catch (Exception e){e.printStackTrace();}
    }


    public Values(String[] columnNames, String[] resultColumns) {
        this();
        fromRawQuery(columnNames, resultColumns);
    }

    public JSONObject getJson(String jsonSchema){
        JSONObject json = null;
        try{
            json = new JSONObject();
            JSONObject schema = new JSONObject(jsonSchema);
            JSONObject db = schema.getJSONObject("in");
            for (int i = 1; i < 51; i++) {
                if(db.has("field"+i)){
                    json.put(db.getString("field"+i),(String)getClass().getField("field"+i).get(this));
                }
            }
        }catch (Exception e){e.printStackTrace();}

        return json;
    }


}
