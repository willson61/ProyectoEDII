package com.example.sthephan.proyectoedii;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String secreto = "noquierorepetirestructurasdedatos2";
    public static Uri file;
    public static String token;
    public static GetLogin log = new GetLogin();
    public static boolean loginVerified = false;
    public static Activity fa;

    @BindView(R.id.txtNombreUsuario)
    EditText txtNombreUsuario;
    @BindView(R.id.txtContraseña)
    EditText txtContrasena;
    @BindView(R.id.imgLogo)
    ImageView imgLogo;
    @BindView(R.id.scrollview)
    ScrollView scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fa = this;
        log = new GetLogin();
    }

    @OnClick({R.id.btnLogin, R.id.btnNuevoUsuario})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                if(txtNombreUsuario.getText().toString().equals("") || txtContrasena.getText().toString().equals("")){
                    Toast message = Toast.makeText(getApplicationContext(), "Por favor ingrese nombre de usuario y contraseña", Toast.LENGTH_LONG);
                    message.show();
                }
                else{
                    log.setContexto(this);
                    log.execute("http://10.130.1.134:3000/users/login?usuario="+txtNombreUsuario.getText().toString().trim()+"&password="+txtContrasena.getText().toString().trim()+"&secreto="+secreto);

                }
                break;
            case R.id.btnNuevoUsuario:
                finish();
                startActivity(new Intent(MainActivity.this, NuevoUsuario.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    public String obtenerNombreDeArchivoDeUri(Uri uri) {
        String displayName = "";
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null, null);
        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        } finally {
            cursor.close();
        }
        return displayName;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String name = obtenerNombreDeArchivoDeUri(data.getData());
        if (requestCode == 1234 && resultCode == RESULT_OK ) {
            try{
                Uri selectedFile = data.getData();
                if (name.contains(".txt")) {
                    file = selectedFile;
                    String selectedFilePath = FilePath.getPath(this, file);
                    File test = new File(selectedFilePath);
                    String[] charsetsToBeTested = {"UTF-8", "windows-1253", "ISO-8859-7"};

                    CharsetDetector cd = new CharsetDetector();
                    Charset charset = cd.detectCharset(test, charsetsToBeTested);
                    txtNombreUsuario.setText(charset.toString());
                    Toast message = Toast.makeText(getApplicationContext(), "Archivo seleccionado exitosamente", Toast.LENGTH_LONG);
                    message.show();
                } else {
                    Toast message = Toast.makeText(getApplicationContext(), "El archivo seleccionado no posee la extension .txt del contenido a cifrar. Por favor seleccione un archivo de extension .txt", Toast.LENGTH_LONG);
                    message.show();
                }
            }catch (Exception e){
                e.printStackTrace();
                Toast message = Toast.makeText(getApplicationContext(), "Error en la lectura de archivo", Toast.LENGTH_LONG);
                message.show();
            }
        }
    }
}

class GetLogin extends AsyncTask<String, Void, String> {
    public Usuario us =new Usuario("","","","","");
    String res="";
    String path;
    ProgressDialog progressDialog;
    public Context contexto;
    int code = 0;
    public boolean end = true;

    public void setContexto(Context c){
        contexto=c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(contexto);
        progressDialog.setMessage("Iniciando Secion...");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            path = strings[0];
            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;
            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(10000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");// set header
            urlConnection.connect();

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
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        FileOutputStream outputStream;
        File path1 = contexto.getExternalFilesDir(null);
        path1.mkdirs();
        File path = new File(path1, "tk.txt");
        if(!path.exists()){
            try {
                path.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (progressDialog != null) {
            progressDialog.dismiss();
            if(String.valueOf(code).contains("200")){
                MainActivity.token = s;
                Token t;
                Gson gson = new Gson();
                Type listType = new TypeToken<Token>(){}.getType();
                t = gson.fromJson(s, listType);
                try {
                    outputStream = new FileOutputStream(path);
                    outputStream.write(t.getToken().getBytes());
                    outputStream.close();
                    MainActivity.loginVerified = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast message = Toast.makeText(contexto, "Login exitoso", Toast.LENGTH_LONG);
                message.show();
                MainActivity.loginVerified = true;
                contexto.startActivity(new Intent(contexto.getApplicationContext(), ListaChats.class));
                MainActivity.fa.finish();
            }
            else if(String.valueOf(code).contains("204")){
                Gson gson = new Gson();
                Type listType = new TypeToken<String>(){}.getType();
                String mes = gson.fromJson(s, listType);
                Toast message = Toast.makeText(contexto, "Usuario o Contraseña no valido", Toast.LENGTH_LONG);
                message.show();
            }
        }
    }
}
