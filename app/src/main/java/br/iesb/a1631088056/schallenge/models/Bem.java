package br.iesb.a1631088056.schallenge.models;

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

public class Bem {
    @JsonProperty("categoria")
    private Integer categoria;
    @JsonProperty("id")
    private String id;
    @JsonProperty("inventariado")
    private Boolean inventariado;
    @JsonProperty("nome_pbms")
    private String nome_pbms;
    @JsonProperty("pbms")
    private String pbms;

    /**
     * No args constructor for use in serialization
     *
     */
    public Bem() {
    }

    /**
     *
     * @param pbms
     * @param id
     * @param categoria
     * @param inventariado
     * @param nomePbms
     */
    public Bem(Integer categoria, String id, Boolean inventariado, String nomePbms, String pbms) {
        super();
        this.categoria = categoria;
        this.id = id;
        this.inventariado = inventariado;
        this.nome_pbms = nomePbms;
        this.pbms = pbms;
    }

    @JsonProperty("categoria")
    public Integer getCategoria() {
        return categoria;
    }

    @JsonProperty("categoria")
    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("inventariado")
    public Boolean getInventariado() {
        return inventariado;
    }

    @JsonProperty("inventariado")
    public void setInventariado(Boolean inventariado) {
        this.inventariado = inventariado;
    }

    @JsonProperty("nome_pbms")
    public String getNomePbms() {
        return nome_pbms;
    }

    @JsonProperty("nome_pbms")
    public void setNomePbms(String nomePbms) {
        this.nome_pbms = nomePbms;
    }

    @JsonProperty("pbms")
    public String getPbms() {
        return pbms;
    }

    @JsonProperty("pbms")
    public void setPbms(String pbms) {
        this.pbms = pbms;
    }

}