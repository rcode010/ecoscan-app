package com.ecoscan.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.ecoscan.app.data.PantryItem;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_pantry);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<PantryItem> items = getFakeData();
        PantryAdapter adapter = new PantryAdapter(items);
        recyclerView.setAdapter(adapter);
    }

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