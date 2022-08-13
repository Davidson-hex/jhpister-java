package com.java.biblioteca.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.*;

/**
 * A Movimentos.
 */
@Entity
@Table(name = "movimentos")
public class Movimentos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "id_livro")
    private Integer idLivro;

    @Column(name = "locacao")
    private Integer locacao;

    @Column(name = "data_locacao")
    private LocalDate dataLocacao;

    @Column(name = "previsao_devolucao")
    private LocalDate previsaoDevolucao;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Column(name = "devolucao")
    private Integer devolucao;

    @Column(name = "status")
    private String status;

    @Column(name = "proprietario")
    private String proprietario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Movimentos() {
        this.devolucao = 1;
    }

    public Long getId() {
        return this.id;
    }

    public Movimentos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return this.idUsuario;
    }

    public Movimentos idUsuario(Integer idUsuario) {
        this.setIdUsuario(idUsuario);
        return this;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdLivro() {
        return this.idLivro;
    }

    public Movimentos idLivro(Integer idLivro) {
        this.setIdLivro(idLivro);
        return this;
    }

    public void setIdLivro(Integer idLivro) {
        this.idLivro = idLivro;
    }

    public Integer getLocacao() {
        return this.locacao;
    }

    public Movimentos locacao(Integer locacao) {
        this.setLocacao(locacao);
        return this;
    }

    public void setLocacao(Integer locacao) {
        this.locacao = locacao;
    }

    public LocalDate getDataLocacao() {
        return this.dataLocacao;
    }

    public Movimentos dataLocacao(LocalDate dataLocacao) {
        this.setDataLocacao(dataLocacao);
        return this;
    }

    public void setDataLocacao(LocalDate dataLocacao) {
        this.dataLocacao = dataLocacao;
    }

    public LocalDate getPrevisaoDevolucao() {
        return this.previsaoDevolucao;
    }

    public Movimentos previsaoDevolucao(LocalDate previsaoDevolucao) {
        this.setPrevisaoDevolucao(previsaoDevolucao);
        return this;
    }

    public void setPrevisaoDevolucao(LocalDate previsaoDevolucao) {
        this.previsaoDevolucao = previsaoDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return this.dataDevolucao;
    }

    public Movimentos dataDevolucao(LocalDate dataDevolucao) {
        this.setDataDevolucao(dataDevolucao);
        return this;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public Integer getDevolucao() {
        return this.devolucao;
    }

    public Movimentos devolucao(Integer devolucao) {
        this.setDevolucao(devolucao);
        return this;
    }

    public void setDevolucao(Integer devolucao) {
        this.devolucao = devolucao;
    }

    public String getStatus() {
        return this.status;
    }

    public Movimentos status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProprietario() {
        return this.proprietario;
    }

    public Movimentos proprietario(String proprietario) {
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
        if (!(o instanceof Movimentos)) {
            return false;
        }
        return id != null && id.equals(((Movimentos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Movimentos{" +
            "id=" + getId() +
            ", idUsuario=" + getIdUsuario() +
            ", idLivro=" + getIdLivro() +
            ", locacao=" + getLocacao() +
            ", dataLocacao='" + getDataLocacao() + "'" +
            ", previsaoDevolucao='" + getPrevisaoDevolucao() + "'" +
            ", dataDevolucao='" + getDataDevolucao() + "'" +
            ", devolucao=" + getDevolucao() +
            ", status='" + getStatus() + "'" +
            ", proprietario='" + getProprietario() + "'" +
            "}";
    }
}
