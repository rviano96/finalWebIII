package ar.iua.edu.webIII.proyecto.viano.business.implementation;

import ar.iua.edu.webIII.proyecto.viano.business.exceptions.BusinessException;
import ar.iua.edu.webIII.proyecto.viano.business.IAuditBusiness;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.NotFoundException;
import ar.iua.edu.webIII.proyecto.viano.model.Audit;
import ar.iua.edu.webIII.proyecto.viano.persistence.AuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class AuditBusiness implements IAuditBusiness {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AuditRepository auditDao;

    public AuditBusiness() {
    }

    @Override
    public Audit load(int id) throws BusinessException, NotFoundException {
        Optional<Audit> op = null;
        try {
            op = auditDao.findById(id);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        if (!op.isPresent())
            throw new NotFoundException("No se encuentra el registro con id = " + id);
        return op.get();
    }

    @Override
    public Audit save(Audit audit) throws BusinessException {
        try {
            return auditDao.save(audit);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public void delete(int id) throws BusinessException, NotFoundException {
        Optional<Audit> op = null;

        try {
            op = auditDao.findById(id);
        } catch (Exception e) {
            throw new BusinessException(e);
        }

        if (!op.isPresent())
            throw new NotFoundException("No se encuentra el registro con id=" + id);
        try {
            auditDao.deleteById(id);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public List<Audit> list() throws BusinessException {
        try {
            return auditDao.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
    }
}
