package com.app.checklist;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

public class Croquis extends Fragment implements View.OnClickListener{
    public View main_view;
    ImageButton btnExhibidor,btnRefri,btnRepisa,btnVitrina,btnTendero,btnRectangulo,btnCirculo;
    ImageButton btnCancelar,btnSiguiente;
    RelativeLayout view;
    public Uri urlTrazo;
    private int _xDelta;
    private int _yDelta;
    Bitmap croquis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub


        main_view=inflater.inflate(R.layout.fragment_croquis, container, false);
        btnExhibidor=(ImageButton)main_view.findViewById(R.id.btnExhibidor);
        btnRefri=(ImageButton)main_view.findViewById(R.id.btnRefri);
        btnRepisa=(ImageButton)main_view.findViewById(R.id.btnRepisa);
        btnVitrina=(ImageButton)main_view.findViewById(R.id.btnVitrina);
        btnTendero=(ImageButton)main_view.findViewById(R.id.btnTendero);
        btnRectangulo=(ImageButton)main_view.findViewById(R.id.btnRectangulo);
        btnCirculo=(ImageButton)main_view.findViewById(R.id.btnCirculo);

        btnCancelar=(ImageButton)main_view.findViewById(R.id.btnCancelar);
        btnSiguiente=(ImageButton)main_view.findViewById(R.id.btnSiguiente);

        view = (RelativeLayout) main_view.findViewById(R.id.lienzo);

        btnExhibidor.setOnClickListener(this);
        btnRefri.setOnClickListener(this);
        btnRepisa.setOnClickListener(this);
        btnVitrina.setOnClickListener(this);
        btnTendero.setOnClickListener(this);
        btnRectangulo.setOnClickListener(this);
        btnCirculo.setOnClickListener(this);

        btnCancelar.setOnClickListener(this);
        btnSiguiente.setOnClickListener(this);
        if(urlTrazo !=null){
            InputStream inputStream = null;
            try {
                inputStream = getContext().getContentResolver().openInputStream(urlTrazo);
                Drawable trazoFondo = Drawable.createFromStream(inputStream, urlTrazo.toString() );
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(trazoFondo);
                    croquis = Bitmap.createBitmap(AppState.Screen_Width, AppState.Screen_Heigth, Bitmap.Config.ARGB_8888);
                    croquis.eraseColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }



        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return main_view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public class ChoiceTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            final int X = (int) motionEvent.getRawX();
            final int Y = (int) motionEvent.getRawY();
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                            .getLayoutParams();
                    layoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin = Y - _yDelta;
                    layoutParams.rightMargin = -250;
                    layoutParams.bottomMargin = -250;
                    view.setLayoutParams(layoutParams);
                    break;
            }
            view.invalidate();
            return true;
        }
    }

    private class ChoiceDragListener implements View.OnDragListener{

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch(dragEvent.getAction()){
                case DragEvent.ACTION_DRAG_STARTED:
                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    break;

                case DragEvent.ACTION_DROP:
                    ImageView image = (ImageView) dragEvent.getLocalState();
                    ((ImageView) view).setImageDrawable(getResources().getDrawable(R.drawable.ic_check_black_24dp));
                    ((ImageView) image).setImageDrawable(null);

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    break;
            }
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        LinearLayout.LayoutParams familyimagelayout = new LinearLayout.LayoutParams(100,100);
        switch (v.getId()){
            case R.id.btnCancelar:
                //finish();
                AppState.fr_manager.popBackStack();
                break;
            case R.id.btnSiguiente:
                Canvas c = new Canvas(croquis);
                view.draw(c);
                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB){
                    new SaveBitmapToDeviceTask(getActivity(),String.valueOf("Croquis"),String.valueOf("Croquis")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,croquis);
                }else{
                    new SaveBitmapToDeviceTask(getActivity(),String.valueOf("Croquis"),String.valueOf("Croquis")).execute(croquis);
                }

                Menu_principal list = new Menu_principal();
                FragmentTransaction trans=AppState.fr_manager.beginTransaction();
                trans.replace(R.id.fragment_container, list,"");
                trans.commit();
                break;

            case R.id.btnExhibidor:
                ImageView option = new ImageView(getActivity());
                option.setImageResource(R.drawable.exhibidor);

                option.setLayoutParams(familyimagelayout);
                option.setOnTouchListener(new ChoiceTouchListener());
                option.setOnDragListener(new ChoiceDragListener());
                view.addView(option);
                break;
            case R.id.btnRefri:
                option = new ImageView(getActivity());
                option.setImageResource(R.drawable.refri);
                option.setLayoutParams(familyimagelayout);
                option.setOnTouchListener(new ChoiceTouchListener());
                option.setOnDragListener(new ChoiceDragListener());
                view.addView(option);
                break;
            case R.id.btnRepisa:
                option = new ImageView(getActivity());
                option.setImageResource(R.drawable.repi);
                option.setLayoutParams(familyimagelayout);
                option.setOnTouchListener(new ChoiceTouchListener());
                option.setOnDragListener(new ChoiceDragListener());
                view.addView(option);
                break;
            case R.id.btnVitrina:
                option = new ImageView(getActivity());
                option.setImageResource(R.drawable.vitrina);
                option.setLayoutParams(familyimagelayout);
                option.setOnTouchListener(new ChoiceTouchListener());
                option.setOnDragListener(new ChoiceDragListener());
                view.addView(option);
                break;

            case R.id.btnTendero:
                option = new ImageView(getActivity());
                option.setImageResource(R.drawable.tendero);
                option.setLayoutParams(familyimagelayout);
                option.setOnTouchListener(new ChoiceTouchListener());
                option.setOnDragListener(new ChoiceDragListener());
                view.addView(option);
                break;

            case R.id.btnRectangulo:
                option = new ImageView(getActivity());
                option.setImageResource(R.drawable.rectangle);
                option.setLayoutParams(familyimagelayout);
                option.setOnTouchListener(new ChoiceTouchListener());
                option.setOnDragListener(new ChoiceDragListener());
                view.addView(option);
                break;

            case R.id.btnCirculo:
                option = new ImageView(getActivity());
                option.setImageResource(R.drawable.circle);
                option.setLayoutParams(familyimagelayout);
                option.setOnTouchListener(new ChoiceTouchListener());
                option.setOnDragListener(new ChoiceDragListener());
                view.addView(option);
                break;
        }
    }
}
