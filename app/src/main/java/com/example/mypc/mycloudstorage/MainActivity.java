package com.example.mypc.mycloudstorage;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    Button b1,b2,b3;
    EditText e1;
    ImageView imageView;
    private static final int PICK_IMAGE_REQUEST = 234;         //request code

    private Uri filepath;          //class to store path of image from phone

    private StorageReference storageReference;          //store image only not text
    private DatabaseReference databaseReference;        //store url of image also store text

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.button2);
        b2 = (Button) findViewById(R.id.button3);
        b3 = (Button) findViewById(R.id.button4);
        e1 = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.imageView);


        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference  = FirebaseDatabase.getInstance().getReference(Constant.DATABASE_PATH_UPLOADS);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        if(v == b1)
        {
            showFileChooser();
        }

        else if(v == b2)
        {
           uploadFile();
        }

        else if(v == b3)
        {
            Intent intent = new Intent(MainActivity.this,ShowImageActivity.class);
            startActivity(intent);
        }
    }


    private void showFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filepath = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                imageView.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    //file extension is method that we made to get extension of image or type of image

    public String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();          //mimetype class that check extension or format
        return mime.getExtensionFromMimeType(cR.getType(uri));      //provide alogo....
        // singleton is method that give ref to read file.....it is instance of MimeTypeMap
    }

    private void uploadFile()
    {
        if(filepath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StorageReference sRef = storageReference.child(Constant.STORAGE_PATH_UPLOADS + System.currentTimeMillis() + " . " + getFileExtension(filepath));
            sRef.putFile(filepath)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    progressDialog.dismiss();

                    Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_SHORT).show();

                    upload upload1 = new upload(e1.getText().toString().trim(),taskSnapshot.getDownloadUrl().toString());

                    String uploadId = databaseReference.push().getKey();   //key generated in server in database
                    databaseReference.child(uploadId).setValue(upload1);
                }
            })


                    .addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploading"+((int)progress)+"%...");
                        }
                    });

        }
    }

}

