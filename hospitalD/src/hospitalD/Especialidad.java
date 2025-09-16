package hospitalD;

//Clase Especialidad médica
public class Especialidad {
 private int idEspecialidad;
 private String nombreEspecialidad;
 private String descripcion;
 
 // Constructores
 public Especialidad() {}
 
 public Especialidad(String nombreEspecialidad, String descripcion) {
     this.nombreEspecialidad = nombreEspecialidad;
     this.descripcion = descripcion;
 }
 
 public Especialidad(int idEspecialidad, String nombreEspecialidad, String descripcion) {
     this(nombreEspecialidad, descripcion);
     this.idEspecialidad = idEspecialidad;
 }
 
 // Getters y setters
 public int getIdEspecialidad() { return idEspecialidad; }
 public void setIdEspecialidad(int idEspecialidad) { this.idEspecialidad = idEspecialidad; }
 
 public String getNombreEspecialidad() { return nombreEspecialidad; }
 public void setNombreEspecialidad(String nombreEspecialidad) { this.nombreEspecialidad = nombreEspecialidad; }
 
 public String getDescripcion() { return descripcion; }
 public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
 
 @Override
 public String toString() {
     return nombreEspecialidad + " (ID: " + idEspecialidad + ")";
 }
 
 @Override
 public boolean equals(Object obj) {
     if (this == obj) return true;
     if (obj == null || getClass() != obj.getClass()) return false;
     Especialidad that = (Especialidad) obj;
     return idEspecialidad == that.idEspecialidad;
 }
 
 @Override
 public int hashCode() {
     return Integer.hashCode(idEspecialidad);
 }
}