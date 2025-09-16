package hospitalD;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Clase Paciente que hereda de Persona
public class Paciente extends Persona {
    private int idPaciente;
    private String numeroHistoria;
    private String tipoSangre;
    private String alergias;
    private String enfermedadesCronicas;
    private String contactoEmergencia;
    private String telefonoEmergencia;
    private String seguroMedico;
    private List<String> historialMedico; // Historial de diagnósticos
    
    // Constructores
    public Paciente() {
        super();
        this.historialMedico = new ArrayList<>();
    }
    
    public Paciente(String nombre, String apellido, String dni, String telefono, String email,
                    String direccion, LocalDate fechaNacimiento, String numeroHistoria,
                    String tipoSangre, String seguroMedico) {
        super(nombre, apellido, dni, telefono, email, direccion, fechaNacimiento);
        this.numeroHistoria = numeroHistoria;
        this.tipoSangre = tipoSangre;
        this.seguroMedico = seguroMedico;
        this.historialMedico = new ArrayList<>();
    }
    
    // Getters y setters específicos de Paciente
    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }
    
    public String getNumeroHistoria() { return numeroHistoria; }
    public void setNumeroHistoria(String numeroHistoria) { this.numeroHistoria = numeroHistoria; }
    
    public String getTipoSangre() { return tipoSangre; }
    public void setTipoSangre(String tipoSangre) { this.tipoSangre = tipoSangre; }
    
    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }
    
    public String getEnfermedadesCronicas() { return enfermedadesCronicas; }
    public void setEnfermedadesCronicas(String enfermedadesCronicas) { this.enfermedadesCronicas = enfermedadesCronicas; }
    
    public String getContactoEmergencia() { return contactoEmergencia; }
    public void setContactoEmergencia(String contactoEmergencia) { this.contactoEmergencia = contactoEmergencia; }
    
    public String getTelefonoEmergencia() { return telefonoEmergencia; }
    public void setTelefonoEmergencia(String telefonoEmergencia) { this.telefonoEmergencia = telefonoEmergencia; }
    
    public String getSeguroMedico() { return seguroMedico; }
    public void setSeguroMedico(String seguroMedico) { this.seguroMedico = seguroMedico; }
    
    public List<String> getHistorialMedico() { return new ArrayList<>(historialMedico); }
    public void setHistorialMedico(List<String> historialMedico) { this.historialMedico = historialMedico; }
    
    // Métodos específicos de Paciente
    public void agregarDiagnostico(String diagnostico) {
        if (diagnostico != null && !diagnostico.trim().isEmpty()) {
            historialMedico.add(LocalDate.now() + ": " + diagnostico);
        }
    }
    
    public void recibirTratamiento(String tratamiento) {
        System.out.println("Paciente " + getNombreCompleto() + " está recibiendo tratamiento: " + tratamiento);
    }
    
    public boolean tieneAlergias() {
        return alergias != null && !alergias.trim().isEmpty() && !"Ninguna".equalsIgnoreCase(alergias.trim());
    }
    
    public boolean tieneEnfermedadesCronicas() {
        return enfermedadesCronicas != null && !enfermedadesCronicas.trim().isEmpty();
    }
    
    public boolean tieneSeguroMedico() {
        return seguroMedico != null && !seguroMedico.trim().isEmpty();
    }
    
    public void mostrarHistorialMedico() {
        System.out.println("=== Historial Médico de " + getNombreCompleto() + " ===");
        if (historialMedico.isEmpty()) {
            System.out.println("Sin historial médico registrado");
        } else {
            for (String registro : historialMedico) {
                System.out.println("- " + registro);
            }
        }
    }
    
    @Override
    public String toString() {
        return super.toString() + " - Historia: " + numeroHistoria + 
               " - Tipo Sangre: " + (tipoSangre != null ? tipoSangre : "No especificado") +
               " - Seguro: " + (seguroMedico != null ? seguroMedico : "Sin seguro");
    }
}