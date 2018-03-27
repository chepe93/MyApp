package com.app.checklist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.app.checklist.model.SyncDataTask;

import org.json.JSONObject;


public class Login extends Fragment {




    EditText tb_email;
    EditText tb_password;
    Button btn_entrar;
    CheckBox cb_recordar;

//    ImageView img_settings;

    View main_view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        main_view=inflater.inflate(R.layout.login, container, false);

        tb_email=(EditText) main_view.findViewById(R.id.tb_email);
        tb_password=(EditText) main_view.findViewById(R.id.tb_password);
        btn_entrar=(Button) main_view.findViewById(R.id.btn_entrar);
        cb_recordar = (CheckBox)main_view.findViewById(R.id.cb_recordar);
//        img_settings = (ImageView)main_view.findViewById(R.id.img_settings);
        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SyncUserTask().execute("");


            }
        });

//        img_settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ServerDialog dialog = new ServerDialog();
//                dialog.show(AppState.fr_manager,"");
//            }
//        });

        return main_view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(AppState.preference.getREMEMBER()){
            Review_element_Test();
        }
        else{
            tb_email.setText(AppState.preference.getUSER());
        }

    }


    public void Review_element_Test()
    {

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


    class SyncUserTask extends AsyncTask<String, Void, Boolean> {
        private String user,password;
        private boolean recordar = cb_recordar.isChecked();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            user=tb_email.getText().toString();
            password=tb_password.getText().toString();

            AppState.Show_Progrees("Entrando...",0);
        }
        @Override
        protected Boolean doInBackground(String... params) {
            try{

                //{"result":"success","data":{"nombre":"USUARIO DE PRUEBA","direccion":"NA","email":"NA","telefono":"NA","entidad":null,
                // "ciudad":null,"usuario":"daniel","clave":"81dc9bdb52d04dc20036dbd8313ed055","pcambio":"2015-05-25","fecha":"2014-11-04","usuarioc":"RAECAM"}}{"result":"fail"}

                String resutl = ApiHelper.login(user,password);
                JSONObject json = new JSONObject(resutl);
                if (json.getString("result").equals("success")) {
                    JSONObject data = json.getJSONObject("data");

                    iPreferences pre=AppState.preference;

                    String nombre = getStringFrom(data,"nombre");
                    String direccion = getStringFrom(data,"direccion");
                    String email = getStringFrom(data,"email");
                    String phone = getStringFrom(data,"telefono");

                    pre.setREMEMBER(recordar);
                    pre.setName(nombre);
                    pre.setAddress(direccion);
                    pre.setEMAIL(email);
                    pre.setPHONE(phone);
                    pre.setUSER(user);
                    pre.setPASSOWRD(password);
                    return true;

                }
            }catch (Exception e){}
            return false;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            AppState.Hide_Progrees();
            if (s){
                Review_element_Test();
            }
            else{
                Toast.makeText(getActivity(), R.string.usuario_contrasena_no,Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            AppState.Hide_Progrees();

        }
    }

    public String getStringFrom(JSONObject json, String key){
        try {
            return json.getString(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
