package ar.iua.edu.webIII.proyecto.viano.web;

import java.util.Date;
import java.util.List;

import ar.iua.edu.webIII.proyecto.viano.business.exceptions.*;
import ar.iua.edu.webIII.proyecto.viano.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ar.iua.edu.webIII.proyecto.viano.business.ITaskBusiness;


@RestController
@RequestMapping(Constantes.URL_BASE_TASKS)
public class TaskRestController extends BaseRestController {

    @Autowired
    private ITaskBusiness taskBusiness;

    @GetMapping("")
    public ResponseEntity<List<Task>> list() {
        try {
            return new ResponseEntity<List<Task>>(taskBusiness.list(), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<List<Task>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Task> load(@PathVariable("id") int id) {
        try {
            return new ResponseEntity<Task>(taskBusiness.findOneById(id), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<Task>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error", e.getMessage());
            return new ResponseEntity<Task>(responseHeaders, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping(value = "/priority/{idsprint}")
    public ResponseEntity<List<Task>> findAllOrderByPriority(@PathVariable("idsprint") int idSprint, @RequestParam(name = "order") String order) {
        try {
            return new ResponseEntity<List<Task>>(taskBusiness.findAllBySprintOrderByPriority(idSprint, order), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<List<Task>>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error", e.getMessage());
            return new ResponseEntity<List<Task>>(responseHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/date/{idSprint}")
    public ResponseEntity<List<Task>> findAllOrderByDate(@PathVariable("idSprint") int idpsrint, @RequestParam(name = "order") String order) {
        try {
            return new ResponseEntity<List<Task>>(taskBusiness.findAllBySprintOrderByCreation(idpsrint, order), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<List<Task>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (NotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error", e.getMessage());
            return new ResponseEntity<List<Task>>(responseHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/estimation/{idSprint}")
    public ResponseEntity<List<Task>> findAllByEstimation(@PathVariable("idSprint") int idpsrint, @RequestParam(value = "estimation") int estimation) {
        try {
            return new ResponseEntity<List<Task>>(taskBusiness.getAllBySprintAndEstimation(idpsrint, estimation), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<List<Task>>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error", e.getMessage());
            return new ResponseEntity<List<Task>>(responseHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/list/{id}/creation")
    public ResponseEntity<List<Task>> findAllByListNameOrderByCreation(@PathVariable("id") int listId, @RequestParam(value="sprintname") String sprintName, @RequestParam(value = "order") String order) {
        try {
            return new ResponseEntity<List<Task>>(taskBusiness.getAllByListIdAndSprintNameOrderByCreation(listId, sprintName, order), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<List<Task>>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error", e.getMessage());
            return new ResponseEntity<List<Task>>(responseHeaders, HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping(value = "/list/{id}/priority")
    public ResponseEntity<List<Task>> findAllByListNameOrderByPriority(@PathVariable("id") int listId,  @RequestParam(value="sprintname") String sprintName, @RequestParam(value = "order") String order) {
        try {
            return new ResponseEntity<List<Task>>(taskBusiness.getAllByListIdAndSprintNameOrderByPriority(listId, sprintName, order), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<List<Task>>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error", e.getMessage());
            return new ResponseEntity<List<Task>>(responseHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/list/{id}")
    public ResponseEntity<List<Task>> findAllByListName(@PathVariable("id") int listId) {
        try {
            return new ResponseEntity<List<Task>>(taskBusiness.getAllByListNameId(listId), HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<List<Task>>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("error", e.getMessage());
            return new ResponseEntity<List<Task>>(responseHeaders, HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping(value = "")
    public ResponseEntity<String> insert(@RequestBody Task task) {
        try {
            taskBusiness.save(task);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("location", Constantes.URL_BASE_TASKS + "/" + task.getId());
            return new ResponseEntity<String>(responseHeaders, HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DateNullException | AssignNotAllowedException | InvalidPriorityException | ListDoesNotExistException| InvalidTaskException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Integer id, @RequestBody Task task) {
        try {
            taskBusiness.isValid(task);
            taskBusiness.update(id, task);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DateNullException | InvalidEstimationException | InvalidPriorityException | NotFoundException | CannotMoveException | InvalidNameException | InvalidTaskException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/move/{idList}/{idTask}")
    public ResponseEntity<String> update(@PathVariable("idList") int idList, @PathVariable("idTask") int idTask) {
        try {
            taskBusiness.canMove(idTask, idList);
            taskBusiness.moveTask(idTask, idList);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DateNullException | InvalidEstimationException | InvalidPriorityException | NotFoundException | CannotMoveException | InvalidNameException | InvalidTaskException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        try {
            taskBusiness.remove(id);
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (BusinessException e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
