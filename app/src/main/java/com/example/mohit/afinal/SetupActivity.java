package com.example.mohit.afinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SetupActivity extends AppCompatActivity {
    private ImageButton mSetupImageBtn;
    private EditText mNameField;
    private Button mSubmitBtn;
    private static final int GALLERY_REQUEST=1;
    private Uri mimageUri=null;
    private DatabaseReference mDatabaseUsers;
    private StorageReference mStorageImage;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        mSetupImageBtn = (ImageButton)findViewById(R.id.SetupImageButton);
        mNameField = (EditText)findViewById(R.id.setupNameField);
        mSubmitBtn = (Button)findViewById(R.id.setupSubmitBtn);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_images");
        mProgess = new ProgressDialog(this);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSetupAccount();
            }
        });
        mSetupImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);


            }
        });
    }

    private void startSetupAccount() {
     final String name = mNameField.getText().toString().trim();
     final String user_id = mAuth.getCurrentUser().getUid();
        if(!TextUtils.isEmpty(name) && mimageUri!=null){
            mProgess.setMessage("finishing setup...");
            mProgess.show();
           StorageReference filepath = mStorageImage.child(mimageUri.getLastPathSegment());
            filepath.putFile(mimageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUri = taskSnapshot.getDownloadUrl().toString();
                    mDatabaseUsers.child(user_id).child("name").setValue(name);
                    mDatabaseUsers.child(user_id).child("image").setValue(downloadUri);
                    mProgess.dismiss();
                    Intent mainIntent= new Intent(SetupActivity.this,MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    if(requestCode==GALLERY_REQUEST && resultCode== RESULT_OK){

        Uri imageUri = data.getData();
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);
    }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mimageUri = result.getUri();
                mSetupImageBtn.setImageURI(mimageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}
