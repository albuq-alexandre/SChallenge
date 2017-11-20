package br.iesb.a1631088056.schallenge.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.iesb.a1631088056.schallenge.R;

public class MainActivity extends AppCompatActivity {

    Button btnOK, btnCadastro;
    EditText txtPasswd, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnOK = (Button) findViewById(R.id.buttonOK);
        btnCadastro = (Button) findViewById(R.id.buttonCadastro);
        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( MainActivity.this, telaCadastro.class));
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Vc clicou OK...", Toast.LENGTH_LONG).show();
            }
        });
        Toast.makeText(this, "Passando pelo onCreate...", Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "Passando pelo onStart...", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Passando pelo onResume...", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "Passando pelo onPause...", Toast.LENGTH_LONG).show();
    }

}


