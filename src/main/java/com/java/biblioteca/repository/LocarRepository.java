package com.java.biblioteca.repository;

import com.java.biblioteca.domain.Locar;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Locar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocarRepository extends JpaRepository<Locar, Long> {

    public Optional<Locar> findByIdLivro(Long idLivro);
    public Optional<Locar> findByProprietario(String user);

    public boolean existsByIdLivro(Long id);

    public boolean existsByIdUsuario(Long id);

    public void deleteByIdUsuario(Long id);
}
