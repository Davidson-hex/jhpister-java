package com.java.biblioteca.repository;

import com.java.biblioteca.domain.Livro;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Livro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    public boolean existsById(Long id);
}
