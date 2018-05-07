package com.app.checklist;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toolbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

//import ulr.co.setab.phothoview.PhotoViewAtacher;

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
    String pruebaGithub="hola errores";

    ImageView rectangle,circle;
    Toolbar toolbar;
    float scalediff;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);



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
        RelativeLayout.LayoutParams parms;
        int startwidth;
        int startheight;
        float dx = 0, dy = 0, x = 0, y = 0;
        float angle = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final ImageView view = (ImageView) v;

            for(int i=0;i<imagenes.size();i++){
                if(view.getId()!=imagenes.get(i).getId()){
                    imagenes.get(i).setEnabled(false );
                }
            }
            ((BitmapDrawable) view.getDrawable()).setAntiAlias(true);
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:

                    parms = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    startwidth = parms.width;
                    startheight = parms.height;
                    dx = event.getRawX() - parms.leftMargin;
                    dy = event.getRawY() - parms.topMargin;
                    mode = DRAG;
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        mode = ZOOM;
                    }

                    d = rotation(event);

                    break;
                case MotionEvent.ACTION_UP:

                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;

                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {

                        x = event.getRawX();
                        y = event.getRawY();

                        parms.leftMargin = (int) (x - dx);
                        parms.topMargin = (int) (y - dy);

                        parms.rightMargin = 0;
                        parms.bottomMargin = 0;
                        parms.rightMargin = parms.leftMargin + (5 * parms.width);
                        parms.bottomMargin = parms.topMargin + (10 * parms.height);

                        view.setLayoutParams(parms);

                    } else if (mode == ZOOM) {

                        if (event.getPointerCount() == 2) {

                            newRot = rotation(event);
                            float r = newRot - d;
                            angle = r;

                            x = event.getRawX();
                            y = event.getRawY();

                            float newDist = spacing(event);
                            if (newDist > 10f) {
                                float scale = newDist / oldDist * view.getScaleX();
                                if (scale > 0.6) {
                                    scalediff = scale;
                                    view.setScaleX(scale);
                                    view.setScaleY(scale);

                                }
                            }

                            view.animate().rotationBy(angle).setDuration(0).setInterpolator(new LinearInterpolator()).start();

                            x = event.getRawX();
                            y = event.getRawY();

                            parms.leftMargin = (int) ((x - dx) + scalediff);
                            parms.topMargin = (int) ((y - dy) + scalediff);

                            parms.rightMargin = 0;
                            parms.bottomMargin = 0;
                            parms.rightMargin = parms.leftMargin + (5 * parms.width);
                            parms.bottomMargin = parms.topMargin + (10 * parms.height);

                            view.setLayoutParams(parms);


                        }
                    }
                    break;
            }
            return true;

        }
    }

    private class ChoiceDragListener implements View.OnDragListener{


       /* for()
            if()
        */
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

                    image.getId();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    break;
            }
            return true;
        }
    }

   /* @Override
    public boolean onLongClick(View v){

    }*/

    /*arreglo de imageview*/
    ArrayList<ImageView> imagenes= new ArrayList <ImageView> ();

    @Override
    public void onClick(View v) {
            LinearLayout.LayoutParams familyimagelayout = new LinearLayout.LayoutParams(100,100);
            LinearLayout.LayoutParams familyimagelayout1 = new LinearLayout.LayoutParams(750,750);

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
                option.setId(imagenes.size());
                imagenes.add(option);
                view.addView(option);
                break;
            case R.id.btnRefri:
                option = new ImageView(getActivity());
                option.setImageResource(R.drawable.refri);
                option.setLayoutParams(familyimagelayout);
                option.setOnTouchListener(new ChoiceTouchListener());
                option.setOnDragListener(new ChoiceDragListener());
                option.setId(imagenes.size());
                imagenes.add(option);
                view.addView(option);
                break;
            case R.id.btnRepisa:
                option = new ImageView(getActivity());
                option.setImageResource(R.drawable.repi);
                option.setLayoutParams(familyimagelayout);
                option.setOnTouchListener(new ChoiceTouchListener());
                option.setOnDragListener(new ChoiceDragListener());
                option.setId(imagenes.size());
                imagenes.add(option);
                view.addView(option);
                break;
            case R.id.btnVitrina:
                option = new ImageView(getActivity());
                option.setImageResource(R.drawable.vitrina);
                option.setLayoutParams(familyimagelayout);
                option.setOnTouchListener(new ChoiceTouchListener());
                option.setOnDragListener(new ChoiceDragListener());
                option.setId(imagenes.size());
                imagenes.add(option);
                view.addView(option);
                break;

            case R.id.btnTendero:
                option = new ImageView(getActivity());
                option.setImageResource(R.drawable.tendero);
                option.setLayoutParams(familyimagelayout);
                option.setOnTouchListener(new ChoiceTouchListener());
                option.setOnDragListener(new ChoiceDragListener());
                option.setId(imagenes.size());
                imagenes.add(option);
                view.addView(option);
                break;

            case R.id.btnRectangulo:
                option = new ImageView(getActivity());
                option.setImageResource(R.drawable.rectangle);
                option.setLayoutParams(familyimagelayout1);
                option.setOnTouchListener(new ChoiceTouchListener());
                option.setOnDragListener(new ChoiceDragListener());
                option.setId(imagenes.size());
                imagenes.add(option);
                view.addView(option);
                break;



            case R.id.btnCirculo:
                option = new ImageView(getActivity());
                option.setImageResource(R.drawable.circle);
                option.setLayoutParams(familyimagelayout1);
                option.setOnTouchListener(new ChoiceTouchListener());
                option.setOnDragListener(new ChoiceDragListener());
                option.setId(imagenes.size());
                imagenes.add(option);
                view.addView(option);
                break;





        }
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }
}





