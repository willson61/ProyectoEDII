package com.example.sthephan.proyectoedii;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListaChats extends AppCompatActivity {

    public static ArrayList<UsuarioItem> ListaChat = new ArrayList<>();
    public static ResponseUsers r;
    public static String user;
    public static Activity fi;
    public static GetAllUsuarioChats get = new GetAllUsuarioChats();
    public static GetLastMessages getLast = new GetLastMessages();
    public static PostToken postT = new PostToken();
    public static GetPassword getPas = new GetPassword();
    public AdapterChat adapter;
    public static String t;

    @BindView(R.id.lstChats)
    ListView lstChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_chats);
        ButterKnife.bind(this);
        t = leerToken();
        if(MainActivity.token != null){
            MainActivity.fa.finish();
        }
        ListaChat = new ArrayList<>();
        adapter = new AdapterChat(this, ListaChat);
        fi = this;
        get = new GetAllUsuarioChats();
        postT = new PostToken();
        getLast = new GetLastMessages();
        getPas = new GetPassword();
        get.setContexto(this);
        postT.setContexto(this);
        getLast.setContexto(this);
        getPas.setContexto(this);
        postT.setToken(t);
        postT.execute("http://10.130.1.134:3000/users/verify4user");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(ListaChats.ListaChat.size() > 0){
                    adapter = new AdapterChat(fi, ListaChat);
                    lstChats.setAdapter(adapter);
                }
            }
        }, 4000);
        lstChats.setClickable(true);
        lstChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UsuarioItem itemValue = (UsuarioItem) lstChats.getItemAtPosition( position );
                itemValue.setNombreUsuarioEmisor(user);
                Chat.usu = itemValue;
                startActivity(new Intent(getApplicationContext(), Chat.class));

                Toast.makeText(getBaseContext(), itemValue.toString(), Toast.LENGTH_LONG).show();

            }
        });
        // Start an alpha animation for clicked item
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(4000);
        //view.startAnimation(animation1);
        //registerForContextMenu(lstChats);
    }

    public String leerToken(){
        FileInputStream inputStream;
        File path1 = this.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        StringBuilder s = new StringBuilder();
        if(path.exists()){
            try {
                inputStream = new FileInputStream(path);
                int val = 0;
                while((val = inputStream.read()) != -1){
                    s.append((char) val);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s.toString();
    }
    public void escribirToken(String token){
        FileOutputStream outputStream;
        File path1 = this.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        if(path.exists()){
            try {
                outputStream = new FileOutputStream(path);
                outputStream.write(token.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void eliminarArchivo(){
        File path1 = this.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        if(path.exists()){
            path.delete();
        }
    }



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Log Out?")
                .setMessage("Esta seguro de que desea cerrar sesion?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        eliminarArchivo();
                        finish();
                        startActivity(new Intent(ListaChats.this, MainActivity.class));
                    }
                }).create().show();
    }

}

class GetAllUsuarioChats extends AsyncTask<String, Void, String> {
    public ArrayList<Usuario> usuarios = new ArrayList<>();
    String res="";
    String path;
    ResponseUsers r;
    ProgressDialog progressDialog;
    public Context contexto;
    public boolean end = false;
    int code = 0;

    public int getCode() {
        return code;
    }

    public boolean isEnd() {
        return end;
    }

    public void limpiarArchivo()throws IOException{
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        PrintWriter writer = new PrintWriter(path);
        writer.print("");
        writer.close();
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setContexto(Context c){
        contexto=c;
    }

    public void escribirToken(String token){
        FileOutputStream outputStream;
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        if(path.exists()){
            try {
                outputStream = new FileOutputStream(path);
                outputStream.write(token.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String leerToken(){
        FileInputStream inputStream;
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        StringBuilder s = new StringBuilder();
        if(path.exists()){
            try {
                inputStream = new FileInputStream(path);
                int val = 0;
                while((val = inputStream.read()) != -1){
                    s.append((char) val);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(contexto);
        progressDialog.setMessage("Cargando usuarios...");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            StringBuilder result = new StringBuilder();
            BufferedReader bufferedReader =null;
            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(10000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");// set header
            urlConnection.connect();
            code = urlConnection.getResponseCode();

            //Read data response from server
            if(urlConnection.getResponseCode() == 401){
                InputStream errorInputStream = urlConnection.getErrorStream();
                bufferedReader = new BufferedReader(new InputStreamReader(errorInputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                res = result.toString();
            }
            else{
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                res = result.toString();
            }
            if(code == 204){
                return String.valueOf(code);
            }
            if(code == 200){
                Gson gson = new Gson();
                Type listType = new TypeToken<ResponseUsers>(){}.getType();
                r = gson.fromJson(res, listType);
                usuarios = r.getUsuarios();
                limpiarArchivo();
                escribirToken(r.getStatus());
                ListaChats.t = leerToken();
                return res;
            }
            else{
                return res;
            }
        } catch (IOException ex) {
            res="Network error !";
            return "Network error !";
        }
    }

    public void eliminarArchivo(){
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        if(path.exists()){
            path.delete();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (progressDialog != null) {
            progressDialog.dismiss();
            if (String.valueOf(code).contains("200") || (String.valueOf(code).contains("204"))) {
                end = true;
                UsuarioItem usI = new UsuarioItem("", "", "", "");
                usI.setNombreUsuarioEmisor(ListaChats.user);
                if(usuarios.size() > 0){
                    for(int i = 0; i < usuarios.size(); i++){
                        usI = new UsuarioItem(ListaChats.user, usuarios.get(i).getUsuario(), "", "");
                        if(!usI.getNombreUsuarioReceptor().equals(usI.getNombreUsuarioEmisor())){
                            ListaChats.ListaChat.add(usI);
                        }
                        /*ListaChats.getLast.execute("http://10.130.1.134:3000/mensajes/ultimos?token=" + ListaChats.t + "&secreto=" + MainActivity.secreto + "&remitente=" + ListaChats.user + "&receptor=" + usI.getNombreUsuarioReceptor());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, 6000);*/
                    }
                }
            }
            if(String.valueOf(code).contains("401")){
                Toast message = Toast.makeText(contexto, "Token Invalido", Toast.LENGTH_LONG);
                message.show();
                eliminarArchivo();
                contexto.startActivity(new Intent(contexto.getApplicationContext(), MainActivity.class));
                ListaChats.fi.finish();
            }
        }
    }
}
class PostToken extends AsyncTask<String, Void, String> {
    public  String token;
    String res="";
    String path;
    ProgressDialog progressDialog;
    public Context contexto;
    int code = 0;
    public boolean end = true;

    public int getCode() {
        return code;
    }

    public void limpiarArchivo()throws IOException{
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        PrintWriter writer = new PrintWriter(path);
        writer.print("");
        writer.close();
    }

    public void setToken(String t) {
        this.token = t;
    }

    public void setContexto(Context c){
        contexto=c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(contexto);
        progressDialog.setMessage("Verificando Token...");
        //progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            path = strings[0];
            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;
            JSONObject dataToSend = new JSONObject();

            dataToSend.put("token", token);
            dataToSend.put("secreto", MainActivity.secreto);

            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(10000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);  //enable output (body data)
            urlConnection.setRequestProperty("Content-Type", "application/json");// set header
            urlConnection.connect();

            OutputStream outputStream = urlConnection.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(dataToSend.toString());
            bufferedWriter.flush();

            if(urlConnection.getResponseCode() == 401){
                InputStream errorInputStream = urlConnection.getErrorStream();
                bufferedReader = new BufferedReader(new InputStreamReader(errorInputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                res = result.toString();
            }
            else{
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                res = result.toString();
            }
            code = urlConnection.getResponseCode();
            return res;

        } catch (IOException ex) {
            res="Network error !";
            return "Network error !";
        } catch (JSONException ex) {
            res="Data Invalid";
            return "Data Invalid !";
        }
    }

    public void escribirToken(String token){
        FileOutputStream outputStream;
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        if(path.exists()){
            try {
                outputStream = new FileOutputStream(path);
                outputStream.write(token.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String leerToken(){
        FileInputStream inputStream;
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        StringBuilder s = new StringBuilder();
        if(path.exists()){
            try {
                inputStream = new FileInputStream(path);
                int val = 0;
                while((val = inputStream.read()) != -1){
                    s.append((char) val);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s.toString();
    }

    public void eliminarArchivo(){
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        if(path.exists()){
            path.delete();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (progressDialog != null) {
            progressDialog.dismiss();
            if(String.valueOf(code).contains("200")){
                Gson gson = new Gson();
                Type listType = new TypeToken<UsuarioToken>(){}.getType();
                UsuarioToken t = gson.fromJson(s, listType);
                ListaChats.user = t.getUsuario();
                try {
                    limpiarArchivo();
                    escribirToken(t.getToken());
                    ListaChats.t = leerToken();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ListaChats.get.execute("http://10.130.1.134:3000/users/conversasiones?token=" + ListaChats.t + "&secreto=" + MainActivity.secreto);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 3000);
            }
            if(String.valueOf(code).contains("401")){
                Toast message = Toast.makeText(contexto, "Token Invalido", Toast.LENGTH_LONG);
                message.show();
                eliminarArchivo();
                contexto.startActivity(new Intent(contexto.getApplicationContext(), MainActivity.class));
                ListaChats.fi.finish();
            }
        }
    }
}
class GetLastMessages extends AsyncTask<String, Void, String> {
    public UltimosMensajes mensajes;
    String res="";
    String path;
    ProgressDialog progressDialog;
    public Context contexto;
    public boolean end = false;
    int code = 0;

    public int getCode() {
        return code;
    }

    public void limpiarArchivo()throws IOException{
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        PrintWriter writer = new PrintWriter(path);
        writer.print("");
        writer.close();
    }

    public boolean isEnd() {
        return end;
    }

    public UltimosMensajes getMensajes() {
        return mensajes;
    }

    public void setContexto(Context c){
        contexto=c;
    }

    public void escribirToken(String token){
        FileOutputStream outputStream;
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        if(path.exists()){
            try {
                outputStream = new FileOutputStream(path);
                outputStream.write(token.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String leerToken(){
        FileInputStream inputStream;
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        StringBuilder s = new StringBuilder();
        if(path.exists()){
            try {
                inputStream = new FileInputStream(path);
                int val = 0;
                while((val = inputStream.read()) != -1){
                    s.append((char) val);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(contexto);
        progressDialog.setMessage("Cargando mensajes...");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            StringBuilder result = new StringBuilder();
            BufferedReader bufferedReader =null;
            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(10000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");// set header
            urlConnection.connect();
            code = urlConnection.getResponseCode();

            //Read data response from server
            if(urlConnection.getResponseCode() == 401){
                InputStream errorInputStream = urlConnection.getErrorStream();
                bufferedReader = new BufferedReader(new InputStreamReader(errorInputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                res = result.toString();
            }
            else{
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                res = result.toString();
            }
            if(code == 204){
                return String.valueOf(code);
            }
            if(code == 200){
                Gson gson = new Gson();
                Type listType = new TypeToken<UltimosMensajes>(){}.getType();
                mensajes = gson.fromJson(res, listType);
                escribirToken(mensajes.getToken());
                ListaChats.t = leerToken();
                return res;
            }
            else{
                return res;
            }
        } catch (IOException ex) {
            res="Network error !";
            return "Network error !";
        }
    }

    public int comparar(Mensaje obj1, Mensaje obj2) {
        Date date1 = null;
        Date date2 = null;
        try{
            date1 = obj1.getFecha();
            date2 = obj2.getFecha();
        }catch(Exception e){
            e.printStackTrace();
        }
        if (date1.equals(date2)) {
            return 0;
        }
        if (String.valueOf(date1).equals("")) {
            return -1;
        }
        if (String.valueOf(date2).equals("")) {
            return 1;
        }
        return date1.compareTo(date2);
    }

    public void eliminarArchivo(){
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        if(path.exists()){
            path.delete();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (progressDialog != null) {
            progressDialog.dismiss();
            if (String.valueOf(code).contains("200")) {
                end = true;
                UsuarioItem usI = new UsuarioItem("", "", "", "");
                usI.setNombreUsuarioEmisor(ListaChats.user);
                if(mensajes.jsonUsuario.size() > 0){
                    for(int i = 0; i < ListaChats.get.usuarios.size(); i++){
                        usI = ListaChats.ListaChat.get(i);
                        if(usI.getNombreUsuarioEmisor().equals(mensajes.jsonUsuario.get(0).getRemitente()) && usI.getNombreUsuarioReceptor().equals(mensajes.jsonUsuario.get(0).getReceptor())){
                            usI.setUltimoMensaje(mensajes.jsonUsuario.get(i).getMensaje());
                            ListaChats.ListaChat.set(i, usI);
                            ListaChats.getPas.us = usI;
                            ListaChats.getPas.execute("http://10.130.1.134:3000/mensajes/claves?token=" + ListaChats.t + "&secreto=" + MainActivity.secreto + "&remitente=" + ListaChats.user + "&receptor=" + usI.getNombreUsuarioReceptor());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                }
                            }, 5000);
                        }
                    }
                }
                /*if(mensajes.jsonUsuario2.size() > 0){

                }*/
            }
            if(String.valueOf(code).contains("401")){
                Toast message = Toast.makeText(contexto, "Token Invalido", Toast.LENGTH_LONG);
                message.show();
                eliminarArchivo();
                ListaChats.fi.finish();
                contexto.startActivity(new Intent(contexto.getApplicationContext(), MainActivity.class));
            }
        }
    }
}
class GetPassword extends AsyncTask<String, Void, String> {
    public claveJson cl;
    public UsuarioItem us;
    String res="";
    String path;
    ProgressDialog progressDialog;
    public Context contexto;
    public boolean end = false;
    int code = 0;

    public int getCode() {
        return code;
    }

    public void limpiarArchivo()throws IOException{
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        PrintWriter writer = new PrintWriter(path);
        writer.print("");
        writer.close();
    }

    public boolean isEnd() {
        return end;
    }

    public claveJson getPassword() {
        return cl;
    }

    public void setContexto(Context c){
        contexto=c;
    }

    public void escribirToken(String token){
        FileOutputStream outputStream;
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        if(path.exists()){
            try {
                outputStream = new FileOutputStream(path);
                outputStream.write(token.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String leerToken(){
        FileInputStream inputStream;
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        StringBuilder s = new StringBuilder();
        if(path.exists()){
            try {
                inputStream = new FileInputStream(path);
                int val = 0;
                while((val = inputStream.read()) != -1){
                    s.append((char) val);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(contexto);
        progressDialog.setMessage("Cargando mensajes...");
        //progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            StringBuilder result = new StringBuilder();
            BufferedReader bufferedReader =null;
            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(10000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");// set header
            urlConnection.connect();
            code = urlConnection.getResponseCode();

            //Read data response from server
            if(urlConnection.getResponseCode() == 401){
                InputStream errorInputStream = urlConnection.getErrorStream();
                bufferedReader = new BufferedReader(new InputStreamReader(errorInputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                res = result.toString();
            }
            else{
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                res = result.toString();
            }
            if(code == 204){
                return String.valueOf(code);
            }
            if(code == 200){
                Gson gson = new Gson();
                Type listType = new TypeToken<claveJson>(){}.getType();
                cl = gson.fromJson(res, listType);
                limpiarArchivo();
                escribirToken(cl.getToken());
                ListaChats.t = leerToken();
                return res;
            }
            else{
                return res;
            }
        } catch (IOException ex) {
            res="Network error !";
            return "Network error !";
        }
    }

    public void eliminarArchivo(){
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        if(path.exists()){
            path.delete();
        }
    }

    public int comparar(Mensaje obj1, Mensaje obj2) {
        Date date1 = null;
        Date date2 = null;
        try{
            date1 = obj1.getFecha();
            date2 = obj2.getFecha();
        }catch(Exception e){
            e.printStackTrace();
        }
        if (date1.equals(date2)) {
            return 0;
        }
        if (String.valueOf(date1).equals("")) {
            return -1;
        }
        if (String.valueOf(date2).equals("")) {
            return 1;
        }
        return date1.compareTo(date2);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (progressDialog != null) {
            progressDialog.dismiss();
            if (String.valueOf(code).contains("200")) {
                end = true;
                UsuarioItem usI = new UsuarioItem("", "", "", "");
                usI.setNombreUsuarioEmisor(ListaChats.user);
                if(ListaChats.ListaChat.size() > 0){
                    for(int i = 0; i < ListaChats.get.usuarios.size(); i++){
                        usI = ListaChats.ListaChat.get(i);
                        if(usI.getNombreUsuarioEmisor().equals(us.getNombreUsuarioEmisor()) && usI.getNombreUsuarioReceptor().equals(us.getNombreUsuarioReceptor())){
                            usI.setClave(cl.getClave());
                            ListaChats.ListaChat.set(i, usI);
                        }
                    }
                }
                /*if(mensajes.jsonUsuario2.size() > 0){

                }*/
            }
            if(String.valueOf(code).contains("401")){
                Toast message = Toast.makeText(contexto, "Token Invalido", Toast.LENGTH_LONG);
                message.show();
                eliminarArchivo();
                ListaChats.fi.finish();
                contexto.startActivity(new Intent(contexto.getApplicationContext(), MainActivity.class));
            }
        }
    }
}
