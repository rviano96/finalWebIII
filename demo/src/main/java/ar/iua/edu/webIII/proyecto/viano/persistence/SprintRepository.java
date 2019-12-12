package ar.iua.edu.webIII.proyecto.viano.persistence;


import ar.iua.edu.webIII.proyecto.viano.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Integer> {
    Optional <Sprint> findByName(String name);
}
