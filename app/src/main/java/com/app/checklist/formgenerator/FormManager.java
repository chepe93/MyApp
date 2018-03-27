package com.app.checklist.formgenerator;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;


import com.app.checklist.AppState;
import com.app.checklist.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class FormManager 
{
	
		
	public static final LayoutParams defaultLayoutParams = new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

	// -- data
	protected Map<String, FormWidget> _map;
	protected ArrayList<FormWidget> _widgets;

	// -- widgets
	protected LinearLayout _container;
	protected LinearLayout _layout;
	protected ScrollView   _viewport;
	public Activity activity;

	public FormManager (Activity activity)
	{
		this.activity=activity;
	}


	public View generateForm( String schema_s,String data )
	{
		_widgets = new ArrayList<FormWidget>();
		_map = new HashMap<String, FormWidget>();

		try
		{
			String name;
			FormWidget widget;
			JSONObject property;
			JSONObject schema = new JSONObject( schema_s );
			JSONArray names = schema.names();
			for( int i= 0; i < names.length(); i++ )
			{
				name = names.getString( i );

				if( name.equals( TypeFieldProperty.Meta )  ) continue;

				property = schema.getJSONObject( name );

				boolean toggles  = hasToggles( property );
				String defaultValue   = getDefault( property );
				int priority = property.getInt( TypeFieldProperty.Priority.toString() );

				widget = getWidget( name, property );
				if( widget == null) continue;

				widget.setPriority( priority );
				if(defaultValue!=null && defaultValue.length()>0)
					widget.setValue( defaultValue );

				if( toggles ){
					widget.setToggles( processToggles( property ) );
					widget.setToggleHandler( new FormManager.FormWidgetToggleHandler() );
				}

				if( property.has(TypeFieldProperty.Hint.toString())) widget.setHint( property.getString( TypeFieldProperty.Hint.toString() ) );

				_widgets.add( widget );
				_map.put( name, widget );
			}
		} catch( JSONException e ) {
			Log.i( "generateForm", e.getMessage() );
		}

        this.populate(data);

		// -- sort widgets on priority
		Collections.sort( _widgets, new PriorityComparison() );

		// -- create the layout
        _container = new LinearLayout( new ContextThemeWrapper(this.activity, R.style.Lineal_Layout), null, 0);
        _container.setOrientation( LinearLayout.VERTICAL );
        _container.setLayoutParams( FormManager.defaultLayoutParams );
        _container.setBackgroundColor(Color.BLACK);

        _viewport  = new ScrollView( new ContextThemeWrapper(this.activity, R.style.Scroll_View) );
        _viewport.setLayoutParams( FormManager.defaultLayoutParams );

        _layout = new LinearLayout( new ContextThemeWrapper(this.activity, R.style.Lineal_Layout), null, 0);
        _layout.setOrientation( LinearLayout.VERTICAL );
        _layout.setLayoutParams( FormManager.defaultLayoutParams );

        initToggles();

        for( int i = 0; i < _widgets.size(); i++ ) {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
            params.setMargins(AppState.dpToPx(10),0,AppState.dpToPx(10),AppState.dpToPx(16));
            View widgets = ( View ) _widgets.get(i).getView();
            widgets.setLayoutParams(params);
            _layout.addView(widgets);
        }

        _viewport.addView( _layout );
        _container.addView( _viewport );

//		LinearLayout.LayoutParams params = (LayoutParams) _container.getLayoutParams();
//		params.setMargins(AppState.dpToPx(10),0,0,0);
//		_container.setLayoutParams(params);
		return _container ;
	}


	public View generateView( String data )
	{
		_widgets = new ArrayList<FormWidget>();
		_map = new HashMap<String, FormWidget>();

		try
		{
			String name;
			FormWidget widget;
			JSONObject property;
			JSONObject schema = new JSONObject( data );
			JSONArray names = schema.names();
			for( int i= 0; i < names.length(); i++ )
			{
				name = names.getString( i );

				if( name.equals( TypeFieldProperty.Meta )  ) continue;

				property = schema.getJSONObject( name );

				boolean toggles  = hasToggles( property );
				String defaultValue   = getDefault( property );
				int priority = property.getInt( TypeFieldProperty.Priority.toString() );

				widget = new FormTextView(this.activity, name, property );
				if( widget == null) continue;

				widget.setPriority( priority );
				if(defaultValue!=null && defaultValue.length()>0)
					widget.setValue( defaultValue );

				if( toggles ){
					widget.setToggles( processToggles( property ) );
					widget.setToggleHandler( new FormManager.FormWidgetToggleHandler() );
				}
				
				if( property.has(TypeFieldProperty.Hint.toString())) widget.setHint( property.getString( TypeFieldProperty.Hint.toString() ) );
				
				_widgets.add( widget );
				_map.put( name, widget );
			}
		} catch( JSONException e ) {
			Log.i( "generateForm", e.getMessage() );
		}
		
		// -- sort widgets on priority
		Collections.sort( _widgets, new PriorityComparison() );	
		
		// -- create the layout
        _container = new LinearLayout( this.activity );
        _container.setOrientation( LinearLayout.VERTICAL );
        _container.setLayoutParams( FormManager.defaultLayoutParams );
        _container.setBackgroundColor(Color.BLACK);
        
        _viewport  = new ScrollView( this.activity );
        _viewport.setLayoutParams( FormManager.defaultLayoutParams );
        
        _layout = new LinearLayout( this.activity );
        _layout.setOrientation( LinearLayout.VERTICAL );
        _layout.setLayoutParams( FormManager.defaultLayoutParams );
        
        initToggles();
        
        for( int i = 0; i < _widgets.size(); i++ ) {
        	_layout.addView( ( View ) _widgets.get(i).getView() );
        }
        
        _viewport.addView( _layout );
        _container.addView( _viewport );
        
		return _container ;
	}
	public void Add_View(View v,LayoutParams param)
	{
		_layout.addView(v,param);
	}
	
	
	public void populate( String jsonString )
	{
		try
		{	
			String prop;
			FormWidget widget;
			JSONObject data = new JSONObject( jsonString );
            populate(data);
		} catch ( JSONException e ) {
			
		}
	}
	
	public void populate(JSONObject data )
	{
		try
		{	
			String prop;
			FormWidget widget;
			JSONArray properties = data.names();
			
			for( int i = 0; i < properties.length(); i ++ )
			{
				prop = properties.getString( i );
				if( _map.containsKey(prop) )  {
					widget = _map.get( prop );
					String val=data.getString(prop);
					if(val!=null && val.length()>0)
						widget.setValue(val);
				}
			}
		} catch ( JSONException e ) {
			
		}
	}
	
	
	public JSONObject save()
	{
		FormWidget widget;
		JSONObject data = new JSONObject();
		
		boolean success = true;
		
		try{
			for( int i = 0; i < _widgets.size(); i++ ) 
			{
	        	widget = _widgets.get(i);
	        	data.put( widget.getPropertyName(), widget.getValue() );
			}
		} catch( JSONException e )
		{
			success = false;
			Log.i( "save", "Save error - " + e.getMessage() );
			return null;
		}
		
		if( success ) {
			Log.i( "save", "Save success " + data.toString() );
			return data;
		}
		return null;
	}
	
	
	protected HashMap<String, ArrayList<String>> processToggles( JSONObject property )
	{
		try{
			ArrayList<String> toggled;
			HashMap<String, ArrayList<String>> toggleMap = new HashMap<String, ArrayList<String>>();
			
			JSONObject toggleList = property.getJSONObject( TypeFieldProperty.Toggles.toString() );
			JSONArray toggleNames = toggleList.names();
			
			for( int j = 0; j < toggleNames.length(); j++ )
			{
				String toggleName = toggleNames.getString(j);
				JSONArray toggleValues = toggleList.getJSONArray( toggleName );
				toggled = new ArrayList<String>();
				toggleMap.put( toggleName, toggled );
				for( int k = 0; k < toggleValues.length(); k++ ) {
					toggled.add( toggleValues.getString(k) );
				}
			}
			
			return toggleMap;
			
		} catch( JSONException e ){
			return null;
		}
	}
	
	/**
	 * returns a boolean indicating that the supplied json object contains a property for toggles
	 */
	protected boolean hasToggles( JSONObject obj ){
		try{
			obj.getJSONObject( TypeFieldProperty.Toggles.toString() );
			return true;
		} catch ( JSONException e ){
			return false;
		}
	}
	
	/**
	 * initializes the visibility of widgets that are togglable 
	 */
	protected void initToggles()
	{
		int i;
		FormWidget widget;
		
		for( i = 0; i < _widgets.size(); i++ )  {
			widget = _widgets.get(i);
			updateToggles( widget );
		}
	}
	
	/**
	 * updates any widgets that need to be toggled on or off
	 * @param widget
	 */
	protected void updateToggles( FormWidget widget ) 
	{
		int i;
		String name;
		ArrayList<String> toggles;
		ArrayList<FormWidget> ignore = new ArrayList<FormWidget>();
		
		toggles = widget.getToggledOn();
		for( i = 0; i < toggles.size(); i++ ) 
		{
			name = toggles.get(i);
			if( _map.get(name) != null ) 
			{
				FormWidget toggle = _map.get(name);
				ignore.add( toggle );
				toggle.setVisibility( View.VISIBLE );
			}
		}
		
		toggles = widget.getToggledOff();
		for( i = 0; i < toggles.size(); i++ ) 
		{
			name = toggles.get(i);
			if( _map.get(name) != null ) 
			{
				FormWidget toggle = _map.get(name);
				if( ignore.contains(toggle) ) continue;
				toggle.setVisibility( View.GONE );
			}
		}
	}
	
	/**
	 * simple callbacks for widgets to use when their values have changed
	 */
	class FormWidgetToggleHandler
	{
		public void toggle( FormWidget widget ) {
			updateToggles( widget );
		}
	}
	
	// -----------------------------------------------
	//
	// utils
	//
	// -----------------------------------------------
	
	protected String getDefault( JSONObject obj ){
		try{
			return obj.getString( TypeFieldProperty.Default.toString() );
		} catch ( JSONException e ){
			return null;
		}
	}
	
	/**
	 * helper class for sorting widgets based on priority
	 */
	class PriorityComparison implements Comparator<FormWidget>
	{
		public int compare( FormWidget item1, FormWidget item2 ) {
			return item1.getPriority() > item2.getPriority() ? 1 : -1;
		}
	}
	
	/**
	 * factory method for actually instantiating widgets
	 */
	protected FormWidget getWidget( String name, JSONObject property ) 
	{
		try
		{
			String type = property.getString( TypeFieldProperty.Type.toString() );
            if( type.equals( TypeFieldType.Text.toString() ) ){
                return new FormTextView( this.activity, name ,property );
            }
			if( type.equals( TypeFieldType.String.toString() ) ){
                if( property.has( TypeFieldProperty.Options.toString() ) )
                {
                    JSONArray options = property.getJSONArray( TypeFieldProperty.Options.toString() );
                    return new FormSpinner(  this.activity, name,property, options );
                }
				return new FormEditText( this.activity, name ,property );
			}

			
			if( type.equals( TypeFieldType.Image.toString() ) ){
				return new FormImage( this.activity, name ,property );
			}
			
			if( type.equals( TypeFieldType.Boolean.toString() ) ){
				return new FormCheckBox( this.activity, name ,property);
			}
			
			if( type.equals( TypeFieldType.Date.toString() ) ){
				return new FormDatePicker( this.activity, name ,property);
			}
			
			if( type.equals(  TypeFieldType.Integer.toString() ) )
			{	
				if( property.has( TypeFieldProperty.Options.toString() ) ) 
				{
					JSONArray options = property.getJSONArray( TypeFieldProperty.Options.toString() );
					return new FormSpinner(  this.activity, name,property, options );
				}else{
					return new FormNumericEditText( this.activity, name ,property);
				}
			}
		} catch( JSONException e ) {
			return null;
		}
		return null;
	}
	
	public String parseFileToString( String filename )
	{
		try
		{
			InputStream stream = activity.getAssets().open( filename );
			int size = stream.available();
			
			byte[] bytes = new byte[size];
			stream.read(bytes);
			stream.close();
			
			return new String( bytes );
			
		} catch ( IOException e ) {
			Log.i("save", "IOException: " + e.getMessage() );
		}
		return null;
	}
}


