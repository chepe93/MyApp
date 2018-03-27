package com.app.checklist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.app.checklist.model.Entity;
import com.app.checklist.model.SyncDataTask;
import com.app.checklist.model.Values;
import com.app.checklist.model.db.EntityRepository;
import com.app.checklist.model.db.ValuesRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


public class ListValues extends Fragment {

    public View main_view;
//    public RecyclerView mRecyclerView;
//    public RecyclerView.Adapter mAdapter;
//    public RecyclerView.LayoutManager mLayoutManager;

    public static MyAdapter mAdapter=null;
    static ListView mRecyclerView;
    public static List<Values> values;
    public static Entity entity;
    Button btn_buscar;
    EditText tb_buscar;

    int MY_PERMISSIONS_REQUEST_WRITE=325;
    Values selectdeValue = null;

    Handler myHandler;

      @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		// TODO Auto-generated method stub


        main_view=inflater.inflate(R.layout.list, container, false);

        MainActivity act = (MainActivity)getActivity();
//        act.getSupportActionBar().show();
//        act.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);



        mRecyclerView = (ListView) main_view.findViewById(R.id.rv_list);
          btn_buscar = (Button) main_view.findViewById(R.id.btn_buscar);
          tb_buscar = (EditText) main_view.findViewById(R.id.tb_buscar);
          btn_buscar.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  AppState.hideKeyboard(getActivity());
                  ValuesRepository valuesRepository = ValuesRepository.getInstance(getActivity());
                  if(tb_buscar.getText().toString().length()>0){
                      values = valuesRepository.buscar(tb_buscar.getText().toString());
                  }else {
                      values = valuesRepository.getAll();
                  }
                  mAdapter.notifyDataSetChanged();
              }
          });

