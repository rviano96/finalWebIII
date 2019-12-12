package ar.iua.edu.webIII.proyecto.viano.web;

public final class Constantes {
	public static final String URL_API_BASE = "/api";
	public static final String URL_API_VERSION = "/v1";
	public static final String URL_BASE = URL_API_BASE + URL_API_VERSION;
	public static final String URL_BASE_TASKS = URL_BASE + "/tasks";
	public static final String URL_BASE_SPRINTS = URL_BASE + "/sprints";
	public static final String URL_BASE_LISTS = URL_BASE + "/lists";
	public static final String URL_BASE_USUARIOS = URL_BASE + "/usuarios";
	public static final String URL_DENY = "/deny";
	public static final String URL_LOGINOK = "/loginok";
	public static final String URL_AUTH_INFO =  "/authinfo";
	public static final String URL_VERSION = "/version";
	public static final String URL_TOKEN = URL_BASE + "/token";

	public static final String BACKLOG_LIST = "BACKLOG";
	public static final String TODO_LIST = "TODO";
	public static final String IN_PROGRESS_LIST = "IN PROGRESS";
	public static final String WAITING_LIST = "WAITING";
	public static final String DONE_LIST = "DONE";
	public final static String LOW_PRIORITY = "baja";
	public final static String MEDIUM_PRIORITY = "media";
	public final static String HIGH_PRIORITY = "alta";
}
