package com.example.miniproject2.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject2.R;
import com.example.miniproject2.data.Movie;
import com.example.miniproject2.ui.MovieDetailActivity;

import java.io.Serializable;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;

    public MovieAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        Context context = holder.itemView.getContext();
        
        holder.tvTitle.setText(movie.title);
        holder.tvDesc.setText(movie.description);

        // Hiển thị ảnh từ thư mục drawable dựa trên tên lưu trong database
        if (movie.posterUrl != null && !movie.posterUrl.isEmpty()) {
            int resID = context.getResources().getIdentifier(movie.posterUrl, "drawable", context.getPackageName());
            if (resID != 0) {
                holder.ivPoster.setImageResource(resID);
            } else {
                // Ảnh mặc định nếu không tìm thấy resource name tương ứng
                holder.ivPoster.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra("EXTRA_MOVIE", (Serializable) movie);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;
        ImageView ivPoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvDesc = itemView.findViewById(R.id.tvMovieDesc);
            ivPoster = itemView.findViewById(R.id.ivMoviePoster);
        }
    }
}