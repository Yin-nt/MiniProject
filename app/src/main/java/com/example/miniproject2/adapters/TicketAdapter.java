package com.example.miniproject2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject2.R;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    public static class TicketDisplay {
        public int ticketId;
        public String movieTitle;
        public String theaterName;
        public String time;
        public String seatNumber;

        public TicketDisplay(int ticketId, String movieTitle, String theaterName, String time, String seatNumber) {
            this.ticketId = ticketId;
            this.movieTitle = movieTitle;
            this.theaterName = theaterName;
            this.time = time;
            this.seatNumber = seatNumber;
        }
    }

    private List<TicketDisplay> tickets;

    public TicketAdapter(List<TicketDisplay> tickets) {
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        TicketDisplay ticket = tickets.get(position);
        holder.tvTicketId.setText("Mã vé: #" + ticket.ticketId);
        holder.tvMovieTitle.setText(ticket.movieTitle);
        holder.tvTheaterName.setText(ticket.theaterName);
        holder.tvTime.setText(ticket.time);
        holder.tvSeat.setText("Ghế: " + ticket.seatNumber);
    }

    @Override
    public int getItemCount() { return tickets.size(); }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView tvTicketId, tvMovieTitle, tvTheaterName, tvTime, tvSeat;
        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTicketId = itemView.findViewById(R.id.tvTicketId);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvTheaterName = itemView.findViewById(R.id.tvTheaterName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvSeat = itemView.findViewById(R.id.tvSeat);
        }
    }
}
