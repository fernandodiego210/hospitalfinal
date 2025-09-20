package hospitalD;

import java.math.BigDecimal;

//Clase TipoExamen para definir los tipos de análisis clínicos
public class TipoExamen {
 private int idTipoExamen;
 private String nombreExamen;
 private String descripcion;
 private BigDecimal precio;
 private int tiempoResultadoHoras;
 private boolean requiereAyuno;
 private String preparacionEspecial;
 private Laboratorio laboratorio; // Relación con Laboratorio
 
 // Constructores
 public TipoExamen() {
     this.tiempoResultadoHoras = 24; // Por defecto 24 horas
     this.requiereAyuno = false;
 }
 
 public TipoExamen(String nombreExamen, String descripcion, BigDecimal precio, 
                  int tiempoResultadoHoras) {
     this();
     this.nombreExamen = nombreExamen;
     this.descripcion = descripcion;
     this.precio = precio;
     this.tiempoResultadoHoras = tiempoResultadoHoras;
 }
 
 public TipoExamen(String nombreExamen, String descripcion, BigDecimal precio, 
                  int tiempoResultadoHoras, boolean requiereAyuno, String preparacionEspecial) {
     this(nombreExamen, descripcion, precio, tiempoResultadoHoras);
     this.requiereAyuno = requiereAyuno;
     this.preparacionEspecial = preparacionEspecial;
 }
 
 // Getters y setters
 public int getIdTipoExamen() { return idTipoExamen; }
 public void setIdTipoExamen(int idTipoExamen) { this.idTipoExamen = idTipoExamen; }
 
 public String getNombreExamen() { return nombreExamen; }
 public void setNombreExamen(String nombreExamen) { this.nombreExamen = nombreExamen; }
 
 public String getDescripcion() { return descripcion; }
 public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
 
 public BigDecimal getPrecio() { return precio; }
 public void setPrecio(BigDecimal precio) { this.precio = precio; }
 
 public int getTiempoResultadoHoras() { return tiempoResultadoHoras; }
 public void setTiempoResultadoHoras(int tiempoResultadoHoras) { this.tiempoResultadoHoras = tiempoResultadoHoras; }
 
 public boolean isRequiereAyuno() { return requiereAyuno; }
 public void setRequiereAyuno(boolean requiereAyuno) { this.requiereAyuno = requiereAyuno; }
 
 public String getPreparacionEspecial() { return preparacionEspecial; }
 public void setPreparacionEspecial(String preparacionEspecial) { this.preparacionEspecial = preparacionEspecial; }
 
 public Laboratorio getLaboratorio() { return laboratorio; }
 public void setLaboratorio(Laboratorio laboratorio) { this.laboratorio = laboratorio; }
 
 // Métodos específicos del tipo de examen
 public boolean esUrgente() {
     return tiempoResultadoHoras <= 2;
 }
 
 public boolean esRapido() {
     return tiempoResultadoHoras <= 4;
 }
 
 public boolean esComplejo() {
     return tiempoResultadoHoras >= 48;
 }
 
 public String getCategoriaTiempo() {
     if (esUrgente()) return "URGENTE";
     if (esRapido()) return "RAPIDO";
     if (esComplejo()) return "COMPLEJO";
     return "NORMAL";
 }
 
 public String getInstruccionesPreparacion() {
     StringBuilder instrucciones = new StringBuilder();
     
     if (requiereAyuno) {
         instrucciones.append("🍽️ AYUNO REQUERIDO: No comer ni beber (excepto agua) 8-12 horas antes del examen.\n");
     }
     
     if (preparacionEspecial != null && !preparacionEspecial.trim().isEmpty()) {
         instrucciones.append("📋 PREPARACIÓN ESPECIAL:\n").append(preparacionEspecial).append("\n");
     }
     
     if (esUrgente()) {
         instrucciones.append("⚡ EXAMEN URGENTE: Resultados en ").append(tiempoResultadoHoras).append(" horas.\n");
     }
     
     if (instrucciones.length() == 0) {
         instrucciones.append("✅ No requiere preparación especial.");
     }
     
     return instrucciones.toString().trim();
 }
 
 public String getInformacionCompleta() {
     return "=== " + nombreExamen.toUpperCase() + " ===\n" +
            "Descripción: " + (descripcion != null ? descripcion : "No disponible") + "\n" +
            "Precio: $" + precio + "\n" +
            "Tiempo de resultado: " + tiempoResultadoHoras + " horas\n" +
            "Categoría: " + getCategoriaTiempo() + "\n" +
            "Requiere ayuno: " + (requiereAyuno ? "SÍ" : "NO") + "\n" +
            "Laboratorio: " + (laboratorio != null ? laboratorio.getNombreLaboratorio() : "No asignado") + "\n" +
            "Instrucciones de preparación:\n" + getInstruccionesPreparacion();
 }
 
 public java.time.LocalDateTime calcularFechaResultado(java.time.LocalDateTime fechaOrden) {
     return fechaOrden.plusHours(tiempoResultadoHoras);
 }
 
 public boolean esCostoso() {
     return precio.compareTo(BigDecimal.valueOf(100)) > 0;
 }
 
 public String getNivelCosto() {
     if (precio.compareTo(BigDecimal.valueOf(20)) <= 0) return "ECONOMICO";
     if (precio.compareTo(BigDecimal.valueOf(100)) <= 0) return "MODERADO";
     return "COSTOSO";
 }
 
 @Override
 public String toString() {
     return nombreExamen + " - $" + precio + 
            " (" + tiempoResultadoHoras + "h) - " + getCategoriaTiempo() + 
            (requiereAyuno ? " - Ayuno requerido" : "");
 }
 
 @Override
 public boolean equals(Object obj) {
     if (this == obj) return true;
     if (obj == null || getClass() != obj.getClass()) return false;
     TipoExamen that = (TipoExamen) obj;
     return idTipoExamen == that.idTipoExamen;
 }
 
 @Override
 public int hashCode() {
     return Integer.hashCode(idTipoExamen);
 }
}
