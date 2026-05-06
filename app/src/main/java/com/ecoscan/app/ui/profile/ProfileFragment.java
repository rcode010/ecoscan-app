package com.ecoscan.app.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.ecoscan.app.R;
import com.ecoscan.app.data.EcoScanDatabase;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Locale;

public class ProfileFragment extends Fragment {

    private static final String PREFS_NAME = "ecoscan_prefs";
    private static final String KEY_DARK_MODE = "dark_mode";

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

        // ── Stats ────────────────────────────────────────────────────────────
        TextView tvMoneySaved   = view.findViewById(R.id.tv_profile_money_saved);
        TextView tvItemsTracked = view.findViewById(R.id.tv_profile_items_tracked);
        TextView tvFoodSaved    = view.findViewById(R.id.tv_profile_food_saved);

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

        db.pantryDao().getConsumedItemsCount().observe(getViewLifecycleOwner(), consumedCount -> {
            if (consumedCount != null) {
                tvFoodSaved.setText(consumedCount + " items");
            } else {
                tvFoodSaved.setText("0 items");
            }
        });

        // ── Dark Mode Switch ─────────────────────────────────────────────────
        SharedPreferences prefs = requireContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        SwitchMaterial switchDarkMode = view.findViewById(R.id.switch_dark_mode);

        // Restore saved preference without triggering the listener
        boolean isDark = prefs.getBoolean(KEY_DARK_MODE, false);
        switchDarkMode.setChecked(isDark);

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Persist choice
            prefs.edit().putBoolean(KEY_DARK_MODE, isChecked).apply();

            // Apply immediately — Activity will recreate itself
            AppCompatDelegate.setDefaultNightMode(
                    isChecked
                            ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO
            );
        });
    }
}