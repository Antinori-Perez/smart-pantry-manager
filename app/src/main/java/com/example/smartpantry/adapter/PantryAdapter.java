package com.example.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.AppPrefs;
import com.example.smartpantry.R;
import com.example.smartpantry.logic.DateUtils;
import com.example.smartpantry.model.PantryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Connects the list of PantryItems (data) to the RecyclerView (screen).
// RecyclerView only creates enough rows to fill the screen, then RE-USES them while scrolling:
//   onCreateViewHolder builds a row once, onBindViewHolder fills a row with the data at a position.
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    // The screen (Activity) implements this to react to taps - the adapter never touches the database
    public interface OnPantryItemListener {
        void onItemClicked(PantryItem item);

        void onDeleteClicked(PantryItem item);
    }

    private final List<PantryItem> items = new ArrayList<>();
    private final OnPantryItemListener listener;
    private boolean expiryAlertsOn = true;
    private long today = DateUtils.startOfToday();

    public PantryAdapter(OnPantryItemListener listener) {
        this.listener = listener;
    }

    // Replaces the data and redraws the list
    public void setItems(List<PantryItem> newItems, boolean expiryAlertsOn) {
        items.clear();
        items.addAll(newItems);
        this.expiryAlertsOn = expiryAlertsOn;
        this.today = DateUtils.startOfToday();
        notifyDataSetChanged();
    }

    // 1. BUILD a new row from item_pantry.xml (only happens for the first few rows)
    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(row);
    }

    // 2. FILL a row with the item at this position (happens every time a row scrolls into view)
    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = items.get(position);

        holder.tvName.setText(item.getName());
        holder.tvQuantity.setText(formatQuantity(item.getQuantity()) + " " + item.getUnit());

        // Expiry line + colour (grey = fine, orange = expiring soon, red = expired)
        int colour = ContextCompat.getColor(holder.itemView.getContext(), R.color.text_secondary);
        String expiryText;
        if (!item.hasExpiryDate()) {
            expiryText = holder.itemView.getContext().getString(R.string.no_expiry);
        } else {
            long days = DateUtils.daysUntil(item.getExpiryDate(), today);
            expiryText = holder.itemView.getContext().getString(R.string.expires_on, DateUtils.format(item.getExpiryDate()));
            if (expiryAlertsOn && days < 0) {
                expiryText = holder.itemView.getContext().getString(R.string.expired_on, DateUtils.format(item.getExpiryDate()));
                colour = ContextCompat.getColor(holder.itemView.getContext(), R.color.expired_red);
            } else if (expiryAlertsOn && days <= AppPrefs.EXPIRING_SOON_DAYS) {
                expiryText = days == 0
                        ? holder.itemView.getContext().getString(R.string.expires_today)
                        : holder.itemView.getContext().getResources()
                        .getQuantityString(R.plurals.expires_in_days, (int) days, (int) days);
                colour = ContextCompat.getColor(holder.itemView.getContext(), R.color.expiring_orange);
            }
        }
        holder.tvExpiry.setText(expiryText);
        holder.tvExpiry.setTextColor(colour);

        // Taps are passed back to the screen
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(item));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClicked(item));
    }

    // 3. HOW MANY rows the list has
    @Override
    public int getItemCount() {
        return items.size();
    }

    // 2.0 -> "2", 0.5 -> "0.5"
    public static String formatQuantity(double quantity) {
        if (quantity == Math.rint(quantity)) {
            return String.valueOf((long) quantity);
        }
        return String.format(Locale.getDefault(), "%.2f", quantity).replaceAll("0+$", "");
    }

    // Holds the widgets of ONE row, so findViewById only runs once per row (faster scrolling)
    static class PantryViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName;
        final TextView tvQuantity;
        final TextView tvExpiry;
        final ImageButton btnDelete;

        PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvQuantity = itemView.findViewById(R.id.tvItemQuantity);
            tvExpiry = itemView.findViewById(R.id.tvItemExpiry);
            btnDelete = itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}