package com.example.tumatchapp_prueba;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
// Quitamos URLEncoder ya que no se usará si la API acepta la URL tal cual
// import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {
    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private String query;
    private String category;
    private ProgressBar progressBar;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        rvProducts = findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this);
        rvProducts.setAdapter(adapter);

        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);

        // Dentro del método onCreate() de ResultsActivity.java

        // Nuevo botón para volver a categorías
        findViewById(R.id.btnBackCategories).setOnClickListener(v -> {
            Intent i = new Intent(this, HomeActivity.class);
            // pasar la categoría actual para que quede preseleccionada
           // if (category != null) i.putExtra(CategoryHostActivity.EXTRA_CATEGORY, category);
            // No limpiar el stack para que el usuario pueda volver atrás si desea
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish(); // Cierra ResultsActivity
        });

        query = getIntent().getStringExtra("query");
        category = getIntent().getStringExtra("category");

        loadAndShowProducts();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void loadAndShowProducts() {
        System.out.println("DEBUG: Iniciando carga de productos...");
        System.out.println("DEBUG: Categoría recibida: " + category);

        if (!isNetworkAvailable()) {
            handleError("No hay conexión a Internet. Por favor, verifica tu conexión y vuelve a intentarlo.");
            return;
        }

        runOnUiThread(() -> {
            progressBar.setVisibility(View.VISIBLE);
            rvProducts.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.GONE);
        });

        new Thread(() -> {
            HttpURLConnection conn = null;
            BufferedReader br = null;
            try {
                String baseUrl = "https://fakestoreapi.com";
                String urlStr = baseUrl + "/products";
                if (category != null && !category.isEmpty()) {
                    String normalizedCategory = category.toLowerCase().trim();
                    System.out.println("DEBUG: Categoría normalizada: " + normalizedCategory);

                    // Mapear las categorías en español a inglés
                    switch (normalizedCategory) {
                        case "electrónica":
                        case "electronica":
                        case "Electrónica":
                        case "Electronica":
                            normalizedCategory = "electronics";
                            break;
                        case "joyería":
                        case "joyeria":
                            normalizedCategory = "jewelery";
                            break;
                        case "ropa hombre":
                            normalizedCategory = "men's clothing";
                            break;
                        case "ropa mujer":
                            normalizedCategory = "women's clothing";
                            break;
                    }

                    System.out.println("DEBUG: Categoría traducida: " + normalizedCategory);
                    // Usar la categoría normalizada directamente si la API la acepta sin codificar
                    urlStr = baseUrl + "/products/category/" + normalizedCategory;
                }

                System.out.println("DEBUG: Intentando conectar a: " + urlStr);

                // Verificar conectividad antes de hacer la petición
                if (!isNetworkAvailable()) {
                    throw new IOException("No hay conexión a Internet disponible");
                }

                URL url = new URL(urlStr);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");

                // Intentar resolver el hostname manualmente
                System.out.println("DEBUG: Resolviendo hostname: " + url.getHost());
                java.net.InetAddress address = java.net.InetAddress.getByName(url.getHost());
                System.out.println("DEBUG: IP Address: " + address.getHostAddress());

                int responseCode = conn.getResponseCode();
                System.out.println("DEBUG: Código de respuesta HTTP: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    br = new BufferedReader(new InputStreamReader(
                            conn.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }

                    String jsonResponse = response.toString();
                    System.out.println("DEBUG: Respuesta JSON recibida: " +
                            jsonResponse.substring(0, Math.min(100, jsonResponse.length())) + "...");

                    processJsonResponse(jsonResponse);
                } else {
                    handleErrorResponse(conn, responseCode);
                }
            } catch (java.net.UnknownHostException e) {
                System.out.println("DEBUG: Error al resolver el hostname: " + e.getMessage());
                handleError("No se puede conectar al servidor. Por favor, verifica tu conexión a Internet.");
            } catch (java.net.SocketTimeoutException e) {
                System.out.println("DEBUG: Timeout en la conexión: " + e.getMessage());
                handleError("La conexión está muy lenta. Por favor, inténtalo de nuevo.");
            } catch (IOException e) {
                System.out.println("DEBUG: Error de red: " + e.getMessage());
                handleError("Error de conexión: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("DEBUG: Error general: " + e.getClass().getName() + " - " + e.getMessage());
                e.printStackTrace();
                handleError("Error inesperado: " + e.getMessage());
            } finally {
                closeResources(br, conn);
            }
        }).start();
    }

    private void processJsonResponse(String jsonResponse) throws JSONException {
        JSONArray arr = new JSONArray(jsonResponse);
        System.out.println("DEBUG: Productos recibidos: " + arr.length());

        List<Product> products = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject o = arr.getJSONObject(i);
                Product p = new Product(
                    o.getInt("id"),
                    o.getString("title"),
                    o.getDouble("price"),
                    o.getString("description"),
                    o.getString("category"),
                    o.getString("image")
                );
                if (matchesQuery(p, query)) {
                    products.add(p);
                    System.out.println("DEBUG: Agregado producto: " + p.title);
                }
            } catch (JSONException je) {
                System.out.println("DEBUG: Error al procesar producto individual: " + je.getMessage());
            }
        }

        updateUI(products);
    }

    private void handleErrorResponse(HttpURLConnection conn, int responseCode) {
        try (BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {

            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorResponse.append(line);
            }

            System.out.println("DEBUG: Error response: " + errorResponse);
            handleError("Error " + responseCode + " - " + errorResponse);

        } catch (IOException e) {
            System.out.println("DEBUG: No se pudo leer el mensaje de error: " + e.getMessage());
            handleError("Error " + responseCode);
        }
    }

    private void handleError(String errorMessage) {
        runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText("Error al cargar productos:\n" + errorMessage);
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        });
    }

    private void updateUI(List<Product> products) {
        runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            if (products.isEmpty()) {
                rvProducts.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText("No se encontraron productos para esta categoría");
            } else {
                tvEmpty.setVisibility(View.GONE);
                rvProducts.setVisibility(View.VISIBLE);
                adapter.submitList(products);
            }
        });
    }

    private void closeResources(BufferedReader br, HttpURLConnection conn) {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            conn.disconnect();
        }
    }

    private boolean matchesQuery(Product p, String q) {
        if (q == null || q.isEmpty()) {
            return true; // Si no hay consulta, coinciden todos los productos
        }
        String lowerCaseQuery = q.toLowerCase().trim(); // Consulta del usuario en minúsculas

        // 1. Comprobar si la consulta coincide con el título o la descripción del producto
        if (p.title.toLowerCase().contains(lowerCaseQuery) ||
            p.description.toLowerCase().contains(lowerCaseQuery)) {
            return true;
        }

        // 2. Comprobar si la consulta coincide directamente con la categoría en inglés del producto
        // (Ej: el usuario escribe "electronics")
        if (p.category.toLowerCase().contains(lowerCaseQuery)) {
            return true;
        }

        // 3. Comprobar si la consulta en español coincide con una categoría mapeada
        // y luego comparar con la categoría en inglés del producto.
        String targetEnglishCategory = null;
        switch (lowerCaseQuery) {
            case "electrónica":
            case "electronica":
                targetEnglishCategory = "electronics";
                break;
            case "joyería":
            case "joyeria":
                targetEnglishCategory = "jewelery";
                break;
            case "ropa hombre":
                targetEnglishCategory = "men's clothing";
                break;
            case "ropa mujer":
                targetEnglishCategory = "women's clothing";
                break;
            // Puedes añadir más mapeos si es necesario
        }

        if (targetEnglishCategory != null) {
            // Compara la categoría del producto (que está en inglés) con el mapeo en inglés.
            // Es importante usar .equals() para una coincidencia exacta de categoría.
            if (p.category.toLowerCase().equals(targetEnglishCategory)) {
                return true;
            }
        }

        return false; // No se encontró coincidencia
    }
}
