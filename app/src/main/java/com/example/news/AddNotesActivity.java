package com.example.news;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.adapters.TagsAdapter;

public class AddNotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        final Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        final RecyclerView recyclerView = findViewById(R.id.listTags);
        final TagsAdapter tagsAdapter = new TagsAdapter();
        recyclerView.setAdapter(tagsAdapter);

        tagsAdapter.addItem("kek");
        tagsAdapter.addItem("lol");
        tagsAdapter.addItem("lol");
        tagsAdapter.addItem("lol");
        tagsAdapter.addItem("lol");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.app_bar_search)
            finish();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manu_for_add_notes, menu);
        return true;
    }

}