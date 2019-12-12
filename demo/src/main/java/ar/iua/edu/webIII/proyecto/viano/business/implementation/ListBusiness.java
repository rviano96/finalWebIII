package ar.iua.edu.webIII.proyecto.viano.business.implementation;

import java.util.List;
import java.util.Optional;

import ar.iua.edu.webIII.proyecto.viano.business.exceptions.*;
import ar.iua.edu.webIII.proyecto.viano.business.IListBusiness;
import ar.iua.edu.webIII.proyecto.viano.model.Sprint;
import ar.iua.edu.webIII.proyecto.viano.model.SprintList;
import ar.iua.edu.webIII.proyecto.viano.persistence.SprintRepository;
import ar.iua.edu.webIII.proyecto.viano.web.Constantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import ar.iua.edu.webIII.proyecto.viano.persistence.ListRepository;

@Service
public class ListBusiness implements IListBusiness {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ListRepository listDao;
    @Autowired
    private SprintRepository sprintDao;

    @Override
    public SprintList getListById(int idList) throws BusinessException, NotFoundException {
        Optional<SprintList> op = null;
        try {
            op = listDao.findById(idList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
        if (!op.isPresent()) {
            log.error("No se encuentra la lista con id =" + idList);
            throw new NotFoundException("No se encuentra la lista con id =" + idList);
        }

        return op.get();

    }

    @Override
    public SprintList save(SprintList sprintList) throws BusinessException, ListAlreadyExistsException, NotFoundException {
        Optional<SprintList> op = null;
        Optional<Sprint> opSprint = null;
        opSprint = sprintDao.findById(sprintList.getSprint().getId());
        if (!opSprint.isPresent()) {
            log.error("El sprint con id " + sprintList.getSprint().getId() + "no existe");
            throw new NotFoundException("El sprint con id " + sprintList.getSprint().getId() + "no existe");
        }
        op = listDao.findByNameAndSprintId(sprintList.getName(), sprintList.getSprint().getId());
        if (op.isPresent()) {
            log.error("La lista de nombre " + sprintList.getName() + " en el sprint " + opSprint.get().getName() + " ya existe");
            throw new ListAlreadyExistsException("La lista de nombre " + sprintList.getName() + " en el sprint " + opSprint.get().getName() + " ya existe");
        }
        try {
            return listDao.save(sprintList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }


    @Override
    public void delete(int id) throws BusinessException, NotFoundException {
        Optional<SprintList> op;

        try {
            op = listDao.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }

        if (!op.isPresent()) {
            log.error("No se encuentra la lista con id =" + id);
            throw new NotFoundException("No se encuentra la lista con id =" + id);
        }

        try {
            listDao.deleteById(op.get().getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }

    }

    @Override
    public List<SprintList> list() throws BusinessException {
        try {
            return listDao.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }

    @Override
    public List<SprintList> getListsBySprint(int idSprint) throws BusinessException, NotFoundException {
        Optional<Sprint> opSprint = null;
        Optional<List<SprintList>> op = null;
        opSprint = sprintDao.findById(idSprint);
        if (!opSprint.isPresent()) {
            log.error("No se encuentra el sprint con id =" + idSprint);
            throw new NotFoundException("No se encuentra el sprint con id =" + idSprint);
        }
        try {
            op = listDao.findBySprintId(idSprint);
            if (!op.isPresent()) {
                log.error("No hay ninguna lista que petenezca al sprint con id =" + idSprint);
                throw new NotFoundException("No hay ninguna lista que petenezca al sprint con id =" + idSprint);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        return op.get();
    }

    @Override
    public SprintList getListByNameAndSprintId(String name, int idSprint) throws BusinessException, NotFoundException {
        Optional<Sprint> opSprint = null;
        Optional<SprintList> op = null;
        opSprint = sprintDao.findById(idSprint);
        if (!opSprint.isPresent()) {
            log.error("No se encuentra el sprint con id =" + idSprint);
            throw new NotFoundException("No se encuentra el sprint con id =" + idSprint);
        }
        try {
            op = listDao.findByNameAndSprintId(name, idSprint);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        if (!op.isPresent()) {
            log.error("No hay ninguna lista de nombre " + name + " que petenezca al sprint con id =" + idSprint);
            throw new NotFoundException("No hay ninguna lista de nombre " + name + " que petenezca al sprint con id =" + idSprint);
        }
        return op.get();
    }

    @Override
    public void isValid(SprintList sprintList) throws InvalidNameException, InvalidSprintException {
        if (sprintList.getName() == null) {
            log.error("El nombre de la lista es null");
            throw new InvalidNameException("El nombre de la lista no puede ser null");
        }
        if (!sprintList.getName().equalsIgnoreCase(Constantes.BACKLOG_LIST) && !sprintList.getName().equalsIgnoreCase(Constantes.TODO_LIST) && !sprintList.getName().equalsIgnoreCase(Constantes.DONE_LIST) &&
                !sprintList.getName().equalsIgnoreCase(Constantes.IN_PROGRESS_LIST) && !sprintList.getName().equalsIgnoreCase(Constantes.WAITING_LIST)) {
            log.error("El nombre " + sprintList.getName() + " no es un nombre valido");
            throw new InvalidNameException("El nombre " + sprintList.getName() + " no es un nombre valido\n Solo se soportan los nombres: " + Constantes.BACKLOG_LIST + "," +
                    Constantes.TODO_LIST + "," + Constantes.IN_PROGRESS_LIST + "," + Constantes.DONE_LIST + "," + Constantes.WAITING_LIST);
        }
        if (sprintList.getSprint() == null) {
            log.error("La lista debe estar asociada a un sprint");
            throw new InvalidSprintException("La lista debe estar asociada a un sprint");
        }
        if (sprintList.getSprint().getId() == null) {
            log.error("El id del sprint es null");
            throw new InvalidSprintException("El id del sprint no puede ser null");
        }
    }
}

