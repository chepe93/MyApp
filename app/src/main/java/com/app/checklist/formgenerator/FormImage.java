package com.app.checklist.formgenerator;


import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


import com.app.checklist.AppState;
import com.app.checklist.Fragment_Image;
import com.app.checklist.R;

import org.json.JSONObject;


public class FormImage extends FormWidget
{
	protected TextView _label;
	protected Button _input;
	public String images;
	
	
	public FormImage( Context context, String fiedl_name, JSONObject property )
	{
		super( context, fiedl_name ,property);
//		AppState.array_images=new ArrayList<Uri>();
		_layout = (LinearLayout) AppState.InflateView(R.layout.form_image);
		_label = (TextView) _layout.findViewById(R.id.lb_field);
		_label.setText( getDisplayText() );
		_input = (Button) _layout.findViewById(R.id.btn_foto);
		_input.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Show_dialog();
			}
		});
//
		_input.setText("Tomar Fotos");

	}
	
	@Override
	public String getValue(){
//		String images="";
//		try{
//			JSONArray jsonArray = new JSONArray();
//			for (int i = 0; i < AppState.array_images.size(); i++) {
//				jsonArray.put(AppState.array_images.get(i).toString());
//			}
//
//			images = jsonArray.toString();
//		}catch (Exception e){e.printStackTrace();}

		return images;
	}
	
	@Override
	public void setValue( String value ) {
		this.images = value;
//		AppState.array_images = new ArrayList<String>();
//		images=value;
//		String[]files=images.split(",");
//		for (int i = 0; i < files.length; i++) {
//			String path = files[i];
//			if(path!=null && new File(path).exists())
//				AppState.array_images.add(path);
//		}
		
	}
	
	@Override 
	public void setHint( String value ){
		_input.setHint( value );
	}
	
	public void Show_dialog()
	{	
		
		
		

		final Fragment_Image newFragment = new Fragment_Image();
		AppState.image_widget=this;
		FragmentTransaction trans= AppState.fr_manager.beginTransaction();
		trans.addToBackStack("");
		trans.add(R.id.fragment_container, newFragment);
		trans.commit();
		
		
		
	}



	
	
	
}
