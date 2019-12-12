package ar.iua.edu.webIII.proyecto.viano.business;

import ar.iua.edu.webIII.proyecto.viano.business.exceptions.BusinessException;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.NotFoundException;
import ar.iua.edu.webIII.proyecto.viano.model.JWTAuth;

public interface IJWTAuthService {
    public JWTAuth save(JWTAuth at) throws BusinessException;

    public JWTAuth load(String token) throws BusinessException, NotFoundException;

    public void delete(JWTAuth at) throws BusinessException;

    public void purgeTokens() throws BusinessException;

}
