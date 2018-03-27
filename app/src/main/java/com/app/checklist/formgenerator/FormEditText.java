package com.app.checklist.formgenerator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatDrawableManager;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.app.checklist.AppState;
import com.app.checklist.R;

import org.json.JSONObject;


public class FormEditText extends FormWidget
{
	protected TextView _label;
	protected EditText _input;

	
	public FormEditText( Context context, String fiedl_name, JSONObject property )
	{
		super( context, fiedl_name ,property);

		_layout = (LinearLayout) AppState.InflateView(R.layout.form_edit_text);
		_label = (TextView) _layout.findViewById(R.id.lb_field);
		_label.setText( getDisplayText() );
		_input = (EditText) _layout.findViewById(R.id.tb_field);
		_input.setInputType( InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS );
		_input.setImeOptions( EditorInfo.IME_ACTION_NEXT );

        handleInfo();



//        _label = new TextView( context);
//		_label.setText( getDisplayText() );
//		_label.setLayoutParams( FormManager.defaultLayoutParams );
//        _layout.addView( _label );
//
//
//		_input = new EditText(context);
//		_input.setLayoutParams( FormManager.defaultLayoutParams );
//		_input.setInputType( InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS );
//		_input.setImeOptions( EditorInfo.IME_ACTION_NEXT );
//		Drawable tb_bg_gray = AppCompatDrawableManager.get().getDrawable(context, R.drawable.tb_bg_gray);
//		_input.setBackground(tb_bg_gray);
//        try {
//            if(_property.has(TypeFieldProperty.ReadOnly.toString()) && _property.getString(TypeFieldProperty.ReadOnly.toString()).equals("true")){
//                _input.setEnabled(false);
//            }
//        }catch (Exception e){e.printStackTrace();}
//
//		LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//		parms.setMargins(0,0, AppState.dpToPx(10),0);
//		_layout.addView( _input ,parms);
//
		if(_property.has(TypeFieldProperty.Remember.toString()))
		{
			String val = AppState.preference.getString("form_"+_field_name);
			if(val!=null && val.length()>0)
				setValue(val);
		}
		
	}
	
	@Override
	public String getValue(){
		String val=_input.getText().toString();
		if(_property.has(TypeFieldProperty.Remember.toString()))
		{
			AppState.preference.setString("form_"+_field_name, val);
		}
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
