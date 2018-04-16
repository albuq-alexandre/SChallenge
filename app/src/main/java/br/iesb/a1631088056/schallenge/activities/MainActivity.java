package br.iesb.a1631088056.schallenge.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import br.iesb.a1631088056.schallenge.R;
import br.iesb.a1631088056.schallenge.helpers.ManageUsuarioFirebaseDB;
import br.iesb.a1631088056.schallenge.models.Usuario;

public class MainActivity extends AppCompatActivity {

    Button btnOK, btnCadastro;
    SignInButton btnGmailLogin;
    EditText txtPasswd, txtEmail;
    TextView esqueciSenha;
    private CallbackManager callbackManagerFB;
    private LoginButton loginButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private Usuario myUsuario;
    private boolean criauser = false;

    private static final String TAG = "MainActivity-login";
    private static final int RC_SIGN_IN = 9001;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private Map<String,Boolean> permRequired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Elementos de Tela
        btnOK = (Button) findViewById(R.id.buttonOK);
        btnCadastro = (Button) findViewById(R.id.buttonCadastro);
        esqueciSenha = (TextView) findViewById(R.id.txtEsqueciSenha);

        // Permissões
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

        permRequired = new HashMap<String, Boolean>();

        permRequired.put(Manifest.permission.CAMERA,false);
        permRequired.put(Manifest.permission.ACCESS_COARSE_LOCATION,false);
        permRequired.put(Manifest.permission.ACCESS_FINE_LOCATION,false);
        permRequired.put(Manifest.permission.WRITE_EXTERNAL_STORAGE,false);
        permRequired.put(Manifest.permission.READ_EXTERNAL_STORAGE,false);
        permRequired.put(Manifest.permission.INTERNET,false);
            //Checa se existem as permissões necessárias para o app


        Iterator<Map.Entry<String,Boolean>> it = permRequired.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry<String,Boolean> permissao = it.next();


