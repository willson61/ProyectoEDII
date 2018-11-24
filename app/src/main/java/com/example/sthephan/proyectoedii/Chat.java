package com.example.sthephan.proyectoedii;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Chat extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
