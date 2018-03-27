package com.app.checklist;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.checklist.model.SyncDataTask;


public class Splash extends Fragment {
	View main_view;
	//Async_Data_Loader async_loader;
	String Call;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		main_view=inflater.inflate(R.layout.splash, container, false);

		
		new Handler().postDelayed(new Runnable(){
            //@Override
            public void run() {

				if(AppState.preference.getREMEMBER()){
					SyncDataTask syn = new SyncDataTask();
					syn.onFinish = new Runnable() {
						@Override
						public void run() {
							AppState.Hide_Progrees();
							Menu_principal list = new Menu_principal();
							FragmentTransaction trans=AppState.fr_manager.beginTransaction();
							trans.replace(R.id.fragment_container, list,"");
							trans.commit();

						}
					};
					syn.execute(getActivity());

					AppState.Show_Progrees("Loading...",0);
				}
				else{
					Login menu = new Login();
					FragmentTransaction trans = AppState.fr_manager.beginTransaction();
					trans.replace(R.id.fragment_container, menu);
					trans.commit();
				}


            
            	
            }
       }, 1000);
		
		return main_view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
}
