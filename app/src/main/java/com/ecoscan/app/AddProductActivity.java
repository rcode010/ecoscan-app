package com.ecoscan.app;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;

public class AddProductActivity extends AppCompatActivity {

    private TextInputEditText etProductName, etBarcode, etExpiryDate;
    private long selectedExpiryDate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

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

            // Success for now (database integration comes later)
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