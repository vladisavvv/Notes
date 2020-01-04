package com.example.news.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.MainActivity;
import com.example.news.R;

import java.util.ArrayList;
import java.util.List;

public class TagsFilterAdapter extends RecyclerView.Adapter<TagsFilterAdapter.TagViewHolder> {
    private List<String> tagsList = new ArrayList<>();
    private MainActivity mainActivity;

    public TagsFilterAdapter() {
        super();
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

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    class TagViewHolder extends RecyclerView.ViewHolder {
        private TextView cardTextView;
        private View view;

        void bind(final String text,
                  final int position) {
            cardTextView.setText(text);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tagsList.remove(position);
                    notifyDataSetChanged();

                    mainActivity.applyFilter(tagsList);
                }
            });
        }

        TagViewHolder(View itemView) {
            super(itemView);

            this.view = itemView;
            cardTextView = itemView.findViewById(R.id.cardTextView);
        }
    }

    public List<String> getTagsList() {
        return tagsList;
    }

}
