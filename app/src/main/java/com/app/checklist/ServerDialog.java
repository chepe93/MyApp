package com.app.checklist;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;


public class ServerDialog extends DialogFragment {



    EditText tb_address;
    EditText tb_password;
    Button btn_aceptar;

    View main_view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        main_view=inflater.inflate(R.layout.server_dialog, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tb_address=(EditText) main_view.findViewById(R.id.tb_address);
        btn_aceptar=(Button) main_view.findViewById(R.id.btn_aceptar);
        tb_password=(EditText) main_view.findViewById(R.id.tb_password);



        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               new SyncApiTask().execute("");

            }
        });






        return main_view;
    }

    @Override
    public void onResume() {
        super.onResume();

        String addr = AppState.preference.getServer();
        if(addr!=null && addr.length()>0){
            tb_address.setText(addr);
        }


    }


    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    class SyncApiTask extends AsyncTask<String, Void, Boolean> {
        private String addres,password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            password=tb_password.getText().toString();
            addres=tb_address.getText().toString();
            AppState.Show_Progrees("Cargando...",0);
        }
        @Override
        protected Boolean doInBackground(String... params) {

            if (addres != null && addres.length()>0) {

                try{
                    ApiHelper.BASE_URL = addres;
                    String resutl = ApiHelper.getEntity();
                    JSONObject json = new JSONObject(resutl);
                    if (json.getString("result").equals("success")) {
                        String passMd5 = json.getString("password");
                        Log.e("MD5",MD5("4321"));
                        if(MD5(password).equals(passMd5)){
                            return true;
                        }



                    }
                }catch (Exception e){}

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            AppState.Hide_Progrees();

            if (s){
                String addr = tb_address.getText().toString();
                if(addr!=null && addr.length()>0){
                    AppState.preference.setServer(addr);
                    ApiHelper.BASE_URL=addr;
                    ApiHelper.BASE_IMAGE_URL=addr+"files/";
                }
                dismiss();
            }
            else{
                Toast.makeText(getActivity(), R.string.Contrase_a_Invalidad,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
            AppState.Hide_Progrees();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            AppState.Hide_Progrees();

        }
    }
}
