package hospitalD;

import java.time.LocalDate;

//Clase Enfermero que hereda de Persona
public class Enfermero extends Persona {
 private int idEnfermero;
 private String numeroColegiatura;
 private String turno; // MAÑANA, TARDE, NOCHE
 private String areaTrabajo;
 private LocalDate fechaIngreso;
 
 // Constructores
 public Enfermero() {
     super();
 }
 
 public Enfermero(String nombre, String apellido, String dni, String telefono, String email,
                  String direccion, LocalDate fechaNacimiento, String numeroColegiatura,
                  String turno, String areaTrabajo, LocalDate fechaIngreso) {
     super(nombre, apellido, dni, telefono, email, direccion, fechaNacimiento);
     this.numeroColegiatura = numeroColegiatura;
     this.turno = turno;
     this.areaTrabajo = areaTrabajo;
     this.fechaIngreso = fechaIngreso;
 }
 
 // Getters y setters específicos de Enfermero
 public int getIdEnfermero() { return idEnfermero; }
 public void setIdEnfermero(int idEnfermero) { this.idEnfermero = idEnfermero; }
 
 public String getNumeroColegiatura() { return numeroColegiatura; }
 public void setNumeroColegiatura(String numeroColegiatura) { this.numeroColegiatura = numeroColegiatura; }
 
 public String getTurno() { return turno; }
 public void setTurno(String turno) { this.turno = turno; }
 
 public String getAreaTrabajo() { return areaTrabajo; }
 public void setAreaTrabajo(String areaTrabajo) { this.areaTrabajo = areaTrabajo; }
 
 public LocalDate getFechaIngreso() { return fechaIngreso; }
 public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
 
 // Métodos específicos de Enfermero
 public void cuidarPaciente(Paciente paciente) {
     System.out.println("Enfermero(a) " + getNombreCompleto() + 
                       " está cuidando al paciente " + paciente.getNombreCompleto());
 }
 
 public void administrarMedicamento(Paciente paciente, String medicamento) {
     System.out.println("Enfermero(a) " + getNombreCompleto() + 
                       " está administrando " + medicamento + 
                       " al paciente " + paciente.getNombreCompleto());
 }
 
 public void tomarSignosVitales(Paciente paciente) {
     System.out.println("Enfermero(a) " + getNombreCompleto() + 
                       " está tomando signos vitales al paciente " + paciente.getNombreCompleto());
 }
 
 public void asistirCirugia(Doctor doctor) {
     System.out.println("Enfermero(a) " + getNombreCompleto() + 
                       " está asistiendo al Dr. " + doctor.getNombreCompleto() + " en cirugía");
 }
 
 public boolean esTurnoNocturno() {
     return "NOCHE".equalsIgnoreCase(turno);
 }
 
 public int getAnosExperiencia() {
     if (fechaIngreso != null) {
         return LocalDate.now().getYear() - fechaIngreso.getYear();
     }
     return 0;
 }
 
 @Override
 public String toString() {
     return super.toString() + " - Enf. " + numeroColegiatura + 
            " - Turno: " + turno + " - Área: " + areaTrabajo;
 }
}