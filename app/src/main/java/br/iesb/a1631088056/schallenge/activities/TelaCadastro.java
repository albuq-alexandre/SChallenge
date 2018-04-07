package br.iesb.a1631088056.schallenge.activities;

import android.content.Intent;
import android.net.Uri;
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
        mAuth = FirebaseAuth.getInstance();

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
                                                updateUI(user);
                                            } else {
                                                // If sign in fails, display a message to the user.
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
                            Toast.makeText(TelaCadastro.this, ("Senha e Confirmação devem ser iguais!"), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(TelaCadastro.this, ("Preencha sua Senha!"), Toast.LENGTH_LONG).show();
                    }



                } else {
                    Toast.makeText(TelaCadastro.this, ("Preencha seu Email!"), Toast.LENGTH_LONG).show();
                }

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
}