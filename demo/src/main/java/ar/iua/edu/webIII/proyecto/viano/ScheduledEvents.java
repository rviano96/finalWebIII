package ar.iua.edu.webIII.proyecto.viano;


import ar.iua.edu.webIII.proyecto.viano.business.exceptions.BusinessException;
import ar.iua.edu.webIII.proyecto.viano.business.IAuthTokenService;
import ar.iua.edu.webIII.proyecto.viano.business.IJWTAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduledEvents {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	
	@Autowired
	private IAuthTokenService authTokenService;
	@Autowired
	private IJWTAuthService jwtTokenService;
	@Scheduled(fixedDelay = 24*60*60*1000)
	public void purgeAuthTokens() {
		try {
			authTokenService.purgeTokens();
			jwtTokenService.purgeTokens();
		} catch (BusinessException e) {
			log.error(e.getMessage(), e);
		}
	}

	
	
	@Scheduled(fixedDelay = 5*1000, initialDelay = 3000)
	public void dummy() {
		//log.info("Ejecutando tarea");
	}
}
