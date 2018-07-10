package com.example.mypc.mycloudstorage;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowImageActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    private List<upload> uploads;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);
        uploads = new ArrayList<>();
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_UPLOADS);

        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                progressDialog.dismiss();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    upload upload = postSnapshot.getValue(upload.class);
                    uploads.add(upload);
                }

                adapter = new Myadapter(getApplicationContext(),uploads);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}
