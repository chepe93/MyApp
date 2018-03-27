package com.app.checklist.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONObject;

@DatabaseTable(tableName = "entity")
public class Entity extends BaseEntity{

    @DatabaseField(generatedId = true)
    public int id;
    @DatabaseField
    public String name;
    @DatabaseField
    public String schema;
    @DatabaseField
    public String idField;
    @DatabaseField
    public String form;
    @DatabaseField
    public String list;



    public Entity(){
        this.id = 0;
        this.name = "";
        this.schema = "";
        this.idField = "";
        this.form = "";
        this.list = "";
    }




    public Entity(String jsonS) {
        try{
            JSONObject json = new JSONObject(jsonS);
        }catch (Exception e){e.printStackTrace();}
    }

    public Entity(JSONObject json) {
        try{
            this.name = json.getString("name");
            this.schema = json.getString("schema");
            this.idField = json.getString("idField");
            this.form = json.getString("form");
            this.list = json.getString("list");
        }catch (Exception e){e.printStackTrace();}
    }

    public Entity(String[] columnNames, String[] resultColumns) {
        this();
        fromRawQuery(columnNames, resultColumns);
    }

    public JSONObject getJson(){
        JSONObject json = null;
        try{
            json = new JSONObject();
            json.put("name",this.name);
            json.put("schema",this.schema);
            json.put("idField",this.idField);
            json.put("form",this.form);
            json.put("list",this.list);
        }catch (Exception e){e.printStackTrace();}

        return json;
    }


}
