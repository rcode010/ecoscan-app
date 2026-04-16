package com.ecoscan.app.ui.product;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ecoscan.app.R;
import com.google.android.material.button.MaterialButton;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Delete button — logic comes in Phase 2
        MaterialButton btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(v -> {
            Toast.makeText(this, "Product removed!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}