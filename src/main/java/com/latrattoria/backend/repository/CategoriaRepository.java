package com.latrattoria.backend.repository;

import com.latrattoria.backend.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    boolean existsByNombre(Categoria.NombreCategoria nombre);
}
