package ar.iua.edu.webIII.proyecto.viano.business;

import java.util.List;


import ar.iua.edu.webIII.proyecto.viano.business.exceptions.*;
import ar.iua.edu.webIII.proyecto.viano.model.SprintList;


public interface IListBusiness {

    // La lista puede no existir -> NotFoundException
    public SprintList getListById(int idList) throws BusinessException, NotFoundException;
    // El nombre de la lista puede ser invalido -> InvalidNameException
    // La lista puede ya estar creada -> ListAlreadyExistsException
    public SprintList save(SprintList sprintList) throws BusinessException, ListAlreadyExistsException, NotFoundException;
    // La lista puede no existir NotFoundException
    public void delete(int id) throws BusinessException, NotFoundException;
    public List<SprintList> list() throws BusinessException;
    public List<SprintList> getListsBySprint(int idSprint) throws BusinessException, NotFoundException;
    public SprintList getListByNameAndSprintId(String name, int idSprint) throws BusinessException, NotFoundException;
    public void isValid(SprintList sprintList) throws InvalidNameException, InvalidSprintException;
}
