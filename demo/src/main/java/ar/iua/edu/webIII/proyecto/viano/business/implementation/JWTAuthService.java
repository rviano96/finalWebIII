package ar.iua.edu.webIII.proyecto.viano.business.implementation;

import ar.iua.edu.webIII.proyecto.viano.business.exceptions.BusinessException;
import ar.iua.edu.webIII.proyecto.viano.business.IJWTAuthService;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.NotFoundException;
import ar.iua.edu.webIII.proyecto.viano.model.JWTAuth;
import ar.iua.edu.webIII.proyecto.viano.persistence.JWTAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JWTAuthService implements IJWTAuthService {
    @Autowired
    JWTAuthRepository jwtAuthDAO;

    @Override
    public JWTAuth save(JWTAuth at) throws BusinessException {
        try {
            return jwtAuthDAO.save(at);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public JWTAuth load(String token) throws BusinessException, NotFoundException {
        Optional<JWTAuth> atO;
        try {
            atO = jwtAuthDAO.findByToken(token);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        if (!atO.isPresent())
            throw new NotFoundException("No se encuentra el token =" + token);
        return atO.get();
    }

    @Override
    public void delete(JWTAuth at) throws BusinessException {
        try {
            jwtAuthDAO.delete(at);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public void purgeTokens() throws BusinessException {
        try {
            jwtAuthDAO.purgeJWT(new Date());
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }
}
