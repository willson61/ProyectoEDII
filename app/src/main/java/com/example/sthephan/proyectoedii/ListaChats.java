package com.example.sthephan.proyectoedii;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListaChats extends AppCompatActivity {

    public static ArrayList<UsuarioItem> ListaChat = new ArrayList<>();
    public AdapterChat adapter = new AdapterChat(this, ListaChat);
    @BindView(R.id.lstChats)
    ListView lstChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_chats);
        ButterKnife.bind(this);
        MainActivity.fa.finish();
        lstChats.setAdapter(adapter);
        lstChats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(4000);
                view.startAnimation(animation1);
            }
        });
        //registerForContextMenu(lstChats);
    }

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.lstChats) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle("Opciones");
            menu.add(Menu.NONE, 1, 1, "Borrar");
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.menu);
        String menuItemName = menuItems[menuItemIndex];
        String listItemName = Countries[info.position];

        TextView text = (TextView)findViewById(R.id.footer);
        text.setText(String.format("Selected %s for item %s", menuItemName, listItemName));
        return true;
    }*/
}

class GetAllUsuarioChats extends AsyncTask<String, Void, String> {
    public ArrayList<Usuario> usuarios = new ArrayList<>();
    String res="";
    String path;
    ProgressDialog progressDialog;
    public Context contexto;
    public boolean end = false;
    int code = 0;

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
