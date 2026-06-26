package com.gimnasio.gimnasio.repository;

import com.gimnasio.gimnasio.domain.Servicio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

    public List<Servicio> findByCategoria_Id(Integer idCategoria);
}