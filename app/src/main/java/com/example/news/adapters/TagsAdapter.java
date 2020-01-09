package com.example.news.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;

import java.util.ArrayList;
import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagViewHolder> {
    private List<String> tagsList = new ArrayList<>();
    private boolean deleteMode;

    public TagsAdapter(boolean deleteMode) {
        this.deleteMode = deleteMode;
    }

    public TagsAdapter() {
        this.deleteMode = true;
    }

    void clear() {
        tagsList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_item_view, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        holder.bind(tagsList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return tagsList.size();
    }

    public void addItem(String tag) {
        tagsList.add(tag);
        notifyDataSetChanged();
    }

    class TagViewHolder extends RecyclerView.ViewHolder {
        private TextView cardTextView;
        private View view;

        void bind(String text,
                  final int position) {
            cardTextView.setText(text);

            if (deleteMode) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tagsList.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        }

        TagViewHolder(View itemView) {
            super(itemView);
            cardTextView = itemView.findViewById(R.id.cardTextView);
            this.view = itemView;
        }
    }

    public List<String> getTagsList() {
        return tagsList;
    }

}
