package com.app.checklist.formgenerator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import org.json.JSONObject;

public class FormTextView extends FormWidget
{
	protected TextView _label;
	protected TextView _input;
	
	public FormTextView( Context context, String fiedl_name, JSONObject property )
	{
		super( context, fiedl_name ,property);
		
		_label = new TextView( context );
		_label.setText( getDisplayText() +": ");
		_label.setLayoutParams( FormManager.defaultLayoutParams );
	
		_input = new TextView( context );
		LayoutParams llp=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		llp.setMargins(50, 5, 5, 5);
		_input.setLayoutParams( llp );
		_input.setTypeface(null, Typeface.NORMAL);
		_input.setTextColor(Color.BLACK);
        _input.setTextSize(16);

        try {
            if(property.has(TypeFieldProperty.TextColor.toString())){
                _input.setTextColor(Color.parseColor(property.getString(TypeFieldProperty.TextColor.toString())));
            }
        }catch (Exception e){e.printStackTrace();}


		_layout.addView( _label );
		_layout.addView( _input,llp );
				
	}
	
	@Override
	public String getValue(){
		String val=_input.getText().toString();
		return val;
	}
	
	@Override
	public void setValue( String value ) {
		_input.setText( value );
	}
	
	@Override 
	public void setHint( String value ){
		_input.setHint( value );
	}
}
