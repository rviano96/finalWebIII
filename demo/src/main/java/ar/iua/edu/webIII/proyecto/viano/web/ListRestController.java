package ar.iua.edu.webIII.proyecto.viano.web;

import java.util.List;

import ar.iua.edu.webIII.proyecto.viano.business.exceptions.*;
import ar.iua.edu.webIII.proyecto.viano.business.IListBusiness;
import ar.iua.edu.webIII.proyecto.viano.model.SprintList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constantes.URL_BASE_LISTS)
public class ListRestController extends BaseRestController {

    @Autowired
    private IListBusiness listBusiness;

    @GetMapping("")
    public ResponseEntity<List<SprintList>> list() {
        try {
            return new ResponseEntity<List<SprintList>>(listBusiness.list(), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<List<SprintList>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<String> insert(@RequestBody SprintList sprintList) {
        try {
            listBusiness.isValid(sprintList);
            listBusiness.save(sprintList);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("location", Constantes.URL_BASE_LISTS + "/" + sprintList.getId());
            return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);

        } catch (BusinessException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ListAlreadyExistsException | NotFoundException | InvalidNameException | InvalidSprintException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping(value = "")
    public ResponseEntity<String> update(@RequestBody SprintList sprintList) {
        try {
            listBusiness.isValid(sprintList);
            listBusiness.save(sprintList);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ListAlreadyExistsException | InvalidNameException | NotFoundException | InvalidSprintException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<SprintList> findOneById(@PathVariable("id") int idList) {
        try {
            return new ResponseEntity<SprintList>(listBusiness.getListById(idList), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<SprintList>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error", e.getMessage());
            return new ResponseEntity<SprintList>(responseHeaders, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping(value = "/{name}/sprint/{id}")
    public ResponseEntity<SprintList> findListByNameAndSprintId(@PathVariable("name") String name, @PathVariable("id") int idSprint) {
        try {
            return new ResponseEntity<SprintList>(listBusiness.getListByNameAndSprintId(name, idSprint), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<SprintList>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error", e.getMessage());
            return new ResponseEntity<SprintList>(responseHeaders, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping(value = "/sprint/{id}")
    public ResponseEntity<List<SprintList>> findListsByIdSprint(@PathVariable("id") int idSprint) {
        try {
            return new ResponseEntity<List<SprintList>>(listBusiness.getListsBySprint(idSprint), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<List<SprintList>>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error", e.getMessage());
            return new ResponseEntity<List<SprintList>>(responseHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        try {
            listBusiness.delete(id);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error", e.getMessage());
            return new ResponseEntity<String>(responseHeaders, HttpStatus.NOT_FOUND);
        }
    }


}
