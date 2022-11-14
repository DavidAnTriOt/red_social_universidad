package com.example.redsocialuniversidadx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Inicio extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference BASE_DE_DATOS;

    ImageView foto_usuario;
    TextView uidPerfil, nombresPerfil, apellidosPerfil, edadPerfil, telefonoPerfil, carreraPerfil, correoPerfil;

    Button CerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null; //El titulo no debe ser nulo
        actionBar.setTitle("Bienvenido a La Red Social");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance(); //Incializa la instancia para el database
        BASE_DE_DATOS = firebaseDatabase.getReference("USUARIOS_RED_SOCIAL");

        foto_usuario = findViewById(R.id.foto_usuario);
        uidPerfil = findViewById(R.id.uidPerfil);
        nombresPerfil = findViewById(R.id.nombresPerfil);
        apellidosPerfil = findViewById(R.id.apellidosPerfil);
        edadPerfil = findViewById(R.id.edadPerfil);
        telefonoPerfil = findViewById(R.id.telefonoPerfil);
        carreraPerfil = findViewById(R.id.carreraPerfil);
        correoPerfil = findViewById(R.id.correoPerfil);

        CerrarSesion = findViewById(R.id.CerrarSesion);

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creacion de metodo para cerrar sesion
                CerrarSesion();
            }
        });
    }

    //Llamada a onStart
    //Se reemplaza la actividad PantallaDeCarga por la actividad Inicio

    @Override
    protected void onStart() {
        //LLamada al metodo para que se ejecute al iniciar la actividad
        VerificacionInicioSesion();
        super.onStart();
    }

    //METODO PARA VERIFICAR SI EL USUARIO YA SE A LOGUEADO PREVIAMENTE
    private void VerificacionInicioSesion(){
        //Si el usuario a iniciado sesion entonces redirigirá a esta actividad
        if (firebaseUser != null) {
            CargarDatos();
            Toast.makeText(this, "Se ha iniciado sesión", Toast.LENGTH_SHORT).show();
        }//Si no a iniciado sesion dirigirá al MainActivity
        else{
            startActivity(new Intent(Inicio.this, MainActivity.class));
            finish();
        }
    }

    //Metodo para recuperar los datos del usuario desde Firebase
    private void CargarDatos(){
        Query query = BASE_DE_DATOS.orderByChild("correo").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Recorre los usuarios registrados en la BD hasta que encuentre el usuario en uso
                for (DataSnapshot ds : snapshot.getChildren()){
                    //Obteniendo los valores
                    String uid = ""+ds.child("uid").getValue();
                    String nombres = ""+ds.child("Nombres").getValue();
                    String apellidos = ""+ds.child("Apellidos").getValue();
                    String edad = ""+ds.child("Edad").getValue();
                    String telefono = ""+ds.child("Telefono").getValue();
                    String carrera = ""+ds.child("Carrera").getValue();
                    String correo = ""+ds.child("Correo").getValue();
                    String imagen = ""+ds.child("imagen").getValue();

                    //Modificacion(SET) de los datos en las vistas
                    uidPerfil.setText(uid);
                    nombresPerfil.setText(nombres);
                    apellidosPerfil.setText(apellidos);
                    edadPerfil.setText(edad);
                    telefonoPerfil.setText(telefono);
                    carreraPerfil.setText(carrera);
                    correoPerfil.setText(correo);

                    //Declaracion de Try Catch para gestionar la foto de perfil y evitar errores
                    try {
                        //Si la imagen del usuario existe en la base de datos
                        Picasso.get().load(imagen).placeholder(R.drawable.img_usuario).into(foto_usuario);
                    }catch (Exception e){
                        //Si el usuario no cuenta con una imagen en la base de datos
                        Picasso.get().load(R.drawable.img_usuario).into(foto_usuario);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Metodo para cerrar sesion
    private void CerrarSesion(){
        firebaseAuth.signOut(); //Cierra la sesion del usuario actual
        Toast.makeText(this, "Ha cerrado su sesión", Toast.LENGTH_SHORT).show();
        //Luego de cerrar la sesion se dirige al mainActivity
        startActivity(new Intent(Inicio.this, MainActivity.class));
    }
}