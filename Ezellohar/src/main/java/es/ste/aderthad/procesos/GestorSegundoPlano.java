package es.ste.aderthad.procesos;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class GestorSegundoPlano implements ServletContextListener {
	private ScheduledExecutorService scheduler;

	@Override
	public void contextInitialized(ServletContextEvent event) {
	    scheduler = Executors.newSingleThreadScheduledExecutor();
	    scheduler.scheduleAtFixedRate(new ProcesarOutbox(), 0, 20, TimeUnit.MINUTES);
	    scheduler.scheduleAtFixedRate(new RevisarBloqueos(), 0, 10, TimeUnit.MINUTES);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	    scheduler.shutdownNow();
	 }
}
