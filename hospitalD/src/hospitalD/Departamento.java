package hospitalD;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Clase Departamento del hospital
public class Departamento {
    private int idDepartamento;
    private String nombreDepartamento;
    private String ubicacion;
    private String telefonoInterno;
    private Doctor jefeDepartamento; // Relación con Doctor
    private BigDecimal presupuesto;
    private List<Doctor> doctores; // Agregación - doctores del departamento
    private List<Enfermero> enfermeros; // Agregación - enfermeros del departamento
    
    // Constructores
    public Departamento() {
        this.doctores = new ArrayList<>();
        this.enfermeros = new ArrayList<>();
    }
    
    public Departamento(String nombreDepartamento, String ubicacion, 
                       String telefonoInterno, BigDecimal presupuesto) {
        this();
        this.nombreDepartamento = nombreDepartamento;
        this.ubicacion = ubicacion;
        this.telefonoInterno = telefonoInterno;
        this.presupuesto = presupuesto;
    }
    
    // Getters y setters
    public int getIdDepartamento() { return idDepartamento; }
    public void setIdDepartamento(int idDepartamento) { this.idDepartamento = idDepartamento; }
    
    public String getNombreDepartamento() { return nombreDepartamento; }
    public void setNombreDepartamento(String nombreDepartamento) { this.nombreDepartamento = nombreDepartamento; }
    
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    
    public String getTelefonoInterno() { return telefonoInterno; }
    public void setTelefonoInterno(String telefonoInterno) { this.telefonoInterno = telefonoInterno; }
    
    public Doctor getJefeDepartamento() { return jefeDepartamento; }
    public void setJefeDepartamento(Doctor jefeDepartamento) { this.jefeDepartamento = jefeDepartamento; }
    
    public BigDecimal getPresupuesto() { return presupuesto; }
    public void setPresupuesto(BigDecimal presupuesto) { this.presupuesto = presupuesto; }
    
    public List<Doctor> getDoctores() { return new ArrayList<>(doctores); }
    public void setDoctores(List<Doctor> doctores) { this.doctores = doctores; }
    
    public List<Enfermero> getEnfermeros() { return new ArrayList<>(enfermeros); }
    public void setEnfermeros(List<Enfermero> enfermeros) { this.enfermeros = enfermeros; }
    
    // Métodos para manejar doctores (relación de agregación)
    public void agregarDoctor(Doctor doctor) {
        if (doctor != null && !doctores.contains(doctor)) {
            doctores.add(doctor);
        }
    }
    
    public void removerDoctor(Doctor doctor) {
        doctores.remove(doctor);
    }
    
    public int getCantidadDoctores() {
        return doctores.size();
    }
    
    // Métodos para manejar enfermeros (relación de agregación)
    public void agregarEnfermero(Enfermero enfermero) {
        if (enfermero != null && !enfermeros.contains(enfermero)) {
            enfermeros.add(enfermero);
        }
    }
    
    public void removerEnfermero(Enfermero enfermero) {
        enfermeros.remove(enfermero);
    }
    
    public int getCantidadEnfermeros() {
        return enfermeros.size();
    }
    
    public int getTotalPersonal() {
        return getCantidadDoctores() + getCantidadEnfermeros();
    }
    
    // Métodos específicos del departamento
    public void mostrarInformacionDepartamento() {
        System.out.println("=== Departamento de " + nombreDepartamento + " ===");
        System.out.println("Ubicación: " + ubicacion);
        System.out.println("Teléfono interno: " + telefonoInterno);
        System.out.println("Presupuesto: $" + presupuesto);
        System.out.println("Jefe de Departamento: " + 
                         (jefeDepartamento != null ? jefeDepartamento.getNombreCompleto() : "No asignado"));
        System.out.println("Personal total: " + getTotalPersonal() + 
                         " (Doctores: " + getCantidadDoctores() + 
                         ", Enfermeros: " + getCantidadEnfermeros() + ")");
    }
    
    public List<Doctor> getDoctoresPorEspecialidad(String especialidad) {
        List<Doctor> resultado = new ArrayList<>();
        for (Doctor doctor : doctores) {
            if (doctor.getEspecialidad() != null && 
                doctor.getEspecialidad().getNombreEspecialidad().equalsIgnoreCase(especialidad)) {
                resultado.add(doctor);
            }
        }
        return resultado;
    }
    
    public List<Enfermero> getEnfermerosPorTurno(String turno) {
        List<Enfermero> resultado = new ArrayList<>();
        for (Enfermero enfermero : enfermeros) {
            if (turno.equalsIgnoreCase(enfermero.getTurno())) {
                resultado.add(enfermero);
            }
        }
        return resultado;
    }
    
    @Override
    public String toString() {
        return "Departamento de " + nombreDepartamento + " - " + ubicacion + 
               " - Personal: " + getTotalPersonal() + " - Presupuesto: $" + presupuesto;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Departamento that = (Departamento) obj;
        return idDepartamento == that.idDepartamento;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idDepartamento);
    }
}