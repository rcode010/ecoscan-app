package com.ecoscan.app;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecoscan.app.data.PantryItem;
import com.google.android.material.chip.Chip;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    private List<PantryItem> items;

    public PantryAdapter(List<PantryItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PantryItem item = items.get(position);

        holder.tvName.setText(item.itemName);
        holder.tvBarcode.setText("Barcode: " + item.barcode);

        long now = System.currentTimeMillis();
        long diff = item.expiryDate - now;
        long daysLeft = TimeUnit.MILLISECONDS.toDays(diff);

        if (daysLeft < 0) {
            holder.chipExpiry.setText("Expired");
            holder.chipExpiry.setChipBackgroundColorResource(R.color.expired_red);
            holder.expiryIndicator.setBackgroundColor(Color.parseColor("#E63946"));
        } else if (daysLeft <= 2) {
            holder.chipExpiry.setText("Expires in " + daysLeft + " day(s)");
            holder.chipExpiry.setChipBackgroundColorResource(R.color.warning_yellow);
            holder.expiryIndicator.setBackgroundColor(Color.parseColor("#F4A261"));
        } else {
            holder.chipExpiry.setText(daysLeft + " days left");
            holder.chipExpiry.setChipBackgroundColorResource(R.color.safe_green);
            holder.expiryIndicator.setBackgroundColor(Color.parseColor("#52B788"));
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
            v.getContext().startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvBarcode;
        Chip chipExpiry;
        View expiryIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvBarcode = itemView.findViewById(R.id.tv_barcode);
            chipExpiry = itemView.findViewById(R.id.chip_expiry);
            expiryIndicator = itemView.findViewById(R.id.expiry_indicator);
        }
    }
}