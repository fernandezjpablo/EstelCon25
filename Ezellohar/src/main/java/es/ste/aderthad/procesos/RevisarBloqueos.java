package es.ste.aderthad.procesos;


import es.ste.aderthad.sql.SQLHabitaciones;

public class RevisarBloqueos implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		SQLHabitaciones.LimpiarBloqueos();
		SQLHabitaciones.LimpiarBloqueosParciales();
	}

}
