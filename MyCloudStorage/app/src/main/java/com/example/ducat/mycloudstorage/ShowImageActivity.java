package com.example.ducat.mycloudstorage;

import android.app.ProgressDialog;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    //recyclerview Object
    private RecyclerView recyclerView;

    //adapter object
    private RecyclerView.Adapter adapter;

    //database refrence

    private DatabaseReference mDatabase;

    //Progress dialog

    ProgressDialog progressDialog;

    //List to hold all the uploaded images

    private List<Upload> uploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
    recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog=new ProgressDialog(this);

        uploads=new ArrayList<>();

        progressDialog.setMessage("Please Wait.....");
        progressDialog.show();
        mDatabase= FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
            progressDialog.dismiss();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Upload upload=postSnapshot.getValue(Upload.class);
                    uploads.add(upload);
                }
                adapter=new MyAdapter(getApplicationContext(),uploads);

                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
}
