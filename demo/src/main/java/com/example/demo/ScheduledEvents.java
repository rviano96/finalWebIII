package com.example.demo;


import com.example.demo.business.IJWTAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.demo.business.BusinessException;
import com.example.demo.business.IAuthTokenService;

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
