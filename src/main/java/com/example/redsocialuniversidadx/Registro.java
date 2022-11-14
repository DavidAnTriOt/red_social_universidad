package com.example.redsocialuniversidadx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {

    EditText nombres, apellidos, edad, telefono, carrera, correo, contraseña;
    Button RegistrarUsuario;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Creación de actionBar, asignación de titulo y habilitación de botón de retroceso
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null; //El titulo no debe ser nulo
        actionBar.setTitle("Registro");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        nombres = findViewById(R.id.nombres);
        apellidos = findViewById(R.id.apellidos);
        edad = findViewById(R.id.edad);
        telefono = findViewById(R.id.telefono);
        carrera = findViewById(R.id.carrera);
        correo = findViewById(R.id.correo);
        contraseña = findViewById(R.id.contraseña);
        RegistrarUsuario = findViewById(R.id.RegistrarUsuario);

        //Creaacion de instancia de Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //Creacion de evento al boton RegistrarUsuario
        RegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Correo = correo.getText().toString(); //Se convierte a String
                String Contra = contraseña.getText().toString(); //Se convierte a String

                //Validacion de correo y contraseña segun las reglas de Firebase: llevar @ y mayor o igual a 6 carateres
                if(!Patterns.EMAIL_ADDRESS.matcher(Correo).matches()){
                    correo.setError("Correo invalido");
                    correo.setFocusable(true);
                }else if(Contra.length()<6){
                    contraseña.setError("La contraseña debe contener minimo 6 digitos");
                    contraseña.setFocusable(true);
                }else{
                    Registrar(Correo, Contra);
                }
            }
        });
    }

    //Metodo de registro de un usuario
    private void Registrar(String Correo, String Contra) {
        firebaseAuth.createUserWithEmailAndPassword(Correo, Contra)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Si se registra exitosamente
                        if(task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            //Datos para registrar
                            //Obtencion de UID
                            assert user != null; //Para que el usuario no sea valor nulo
                            String uid = user.getUid();
                            String Nombres = nombres.getText().toString();
                            String Apellidos = apellidos.getText().toString();
                            String Edad = edad.getText().toString();
                            String Telefono = telefono.getText().toString();
                            String Carrera = carrera.getText().toString();
                            String Correo = correo.getText().toString();
                            String Contra = contraseña.getText().toString();

                            //HASHMAP para mandar los datos a Firebase
                            HashMap<Object, String> DatosUsuario = new HashMap<>();

                            DatosUsuario.put("uid", uid);
                            DatosUsuario.put("Nombres", Nombres);
                            DatosUsuario.put("Apellidos", Apellidos);
                            DatosUsuario.put("Edad", Edad);
                            DatosUsuario.put("Telefono", Telefono);
                            DatosUsuario.put("Carrera", Carrera);
                            DatosUsuario.put("Correo", Correo);
                            DatosUsuario.put("Contra", Contra);
                            //IMAGEN
                            DatosUsuario.put("imagen", "");

                            //Instanciamos a la base de datos de Firebase
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            //Creacion de la base de datos
                            DatabaseReference reference = database.getReference("USUARIOS_RED_SOCIAL"); //Nombre de base de datos no relacional
                            reference.child(uid).setValue(DatosUsuario);
                            Toast.makeText(Registro.this, "Se a registrado exitosamente :D", Toast.LENGTH_SHORT).show();
                            //El registro exitoso nos dirige a la actividad Inicio
                            startActivity(new Intent(Registro.this,Inicio.class));
                        }else{
                            Toast.makeText(Registro.this, "Algo salió mal D:", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registro.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Habilitación de boton para retroceder(actividad anterior)
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}