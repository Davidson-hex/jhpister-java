package com.java.biblioteca.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.java.biblioteca.domain.Locar} entity.
 */
public class LocarDTO implements Serializable {

    private Long id;

    private Integer idUsuario;

    private Integer idLivro;

    private Integer locacao;

    private LocalDate dataLocacao;

    private LocalDate previsaoDevolucao;

    private LocalDate dataDevolucao;

    private Integer devolucao;

    private String status;

    private String proprietario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(Integer idLivro) {
        this.idLivro = idLivro;
    }

    public Integer getLocacao() {
        return locacao;
    }

    public void setLocacao(Integer locacao) {
        this.locacao = locacao;
    }

    public LocalDate getDataLocacao() {
        return dataLocacao;
    }

    public void setDataLocacao(LocalDate dataLocacao) {
        this.dataLocacao = dataLocacao;
    }

    public LocalDate getPrevisaoDevolucao() {
        return previsaoDevolucao;
    }

    public void setPrevisaoDevolucao(LocalDate previsaoDevolucao) {
        this.previsaoDevolucao = previsaoDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public Integer getDevolucao() {
        return devolucao;
    }

    public void setDevolucao(Integer devolucao) {
        this.devolucao = devolucao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        if (!(o instanceof LocarDTO)) {
            return false;
        }

        LocarDTO locarDTO = (LocarDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, locarDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocarDTO{" +
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
