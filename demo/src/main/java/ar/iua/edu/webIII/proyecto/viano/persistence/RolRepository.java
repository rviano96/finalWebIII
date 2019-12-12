package ar.iua.edu.webIII.proyecto.viano.persistence;

import java.util.List;

import ar.iua.edu.webIII.proyecto.viano.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

	public List<Rol> findByRol(String rol); 
}
