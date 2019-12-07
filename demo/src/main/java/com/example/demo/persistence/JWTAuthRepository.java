package com.example.demo.persistence;

import com.example.demo.model.JWTAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Repository
public interface JWTAuthRepository extends JpaRepository<JWTAuth, String> {

    @Transactional
    @Modifying
    //@Query(value = "DELETE FROM jwt_auth WHERE tipo='DEFAULT' AND DATE_ADD(last_used, INTERVAL validity_seconds SECOND)<?", nativeQuery = true)
    @Query(value = "DELETE FROM jwt_auth WHERE  hasta<?", nativeQuery = true)
    public int purgeJWT(Date hasta);

    public Optional <JWTAuth> findByToken(String token);

    /*@Transactional
    @Modifying
    @Query(value = "DELETE FROM jwt_auth WHERE tipo='TO_DATE' AND hasta<?", nativeQuery = true)
    public int purgeToDate(Date hasta);



    @Transactional
    @Modifying
    @Query(value = "DELETE FROM jwt_auth WHERE tipo='FROM_TO_DATE' AND hasta<<?", nativeQuery = true)
    public int purgeFromToDate(Date hasta);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM jwt_auth WHERE tipo='REQUEST_LIMIT' AND request_count>=request_limit", nativeQuery = true)
    public int purgeRequestLimit();*/

}
