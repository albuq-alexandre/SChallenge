package br.iesb.a1631088056.schallenge.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import br.iesb.a1631088056.schallenge.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilUsuario extends AppCompatActivity {

    private CircleImageView imgUsuarioAvatar;
    private EditText etxtTelefone, etxtNome, etxtCidade;
    private TextView tvEmail;
    private Button  btnSalvar, btnVoltar;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference dRef;
    private StorageReference sRef;
    private FirebaseUser mUser;
    private DataSnapshot dSUser;

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

        etxtNome.setText(mUser.getDisplayName());
        tvEmail.setText(mUser.getEmail());

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
                            etxtTelefone.setText(ds.child("Telefone").getValue().toString());
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











    }


}
