package com.example.tumatchapp_prueba;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;

public class HomeActivity extends AppCompatActivity {
    private EditText etQuery;
    private Button btnSearch;
    private Button btnBackHome;

    private MaterialCardView cardElectronics, cardClothingMen, cardClothingWomen, cardFood, cardJewelry;
    private String selectedCategory = ""; // empty = Todas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        etQuery = findViewById(R.id.etQuery);
        btnSearch = findViewById(R.id.btnSearchPrefs);
        btnBackHome = findViewById(R.id.btnBackHome);

        cardElectronics = findViewById(R.id.cardElectronics);
        cardClothingMen = findViewById(R.id.cardClothingMen);
        cardClothingWomen = findViewById(R.id.cardClothingWomen);
       //cardFood = findViewById(R.id.cardFood);
        cardJewelry = findViewById(R.id.cardJewelry);

        // Click listeners para las tarjetas: seleccionar categoría y marcar visualmente
        // Para la categoría "Electrónica" navegamos directamente a ResultsActivity con la categoría
        cardElectronics.setOnClickListener(v -> {
            // marcar visualmente
            selectCategory("electronics", cardElectronics);
            // iniciar ResultsActivity con la categoría 'electronics'
            Intent i = new Intent(this, ResultsActivity.class);
            i.putExtra("query", "");
            i.putExtra("category", "electronics");
            startActivity(i);
            // animación coherente con el resto
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        cardClothingMen.setOnClickListener(v -> {
            // marcar visualmente
            selectCategory("men's clothing", cardClothingMen);
            // iniciar ResultsActivity con la categoría
            Intent i = new Intent(this, ResultsActivity.class);
            i.putExtra("query", "");
            i.putExtra("category", "men's clothing");
            startActivity(i);
            // animación coherente con el resto
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        cardClothingWomen.setOnClickListener(v -> {
            // marcar visualmente
            selectCategory("women's clothing", cardClothingWomen);
            // iniciar ResultsActivity con la categoría
            Intent i = new Intent(this, ResultsActivity.class);
            i.putExtra("query", "");
            i.putExtra("category", "women's clothing");
            startActivity(i);
            // animación coherente con el resto
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        cardJewelry.setOnClickListener(v -> {
            // marcar visualmente
            selectCategory("jewelery", cardJewelry);
            // iniciar ResultsActivity con la categoría
            Intent i = new Intent(this, ResultsActivity.class);
            i.putExtra("query", "");
            i.putExtra("category", "jewelery");
            startActivity(i);
            // animación coherente con el resto
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });



        // Preselección si vienen desde ResultsActivity o ProductDetailActivity
        String pre = getIntent().getStringExtra("selected_category");
        if (pre != null && !pre.isEmpty()) {
            switch (pre.toLowerCase()) {
                case "electronics":
                    selectCategory("electronics", cardElectronics);
                    break;
                case "jewelery":
                case "jewelry":
                    selectCategory("jewelery", cardJewelry);
                    break;
                case "men's clothing":
                case "men's":
                    selectCategory("men's clothing", cardClothingMen);
                    break;
                case "women's clothing":
                case "women's":
                    selectCategory("women's clothing", cardClothingWomen);
                    break;

                default:
                    // no preselection
                    break;
            }
        }

        btnSearch.setOnClickListener(v -> {
            String q = etQuery.getText().toString().trim();
            String cat = selectedCategory;
            Intent i = new Intent(this, ResultsActivity.class);
            i.putExtra("query", q);
            // pasar categoría vacía si no seleccionada o si es 'food' (no existe en fakestore)
            i.putExtra("category", (cat == null || cat.isEmpty() || cat.equals("food")) ? "" : cat);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        btnBackHome.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
    }

    private void selectCategory(String category, MaterialCardView clicked) {
        selectedCategory = category;
        // reset styles
        int strokeColor = ContextCompat.getColor(this, R.color.primary);
        int defaultStroke = ContextCompat.getColor(this, R.color.secondary);
        cardElectronics.setStrokeColor(defaultStroke);
        cardClothingMen.setStrokeColor(defaultStroke);
        cardClothingWomen.setStrokeColor(defaultStroke);
       // cardFood.setStrokeColor(defaultStroke);
        cardJewelry.setStrokeColor(defaultStroke);

        // marcar la tarjeta seleccionada
        clicked.setStrokeColor(strokeColor);
        clicked.setCardBackgroundColor(ContextCompat.getColor(this, R.color.primaryContainer));

        // devolver las otras tarjetas al fondo blanco
        if (clicked != cardElectronics) cardElectronics.setCardBackgroundColor(ContextCompat.getColor(this, R.color.surface));
        if (clicked != cardClothingMen) cardClothingMen.setCardBackgroundColor(ContextCompat.getColor(this, R.color.surface));
        if (clicked != cardClothingWomen) cardClothingWomen.setCardBackgroundColor(ContextCompat.getColor(this, R.color.surface));
       // if (clicked != cardFood) cardFood.setCardBackgroundColor(ContextCompat.getColor(this, R.color.surface));
        if (clicked != cardJewelry) cardJewelry.setCardBackgroundColor(ContextCompat.getColor(this, R.color.surface));

        // animación de selección (scale up)
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        clicked.startAnimation(anim);
    }
}
