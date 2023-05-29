/**
 * Medico.java
 * 
 * @author Mario Álvarez
 * 
 * Solo hay un médico para tratar las dos alas, este lo que hace es esperar
 * signals de enfermero y comprobar en que ala hay pacientes valorados por 
 * los enfermeros. Una vez diagnosticados hace un signal a los pacientes 
 * para que se vayan del hospital o puede enviarlos a las ventana de citas
 * para el especialista, haciendo un signal a la clase Especialista.
 * 
 */

import java.util.concurrent.Semaphore;

public class Medico extends Thread{

    private String nombre = "Medico";
    private Semaphore S_medico;
    private Semaphore mutexDiagnosticado;
    private Semaphore S_especialista;
    private Semaphore mutexMarcharse;

    public Medico (Semaphore S_medico, Semaphore mutexDiagnosticado, 
        Semaphore S_especialista, Semaphore mutexMarcharse){
        
        this.S_medico = S_medico;
        this.mutexDiagnosticado = mutexDiagnosticado;
        this.S_especialista = S_especialista;
        this.mutexMarcharse = mutexMarcharse;
    }

    public void run (){
        try {
            while(true){
                S_medico.acquire();

                int ala = getAlaMedico();
                int idPaciente = Hospital.getIdPaciente(nombre);
                System.out.println("Medico diagnosticando al paciente " + idPaciente +
                    " en el ala " + Principal.alas[ala]);
                
                Thread.sleep((int) (Math.random() * 3000 + 1000));

                mutexDiagnosticado.release();
                int especialista = (int) (Math.random() * 10 + 1);
                if (especialista > 6){
                    System.out.println("Médico mandó al paciente " + idPaciente +
                        " del ala " + Principal.alas[ala] + " al especialista");
                    S_especialista.release();
                }
                else {
                    System.out.println("Medico ha diagnosticado al paciente " + 
                        idPaciente + " en el ala " + Principal.alas[ala]);
                    mutexMarcharse.release();
                }
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static int getAlaMedico(){
        if (Principal.pacientesV[1] > 0 ){
            return 1;
        }
        else if (Principal.pacientesV[0] > 0){
            return 0;
        }
        else{
            System.out.println("No quedan pacientes valorados");
            return 2;
        }
    }
}
