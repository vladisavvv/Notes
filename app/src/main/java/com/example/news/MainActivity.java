package com.example.news;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.news.adapters.NotesAdapter;
import com.example.news.adapters.NotesForGridAdapter;
import com.example.news.adapters.TagsFilterAdapter;
import com.example.news.helpers.DBHelper;
import com.example.news.helpers.Note;
import com.example.news.helpers.SortingType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static DBHelper dbHelper;
    private NotesAdapter notesAdapter;
    private NotesForGridAdapter notesForGridAdapter;
    private static TagsFilterAdapter tagsFilterAdapter = new TagsFilterAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextInputEditText tagsTextInputEditText = findViewById(R.id.textInputEditTextForTag);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getApplicationContext(), AddNotesActivity.class);
                startActivity(intent);
            }
        });

        final RecyclerView recyclerView1 = findViewById(R.id.listTags);
        tagsFilterAdapter.setMainActivity(this);
        recyclerView1.setAdapter(tagsFilterAdapter);

        tagsTextInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && tagsTextInputEditText.getText() != null && tagsTextInputEditText.getText().length() > 0) {
                    tagsFilterAdapter.addItem(tagsTextInputEditText.getText().toString());
                    tagsTextInputEditText.setText("");

                    applyFilter(tagsFilterAdapter.getTagsList());
                }
            }
        });

        dbHelper = new DBHelper(getApplicationContext());

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            final RecyclerView recyclerView = findViewById(R.id.listNotes);
            notesAdapter = new NotesAdapter();
            recyclerView.setAdapter(notesAdapter);
        } else {
            final GridView gridView = findViewById(R.id.kek);
            notesForGridAdapter = new NotesForGridAdapter();
            gridView.setAdapter(notesForGridAdapter);
        }

        applyFilter(tagsFilterAdapter.getTagsList());
    }

    public void applyFilter(final List<String> tagsList) {
        updateAdapter();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            notesAdapter.applyFilter(tagsList);
        else
            notesForGridAdapter.applyFilter(tagsList);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.dateSorting) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                notesAdapter.sort(SortingType.NEW_FIRST);
            else
                notesForGridAdapter.sort(SortingType.NEW_FIRST);

            return true;
        }

        if (id == R.id.nameSorting) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                notesAdapter.sort(SortingType.NAME_LEXICOGRAPHICAL_ORDER);
            else
                notesForGridAdapter.sort(SortingType.NAME_LEXICOGRAPHICAL_ORDER);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateAdapter();
    }

    private void updateAdapter() {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Cursor c = db.query("notes", null, null, null, null, null, null);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            notesAdapter.clear();

            if (c.moveToFirst()) {
                int idColIndex = c.getColumnIndex("id");
                int nameColIndex = c.getColumnIndex("name");
                int notesColIndex = c.getColumnIndex("notes");
                int tagsColIndex = c.getColumnIndex("tags");
                int dateColIndex = c.getColumnIndex("date");

                do {
                    List<String> tags = Arrays.asList(c.getString(tagsColIndex).split(","));
                    if (tags.size() == 1 && tags.get(0).equals(""))
                        tags = new ArrayList<>();

                    notesAdapter.addItem(new Note(c.getString(nameColIndex), c.getString(notesColIndex), tags, c.getString(dateColIndex), c.getInt(idColIndex)));
                } while (c.moveToNext());
            }
        } else {
            notesForGridAdapter.clear();

            if (c.moveToFirst()) {
                int idColIndex = c.getColumnIndex("id");
                int nameColIndex = c.getColumnIndex("name");
                int notesColIndex = c.getColumnIndex("notes");
                int tagsColIndex = c.getColumnIndex("tags");
                int dateColIndex = c.getColumnIndex("date");

                do {
                    List<String> tags = Arrays.asList(c.getString(tagsColIndex).split(","));
                    if (tags.size() == 1 && tags.get(0).equals(""))
                        tags = new ArrayList<>();

                    notesForGridAdapter.addItem(new Note(c.getString(nameColIndex), c.getString(notesColIndex), tags, c.getString(dateColIndex), c.getInt(idColIndex)));
                } while (c.moveToNext());
            }
        }

        c.close();
    }

    public static DBHelper getDbHelper() {
        return dbHelper;
    }
}
