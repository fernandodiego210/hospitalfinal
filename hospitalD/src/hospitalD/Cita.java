package hospitalD;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;

// Clase Cita médica
public class Cita {
    private int idCita;
    private Paciente paciente; // Relación con Paciente
    private Doctor doctor; // Relación con Doctor
    private LocalDateTime fechaCita;
    private String motivoConsulta;
    private String estadoCita; // PROGRAMADA, COMPLETADA, CANCELADA
    private String observaciones;
    private BigDecimal costoConsulta;
    private LocalDateTime fechaCreacion;
    private String diagnostico;
    private String tratamiento;
    
    // Constructores
    public Cita() {
        this.estadoCita = "PROGRAMADA";
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Cita(Paciente paciente, Doctor doctor, LocalDateTime fechaCita, 
                String motivoConsulta, BigDecimal costoConsulta) {
        this();
        this.paciente = paciente;
        this.doctor = doctor;
        this.fechaCita = fechaCita;
        this.motivoConsulta = motivoConsulta;
        this.costoConsulta = costoConsulta;
    }
    
    // Getters y setters
    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }
    
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    
    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    
    public LocalDateTime getFechaCita() { return fechaCita; }
    public void setFechaCita(LocalDateTime fechaCita) { this.fechaCita = fechaCita; }
    
    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }
    
    public String getEstadoCita() { return estadoCita; }
    public void setEstadoCita(String estadoCita) { this.estadoCita = estadoCita; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    public BigDecimal getCostoConsulta() { return costoConsulta; }
    public void setCostoConsulta(BigDecimal costoConsulta) { this.costoConsulta = costoConsulta; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    
    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }
    
    // Métodos específicos de Cita
    public boolean estaProgramada() {
        return "PROGRAMADA".equalsIgnoreCase(estadoCita);
    }
    
    public boolean estaCompletada() {
        return "COMPLETADA".equalsIgnoreCase(estadoCita);
    }
    
    public boolean estaCancelada() {
        return "CANCELADA".equalsIgnoreCase(estadoCita);
    }
    
    public void completarCita(String diagnostico, String tratamiento) {
        if (estaProgramada()) {
            this.diagnostico = diagnostico;
            this.tratamiento = tratamiento;
            this.estadoCita = "COMPLETADA";
            
            // Agregar diagnóstico al historial del paciente
            if (paciente != null && diagnostico != null && !diagnostico.trim().isEmpty()) {
                paciente.agregarDiagnostico(diagnostico);
            }
            
            System.out.println("Cita completada para " + 
                             (paciente != null ? paciente.getNombreCompleto() : "paciente desconocido"));
        }
    }
    
    public void cancelarCita(String motivo) {
        if (estaProgramada()) {
            this.estadoCita = "CANCELADA";
            this.observaciones = (observaciones != null ? observaciones + " | " : "") + 
                               "Cancelada: " + motivo;
            System.out.println("Cita cancelada para " + 
                             (paciente != null ? paciente.getNombreCompleto() : "paciente desconocido") +
                             ". Motivo: " + motivo);
        }
    }
    
    public void reprogramarCita(LocalDateTime nuevaFecha) {
        if (estaProgramada()) {
            LocalDateTime fechaAnterior = this.fechaCita;
            this.fechaCita = nuevaFecha;
            this.observaciones = (observaciones != null ? observaciones + " | " : "") + 
                               "Reprogramada desde: " + fechaAnterior.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            System.out.println("Cita reprogramada para " + 
                             (paciente != null ? paciente.getNombreCompleto() : "paciente desconocido") +
                             " al " + nuevaFecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        }
    }
    
    public boolean esHoy() {
        return fechaCita != null && fechaCita.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }
    
    public boolean yaVencio() {
        return fechaCita != null && fechaCita.isBefore(LocalDateTime.now());
    }
    
    public String getResumenCita() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Cita ID: " + idCita + 
               "\nPaciente: " + (paciente != null ? paciente.getNombreCompleto() : "No asignado") +
               "\nDoctor: " + (doctor != null ? doctor.getNombreCompleto() : "No asignado") +
               "\nEspecialidad: " + (doctor != null && doctor.getEspecialidad() != null ? 
                                   doctor.getEspecialidad().getNombreEspecialidad() : "No especificada") +
               "\nFecha: " + (fechaCita != null ? fechaCita.format(formatter) : "No programada") +
               "\nMotivo: " + (motivoConsulta != null ? motivoConsulta : "No especificado") +
               "\nEstado: " + estadoCita +
               "\nCosto: $" + (costoConsulta != null ? costoConsulta : "0.00");
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Cita #" + idCita + " - " + 
               (paciente != null ? paciente.getNombreCompleto() : "Sin paciente") + 
               " con Dr. " + (doctor != null ? doctor.getNombreCompleto() : "Sin doctor") +
               " - " + (fechaCita != null ? fechaCita.format(formatter) : "Sin fecha") +
               " - Estado: " + estadoCita;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cita cita = (Cita) obj;
        return idCita == cita.idCita;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idCita);
    }
}