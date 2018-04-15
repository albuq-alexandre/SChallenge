package br.iesb.a1631088056.schallenge.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.io.FileNotFoundException;
import java.io.InputStream;

import br.iesb.a1631088056.schallenge.Manifest;
import br.iesb.a1631088056.schallenge.R;
import br.iesb.a1631088056.schallenge.fragments.CellBemFragment;
import br.iesb.a1631088056.schallenge.helpers.ManageUsuarioFirebaseDB;
import br.iesb.a1631088056.schallenge.helpers.dummy.DummyContent;
import br.iesb.a1631088056.schallenge.models.Usuario;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import br.iesb.a1631088056.schallenge.adapters.FragmentAdapter;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MainMenuActivity extends AppCompatActivity
        implements OnNavigationItemSelectedListener, CellBemFragment.OnListFragmentInteractionListener {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ImageView mUserAvatar;
    TextView mUserName, mUserEmail;
    NavigationView mNavigationView;
    private final int SELECT_PHOTO = 1;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final String TAG = "MainMenuActivity";
    private final int PICK_IMAGE_REQUEST = 71;
    private ManageUsuarioFirebaseDB manageUsuarioFirebaseDB;
    private DataSnapshot dsRefUsuario, myDS;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private FirebaseDatabase mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        View header = mNavigationView.getHeaderView(0);
        mUserAvatar = header.findViewById(R.id.userAvatar);
        mUserName = header.findViewById(R.id.userFullName);
        mUserEmail = header.findViewById(R.id.userEmail);
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference("usersAvatar");

        //Resgatando informações do Usuário Logado;
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        //iniciando helper FirebaseDB
        manageUsuarioFirebaseDB = new ManageUsuarioFirebaseDB();





        if (mUser != null ) {

            FirebaseDatabase.getInstance()
                    .getReference("Usuarios")
                    .orderByChild("email")
                    .equalTo(mUser.getEmail())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (ds.child("avatarURL").exists()) {
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setPhotoUri(Uri.parse(ds.child("avatarURL").getValue().toString()))
                                            .build();
                                    try {
                                        mUser.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.i(TAG, "Foto atualizada");
                                                        }
                                                    }
                                                });
                                    } catch (Exception e) {
                                        Log.e(TAG, "Erro na foto" + e.getMessage());
                                    }


                                } else {
                                    if (ContextCompat.checkSelfPermission(MainMenuActivity.this,
                                            Manifest.permission.READ_EXTERNAL_STORAGE)
                                            != PackageManager.PERMISSION_GRANTED) {

                                        // Should we show an explanation?
                                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainMenuActivity.this,
                                                Manifest.permission.READ_EXTERNAL_STORAGE)) {

                                            // Show an expanation to the user *asynchronously* -- don't block
                                            // this thread waiting for the user's response! After the user
                                            // sees the explanation, try again to request the permission.

                                        } else {

                                            // No explanation needed, we can request the permission.

                                            ActivityCompat.requestPermissions(MainMenuActivity.this,
                                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                            // app-defined int constant. The callback method gets the
                                            // result of the request.
                                        }
                                    }

                                    switch (mUser.getProviderId()) {
                                        case "Facebook": {
                                            try {
                                    //            mmstoreAvatar(mUser.getPhotoUrl(), ds.getKey());
                                            } catch (Exception e) {

                                                Log.e(TAG, "linha 130" + e.getMessage());
                                            }
                                        }
                                        case "Google": {
                                            try {
                                  //              mmstoreAvatar(mUser.getPhotoUrl(), ds.getKey());
                                            } catch (Exception e) {
                                                Log.e(TAG, "linha 137" + e.getMessage());
                                            }
                                        }
                                        case "firebase": {
                                            try {
                                //                mmstoreAvatar(mUser.getPhotoUrl(), ds.getKey());
                                            } catch (Exception e) {
                                                Log.e(TAG, "linha 144" + e.getMessage());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                try {
                mUserEmail.setText(mUser.getEmail());
                mUserName.setText(mUser.getDisplayName());
                Picasso.with(this.getApplicationContext())
                        .load(mUser.getPhotoUrl().toString())
                        .transform(new CropCircleTransformation())
                        .into(mUserAvatar);

            } catch (Exception e) {
                Log.e(TAG, mUser.getEmail());
                Log.e(TAG, "erro AccountHeather: " + e.getMessage());
                mUserEmail.setText(mUser.getEmail());
                mUserName.setText(mUser.getDisplayName());
            }
        }

        ViewPager vp_pages= (ViewPager) findViewById(R.id.vp_pages);
        PagerAdapter pagerAdapter=new FragmentAdapter(getSupportFragmentManager());
        vp_pages.setAdapter(pagerAdapter);

        TabLayout tbl_pages= (TabLayout) findViewById(R.id.tbl_pages);
        tbl_pages.setupWithViewPager(vp_pages);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Logout...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                mAuth.signOut();
                startActivity(new Intent( MainMenuActivity.this, MainActivity.class));
                finish();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mUserAvatar.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent iAvatarPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                iAvatarPickerIntent.setType("image/*");
                startActivityForResult(iAvatarPickerIntent., SELECT_PHOTO);*/
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Escolha a Imagem"), PICK_IMAGE_REQUEST);
            }
        });

        mUserEmail.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              startActivity(new Intent(MainMenuActivity.this, PerfilUsuario.class));
                                          }
                                      }
        );

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case PICK_IMAGE_REQUEST:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(imageUri)
                                .build();
                        try {
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.i(TAG, "Foto atualizada");

                                                try {
                                                    DataSnapshot usuario = manageUsuarioFirebaseDB.getUsuarioFromFirebaseUid(mUser);
                                                    mmstoreAvatar(imageUri, usuario.getKey());
                                                    //manageUsuarioFirebaseDB.updateUsuario( usuario, "avatarURL", path);

                                                } catch (Exception e) {
                                                    Log.e(TAG, "Erro ao gravar a foto no storage");
                                                    Log.e(TAG, e.getMessage());
                                                }
                                            }
                                        }
                                    });
                        } catch (Exception e) {
                            Log.e(TAG, "Erro na foto" + e.getMessage());
                        }



                        Picasso.with(this.getApplicationContext())
                                .load(imageUri)
                                .transform(new CropCircleTransformation())
                                .into(mUserAvatar);




                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public void mmstoreAvatar(Uri uri, String myid) {

        final String mId = myid;


        if (!uri.toString().isEmpty()) {
            mStorageRef.child(myid)
                    .putFile(uri)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.e(TAG, e.getMessage());
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                               @Override
                                               public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                   try {
                                                       if (!TextUtils.isEmpty(dsRefUsuario.child("avatarURL").getKey())) {
                                                           mStorageRef.child(mId).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                               @Override
                                                               public void onComplete(@NonNull Task<Uri> task) {
                                                                   FirebaseDatabase
                                                                           .getInstance()
                                                                           .getReference("Usuarios")
                                                                           .child(mId).child("avatarURL")
                                                                           .setValue(task.getResult().toString());
                                                                   Log.i(TAG, task.getResult().toString());
                                                               }
                                                           });

                                                       }

                                                   } catch (Exception e) {

                                                       Log.e(TAG, e.getMessage());
                                                   }
                                               }
                                           }

                    );
        }
        return;
    }






}


