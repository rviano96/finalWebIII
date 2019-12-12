package ar.iua.edu.webIII.proyecto.viano.business.implementation;

import ar.iua.edu.webIII.proyecto.viano.business.ISprintBusiness;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.BusinessException;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.DateNullException;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.InvalidSprintException;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.NotFoundException;
import ar.iua.edu.webIII.proyecto.viano.model.Sprint;
import ar.iua.edu.webIII.proyecto.viano.persistence.SprintRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SprintBusiness implements ISprintBusiness {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SprintRepository sprintDao;

    @Override
    public List<Sprint> list() throws BusinessException {
        try {
            return sprintDao.findAll();
        } catch (Exception e) {
            log.error("error al obtener todos los sprints" + e.getMessage());
            throw new BusinessException(e);
        }
    }

    @Override
    public Sprint save(Sprint sprint) throws BusinessException, DateNullException, InvalidSprintException {
        Optional<Sprint> op = null;
        op = sprintDao.findByName(sprint.getName());
        if (op.isPresent()) {
            log.error("Error al crear sprint \n El sprint con el nombre " + sprint.getName() + " ya existe");
            throw new InvalidSprintException("El sprint con el nombre " + sprint.getName() + " ya existe");
        }
        try {
            sprint = sprintDao.save(sprint);
            log.info("Se creo el sprint " + sprint);
            return sprint;
        } catch (Exception e) {
            log.error("Error al crear sprint " + e.getMessage());
            throw new BusinessException(e);
        }
    }

    @Override
    public void remove(int id) throws BusinessException, NotFoundException {
        Optional<Sprint> op = null;
        op = sprintDao.findById(id);
        if (!op.isPresent()) {
            log.error("No se ecuentra el sprint con id= " + id);
            throw new NotFoundException("No se ecuentra el sprint con id= " + id);
        }
        try {
            sprintDao.deleteById(id);
        } catch (Exception e) {
            log.error("Error al eliminar el sprint " + e.getMessage());
            throw new BusinessException(e);
        }
    }

    @Override
    public Sprint findSprintById(int sprintId) throws NotFoundException, BusinessException {
        Optional<Sprint> op = null;
        op = sprintDao.findById(sprintId);
        if (!op.isPresent()) {
            log.error("No se ecuentra el sprint con id= " + sprintId);
            throw new NotFoundException("No se ecuentra el sprint con id= " + sprintId);
        }
        try {
            return op.get();
        } catch (Exception e) {
            log.error("Error al eliminar el sprint " + e.getMessage());
            throw new BusinessException(e);
        }
    }


    @Override
    public void isValid(Sprint sprint) throws InvalidSprintException, DateNullException {
        if (sprint.getName() == null) {
            log.error("El nombre del sprint no puede ser null.");
            throw new InvalidSprintException("El nombre del sprint no puede ser null.");
        }

        if (sprint.getStartDate() == null) {
            log.error("La fecha de comienzo no puede ser null");


            throw new DateNullException("La fecha de comienzo no puede ser null");

        }
        /*if (sprint.getStartDate().before(new Date())) {
            log.error("El comienzo del sprint debe ser despues o igual a la fecha actual\"");
            throw new InvalidSprintException("El comienzo del sprint debe ser despues o igual a la fecha actual");
        }*/

    }

    @Override
    public Sprint update(int idSprint, Sprint sprint) throws
            NotFoundException, BusinessException, InvalidSprintException {
        Optional<Sprint> op = null;
        op = sprintDao.findById(idSprint);
        if (!op.isPresent()) {
            log.error("No se ecuentra el sprint con id= " + idSprint);
            throw new NotFoundException("No se ecuentra el sprint con id= " + idSprint);
        }
        if (!op.get().getName().equalsIgnoreCase(sprint.getName())) {
            log.error("Error al actualizar el sprint \n El nombre de sprint no se puede modificar ");
            throw new InvalidSprintException("Error al actualizar el sprint \n El nombre de sprint no se puede modificar ");
        }
        try {
            sprint = sprintDao.save(sprint);
            log.info("Se creo el sprint " + sprint);
            return sprint;
        } catch (Exception e) {
            log.error("Error al crear sprint " + e.getMessage());
            throw new BusinessException(e);
        }
    }
}
