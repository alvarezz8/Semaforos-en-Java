/**
 * Principal.java
 * 
 * @author Mario Álvarez
 * 
 * Clase principal del programa que se encarga de crear y arrancar los 
 * diferentes hilos de ejecución.
 * 
 */

import java.util.concurrent.Semaphore;


public class Principal{

    
    //Capacidad de la recepción del hospital.
    static final int MAX_CAP = 8;    

    //Capacidad de ambas alas del hospital.
    static final int ALA_CAP = 4;
    
    //Numero de habitaciones que hay en cada ala.
    static final int N_HAB_ALA = 2;       
    
    //Numero de pacientes que hay en la recepción, en habitaciones, en la ala
    //además de valorados.
    static int pacientesRecepcion = 0;             
    static int [] pacientesH = {0,0};          
    static int [] pacientesAla = {0,0};
    static int [] pacientesV = {0,0};
    static String [] alas = {"A", "B"};

    //Semaforos para la exclusión mutua de los diferentes hilos de ejecución.                                                                                   
    static Semaphore mutexPaciente = new Semaphore (1);
    static Semaphore [] mutexHab = 
        new Semaphore [] {new Semaphore(1), new Semaphore(1)};

    //Semaforos inicializados a 0 para que el paciente espere a un signal de los consumidores 
    //para poder seguir su ejecución.
    static Semaphore mutexAsignarAla = new Semaphore (0);
    static Semaphore mutexDiagnosticado = new Semaphore (0);
    static Semaphore mutexMarcharse = new Semaphore (0);

    //Semaforos con cola de bloqueo.
    static Semaphore S_recepcion = new Semaphore(MAX_CAP);               
    static Semaphore [] S_ala = 
        new Semaphore [] {new Semaphore(ALA_CAP), new Semaphore(ALA_CAP)};                 
    static Semaphore [] S_hab = 
        new Semaphore [] {new Semaphore (N_HAB_ALA), new Semaphore (N_HAB_ALA)};
    
    //Consumidores.
    static Semaphore S_recepcionista = new Semaphore(0);                       
    static Semaphore S_medico = new Semaphore(0);                              
    static Semaphore [] S_enf = new Semaphore [] {new Semaphore(0), new Semaphore(0)};
    static Semaphore S_especialista = new Semaphore (0);

    //Función principal del programa que se encarga de crear y arrancar los diferentes hilos de ejecución.
    public static void main(String[] args){
 
        int nPacientes = 10;    //Numero de pacientes que entran al hospital.
        int nEnfermeros = 2;    //Numero de enfermeros. Realmente son 1 por ala.

        Hospital hospital = new Hospital(nPacientes);

        //Creación del hilo recepcionista.
        Recepcionista r = new Recepcionista(S_recepcionista, S_ala, mutexAsignarAla, hospital);
        r.start();

        //Creación de los hilos enfermeros.
        for (int i = 1; i <= nEnfermeros; i++){
            new Enfermero(i, S_enf, S_medico).start();
        }

        //Creación del hilo médico.
        Medico m = new Medico(S_medico, mutexDiagnosticado, S_especialista, mutexMarcharse);
        m.start();

        //Creación del hilo Especialista;
        Especialista e = new Especialista (S_especialista, mutexMarcharse);
        e.start();

        //Creación de los pacientes.
        for (int i = 1; i <= nPacientes; i++){
            new Paciente (i, mutexPaciente, S_recepcion, S_recepcionista, mutexAsignarAla, S_ala, S_hab,
            S_enf, mutexDiagnosticado, mutexMarcharse, hospital).start();
        } 
        

    }
}
