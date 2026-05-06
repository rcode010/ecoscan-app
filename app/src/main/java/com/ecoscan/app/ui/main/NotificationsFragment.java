package com.ecoscan.app.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecoscan.app.R;
import com.ecoscan.app.data.EcoScanDatabase;
import com.ecoscan.app.ui.product.PantryAdapter;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private EcoScanDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Database
        db = EcoScanDatabase.getInstance(requireContext());

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        PantryAdapter adapter = new PantryAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Calculate time for "soon" (2 days from now)
        long twoDaysFromNow = System.currentTimeMillis() + (2 * 24 * 60 * 60 * 1000);

        // Fetch and observe items expiring soon
        db.pantryDao().getItemsExpiringSoon(twoDaysFromNow).observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                adapter.updateItems(items);
            }
        });
    }
}