//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(0));
        return main_view;
	}

	@Override
	public void onResume() {
		super.onResume();
        EntityRepository entityRepo = EntityRepository.getInstance(getActivity());
        List<Entity> entityList = entityRepo.getAll();
        Entity entity = null;
        if (entityList.size()>0){
            entity = entityList.get(0);
        }
        ValuesRepository valuesRepository = ValuesRepository.getInstance(getActivity());
        if(tb_buscar.getText().toString().length()>0){
            values = valuesRepository.buscar(tb_buscar.getText().toString());
        }else {
            values = valuesRepository.getAll();
        }
        this.entity = entity;
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);


        AppState.OnChange = new Runnable() {
            @Override
            public void run() {
                ValuesRepository valuesRepository = ValuesRepository.getInstance(getActivity());
                if(tb_buscar.getText().toString().length()>0){
                    values = valuesRepository.buscar(tb_buscar.getText().toString());
                }else {
                    values = valuesRepository.getAll();
                }
                mAdapter.notifyDataSetChanged();
            }
        };

        new SyncDataTask().execute(getActivity());
	}

    public static void uploading(){

        SyncDataTask syn = new SyncDataTask();
        syn.onFinish = new Runnable() {
            @Override
            public void run() {
                AppState.Hide_Progrees();
                ListValues list = new ListValues();
                FragmentTransaction trans=AppState.fr_manager.beginTransaction();
                trans.replace(R.id.fragment_container, list,"");
                trans.commit();

            }
        };
        syn.execute(AppState.activity);

        AppState.Show_Progrees("Uploading...",0);
    }

    public static void actualizar(){
        EntityRepository entityRepo = EntityRepository.getInstance(AppState.activity);
        List<Entity> entityList = entityRepo.getAll();
        Entity entity = null;
        if (entityList.size()>0){
            entity = entityList.get(0);
        }
        ValuesRepository valuesRepository = ValuesRepository.getInstance(AppState.activity);
        ListValues.values = valuesRepository.getAll();
        ListValues.entity = entity;
        uploading();
    }

    @Override
    public void onPause() {
        super.onPause();
        AppState.OnChange = null;
    }


    @SuppressWarnings("deprecation")
    public void setTitleHtml(TextView tb,String title, String content){
        String ftitle = "<b>"+title+"</b> "+content;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tb.setText(Html.fromHtml(ftitle,Html.FROM_HTML_MODE_LEGACY));
        } else {
            tb.setText(Html.fromHtml(ftitle));
        }
    }

    public void editValue(Values val){
        AppState.CurrentValue = val;
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE);
            selectdeValue = val;
        }else{

            /*Fragment_Form_Element newFragment = new Fragment_Form_Element();
            newFragment.entity =entity;
            newFragment.values=val.getJson(entity.schema).toString();
            FragmentTransaction trans=AppState.fr_manager.beginTransaction();
            trans.addToBackStack("list");
            trans.replace(R.id.fragment_container, newFragment,"");
            trans.commit();*/
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        if(requestCode==MY_PERMISSIONS_REQUEST_WRITE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (selectdeValue != null) {
                    editValue(selectdeValue);
                    selectdeValue = null;
                }
            }
        }


    }

    public class MyAdapter extends BaseAdapter {

        public MyAdapter() { }

        @Override
        public int getCount() {
            return values.size();
        }

        @Override
        public Object getItem(int position) {
            return values.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
            final Values values = ListValues.this.values.get(position);

            try {
                JSONArray listJson=new JSONArray(entity.list);
                JSONObject schema = new JSONObject(entity.schema );
                LinearLayout ll = null;
                int enviado=0;
                System.out.println("listJson "+listJson);
                for (int i = 0; i < listJson.length(); i++) {

                    JSONObject columm = listJson.getJSONObject(i);
                    String title = columm.getString("Title");
                    String field = columm.getString("Field");
                    JSONObject out = schema.getJSONObject("out");
                    String value = "";
                    if(out.has(field)) {
                        String fieldN = out.getString(field);
                        value = (String) values.getClass().getField(fieldN).get(values);
                    }
                    if(i%2==0){
                        ll = new LinearLayout(getActivity());
                        if(values.edited)
//                            ll.setBackgroundColor(Color.parseColor("#fff2ab"));
                            ll.setBackgroundColor(getResources().getColor(R.color.list_edited));
                        if(values.sync) {
                            ll.setBackgroundColor(getResources().getColor(R.color.list_sync));
                            enviado=1;
                        }
                        TextView textView = new TextView(getActivity());
                        textView.setTextSize(16);
                        textView.setTextColor(Color.BLACK);
                        setTitleHtml(textView,title,value);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0,0,0,0);
                        params.weight=0.5f;
                        ll.addView(textView,params);
                    }else if(i%2==1){
                        TextView textView = new TextView(getActivity());
                        textView.setTextSize(16);
                        textView.setTextColor(Color.BLACK);
                        setTitleHtml(textView,title,value);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(AppState.dpToPx(16),0,0,0);
                        params.weight=0.5f;
                        ll.addView(textView,params);
                        view.addView(ll,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        ll = null;
                    }



                }

                if(ll!=null){
                    view.addView(ll,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                if(enviado==1){

                    view.removeView(ll);
                }
            }catch (Exception e){e.printStackTrace();}


            view.setTag(values);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppState.itemSelected=0;
                    Values values1 = (Values) v.getTag();
                    editValue(values1);
                    ListItems list = new ListItems();
                    try {
                        JSONObject schema = new JSONObject(entity.schema);
                        JSONObject out = schema.getJSONObject("out");
                        String idField = entity.idField;
                        if (out.has(idField)) {
                            String fieldN = out.getString(idField);
                            list.guia = (String) values1.getClass().getField(fieldN).get(values1);
                        }

                    } catch (Exception e) { e.printStackTrace(); }
                    list.entity=entity;
                    FragmentTransaction trans=AppState.fr_manager.beginTransaction();
                    trans.addToBackStack("list");
                    trans.replace(R.id.fragment_container, list,"");
                    trans.commit();

//                Fragment_Form_Element newFragment = new Fragment_Form_Element();
//                newFragment.entity =entity;
//                newFragment.values=values.getJson(entity.schema).toString();
//                FragmentTransaction trans=AppState.fr_manager.beginTransaction();
//                trans.addToBackStack("list");
//                trans.replace(R.id.fragment_container, newFragment,"");
//                trans.commit();
                }
            });
            return view;
        }

        }


    }





