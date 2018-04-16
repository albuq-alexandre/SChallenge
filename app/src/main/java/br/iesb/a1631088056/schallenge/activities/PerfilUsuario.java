package br.iesb.a1631088056.schallenge.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;

import br.iesb.a1631088056.schallenge.R;
import br.iesb.a1631088056.schallenge.helpers.ManageUsuarioFirebaseDB;
import br.iesb.a1631088056.schallenge.models.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilUsuario extends AppCompatActivity {

    private CircleImageView imgUsuarioAvatar;
    private CheckBox imgEmailVerified;
    private EditText etxtTelefone, etxtNome, etxtCidade;
    private TextView tvEmail;
    private Button  btnSalvar, btnVoltar, btnVerifyEmail, btnRedefSenha;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference dRef;
    private StorageReference sRef;
    private FirebaseUser mUser;
    private DataSnapshot dSUser;
    private UserProfileChangeRequest mProfile;
    private static final String TAG = "PerfilUsuarioActivity";
    private final int YES_NO_CALL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        dRef = firebaseDatabase.getReference("Usuarios");
        sRef = firebaseStorage.getReference("usersAvatar");

        mUser = firebaseAuth.getCurrentUser();

        etxtNome = (EditText) findViewById(R.id.etUsuarioNome);
        etxtTelefone = (EditText) findViewById(R.id.etxtUsuarioTelefone);
        tvEmail = (TextView) findViewById(R.id.tVUsuarioEmail);
        etxtCidade = (EditText) findViewById(R.id.eTxtUsuarioCidade);
        imgUsuarioAvatar = (CircleImageView) findViewById(R.id.usuarioAvatar);
        btnSalvar = (Button) findViewById(R.id.btnOkUsuarioSalvar);
        btnVoltar = (Button) findViewById(R.id.btnUsuarioVoltar);
        btnVerifyEmail = (Button) findViewById(R.id.btnSendVerifyEmail);
        btnRedefSenha = (Button) findViewById(R.id.btnRedefSenha);
        imgEmailVerified = (CheckBox) findViewById(R.id.imgEmailVerified);

        etxtNome.setText(mUser.getDisplayName());
        tvEmail.setText(mUser.getEmail());
        if (mUser.isEmailVerified()) {
            imgEmailVerified.setChecked(true);
        } else {
            imgEmailVerified.setChecked(false);
        }


        Picasso.with(this.getApplicationContext())
                .load(mUser.getPhotoUrl())
                .into(imgUsuarioAvatar);



        dRef.orderByChild("email")
            .equalTo(mUser.getEmail())
            .addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            dSUser = ds;
                            if (ds.hasChild("Telefone"))
                            etxtTelefone.setText(ds.child("Telefone").getValue().toString());
                            if (ds.hasChild("Cidade"))
                            etxtCidade.setText(ds.child("Cidade").getValue().toString());
                     }
                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               mProfile = new UserProfileChangeRequest.Builder()
                        .setDisplayName(etxtNome.getText().toString())
                        .build();

               mUser.updateProfile(mProfile)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Usuário atualizado.");
                                }
                            }
                        });

               Log.e(TAG, mUser.getProviderId());

               if (dSUser==null) {

                       try {
                           String id = dRef.push().getKey();
                           Usuario usuario = new Usuario(mUser.getUid(), mUser.getDisplayName(), mUser.getEmail());
                           dRef.child(id).setValue(usuario);
                           Log.i(TAG, "Usuario criado: " + id);
                       } catch (Exception e) {
                           Log.e(TAG, e.getMessage());
                       }


                   try {
                       Log.i(TAG, "Usuário criado para authprovider: " + dSUser.getKey().toString());
                   } catch (Exception e) {
                       Log.e(TAG, "Usuário NÃO criado para authprovider ");
                       Log.e(TAG, e.getMessage());
                   }
               }

               try {
                   dRef.child(dSUser.getKey()).child("Telefone").setValue(etxtTelefone.getText().toString());
                   dRef.child(dSUser.getKey()).child("Cidade").setValue(etxtCidade.getText().toString());
                   dRef.child(dSUser.getKey()).child("nome").setValue(etxtNome.getText().toString());
               } catch (Exception e) {
                   Log.e (TAG, e.getMessage());
               }


            }
        });

        btnVerifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mUser.isEmailVerified()) {

                    Snackbar.make(view, "Email Já verificado.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {

                    mUser.sendEmailVerification()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Snackbar.make(view, "Email enviado. Veja sua Caixa Postal...", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            });
                }
            }
        });

        btnRedefSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.sendPasswordResetEmail(mUser.getEmail())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Snackbar.make(view, "Email enviado. Veja sua Caixa Postal...", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

 }



