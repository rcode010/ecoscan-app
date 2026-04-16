package com.ecoscan.app.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import com.ecoscan.app.ui.product.PantryAdapter;
import com.ecoscan.app.R;
import com.ecoscan.app.data.Pantry.PantryItem;

public class HomeFragment extends Fragment {

    // onCreateView creates the UI (home fragment) | converting the XML to View objects
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    // onViewCreated uses the UI that was created by onCreateView to populate it with data
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_pantry);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<PantryItem> items = getFakeData();
        PantryAdapter adapter = new PantryAdapter(items);
        recyclerView.setAdapter(adapter);
    }

    // Helper method to generate dummy data
    private List<PantryItem> getFakeData() {
        List<PantryItem> items = new ArrayList<>();
        long now = System.currentTimeMillis();
        long oneDay = 24 * 60 * 60 * 1000L;

        items.add(new PantryItem("Whole Milk", "012345678901", now + 7 * oneDay));
        items.add(new PantryItem("Orange Juice", "098765432109", now + 10 * oneDay));
        items.add(new PantryItem("Greek Yogurt", "011223344556", now + 5 * oneDay));
        items.add(new PantryItem("Cheddar Cheese", "019283746501", now + 2 * oneDay));
        items.add(new PantryItem("Fresh Bread", "018273645501", now + 1 * oneDay));
        items.add(new PantryItem("Strawberries", "017263545501", now - 1 * oneDay));

        return items;
    }
}