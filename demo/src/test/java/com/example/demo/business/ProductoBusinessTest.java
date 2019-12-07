package com.example.demo.business;

import com.example.demo.model.Producto;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductoBusinessTest {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ProductoBusiness productoBusiness;

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	private static Producto prod1 = new Producto();
	private static String description = "Lomito2";

	@BeforeClass
	public static void setup() {
		prod1.setDescripcion(description) ;
		prod1.setPrecio(125.67);
	}


	@Test
	public void testLoadSuccess() throws  BusinessException, NotFoundException  {
		int id = 7;
		assertEquals("leche", productoBusiness.load(id).getDescripcion());
		assertEquals(66, productoBusiness.load(id).getPrecio());
	}


	@Test
	public void testSaveSuccess() throws  BusinessException, NotFoundException  {
		//prod1.setDescripcion("Lomito2") ;
		//prod1.setPrecio(125.67);
		Producto p2 = productoBusiness.save(prod1);
		assertEquals("Lomito2",p2.getDescripcion());
		assertEquals(125.67 + 125.67*0.1 , p2.getPrecio());

		/*prod1.setDescripcion("Lomito3") ;
		prod1.setPrecio(40);
		prod1 = productoBusiness.save(prod1);
		assertEquals("Lomito3",prod1.getDescripcion());
		assertEquals(40 - 40*0.1 , prod1.getPrecio());*/
	}


	@Test
	public void testLoadFailure() throws  BusinessException, NotFoundException  {
		int id = 7;
		assertNotEquals("Descripcion distinta", productoBusiness.load(id).getDescripcion());
		assertNotEquals(50, productoBusiness.load(id).getPrecio());
	}


	@Test(expected = com.example.demo.business.NotFoundException.class)
	public void testLoadNotFoundException() throws  BusinessException, NotFoundException  {
		int id = 128;
		productoBusiness.load(id);
		expectedEx.expect(com.example.demo.business.NotFoundException.class);
		expectedEx.expectMessage("No se encuentra el producto con id="+id);
	}


}
