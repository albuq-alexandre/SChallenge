package br.iesb.a1631088056.schallenge.interfaces;

import java.util.List;
import java.util.Map;

import br.iesb.a1631088056.schallenge.models.Bem;
import br.iesb.a1631088056.schallenge.models.Bens;
import retrofit2.Call;
import retrofit2.http.GET;


public interface GBensInventario {
    @GET("Bens.json")
    //@GET("csvjson.json")

    //Call<Bem> listarBens();

    Call<List<Bem>> listarBens();

}
