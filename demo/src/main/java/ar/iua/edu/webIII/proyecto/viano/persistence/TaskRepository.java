package ar.iua.edu.webIII.proyecto.viano.persistence;


import ar.iua.edu.webIII.proyecto.viano.model.SprintList;
import ar.iua.edu.webIII.proyecto.viano.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findOneByName(String name);

    Optional<List<Task>> findAllByListNameSprintIdOrderByPriorityAsc(int sprintId);

    Optional<List<Task>> findAllByListNameSprintIdOrderByPriorityDesc(int sprintId);

    Optional<List<Task>> findAllByListNameSprintIdOrderByCreationAsc(int sprintId);

    Optional<List<Task>> findAllByListNameSprintIdOrderByCreationDesc(int sprintId);

    Optional<List<Task>> findAllByListNameSprintIdAndEstimation(int sprintId, int estimation);

    Optional<List<Task>> findAllByListNameIdAndListNameSprintNameOrderByCreationAsc(int idList, String name);
    Optional<List<Task>> findAllByListNameIdAndListNameSprintNameOrderByCreationDesc(int idList, String name);

    Optional<List<Task>> findAllByListNameIdAndListNameSprintNameOrderByPriorityAsc(int idList, String name);
    Optional<List<Task>> findAllByListNameIdAndListNameSprintNameOrderByPriorityDesc(int idList, String name);

    Optional<List<Task>> findAllByListNameId(int idList);
}
