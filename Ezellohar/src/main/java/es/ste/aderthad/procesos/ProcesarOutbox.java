package es.ste.aderthad.procesos;

import es.ste.aderthad.mensajeria.ServiciosMensajeria;

public class ProcesarOutbox implements Runnable{
	@Override
	public void run() {
		String resultado = ServiciosMensajeria.enviarMensajes();
		/*
		 * Hay que registrar en el log el resultado
		 * */
	  }
}
