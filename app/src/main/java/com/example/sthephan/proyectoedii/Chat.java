package com.example.sthephan.proyectoedii;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Chat extends AppCompatActivity {

    public static UsuarioItem usu;
    public static ArrayList<Mensaje> mensajes = new ArrayList<>();
    public static  String t;
    public static Activity fi;
    @BindView(R.id.edittext_chatbox)
    EditText edittextChatbox;
    @BindView(R.id.layout_chatbox)
    LinearLayout layoutChatbox;
    @BindView(R.id.message_list)
    ListView messageList;

    private EditText editText;
    public AdapterMensajes adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        messageList.setDivider(null);
        messageList.setDividerHeight(0);
        adapter = new AdapterMensajes(this, mensajes, usu);
        messageList.setLongClickable(true);
        //editText = (EditText) findViewById(R.id.edittext_chatbox);
        registerForContextMenu(messageList);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ct_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Mensaje m = (Mensaje) messageList.getItemAtPosition(info.position);
        switch (item.getItemId()) {
            case R.id.borrarMensaje:
                Toast message = Toast.makeText(getApplicationContext(), "No se ha escrito ningun mensaje", Toast.LENGTH_LONG);
                message.show();
                edittextChatbox.setText(m.getRemitente());
                return true;
            case R.id.infoMensaje:
                edittextChatbox.setText(m.getRemitente());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void sendMessage(View view) {
        String message = editText.getText().toString();
        if (message.length() > 0) {

            editText.getText().clear();
        }
    }


    public String leerToken() {
        FileInputStream inputStream;
        File path1 = this.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        StringBuilder s = new StringBuilder();
        if (path.exists()) {
            try {
                inputStream = new FileInputStream(path);
                int val = 0;
                while ((val = inputStream.read()) != -1) {
                    s.append((char) val);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s.toString();
    }

    public void escribirToken(String token) {
        FileOutputStream outputStream;
        File path1 = this.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        if (path.exists()) {
            try {
                outputStream = new FileOutputStream(path);
                outputStream.write(token.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.button_chatbox_send)
    public void onViewClicked() {
    }

    @OnClick({R.id.button_chatbox_send, R.id.button_chatbox_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_chatbox_send:
                if (edittextChatbox.getText().toString().equals("")) {
                    Toast message = Toast.makeText(getApplicationContext(), "No se ha escrito ningun mensaje", Toast.LENGTH_LONG);
                    message.show();
                } else {
                    Mensaje m = new Mensaje(usu.nombreUsuarioEmisor, usu.nombreUsuarioReceptor, edittextChatbox.getText().toString(), "mensaje", new Date());
                    m.setType("S");
                    mensajes.add(m);
                    messageList.setAdapter(adapter);
                    PostMensaje post = new PostMensaje();
                    post.setContexto(this);
                    post.setMsj(m);
                    post.execute("http://10.130.1.134:3000/mensajes?token=" + leerToken() + "&secreto=" + MainActivity.secreto);
                }
                break;
            case R.id.button_chatbox_refresh:
                GetMessage get = new GetMessage();
                get.setContexto(this);
                get.execute("http://10.130.1.134:3000/mensajes/all?token=" + leerToken() + "&secreto=" + MainActivity.secreto + "&remitente=" + Chat.usu.getNombreUsuarioEmisor() + "&receptor=" + Chat.usu.getNombreUsuarioReceptor());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(Chat.mensajes.size() > 0){
                            messageList.setAdapter(adapter);
                        }
                    }
                }, 2000);
                break;
        }
    }
}

class PostMensaje extends AsyncTask<String, Void, String> {


    public Mensaje msj = new Mensaje("", "", "", "", new Date());
    String res = "";
    String path;
    ProgressDialog progressDialog;
    public Context contexto;
    int code = 0;
    public boolean end = true;

    public void setMsj(Mensaje msj) {
        this.msj = msj;
    }

    public void setContexto(Context c) {
        contexto = c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(contexto);
        progressDialog.setMessage("Creando Usuario...");
        //progressDialog.show();
    }

    public String leerToken() {
        FileInputStream inputStream;
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        StringBuilder s = new StringBuilder();
        if (path.exists()) {
            try {
                inputStream = new FileInputStream(path);
                int val = 0;
                while ((val = inputStream.read()) != -1) {
                    s.append((char) val);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s.toString();
    }

    public void escribirToken(String token) {
        FileOutputStream outputStream;
        File path1 = contexto.getExternalFilesDir(null);
        File path = new File(path1, "tk.txt");
        if (path.exists()) {
            try {
                outputStream = new FileOutputStream(path);
                outputStream.write(token.getBytes());
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            path = strings[0];
            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;
            JSONObject dataToSend = new JSONObject();

            dataToSend.put("remitente", msj.getRemitente());
            dataToSend.put("receptor", msj.getReceptor());
            dataToSend.put("mensaje", msj.getMensaje());
            dataToSend.put("tipo", msj.getTipo());
            dataToSend.put("fecha", msj.getFecha());

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
            code = urlConnection.getResponseCode();

            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }
            res = result.toString();
            return res;

        } catch (IOException ex) {
            res = "Network error !";
            return "Network error !";
        } catch (JSONException ex) {
            res = "Data Invalid";
            return "Data Invalid !";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
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
            if (String.valueOf(code).contains("201")) {
                Gson gson = new Gson();
                Type listType = new TypeToken<Token>() {
                }.getType();
                Token t = gson.fromJson(s, listType);
                escribirToken(t.getToken());
                Toast message = Toast.makeText(contexto, "Mensaje enviado exitosamente", Toast.LENGTH_LONG);
                message.show();
            }
            if(String.valueOf(code).contains("401")){
                Toast message = Toast.makeText(contexto, "Token Invalido", Toast.LENGTH_LONG);
                message.show();
                eliminarArchivo();
                contexto.startActivity(new Intent(contexto.getApplicationContext(), MainActivity.class));
                Chat.fi.finish();
            }
        }
    }
}
class GetMessage extends AsyncTask<String, Void, String> {
    String res="";
    ResponseChat r;
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
            BufferedReader bufferedReader = null;
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
                Type listType = new TypeToken<ResponseChat>(){}.getType();
                r = gson.fromJson(res, listType);
                limpiarArchivo();
                escribirToken(r.getToken());
                Chat.t = leerToken();
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
        ArrayList<Mensaje> mensajes = new ArrayList<>();
        if (progressDialog != null) {
            progressDialog.dismiss();
            if (String.valueOf(code).contains("200") || (String.valueOf(code).contains("204"))) {
                end = true;
                Chat.mensajes.clear();
                if(r.docs.size() > 0){
                    mensajes.addAll(r.docs);
                }
                if(r.docs2.size() > 0){
                    mensajes.addAll(r.docs2);
                }
                if(mensajes.size() > 0){
                    Collections.sort(mensajes, new DateComparator());
                }
                Chat.mensajes.addAll(mensajes);
            }
            if(String.valueOf(code).contains("401")){
                Toast message = Toast.makeText(contexto, "Token Invalido", Toast.LENGTH_LONG);
                message.show();
                eliminarArchivo();
                contexto.startActivity(new Intent(contexto.getApplicationContext(), MainActivity.class));
                Chat.fi.finish();
            }
        }
    }
}

