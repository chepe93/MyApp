package com.app.checklist;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


import com.app.checklist.model.Entity;
import com.app.checklist.model.Values;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;


public class ApiHelper {

	
	public static String BASE_URL ="http://www.gizone.mx/free/checklist/api.php";
    public static String BASE_IMAGE_URL ="http://";

//    public static String BASE_URL ="http://";
//    public static String BASE_IMAGE_URL ="http://";

	public static int time_out=6000;

    public static String ResutlError="{\"result\":\"fail\",\"code\":\"0\"}";

    public static String genericCall(String services, String json)
    {

        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n";
        String charset = "UTF-8";

        PrintWriter writer = null;
        URLConnection connection = null;
        try {
            connection = new URL(BASE_URL+services).openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStream output = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
            writer.append(json);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (writer != null)
            writer.close();


        try {
            InputStream is = connection.getInputStream();
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";


        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResutlError;

    }


    public static String login(String user, String password){

        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n";
        String charset = "UTF-8";

        PrintWriter writer = null;
        URLConnection connection = null;
        try {
            connection = new URL(BASE_URL+"?action=login").openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream output = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"user\"").append(CRLF);
            writer.append(CRLF);
            writer.append(user).append(CRLF).flush();

            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"password\"").append(CRLF);
            writer.append(CRLF);
            writer.append(password).append(CRLF).flush();
            writer.append("--" + boundary + "--").append(CRLF);
        }
        catch (Exception e) {

        }

        if (writer != null)
            writer.close();


        try {
            InputStream is = connection.getInputStream();
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";

            Log.e("Api login",result);

            return 	result;



        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;

    }


    public static String getEntity(){

        URLConnection connection = null;
        try {

            connection = new URL(BASE_URL).openConnection();
            connection.setConnectTimeout(5000);
        }
        catch (Exception e) {}

        try {
            InputStream is = connection.getInputStream();
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            String r = s.hasNext() ? s.next() : "";
            return r;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{}";

    }

    public static String getList(String user){

        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n";
        String charset = "UTF-8";
        System.out.println("Usuariol"+user);
        PrintWriter writer = null;
        URLConnection connection = null;
        try {
            connection = new URL(BASE_URL+"?action=list").openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream output = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"user\"").append(CRLF);
            writer.append(CRLF);
            writer.append(user).append(CRLF).flush();
        }
        catch (Exception e) {
            System.out.println("exception con "+e);
        }

        if (writer != null)
            writer.close();


        try {
            InputStream is = connection.getInputStream();
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";

            Log.e("Api list",result);

            return 	result;



        } catch (Exception e) {
            System.out.println("exception con get"+e);
            e.printStackTrace();
        }


        return null;

    }

    public static String uploadFile( ContentResolver resolver, String fileUri, String name, String idField,String guia){

        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n";
        String charset = "UTF-8";

        PrintWriter writer = null;
        URLConnection connection = null;
        try {
            connection = new URL(BASE_URL+"?action=upload").openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            connection.setDoOutput(true);
            OutputStream output = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"name\"").append(CRLF);
            writer.append(CRLF);
            writer.append(name).append(CRLF).flush();

            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"idField\"").append(CRLF);
            writer.append(CRLF);
            writer.append(idField).append(CRLF).flush();

            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"guia\"").append(CRLF);
            writer.append(CRLF);
            writer.append(guia).append(CRLF).flush();

