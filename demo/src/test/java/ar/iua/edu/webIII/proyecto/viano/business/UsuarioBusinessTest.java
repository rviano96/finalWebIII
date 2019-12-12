package ar.iua.edu.webIII.proyecto.viano.business;

import ar.iua.edu.webIII.proyecto.viano.business.exceptions.BusinessException;
import ar.iua.edu.webIII.proyecto.viano.business.exceptions.NotFoundException;
import ar.iua.edu.webIII.proyecto.viano.business.implementation.UsuarioBusiness;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioBusinessTest {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UsuarioBusiness usuarioBusiness;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();




    @BeforeAll
    public static void setup() {

    }


    @Test
    public void testLoadSuccess() throws BusinessException, NotFoundException {
        String username = "rodrigoUserAdmin";
        assertEquals("rodrigoUserAdmin",usuarioBusiness.load(username).getUsername());
        assertEquals("rodrigo_user_admin@mail.com",usuarioBusiness.load(username).getEmail());
        assertEquals("Rodrigo User Admin",usuarioBusiness.load(username).getFirstName());
        assertEquals("Viano",usuarioBusiness.load(username).getLastName());
        assertEquals("$2a$10$W7HgX4V4eBYX7KilSCTwv.kOwuQ9l9tVV5eTTtN.yk84VwC0clso.",usuarioBusiness.load(username).getPassword());
        assertEquals(null,usuarioBusiness.load(username).getSessionToken());
    }



    @Test
    public void testLoadFailure() throws  BusinessException, NotFoundException  {

        String username = "rodrigoUserAdmin";
        assertNotEquals("admin1",usuarioBusiness.load(username).getUsername());
        assertNotEquals("admin1@mail.com",usuarioBusiness.load(username).getEmail());
        assertNotEquals("Admin",usuarioBusiness.load(username).getFirstName());
        assertNotEquals("García1",usuarioBusiness.load(username).getLastName());
        assertNotEquals(".hr.oNe3YbI9rV5qeprPKoL7OUw7TvhO",usuarioBusiness.load(username).getPassword());
        assertNotEquals("$2a$10$yNF2FaFAiV2MbTdCQVarE.hr.oNe3YbI9rV5qeprPKoL7OUw7TvhO",usuarioBusiness.load(username).getSessionToken());
    }


    @Test(expected = NotFoundException.class)
    public void testLoadNotFoundException() throws  BusinessException, NotFoundException  {
        String username = "admin2";
        usuarioBusiness.load(username);
        expectedEx.expect(NotFoundException.class);
        expectedEx.expectMessage("No se encuentra el usuario con username="+username);
    }

}
