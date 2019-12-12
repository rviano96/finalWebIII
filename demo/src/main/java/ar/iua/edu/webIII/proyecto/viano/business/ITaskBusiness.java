package ar.iua.edu.webIII.proyecto.viano.business;

import java.util.List;

import ar.iua.edu.webIII.proyecto.viano.business.exceptions.*;
import ar.iua.edu.webIII.proyecto.viano.model.SprintList;
import ar.iua.edu.webIII.proyecto.viano.model.Task;

public interface ITaskBusiness {

	public List<Task> list() throws BusinessException;
	public Task save(Task task) throws BusinessException, InvalidPriorityException, DateNullException, AssignNotAllowedException, ListDoesNotExistException;
	public void remove(int id) throws BusinessException, NotFoundException;
	// update se usa para cambiar datos de las tareas( estimacion, descripcion y prioridad) , pero no para moverlas
	public Task update(int id, Task task) throws BusinessException, InvalidPriorityException, DateNullException, InvalidEstimationException, CannotMoveException, NotFoundException;
	public Task moveTask(int id, int idList) throws BusinessException;
	public Task findOneById(int idTarea) throws BusinessException, NotFoundException;
	public List<Task> findAllBySprintOrderByPriority(int id, String order) throws BusinessException, NotFoundException;
	public List<Task> findAllBySprintOrderByCreation(int id, String order) throws BusinessException, NotFoundException;
	public List<Task> getAllBySprintAndEstimation(int id, int estimation) throws BusinessException, NotFoundException;
	public List<Task> getAllByListIdAndSprintNameOrderByCreation(int idList, String sprintName, String order) throws BusinessException, NotFoundException;
	public List<Task> getAllByListIdAndSprintNameOrderByPriority(int idList,  String sprintName,String order) throws BusinessException, NotFoundException;
	public List<Task> getAllByListNameId(int idList) throws BusinessException, NotFoundException;
	public void isValid(Task task) throws InvalidPriorityException, DateNullException, InvalidNameException, InvalidTaskException;
	public void canMove(int idTask, int idList) throws BusinessException, InvalidEstimationException, CannotMoveException, NotFoundException, DateNullException, InvalidTaskException, InvalidNameException, InvalidPriorityException;

}
