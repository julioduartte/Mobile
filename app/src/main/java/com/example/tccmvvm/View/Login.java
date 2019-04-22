package com.example.tccmvvm.View;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tccmvvm.R;
import com.example.tccmvvm.VModel;
import com.example.tccmvvm.databinding.ActivityLoginBinding;


public class Login extends AppCompatActivity {
    //Binding da view de login
    ActivityLoginBinding loginBinding;
    //Instãncia para a viewModel
    VModel vModel;
    //instância da progressbar
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // controi o vModel
        vModel = new VModel();
        // vincula a view login ao vModel
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginBinding.setVModel(vModel);
        loginBinding.executePendingBindings();

        Button btlogin = findViewById(R.id.btlogin);
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //função que trata o clique do botão de login e inicia a conexão com a API.binding.getVModel().onLoginClick();
                loginBinding.getVModel().onLoginClick();
            }
        });
        progressBar = findViewById(R.id.progressBar);
        //inicia a observação dos campos
        observe();
    }

    public void observe(){
        //após iniciar a chamada no webservice exibe a progressbar e finaliza ao terminar o serviço
        loginBinding.getVModel().obsProgress.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        boolean isLoading = loginBinding.getVModel().obsProgress.get();
                        if (isLoading){
                            progressBar.setVisibility(View.VISIBLE);

                        } else {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
        //exibe o retorno de sucesso com o webservice
        loginBinding.getVModel().retorno.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        String retorno = loginBinding.getVModel().retorno.get();
                        if (retorno!=null && !retorno.equals("")){
                            //cria a intenção da activity para exibir os dados do alino
                            Intent it = new Intent(Login.this, MainActivity.class);
                            it.putExtra("aluno",retorno);
                            startActivity(it);
                        }
                    }
                });
        //exibe o erro ocorrido no webservice
        loginBinding.getVModel().erro.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        String erro = loginBinding.getVModel().erro.get();
                        //valida e exibe o Toast com o retorno de erro
                        if (erro!=null && !erro.equals("")){
                            errMsg(erro);
                        }
                    }
                });
    }
    private void errMsg(String err){
        Toast.makeText(this, err, Toast.LENGTH_LONG).show();
    }
}
