package ar.iua.edu.webIII.proyecto.viano.business;

import ar.iua.edu.webIII.proyecto.viano.business.exceptions.*;
import ar.iua.edu.webIII.proyecto.viano.business.implementation.TaskBusiness;
import ar.iua.edu.webIII.proyecto.viano.model.Sprint;
import ar.iua.edu.webIII.proyecto.viano.model.SprintList;
import ar.iua.edu.webIII.proyecto.viano.model.Task;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskBusinessTest {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
    TaskBusiness taskBusiness;

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	private static Task task = new Task();
    private static SprintList list = new SprintList();
    private static Sprint sprint = new Sprint();
	@BeforeClass
	public static void setup() {
		task.setName("Tarea prueba");
		task.setPriority("Alta");
		task.setCreation(new Date());
		task.setDescription("Tarea test");
		task.setEstimation(2);
		task.setModification(new Date());
	}


	@Test(expected =ar.iua.edu.webIII.proyecto.viano.business.exceptions.NotFoundException.class)
	public void testLoadNotFound() throws BusinessException, NotFoundException {
		int id = 50;
        taskBusiness.findOneById(id);
        expectedEx.expect(ar.iua.edu.webIII.proyecto.viano.business.exceptions.NotFoundException.class);
        expectedEx.expectMessage("No hay ninguna tarea de id= " + id);
	}

    @Test(expected =ar.iua.edu.webIII.proyecto.viano.business.exceptions.ListDoesNotExistException.class)
    public void testSaveFaliure() throws BusinessException, DateNullException, ListDoesNotExistException, InvalidPriorityException, AssignNotAllowedException {
        list.setId(50);
        task.setName("Tarea prueba");
        task.setListName(list);
        task.setPriority("Alta");
        task.setCreation(new Date());
        task.setDescription("Tarea test");
        task.setEstimation(2);
        task.setModification(new Date());
	    taskBusiness.save(task);
        expectedEx.expect(ar.iua.edu.webIII.proyecto.viano.business.exceptions.ListDoesNotExistException.class);
        expectedEx.expectMessage("La lista con id " + 50 + " no existe");
    }

	@Test
	public void testSaveSuccess() throws BusinessException, DateNullException, ListDoesNotExistException, InvalidPriorityException, AssignNotAllowedException, NotFoundException {
       task = new Task();
       task.setName("tarea prueba");
       task.setModification(new Date());
       task.setCreation(new Date());
       task.setEstimation(3);
       task.setDescription("Tarea prueba");
       task.setPriority("Alta");
       list = new SprintList();
       list.setId(1);
       task.setListName(list);
       task = taskBusiness.save(task);
        assertEquals("tarea prueba",taskBusiness.findOneById(task.getId()).getName());
        assertEquals("alta",taskBusiness.findOneById(task.getId()).getPriority().toLowerCase());
        assertEquals(java.util.Optional.of(1),java.util.Optional.ofNullable(taskBusiness.findOneById(task.getId()).getListName().getId()));
        assertEquals("Tarea prueba",taskBusiness.findOneById(task.getId()).getDescription());
	}




}
