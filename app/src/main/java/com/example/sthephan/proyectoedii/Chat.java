package com.example.sthephan.proyectoedii;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Chat extends AppCompatActivity {

    public static UsuarioItem usu;
    @BindView(R.id.edittext_chatbox)
    EditText edittextChatbox;
    @BindView(R.id.layout_chatbox)
    LinearLayout layoutChatbox;

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        editText = (EditText) findViewById(R.id.edittext_chatbox);
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

            dataToSend.put("remitente", msj.getRemitente());
            dataToSend.put("password", msj.getReceptor());
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
            res = "Network error !";
            return "Network error !";
        } catch (JSONException ex) {
            res = "Data Invalid";
            return "Data Invalid !";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (progressDialog != null) {
            progressDialog.dismiss();
            if (String.valueOf(code).contains("201")) {
                Toast message = Toast.makeText(contexto, "Mensaje enviado exitosamente", Toast.LENGTH_LONG);
                message.show();
            }
        }
    }
}

