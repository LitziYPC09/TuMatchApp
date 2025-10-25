package com.example.tumatchapp_prueba;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etPasswordConfirm;
    private Button btnRegister;
    private TextView tvLogin;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        session = new SessionManager(this);
      // enviarDatosAN8n enviarDatosN8n = new enviarDatosAN8n();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString();
            String pass2 = etPasswordConfirm.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(pass2)) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pass.equals(pass2)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean ok = session.register(email, pass);
            if (ok) {
                Intent i = new Intent(this, MainActivity.class);
                enviarDatosAN8n(email);

                startActivity(i);
                finish();
            } else {
                Toast.makeText(this, "El usuario ya existe o hubo un error", Toast.LENGTH_SHORT).show();
            }
        });

        tvLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
    }

    public void enviarDatosAN8n(String mensaje) {
        // 1. Configurar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                // Pega aquí la URL BASE de tu instancia de n8n (SIN la ruta)
                .baseUrl("https://primary-production-0087.up.railway.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 2. Crear una instancia del servicio
        N8nApiService apiService = retrofit.create(N8nApiService.class);

        // 3. Crear el objeto de datos
        DatosParaN8n datosParaEnviar = new DatosParaN8n(mensaje);

        // 4. Realizar la llamada de red asíncrona
        apiService.enviarDatos(datosParaEnviar).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("N8N_INTEGRATION", "Datos enviados a n8n con éxito.");
                } else {
                    Log.e("N8N_INTEGRATION", "Error al enviar datos: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("N8N_INTEGRATION", "Fallo de red: " + t.getMessage());
            }
        });
    }
}

