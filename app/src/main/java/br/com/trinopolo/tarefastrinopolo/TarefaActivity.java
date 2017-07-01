package br.com.trinopolo.tarefastrinopolo;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TarefaActivity extends AppCompatActivity {

    TextView tvSalvarRetorno;
    EditText etCorpo;
    Switch swConcluida;
    Button btnSalvar;

    Retrofit retrofit;
    TarefaService tarefaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa);

        setup();

        final String tarefaId = getIntent().getStringExtra("tarefa_id");

        // TODO: Pegar a tarefa
        tarefaService.get(tarefaId).enqueue(new Callback<Tarefa>() {
            @Override
            public void onResponse(Call<Tarefa> call, Response<Tarefa> response) {

                Log.d("LOG", response.body().corpo);

                final Tarefa tarefa = response.body();
                etCorpo.setText(tarefa.corpo);
                swConcluida.setChecked(tarefa.concluida);

                setTitle(tarefa.corpo);

                // TODO: Salvar a tarefa
                btnSalvar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        tarefa.corpo = etCorpo.getText().toString();
                        tarefa.concluida = swConcluida.isChecked();

                        tarefaService.post(tarefa).enqueue(new Callback<Tarefa>() {
                            @Override
                            public void onResponse(Call<Tarefa> call, Response<Tarefa> response) {
                                Log.d("LOG", "Tarefa.id:" + response.body().id);
                                tvSalvarRetorno.setText("Tarefa salva com sucesso.");
                                tvSalvarRetorno.setTextColor(Color.GREEN);
                                tvSalvarRetorno.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(Call<Tarefa> call, Throwable t) {
                                tvSalvarRetorno.setTextColor(Color.RED);
                                tvSalvarRetorno.setText("Erro ao salvar tarefa");
                            }
                        });

                    }
                });
            }

            @Override
            public void onFailure(Call<Tarefa> call, Throwable t) {

            }
        });

        // TODO: PEGANDO OS STATUS DO INTENT
        //String tarefaCorpo = getIntent().getStringExtra("tarefa_corpo");
        //Boolean tarefaConcluida = getIntent().getBooleanExtra("tarefa_concluida", false);
        //setTitle(tarefaCorpo);
        //this.etCorpo.setText(tarefaCorpo);
        //this.swConcluida.setChecked(tarefaConcluida);

    }

    private void setup() {
        this.tvSalvarRetorno = (TextView) findViewById(R.id.tvSalvarRetorno);
        this.tvSalvarRetorno.setVisibility(View.INVISIBLE);
        this.etCorpo = (EditText) findViewById(R.id.etCorpo);
        this.swConcluida = (Switch) findViewById(R.id.swConcluida);
        this.btnSalvar = (Button) findViewById(R.id.btnSalvar);

        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://mysterious-meadow-17207.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.tarefaService = this.retrofit.create(TarefaService.class);

    }
}
