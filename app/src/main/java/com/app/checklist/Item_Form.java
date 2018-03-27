package com.app.checklist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.app.checklist.formgenerator.FormManager;
import com.app.checklist.model.Entity;
import com.app.checklist.model.Items;
import com.app.checklist.model.Values;
import com.app.checklist.model.db.ItemsRepository;
import com.app.checklist.model.db.ValuesRepository;

import org.json.JSONObject;

import java.util.List;

public class Item_Form extends Fragment {

	public FormManager form_manager;
	public boolean ReadOnly;
	public Items item;
	public String form;
	public String schema;
	public Entity entity;
	//public Runnable onSave;
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		form_manager= new FormManager(getActivity());
		LinearLayout form_view;
		if(form ==null || form.length()==0){
			Toast.makeText(getActivity(),"Estructura Invalidad",Toast.LENGTH_LONG).show();
			return  new View(getActivity());
		}

		if(!ReadOnly) {
			form_view = (LinearLayout) form_manager.generateForm(form, item.getJson(schema).toString());
		}else {
			form_view = (LinearLayout) form_manager.generateView(form);
		}
		LayoutParams ll_param= new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout btn_ll = (LinearLayout) AppState.InflateView(R.layout.form_form);
		Button btn_save= (Button) btn_ll.findViewById(R.id.btn_guardar);
		btn_save.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				try {

					ItemsRepository repo = ItemsRepository.getInstance(getActivity());
					JSONObject edite= form_manager.save();

					Log.e("Item_Form",edite.toString());

					Items item = new Items(edite.toString(),schema);
					item.guia = Item_Form.this.item.guia;
					item.edited = true;
					if(Item_Form.this.item.id>0)
					{
						item.id= Item_Form.this.item.id;
						item.edited = true;
						repo.update(item);
						setEditGuia(item.guia,item.articulo);
						AppState.fr_manager.popBackStack();
					}else{
						Log.e("repo.create",item.articulo+" "+item.guia);
						repo.create(item);
						setEditGuia(item.guia,item.articulo);
						AppState.fr_manager.popBackStack();
					}

//
//					Values jvalue = new Values( values,schema);
//                    Values value = null;
//					JSONObject schema = new JSONObject(entity.schema);
//					JSONObject out = schema.getJSONObject("out");
//					String idField = entity.idField;
//                    if(out.has(idField)) {
//                        String fieldN = out.getString(idField);
//                        List<Values> l = valuesRepository.queryForEq(fieldN, jvalue.getClass().getField(fieldN).get(jvalue));
//                        if (l.size() > 0) {
//                            value = l.get(0);
//                        }
//                    }
//
//                    if(value!=null) {
//                        Iterator<String> iter = edite.keys();
//                        while (iter.hasNext()) {
//                            String field = iter.next();
//                            if (out.has(field)) {
//                                String fieldN = out.getString(field);
//                                value.getClass().getField(fieldN).set(value, edite.getString(field));
//                            }
//                        }
//                        value.edited = true;
//                        value.sync = false;
//                        valuesRepository.update(value);
//						new SyncDataTask().execute(getActivity());
//                        AppState.fr_manager.popBackStack();
//                    }




				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
		});

		Button btn_close= (Button) btn_ll.findViewById(R.id.btn_cancelar);
		if(!ReadOnly)
			btn_close.setText(R.string.Cancelar);
		else
			btn_close.setText(R.string.Cerrar);
		btn_close.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				try {

					AppState.fr_manager.popBackStack();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
		});

		form_manager.Add_View(btn_ll,ll_param);

		if(savedInstanceState!=null)
		{
			String vals = savedInstanceState.getString("values");

			form_manager.populate(vals);
		}

		return form_view;

	}

	public void setEditGuia(String guia,String articulo){
		try {
			JSONObject schema = new JSONObject(this.schema);
			JSONObject out;
			if(articulo.equals("item_176")) {
				out= schema.getJSONObject("esp");
			}else{
				out= schema.getJSONObject("out");
			}
			String idField = entity.idField;
			if (out.has(idField)) {
				String fieldN = out.getString(idField);
				ValuesRepository repo = ValuesRepository.getInstance(getActivity());
				List<Values> values = repo.queryForEq(fieldN,guia);
				if (values.size()>0){
					Values v = values.get(0);
					v.edited = true;
					repo.update(v);
				}
			}

		}catch (Exception e){e.printStackTrace();}


	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		outState.putString("values", form_manager.save().toString());
	}

	@Override
	public void onResume() {
		super.onResume();
	}
}
