package br.com.trinopolo.tarefastrinopolo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    LinearLayout llListaTarefas;
    TarefaService tarefaService;
    Retrofit retrofit;
    String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.usuarioId = "roberguerra";
        setup();


        // TODO:  Postar tarefas
        /*
        Tarefa tarefa1 = new Tarefa();
        tarefa1.corpo = "Tarefa 3 do Android";
        tarefa1.concluida = false;
        tarefa1.usuarioId = this.usuarioId;

        postTarefa(tarefa1);

        Tarefa tarefa2 = new Tarefa();
        tarefa2.corpo = "Tarefa 4 do Android";
        tarefa2.concluida = false;
        tarefa2.usuarioId = this.usuarioId;

        postTarefa(tarefa2);
        */

        showListaTarefa();

    }

    private void setup() {

        setTitle("Tarefas Trino Polo");

        this.llListaTarefas = (LinearLayout) findViewById(R.id.llListaTarefas);

        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://mysterious-meadow-17207.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.tarefaService = this.retrofit.create(TarefaService.class);
    }

    private void showListaTarefa() {
        tarefaService.getAll(this.usuarioId).enqueue(new Callback<List<Tarefa>>() {
            @Override
            public void onResponse(Call<List<Tarefa>> call, Response<List<Tarefa>> response) {

                Log.d("LOG", String.valueOf(response.body().size()));

                for (Tarefa tarefa : response.body()) {
                    Log.d("LOG", tarefa.id);
                    Log.d("LOG", tarefa.usuarioId);
                    Log.d("LOG", tarefa.corpo);
                    Log.d("LOG", "----------------");

                    montarButtonTarefa(tarefa);
                }
            }

            @Override
            public void onFailure(Call<List<Tarefa>> call, Throwable t) {

            }
        });
    }

    private void montarButtonTarefa(final Tarefa tarefa) {
        Button btnTarefa = new Button(this);
        btnTarefa.setText(tarefa.corpo);

        // Define funcao onClick do botao da tarefa
        btnTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentTarefaActivity = new Intent(MainActivity.this, TarefaActivity.class);

                // passar parametros para a tela TarefaActivity
                intentTarefaActivity.putExtra("tarefa_id", tarefa.id);
//                        .putExtra("tarefa_corpo", tarefa.corpo)
//                        .putExtra("tarefa_concluida", tarefa.concluida);

                // Abre a tela TarefaActivity
                MainActivity.this.startActivity(intentTarefaActivity);

            }
        });

        this.llListaTarefas.addView(btnTarefa, 0);
    }

    private void postTarefa(Tarefa tarefa) {

        tarefaService.post(tarefa).enqueue(new Callback<Tarefa>() {
            @Override
            public void onResponse(Call<Tarefa> call, Response<Tarefa> response) {
                Log.d("LOG", "Tarefa.id:" + response.body().id);
            }

            @Override
            public void onFailure(Call<Tarefa> call, Throwable t) {

            }
        });
    }


}
