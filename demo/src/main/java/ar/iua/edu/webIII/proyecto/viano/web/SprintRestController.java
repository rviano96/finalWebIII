package ar.iua.edu.webIII.proyecto.viano.web;


import ar.iua.edu.webIII.proyecto.viano.business.ISprintBusiness;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.*;
import ar.iua.edu.webIII.proyecto.viano.model.Sprint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constantes.URL_BASE_SPRINTS)
public class SprintRestController extends BaseRestController{
    @Autowired
    private ISprintBusiness sprintBusiness;

    @GetMapping("")
    public ResponseEntity<List<Sprint>> list() {
        try {
            return new ResponseEntity<List<Sprint>>(sprintBusiness.list(), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<List<Sprint>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<String> insert(@RequestBody Sprint sprint) {
        try {
            sprintBusiness.isValid(sprint);
            sprintBusiness.save(sprint);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("location", Constantes.URL_BASE_SPRINTS + "/" + sprint.getId());
            return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DateNullException | InvalidSprintException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error", e.getMessage());
            return new ResponseEntity<String>(responseHeaders,HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<String> update(@PathVariable("id") int idSprint, @RequestBody Sprint sprint) {
        try {
            sprintBusiness.isValid(sprint);
            sprintBusiness.update(idSprint, sprint);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DateNullException | InvalidSprintException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error", e.getMessage());
            return new ResponseEntity<String>(responseHeaders, HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error", e.getMessage());
            return new ResponseEntity<String>(responseHeaders,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Sprint> findSprintById(@PathVariable("id") int idSprint) {
        try {
            return new ResponseEntity<Sprint>(sprintBusiness.findSprintById(idSprint), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<Sprint>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error", e.getMessage());
            return new ResponseEntity<Sprint>(responseHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        try {
            sprintBusiness.remove(id);
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
