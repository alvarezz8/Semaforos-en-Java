/**
 * Recepcionista.java
 * 
 * @author Mario Álvarez
 * 
 * Esta clase simula ser un recepcionista que asigna a un ala a los pacientes
 * que van entrando al hospital y hacen signal a la recepcionsita.
 * 
 */

import java.util.concurrent.Semaphore;

public class Recepcionista extends Thread {

	private Hospital hospital;

	//Semáforos necesarios para la ejecución del programa.
	private String nombre = "Recepcionista";
	private Semaphore S_recepcionista;
	private Semaphore S_ala [];
	private Semaphore mutexAsignarAla;

	//Contructor.
	public Recepcionista(Semaphore S_recepcionista, Semaphore S_ala [], 
		Semaphore mutexAsignarAla, Hospital hospital){
		
		this.S_recepcionista = S_recepcionista;
		this.S_ala = S_ala;
		this.mutexAsignarAla = mutexAsignarAla;
		this.hospital = hospital;
	}
	
	public void run(){
		try {
			while (true){
				S_recepcionista.acquire();
				int idPaciente = Hospital.getIdPaciente(nombre);
				System.out.println("Recepcionista atendiendo al paciente " + idPaciente);
				Thread.sleep((int) (Math.random() * 2000 + 1));
				
				int ala = asignarAla();
				hospital.setAla(ala);
				S_ala[ala].acquire();

				Principal.pacientesAla[ala]++;
				System.out.println("Recepcionista asignó al paciente " + idPaciente + 
					" al Ala: " + Principal.alas[ala]);
				mutexAsignarAla.release();
			} 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int asignarAla(){
        if (Principal.pacientesAla[1] < Principal.pacientesAla[0]){
            return 1;
        }else return 0;
    }
}
