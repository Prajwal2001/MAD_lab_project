package com.example.collegemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewCollection extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ListView listView;
    ArrayList<ViewListItem> viewList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_collection);
        Intent intent = getIntent();
        String collection = intent.getStringExtra("collection");

        TextView addBtn = findViewById(R.id.addDocument);
        listView = findViewById(R.id.lv);

        addBtn.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), AddCollection.class);
            i.putExtra("collection",collection);
            startActivity(i);
        });

        db.collection(collection).addSnapshotListener((documentSnapshots, e) -> {
            if (e != null)
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            viewList.clear();
            if (documentSnapshots != null) {
                for (DocumentSnapshot doc : documentSnapshots) {
                    Map<String, Object> data = doc.getData();
                    String item = "";
                    assert data != null;
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        item = item.concat(entry.getKey() + ": " + entry.getValue() + "\n");
                    }
                    ViewListItem listItem = new ViewListItem(item, doc.getId());
                    viewList.add(listItem);
                }
            }
            Log.d("List View", "onEvent: " + viewList.toString());

            ViewListItemAdapter viewListItemAdapter = new ViewListItemAdapter(ViewCollection.this, viewList, collection);
            listView.setAdapter(viewListItemAdapter);
        });
    }
}
