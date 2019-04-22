package com.example.tccmvvm.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tccmvvm.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        TextView txtDados = findViewById(R.id.txtDados);

        if(bundle!= null)
        {
            //recebe o aluno retornado pelo webservice e transforma em um objeto json e extrai os dados para a tela
            try {
                JSONObject aluno = new JSONObject(bundle.getString("aluno"));
                String dados = aluno.get("Nome").toString() +"\n"+
                        aluno.get("Cpf").toString()+"\n"+
                        aluno.get("Endereco").toString()+"\n"+
                        aluno.get("Bairro").toString()+"\n"+
                        aluno.get("Municipio").toString()+"\n"+
                        aluno.get("Uf").toString()+"\n"+
                        aluno.get("Telefone").toString()+"\n"+
                        aluno.get("Email").toString()+"\n"+
                        aluno.get("Senha").toString();
                txtDados.setText(dados);
                Toast.makeText(this, "Aluno logado com sucesso!", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
