package com.example.sthephan.proyectoedii;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

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
    }

    @OnClick({R.id.btnLogin, R.id.btnNuevoUsuario})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
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
}
