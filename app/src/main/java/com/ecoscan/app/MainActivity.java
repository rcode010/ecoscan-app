package com.ecoscan.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecoscan.app.data.PantryItem;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_pantry);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load fake data for UI demo
        List<PantryItem> itemList = getFakeData();

        PantryAdapter adapter = new PantryAdapter(itemList);
        recyclerView.setAdapter(adapter);

        // FAB → go to scan screen
        ExtendedFloatingActionButton fab = findViewById(R.id.fab_scan);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
            startActivity(intent);
        });
    }

    private List<PantryItem> getFakeData() {
        List<PantryItem> items = new ArrayList<>();

        long now = System.currentTimeMillis();
        long oneDay = 24 * 60 * 60 * 1000L;

        // Safe items
        items.add(new PantryItem("Whole Milk", "012345678901", now + 7 * oneDay));
        items.add(new PantryItem("Orange Juice", "098765432109", now + 10 * oneDay));
        items.add(new PantryItem("Greek Yogurt", "011223344556", now + 5 * oneDay));

        // Warning items (expiring soon)
        items.add(new PantryItem("Cheddar Cheese", "019283746501", now + 2 * oneDay));
        items.add(new PantryItem("Fresh Bread", "018273645501", now + 1 * oneDay));

        // Expired items
        items.add(new PantryItem("Strawberries", "017263545501", now - 1 * oneDay));

        return items;
    }
}