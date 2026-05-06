package com.ecoscan.app.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ecoscan.app.R;
import com.ecoscan.app.data.EcoScanDatabase;

import java.util.Locale;

public class ProfileFragment extends Fragment {

    private EcoScanDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = EcoScanDatabase.getInstance(requireContext());
        TextView tvMoneySaved = view.findViewById(R.id.tv_profile_money_saved);
        TextView tvItemsTracked = view.findViewById(R.id.tv_profile_items_tracked);

        db.pantryDao().getTotalMoneySaved().observe(getViewLifecycleOwner(), totalSaved -> {
            if (totalSaved != null) {
                tvMoneySaved.setText(String.format(Locale.getDefault(), "$%.2f", totalSaved));
            } else {
                tvMoneySaved.setText("$0.00");
            }
        });

        db.pantryDao().getTotalItemsTracked().observe(getViewLifecycleOwner(), totalItems -> {
            if (totalItems != null) {
                tvItemsTracked.setText(String.valueOf(totalItems));
            } else {
                tvItemsTracked.setText("0");
            }
        });

        TextView tvFoodSaved = view.findViewById(R.id.tv_profile_food_saved);
        db.pantryDao().getConsumedItemsCount().observe(getViewLifecycleOwner(), consumedCount -> {
            if (consumedCount != null) {
                tvFoodSaved.setText(consumedCount + " items");
            } else {
                tvFoodSaved.setText("0 items");
            }
        });
    }
}