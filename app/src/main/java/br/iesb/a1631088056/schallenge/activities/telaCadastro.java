package br.iesb.a1631088056.schallenge.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.iesb.a1631088056.schallenge.R;




public class telaCadastro extends AppCompatActivity {

    Button btnOKCadastro, btnVoltar;
    EditText email, pwd, pwdC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        btnOKCadastro = (Button) findViewById(R.id.btnOkCadastro);
        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        email = (EditText) findViewById(R.id.txtCadEmail);
        pwd = (EditText) findViewById(R.id.editCadTextpwd);
        pwdC = (EditText) findViewById(R.id.editTextpwdConf);

        btnOKCadastro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().isEmpty()) {
                    Toast.makeText(telaCadastro.this, ("Email " + email.getText().toString()), Toast.LENGTH_LONG).show();

                }
            }
        });

    }
}
