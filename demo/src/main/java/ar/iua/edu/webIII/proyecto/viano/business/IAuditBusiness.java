package ar.iua.edu.webIII.proyecto.viano.business;

import ar.iua.edu.webIII.proyecto.viano.business.exceptions.BusinessException;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.NotFoundException;
import ar.iua.edu.webIII.proyecto.viano.model.Audit;

import java.util.List;

public interface IAuditBusiness {
    public Audit load(int id) throws BusinessException, NotFoundException;

    public Audit save(Audit audit) throws BusinessException;

    public void delete(int id) throws BusinessException, NotFoundException;

    public List<Audit> list() throws BusinessException;


}
