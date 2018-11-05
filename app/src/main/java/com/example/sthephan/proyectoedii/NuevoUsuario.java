package com.example.sthephan.proyectoedii;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ScrollView;
import android.app.AlertDialog;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnCrear)
    public void onViewClicked() {
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
