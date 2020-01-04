package com.example.news.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.news.AddNotesActivity;
import com.example.news.R;
import com.example.news.helpers.Note;
import com.example.news.helpers.SortingType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotesForGridAdapter extends BaseAdapter {
    private List<Note> notesList = new ArrayList<>();

    public void sort(final SortingType sortingType) {
        switch (sortingType) {
            case NEW_FIRST:
                Collections.sort(notesList, new Comparator<Note>() {
                    @Override
                    public int compare(final Note o1,
                                       final Note o2) {
                        return o1.getDate().compareTo(o2.getDate());
                    }
                });
                Collections.reverse(notesList);

                break;
            case NAME_LEXICOGRAPHICAL_ORDER:
                Collections.sort(notesList, new Comparator<Note>() {
                    @Override
                    public int compare(final Note o1,
                                       final Note o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

                break;
        }

        notifyDataSetChanged();
    }

    public void addItem(final Note note) {
        notesList.add(note);
        notifyDataSetChanged();
    }

    public void clear() {
        notesList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return notesList.size();
    }

    @Override
    public Object getItem(int position) {
        return notesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position,
                        View convertView,
                        final ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item_view, parent, false);

        TextView cardTextView = convertView.findViewById(R.id.cardTextView);
        TextView cardTextView2 = convertView.findViewById(R.id.cardTextView2);
        TextView dateTextView = convertView.findViewById(R.id.dateTextView);

        RecyclerView recyclerView = convertView.findViewById(R.id.listTagsForNote);
        TagsAdapter tagsAdapter = new TagsAdapter();
        recyclerView.setAdapter(tagsAdapter);

        cardTextView.setText(notesList.get(position).getName().length() > 0 ? notesList.get(position).getName() : notesList.get(position).getDate());
        cardTextView2.setText(notesList.get(position).getDescription());

        if (notesList.get(position).getName().length() > 0)
            dateTextView.setText(notesList.get(position).getDate());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent browserIntent = new Intent(parent.getContext(), AddNotesActivity.class);

                browserIntent.putExtra("name", notesList.get(position).getName());
                browserIntent.putExtra("description", notesList.get(position).getDescription());
                browserIntent.putExtra("position", notesList.get(position).getId() + 1);
                browserIntent.putExtra("date", notesList.get(position).getDate());

                final StringBuilder tags = new StringBuilder();
                for (final String tag : notesList.get(position).getTags())
                    tags.append(tag).append(",");
                browserIntent.putExtra("tags", tags.toString());

                parent.getContext().startActivity(browserIntent);
            }
        });

        for (int i = 0; i < notesList.get(position).getTags().size(); ++i)
            tagsAdapter.addItem(notesList.get(position).getTags().get(i));

        return convertView;
    }

    public void applyFilter(final List<String> tagsList) {
        final List<Note> newNotesList = new ArrayList<>();

        for (int i = 0; i < notesList.size(); ++i) {
            final Note note = notesList.get(i);
            boolean isAcceptedNote = true;

            for (int j = 0; j < tagsList.size() && isAcceptedNote; ++j) {
                if (!note.getTags().contains(tagsList.get(j)))
                    isAcceptedNote = false;
            }

            if (isAcceptedNote)
                newNotesList.add(note);
        }

        notesList = newNotesList;
        notifyDataSetChanged();
    }
}
