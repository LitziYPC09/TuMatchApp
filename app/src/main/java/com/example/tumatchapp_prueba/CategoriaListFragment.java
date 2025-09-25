package com.example.tumatchapp_prueba;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CategoriaListFragment extends Fragment {
    private static final String ARG_CATEGORY = "categoria";

    private String categoria;
    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvEmpty;

    public static CategoriaListFragment newInstance(String categoria) {
        CategoriaListFragment f = new CategoriaListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, categoria);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoria = getArguments().getString(ARG_CATEGORY, "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_categoria_list, container, false);
        rvProducts = v.findViewById(R.id.rvProductsCat);
        progressBar = v.findViewById(R.id.progressBarCat);
        tvEmpty = v.findViewById(R.id.tvEmptyCat);

        rvProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ProductAdapter(requireContext());
        rvProducts.setAdapter(adapter);

        loadProducts();
        return v;
    }

    private void loadProducts() {
        // show progress
        if (getActivity() == null) return;
        requireActivity().runOnUiThread(() -> {
            progressBar.setVisibility(View.VISIBLE);
            rvProducts.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.GONE);
        });

        new Thread(() -> {
            try {
                String urlStr = "https://fakestoreapi.com/products";
                if (categoria != null && !categoria.isEmpty()) {
                    // use the fakestore category endpoint
                    urlStr = "https://fakestoreapi.com/products/category/" + java.net.URLEncoder.encode(categoria, "UTF-8");
                }
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                int code = conn.getResponseCode();
                if (code != 200) {
                    if (getActivity() != null) {
                        requireActivity().runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            tvEmpty.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "Error API: " + code, Toast.LENGTH_SHORT).show();
                        });
                    }
                    return;
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                br.close();
                conn.disconnect();

                JSONArray arr = new JSONArray(sb.toString());
                List<Product> products = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    int id = o.getInt("id");
                    String title = o.getString("title");
                    double price = o.getDouble("price");
                    String description = o.getString("description");
                    String cat = o.getString("category");
                    String image = o.getString("image");
                    products.add(new Product(id, title, price, description, cat, image));
                }

                if (getActivity() != null) {
                    requireActivity().runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        if (products.isEmpty()) {
                            rvProducts.setVisibility(View.GONE);
                            tvEmpty.setVisibility(View.VISIBLE);
                        } else {
                            tvEmpty.setVisibility(View.GONE);
                            rvProducts.setVisibility(View.VISIBLE);
                            adapter.submitList(products);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    requireActivity().runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Error al obtener productos", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }).start();
    }
}

