package ar.iua.edu.webIII.proyecto.viano.persistence;

import ar.iua.edu.webIII.proyecto.viano.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Integer> {
}
