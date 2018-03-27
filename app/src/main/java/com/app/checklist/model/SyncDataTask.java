package com.app.checklist.model;


import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;


import com.app.checklist.ApiHelper;
import com.app.checklist.AppState;
import com.app.checklist.ListValues;
import com.app.checklist.MainActivity;
import com.app.checklist.R;
import com.app.checklist.model.db.EntityRepository;
import com.app.checklist.model.db.ItemsRepository;
import com.app.checklist.model.db.ValuesRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class SyncDataTask extends AsyncTask<Context, Void, String> {

    public Runnable onFinish=null;
    public static boolean running = false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
    @Override
    protected String doInBackground(Context... params) {
        if(!running) {
            running = true;

            try {
                Context ctx = params[0];

                EntityRepository entityRepo = EntityRepository.getInstance(ctx);

                List<Entity> entityList = entityRepo.getAll();
                Entity entity = null;
                if (entityList.size() > 0) {
                    entity = entityList.get(0);
                } else {
                    String entityS = ApiHelper.getEntity();
                    JSONObject entityJ = new JSONObject(entityS);
                    if (entityJ.getString("result").equals("success")) {
                        Entity e = new Entity(entityJ);
                        entityRepo.create(e);
                        entity = e;
                    }
                }
                List<Values> edites=null;
                int conteoData=0;
                if (entity != null) {
                    ValuesRepository valuesRepository = ValuesRepository.getInstance(ctx);
                    ItemsRepository itemsRepository = ItemsRepository.getInstance(ctx);
                    edites = valuesRepository.getSync();
                    for (int i = 0; i < edites.size(); i++) {
                        Values value = edites.get(i);
                        JSONArray jsonItems = new JSONArray();
                        String guia = "";
                        System.out.println("value "+value.entity+" "+value.id+" "+value.field1);
                        JSONObject schema = new JSONObject(entity.schema);
                        JSONObject out = schema.getJSONObject("out");
                        String idField = entity.idField;
                        if (out.has(idField)) {
                            String fieldN = out.getString(idField);
                            guia = (String) value.getClass().getField(fieldN).get(value);

                            List<Items> items = itemsRepository.getAll(guia);
                            if (items != null && items.size() > 0) {
                                for (int j = 0; j < items.size(); j++) {
                                    Items itm = items.get(j);
                                    jsonItems.put(itm.getJson(AppState.preference.getItemsSchema()));
                                }
                            }

                        }


                        Log.e("SyncDataTask",jsonItems.toString());
                        String resultS = ApiHelper.updateItems(AppState.preference.getUSER(), ctx.getContentResolver(), jsonItems);
                        JSONObject result = new JSONObject(resultS);
                        if (result.getString("result").equals("success-no")) {
                            Log.e("update","Delete row");
                            itemsRepository.deleteAll(guia);
                            valuesRepository.delete(value);
                        }



                    }
                    //limpiar valuesRepository
                    valuesRepository = ValuesRepository.getInstance(ctx);
                    edites = valuesRepository.getAll();
                    System.out.println("edites.length "+edites.size());
                    for (int i = 0; i < edites.size(); i++) {
                        Values value = edites.get(i);
                        valuesRepository.delete(value);
                    }
                    edites = valuesRepository.getAll();
                    System.out.println("edites.length "+edites.size());



                    String lists = ApiHelper.getList(AppState.preference.getUSER());

                    JSONObject list = new JSONObject(lists);

                    if (list.getString("result").equals("success")) {
                        JSONArray data = list.getJSONArray("data");
                        JSONObject schema = new JSONObject(entity.schema);
                        conteoData=data.length();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject val = data.getJSONObject(i);
                            Values value = new Values(val.toString(), entity.schema, entity.name);

                            JSONObject out = schema.getJSONObject("out");
                            String idField = entity.idField;
                            if (out.has(idField)) {
                                String fieldN = out.getString(idField);
                                List<Values> l = valuesRepository.queryForEq(fieldN, val.getString(idField));
                                if (l.size() == 0) {
                                    valuesRepository.create(value);
                                }
//                        else {
//                            valuesRepository.update(value);
//                        }
                            }


                        }
                    }else{

                    }


                }


                String itemsS = ApiHelper.getItems();
                JSONObject items = new JSONObject(itemsS);
                if (items.getString("result").equals("success")) {
                    JSONObject schema = items.getJSONObject("schema");
                    JSONObject form = items.getJSONObject("form");
                    JSONObject formEsp = items.getJSONObject("formEsp");
                    JSONArray list = items.getJSONArray("list");
                    JSONArray listEsp = items.getJSONArray("listEsp");
                    JSONArray data = items.getJSONArray("data");

                    AppState.preference.setItemsSchema(schema.toString());
                    AppState.preference.setItemsForm(form.toString());
                    AppState.preference.setItemsFormEsp(formEsp.toString());
                    AppState.preference.setItemsList(list.toString());
                    AppState.preference.setItemsListEsp(listEsp.toString());
                    AppState.preference.setItemsData(data.toString());

                }

                if(edites!=null && edites.size()>0){
                    ListValues.uploading();
                    System.out.println("entro S");
                    return "S";
                }
                    //ListValues.actualizar();
                System.out.println("ListValues.mAdapter.getCount() " + ListValues.mAdapter.getCount() + " data.length() " + conteoData);
                if (ListValues.mAdapter != null){
                    if(ListValues.mAdapter.getCount() != conteoData || conteoData==0) {
                        System.out.println("entro _");
                        ListValues.actualizar();
                        //ListValues.mAdapter.notifyDataSetChanged();

                        return "";
                    }
                }


            } catch (Exception e) {
            }
        }
        return ApiHelper.ResutlError;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        running = false;
        if (onFinish != null) {


            System.out.println("entro prenotify");

            AppState.activity.runOnUiThread(onFinish);
            onFinish=null;
        }

        if (AppState.OnChange != null) {
            AppState.activity.runOnUiThread(AppState.OnChange);
//            onFinish=null;
        }

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        running = false;
        if (onFinish != null) {
            AppState.activity.runOnUiThread(onFinish);
            onFinish=null;
        }
    }
}