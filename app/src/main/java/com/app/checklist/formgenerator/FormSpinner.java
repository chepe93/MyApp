package com.app.checklist.formgenerator;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.app.checklist.AppState;
import com.app.checklist.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FormSpinner extends FormWidget
{
	protected JSONArray _options;
	protected TextView _label;
	protected Spinner _spinner;
	protected Map<String, String> _propmap;
	protected ArrayAdapter<String> _adapter;
	
	public FormSpinner( Context context, String field_name,JSONObject property, JSONArray options )
	{
		super( context, field_name, property );
	
		_options = options;

		_layout = (LinearLayout) AppState.InflateView(R.layout.form_spiner);
		_label = (TextView) _layout.findViewById(R.id.lb_field);
		_label.setText( getDisplayText() );
		_spinner = (Spinner) _layout.findViewById(R.id.sp_field);


		handleInfo();
		



		String p;
		String name;
//		JSONArray propertyNames = options.names();
		
		_propmap = new HashMap<String, String>();
		_adapter = new ArrayAdapter<String>( context, android.R.layout.simple_spinner_item );
		_adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		_spinner.setAdapter( _adapter );
		_spinner.setSelection( 0 );
		
		try{
			for( int i = 0; i < options.length(); i++ ) 
			{
				JSONObject opt = options.getJSONObject(i);
				name =  opt.getString("text");
				p = opt.getString("value");
				
				_adapter.add( p );
				_propmap.put( p, name );
			}
		} catch( JSONException e){
			
		}

		
		if(_property.has(TypeFieldProperty.Remember.toString()))
		{
			String val = AppState.preference.getString("form_"+_field_name);
			if(val!=null && val.length()>0)
				setValue(val);
		}
	}
	
	@Override
	public String getValue() {
//		String id = _propmap.get( _adapter.getItem( _spinner.getSelectedItemPosition() ) );
		String val="";
		try {
			val=_options.getJSONObject(_spinner.getSelectedItemPosition()).getString("value");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(_property.has(TypeFieldProperty.Remember.toString()))
		{
			AppState.preference.setString("form_"+_field_name, val);
		}
		return val;
	}
	
	@Override
	public void setValue(String value)
	{
		try{
			_spinner.setSelection( _adapter.getPosition(value) );
			/*String name;
			JSONArray names = _options.names();
			for( int i = 0; i < names.length(); i++ )
			{
				name = names.getString(i);
				
				if( name.equals(value) )
				{
					String item = _options.getString(name);
					_spinner.setSelection( _adapter.getPosition(item) );
				}
			}
			*/
		} catch( Exception e ){
			Log.i("Lykaion", e.getMessage() );
		}
	}
	
	@Override 
	public void setToggleHandler( FormManager.FormWidgetToggleHandler handler )
	{
		super.setToggleHandler(handler);
		_spinner.setOnItemSelectedListener( new SelectionHandler( this ) );
	}
	
	class SelectionHandler implements AdapterView.OnItemSelectedListener 
	{
		protected FormWidget _widget;
		
		public SelectionHandler( FormWidget widget ){
			_widget = widget;
		}
		
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if( _handler != null ){
				_handler.toggle( _widget );
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
		
	}
}
