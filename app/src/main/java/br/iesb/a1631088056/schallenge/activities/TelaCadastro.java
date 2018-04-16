package br.iesb.a1631088056.schallenge.activities;

import android.content.Intent;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import br.iesb.a1631088056.schallenge.R;
import br.iesb.a1631088056.schallenge.helpers.ManageUsuarioFirebaseDB;
import br.iesb.a1631088056.schallenge.models.Usuario;


public class TelaCadastro extends AppCompatActivity {

    private Button btnOKCadastro, btnVoltar;
    private EditText email, pwd, pwdC, mNome;
    private FirebaseAuth mAuth;
    private UserProfileChangeRequest mProfile;
    private static final String TAG = "TelaCadastro";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        btnOKCadastro = (Button) findViewById(R.id.btnOkCadastro);
        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        email = (EditText) findViewById(R.id.txtCadEmail);
        mNome = (EditText) findViewById(R.id.txtNome);
        pwd = (EditText) findViewById(R.id.editCadTextpwd);
        pwdC = (EditText) findViewById(R.id.editTextpwdConf);

        // Setup Firebase auth
        mAuth = FirebaseAuth.getInstance();



        //Setup comportamento dos Botoes;


        btnOKCadastro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().isEmpty()) {
                    if (!pwd.getText().toString().isEmpty()) {
                        if ((!pwdC.getText().toString().isEmpty()) && (pwd.getText().toString().equals(pwdC.getText().toString()))) {
                            mAuth.createUserWithEmailAndPassword(email.getText().toString(), pwd.getText().toString())
                                    .addOnCompleteListener(TelaCadastro.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "createUserWithEmail:sucesso");
                                                if (mNome.getText().toString().isEmpty()) {
                                                    String mNick = email.getText().toString();
                                                    int mIndex = mNick.indexOf('@');
                                                    mNick = mNick.substring(0, mIndex);
                                                    mNome.setText(mNick);

                                                }
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                mProfile = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(mNome.getText().toString())
                                                        .build();

                                                user.updateProfile(mProfile)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(TAG, "Nome do Usuário Criado.");
                                                                }
                                                            }
                                                        });
                                                criaUsuario(mAuth.getCurrentUser());
                                                updateUI(mAuth.getCurrentUser());
                                            } else {
                                                // Se houver erro, mostra mensagem ao usuário.
                                                Log.w(TAG, "createUserWithEmail:falha", task.getException());
                                                Toast.makeText(TelaCadastro.this, "Não foi possível criar a conta, tente novamente.",
                                                        Toast.LENGTH_SHORT).show();
                                                Toast.makeText(TelaCadastro.this, task.getException().getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                                updateUI(null);
                                            }

                                        }
                                    });

                        } else {
                            pwdC.setError("Senha e Confirmação devem ser iguais!");
                            pwdC.requestFocus();
//
                        }
                    } else {
//
                        pwd.setFocusable(true);
                        pwd.requestFocus();
                        pwd.setError("Preencha sua Senha!");
                    }



                } else {
//
                    email.requestFocus();
                    email.setError("Preencha seu Email!");

                }

            }
        });


        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {
            startActivity(new Intent( TelaCadastro.this, MainMenuActivity.class));
        } else {
            LoginManager.getInstance().logOut();
            startActivity(new Intent( TelaCadastro.this, MainActivity.class));
        }
    }

    private void criaUsuario(FirebaseUser user) {

        ManageUsuarioFirebaseDB mMUsuario = new ManageUsuarioFirebaseDB();
        Usuario mUsuario = new Usuario(user.getUid(), mNome.getText().toString(), email.getText().toString());

        if (mMUsuario.makeUsuario(mUsuario)) {
            Log.i (TAG, "criou usuario");
            //mMUsuario.storeAvatar(user.getPhotoUrl(), mUsuario.id);

        } else {
            Log.e (TAG, "não criou usuario");
        }

    }
}