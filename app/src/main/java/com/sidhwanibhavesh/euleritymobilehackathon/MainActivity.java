package com.sidhwanibhavesh.euleritymobilehackathon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ListView imageListView;
    private TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageListView = findViewById(R.id.image_list);
        emptyTextView = findViewById(R.id.empty_view);

        HttpRequestExecutor.getJsonData(this);

        imageListView.setEmptyView(emptyTextView);
    }

    public void initAdapter () {
        ImageListAdapter imageListAdapter = new ImageListAdapter(this, Constants.imageArray);
        imageListView.setAdapter(imageListAdapter);

        imageListView.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, EditImageActivity.class);
            intent.putExtra(Constants.INTENT_EXTRA_POS_KEY, position);
            startActivity(intent);
        });
    }
}