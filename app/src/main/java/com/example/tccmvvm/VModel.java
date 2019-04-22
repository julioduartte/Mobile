package com.example.tccmvvm;

import android.annotation.SuppressLint;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VModel {

    public ObservableField<String> mail = new ObservableField<>();
    public ObservableField<String> senha = new ObservableField<>();
    public ObservableField<String> retorno = new ObservableField<>();
    public ObservableField<String> erro = new ObservableField<>();
    public ObservableBoolean obsProgress = new ObservableBoolean();
    private String error = null;

    public void onLoginClick() {
        //verifica se os campos caso null ou vazio informados e prossegue
        if ((mail.get() == null || senha.get() == null) || (mail.get().equals("") || senha.get().equals(""))) {
            return;
        }
        // exibe o progressbar
        obsProgress.set(true);
        //chama a asynck task do webservice
        new Sinc().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class Sinc extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... unused) {
            HttpURLConnection connection = null;
            try {
                //monta a url com os dados passados pelo usuário
                URL url = new URL("http://julioduarte.ddns.net:8181/api/Aluno?email=" + mail.get() + "&password=" + senha.get());
                //abre a conexão
                connection = (HttpURLConnection) url.openConnection();
                //informa o método GET
                connection.setRequestMethod("GET");
                //adiciona o cabeçalho
                connection.setRequestProperty("Accept", "application/json");
                //inicia a conexão
                connection.connect();
                //coleta o codigo de retorno e define qual rota tomar
                int status = connection.getResponseCode();
                switch (status) {
                    case 200:
                        //atualiza o campo de retorno para a view de Login com os dados do aluno se tudo correr bem
                        retorno.set(readStream(connection.getInputStream()));
                }
                switch (status) {
                    case 404:
                    case 405:
                    case 500:
                    case 503:
                        //atualiza o campo de erro para a view Login caso os dados tenham sido informados incorretamente
                        //ou tenha ocorrido algum erro interno.
                        error = new JSONObject(readStream(connection.getErrorStream())).get("Message").toString();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //oculta a progressbar da view de Login e manda o erro caso tenha ocorrido ao final da async task
            obsProgress.set(false);
            if (error != null) {
                erro.set(error);
            }
        }
    }
    //responsável por transformar os retornos em string
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }
}
