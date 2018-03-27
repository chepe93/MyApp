package com.app.checklist;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.checklist.model.Entity;
import com.app.checklist.model.Values;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Fragment_Image extends Fragment {
	
	private View mainView;
	private ViewPager image_view_pager;
	private LinearLayout nav_bar;
	private ImagePagerAdapter page_adapter;
	public static int pick_photo=789;

	private static final int REQUEST_FIRMA = 1239;
	public Entity entity;
	public java.util.List<Values> values;

	public Fragment_Image() {
		super();

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		AppState.array_images=new ArrayList<Uri>();
        if (AppState.image_widget != null) {
            try {
                String images =AppState.image_widget.getValue();
                JSONArray jarr = new JSONArray(images);
                for (int i = 0; i <jarr.length() ; i++) {
                    String img = jarr.getString(i);
                    Uri uri = Uri.parse(img);
                    AppState.array_images.add(uri);
                }
            }catch (Exception e){e.printStackTrace();}

        }

		mainView = inflater.inflate(R.layout.fragment_image, container, false);
		mainView.setOnClickListener(new OnClickListener() {			
			public void onClick(View arg0) {}});
		
		image_view_pager = (ViewPager) mainView.findViewById(R.id.image_view_pager);
		nav_bar = (LinearLayout) mainView.findViewById(R.id.nav_bar);
		
		LinearLayout btn_add= (LinearLayout) mainView.findViewById(R.id.ll_add);
        LinearLayout btn_remove= (LinearLayout) mainView.findViewById(R.id.ll_delete);
        LinearLayout btn_save= (LinearLayout) mainView.findViewById(R.id.ll_save);
		
		btn_add.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
                String fileName = (System.currentTimeMillis()/1000)+"";

//				try{
//
//					File imageFile = new File(AppState.downloads+fileName);
//					if(!imageFile.exists())
//						imageFile.createNewFile();
//					AppState.imageUri = Uri.fromFile(imageFile);
//				}catch (Exception e){e.printStackTrace();}
//
				ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
//                values.put(MediaStore.Images.Media.DESCRIPTION, description);
//                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//                // Add the date meta data to ensure the image is added at the front of the gallery
//                values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
//                values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
				AppState.imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, AppState.imageUri);
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				startActivityForResult(intent,pick_photo);
            }
		});

		btn_remove.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(AppState.array_images.size()>0)
				{
					Builder confirm=new Builder(getActivity());
	       			confirm.setTitle("Eliminar Imagen");
	       			confirm.setMessage("Desea eliminar la imagen");
	       			confirm.setPositiveButton(R.string.aceptar,new DialogInterface.OnClickListener() {
	       				public void onClick(DialogInterface arg0, int arg1) {
	       					Uri path=AppState.array_images.get(page_adapter.position);
	       					AppState.array_images.remove(page_adapter.position);

//	       					new File(path).delete();
							page_adapter = new ImagePagerAdapter(AppState.array_images,nav_bar);
							image_view_pager.setAdapter(page_adapter);
							image_view_pager.setOnPageChangeListener(page_adapter);
							if(AppState.array_images.size()>0)
								page_adapter.set_nav_bar(0);
	       				}});
	       			confirm.setNegativeButton(R.string.Cancelar, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {}
						});
	       			confirm.show();
				}
			}	
			});
		
		
		btn_save.setOnClickListener(new OnClickListener() {			
			public void onClick(View arg0) {
				String images="";

				try{
					JSONArray arr = new JSONArray();

					for (int i = 0; i < AppState.array_images.size(); i++) {
						arr.put(AppState.array_images.get(i).toString());
					}
                    images = arr.toString();
                    AppState.image_widget.setValue(images);
				}catch (Exception e){ e.printStackTrace();}

				
				FragmentManager manager=getFragmentManager();
				FragmentTransaction trans=manager.beginTransaction();
				manager.popBackStack();
				trans.commit();

				
			}});
				
		return mainView;

	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		page_adapter = new ImagePagerAdapter(AppState.array_images,nav_bar);
		
		image_view_pager.setAdapter(page_adapter);
		image_view_pager.setOnPageChangeListener(page_adapter);
		if(AppState.array_images.size()>0)
			page_adapter.set_nav_bar(0);
	}

	
	
	@Override
	public void onStop() {
		super.onStop();
		
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == pick_photo &&  resultCode == Activity.RESULT_OK)
		{
				if(AppState.imageUri!=null)
				{
					AppState.array_images.add(AppState.imageUri);
				}
			page_adapter.notifyDataSetChanged();
		}else if (requestCode == REQUEST_FIRMA && resultCode == Activity.RESULT_OK){
			AppState.array_images.add(0,(Uri) data.getExtras().get(FirmaModal.EXTRA_URI_FIRMA));
			page_adapter.notifyDataSetChanged();
		}
	}

	private void copyFile(InputStream in, OutputStream out)  {
		try {
			byte[] buffer = new byte[1024];
			int read;
			while((read = in.read(buffer)) != -1){

				out.write(buffer, 0, read);

			}} catch (IOException e) {e.printStackTrace();}
	}
}
