package com.app.checklist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



public class Menu_principal extends Fragment {
    View main_view;
    Button btn_lienzo,btn_foto,btn_video;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        main_view=inflater.inflate(R.layout.fragment_menu_principal, container, false);
        btn_lienzo=(Button) main_view.findViewById(R.id.btn_lienzo);
        btn_foto=(Button) main_view.findViewById(R.id.btn_foto);
        btn_video=(Button) main_view.findViewById(R.id.btn_video);
        btn_lienzo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirmaModal list = new FirmaModal();
                FragmentTransaction trans=AppState.fr_manager.beginTransaction();
                trans.addToBackStack("list");
                trans.replace(R.id.fragment_container, list,"");
                trans.commit();
            }
        });

        btn_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Fragment_Image newFragment = new Fragment_Image();
                FragmentTransaction trans= AppState.fr_manager.beginTransaction();
                trans.addToBackStack("");
                trans.add(R.id.fragment_container, newFragment);
                trans.commit();
            }
        });

        btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Camera2VideoFragment newFragment = new Camera2VideoFragment();
                FragmentTransaction trans= AppState.fr_manager.beginTransaction();
                trans.addToBackStack("");
                trans.add(R.id.fragment_container, newFragment);
                trans.commit();
            }
        });

        return main_view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