            if (ActivityCompat.checkSelfPermission(MainActivity.this,permissao.getKey())!= PackageManager.PERMISSION_GRANTED){
                Log.i (TAG, "Pedir permissão para "+ permissao.getKey());
                sentToSettings = true;
            } else {
                Log.i (TAG, "Permissão concedida para "+ permissao.getKey());
                permRequired.put(permissao.getKey(),true);
            }
        }




        if (sentToSettings){
                sentToSettings = false;
                requererpermissao();
            }


        // Configura Firebase
        mAuth = FirebaseAuth.getInstance();
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPasswd = (EditText) findViewById(R.id.txtPasswd);


        // Configura Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGmailLogin = (SignInButton) findViewById(R.id.gmail_sign_in_button);

        btnGmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
            }
        });


        // Config para FB
        callbackManagerFB = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");

        // Callback registration
        loginButton.registerCallback(callbackManagerFB, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                Toast.makeText(MainActivity.this, "Facebook id" + loginResult.getAccessToken().getUserId(), Toast.LENGTH_LONG).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
                updateUI(mAuth.getCurrentUser());
                // startActivity(new Intent( MainActivity.this, MainMenuActivity.class));
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                Toast.makeText(MainActivity.this, "Vc cancelou o login FB...", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "facebook:onError", exception);
                Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Cadastro no Firebase - email e senha


        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TelaCadastro.class));
            }
        });


        // Login Firebase email e senha


        btnOK.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                //startActivity(new Intent( MainActivity.this, MainMenuActivity.class));

                mAuth.signInWithEmailAndPassword(txtEmail.getText().toString(), txtPasswd.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    Log.d(TAG, task.getException().getMessage());
                                } else {
                                    Toast.makeText(MainActivity.this, "Logado", Toast.LENGTH_LONG).show();
                                    FirebaseUser user = task.getResult().getUser();

                                    if (user != null) {
                                        startActivity(new Intent(MainActivity.this, MainMenuActivity.class));
                                    }
                                }
                            }
                        });
            }
        });

        esqueciSenha.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   if (TextUtils.isEmpty(txtEmail.getText())) {
                       txtEmail.setError("Informe seu Email");
                       txtEmail.requestFocus();
                   } else {

                       try {
                           mAuth.sendPasswordResetEmail(txtEmail.getText().toString())
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           Snackbar.make(view, "Email enviado. Veja sua Caixa Postal...", Snackbar.LENGTH_LONG)
                                                   .setAction("Action", null).show();
                                       }
                                   });
                       } catch (Exception e) {
                           Log.e (TAG, "Falha ao enviar email restart da senha");
                           Log.e (TAG, e.getMessage());
                           Snackbar.make(view, "Falha ao enviar o Email. Verifique ou Faça Cadastro.", Snackbar.LENGTH_LONG)
                                   .setAction("Action", null).show();
                       }
                   }
               }
           });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManagerFB.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Falha no Google Sign In
                Log.w(TAG, "Falha no Login com Google", e);
                updateUI(null);

            }
        }

        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }

    }

    // Helper para Autenticar com google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        final GoogleSignInAccount macct = acct;

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredentialGoogle:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(macct.getPhotoUrl())
                                    .build();
                            try {
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "Foto atualizada");
                                                }
                                            }
                                        });
                            } catch (Exception e) {
                                Log.d(TAG, "Erro na foto" + e.getMessage());
                            }

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredentialGoogle:failure", task.getException());
                            Snackbar.make(findViewById(R.id.linearBottonLogin), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }


    // Helper para Autenticar com Facebook

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredentialFB:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredentialFB:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Verificando se existe conta já logada
        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateUI(currentUser);
    }

    //helper para redirecionamento/isolamento da área segura

    private void updateUI(FirebaseUser user) {

        if (user != null) {
            startActivity(new Intent(MainActivity.this, MainMenuActivity.class));
        } else {
            findViewById(R.id.gmail_sign_in_button).setVisibility(View.VISIBLE);
            LoginManager.getInstance().logOut();
        }
    }

    // helper para criar o usuário no FirebaseDatabase

    private void criaUsuario(FirebaseUser user, Uri photoUri) {

        ManageUsuarioFirebaseDB mMUsuario = new ManageUsuarioFirebaseDB();

            myUsuario = new Usuario(user.getUid(), user.getDisplayName(), user.getEmail());

            if (mMUsuario.makeUsuario(myUsuario)) {
                Log.i(TAG, "criou usuario");
                try {
                    mMUsuario.storeAvatar(photoUri, mMUsuario.getUsuarioFromFirebaseUid(user).getKey());
                } catch (Exception e) {
                    Log.e(TAG, "Não guardou a foto do avatar");
                    Log.e(TAG, e.getMessage());
                }

            } else {
                Log.e(TAG, "não criou usuario");
            }
    }

    private void requererpermissao() {


                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Permissões Múltiplas são nessessárias");
                builder.setMessage("Esse app precissa de permissão para Câmera, Localização e Gravação de Conteúdo");
                builder.setPositiveButton("Conceder", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Iterator<Map.Entry<String,Boolean>> it = permRequired.entrySet().iterator();

                        while (it.hasNext()) {

                            Map.Entry<String,Boolean> permissao = it.next();

                            if (permissao.getValue()) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permissao.getKey()}, REQUEST_PERMISSION_SETTING);
                                Log.e (TAG, "Permissão concedida para "+ permissao.getKey());
                            }
                        }
                        dialog.cancel();

                    }
                });
                builder.setNegativeButton("Cancela", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e (TAG, "Permissão Não concedida");
                        dialog.cancel();
                    }
                });
                builder.show();








    }

    /*@Override
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        switch (requestCode) {
            case REQUEST_PERMISSION_SETTING :
            if (grantResults.length > 0) {
                permissionStatus.edit();
                permissionStatus.putBoolean(permissions[0],true);
                permissionStatus.commit();
            }

        }

    }*/

    protected void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        Toast.makeText(getBaseContext(), "Permissão Concedida", Toast.LENGTH_LONG).show();
    }
}






