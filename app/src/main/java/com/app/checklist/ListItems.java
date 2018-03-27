package com.app.checklist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.checklist.model.Entity;
import com.app.checklist.model.Items;
import com.app.checklist.model.SyncDataTask;
import com.app.checklist.model.Values;
import com.app.checklist.model.db.ItemsRepository;
import com.app.checklist.model.db.ValuesRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ListItems extends Fragment {

    public View main_view;
//    public RecyclerView mRecyclerView;
//    public RecyclerView.Adapter mAdapter;
//    public RecyclerView.LayoutManager mLayoutManager;

    MyAdapter mAdapter;
    ListView mRecyclerView;
    public List<Items> values;
    public String guia;
    public Entity entity;
    Button btn_finalizar;
    Button btn_vover;

      @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		// TODO Auto-generated method stub


        main_view=inflater.inflate(R.layout.list_items, container, false);
        MainActivity act = (MainActivity)getActivity();
//        act.getSupportActionBar().show();
//        act.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);



        mRecyclerView = (ListView) main_view.findViewById(R.id.rv_list);
          btn_finalizar = (Button) main_view.findViewById(R.id.btn_finalizar);
          btn_finalizar.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  AppState.hideKeyboard(getActivity());
                  try {
                      JSONObject schema = new JSONObject(entity.schema);
                      JSONObject out = schema.getJSONObject("out");
                      String idField = entity.idField;
                      String fieldN = null;
                      if (out.has(idField)) {
                          fieldN = out.getString(idField);
                          ValuesRepository repository = ValuesRepository.getInstance(getActivity());
                          List<Values> values = repository.queryForEq(fieldN,guia);
                          if(values.size()>0){
                              Values val = values.get(0);
                              val.edited = true;
                              val.sync = true;
                              repository.update(val);
                          }
                        }
                  }catch (Exception e){e.printStackTrace();}
                  AppState.itemSelected=0;
                  AppState.fr_manager.popBackStack();
              }
          });

          btn_vover = (Button) main_view.findViewById(R.id.btn_vover);
          btn_vover.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  AppState.itemSelected=0;
                  AppState.fr_manager.popBackStack();
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

        values = loadItems();
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setSelection(AppState.itemSelected);
	}

    public List<Items> loadItems() {

        ItemsRepository itemsRepository = ItemsRepository.getInstance(getActivity());

        JSONArray jarr = AppState.preference.getItemsData();
        String itemsSchema = AppState.preference.getItemsSchema();

        List<Items> result = new ArrayList<>();
        for (int i = 0; i < jarr.length(); i++) {
            try {
                JSONObject json = jarr.getJSONObject(i);
                Items item = new Items(json.toString(),itemsSchema);
                Items itemsDB = itemsRepository.getItem(guia,item.articulo);
                if(itemsDB!=null){
                    result.add(itemsDB);
                }
                else{
                    item.guia = guia;
                    result.add(item);
                }

            }catch (Exception e){e.printStackTrace();}


        }
        return result;

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
            final Items values = ListItems.this.values.get(position);
            try {
                JSONArray listJson=new JSONArray(AppState.preference.getItemsList());
                JSONArray listJsonEsp=new JSONArray(AppState.preference.getItemsListEsp());
                JSONObject schema = new JSONObject(AppState.preference.getItemsSchema());
                LinearLayout ll = null;
                if(values.articulo.equals("item_176")) {
                    for (int i = 0; i < listJsonEsp.length(); i++) {
                        JSONObject columm = listJsonEsp.getJSONObject(i);
                        String title = columm.getString("Title");
                        String field = columm.getString("Field");
                        JSONObject out = schema.getJSONObject("esp");
                        String value = "";
                        if (out.has(field)) {
                            String fieldN = out.getString(field);
                            value = (String) values.getClass().getField(fieldN).get(values);
                        }

                        ll = new LinearLayout(getActivity());
                        if (values.edited)
                            ll.setBackgroundColor(getResources().getColor(R.color.list_edited));
                        if (values.sync)
                            ll.setBackgroundColor(getResources().getColor(R.color.list_edited));
                        TextView textView = new TextView(getActivity());
                        textView.setTextSize(16);
                        textView.setTextColor(Color.BLACK);
                        setTitleHtml(textView, title, value);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 0, 0);
                        params.weight = 0.5f;
                        ll.addView(textView, params);
                        view.addView(ll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                }else{
                    for (int i = 0; i < listJson.length(); i++) {
                        JSONObject columm = listJson.getJSONObject(i);
                        String title = columm.getString("Title");
                        String field = columm.getString("Field");
                        JSONObject out = schema.getJSONObject("out");
                        String value = "";
                        if (out.has(field)) {
                            String fieldN = out.getString(field);
                            value = (String) values.getClass().getField(fieldN).get(values);
                        }

                        ll = new LinearLayout(getActivity());
                        if (values.edited)
                            ll.setBackgroundColor(getResources().getColor(R.color.list_edited));
                        if (values.sync)
                            ll.setBackgroundColor(getResources().getColor(R.color.list_edited));
                        TextView textView = new TextView(getActivity());
                        textView.setTextSize(16);
                        textView.setTextColor(Color.BLACK);
                        setTitleHtml(textView, title, value);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 0, 0);
                        params.weight = 0.5f;
                        ll.addView(textView, params);
                        view.addView(ll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                }

            }catch (Exception e){e.printStackTrace();}


            view.setTag(values);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Items values1 = (Items) v.getTag();
                    Item_Form newFragment = new Item_Form();
                    newFragment.item =values1;
                    AppState.itemSelected = position;
                    if(newFragment.item.articulo.equals("item_176")) {
                        newFragment.form = AppState.preference.getItemsFormEsp();
                        newFragment.schema = AppState.preference.getItemsSchema();
                        newFragment.entity = entity;
                        FragmentTransaction trans = AppState.fr_manager.beginTransaction();
                        trans.addToBackStack("list");
                        trans.replace(R.id.fragment_container, newFragment, "");
                        trans.commit();
                    }else{
                        newFragment.form = AppState.preference.getItemsForm();
                        newFragment.schema = AppState.preference.getItemsSchema();
                        newFragment.entity = entity;
                        FragmentTransaction trans = AppState.fr_manager.beginTransaction();
                        trans.addToBackStack("list");
                        trans.replace(R.id.fragment_container, newFragment, "");
                        trans.commit();
                    }
                }
            });
            return view;
        }

        }


    }





