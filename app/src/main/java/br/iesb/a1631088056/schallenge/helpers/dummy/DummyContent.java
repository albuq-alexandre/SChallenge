package br.iesb.a1631088056.schallenge.helpers.dummy;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.iesb.a1631088056.schallenge.interfaces.GBensInventario;
import br.iesb.a1631088056.schallenge.models.Bem;
import br.iesb.a1631088056.schallenge.models.Bens;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        /*for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }*/
        buscarDados();
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.mCodBem, item);
    }

    private static DummyItem createDummyItem(Bem bem) {
        return new DummyItem(bem.getId(), bem.getPbms(), bem.getNomePbms(), bem.getCategoria());
    }


    private static void buscarDados() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        gsonBuilder.disableHtmlEscaping();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();


        Retrofit retrofit = new Retrofit.Builder()
              //  .baseUrl("https://develop.backendless.com/85EB22FD-CDF6-084E-FF56-1B215148FA00/console/pkvohvzblelqkxufsfegomiuxuakzjlgvwll/files/view/import/")
                .baseUrl("https://desafio-e5b4a.firebaseio.com/")
              //  .addConverterFactory(JacksonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GBensInventario service = retrofit.create(GBensInventario.class);

        Call<List<Bem>> bens = service.listarBens();

        bens.enqueue(new Callback<List<Bem>>() {
            @Override
            public void onResponse(Call<List<Bem>> call,
                                   Response<List<Bem>> response) {
                List<Bem> bens = response.body();

               for (Bem bem : bens) {
                    addItem(createDummyItem(bem));
                }
            }

            @Override
            public void onFailure(Call<List<Bem>> call,
                                  Throwable t) {
                Log.e("DUMMYCONTENT", t.getMessage());
            }
        });
    }


    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String mCodBem;
        public final String mPBMS;
        public final String mNomeBem;
        public final int mCategoryBem;

        public DummyItem(String id, String pPBMS, String pNomeBem, int mCategoryBem) {
            this.mCodBem = id;
            this.mPBMS = pPBMS;
            this.mNomeBem = pNomeBem;
            this.mCategoryBem = mCategoryBem;
        }

        @Override
        public String toString() {
            return mCodBem + " - " + mNomeBem;
        }
    }
}
