/**
 * Especialista.java
 * 
 * @author Mario Álvarez
 * 
 * Esta clase simula ser una ventana de citas para el especialista.
 * La clase está esperando a signals del médico, para asignar al paciente 
 * el día y la hora de la cita con el especialista. 
 * Una vez hecho esto hace un signal al paciente para que pueda marcharse 
 * del hospital.
 * 
 */
import java.util.concurrent.Semaphore;

public class Especialista extends Thread{
    
    private String nombre = "Especialista";
    private Semaphore S_especialista;
    private Semaphore mutexMarcharse;
    

    public Especialista (Semaphore S_especialista, Semaphore mutexMarcharse){
        
        this.S_especialista = S_especialista;
        this.mutexMarcharse = mutexMarcharse;
    }

    public void run (){
        try {
            while(true){
                S_especialista.acquire();

                int idPaciente = Hospital.getIdPaciente(nombre);
                System.out.println("Paciente " + idPaciente +
                    " esperando cita para el especialista");

                Thread.sleep((int) (Math.random() * 3000 + 1000));
                String fecha = getFecha();
                System.out.println("Paciente con cita para el especialista el dia " + fecha);

                mutexMarcharse.release();
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public String getFecha(){
        String cadena;
        int mes = (int) (Math.random() * 12 + 1);
        int dia = 0;
        if (mes == 4 || mes == 6 || mes == 9 || mes == 11){
            dia = (int) (Math.random() * 30 + 1);
        }
        else if (mes == 2){
            dia = (int) (Math.random() * 28 + 1);
        }
        else {
            dia = (int) (Math.random() * 31 + 1);
        }

        int año = 2023;
        int hora = (int) (Math.random() * 14 + 6); //Solo atiende por las mañanas.
        int min = (int) (Math.random() * 59 + 1);

        cadena = dia + "/" + mes + "/" + año + " a las " + hora + ":" + min;

        return cadena;
    }

}
