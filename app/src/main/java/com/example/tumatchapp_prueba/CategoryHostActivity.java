package com.example.tumatchapp_prueba;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class CategoryHostActivity extends AppCompatActivity {
    public static final String EXTRA_CATEGORY = "categoria";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_host);

        Toolbar toolbar = findViewById(R.id.toolbarHost);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String categoria = getIntent().getStringExtra(EXTRA_CATEGORY);
        if (categoria == null) categoria = "";

        // Establecer título legible según la categoría
        String title = mapCategoryToDisplay(categoria);
        if (title != null && !title.isEmpty()) {
            toolbar.setTitle(title);
        }

        CategoriaListFragment fragment = CategoriaListFragment.newInstance(categoria);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.containerCategory, fragment);
        ft.commit();
    }

    private String mapCategoryToDisplay(String cat) {
        if (cat == null) return "Categoría";
        switch (cat.toLowerCase()) {
            case "electronics":
                return "Electrónica";
            case "jewelery":
            case "jewelry":
                return "Joyería";
            case "men's clothing":
                return "Ropa (Hombre)";
            case "women's clothing":
                return "Ropa (Mujer)";
            case "food":
                return "Alimentos";
            default:
                return "Categoría";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
