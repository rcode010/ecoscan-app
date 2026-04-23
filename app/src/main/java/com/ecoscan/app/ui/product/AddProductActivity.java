package com.ecoscan.app.ui.product;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.ecoscan.app.R;
import com.ecoscan.app.data.EcoScanDatabase;
import com.ecoscan.app.data.Pantry.PantryItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddProductActivity extends AppCompatActivity {

    private TextInputEditText etProductName, etBarcode, etExpiryDate;
    EcoScanDatabase db;
    private long selectedExpiryDate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        db = EcoScanDatabase.getInstance(this);
        etProductName = findViewById(R.id.et_product_name);
        etBarcode = findViewById(R.id.et_barcode);
        etExpiryDate = findViewById(R.id.et_expiry_date);

        // Back button
        findViewById(R.id.toolbar);
        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Date picker on expiry field click
        etExpiryDate.setOnClickListener(v -> showDatePicker());

        // Add button
        MaterialButton btnAdd = findViewById(R.id.btn_add_product);
        btnAdd.setOnClickListener(v -> {
            String name = etProductName.getText().toString().trim();
            String barcode = etBarcode.getText().toString().trim();

            if (name.isEmpty()) {
                etProductName.setError("Enter product name");
                return;
            }
            if (selectedExpiryDate == 0) {
                etExpiryDate.setError("Select expiry date");
                return;
            }

            /*
            * Executor is a thread used to run database queries as mentioned in MainActivity.java
            * We always run database queries on a different thread than the main thread to avoid crashing the app.
            * */
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                PantryItem newItem = new PantryItem(name, "", selectedExpiryDate);
                db.pantryDao().insert(newItem);
            });

            Toast.makeText(this, "✅ " + name + " added to pantry!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(this, (view, y, m, d) -> {
            selectedExpiryDate = getTimestamp(y, m, d);
            etExpiryDate.setText(d + " / " + (m + 1) + " / " + y);
        }, year, month, day);

        // Only allow future dates
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        datePicker.show();
    }

    private long getTimestamp(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, 0, 0, 0);
        return cal.getTimeInMillis();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}