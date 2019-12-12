package ar.iua.edu.webIII.proyecto.viano;

import ar.iua.edu.webIII.proyecto.viano.model.Audit;
import ar.iua.edu.webIII.proyecto.viano.model.JWTAuth;
import ar.iua.edu.webIII.proyecto.viano.model.Usuario;
import ar.iua.edu.webIII.proyecto.viano.persistence.AuditRepository;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.Enumeration;


public class AuditFilter extends OncePerRequestFilter {
    private AuditRepository auditDao;
    private Audit audit;
    public static String AUTH_HEADER = "Authorization";
    private String headers;
    private boolean auditBody;
    private int bodySize;
    private String regex;
    private String[] splittedHeaders;
    public static String AUTH_HEADER_CUSTOM = "X-AUTH-TOKEN";
    public static String AUTH_HEADER1_CUSTOM = "XAUTHTOKEN";
    public static String AUTH_PARAMETER = "xauthtoken";
    public static String AUTH_PARAMETER1 = "token";
    public static String TOKEN_PREFIX = "Bearer ";
    public AuditFilter(AuditRepository auditDao, String header, String auditBody, int bodySize, String regex) {
        this.auditDao = auditDao;
        this.headers = header;
        this.auditBody = Boolean.parseBoolean(auditBody);
        this.bodySize = bodySize;
        this.regex = regex;
        this.splittedHeaders = headers.split(",");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        audit = new Audit();
        JWTAuth jwt = new JWTAuth();
        CustomHttpServletRequestWrapper customRequest = new CustomHttpServletRequestWrapper(request);
        request = customRequest;

        // si no hay ningun header de los que nos interesan o esa uri no nos interesa sigo la cadena de filtros
        if (getHeaders(request) == null || !request.getRequestURI().matches(regex)) {
            chain.doFilter(request, response);
            return;
        }
        audit.setCabeceras(getHeaders(request));
        if (auditBody)
            audit.setBody(customRequest.body.getBytes());
        audit.setTime(new Date(System.currentTimeMillis()));
        audit.setUri(request.getRequestURI());
        audit.setParametros(getParameters(request));

        audit.setMetodo(request.getMethod());

        if (request.getHeader(AUTH_HEADER_CUSTOM) != null || request.getHeader(AUTH_HEADER1_CUSTOM) != null ||
                request.getParameter(AUTH_PARAMETER) != null || request.getParameter(AUTH_PARAMETER1) != null) {
            audit.setTypeOfToken("CUSTOM TOKEN");
        } else {
            if (request.getHeader(AUTH_HEADER) != null) {
                audit.setTypeOfToken("JWT");
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = null;
        if (auth != null && auth.getPrincipal() instanceof Usuario) {
            usuario = (Usuario) auth.getPrincipal();
            audit.setUsuario(usuario.getUsername());
            if(audit.getTypeOfToken().equals("JWT"))
                audit.setRol(jwt.getRoleFromToken(request.getHeader(AUTH_HEADER).replace(TOKEN_PREFIX, "")));
            else
                audit.setRol("Admin");
            audit.setAutorizado("YES");
        } else {
            audit.setAutorizado("NO");
        }
        auditDao.save(audit);
        chain.doFilter(request, response);
    }


    public String getParameters(HttpServletRequest request) {
        Enumeration<String> param = request.getParameterNames();
        JSONObject json = new JSONObject();
        String parameter;
        while (param != null && param.hasMoreElements()) {

            parameter = param.nextElement();
            if (parameter != "")
                json.put(parameter, request.getParameter(parameter));
        }
        return json.toString();
    }

    //obtiene todos los headers
    private String getAllHeaders(HttpServletRequest request, Enumeration<String> headerNames) {
        JSONObject json = new JSONObject();
        String header;
        while (headerNames != null && headerNames.hasMoreElements()) {
            header = headerNames.nextElement();
            if (request.getHeader(header) != "")
                json.put(header, request.getHeader(header));
        }
        return json.toString();
    }

    // si headers es *, obtengo todos los headers
    // si hay algun header de los que hay en la lista de headers, devuelvo todos
    public String getHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        boolean flag = false;
        if (headers.equals("*")) {
            flag = true;

        } else {
            for (int i = 0; i < splittedHeaders.length; i++) {
                if (request.getHeader(splittedHeaders[i]) != null) {
                    flag = true;
                    break;
                }
            }
        }
        if (flag)
            return getAllHeaders(request, headerNames);
        return null;
    }

    public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {
        private final String body;

        public CustomHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);

            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = null;
            // si el contenido es multipart/form-data no lo guardo, no puedo leer ese tipo de
            // contenido sin romper las otras cosas
            if (request.getContentType() == "multipart/form-data") {
                body = "";
                return;
            }
            try {
                InputStream inputStream = request.getInputStream();

                if (inputStream != null) {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    char[] charBuffer = new char[128];
                    int bytesRead = -1;

                    while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                        stringBuilder.append(charBuffer, 0, bytesRead);
                    }
                }
            } catch (IOException ex) {
                logger.error("Error reading the request body...");
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        logger.error("Error closing bufferedReader...");
                    }
                }
            }

            body = stringBuilder.toString();
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());

            ServletInputStream inputStream = new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }
            };

            return inputStream;
        }
    }

}
