package com.app.checklist.formgenerator;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.checklist.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

abstract class FormWidget 
{
	protected View 			_view;
	protected String 		_field_name;
	protected String 		_displayText;
	protected int 	 		_priority;
	protected LinearLayout 	_layout;
	protected FormManager.FormWidgetToggleHandler _handler;
	public TextView _error;
	public JSONObject _property;
	public Context context;
	public TextView lb_info;
//	public ImageView img_info;
	public Boolean showInfo = false;
	
	protected HashMap<String, ArrayList<String>> _toggles;
	
	public FormWidget( Context context, String field_name, JSONObject property )
	{
		this.context=context;
		_property=property;
		_layout = new LinearLayout( context );
		_layout.setLayoutParams( FormManager.defaultLayoutParams );
		_layout.setOrientation( LinearLayout.VERTICAL );
		
		_field_name 		= field_name;
		if(property.has(TypeFieldProperty.Title.toString()))
		{
			try {
				_displayText=property.getString(TypeFieldProperty.Title.toString());
			} catch (Exception e) {e.printStackTrace();}
		}
		else
		{
			
		 _displayText	= field_name.replace( "_", " ");
		 _displayText 	= toTitleCase( _displayText );
		}
	}

	public void handleInfo(){
		try {
//			img_info = (ImageView) _layout.findViewById(R.id.img_info);
			lb_info = (TextView) _layout.findViewById(R.id.lb_info);
			if(_property.has("Info")){
				lb_info.setText(_property.getString("Info"));
				lb_info.setVisibility(View.VISIBLE);
//				img_info.setVisibility(View.VISIBLE);
//				img_info.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						if(showInfo){
//							showInfo = false;
//							lb_info.setVisibility(View.GONE);
//						}else {
//							showInfo = true;
//							lb_info.setVisibility(View.VISIBLE);
//
//						}
//					}
//				});
			}else{
				lb_info.setText("");
				lb_info.setVisibility(View.GONE);
//				img_info.setVisibility(View.INVISIBLE);
			}

		}catch (Exception e){e.printStackTrace();}

	}
	
	// -----------------------------------------------
	//
	// viewMakeMachine
	//
	// -----------------------------------------------
	/**
	 * return LinearLayout containing this widget's view elements
	 */
	public View getView() {
		return _layout;
	}
	
	/**
	 * toggles the visibility of this widget
	 * @param value
	 */
	public void setVisibility( int value ){
		_layout.setVisibility( value );
	}
	
	// -----------------------------------------------
	//
	// set / get value
	//
	// -----------------------------------------------
	
	/**
	 * returns value of this widget as String
	 */
	public String getValue() {
		return "";
	}

	/**
	 * sets value of this widget, method should be overridden in sub-class
	 * @param value
	 */
	public void setValue( String value ) {
		// -- override 
	}
	
	// -----------------------------------------------
	//
	// modifiers
	//
	// -----------------------------------------------
	
	/**
	 * sets the hint for the widget, method should be overriden in sub-class
	 */
	public void setHint( String value ){
		// -- override
	}
	
	/**
	 * sets an object that contains keys for special properties on an object
	 * @param modifiers
	 */
	public void setModifiers( JSONObject modifiers ) {
		// -- override
	}
	
	// -----------------------------------------------
	//
	// set / get priority
	//
	// -----------------------------------------------
	
	/**
	 * sets the visual priority of this widget
	 * essentially this means it's physical location in the form
	 */
	public void setPriority( int value ) {
		_priority = value;
	}
	
	/**
	 * returns visual priority
	 * @return
	 */
	public int getPriority() {
		return _priority;
	}
	
	// -----------------------------------------------
	//
	// property name mods
	//
	// -----------------------------------------------
	
	/**
	 * returns the un-modified name of the property this widget represents
	 */
	public String getPropertyName(){
		return _field_name;
	}
	
	/**
	 * returns a title case version of this property
	 * @return
	 */
	public String getDisplayText(){
		return _displayText;
	}
	
	/**
	 * takes a property name and modifies 
	 * @param s
	 * @return
	 */
	public String toTitleCase( String s ) 
	{
		char[] chars = s.trim().toLowerCase().toCharArray();
		boolean found = false;
	 
		for (int i=0; i<chars.length; i++) {
			if (!found && Character.isLetter(chars[i])) {
				chars[i] = Character.toUpperCase(chars[i]);
				found = true;
			} else if (Character.isWhitespace(chars[i])) {
				found = false;
			}
		}
	 
		return String.valueOf(chars);
	}
	
	// -----------------------------------------------
	//
	// toggles
	//
	// -----------------------------------------------
	
	/**
	 * sets the list of toggles for this widgets
	 * the structure of the data looks like this:
	 * HashMap<value of property for visibility, ArrayList<list of properties to toggle on>>
	 */
	public void setToggles( HashMap<String, ArrayList<String>> toggles ) {
		_toggles = toggles;
	}
	
	/**
	 * return list of widgets to toggle on
	 * @param value
	 * @return
	 */
	public ArrayList<String> getToggledOn() 
	{
		if( _toggles == null ) return new ArrayList<String>();
		
		if( _toggles.get( getValue() ) != null ) {
			return _toggles.get( getValue() );
		} else {
			return new ArrayList<String>();
		}
	}
	
	/**
	 * return list of widgets to toggle off
	 * @param value
	 * @return
	 */
	public ArrayList<String> getToggledOff() 
	{
		ArrayList<String> result = new ArrayList<String>();
		if( _toggles == null ) return result;
		
		Set<String> set = _toggles.keySet();
		
		 for (String key : set)
		 {
			 if( !key.equals( getValue() ) ) 
			 {
				ArrayList<String> list = _toggles.get(key);
				if( list == null ) return new ArrayList<String>();
				for( int i = 0; i < list.size(); i++ ) {
					result.add( list.get(i) );
				}
			}
		 }
		
		return result;
	}
	
	/**
	 * sets a handler for value changes
	 * @param handler
	 */
	public void setToggleHandler( FormManager.FormWidgetToggleHandler handler ){
		_handler = handler;
	}
}
