package com.ecoscan.app.ui.scan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.ecoscan.app.R;
import com.ecoscan.app.ui.product.AddProductActivity;
import com.google.android.material.button.MaterialButton;

public class ScanFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton btnManual = view.findViewById(R.id.btn_manual_entry);
        if (btnManual != null) {
            btnManual.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), AddProductActivity.class);
                startActivity(intent);
            });
        }
    }
}