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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        EcoScanDatabase db = EcoScanDatabase.getInstance(this);
        PantryItem item = getIntent().getParcelableExtra("item");

        TextView tvName = findViewById(R.id.tv_detail_name);
        TextView tvBarcode = findViewById(R.id.tv_detail_barcode);
        TextView tvDateAdded = findViewById(R.id.tv_date_added);
        TextView tvExpiryDate = findViewById(R.id.tv_expiry_date);
        Chip statusDetailChip = findViewById(R.id.chip_detail_status);

        if (item == null) {
            Toast.makeText(this, "Item not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvName.setText(item.itemName);
        tvBarcode.setText(item.barcode);
        tvDateAdded.setText(formatDate(item.dateAdded));
        tvExpiryDate.setText(formatDate(item.expiryDate));

        // Freshness Calculation
        ProgressBar progressFreshness = findViewById(R.id.progress_freshness);
        TextView tvFreshnessLabel = findViewById(R.id.tv_freshness_label);
        calculateFreshness(item, progressFreshness, tvFreshnessLabel, statusDetailChip);

        // Delete button
        MaterialButton btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(v -> {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                db.pantryDao().delete(item);
            });

            Toast.makeText(this, "Product removed!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void calculateFreshness(PantryItem item, ProgressBar progressBar, TextView label, Chip statusChip) {
        long now = System.currentTimeMillis();
        long totalDuration = item.expiryDate - item.dateAdded;
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

        // Update UI based on freshness
        if (percentage > 70) {
            label.setText("Fresh — " + percentage + "% fresh");
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#52B788"))); // safe_green
            statusChip.setText("Fresh");
            statusChip.setChipBackgroundColorResource(R.color.safe_green);
            statusChip.setTextColor(Color.WHITE);
        } else if (percentage > 30) {
            label.setText("Good — " + percentage + "% fresh");
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#F4A261"))); // warning_yellow
            statusChip.setText("Expiring Soon");
            statusChip.setChipBackgroundColorResource(R.color.warning_yellow);
            statusChip.setTextColor(Color.WHITE);
        } else if (percentage > 0) {
            label.setText("Use Soon — " + percentage + "% fresh");
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#E63946"))); // expired_red
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

    private String formatDate(long date) { // 1112345
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(new Date(date));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}