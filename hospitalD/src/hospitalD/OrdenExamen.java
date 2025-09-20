package hospitalD;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Clase OrdenExamen para gestionar órdenes de análisis clínicos
public class OrdenExamen {
    private int idOrdenExamen;
    private Cita cita; // Relación con Cita
    private Paciente paciente; // Relación con Paciente
    private Doctor doctor; // Relación con Doctor
    private TipoExamen tipoExamen; // Relación con TipoExamen
    private LocalDateTime fechaOrden;
    private LocalDateTime fechaTomamuestra;
    private LocalDateTime fechaResultado;
    private String estadoOrden; // PENDIENTE, TOMADA, PROCESANDO, COMPLETADA
    private String observaciones;
    private String resultados;
    private String valoresReferencia;
    
    // Constructores
    public OrdenExamen() {
        this.fechaOrden = LocalDateTime.now();
        this.estadoOrden = "PENDIENTE";
    }
    
    public OrdenExamen(Cita cita, Paciente paciente, Doctor doctor, TipoExamen tipoExamen) {
        this();
        this.cita = cita;
        this.paciente = paciente;
        this.doctor = doctor;
        this.tipoExamen = tipoExamen;
    }
    
    // Getters y setters
    public int getIdOrdenExamen() { return idOrdenExamen; }
    public void setIdOrdenExamen(int idOrdenExamen) { this.idOrdenExamen = idOrdenExamen; }
    
    public Cita getCita() { return cita; }
    public void setCita(Cita cita) { this.cita = cita; }
    
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    
    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    
    public TipoExamen getTipoExamen() { return tipoExamen; }
    public void setTipoExamen(TipoExamen tipoExamen) { this.tipoExamen = tipoExamen; }
    
    public LocalDateTime getFechaOrden() { return fechaOrden; }
    public void setFechaOrden(LocalDateTime fechaOrden) { this.fechaOrden = fechaOrden; }
    
    public LocalDateTime getFechaTomamuestra() { return fechaTomamuestra; }
    public void setFechaTomamuestra(LocalDateTime fechaTomamuestra) { this.fechaTomamuestra = fechaTomamuestra; }
    
    public LocalDateTime getFechaResultado() { return fechaResultado; }
    public void setFechaResultado(LocalDateTime fechaResultado) { this.fechaResultado = fechaResultado; }
    
