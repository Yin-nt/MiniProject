package com.example.miniproject2.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject2.R;
import com.example.miniproject2.ui.BookingActivity;
import com.example.miniproject2.ui.LoginActivity;
import com.example.miniproject2.data.Showtime;
import com.example.miniproject2.utils.SessionManager;

import java.util.List;

public class ShowtimeFullAdapter extends RecyclerView.Adapter<ShowtimeFullAdapter.ViewHolder> {
    private List<Showtime> showtimes;

    public ShowtimeFullAdapter(List<Showtime> showtimes) {
        this.showtimes = showtimes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_showtime_full, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Showtime st = showtimes.get(position);
        holder.tvMovieTitle.setText(st.movieTitle != null ? st.movieTitle : "N/A");
        holder.tvTheaterName.setText(st.theaterName != null ? st.theaterName : "N/A");
        holder.tvTime.setText(st.time);

        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            SessionManager session = new SessionManager(context);
            if (session.checkIsLoggedIn()) {
                Intent intent = new Intent(context, BookingActivity.class);
                intent.putExtra(BookingActivity.EXTRA_SHOWTIME_ID, st.id);
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra(LoginActivity.EXTRA_SHOWTIME_ID, st.id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return showtimes.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieTitle, tvTheaterName, tvTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvTheaterName = itemView.findViewById(R.id.tvTheaterName);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
