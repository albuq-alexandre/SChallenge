package br.iesb.a1631088056.schallenge.models;

import java.util.List;

public class Usuario {

    private String id, nome, email, avatarURL;
    private List<String> inventariadas;

    public Usuario(String id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public List<String> getInventariadas() {
        return inventariadas;
    }

    public void addInventariadas(String inventariada) {
        this.inventariadas.add(inventariada);
    }
}
