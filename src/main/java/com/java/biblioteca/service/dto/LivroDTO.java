package com.java.biblioteca.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.java.biblioteca.domain.Livro} entity.
 */
public class LivroDTO implements Serializable {

    private Long id;

    private String autor;

    private String titulo;

    private LocalDate dataCriacao;

    private Long ativo;

    private Long idUsuarioCadastro;

    private String proprietario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Long getAtivo() {
        return ativo;
    }

    public void setAtivo(Long ativo) {
        this.ativo = ativo;
    }

    public Long getIdUsuarioCadastro() {
        return idUsuarioCadastro;
    }

    public void setIdUsuarioCadastro(Long idUsuarioCadastro) {
        this.idUsuarioCadastro = idUsuarioCadastro;
    }

    public String getProprietario() {
        return proprietario;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LivroDTO)) {
            return false;
        }

        LivroDTO livroDTO = (LivroDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, livroDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LivroDTO{" +
            "id=" + getId() +
            ", autor='" + getAutor() + "'" +
            ", titulo='" + getTitulo() + "'" +
            ", dataCriacao='" + getDataCriacao() + "'" +
            ", ativo=" + getAtivo() +
            ", idUsuarioCadastro=" + getIdUsuarioCadastro() +
            ", proprietario='" + getProprietario() + "'" +
            "}";
    }
}
