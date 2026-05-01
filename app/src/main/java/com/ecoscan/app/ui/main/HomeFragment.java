package com.ecoscan.app.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.ecoscan.app.data.EcoScanDatabase;
import com.ecoscan.app.ui.product.PantryAdapter;
import com.ecoscan.app.R;
import com.ecoscan.app.data.Pantry.PantryItem;

public class HomeFragment extends Fragment {
    private EcoScanDatabase db;

    // onCreateView creates the UI (home fragment) | converts the XML to View java objects
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    // onViewCreated uses the UI that was created by onCreateView to populate it with data
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
         * On the following line when we create a db instance,
         * We can't pass in `this` because the current class (Fragment) is NOT considered a context.
         * Calling requireContext() returns the context of the Activity class that this fragment is attached to.
         * */
        db = EcoScanDatabase.getInstance(requireContext());
        long twoDaysFromNow = System.currentTimeMillis() + (2 * 24 * 60 * 60 * 1000);
        TextView totalItemsStat = view.findViewById(R.id.totalItemsStat);
        TextView expiringSoonStat = view.findViewById(R.id.expiringSoonStat);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_pantry);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        /* *
        * We'll pass in empty list initially to the adapter to avoid crashing
        * Because when the onViewCreated() finishes the data isn't yet fetched and is null
        * Passing null to the adapter would cause issues
        * */
        PantryAdapter adapter = new PantryAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        /* *
         * When we deal with LiveData (data that changes over time) (returned by the getAllItems() method)
         * We use the .observe() method to sync the UI with the data as it changes over time.
         *
         * The getViewLifecycleOwner() method returns the lifecycle of the current fragment.
         * It tells the observer for how long it should stay active and watch for changes.
         * Because if the fragment is removed (user navigates to another screen) the observer is no longer needed because we don't want to push new data to a recycler view that no longer exists.
         *
         * NOTE: udpateItems() is a method of the class PantryAdapter
         * */
        db.pantryDao().getAllItems().observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                adapter.updateItems(items);
                setStatCardValue(totalItemsStat, items.size());
            }
        });

        db.pantryDao().getItemsExpiringSoon(twoDaysFromNow).observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                setStatCardValue(expiringSoonStat, items.size());
            }
        });
    }

    // A dynamic helper function to set a value to a status card
    private void setStatCardValue(TextView textView, int value) {
        String text = String.valueOf(value);
        textView.setText(text);
    }
}