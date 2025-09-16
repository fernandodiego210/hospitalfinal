package hospitalD;

import java.time.LocalDate;
import java.time.LocalTime;
import java.math.BigDecimal;

// Clase Doctor que hereda de Persona
public class Doctor extends Persona {
    private int idDoctor;
    private String numeroLicencia;
    private Especialidad especialidad; // Relación con Especialidad
    private LocalDate fechaIngreso;
    private BigDecimal salario;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private String estado;
    
    // Constructores
    public Doctor() {
        super();
        this.estado = "ACTIVO";
    }
    
    public Doctor(String nombre, String apellido, String dni, String telefono, String email,
                  String direccion, LocalDate fechaNacimiento, String numeroLicencia,
                  Especialidad especialidad, LocalDate fechaIngreso, BigDecimal salario,
                  LocalTime horarioInicio, LocalTime horarioFin) {
        super(nombre, apellido, dni, telefono, email, direccion, fechaNacimiento);
        this.numeroLicencia = numeroLicencia;
        this.especialidad = especialidad;
        this.fechaIngreso = fechaIngreso;
        this.salario = salario;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
        this.estado = "ACTIVO";
    }
    
    // Getters y setters específicos de Doctor
    public int getIdDoctor() { return idDoctor; }
    public void setIdDoctor(int idDoctor) { this.idDoctor = idDoctor; }
    
    public String getNumeroLicencia() { return numeroLicencia; }
    public void setNumeroLicencia(String numeroLicencia) { this.numeroLicencia = numeroLicencia; }
    
    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }
    
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    
    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }
    
    public LocalTime getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(LocalTime horarioInicio) { this.horarioInicio = horarioInicio; }
    
    public LocalTime getHorarioFin() { return horarioFin; }
    public void setHorarioFin(LocalTime horarioFin) { this.horarioFin = horarioFin; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    // Métodos específicos de Doctor
    public void atenderPaciente() {
        System.out.println("Dr. " + getNombreCompleto() + " está atendiendo pacientes");
    }
    
    public void realizarCirugia() {
        System.out.println("Dr. " + getNombreCompleto() + " está realizando una cirugía");
    }
    
    public boolean estaDisponible() {
        return "ACTIVO".equals(estado);
    }
    
    public boolean estaEnHorario(LocalTime hora) {
        return hora.isAfter(horarioInicio) && hora.isBefore(horarioFin);
    }
    
    public int getAnosExperiencia() {
        if (fechaIngreso != null) {
            return LocalDate.now().getYear() - fechaIngreso.getYear();
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return super.toString() + " - Dr. " + 
               (especialidad != null ? especialidad.getNombreEspecialidad() : "Sin especialidad") +
               " - Licencia: " + numeroLicencia + " - Estado: " + estado;
    }
}