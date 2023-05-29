/**
 * Hospital.java
 * 
 * @author Mario Álvarez
 * 
 * En esta clase guardamos los datos de los pacientes para poder acceder 
 * a ellos desde cualquier clase.
 * 
 */

//E
public class Hospital {
    
    //Vectores que guardan los valores de cada paciente.
    static int [] idPaciente;
    static int [] ala;

    //Variables que llevan el conteo de los pacientes en cada clase.
    static int ordenP = 0;
    static int ordenR = 0;
    static int ordenE = 0;
    static int ordenM = 0;
    static int ordenEsp = 0;

    //Constructor en el que se inicializan los vectores con la 
    //longitud de pacientes que hay en el hospital.
    public Hospital(int nPacientes){
        this.idPaciente = new int [nPacientes + 1];
        this.ala = new int [nPacientes + 1];
    }

    //Coloca el id del paciente en el vector ordenadamente.
    public static void setIdPaciente(int idPaciente) {
        Hospital.idPaciente [ordenP] = idPaciente;
        ordenP++;
    }

    //Coloca el ala asignada en el vector ordenadamente.
    public static void setAla(int ala){
        Hospital.ala [ordenR]= ala;
    }

    //Recoge el ala asignada al paciente especificado. 
    public static int getAla(){
        return ala[ordenP];
    }

    //Recoge el id del paciente dependiendo desde que clase se le llama.
    public static int getIdPaciente(String nombre){ 
        if (nombre == "Enfermero"){
            ordenE++;
            return idPaciente[ordenE - 1];
        }
        else if(nombre == "Recepcionista"){
            ordenR++;
            return idPaciente[ordenR - 1];
        }
        else if (nombre == "Medico"){
            ordenM++;
            ordenEsp++;
            return idPaciente[ordenM - 1];
        }
        else if (nombre == "Especialista"){
            return idPaciente[ordenEsp - 1];
        }
        else return 0;
    }
}
