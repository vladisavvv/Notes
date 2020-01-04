package com.example.news;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.adapters.TagsAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddNotesActivity extends AppCompatActivity {
    private TagsAdapter tagsAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        final Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        final RecyclerView recyclerView = findViewById(R.id.listTags);
        tagsAdapter = new TagsAdapter();
        recyclerView.setAdapter(tagsAdapter);

        final TextInputEditText nameTextInputEditText = findViewById(R.id.headingEditText);
        final TextInputEditText descriptionTextInputEditText = findViewById(R.id.textInputEditText);
        final TextInputEditText tagsTextInputEditText = findViewById(R.id.textInputEditTextForTag);

        tagsTextInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && tagsTextInputEditText.getText() != null && tagsTextInputEditText.getText().length() > 0) {
                    tagsAdapter.addItem(tagsTextInputEditText.getText().toString());
                    tagsTextInputEditText.setText("");
                }
            }
        });

        if (getIntent().getExtras() != null && getIntent().getExtras().getString("name") != null)
            nameTextInputEditText.setText(getIntent().getExtras().getString("name"));

        if (getIntent().getExtras() != null && getIntent().getExtras().getString("description") != null)
            descriptionTextInputEditText.setText(getIntent().getExtras().getString("description"));

        if (getIntent().getExtras() != null && getIntent().getExtras().getString("tags") != null) {
            final List<String> tags = Arrays.asList(getIntent().getExtras().getString("tags").split(","));

            for (int i = 0; i < tags.size(); ++i)
                tagsAdapter.addItem(tags.get(i));
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.app_bar_search)
            finish();

        ContentValues cv = new ContentValues();

        final TextInputEditText nameTextInputEditText = findViewById(R.id.headingEditText);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
        cv.put("date", sdf.format(new Date()));

        cv.put("name", nameTextInputEditText.getText().toString());
        cv.put("notes", ((TextInputEditText) findViewById(R.id.textInputEditText)).getText().toString());

        final StringBuilder tags = new StringBuilder();
        for (final String tag : tagsAdapter.getTagsList())
            tags.append(tag).append(",");

        cv.put("tags", tags.toString());

        if (getIntent().getExtras() != null && getIntent().getExtras().getInt("position") != 0) {
            MainActivity.getDbHelper().getWritableDatabase().update("notes", cv,  "id=" + getIntent().getExtras().getInt("position"), null);
        } else
            MainActivity.getDbHelper().getWritableDatabase().insert("notes", null, cv);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.manu_for_add_notes, menu);
        return true;
    }
}