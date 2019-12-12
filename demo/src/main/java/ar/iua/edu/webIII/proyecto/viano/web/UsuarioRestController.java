package ar.iua.edu.webIII.proyecto.viano.web;

import ar.iua.edu.webIII.proyecto.viano.business.exceptions.BusinessException;
import ar.iua.edu.webIII.proyecto.viano.business.IUsuarioBusiness;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.InvalidArgumentException;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.NotFoundException;
import ar.iua.edu.webIII.proyecto.viano.model.Usuario;
import ar.iua.edu.webIII.proyecto.viano.model.dto.ChangeSensitiveDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constantes.URL_BASE_USUARIOS)

public class UsuarioRestController extends BaseRestController {
    @Autowired
    private IUsuarioBusiness usuarioBusiness;

    @PostMapping(value = "")
    public ResponseEntity<String> insert(@RequestBody Usuario usuario) {
        try {
            usuarioBusiness.save(usuario);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("location", Constantes.URL_BASE_USUARIOS + "/" + usuario.getId());
            return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}/{field}")
    public ResponseEntity<String> updateSensitiveData(@PathVariable (name = "id")  String name, @PathVariable (name = "field") String field, @RequestBody ChangeSensitiveDataDto changeDataDao ) {
        try {

            usuarioBusiness.update(field, changeDataDao.getPassword(), changeDataDao.getNewValue(), name);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error", e.getMessage());
            return new ResponseEntity<String>(responseHeaders, HttpStatus.NOT_FOUND);
        } catch (InvalidArgumentException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(value = "")
    public ResponseEntity<Usuario> findUserByName(@RequestParam(name = "username") String user) {
        try {
            return new ResponseEntity<Usuario>(usuarioBusiness.load(user),HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<Usuario>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error", e.getMessage());
            return new ResponseEntity<Usuario>(responseHeaders, HttpStatus.NOT_FOUND);

        }
    }
}
