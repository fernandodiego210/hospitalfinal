package hospitalD;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Clase base Persona para el sistema hospitalario
public class Persona {
    protected int idPersona;
    protected String nombre;
    protected String apellido;
    protected String dni;
    protected String telefono;
    protected String email;
    protected String direccion;
    protected LocalDate fechaNacimiento;
    protected LocalDateTime fechaRegistro;
    
    // Constructor vacío
    public Persona() {
        this.fechaRegistro = LocalDateTime.now();
    }
    
    // Constructor con parámetros
    public Persona(String nombre, String apellido, String dni, String telefono, 
                   String email, String direccion, LocalDate fechaNacimiento) {
        this();
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
    }
    
    // Getters y setters
    public int getIdPersona() { return idPersona; }
    public void setIdPersona(int idPersona) { this.idPersona = idPersona; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    // Método para obtener nombre completo
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    // Método para calcular edad
    public int getEdad() {
        if (fechaNacimiento != null) {
            return LocalDate.now().getYear() - fechaNacimiento.getYear();
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return "ID: " + idPersona + " - " + getNombreCompleto() + 
               " (DNI: " + dni + ") - Edad: " + getEdad();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Persona persona = (Persona) obj;
        return dni.equals(persona.dni);
    }
    
    @Override
    public int hashCode() {
        return dni.hashCode();
    }
}