            String timeStamp="";
            String[] proj = { MediaStore.Images.Media.TITLE };
            Cursor cursor = resolver.query(Uri.parse(fileUri), proj, null, null, null);
            if (cursor != null && cursor.getCount() != 0) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                cursor.moveToFirst();
                timeStamp=cursor.getString(columnIndex);
            }
            if (cursor != null) {
                cursor.close();
            }

            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"timeStamp\"").append(CRLF);
            writer.append(CRLF);
            writer.append(timeStamp).append(CRLF).flush();

            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"image\"; filename=\"image.jpg\"").append(CRLF);
            writer.append("Content-Type: image/jpeg").append(CRLF);
            writer.append(CRLF).flush();
            InputStream input = resolver.openInputStream(Uri.parse(fileUri));
            byte[] buffer = new byte[1024];
            for (int length = 0; (length = input.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
            }
            output.flush();






            if (input != null)
                input.close();
            writer.append(CRLF).flush();

            writer.append("--" + boundary + "--").append(CRLF);



            if (writer != null)
                writer.close();


            try {
                InputStream is = connection.getInputStream();
                java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
                String result = s.hasNext() ? s.next() : "";
                return 	result;
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {}

        return null;
    }

    public static String updateItems(String user, ContentResolver resolver, JSONArray items)
    {
        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n";
        String charset = "UTF-8";
        PrintWriter writer = null;
        URLConnection connection = null;

        try {



            JSONArray itemsSend = new JSONArray();
            for (int i = 0; i < items.length(); i++) {
                JSONObject jval = items.getJSONObject(i);
                JSONArray fotos = new JSONArray();
                if(jval.has("FOTO")){
                    try {
                        String fotosS = jval.getString("FOTO");
                        String articulo = jval.getString("articulo");
                        String guia = jval.getString("guia");
                        if(fotosS.length()>2){
                            JSONArray fotosArr =  new JSONArray(fotosS);
                            for (int j = 0; j < fotosArr.length(); j++) {
                                String f = fotosArr.getString(j);
                                String resultS = uploadFile(resolver,f,articulo+"_"+(j+1)+".jpg",articulo,guia);
                                JSONObject result = new JSONObject(resultS);
                                if (result.getString("result").equals("success")) {
                                    fotos.put(articulo+"_"+(j+1)+".jpg");
                                }
                            }
                        }
                    }catch (Exception e){ e.printStackTrace();}

                }

                jval.put("FOTO",fotos);
                itemsSend.put(jval);
            }

            String data = itemsSend.toString();
            Log.e("updateItems",data);
            connection = new URL(BASE_URL+"?action=updateItem").openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            connection.setDoOutput(true);
            OutputStream output = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(output, charset), true);


            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"data\"").append(CRLF);
            writer.append(CRLF);
            writer.append(data).append(CRLF).flush();

            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"user\"").append(CRLF);
            writer.append(CRLF);
            writer.append(user).append(CRLF).flush();



            writer.append("--" + boundary + "--").append(CRLF);



        }
        catch (Exception e) {
            e.printStackTrace();

        }

        if (writer != null)
            writer.close();


        try {
            InputStream is = connection.getInputStream();
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";

            Log.e("Api update",result);

            return 	result;



        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;


    }


    public static String getItems(){


        URLConnection connection = null;
        try {

            connection = new URL(BASE_URL+"?action=items").openConnection();
            connection.setConnectTimeout(5000);
        }
        catch (Exception e) {}

        try {
            InputStream is = connection.getInputStream();
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            String r = s.hasNext() ? s.next() : "";
            Log.e("Api items",r);
            return r;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "{}";

    }





//    public static String update(String user,ContentResolver resolver, Entity entity, Values value)
//    {
//
//        String IMGUR_CLIENT_ID = "...";
//        OkHttpClient client = new OkHttpClient();
//
//        try {
//
//            JSONObject jval = value.getJson(entity.schema);
//            String data = jval.toString();
//            String fotosS = jval.getString("FOTO");
//            JSONArray fotos =  new JSONArray(fotosS);
//            String f = fotos.getString(0);
//            String f2 = fotos.getString(0);
//
//
//            if(uriToFile(resolver,Uri.parse(f),AppState.downloads+"foto_1") && uriToFile(resolver,Uri.parse(f2),AppState.downloads+"foto_2")){
//                RequestBody requestBody = new MultipartBody.Builder()
//                        .setType(MultipartBody.FORM)
//                        .addFormDataPart("data", data)
//                        .addFormDataPart("user", user)
//                        .addFormDataPart("foto_1","foto_1.jpg", RequestBody.create(MediaType.parse("image/jpeg"),new File(AppState.downloads+"foto_1")))
//                        .addFormDataPart("f1", "001")
//                        .addFormDataPart("foto_2","foto_2.jpg", RequestBody.create(MediaType.parse("image/jpeg"),new File(AppState.downloads+"foto_2")))
//                        .addFormDataPart("f2", "002")
//                        .build();
//
//                Request request = new Request.Builder()
////                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
//                        .url(BASE_URL+"?action=update")
//                        .post(requestBody)
//                        .build();
//
//                Response response = client.newCall(request).execute();
//                if (!response.isSuccessful()) {
//                    Log.e("Api update error",response.code()+"");
//                    //            ("Unexpected code " + response);
//                }else{
//                    Log.e("Api update",response.toString());
//                    return  response.toString();
//                }
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//
//    }
//

    public static boolean uriToFile(ContentResolver resolver, Uri uri,String filePath) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = resolver.openInputStream(uri);
            outputStream = new FileOutputStream(new File(filePath));
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    // outputStream.flush();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        return false;
    }





	
