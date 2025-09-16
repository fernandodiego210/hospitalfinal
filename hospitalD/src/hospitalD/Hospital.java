package hospitalD;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

// Clase principal Hospital que gestiona todo el sistema
public class Hospital {
    private int idHospital;
    private String nombreHospital;
    private String direccion;
    private String telefono;
    private String email;
    private List<Departamento> departamentos; // Agregación
    private List<Doctor> doctores; // Agregación
    private List<Paciente> pacientes; // Agregación
    private List<Enfermero> enfermeros; // Agregación
    private List<Habitacion> habitaciones; // Composición
    private List<Cita> citas; // Composición
    private List<Especialidad> especialidades; // Agregación
    
    // Constructor
    public Hospital() {
        this.departamentos = new ArrayList<>();
        this.doctores = new ArrayList<>();
        this.pacientes = new ArrayList<>();
        this.enfermeros = new ArrayList<>();
        this.habitaciones = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.especialidades = new ArrayList<>();
    }
    
    public Hospital(String nombreHospital, String direccion, String telefono, String email) {
        this();
        this.nombreHospital = nombreHospital;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }
    
    // Getters y setters básicos
    public int getIdHospital() { return idHospital; }
    public void setIdHospital(int idHospital) { this.idHospital = idHospital; }
    
