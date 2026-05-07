package com.ecoscan.app.ui.product;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ecoscan.app.R;
import com.ecoscan.app.data.EcoScanDatabase;
import com.ecoscan.app.data.Pantry.PantryItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductDetailActivity extends AppCompatActivity {

    private PantryItem item;
    private EcoScanDatabase db;
    private TextView tvExpiryDate;
    private TextView tvPrice;
    private ProgressBar progressFreshness;
    private TextView tvFreshnessLabel;
    private Chip statusDetailChip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = EcoScanDatabase.getInstance(this);
        item = getIntent().getParcelableExtra("item");

        TextView tvName       = findViewById(R.id.tv_detail_name);
        TextView tvBarcode    = findViewById(R.id.tv_detail_barcode);
        TextView tvDateAdded  = findViewById(R.id.tv_date_added);
        tvExpiryDate          = findViewById(R.id.tv_expiry_date);
        tvPrice               = findViewById(R.id.tv_price);
        statusDetailChip      = findViewById(R.id.chip_detail_status);
        progressFreshness     = findViewById(R.id.progress_freshness);
        tvFreshnessLabel      = findViewById(R.id.tv_freshness_label);

        if (item == null) {
            Toast.makeText(this, "Item not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Populate fields
        tvName.setText(item.itemName);
        tvBarcode.setText(item.barcode);
        tvDateAdded.setText(formatDate(item.dateAdded));
        tvExpiryDate.setText(formatDate(item.expiryDate));
        tvPrice.setText(String.format(Locale.getDefault(), "$%.2f", item.price));

        // Freshness
        calculateFreshness(item, progressFreshness, tvFreshnessLabel, statusDetailChip);

        // Mark as Consumed
        MaterialButton btnConsumed = findViewById(R.id.btn_mark_consumed);
        btnConsumed.setOnClickListener(v -> {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                item.isConsumed = true;
                db.pantryDao().update(item);
            });
            Toast.makeText(this, "Great! Money saved: $" + item.price, Toast.LENGTH_SHORT).show();
            finish();
        });

        // Delete
        MaterialButton btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(v -> {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> db.pantryDao().delete(item));
            Toast.makeText(this, "Product removed!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // Freshness Calculation
    private void calculateFreshness(PantryItem item, ProgressBar progressBar,
                                    TextView label, Chip statusChip) {
        long now               = System.currentTimeMillis();
        long totalDuration     = item.expiryDate - item.dateAdded;
        long remainingDuration = item.expiryDate - now;

        int percentage;
        if (now >= item.expiryDate) {
            percentage = 0;
        } else if (now <= item.dateAdded) {
            percentage = 100;
        } else {
            percentage = (int) ((remainingDuration * 100) / totalDuration);
        }

        progressBar.setProgress(percentage);

        if (percentage > 70) {
            label.setText("Fresh — " + percentage + "% fresh");
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#52B788")));
            statusChip.setText("Fresh");
            statusChip.setChipBackgroundColorResource(R.color.safe_green);
            statusChip.setTextColor(Color.WHITE);
        } else if (percentage > 30) {
            label.setText("Good — " + percentage + "% fresh");
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#F4A261")));
            statusChip.setText("Expiring Soon");
            statusChip.setChipBackgroundColorResource(R.color.warning_yellow);
            statusChip.setTextColor(Color.WHITE);
        } else if (percentage > 0) {
            label.setText("Use Soon — " + percentage + "% fresh");
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#E63946")));
            statusChip.setText("Critical");
            statusChip.setChipBackgroundColorResource(R.color.expired_red);
            statusChip.setTextColor(Color.WHITE);
        } else {
            label.setText("Expired — 0% fresh");
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#E63946")));
            statusChip.setText("Expired");
            statusChip.setChipBackgroundColorResource(R.color.expired_red);
            statusChip.setTextColor(Color.WHITE);
        }
    }

    private String formatDate(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(new Date(date));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
