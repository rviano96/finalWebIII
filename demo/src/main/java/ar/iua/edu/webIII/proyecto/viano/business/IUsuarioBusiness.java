package ar.iua.edu.webIII.proyecto.viano.business;


import ar.iua.edu.webIII.proyecto.viano.business.exceptions.BusinessException;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.InvalidArgumentException;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.NotFoundException;
import ar.iua.edu.webIII.proyecto.viano.model.Usuario;

public interface IUsuarioBusiness {

	public Usuario load(String username) throws BusinessException, NotFoundException;
	public Usuario save(Usuario usuario) throws BusinessException;
	public Usuario update(String field, String password, Object newValue,  String name) throws BusinessException, NotFoundException, InvalidArgumentException;
}
