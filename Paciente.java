/**
 * Paciente.java
 * 
 * @author Mario Álvarez
 * 
 * Esta clase simula ser un paciente que se va comunicando con las demás 
 * clases hasta salir del hospital donde se acaba.
 * Su vida se trata en entrar al hospital y hacer signal a la recepcionista
 * para asignarles un ala, una vez con el ala asignada entran a su ala y
 * comprueban si hay alguna habitación libre, si lo hay entran y hacen un signal
 * a los enfermeros. Una vez valorados por los enfermeros esperan a recibir un 
 * diagóstico del
 * 
 */

import java.util.concurrent.Semaphore;

public class Paciente extends Thread {

	private Hospital hospital;

	//Variables de la clase.
	private int idPaciente;
	private String [] ala = {"A", "B"};
	private int alaAsignada;

	//Semaforos necesarios.
	private Semaphore mutexPaciente;
	private Semaphore S_recepcion;
	private Semaphore S_recepcionista;
	private Semaphore mutexAsignarAla;
	private Semaphore S_ala [];
	private Semaphore S_hab [];
	private Semaphore S_enf [];
	private Semaphore mutexDiagnosticado;
	private Semaphore mutexMarcharse;

	//Contructor con todos los semáforos pasados por la clase principal, además del id 
	//de cada paciente.
	public Paciente(int idPaciente, Semaphore mutexPaciente, Semaphore S_recepcion, 
		Semaphore S_recepcionista, Semaphore mutexAsignarAla, Semaphore S_ala [], 
		Semaphore S_hab [], Semaphore S_enf [], Semaphore mutexDiagnosticado, 
		Semaphore mutexMarcharse, Hospital hospital){
					
		this.mutexPaciente = mutexPaciente;
		this.idPaciente = idPaciente;
		this.S_recepcion = S_recepcion;
		this.S_recepcionista = S_recepcionista;
		this.hospital = hospital;
		this.mutexAsignarAla = mutexAsignarAla;
		this.S_ala = S_ala;
		this.S_hab = S_hab;
		this.S_enf = S_enf;
		this.mutexDiagnosticado = mutexDiagnosticado;
		this.mutexMarcharse = mutexMarcharse;

	}

	//Cada hilo paciente ejecuta este codigo y va esperando y mandando signals con las
	//demás clases por medio de semáforos.
	public void run(){
		try {
			S_recepcion.acquire();
			Principal.pacientesRecepcion++;
			System.out.println("Paciente " + idPaciente + " ha entrado al hospital");

            mutexPaciente.acquire();

			hospital.setIdPaciente(idPaciente);

			S_recepcionista.release();

			System.out.println("Paciente " + idPaciente + 
			" esperando a recepcionista mientras le asigna el ala");

			mutexAsignarAla.acquire();

			alaAsignada = hospital.getAla();
			System.out.println("Paciente " + idPaciente + " ha entrado al Ala: " +
				ala[alaAsignada]);

			Principal.pacientesRecepcion--;
			mutexPaciente.release();
			S_recepcion.release();

			S_hab[alaAsignada].acquire();

			Principal.pacientesH[alaAsignada]++;
			System.out.println("Paciente " + idPaciente + 
				" ha entrado a una habitacióndel ala " + 
				Principal.alas[alaAsignada] + " y está esperando");
			
			S_enf[alaAsignada].release();
			
			mutexDiagnosticado.acquire();

			Principal.pacientesH[alaAsignada]--;
			Principal.pacientesAla[alaAsignada]--;
			Principal.pacientesV[alaAsignada]--;

			S_hab[alaAsignada].release();
			S_ala[alaAsignada].release();

			mutexMarcharse.acquire();
		
			System.out.println("Paciente " + idPaciente + " ha salido del hospital.");

        } catch (InterruptedException e){
            e.printStackTrace();  
		}
	}
}
