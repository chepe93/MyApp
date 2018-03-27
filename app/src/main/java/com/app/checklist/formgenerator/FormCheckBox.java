package com.app.checklist.formgenerator;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.checklist.AppState;
import com.app.checklist.R;

import org.json.JSONObject;

public class FormCheckBox extends FormWidget
{

	protected int 			_priority;
	protected CheckBox		_checkbox;
	protected TextView _label;
	
	public FormCheckBox( Context context, String field_name, JSONObject property ) 
	{
		super( context, field_name,property );

		_layout = (LinearLayout) AppState.InflateView(R.layout.form_check);
		_label = (TextView) _layout.findViewById(R.id.lb_field);
		_label.setText( getDisplayText() );
		_checkbox = (CheckBox) _layout.findViewById(R.id.cb_field);
        handleInfo();
	}

	@Override
	public String getValue() {
		return String.valueOf( _checkbox.isChecked() ? "1" : "0" );
	}

	public void setValue( String value ) {
		_checkbox.setChecked( value.equals("1") );
	}
	
	@Override 
	public void setToggleHandler( FormManager.FormWidgetToggleHandler handler )
	{
		super.setToggleHandler(handler);
		_checkbox.setOnCheckedChangeListener( new ChangeHandler(this) );
	}
	
	class ChangeHandler implements CompoundButton.OnCheckedChangeListener 
	{
		protected FormWidget _widget;
		
		public ChangeHandler( FormWidget widget ) {
			_widget = widget;
		}
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
		{
			if( _handler != null ){
				_handler.toggle( _widget );
			}
		}
		
	}
}
