package br.puc.devandroid.projetoaluno.service;

import br.puc.devandroid.projetoaluno.entity.Aluno;
import br.puc.devandroid.projetoaluno.entity.AlunosResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by renatorp on 07/09/16.
 */
public interface APIService {
    String BASE_URL  = "https://parseapi.back4app.com/classes/";

    @Headers({"X-Parse-Application-Id: FWmmldOSRF8GE7jR8424Ex9Tu2ZHLTrggQHLJvjY", "X-Parse-REST-API-Key: RegHHKDEd3qf260q0mGUM7Z7GMsWry79eKsv3Jic"})
    @GET("Aluno")
    Call<AlunosResult> findAlunos();

    @Headers({"X-Parse-Application-Id: FWmmldOSRF8GE7jR8424Ex9Tu2ZHLTrggQHLJvjY", "X-Parse-REST-API-Key: RegHHKDEd3qf260q0mGUM7Z7GMsWry79eKsv3Jic"})
    @DELETE("Aluno/{id}")
    Call<Void> deleteAluno(@Path("id") String id);

    @Headers({"X-Parse-Application-Id: FWmmldOSRF8GE7jR8424Ex9Tu2ZHLTrggQHLJvjY", "X-Parse-REST-API-Key: RegHHKDEd3qf260q0mGUM7Z7GMsWry79eKsv3Jic"})
    @POST("Aluno")
    Call<Aluno> createAluno(@Body Aluno aluno);
}
