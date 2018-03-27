package com.app.checklist;

import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ImagePagerAdapter extends PagerAdapter implements OnPageChangeListener {

	private ArrayList<Uri> url_image;
	public LinearLayout nav_bar;
	public int position;
	public ImagePagerAdapter(ArrayList<Uri> url_image, LinearLayout nav_bar)
	{
		this.url_image=url_image;
		this.nav_bar=nav_bar;	
		
	}
    public int getCount() {
        return url_image.size();
    }
    
    public void set_nav_bar(int pos)
    {
    	if(nav_bar!=null)
    	{
			nav_bar.removeAllViews();
			for (int i = 0; i < url_image.size(); i++) {
				if(pos==i)
				{
					ImageView img= new ImageView(AppState.activity);
					img.setBackgroundResource(R.drawable.gtc_nav_bar_active);
					LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.gravity = Gravity.CENTER_VERTICAL; 
					params.setMargins(3, 3, 3, 3);
					img.setLayoutParams(params);
					nav_bar.addView(img);
				}
				else
				{
					ImageView img= new ImageView(AppState.activity);
					img.setBackgroundResource(R.drawable.gtc_nav_bar_inactive);
					LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.gravity = Gravity.CENTER_VERTICAL; 
					params.setMargins(3, 3, 3, 3);
					img.setLayoutParams(params);
					nav_bar.addView(img);
				}
				
			}

    	}
    }

    public Object instantiateItem(View collection, final int position) {
        
        ImageView img = new ImageView(AppState.activity);
        img.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				//FragmentManager manager=getFragmentManager();
//				News_Detail_image newFragment = new News_Detail_image();
//				newFragment.img_url=url_image.get(position);
//				FragmentTransaction trans=manager.beginTransaction();
//				trans.replace(R.id.fragment_container, newFragment);
//				trans.addToBackStack(null);
//				trans.commit();
				
				//Toast.makeText(AplicationState.activity.getf, , Toast.LENGTH_LONG).show();
			}
		});




        Picasso.with(AppState.activity).load(url_image.get(position)).resize(AppState.Screen_Width,  AppState.Screen_Width).centerInside().into(img);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        img.setLayoutParams(params);
        img.setScaleType(ScaleType.CENTER_INSIDE);
        ((ViewPager) collection).addView(img, 0);
        return img;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);

    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);

    }

    @Override
    public Parcelable saveState() {
        return null;
    }
    
	public void onPageScrollStateChanged(int arg0) {}
	public void onPageScrolled(int arg0, float arg1, int arg2) {}
	public void onPageSelected(int pos) {
		 set_nav_bar(pos);
		 position=pos;
	}


    
}