package com.example.mohit.afinal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mPlist;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent= new Intent(MainActivity.this,LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Work");
        mPlist=(RecyclerView) findViewById(R.id.project_list);
        mPlist.setHasFixedSize(true);
        mPlist.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Project,Projectviewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Project, Projectviewholder>(
                Project.class,
                R.layout.project_list,
                Projectviewholder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(Projectviewholder viewHolder, Project model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setUsername(model.getUsername());
            }
        };
  mPlist.setAdapter(firebaseRecyclerAdapter);
    }
    public static class Projectviewholder extends RecyclerView.ViewHolder{
         View mView;
        public Projectviewholder(View itemView) {
            super(itemView);
           mView = itemView;
        }
        public void setTitle(String title){
            TextView post_title= (TextView)mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }
        public void setDesc(String desc){
            TextView post_desc = (TextView)mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }
        public void setUsername(String username){
            TextView post_username = (TextView)mView.findViewById(R.id.post_username);
            post_username.setText(username);
        }
        public void setImage(Context ctx ,String image){
            ImageView post_image= (ImageView)mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_add){
            startActivity(new Intent(MainActivity.this,PostActivity.class));
        }
        if(item.getItemId()==R.id.action_logout){
            logout();
        }
        if(item.getItemId()==R.id.account_setup){
            startActivity(new Intent(MainActivity.this,SetupActivity.class));
        }
        if(item.getItemId()==R.id.chat){
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.mohit.mychatapp");
            if (launchIntent != null) {
                startActivity(launchIntent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
         mAuth.signOut();
    }
}
