package com.app.checklist;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.app.checklist.model.db.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_CAMERA_REQUEST_CODE);
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        AppState.Get_Intance();
        AppState.activity = this;

        AppState.Screen_Width = displaymetrics.widthPixels;
        AppState.Screen_Heigth = displaymetrics.heightPixels;
        AppState.itemSelected = 0;
        AppState.inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AppState.fr_manager = getSupportFragmentManager();
        AppState.Android_ID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        AppState.preference = iPreferences.Get_Instance();
        AppState.preference.setContext(this);

        //Inicialiso Base de datos de Sitios Seguros
        //Inicialiso Base de datos de Sitios Seguros
        String appPath =getApplicationContext().getFilesDir().getAbsolutePath();
        File downloads = new File(appPath+"/downloads/");
        if(!downloads.exists())
            downloads.mkdirs();
        DatabaseHelper.DB_PATH = appPath+"/downloads/"+DatabaseHelper.DATABASE_NAME;
        File db_path = new File(DatabaseHelper.DB_PATH);
        if(!db_path.exists())
            copyDataBase();

        AppState.downloads=appPath+"/downloads/";




        String addr = AppState.preference.getServer();
        if(addr!=null && addr.length()>0){
            ApiHelper.BASE_URL=addr;
            ApiHelper.BASE_IMAGE_URL=addr+"files/";
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Menu_principal menu = new Menu_principal();
        FragmentTransaction trans = AppState.fr_manager.beginTransaction();
        trans.add(R.id.fragment_container, menu);
        trans.commit();

    }
    private static final int REQUEST_FIRMA = 1239;

    private void copyDataBase() {
        try {
            //Open your local db as the input stream
            InputStream myInput = getAssets().open(DatabaseHelper.DATABASE_NAME);
            // Path to the just created empty db
            String outFileName = DatabaseHelper.DB_PATH;
            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }
            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_exit) {

            AlertDialog.Builder confirm=new AlertDialog.Builder(AppState.activity);
            confirm.setTitle(R.string.Cerrar_sesion);
            confirm.setMessage(R.string.Se_eliminaran_todos_datos_);
            confirm.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AppState.fr_manager.popBackStackImmediate();
                    AppState.preference.setREMEMBER(false);
                    copyDataBase();
                    Login menu = new Login();
                    FragmentTransaction trans = AppState.fr_manager.beginTransaction();
                    trans.add(R.id.fragment_container, menu);
                    trans.commit();
                }
            });
            confirm.setNegativeButton("Cancelar",null);
            confirm.show();



            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_CAMERA_REQUEST_CODE : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
