package br.iesb.a1631088056.schallenge.models;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "categoria",
        "id",
        "inventariado",
        "nome_pbms",
        "pbms"
})



public class RepositorioJSON {
    @JsonProperty("categoria")
    private Integer categoria;
    @JsonProperty("id")
    private String id;
    @JsonProperty("inventariado")
    private boolean inventariado;
    @JsonProperty("nome_pbms")
    private String nome_pbms;
    @JsonProperty("pbms")
    private String pbms;


//    private List<Item> items = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();



    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}