package com.example.miniproject2.adapters;

import android.content.Context;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject2.R;
import com.example.miniproject2.models.Showtime;

import java.util.List;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder> {

    private List<Showtime> showtimes;

    public ShowtimeAdapter(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }

    @NonNull
    @Override
    public ShowtimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_showtime, parent, false);
        return new ShowtimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimeViewHolder holder, int position) {
        Showtime showtime = showtimes.get(position);
        holder.tvTheater.setText(showtime.getTheaterName());
        holder.tvTime.setText(showtime.getTime());

        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Toast.makeText(context, "Đang chuyển sang Đặt vé...", Toast.LENGTH_SHORT).show();

            // TODO: Bỏ comment khi Dev 4 làm xong BookingActivity
            // Intent intent = new Intent(context, BookingActivity.class);
            // intent.putExtra("EXTRA_SHOWTIME_ID", showtime.getId());
            // context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return showtimes.size();
    }

    public static class ShowtimeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTheater, tvTime;

        public ShowtimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTheater = itemView.findViewById(R.id.tvTheaterName);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}