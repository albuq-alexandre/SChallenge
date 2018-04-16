package br.iesb.a1631088056.schallenge.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.iesb.a1631088056.schallenge.R;

public class Permissoes extends AppCompatActivity {

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private TextView txtPermissions;
    private Button btnCheckPermissions;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);

        txtPermissions = (TextView) findViewById(R.id.txtPermissions);
        btnCheckPermissions = (Button) findViewById(R.id.btnCheckPermissions);

        btnCheckPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(Permissoes.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(Permissoes.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(Permissoes.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(Permissoes.this,permissionsRequired[0])
                            || ActivityCompat.shouldShowRequestPermissionRationale(Permissoes.this,permissionsRequired[1])
                            || ActivityCompat.shouldShowRequestPermissionRationale(Permissoes.this,permissionsRequired[2])){
                        //Show Information about why you need the permission
                        AlertDialog.Builder builder = new AlertDialog.Builder(Permissoes.this);
                        builder.setTitle("Permissões Múltiplas são nessessárias");
                        builder.setMessage("Esse app precissa de permissão para Câmera, Localização e Gravação de Conteúdo");
                        builder.setPositiveButton("Conceder", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                ActivityCompat.requestPermissions(Permissoes.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                            }
                        });
                        builder.setNegativeButton("Cancela", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {
                        //Previously Permission Request was cancelled with 'Dont Ask Again',
                        // Redirect to Settings after showing Information about why you need the permission
                        AlertDialog.Builder builder = new AlertDialog.Builder(Permissoes.this);
                        builder.setTitle("Permissões Múltiplas são nessessárias");
                        builder.setMessage("Esse app precissa de permissão para Câmera, Localização e Gravação de Conteúdo");
                        builder.setPositiveButton("Conceder", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                sentToSettings = true;
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                Toast.makeText(getBaseContext(), "Vá até Permissões e conceda Camera, Localização e Gravação de Conteúdo", Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Cancela", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }  else {
                        //just request the permission
                        ActivityCompat.requestPermissions(Permissoes.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }

                    txtPermissions.setText("Permissões são Requeridas");

                    SharedPreferences.Editor editor = permissionStatus.edit();
                    editor.putBoolean(permissionsRequired[0],true);
                    editor.commit();
                } else {
                    //You already have the permission, just go ahead.
                    proceedAfterPermission();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                proceedAfterPermission();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(Permissoes.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Permissoes.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Permissoes.this,permissionsRequired[2])){
                txtPermissions.setText("Permissões são Requeridas");
                AlertDialog.Builder builder = new AlertDialog.Builder(Permissoes.this);
                builder.setTitle("Permissões Múltiplas são nessessárias");
                builder.setMessage("Esse app precissa de permissão para Câmera, Localização e Gravação de Conteúdo");
                builder.setPositiveButton("Concede", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Permissoes.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancela", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(),"Não foi possível obter a Permissão",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(Permissoes.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {
        txtPermissions.setText("Todas Permissões Concedidas");
        Toast.makeText(getBaseContext(), "Todas as Permissões Concedidas", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(Permissoes.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
}
/*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissoes);
    }
}
*/
