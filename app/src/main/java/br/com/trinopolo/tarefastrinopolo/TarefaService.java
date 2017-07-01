package br.com.trinopolo.tarefastrinopolo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by guerra on 01/07/17.
 */

public interface TarefaService {


    @GET("tarefas/{usuarioId}/todas")
    Call<List<Tarefa>> getAll(@Path("usuarioId") String usuarioId);

    @GET("/tarefas/{id}")
    Call<Tarefa> get(@Path("id") String id);

    @PUT("/tarefas/{id}")
    Call<Tarefa> put(@Path("id") String id, @Body Tarefa tarefa);

    @DELETE("/tarefas/{id}")
    Call<Void> delete(@Path("id") String id);

    @POST("/tarefas")
    Call<Tarefa> post(@Body Tarefa tarefa);
}
