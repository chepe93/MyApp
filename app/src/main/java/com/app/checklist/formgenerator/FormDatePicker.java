package com.app.checklist.formgenerator;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FormDatePicker extends FormWidget
{
	protected TextView _label;
	protected Button _input;
	protected DatePicker _date;
	public Date date;
	public String dateFormat;
	
	public FormDatePicker( Context context, String fiedl_name, JSONObject property )
	{
		super( context, fiedl_name ,property);
		
		_label = new TextView( context );
		_label.setText( getDisplayText() );
		_label.setLayoutParams( FormManager.defaultLayoutParams );
		_label.setFocusable(true);
		_label.setFocusableInTouchMode(true);
		_label.setImeOptions( EditorInfo.IME_ACTION_NEXT );
		
		_input = new Button( context );
		
		
		_input.setLayoutParams( new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) );
		_input.setOnClickListener(new OnClickListener() {			
			public void onClick(View arg0) {
				Show_dialog();
			}
		});
		try {
			if(property.has(TypeFieldProperty.DateFormat.toString()))
			{
				dateFormat=property.getString(TypeFieldProperty.DateFormat.toString());
			}
			if(dateFormat==null || dateFormat.length()==0)
			{
				dateFormat="dd/MM/yyyy";
			}
			
			if(property.has(TypeFieldProperty.Default.toString()))
			{
				String val = property.getString(TypeFieldProperty.Default.toString());
				if(val!=null && val.length()>0)
				{
					StrToDate_str(val, dateFormat);
				}
				else
					date=new Date(System.currentTimeMillis());
			}
			if(date==null)
			{
				date=new Date(System.currentTimeMillis());
			}
			
			_input.setText( getValue() );
			
			
		} catch (JSONException e) {	e.printStackTrace();}
		
		
		_layout.addView( _label );
		_layout.addView( _input );
	}
	
	@Override
	public String getValue(){
		return (String) DateFormat.format(dateFormat, date.getTime());
	}
	
	@Override
	public void setValue( String value ) {
		if(value==null || value.length()==0)
		{
			value=getValue();
		}
		else
		{
			_input.setText( value );
			StrToDate_str(value, dateFormat);
		}
	}
	
	@Override 
	public void setHint( String value ){
		_input.setHint( value );
	}
	
	public void Show_dialog()
	{
		
		
		DatePickerDialog.OnDateSetListener dateListener =
		    new DatePickerDialog.OnDateSetListener() {
		        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
		        	Calendar ca= Calendar.getInstance();
		        	ca.set(yy, mm, dd);
		        	date=ca.getTime();
		        	_input.setText(getValue());
		     }
		};
		Calendar ca= Calendar.getInstance();
		ca.setTime(date);
		
		int dd=ca.get(Calendar.DAY_OF_MONTH);
		int mm=ca.get(Calendar.MONTH);
		int yy=ca.get(Calendar.YEAR);

		DatePickerDialog  dialo = new DatePickerDialog(context,dateListener,yy,mm,dd);
		
		dialo.show();
		
	}
	
	
	public void StrToDate_str(String date_s,String format)
	{
		
		//String format="dd/MM/yyyy";
		
		Locale usEnglish = new Locale("en","US");
		SimpleDateFormat formatter = new SimpleDateFormat(format,usEnglish);
		 long r=new Date().getTime();

			try {
				date=formatter.parse(date_s);
			} catch (ParseException e) {
				date = new Date(System.currentTimeMillis());
				e.printStackTrace();
			}	
	
	}
}
