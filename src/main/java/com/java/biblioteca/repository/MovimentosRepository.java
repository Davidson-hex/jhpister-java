package com.java.biblioteca.repository;

import com.java.biblioteca.domain.Movimentos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Movimentos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MovimentosRepository extends JpaRepository<Movimentos, Long> {}
