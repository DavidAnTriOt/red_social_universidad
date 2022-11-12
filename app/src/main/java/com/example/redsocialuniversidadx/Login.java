package com.example.redsocialuniversidadx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText CorreoLog, ContraLog;
    Button Ingresar;

    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null; //El titulo no debe ser nulo
        actionBar.setTitle("Login");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        CorreoLog = findViewById(R.id.CorreoLog);
        ContraLog = findViewById(R.id.ContraLog);
        Ingresar = findViewById(R.id.Ingresar);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = new ProgressBar(Login.this); //Inicialización del ProgressBar
        dialog = new Dialog(Login.this);  //Inicialización del cuadro de dialogo

        //Asignacion de evento al boton INGRESAR
        Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Conversion del correo y contraseña a String
                String correo = CorreoLog.getText().toString();
                String contra = ContraLog.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    CorreoLog.setError("Correo no valido");
                    CorreoLog.setFocusable(true);
                } else if (contra.length() < 6) {
                    ContraLog.setError("La contraseña debe tener minimo 6 caracteres");
                    ContraLog.setFocusable(true);
                }else {
                    //Ejecucion del metodo
                    LOGINUSUARIO(correo, contra);
                }
            }
        });
    }


    //Metodo para validar usuario
    private void LOGINUSUARIO(String correo, String contra) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.showContextMenu();
        firebaseAuth.signInWithEmailAndPassword(correo, contra)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Si se inicia sesion correctamente
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.GONE); //el progreso se cierra, oculta
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            //al iniciar sesion nos mandara a la actividad Inicio
                            startActivity(new Intent(Login.this, Inicio.class));
                            assert user != null; //para que el usuario no sea nulo
                            Toast.makeText(Login.this, "Bienvenido(a)"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();

                        }else {
                            progressBar.setVisibility(View.VISIBLE);
                            Toast.makeText(Login.this,"A ingresado sus datos incorrectamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.VISIBLE);
                        //REEMPLAZO DE MENSAJE DE ERROR
                        Dialog_error_sesion();
                        //para que muestre el mensaje
                        //Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Creacion del Dialogo personalizado
    private void Dialog_error_sesion(){
        Button Ok_ErrorSesion;
        dialog.setContentView(R.layout.error_sesion); //Conexion con la vista creada
        Ok_ErrorSesion = dialog.findViewById(R.id.Ok_ErrorSesion);

        //Cerrar cuadro de dialogo al oprimir el boton
        Ok_ErrorSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); //Cerrar cuadro de dialogo
            }
        });

        dialog.setCancelable(false); //Para que no se cierre si no presionan un area que no sea el boton D:
        dialog.show();

    }

    //Habilitación de boton para retroceder(actividad anterior)
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    //Mensaje personalizado cuando el usuario no pueda iniciar sesion


}