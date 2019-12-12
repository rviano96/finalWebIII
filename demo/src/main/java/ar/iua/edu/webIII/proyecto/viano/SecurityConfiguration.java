package ar.iua.edu.webIII.proyecto.viano;

import java.util.Arrays;

import ar.iua.edu.webIII.proyecto.viano.business.IAuthTokenService;
import ar.iua.edu.webIII.proyecto.viano.persistence.AuditRepository;
import ar.iua.edu.webIII.proyecto.viano.persistence.UsuarioRepository;
import ar.iua.edu.webIII.proyecto.viano.web.Constantes;
import ar.iua.edu.webIII.proyecto.viano.business.IJWTAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailService;
    @Value("${app.session.audit.enable}")
    private String enbleAudit;
    @Value("${app.session.audit.headers}")
    private String headers;
    @Value("${app.session.audit.body}")
    private String auditBody;
    @Value("${app.session.audit.body.size}")
    private int bodySize;
    @Value("${app.session.audit.regex}")
    private String regex;
    @Autowired
    private IAuthTokenService authTokenService;
    @Autowired
    private UsuarioRepository usuariosDAO;
    @Autowired
    private IJWTAuthService jwtAuthService;
    @Autowired
    private AuditRepository auditDao;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // auth.inMemoryAuthentication().withUser("pepe").password(passwordEncoder.encode("clave")).roles("USER").and()
        // .withUser("admin").password(passwordEncoder.encode("123")).roles("ADMIN");
        auth.userDetailsService(userDetailService);

    }

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        // return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Access-Control-Allow-Origin",
                "Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin", "Cache-Control",
                "Content-Type", "Authorization"));
        configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "POST", "PATCH", "PUT"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(new CustomTokenAuthenticationFilter(authTokenService, usuariosDAO),
                UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(new JWTAuthTokenFilter(jwtAuthService, usuariosDAO),
                CustomTokenAuthenticationFilter.class);
        // Si esta habilitado la auditoria, agrego el filtro, sino no.
        if(Boolean.parseBoolean(enbleAudit))
            http.addFilterAfter(new AuditFilter(auditDao, headers, auditBody, bodySize, regex), JWTAuthTokenFilter.class);
        http.httpBasic();
        http.authorizeRequests()
                .antMatchers("/api/v1/**").authenticated()
                .antMatchers( "/v2/api-docs","/configuration/**", "/swagger*/**", "/webjars/**", "/","/csrf").permitAll();
        http.authorizeRequests().antMatchers(Constantes.URL_AUTH_INFO, Constantes.URL_LOGINOK, Constantes.URL_TOKEN)
                .authenticated();
        http.formLogin().loginPage(Constantes.URL_DENY).successForwardUrl(Constantes.URL_LOGINOK)
                .loginProcessingUrl("/dologin").failureUrl(Constantes.URL_DENY);
        http.headers().frameOptions().disable();
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();
    }

    /*
     * @Autowired private DataSource ds;
     *
     * private PersistentTokenRepository rmRepository() { JdbcTokenRepositoryImpl r
     * = new JdbcTokenRepositoryImpl(); r.setDataSource(ds); return r; }
     */
    /*
     * CREATE TABLE `persistent_logins` ( `username` varchar(100) NOT NULL, `series`
     * varchar(64) NOT NULL, `token` varchar(64) NOT NULL, `last_used` timestamp NOT
     * NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY
     * (`series`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8
     */

}