    public String getNombreHospital() { return nombreHospital; }
    public void setNombreHospital(String nombreHospital) { this.nombreHospital = nombreHospital; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    // Getters para las listas (devuelven copias para encapsulación)
    public List<Departamento> getDepartamentos() { return new ArrayList<>(departamentos); }
    public List<Doctor> getDoctores() { return new ArrayList<>(doctores); }
    public List<Paciente> getPacientes() { return new ArrayList<>(pacientes); }
    public List<Enfermero> getEnfermeros() { return new ArrayList<>(enfermeros); }
    public List<Habitacion> getHabitaciones() { return new ArrayList<>(habitaciones); }
    public List<Cita> getCitas() { return new ArrayList<>(citas); }
    public List<Especialidad> getEspecialidades() { return new ArrayList<>(especialidades); }
    
    // Métodos para gestionar Especialidades
    public void agregarEspecialidad(Especialidad especialidad) {
        if (especialidad != null && !especialidades.contains(especialidad)) {
            especialidades.add(especialidad);
        }
    }
    
    public Especialidad buscarEspecialidadPorNombre(String nombre) {
        return especialidades.stream()
                .filter(e -> e.getNombreEspecialidad().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }
    
    // Métodos para gestionar Doctores
    public void agregarDoctor(Doctor doctor) {
        if (doctor != null && !doctores.contains(doctor)) {
            doctores.add(doctor);
        }
    }
    
    public void removerDoctor(Doctor doctor) {
        doctores.remove(doctor);
    }
    
    public Doctor buscarDoctorPorLicencia(String numeroLicencia) {
        return doctores.stream()
                .filter(d -> d.getNumeroLicencia().equals(numeroLicencia))
                .findFirst()
                .orElse(null);
    }
    
    public List<Doctor> getDoctoresPorEspecialidad(String especialidad) {
        return doctores.stream()
                .filter(d -> d.getEspecialidad() != null && 
                            d.getEspecialidad().getNombreEspecialidad().equalsIgnoreCase(especialidad))
                .collect(Collectors.toList());
    }
    
    public List<Doctor> getDoctoresDisponibles() {
        return doctores.stream()
                .filter(Doctor::estaDisponible)
                .collect(Collectors.toList());
    }
    
    // Métodos para gestionar Pacientes
    public void agregarPaciente(Paciente paciente) {
        if (paciente != null && !pacientes.contains(paciente)) {
            pacientes.add(paciente);
        }
    }
    
    public void removerPaciente(Paciente paciente) {
        pacientes.remove(paciente);
    }
    
    public Paciente buscarPacientePorHistoria(String numeroHistoria) {
        return pacientes.stream()
                .filter(p -> p.getNumeroHistoria().equals(numeroHistoria))
                .findFirst()
                .orElse(null);
    }
    
    public Paciente buscarPacientePorDni(String dni) {
        return pacientes.stream()
                .filter(p -> p.getDni().equals(dni))
                .findFirst()
                .orElse(null);
    }
    
    // Métodos para gestionar Enfermeros
    public void agregarEnfermero(Enfermero enfermero) {
        if (enfermero != null && !enfermeros.contains(enfermero)) {
            enfermeros.add(enfermero);
        }
    }
    
    public void removerEnfermero(Enfermero enfermero) {
        enfermeros.remove(enfermero);
    }
    
    public List<Enfermero> getEnfermerosPorTurno(String turno) {
        return enfermeros.stream()
                .filter(e -> e.getTurno().equalsIgnoreCase(turno))
                .collect(Collectors.toList());
    }
    
    // Métodos para gestionar Departamentos
    public void agregarDepartamento(Departamento departamento) {
        if (departamento != null && !departamentos.contains(departamento)) {
            departamentos.add(departamento);
        }
    }
    
    public void removerDepartamento(Departamento departamento) {
        departamentos.remove(departamento);
    }
    
    public Departamento buscarDepartamentoPorNombre(String nombre) {
        return departamentos.stream()
                .filter(d -> d.getNombreDepartamento().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }
    
    // Métodos para gestionar Habitaciones
    public void agregarHabitacion(Habitacion habitacion) {
        if (habitacion != null && !habitaciones.contains(habitacion)) {
            habitaciones.add(habitacion);
        }
    }
    
    public void removerHabitacion(Habitacion habitacion) {
        habitaciones.remove(habitacion);
    }
    
    public List<Habitacion> getHabitacionesDisponibles() {
        return habitaciones.stream()
                .filter(Habitacion::estaDisponible)
                .collect(Collectors.toList());
    }
    
    public List<Habitacion> getHabitacionesPorTipo(String tipo) {
        return habitaciones.stream()
                .filter(h -> h.getTipoHabitacion().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }
    
    public Habitacion buscarHabitacionPorNumero(String numero) {
        return habitaciones.stream()
                .filter(h -> h.getNumeroHabitacion().equals(numero))
                .findFirst()
                .orElse(null);
    }
    
    // Métodos para gestionar Citas
    public void agendarCita(Cita cita) {
        if (cita != null && !citas.contains(cita)) {
            citas.add(cita);
        }
    }
    
    public void cancelarCita(Cita cita, String motivo) {
        if (cita != null && citas.contains(cita)) {
            cita.cancelarCita(motivo);
        }
    }
    
    public List<Cita> getCitasPorDoctor(Doctor doctor) {
        return citas.stream()
                .filter(c -> c.getDoctor().equals(doctor))
                .collect(Collectors.toList());
    }
    
    public List<Cita> getCitasPorPaciente(Paciente paciente) {
        return citas.stream()
                .filter(c -> c.getPaciente().equals(paciente))
                .collect(Collectors.toList());
    }
    
    public List<Cita> getCitasDeHoy() {
        return citas.stream()
                .filter(Cita::esHoy)
                .collect(Collectors.toList());
    }
    
    public List<Cita> getCitasProgramadas() {
        return citas.stream()
                .filter(Cita::estaProgramada)
                .collect(Collectors.toList());
    }
    
    // Métodos de estadísticas y reportes
    public void mostrarEstadisticasGenerales() {
        System.out.println("=== ESTADÍSTICAS DEL " + nombreHospital.toUpperCase() + " ===");
        System.out.println("Dirección: " + direccion);
        System.out.println("Teléfono: " + telefono);
        System.out.println("Email: " + email);
        System.out.println();
        System.out.println("PERSONAL:");
        System.out.println("- Doctores: " + doctores.size());
        System.out.println("- Enfermeros: " + enfermeros.size());
        System.out.println("- Total personal médico: " + (doctores.size() + enfermeros.size()));
        System.out.println();
        System.out.println("INFRAESTRUCTURA:");
        System.out.println("- Departamentos: " + departamentos.size());
        System.out.println("- Habitaciones totales: " + habitaciones.size());
        System.out.println("- Habitaciones disponibles: " + getHabitacionesDisponibles().size());
        System.out.println("- Habitaciones ocupadas: " + 
                          habitaciones.stream().mapToInt(h -> h.estaOcupada() ? 1 : 0).sum());
        System.out.println();
        System.out.println("ATENCIÓN:");
        System.out.println("- Pacientes registrados: " + pacientes.size());
        System.out.println("- Citas programadas: " + getCitasProgramadas().size());
        System.out.println("- Citas de hoy: " + getCitasDeHoy().size());
        System.out.println("- Especialidades disponibles: " + especialidades.size());
    }
    
    public void mostrarResumenDepartamentos() {
        System.out.println("=== RESUMEN DE DEPARTAMENTOS ===");
        for (Departamento dept : departamentos) {
            dept.mostrarInformacionDepartamento();
            System.out.println();
        }
    }
    
    public void mostrarCitasDelDia() {
        System.out.println("=== CITAS PROGRAMADAS PARA HOY ===");
        List<Cita> citasHoy = getCitasDeHoy();
        if (citasHoy.isEmpty()) {
            System.out.println("No hay citas programadas para hoy.");
        } else {
            for (Cita cita : citasHoy) {
                System.out.println(cita.getResumenCita());
                System.out.println("---");
            }
        }
    }
    
    @Override
    public String toString() {
        return nombreHospital + " - " + direccion + 
               " | Doctores: " + doctores.size() + 
               " | Pacientes: " + pacientes.size() + 
               " | Habitaciones: " + habitaciones.size();
    }
}
