package com.app.checklist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.app.checklist.model.Entity;
import com.app.checklist.model.db.EntityRepository;
import com.app.checklist.model.db.ValuesRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

public class FirmaModal extends Fragment implements View.OnClickListener {

    public View main_view;
    public static final String EXTRA_URI_FIRMA = "EXTRA_URI_FIRMA";
    DrawableView drawableView;
    DrawableViewConfig config;
    ImageButton btnUndoFirma,btnClearFirma,btnMinStrokeFirma,btnAddStrokeFirma,btnCancelFirma,btnAcceptFirma;
    Bitmap mFirma;
    private Object mGuia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        // TODO Auto-generated method stub


        main_view=inflater.inflate(R.layout.activity_firma_modal, container, false);

        drawableView = (DrawableView)main_view.findViewById(R.id.paintView);

        btnUndoFirma=(ImageButton)main_view.findViewById(R.id.btnUndoFirma);
        btnClearFirma=(ImageButton)main_view.findViewById(R.id.btnClearFirma);
        btnMinStrokeFirma=(ImageButton)main_view.findViewById(R.id.btnMinStrokeFirma);
        btnAddStrokeFirma=(ImageButton)main_view.findViewById(R.id.btnAddStrokeFirma);
        btnAcceptFirma=(ImageButton)main_view.findViewById(R.id.btnAcceptFirma);
        btnCancelFirma=(ImageButton)main_view.findViewById(R.id.btnCancelFirma);

        btnUndoFirma.setOnClickListener(this);
        btnClearFirma.setOnClickListener(this);
        btnMinStrokeFirma.setOnClickListener(this);
        btnAddStrokeFirma.setOnClickListener(this);
        btnAcceptFirma.setOnClickListener(this);
        btnCancelFirma.setOnClickListener(this);

        /*------------------------------------------------------------------------------------------------------*/
        EntityRepository entityRepo = EntityRepository.getInstance(getActivity());
        List<Entity> entityList = entityRepo.getAll();
        Entity entity = null;
        if (entityList.size()>0){
            entity = entityList.get(0);
        }
        ValuesRepository valuesRepository = ValuesRepository.getInstance(getActivity());

        /*JSONObject schema = null;
        try {
            schema = new JSONObject(entity.schema);
            JSONObject out = schema.getJSONObject("out");
            String idField = entity.idField;
            String fieldN = out.getString(idField);
            mGuia = AppState.CurrentValue.getClass().getField(fieldN).get(AppState.CurrentValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }*/

        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return main_view;
    }

    @Override
    public void onResume() {
        super.onResume();
        configDrawableView();
    }

    private void configDrawableView() {
        config = new DrawableViewConfig();
        config.setStrokeColor(ContextCompat.getColor(getActivity(),R.color.negro));
        config.setShowCanvasBounds(true); // If the view is bigger than canvas, with this the user will see the bounds (Recommended)
        config.setStrokeWidth(10.0f);
        config.setCanvasHeight(AppState.Screen_Heigth);
        config.setCanvasWidth(AppState.Screen_Width);
        drawableView.setConfig(config);
        mFirma = Bitmap.createBitmap(config.getCanvasWidth(),config.getCanvasHeight(), Bitmap.Config.ARGB_8888);
        mFirma.eraseColor(ContextCompat.getColor(getActivity(),R.color.white));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUndoFirma:
                undo();
                break;
            case R.id.btnClearFirma:
                clear();
                break;
            case R.id.btnMinStrokeFirma:
                minStroke();
                break;
            case R.id.btnAddStrokeFirma:
                addStroke();
                break;
            case R.id.btnCancelFirma:
                AppState.fr_manager.popBackStack();
                break;
            case R.id.btnAcceptFirma:
                drawableView.obtainBitmap(mFirma);
                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB){
                    new SaveBitmapToDeviceTask(getActivity(),String.valueOf("Trazo"),String.valueOf("Trazo")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,mFirma);
                }else{
                    new SaveBitmapToDeviceTask(getActivity(),String.valueOf("Trazo"),String.valueOf("Trazo")).execute(mFirma);
                }
                break;
        }
    }

    void undo(){
        drawableView.undo();
    }

    void clear(){
        drawableView.clear();
    }

    void minStroke(){
        config.setStrokeWidth(config.getStrokeWidth() - 2);
    }

    void addStroke(){
        config.setStrokeWidth(config.getStrokeWidth() + 2);
    }

    static void accept(Uri url){
        Croquis list = new Croquis();
        list.urlTrazo=url;
        System.out.println(url);
        FragmentTransaction trans=AppState.fr_manager.beginTransaction();
        trans.addToBackStack("list");
        trans.replace(R.id.fragment_container, list,"");
        trans.commit();
    }
}
