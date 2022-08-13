package com.java.biblioteca.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A Livro.
 */
@Entity
@Table(name = "livro")
public class Livro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "autor")
    private String autor;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "data_criacao")
    private LocalDate dataCriacao;

    @Column(name = "ativo")
    private Long ativo;

    @Column(name = "id_usuario_cadastro")
    private Long idUsuarioCadastro;

    @Column(name = "proprietario")
    private String proprietario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Livro id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAutor() {
        return this.autor;
    }

    public Livro autor(String autor) {
        this.setAutor(autor);
        return this;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Livro titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getDataCriacao() {
        return this.dataCriacao;
    }

    public Livro dataCriacao(LocalDate dataCriacao) {
        this.setDataCriacao(dataCriacao);
        return this;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Long getAtivo() {
        return this.ativo;
    }

    public Livro ativo(Long ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Long ativo) {
        this.ativo = ativo;
    }

    public Long getIdUsuarioCadastro() {
        return this.idUsuarioCadastro;
    }

    public Livro idUsuarioCadastro(Long idUsuarioCadastro) {
        this.setIdUsuarioCadastro(idUsuarioCadastro);
        return this;
    }

    public void setIdUsuarioCadastro(Long idUsuarioCadastro) {
        this.idUsuarioCadastro = idUsuarioCadastro;
    }

    public String getProprietario() {
        return this.proprietario;
    }

    public Livro proprietario(String proprietario) {
        this.setProprietario(proprietario);
        return this;
    }

    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Livro)) {
            return false;
        }
        return id != null && id.equals(((Livro) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Livro{" +
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
