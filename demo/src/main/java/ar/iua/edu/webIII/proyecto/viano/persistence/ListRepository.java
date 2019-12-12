package ar.iua.edu.webIII.proyecto.viano.persistence;


import ar.iua.edu.webIII.proyecto.viano.model.SprintList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListRepository extends JpaRepository<SprintList, Integer> {
     Optional<List<SprintList>> findBySprintId(int idSprint);
     Optional<SprintList> findByNameAndSprintId(String name, int idSprint);

}