    public String getEstadoOrden() { return estadoOrden; }
    public void setEstadoOrden(String estadoOrden) { this.estadoOrden = estadoOrden; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    public String getResultados() { return resultados; }
    public void setResultados(String resultados) { this.resultados = resultados; }
    
    public String getValoresReferencia() { return valoresReferencia; }
    public void setValoresReferencia(String valoresReferencia) { this.valoresReferencia = valoresReferencia; }
    
    // Métodos de estado
    public boolean estaPendiente() {
        return "PENDIENTE".equalsIgnoreCase(estadoOrden);
    }
    
    public boolean estaTomada() {
        return "TOMADA".equalsIgnoreCase(estadoOrden);
    }
    
    public boolean estaProcesando() {
        return "PROCESANDO".equalsIgnoreCase(estadoOrden);
    }
    
    public boolean estaCompletada() {
        return "COMPLETADA".equalsIgnoreCase(estadoOrden);
    }
    
    // Métodos de operación
    public void tomarMuestra() {
        if (estaPendiente()) {
            this.fechaTomamuestra = LocalDateTime.now();
            this.estadoOrden = "TOMADA";
            System.out.println("Muestra tomada para examen: " + tipoExamen.getNombreExamen() +
                             " - Paciente: " + paciente.getNombreCompleto());
        } else {
            System.out.println("No se puede tomar muestra. Estado actual: " + estadoOrden);
        }
    }
    
    public void iniciarProcesamiento() {
        if (estaTomada()) {
            this.estadoOrden = "PROCESANDO";
            System.out.println("Iniciado procesamiento de: " + tipoExamen.getNombreExamen());
        } else {
            System.out.println("No se puede procesar. Se requiere toma de muestra primero");
        }
    }
    
    public void completarExamen(String resultados) {
        if (estaProcesando() || estaTomada()) {
            this.resultados = resultados;
            this.fechaResultado = LocalDateTime.now();
            this.estadoOrden = "COMPLETADA";
            System.out.println("Examen completado: " + tipoExamen.getNombreExamen() +
                             " - Paciente: " + paciente.getNombreCompleto());
        } else {
            System.out.println("No se puede completar el examen. Estado actual: " + estadoOrden);
        }
    }
    
    public void cancelarOrden(String motivo) {
        if (!estaCompletada()) {
            this.estadoOrden = "CANCELADA";
            this.observaciones = (observaciones != null ? observaciones + " | " : "") + 
                               "CANCELADA: " + motivo;
            System.out.println("Orden cancelada: " + tipoExamen.getNombreExamen() + 
                             ". Motivo: " + motivo);
        }
    }
    
    // Métodos de tiempo
    public LocalDateTime getFechaEstimadaResultado() {
        if (fechaOrden != null && tipoExamen != null) {
            return fechaOrden.plusHours(tipoExamen.getTiempoResultadoHoras());
        }
        return null;
    }
    
    public boolean estaVencida() {
        LocalDateTime fechaEstimada = getFechaEstimadaResultado();
        return fechaEstimada != null && LocalDateTime.now().isAfter(fechaEstimada) && !estaCompletada();
    }
    
    public long getHorasTranscurridas() {
        if (fechaOrden == null) return 0;
        return java.time.Duration.between(fechaOrden, LocalDateTime.now()).toHours();
    }
    
    public long getHorasRestantes() {
        LocalDateTime fechaEstimada = getFechaEstimadaResultado();
        if (fechaEstimada == null || estaCompletada()) return 0;
        
        long horas = java.time.Duration.between(LocalDateTime.now(), fechaEstimada).toHours();
        return Math.max(0, horas);
    }
    
    public String getEstadoTiempo() {
        if (estaCompletada()) return "COMPLETADO";
        if (estaVencida()) return "VENCIDO";
        
        long horasRestantes = getHorasRestantes();
        if (horasRestantes <= 2) return "URGENTE";
        if (horasRestantes <= 6) return "PRÓXIMO";
        return "EN_TIEMPO";
    }
    
    // Métodos informativos
    public boolean requiereAyuno() {
        return tipoExamen != null && tipoExamen.isRequiereAyuno();
    }
    
    public String getInstruccionesPreparacion() {
        if (tipoExamen != null) {
            return tipoExamen.getInstruccionesPreparacion();
        }
        return "No hay instrucciones específicas";
    }
    
    public String getResumenOrden() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        return "=== ORDEN DE EXAMEN #" + idOrdenExamen + " ===\n" +
               "Paciente: " + (paciente != null ? paciente.getNombreCompleto() : "No asignado") + "\n" +
               "Doctor: " + (doctor != null ? doctor.getNombreCompleto() : "No asignado") + "\n" +
               "Examen: " + (tipoExamen != null ? tipoExamen.getNombreExamen() : "No especificado") + "\n" +
               "Fecha orden: " + (fechaOrden != null ? fechaOrden.format(formatter) : "No registrada") + "\n" +
               "Estado: " + estadoOrden + " (" + getEstadoTiempo() + ")\n" +
               "Fecha estimada resultado: " + 
               (getFechaEstimadaResultado() != null ? getFechaEstimadaResultado().format(formatter) : "No disponible") + "\n" +
               "Horas transcurridas: " + getHorasTranscurridas() + "h\n" +
               "Horas restantes: " + getHorasRestantes() + "h\n" +
               (requiereAyuno() ? "⚠️ REQUIERE AYUNO\n" : "") +
               (observaciones != null ? "Observaciones: " + observaciones + "\n" : "") +
               (resultados != null ? "Resultados: " + resultados + "\n" : "");
    }
    
    public String getAlertasOrden() {
        StringBuilder alertas = new StringBuilder();
        
        if (estaVencida()) {
            alertas.append("🚨 ORDEN VENCIDA - Revisar inmediatamente\n");
        } else if ("URGENTE".equals(getEstadoTiempo())) {
            alertas.append("⚡ URGENTE - Menos de 2 horas restantes\n");
        } else if ("PRÓXIMO".equals(getEstadoTiempo())) {
            alertas.append("⏰ PRÓXIMO A VENCER - Menos de 6 horas\n");
        }
        
        if (requiereAyuno() && estaPendiente()) {
            alertas.append("🍽️ VERIFICAR AYUNO antes de toma de muestra\n");
        }
        
        if (tipoExamen != null && tipoExamen.esUrgente()) {
            alertas.append("⚡ EXAMEN URGENTE - Prioridad alta\n");
        }
        
        return alertas.length() > 0 ? alertas.toString().trim() : "✅ Sin alertas";
    }
    
    @Override
    public String toString() {
        return "Orden #" + idOrdenExamen + " - " +
               (tipoExamen != null ? tipoExamen.getNombreExamen() : "Examen no especificado") +
               " - " + (paciente != null ? paciente.getNombreCompleto() : "Paciente no asignado") +
               " - Estado: " + estadoOrden + " (" + getEstadoTiempo() + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OrdenExamen that = (OrdenExamen) obj;
        return idOrdenExamen == that.idOrdenExamen;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idOrdenExamen);
    }
}