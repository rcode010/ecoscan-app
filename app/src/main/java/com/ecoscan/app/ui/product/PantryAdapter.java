package com.ecoscan.app.ui.product;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecoscan.app.R;
import com.ecoscan.app.data.Pantry.PantryItem;
import com.google.android.material.chip.Chip;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    private List<PantryItem> items;

    public PantryAdapter(List<PantryItem> items) {
        this.items = items;
    }

    public void updateItems(List<PantryItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    // The onCreateViewHolder() method is called when the RecyclerView needs to show a new ViewHolder to represent an item on the UI.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_product, parent, false);
        return new ViewHolder(view);
    }

    // Binds the data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PantryItem item = items.get(position);

        // Set the TextViews data
        holder.tvName.setText(item.itemName);
        holder.tvBarcode.setText("Barcode: " + item.barcode);

        // Set the Chip data
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

        // Set click listener for each item when clicked it opens the product detail activity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);

            // Pass in the item to the detail activity
            intent.putExtra("item", item.toString());

            v.getContext().startActivity(intent);
        });
    }

    // Tells the RecyclerView how many items are in the list to display
    @Override
    public int getItemCount() {
        return items.size();
    }

    /*
     *
     * ViewHolder: holds reference to the views (UI components) inside each item of the list
     *
     * ViewHolder fetches them (UI components) only once and caches them.
     * That way we don't have to call findViewById() on every scroll event which would make the app slow.
     *
     * EXAMPLE FLOW:
     * App opens, 10 items fit on screen
        → onCreateViewHolder()  ×10   (inflates 10 row layouts, finds all views, caches them)
        → onBindViewHolder()    ×10   (fills each with item[0] through item[9] data)

        User scrolls down — item[0] goes off screen, item[10] comes in
        → onCreateViewHolder()  ×0    (no new inflation — nothing)
        → onBindViewHolder()    ×1    (takes the recycled ViewHolder from item[0], overwrites its tvName, tvBarcode, chip with item[10]'s data)

        User keeps scrolling — item[1] off, item[11] in
        → onBindViewHolder()    ×1    (reuses item[1]'s ViewHolder, binds item[11])
     * */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvBarcode;
        Chip chipExpiry;
        View expiryIndicator;

        // Constructor to initialize the views once
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvBarcode = itemView.findViewById(R.id.tv_barcode);
            chipExpiry = itemView.findViewById(R.id.chip_expiry);
            expiryIndicator = itemView.findViewById(R.id.expiry_indicator);
        }
    }
}