package hospitalD;

import java.time.LocalDateTime;

//Clase Proveedor para medicamentos y equipos médicos
public class Proveedor {
 private int idProveedor;
 private String nombreProveedor;
 private String ruc;
 private String direccion;
 private String telefono;
 private String email;
 private String contactoRepresentante;
 private String estado;
 private LocalDateTime fechaRegistro;
 
 // Constructores
 public Proveedor() {
     this.estado = "ACTIVO";
     this.fechaRegistro = LocalDateTime.now();
 }
 
 public Proveedor(String nombreProveedor, String ruc, String direccion, 
                  String telefono, String email, String contactoRepresentante) {
     this();
     this.nombreProveedor = nombreProveedor;
     this.ruc = ruc;
     this.direccion = direccion;
     this.telefono = telefono;
     this.email = email;
     this.contactoRepresentante = contactoRepresentante;
 }
 
 // Getters y setters
 public int getIdProveedor() { return idProveedor; }
 public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }
 
 public String getNombreProveedor() { return nombreProveedor; }
 public void setNombreProveedor(String nombreProveedor) { this.nombreProveedor = nombreProveedor; }
 
 public String getRuc() { return ruc; }
 public void setRuc(String ruc) { this.ruc = ruc; }
 
 public String getDireccion() { return direccion; }
 public void setDireccion(String direccion) { this.direccion = direccion; }
 
 public String getTelefono() { return telefono; }
 public void setTelefono(String telefono) { this.telefono = telefono; }
 
 public String getEmail() { return email; }
 public void setEmail(String email) { this.email = email; }
 
 public String getContactoRepresentante() { return contactoRepresentante; }
 public void setContactoRepresentante(String contactoRepresentante) { this.contactoRepresentante = contactoRepresentante; }
 
 public String getEstado() { return estado; }
 public void setEstado(String estado) { this.estado = estado; }
 
 public LocalDateTime getFechaRegistro() { return fechaRegistro; }
 public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
 
 // Métodos específicos
 public boolean estaActivo() {
     return "ACTIVO".equalsIgnoreCase(estado);
 }
 
 public void activar() {
     this.estado = "ACTIVO";
     System.out.println("Proveedor " + nombreProveedor + " activado");
 }
 
 public void desactivar() {
     this.estado = "INACTIVO";
     System.out.println("Proveedor " + nombreProveedor + " desactivado");
 }
 
 public String getInformacionContacto() {
     return "Contacto: " + contactoRepresentante + 
            "\nTeléfono: " + telefono + 
            "\nEmail: " + email;
 }
 
 @Override
 public String toString() {
     return nombreProveedor + " (RUC: " + ruc + ") - " + estado + 
            " - Contacto: " + contactoRepresentante;
 }
 
 @Override
 public boolean equals(Object obj) {
     if (this == obj) return true;
     if (obj == null || getClass() != obj.getClass()) return false;
     Proveedor proveedor = (Proveedor) obj;
     return ruc.equals(proveedor.ruc);
 }
 
 @Override
 public int hashCode() {
     return ruc.hashCode();
 }
}