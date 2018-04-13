package br.iesb.a1631088056.schallenge.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Usuario {


    public String firebaseUid, id, nome, email, avatarURL;
//    public Map <String, Object> inventariadas;

    public Usuario() {
        // construtor padrão requerido
    }

    public Usuario(String firebaseUid, String mnome, String email) {
        this.firebaseUid = firebaseUid;
        this.nome = mnome;
        this.email = email;

    }

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
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

/*
    // TODO: Implementar a Classe Inventariadas
    public Map<String, Object> getInventariadas() {
        return inventariadas;
    }

    public void addInventariadas(String inventariada) {
        this.inventariadas.put("Inventariada", inventariada);
    }
    */



}