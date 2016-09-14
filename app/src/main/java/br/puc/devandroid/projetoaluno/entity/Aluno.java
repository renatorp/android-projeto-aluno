//-----------------------------------br.puc.devandroid.projetoaluno.entity.Aluno.java-----------------------------------

package br.puc.devandroid.projetoaluno.entity;

import java.util.Date;

public class Aluno {

    private String objectId;
    private String fotoUrl;
    private Integer idade;
    private String nome;
    private String telefone;
    private String endereco;
    private Date createdAt;
    private Date updatedAt;

    /**
     *
     * @return
     * The objectId
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     *
     * @param objectId
     * The objectId
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     *
     * @return
     * The fotoUrl
     */
    public String getFotoUrl() {
        return fotoUrl;
    }

    /**
     *
     * @param fotoUrl
     * The fotoUrl
     */
    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    /**
     *
     * @return
     * The idade
     */
    public Integer getIdade() {
        return idade;
    }

    /**
     *
     * @param idade
     * The idade
     */
    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    /**
     *
     * @return
     * The nome
     */
    public String getNome() {
        return nome;
    }

    /**
     *
     * @param nome
     * The nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     *
     * @return
     * The telefone
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     *
     * @param telefone
     * The telefone
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     *
     * @return
     * The endereco
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     *
     * @param endereco
     * The endereco
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The createdAt
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updatedAt
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        Aluno aluno = (Aluno)o;
        return this.getObjectId() != null && aluno.getObjectId() != null && aluno.getObjectId().equals(this.getObjectId());
    }
}