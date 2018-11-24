package com.example.sthephan.proyectoedii;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    public static Activity fi;
    public static  VerifyToken verify = new VerifyToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        fi = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                verify.setContexto(fi);
                String token = leerToken();
                if(token != ""){
                    verify.setToken(token);
                    verify.execute("http://10.130.1.134:3000/users/verify");
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2500);
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
}

class VerifyToken extends AsyncTask<String, Void, String> {
    public  String token;
    String res="";
    String path;
    ProgressDialog progressDialog;
    public Context contexto;
    int code = 0;
    public boolean end = true;

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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (progressDialog != null) {
            progressDialog.dismiss();
            if(String.valueOf(code).contains("200")){
                Gson gson = new Gson();
                Type listType = new TypeToken<Token>(){}.getType();
                Token t = gson.fromJson(s, listType);
                escribirToken(t.getToken());
                Toast message = Toast.makeText(contexto, "Login Exitoso!", Toast.LENGTH_LONG);
                message.show();
                SplashActivity.fi.finish();
                contexto.startActivity(new Intent(contexto.getApplicationContext(), ListaChats.class));
            }
            if(String.valueOf(code).contains("401")){
                Toast message = Toast.makeText(contexto, "Token Invalido", Toast.LENGTH_LONG);
                message.show();
                SplashActivity.fi.finish();
                contexto.startActivity(new Intent(contexto.getApplicationContext(), MainActivity.class));
            }
        }
    }
}
