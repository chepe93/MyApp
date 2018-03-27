package com.app.checklist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
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
import com.app.checklist.model.SyncDataTask;
import com.app.checklist.model.Values;
import com.app.checklist.model.db.ValuesRepository;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class Fragment_Form_Element extends Fragment {
	
	public FormManager form_manager;
	public boolean ReadOnly;
	public Entity entity;
	public String values;
	//public Runnable onSave;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		form_manager= new FormManager(getActivity());
		LinearLayout form_view;
        if(entity.form ==null || entity.form.length()==0){
            Toast.makeText(getActivity(),"Estructura Invalidad",Toast.LENGTH_LONG).show();
            return  new View(getActivity());
        }

		if(!ReadOnly)
		  form_view= (LinearLayout) form_manager.generateForm(entity.form,values) ;
		else
		  form_view= (LinearLayout) form_manager.generateView(entity.form) ;
		
//		if(AplicationState.review_values!=null && AplicationState.review_values.length()>0)
//		form_manager.populate(AplicationState.review_values);

		LayoutParams ll_param= new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout btn_ll = (LinearLayout) AppState.InflateView(R.layout.form_form);
        Button btn_save= (Button) btn_ll.findViewById(R.id.btn_guardar);
        btn_save.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                try {

                    JSONObject edite= form_manager.save();
                    ValuesRepository valuesRepository	= ValuesRepository.getInstance(getActivity());

					Values jvalue = new Values( values,entity.schema,entity.name);
                    Values value = null;
					JSONObject schema = new JSONObject(entity.schema);
					JSONObject out = schema.getJSONObject("out");
					String idField = entity.idField;
                    if(out.has(idField)) {
                        String fieldN = out.getString(idField);
                        List<Values> l = valuesRepository.queryForEq(fieldN, jvalue.getClass().getField(fieldN).get(jvalue));
                        if (l.size() > 0) {
                            value = l.get(0);
                        }
                    }

                    if(value!=null) {
                        Iterator<String> iter = edite.keys();
                        while (iter.hasNext()) {
                            String field = iter.next();
                            if (out.has(field)) {
                                String fieldN = out.getString(field);
                                value.getClass().getField(fieldN).set(value, edite.getString(field));
                            }
                        }
                        value.edited = true;
                        value.sync = false;
                        valuesRepository.update(value);
						new SyncDataTask().execute(getActivity());
                        AppState.fr_manager.popBackStack();
                    }




                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        });


		//form_manager.Add_View(btn_save,btn_Param);
		
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
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putString("values", form_manager.save().toString());
	}

    @Override
    public void onResume() {
        super.onResume();
        if(entity.form ==null || entity.form.length()==0){
//            AppState.fr_manager.popBackStackImmediate();
        }
    }
}
