package ar.iua.edu.webIII.proyecto.viano.business.implementation;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import ar.iua.edu.webIII.proyecto.viano.business.exceptions.*;
import ar.iua.edu.webIII.proyecto.viano.business.ITaskBusiness;

import ar.iua.edu.webIII.proyecto.viano.model.Sprint;
import ar.iua.edu.webIII.proyecto.viano.model.SprintList;
import ar.iua.edu.webIII.proyecto.viano.model.Task;
import ar.iua.edu.webIII.proyecto.viano.persistence.ListRepository;
import ar.iua.edu.webIII.proyecto.viano.persistence.SprintRepository;
import ar.iua.edu.webIII.proyecto.viano.persistence.TaskRepository;
import ar.iua.edu.webIII.proyecto.viano.web.Constantes;
import org.aspectj.weaver.ast.Not;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskBusiness implements ITaskBusiness {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TaskRepository taskDao;

    @Autowired
    private ListRepository listDao;

    @Autowired
    private SprintRepository sprintDao;

    @Override
    public List<Task> list() throws BusinessException {
        try {
            return taskDao.findAll();
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public Task save(Task task) throws BusinessException, InvalidPriorityException, DateNullException, AssignNotAllowedException, ListDoesNotExistException {
        Optional<Task> op = null;
        Optional<SprintList> opList = null;
        //op = taskDao.findOneByName(task.getName());
        opList = listDao.findById(task.getListName().getId());

        if (!opList.isPresent()) {
            throw new ListDoesNotExistException("La lista con id " + task.getListName().getId() + " no existe");
        }
        if (!opList.get().getName().equalsIgnoreCase(Constantes.BACKLOG_LIST)) {
            throw new AssignNotAllowedException("La tarea solo puede estar asignada a la lista " + Constantes.BACKLOG_LIST);
        }
        if (task.getCreation() == null) {
            throw new DateNullException("La tarea debe tener asignada una fecha de creacion");
        }
        if (!task.getPriority().equalsIgnoreCase(Constantes.MEDIUM_PRIORITY) && !task.getPriority().equalsIgnoreCase(Constantes.HIGH_PRIORITY) &&
                !task.getPriority().equalsIgnoreCase(Constantes.LOW_PRIORITY)) {
            throw new InvalidPriorityException("La tarea debe tener asignada una de las siguientes prioridades : " + Constantes.LOW_PRIORITY + ", " + Constantes.MEDIUM_PRIORITY + ", " + Constantes.HIGH_PRIORITY);
        }
        try {
            task = taskDao.save(task);
            log.info("se creo la tarea " + task);
            return task;
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public void remove(int idTask) throws BusinessException, NotFoundException {
        Optional<Task> op = null;

        try {
            op = taskDao.findById(idTask);
        } catch (Exception e) {
            throw new BusinessException(e);
        }

        if (!op.isPresent())
            throw new NotFoundException("No se encuentra la tarea con id =" + idTask);
        try {
            taskDao.deleteById(op.get().getId());
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public Task moveTask(int idTask, int idList) throws BusinessException {
        Optional<Task> op = null;
        Optional<SprintList> opList = null;
        op = taskDao.findById(idTask);
        opList = listDao.findById(idList);

        try {
            op.get().setListName(opList.get());
            return taskDao.save(op.get());
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public Task update(int id, Task task) throws BusinessException, CannotMoveException {
        Optional<Task> op = null;
        Optional<SprintList> opList = null;
        op = taskDao.findById(id);
        opList = listDao.findById(task.getListName().getId());
        // si la lista a la que pertenece la tarea es diferente, tiro un error
        if (!op.get().getListName().getName().equalsIgnoreCase(opList.get().getName())) {
            log.error("La tarea no se puede mover de la lista \n Para eso utilizar el endpoint " + Constantes.URL_BASE_TASKS + "/move/{param}");
            throw new CannotMoveException("La tarea no se puede mover de la lista \n Para eso utilizar el endpoint " + Constantes.URL_BASE_TASKS + "/move/{param}");
        }

        try {
            task.setId(id);
            return taskDao.save(task);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public Task findOneById(int idTarea) throws BusinessException, NotFoundException {
        Optional<Task> op = null;
        try {
            op = taskDao.findById(idTarea);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        if (!op.isPresent()) {
            throw new NotFoundException("No hay ninguna tarea de id= " + idTarea);
        }
        return op.get();
    }


    @Override
    public List<Task> findAllBySprintOrderByPriority(int id, String order) throws BusinessException, NotFoundException {
        Optional<List<Task>> op = null;
        Optional<Sprint> opSprint = sprintDao.findById(id);
        if (!opSprint.isPresent()) {
            log.error("No hay ningun sprint con el id " + id);
            throw new NotFoundException("No hay ningun sprint con el id " + id);
        }
        try {
            if (order == null || order.equalsIgnoreCase("asc"))
                op = taskDao.findAllByListNameSprintIdOrderByPriorityAsc(id);
            else
                op = taskDao.findAllByListNameSprintIdOrderByPriorityDesc(id);

            return op.get();
        } catch (Exception e) {
            throw new BusinessException(e);
        }

    }

    @Override
    public List<Task> findAllBySprintOrderByCreation(int id, String order) throws BusinessException, NotFoundException {
        Optional<List<Task>> op = null;

        Optional<Sprint> opSprint = sprintDao.findById(id);
        if (!opSprint.isPresent()) {
            log.error("No hay ningun sprint con el id " + id);
            throw new NotFoundException("No hay ningun sprint con el id " + id);
        }

        try {
            if (order == null || order.equalsIgnoreCase("asc"))
                op = taskDao.findAllByListNameSprintIdOrderByCreationAsc(id);
            else
                op = taskDao.findAllByListNameSprintIdOrderByCreationDesc(id);

            return op.get();
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public List<Task> getAllBySprintAndEstimation(int id, int estimation) throws BusinessException, NotFoundException {
        Optional<List<Task>> op = null;
        Optional<Sprint> opSprint = null;
        opSprint = sprintDao.findById(id);
        if (!opSprint.isPresent()) {
            log.error("No hay ningun sprint con el id " + id);
            throw new NotFoundException("No hay ningun sprint con el id " + id);
        }
        try {
            op = taskDao.findAllByListNameSprintIdAndEstimation(id, estimation);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        if (!op.isPresent()) {
            throw new NotFoundException("No hay ninguna tarea con estimacion= " + estimation);
        }
        return op.get();
    }

    @Override
    public List<Task> getAllByListIdAndSprintNameOrderByCreation(int listId, String nameSprint, String order) throws BusinessException, NotFoundException {
        Optional<List<Task>> op = null;
        try {
            if (order == null || order.equalsIgnoreCase("asc"))
                op = taskDao.findAllByListNameIdAndListNameSprintNameOrderByCreationAsc(listId, nameSprint);
            else
                op = taskDao.findAllByListNameIdAndListNameSprintNameOrderByCreationDesc(listId, nameSprint);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException(e);
        }
        if (!op.isPresent()) {
            log.error("No hay ninguna tarea perteneciente a la lista con id = " + listId + " en el sprint " + nameSprint);
            throw new NotFoundException("No hay ninguna tarea perteneciente a la lista con id = " + listId + " en el sprint " + nameSprint);
        }
        return op.get();
    }

    @Override
    public List<Task> getAllByListIdAndSprintNameOrderByPriority(int listId, String nameSprint, String order) throws BusinessException, NotFoundException {
        Optional<List<Task>> op = null;
        Optional<Sprint> opSprint = null;
        opSprint = sprintDao.findByName(nameSprint);
        if (!opSprint.isPresent()) {
            log.error("No hay ningun sprint con el nombre" + nameSprint);
            throw new NotFoundException("No hay ningun sprint con el nombre" + nameSprint);
        }
        try {
            if (order == null || order.equalsIgnoreCase("asc"))
                op = taskDao.findAllByListNameIdAndListNameSprintNameOrderByPriorityAsc(listId, nameSprint);
            else
                op = taskDao.findAllByListNameIdAndListNameSprintNameOrderByPriorityDesc(listId, nameSprint);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        if (!op.isPresent()) {
            log.error("No hay ninguna tarea en el sprint " + nameSprint + " perteneciente a la lista  con id = " + listId);
            throw new NotFoundException("No hay ninguna tarea en el sprint " + nameSprint + " perteneciente a la lista  con id = " + listId);
        }
        return op.get();
    }

    @Override
    public List<Task> getAllByListNameId(int listId) throws BusinessException, NotFoundException {
        Optional<List<Task>> op = null;
        Optional<SprintList> sprintList = null;
        sprintList = listDao.findById(listId);
        if (!sprintList.isPresent()) {
            throw new NotFoundException("No hay ninguna lista con id = " + listId);
        }
        try {
            op = taskDao.findAllByListNameId(listId);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        if (!op.isPresent()) {
            throw new NotFoundException("No hay ninguna tarea perteneciente a la lista con id = " + listId);
        }
        return op.get();
    }

    @Override
    public void isValid(Task task) throws InvalidPriorityException, DateNullException, InvalidNameException, InvalidTaskException {
        if (task.getName() == null) {
            log.error("El nombre es null");
            throw new InvalidNameException("El nombre de la tarea no puede ser null");
        }
        if (task.getPriority() == null) {
            log.error("La prioridad es null");
            throw new InvalidPriorityException("La prioridad no puede ser null.\n Solo se aceptan estas prioridades " +
                    Constantes.LOW_PRIORITY + ", " + Constantes.MEDIUM_PRIORITY + ", " + Constantes.HIGH_PRIORITY);
        }

        if (!task.getPriority().equalsIgnoreCase(Constantes.LOW_PRIORITY) && !task.getPriority().equalsIgnoreCase(Constantes.MEDIUM_PRIORITY) && !task.getPriority().equalsIgnoreCase(Constantes.HIGH_PRIORITY)) {
            log.error("La prioridad " + task.getPriority() + "no es correcta");
            throw new InvalidPriorityException("La prioridad " + task.getPriority() + "no es correcta\n Solo se aceptan estas prioridades " +
                    Constantes.LOW_PRIORITY + ", " + Constantes.MEDIUM_PRIORITY + ", " + Constantes.HIGH_PRIORITY);
        }
        if (task.getCreation() == null || task.getModification() == null) {
            log.error("La tarea debe tener asignada una fecha de creacion o de modificacion");
            throw new DateNullException("La tarea debe tener asignada una fecha de creacion o de modificacion");
        }
        if (task.getListName() == null) {
            log.error("La tarea no esta asociada a una lista");
            throw new InvalidTaskException("La tarea debe estar asociada a una lista");
        }
        if (task.getListName().getId() == null) {
            log.error("La lista a la que esta asociada la tarea no tiene un id");
            throw new InvalidTaskException("La lista a la que esta asociada la tarea no tiene un id");
        }
    }

    @Override
    public void canMove(int idTask, int idList) throws BusinessException, CannotMoveException, InvalidEstimationException, NotFoundException, DateNullException, InvalidTaskException, InvalidNameException, InvalidPriorityException {

        Optional<Task> op = null;
        Optional<SprintList> opList = null;
        try {
            op = taskDao.findById(idTask);
            opList = listDao.findById(idList);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        if (!op.isPresent()) {
            log.error("La tarea con id " + idTask + " no existe");
            throw new NotFoundException("La tarea con id " + idTask + " no existe");
        }
        if (!opList.isPresent()) {
            log.error("La lista con id " + idList + " no existe");
            throw new NotFoundException("La lista con id " + idList + " no existe");
        }
        isValid(op.get());
        if (op.get().getEstimation() == null) {
            log.error("La tarea no tiene establecida una estimacion");
            throw new InvalidEstimationException("La tarea debe tener una estimacion");
        }
        if ((op.get().getListName().getName().equalsIgnoreCase(Constantes.BACKLOG_LIST) && !opList.get().getName().equalsIgnoreCase(Constantes.TODO_LIST)) ||
                (op.get().getListName().getName().equalsIgnoreCase(Constantes.DONE_LIST))) {
            log.error("La tarea no se puede mover de la lista " + op.get().getListName().getName() +
                    " a la lista " + opList.get().getName());
            throw new CannotMoveException("La tarea no se puede mover de la lista " + op.get().getListName().getName() +
                    " a la lista " + opList.get().getName());
        }

    }


}