//	public static ArrayList<State> parseStates(String json){
//
//		ArrayList<State> result= new ArrayList<State>();
//		try {
//			JSONArray jar= new JSONArray(json);
//			for (int i = 0; i < jar.length(); i++) {
//				JSONObject jo=jar.getJSONObject(i);
//				State st = new State(jo);
//				result.add(st);			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return result;
//
//	}

//	public static String getCity(){
//
//		URLConnection connection = null;
//		try {
//
//			connection = new URL(BASE_URL+"city").openConnection();
//			InputStream is = connection.getInputStream();
//			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
//			String r = s.hasNext() ? s.next() : "";
//
//			return r;
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return "[]";
//
//	}
//
//	public static ArrayList<City> parseCities(String json){
//
//		ArrayList<City> result= new ArrayList<City>();
//		try {
//			JSONArray jar= new JSONArray(json);
//			for (int i = 0; i < jar.length(); i++) {
//				JSONObject jo=jar.getJSONObject(i);
//                City st = new City(jo);
//				result.add(st);			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return result;
//
//	}







//    public static ArrayList<Linea> getLineas(long lastUpdate)
//    {
//        ArrayList<Linea> resutl = new ArrayList<>();
//
//        try{
//            JSONObject jsonRequest = new JSONObject();
//            jsonRequest.put("lastUpdate",lastUpdate);
//            String data=jsonRequest.toString();
//            String json_s = ApiHelper.genericCall("api/lineas",data);
//            JSONArray lineas=new JSONArray(json_s);
//            for (int i = 0; i < lineas.length() ; i++) {
//                JSONObject jl=lineas.getJSONObject(i);
//                Linea l = new Linea(jl.toString());
//                resutl.add(l);
//            }
//
//        }catch (Exception e){
//
//        }
//
//        Log.e("ApiHelper Lineas:",resutl.size()+"");
//
//        return resutl;
//
//    }
//
//    public static ArrayList<SubLinea> getSubLineas(long lastUpdate)
//    {
//        ArrayList<SubLinea> resutl = new ArrayList<>();
//
//        try{
//            JSONObject jsonRequest = new JSONObject();
//            jsonRequest.put("lastUpdate",lastUpdate);
//            String data=jsonRequest.toString();
//            String json_s = ApiHelper.genericCall("api/sublineas",data);
//            JSONArray lineas=new JSONArray(json_s);
//            for (int i = 0; i < lineas.length() ; i++) {
//                JSONObject jl=lineas.getJSONObject(i);
//                SubLinea l = new SubLinea(jl.toString());
//                resutl.add(l);
//            }
//
//        }catch (Exception e){
//
//        }
//
//        Log.e("ApiHelper SubLineas:",resutl.size()+"");
//
//        return resutl;
//
//    }
//
//    public static String venta(Ventas venta)
//    {
//
//        try{
//            JSONObject json = new JSONObject();
//            json.put("idCliente",venta.idCliente);
//            json.put("fechaHora",venta.fechaHora);
//            json.put("formaPago",venta.formaPago);
//            json.put("pago",venta.pago);
//            json.put("total",venta.total);
//            json.put("vuelto",venta.vuelto);
//            json.put("estado",venta.estado);
//            json.put("idCaja",AppState.preference.getIdCaja());
//            json.put("idUsuario",AppState.preference.getUserID());
//            json.put("tokenAutenticacion",AppState.preference.getTokenAutenticacion());
//
//            JSONArray json_art= new JSONArray();
//            for (VentasDetalles articulo : venta.articulos) {
//                json_art.put(articulo.toJson());
//            }
//            json.put("articulo",json_art);
//
//            String data=json.toString();
//            String resutl = ApiHelper.genericCall("api/venta",data);
//            return resutl;
//        }catch (Exception e){
//
//        }
//
//        return ResutlError;
//
//    }







	

	
}
