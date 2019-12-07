package com.example.demo.business;

import com.example.demo.model.Audit;

import java.util.List;

public interface IAuditBusiness {
    public Audit load(int id) throws BusinessException, NotFoundException;

    public Audit save(Audit audit) throws BusinessException;

    public void delete(int id) throws BusinessException, NotFoundException;

    public List<Audit> list() throws BusinessException;


}
