package com.example.miniproject2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject2.R;
import com.example.miniproject2.data.Theater;

import java.util.List;

public class TheaterAdapter extends RecyclerView.Adapter<TheaterAdapter.TheaterViewHolder> {
    private List<Theater> theaters;

    public TheaterAdapter(List<Theater> theaters) {
        this.theaters = theaters;
    }

    @NonNull
    @Override
    public TheaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theater, parent, false);
        return new TheaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheaterViewHolder holder, int position) {
        Theater theater = theaters.get(position);
        holder.tvName.setText(theater.name);
        holder.tvAddress.setText(theater.address);
    }

    @Override
    public int getItemCount() { return theaters.size(); }

    public static class TheaterViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress;
        public TheaterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTheaterName);
            tvAddress = itemView.findViewById(R.id.tvTheaterAddress);
        }
    }
}
