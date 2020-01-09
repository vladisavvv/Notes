package com.example.news.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.AddNotesActivity;
import com.example.news.MainActivity;
import com.example.news.R;
import com.example.news.helpers.Note;
import com.example.news.helpers.SortingType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private List<Note> notesList = new ArrayList<>();

    public void clear() {
        notesList.clear();
        notifyDataSetChanged();
    }

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

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item_view, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.bind(notesList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void addItem(final Note note) {
        notesList.add(note);
        notifyDataSetChanged();
    }

    public void applyFilter(final List<String> tagsList) {
        final List<Note> newNotesList = new ArrayList<>();

        for (int i = 0; i < notesList.size(); ++i) {
            final Note note = notesList.get(i);
            boolean isRequiredNote = true;

            for (int j = 0; j < tagsList.size() && isRequiredNote; ++j) {
                if (!note.getTags().contains(tagsList.get(j)))
                    isRequiredNote = false;
            }

            if (isRequiredNote)
                newNotesList.add(note);
        }

        notesList = newNotesList;
        notifyDataSetChanged();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView dateTextView;
        private Button button;
        private TagsAdapter tagsAdapter;
        private View view;

        void bind(final Note note,
                  final int position) {
            titleTextView.setText((note.getName().length() > 0 ? note.getName() : note.getDate()));
            descriptionTextView.setText(note.getDescription());

            if (note.getName().length() > 0)
                dateTextView.setText(note.getDate());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(view.getContext(), AddNotesActivity.class);

                    intent.putExtra("name", note.getName());
                    intent.putExtra("description", note.getDescription());
                    intent.putExtra("position", note.getId());
                    intent.putExtra("date", note.getDate());

                    final StringBuilder tags = new StringBuilder();
                    for (final String tag : notesList.get(position).getTags())
                        if (!tag.equals(notesList.get(notesList.size() - 1)))
                            tags.append(tag).append(",");
                        else
                            tags.append(tag);
                    intent.putExtra("tags", tags.toString());

                    view.getContext().startActivity(intent);
                }
            });

            tagsAdapter.clear();
            for (int i = 0; i < note.getTags().size(); ++i)
                tagsAdapter.addItem(note.getTags().get(i));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notesList.remove(position);
                    MainActivity.getDbHelper().getWritableDatabase().delete("notes", "id=" + note.getId(), null);

                    notifyDataSetChanged();
                }
            });
        }

        NotesViewHolder(View itemView) {
            super(itemView);

            this.view = itemView;

            button = itemView.findViewById(R.id.button);

            titleTextView = itemView.findViewById(R.id.cardTextView);
            descriptionTextView = itemView.findViewById(R.id.cardTextView2);
            dateTextView = itemView.findViewById(R.id.dateTextView);

            RecyclerView recyclerView = itemView.findViewById(R.id.listTagsForNote);
            tagsAdapter = new TagsAdapter(false);
            recyclerView.setAdapter(tagsAdapter);
        }
    }

}
