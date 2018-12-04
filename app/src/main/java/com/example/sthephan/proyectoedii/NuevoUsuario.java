package com.example.sthephan.proyectoedii;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NuevoUsuario extends AppCompatActivity {

    @BindView(R.id.txtContraseña2)
    EditText txtContrasena2;
    @BindView(R.id.txtContraseña)
    EditText txtContrasena;
    @BindView(R.id.txtUsuario)
    EditText txtUsuario;
    @BindView(R.id.txtCorreo)
    EditText txtCorreo;
    @BindView(R.id.txtApellido)
    EditText txtApellido;
    @BindView(R.id.txtNombre)
    EditText txtNombre;
    @BindView(R.id.scrollview)
    ScrollView scrollview;

    public static ArrayList<Usuario> users = new ArrayList<>();
    public boolean ready = false;
    public static GetAllUsuario get = new GetAllUsuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);
        ButterKnife.bind(this);
        if(get.getCode() != 0){
            get = new GetAllUsuario();
        }
        get.setContexto(this);
        get.execute("http://10.130.1.134:3000/users/allusers");
    }

    @OnClick(R.id.btnCrear)
    public void onViewClicked() {
        if(txtNombre.getText().toString().equals("") || txtApellido.getText().toString().equals("") || txtUsuario.getText().toString().equals("") || txtCorreo.getText().toString().equals("") || txtContrasena.getText().toString().equals("") || txtContrasena2.getText().toString().equals("")){
            Toast message = Toast.makeText(getApplicationContext(), "Algun campo se encuentra vacio", Toast.LENGTH_LONG);
            message.show();
        }
        else{
            if(txtContrasena.getText().toString().equals(txtContrasena2.getText().toString())){
                Usuario us = new Usuario(txtUsuario.getText().toString(), txtContrasena.getText().toString(), txtNombre.getText().toString(), txtApellido.getText().toString(), txtCorreo.getText().toString());
                PostUsuario post;
                switch(revisarUsuario()){
                    case "Clear":
                        post = new PostUsuario();
                        post.setContexto(this);
                        post.setUs(us);
                        post.execute("http://10.130.1.134:3000/users/register");
                        txtUsuario.setText("");
                        txtNombre.setText("");
                        txtApellido.setText("");
                        txtCorreo.setText("");
                        txtContrasena.setText("");
                        txtContrasena2.setText("");
                        get = new GetAllUsuario();
                        get.setContexto(this);
                        get.execute("http://10.130.1.134:3000/users/allusers");
                        break;
                    case "Found":
                        Toast message = Toast.makeText(getApplicationContext(), "Ya existe un usuario con el mismo nombre de usuario", Toast.LENGTH_LONG);
                        message.show();
                        break;
                    case "Empty":
                        post = new PostUsuario();
                        post.setContexto(this);
                        post.setUs(us);
                        post.execute("http://10.130.1.134:3000/users/register");
                        txtUsuario.setText("");
                        txtNombre.setText("");
                        txtApellido.setText("");
                        txtCorreo.setText("");
                        txtContrasena.setText("");
                        txtContrasena2.setText("");
                        get = new GetAllUsuario();
                        get.setContexto(this);
                        get.execute("http://10.130.1.134:3000/users/allusers");
                        break;
                    case "Running":
                        Toast message2 = Toast.makeText(getApplicationContext(), "Espere a que termine la carga de usuarios", Toast.LENGTH_LONG);
                        message2.show();
                        break;
                }
            }
            else{
                Toast message = Toast.makeText(getApplicationContext(), "La confirmacion de la contraseña no coincide con la contraseña ingresada", Toast.LENGTH_LONG);
                message.show();
            }
        }
    }

    public String revisarUsuario(){
        if(get.isEnd()){
            NuevoUsuario.users = get.getUsuarios();
            if(get.getUsuarios().size() == 0){
                return "Empty";
            }
            else{
                for(int i = 0; i < get.getUsuarios().size(); i++){
                    if(get.getUsuarios().get(i).getUsuario().equals(txtUsuario.getText().toString())){
                        return "Found";
                    }
                }
            }
        }
        else{
            return "Running";
        }
        return "Clear";
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                        startActivity(new Intent(NuevoUsuario.this, MainActivity.class));
                    }
                }).create().show();
    }
}

class PostUsuario extends AsyncTask<String, Void, String> {
    public Usuario us =new Usuario("","","","","");
    String res="";
    String path;
    ProgressDialog progressDialog;
    public Context contexto;
    int code = 0;
    public boolean end = true;

    public void setUs(Usuario us) {
        this.us = us;
    }

    public void setContexto(Context c){
        contexto=c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(contexto);
        progressDialog.setMessage("Creando Usuario...");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            path = strings[0];
            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;
            JSONObject dataToSend = new JSONObject();

            dataToSend.put("usuario", us.getUsuario());
            dataToSend.put("password", us.getPassword());
            dataToSend.put("nombre", us.getNombre());
            dataToSend.put("apellido", us.getApellido());
            dataToSend.put("correo", us.getCorreo());

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

            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }
            res = result.toString();
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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (progressDialog != null) {
            progressDialog.dismiss();
            if(String.valueOf(code).contains("201")){
                Toast message = Toast.makeText(contexto, "Usuario ingresado exitosamente", Toast.LENGTH_LONG);
                message.show();
            }
        }
    }
}
class GetAllUsuario extends AsyncTask<String, Void, String> {
    public ArrayList<Usuario> usuarios = new ArrayList<>();
    String res="";
    String path;
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

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setContexto(Context c){
        contexto=c;
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
            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }
            res = result.toString();
            path = strings[0];
            if(code == 204){
                return String.valueOf(code);
            }
            if(code == 200){
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Usuario>>(){}.getType();
                usuarios = gson.fromJson(res, listType);
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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (progressDialog != null) {
            progressDialog.dismiss();
            if (String.valueOf(code).contains("200") || (String.valueOf(code).contains("204"))) {
                end = true;
            }
        }
    }
}