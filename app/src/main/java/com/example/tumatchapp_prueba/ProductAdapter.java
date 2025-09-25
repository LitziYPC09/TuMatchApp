package com.example.tumatchapp_prueba;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class ProductAdapter extends ListAdapter<Product, ProductAdapter.ViewHolder> {
    private final Context ctx;

    public ProductAdapter(Context ctx) {
        super(DIFF_CALLBACK);
        this.ctx = ctx;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvTitle, tvPrice, tvDesc;
        MaterialButton btnViewMore;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgProduct);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            btnViewMore = itemView.findViewById(R.id.btnViewMore);
        }
    }

    private static final DiffUtil.ItemCallback<Product> DIFF_CALLBACK = new DiffUtil.ItemCallback<Product>() {
        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            // crude but sufficient for now
            return oldItem.title.equals(newItem.title)
                    && Double.compare(oldItem.price, newItem.price) == 0
                    && oldItem.description.equals(newItem.description)
                    && oldItem.image.equals(newItem.image);
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_productos, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = getItem(position);
        holder.tvTitle.setText(p.title);
        holder.tvPrice.setText(String.format(Locale.getDefault(), "$%.2f", p.price));
        holder.tvDesc.setText(p.description.length() > 120 ? p.description.substring(0, 120) + "..." : p.description);
        // Usar Glide para carga de imágenes con placeholder
        Glide.with(ctx)
                .load(p.image)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.img);

        // Usar el botón "Ver más" para abrir el detalle
        holder.btnViewMore.setOnClickListener(v -> {
            Intent i = new Intent(ctx, ProductDetailActivity.class);
            i.putExtra("id", p.id);
            i.putExtra("title", p.title);
            i.putExtra("price", p.price);
            i.putExtra("description", p.description);
            i.putExtra("category", p.category);
            i.putExtra("image", p.image);
            ctx.startActivity(i);
            // Añadir transición después de startActivity si el contexto es Activity
            if (ctx instanceof Activity) {
                ((Activity) ctx).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // También permitir click en la tarjeta entera (opcional)
        holder.itemView.setOnClickListener(v -> holder.btnViewMore.performClick());
    }
}
