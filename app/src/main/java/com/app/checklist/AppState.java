package com.app.checklist;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import com.app.checklist.formgenerator.FormImage;
import com.app.checklist.model.Values;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


/*
 * This is a Singleton Class use to control the application State.  
 */

public class AppState {

	public static AppState _intance;
	public static Context aplicationContext;
	public static FragmentActivity activity;
	public static String downloads;
	public static LayoutInflater inflater;
	

	public static boolean dialog_showing=false;
	public static ProgressDialog dialog;
	public static Timer dialog_timer;
	public static boolean dialog_timer_run=false;
	public static String lang;
	public static int Screen_Width;
	public static int Screen_Heigth;
	public static int itemSelected;

    public static iPreferences preference;
	
	public static FragmentManager fr_manager;

	public static String Android_ID;
	public static String db_path;

	
	public static boolean debug=false;
	
	public static String ping="";
	public static ArrayList<Uri> array_images;
    public static Uri imageUri;
    public static FormImage image_widget;

	public static Runnable OnChange;

	public static Values CurrentValue;

    private AppState()
	{
		lang = Locale.getDefault().getLanguage();
		if(lang==null || lang.length()==0)
		{
			lang="en";
		}
		Android_ID="";
		db_path="";

		array_images = new ArrayList<>();
		imageUri = null;

        OnChange = null;
	}




	public static void Show_Progrees(String text, long time)
	{
		if(dialog_showing)
			dialog.setTitle(text);
		else
			{
			dialog= ProgressDialog.show(activity, null,text);
	        dialog_showing=true;
			}
		
		if(dialog_timer_run)
		{
			dialog_timer.cancel();
			dialog_timer_run = false;	
		}
		if(time>0)
		{
			dialog_timer = new Timer();
			dialog_timer.schedule(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					dialog_timer_run=false;
					dialog_timer.cancel();
				}
			},time);
			dialog_timer_run = true;
			
		}
		
	}
	
	public static void Show_Progrees(int text, long time)
	{
		if(dialog_showing)
			dialog.setTitle(text);
		else
			{
			dialog= ProgressDialog.show(activity, null,AppState.getString(text));
	        dialog_showing=true;
			}
		
		if(dialog_timer_run)
		{
			dialog_timer.cancel();
			dialog_timer_run = false;	
		}
		if(time>0)
		{
			dialog_timer = new Timer();
			dialog_timer.schedule(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					dialog_timer_run=false;
					dialog_timer.cancel();
				}
			},time);
			dialog_timer_run = true;
			
		}
		
	}
	
	public static void Hide_Progrees()
	{
		if(dialog_showing)
		{
			dialog.cancel();						
	        dialog_showing=false;
		}
	}
	
	
	public static void Set_Activity(FragmentActivity activity)
	{
		AppState.activity=activity;
	}

	public static void hideKeyboard(Activity act) {
		InputMethodManager inputManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);

		// check if no view has focus:
		View v = act.getCurrentFocus();
		if (v == null)
			return;

		inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
	
	public static AppState Get_Intance()
	{
		if(_intance==null)
		{
			_intance = new AppState();
		}
		
		return _intance;
	}
	
	public static View InflateView(int layout)
	{
		return inflater.inflate(layout, null);
	}
	
	public static String getString(int string)
	{
		return activity.getString(string);
	}
	
	public static View findView(int id)
	{
		return activity.findViewById(id);
	}
	
	public static void Show_Alert(String title, String Messange, String Ok_button)
	{
		Builder confirm=new Builder(AppState.activity);
		if(title!=null && title.length()>0)
			confirm.setTitle(title);
		confirm.setMessage(Messange);
		confirm.setPositiveButton(Ok_button,null);
		confirm.show();
	}
	
	
	


	
	
	
	public static int strToInteger(String Val)
	{
 
		int r=0;
			try {
				r= Integer.parseInt(Val);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return r;	
	}
	
	public static long strToLong(String Val)
	{
 
		long r=0;
			try {
				r= Long.parseLong(Val);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return r;	
	}
	
	public static double strToDouble(String Val)
	{
		Val=Val.replace(",", ".");
 
		double r=0;
			try {
				r= Double.parseDouble(Val);
				return r;
			} catch (Exception e) {
			}
		try {
			r= NumberFormat.getCurrencyInstance().parse(Val).doubleValue();
			return r;
		} catch (Exception e) {
		}


		return r;	
	}
	
	
	public static boolean strToBoolean(String Val)
	{
		
 
		boolean r=false;
			try {
				r= Boolean.parseBoolean(Val);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(Val!=null && Val.equals("1"))
			return true;

		return r;	
	}


	public static String LongToDateStr(long r)
	{


		String format="dd-MM HH:mm:ss a";

		Locale usEnglish = new Locale("en","US");
		SimpleDateFormat formatter = new SimpleDateFormat(format,usEnglish);

		return (String) DateFormat.format(format, r);

	}

	public static String longSecoundToDateStr(long r)
	{

        if(r<=0)
            return "";

//		String format="dd-MM HH:mm:ss a";
		String format="dd/MM/yyyy";
		Locale usEnglish = new Locale("en","US");
		SimpleDateFormat formatter = new SimpleDateFormat(format,usEnglish);

		return (String) DateFormat.format(format, r*1000);

	}

	public static Date strToDate(String date, String format)
	{

//		String format="dd/MM/yyyy";

		Locale usEnglish = new Locale("en","US");
		SimpleDateFormat formatter = new SimpleDateFormat(format,usEnglish);
		Date d= new Date();

		try {
			d=formatter.parse(date);
		} catch (ParseException e) {

		}
//		return (String) DateFormat.format("yyyy-MM-dd", r);

		return d;

	}

	public static String dateToStr(Date date, String format)
	{
//		return (String) DateFormat.format("yyyy-MM-dd", r);
//		DateFormat.format()
		return (String) DateFormat.format(format,date.getTime());
	}




	public static long getTime()
	{
		return System.currentTimeMillis()/1000;
	}

	public static String getTimeS()
	{
		return getTime()+"";
	}

    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int pxToDp(int px) {
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }


    public static String readFileAssetsToString( String filename )
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
