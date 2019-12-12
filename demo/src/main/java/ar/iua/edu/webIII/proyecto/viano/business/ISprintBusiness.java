package ar.iua.edu.webIII.proyecto.viano.business;

import ar.iua.edu.webIII.proyecto.viano.business.exceptions.*;
import ar.iua.edu.webIII.proyecto.viano.model.Sprint;

import java.util.List;

public interface ISprintBusiness {
    public List<Sprint> list() throws BusinessException;
    public Sprint save(Sprint sprint) throws BusinessException, DateNullException, InvalidSprintException;
    public void remove(int id) throws BusinessException, NotFoundException;
    public Sprint findSprintById (int sprintId) throws NotFoundException, BusinessException;
    public void isValid(Sprint sprint) throws InvalidSprintException, DateNullException;
    public Sprint update(int idsprint, Sprint sprint) throws NotFoundException, BusinessException, InvalidSprintException;
}
