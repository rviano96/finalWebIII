package com.example.demo.business;

import com.example.demo.model.JWTAuth;

public interface IJWTAuthService {
    public JWTAuth save(JWTAuth at) throws BusinessException;

    public JWTAuth load(String token) throws BusinessException, NotFoundException;

    public void delete(JWTAuth at) throws BusinessException;

    public void purgeTokens() throws BusinessException;

}
