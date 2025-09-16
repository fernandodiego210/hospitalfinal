package hospitalD;

import java.math.BigDecimal;

//Clase Habitación del hospital
public class Habitacion {
 private int idHabitacion;
 private String numeroHabitacion;
 private String tipoHabitacion; // INDIVIDUAL, DOBLE, UCI, EMERGENCIA
 private Departamento departamento; // Relación con Departamento
 private int capacidad;
 private BigDecimal precioDia;
 private String estado; // DISPONIBLE, OCUPADA, MANTENIMIENTO
 private Paciente pacienteActual; // Paciente que ocupa la habitación
 
 // Constructores
 public Habitacion() {
     this.estado = "DISPONIBLE";
     this.capacidad = 1;
 }
 
 public Habitacion(String numeroHabitacion, String tipoHabitacion, 
                   Departamento departamento, int capacidad, BigDecimal precioDia) {
     this();
     this.numeroHabitacion = numeroHabitacion;
     this.tipoHabitacion = tipoHabitacion;
     this.departamento = departamento;
     this.capacidad = capacidad;
     this.precioDia = precioDia;
 }
 
 // Getters y setters
 public int getIdHabitacion() { return idHabitacion; }
 public void setIdHabitacion(int idHabitacion) { this.idHabitacion = idHabitacion; }
 
 public String getNumeroHabitacion() { return numeroHabitacion; }
 public void setNumeroHabitacion(String numeroHabitacion) { this.numeroHabitacion = numeroHabitacion; }
 
 public String getTipoHabitacion() { return tipoHabitacion; }
 public void setTipoHabitacion(String tipoHabitacion) { this.tipoHabitacion = tipoHabitacion; }
 
 public Departamento getDepartamento() { return departamento; }
 public void setDepartamento(Departamento departamento) { this.departamento = departamento; }
 
 public int getCapacidad() { return capacidad; }
 public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
 
 public BigDecimal getPrecioDia() { return precioDia; }
 public void setPrecioDia(BigDecimal precioDia) { this.precioDia = precioDia; }
 
 public String getEstado() { return estado; }
 public void setEstado(String estado) { this.estado = estado; }
 
 public Paciente getPacienteActual() { return pacienteActual; }
 public void setPacienteActual(Paciente pacienteActual) { this.pacienteActual = pacienteActual; }
 
 // Métodos específicos de Habitación
 public boolean estaDisponible() {
     return "DISPONIBLE".equalsIgnoreCase(estado);
 }
 
 public boolean estaOcupada() {
     return "OCUPADA".equalsIgnoreCase(estado);
 }
 
 public boolean estaEnMantenimiento() {
     return "MANTENIMIENTO".equalsIgnoreCase(estado);
 }
 
 public boolean ocuparHabitacion(Paciente paciente) {
     if (estaDisponible()) {
         this.pacienteActual = paciente;
         this.estado = "OCUPADA";
         System.out.println("Habitación " + numeroHabitacion + " ocupada por " + paciente.getNombreCompleto());
         return true;
     }
     return false;
 }
 
 public boolean liberarHabitacion() {
     if (estaOcupada()) {
         System.out.println("Habitación " + numeroHabitacion + " liberada. Paciente: " + 
                          (pacienteActual != null ? pacienteActual.getNombreCompleto() : "Desconocido"));
         this.pacienteActual = null;
         this.estado = "DISPONIBLE";
         return true;
     }
     return false;
 }
 
 public void ponerEnMantenimiento() {
     if (pacienteActual != null) {
         System.out.println("No se puede poner en mantenimiento. Habitación ocupada por: " + 
                          pacienteActual.getNombreCompleto());
         return;
     }
     this.estado = "MANTENIMIENTO";
     System.out.println("Habitación " + numeroHabitacion + " puesta en mantenimiento");
 }
 
 public void terminarMantenimiento() {
     if (estaEnMantenimiento()) {
         this.estado = "DISPONIBLE";
         System.out.println("Mantenimiento completado. Habitación " + numeroHabitacion + " disponible");
     }
 }
 
 public boolean esUCI() {
     return "UCI".equalsIgnoreCase(tipoHabitacion);
 }
 
 public boolean esEmergencia() {
     return "EMERGENCIA".equalsIgnoreCase(tipoHabitacion);
 }
 
 public String getUbicacionCompleta() {
     if (departamento != null) {
         return departamento.getNombreDepartamento() + " - " + numeroHabitacion;
     }
     return numeroHabitacion;
 }
 
 @Override
 public String toString() {
     return "Habitación " + numeroHabitacion + " (" + tipoHabitacion + ") - " +
            "Capacidad: " + capacidad + " - Estado: " + estado + 
            " - Precio/día: $" + precioDia +
            (pacienteActual != null ? " - Ocupada por: " + pacienteActual.getNombreCompleto() : "");
 }
 
 @Override
 public boolean equals(Object obj) {
     if (this == obj) return true;
     if (obj == null || getClass() != obj.getClass()) return false;
     Habitacion that = (Habitacion) obj;
     return idHabitacion == that.idHabitacion;
 }
 
 @Override
 public int hashCode() {
     return Integer.hashCode(idHabitacion);
 }
}