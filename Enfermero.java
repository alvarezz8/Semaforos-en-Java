/**
 * Enfermero.java
 * 
 * @author Mario Álvarez
 * 
 * Hay dos enfermeros y hay uno por ala.
 * Esta clase simula ser un Enfermero esperandoa signals de los pacientes al 
 * entrar a una habitación. Una vez valorados hacen un signal a la clase Medico
 * para que les haga el diagnóstico.
 *  
 */

import java.util.concurrent.Semaphore;

public class Enfermero extends Thread {

    private int idEnfermero;
    private String nombre = "Enfermero";
    private Semaphore [] S_enf;
    private Semaphore S_medico;

    public Enfermero(int idEnfermero, Semaphore S_enf [], 
        Semaphore S_medico){
        
        this.idEnfermero = idEnfermero - 1;
        this.S_enf = S_enf;
        this.S_medico = S_medico;
    }

    public void run (){
        try {  
            while(true){
                S_enf[idEnfermero].acquire();
                
                int idPaciente = Hospital.getIdPaciente(nombre);
                System.out.println("Enfermero " + Principal.alas[idEnfermero] + 
                    " atendiendo al paciente " + idPaciente );

                Thread.sleep((int) (Math.random() * 2000 + 1));

                Principal.pacientesV[idEnfermero]++;
                System.out.println("Enfermero " + Principal.alas[idEnfermero] + 
                    " ha valorado al paciente " + idPaciente + ", esperando médico");
			
                S_medico.release();
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }


}
