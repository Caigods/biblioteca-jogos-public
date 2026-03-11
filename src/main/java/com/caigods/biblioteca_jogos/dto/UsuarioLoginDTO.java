package com.caigods.biblioteca_jogos.dto;

public class UsuarioLoginDTO {
    private String email;
    private String senha;

    public UsuarioLoginDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public UsuarioLoginDTO() {

    }


    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




}
