package com.example.tumatchapp_prueba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class ProductDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Habilitar flecha de retroceso
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        ImageView img = findViewById(R.id.imgProduct);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvPrice = findViewById(R.id.tvPrice);
        TextView tvCategory = findViewById(R.id.tvCategory);
        TextView tvDesc = findViewById(R.id.tvDesc);

        String title = getIntent().getStringExtra("title");
        double price = getIntent().getDoubleExtra("price", 0);
        String desc = getIntent().getStringExtra("description");
        String category = getIntent().getStringExtra("category");
        String image = getIntent().getStringExtra("image");

        if (title != null) {
            tvTitle.setText(title);
            toolbar.setTitle(title);
        }
        tvPrice.setText(String.format("$%.2f", price));
        tvCategory.setText(category);
        tvDesc.setText(desc);
        // Glide para cargar la imagen
        Glide.with(this)
                .load(image)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(img);

        // Botón para volver a categorías
        MaterialButton btnBackCategories = findViewById(R.id.btnBackCategories);
        if (btnBackCategories != null) {
            btnBackCategories.setOnClickListener(v -> {
                Intent i = new Intent(ProductDetailActivity.this, HomeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            });
        }

    }
}
