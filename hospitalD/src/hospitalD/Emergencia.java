package hospitalD;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

// Clase Emergencia para gestionar casos de urgencias médicas
public class Emergencia {
    private int idEmergencia;
    private Paciente paciente; // Relación con Paciente
    private LocalDateTime fechaIngreso;
    private String sintomasPrincipales;
    private String nivelPrioridad; // CRITICO, URGENTE, MODERADO, LEVE
    private String estadoEmergencia; // EN_ATENCION, ESTABLE, DERIVADO, ALTA, FALLECIDO
    private Doctor doctorAsignado; // Relación con Doctor
    private Ambulancia ambulancia; // Relación con Ambulancia (si llegó en ambulancia)
    private int tiempoEsperaMinutos;
    private String observaciones;
    private String tratamientoAplicado;
    private LocalDateTime fechaAlta;
    
    // Constructores
    public Emergencia() {
        this.fechaIngreso = LocalDateTime.now();
        this.estadoEmergencia = "EN_ATENCION";
        this.nivelPrioridad = "MODERADO";
    }
    
    public Emergencia(Paciente paciente, String sintomasPrincipales, String nivelPrioridad) {
        this();
        this.paciente = paciente;
        this.sintomasPrincipales = sintomasPrincipales;
        this.nivelPrioridad = nivelPrioridad;
    }
    
    // Getters y setters
    public int getIdEmergencia() { return idEmergencia; }
    public void setIdEmergencia(int idEmergencia) { this.idEmergencia = idEmergencia; }
    
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    
    public LocalDateTime getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDateTime fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    
    public String getSintomasPrincipales() { return sintomasPrincipales; }
    public void setSintomasPrincipales(String sintomasPrincipales) { this.sintomasPrincipales = sintomasPrincipales; }
    
    public String getNivelPrioridad() { return nivelPrioridad; }
    public void setNivelPrioridad(String nivelPrioridad) { this.nivelPrioridad = nivelPrioridad; }
    
    public String getEstadoEmergencia() { return estadoEmergencia; }
    public void setEstadoEmergencia(String estadoEmergencia) { this.estadoEmergencia = estadoEmergencia; }
    
    public Doctor getDoctorAsignado() { return doctorAsignado; }
    public void setDoctorAsignado(Doctor doctorAsignado) { this.doctorAsignado = doctorAsignado; }
    
    public Ambulancia getAmbulancia() { return ambulancia; }
    public void setAmbulancia(Ambulancia ambulancia) { this.ambulancia = ambulancia; }
    
    public int getTiempoEsperaMinutos() { return tiempoEsperaMinutos; }
    public void setTiempoEsperaMinutos(int tiempoEsperaMinutos) { this.tiempoEsperaMinutos = tiempoEsperaMinutos; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    public String getTratamientoAplicado() { return tratamientoAplicado; }
    public void setTratamientoAplicado(String tratamientoAplicado) { this.tratamientoAplicado = tratamientoAplicado; }
    
    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }
    
    // Métodos de clasificación de prioridad
    public boolean esCritico() {
        return "CRITICO".equalsIgnoreCase(nivelPrioridad);
    }
    
    public boolean esUrgente() {
        return "URGENTE".equalsIgnoreCase(nivelPrioridad);
    }
    
    public boolean esModerado() {
        return "MODERADO".equalsIgnoreCase(nivelPrioridad);
    }
    
    public boolean esLeve() {
        return "LEVE".equalsIgnoreCase(nivelPrioridad);
    }
    
    // Métodos de estado
    public boolean estaEnAtencion() {
        return "EN_ATENCION".equalsIgnoreCase(estadoEmergencia);
    }
    
    public boolean estaEstable() {
        return "ESTABLE".equalsIgnoreCase(estadoEmergencia);
    }
    
    public boolean fueDerivado() {
        return "DERIVADO".equalsIgnoreCase(estadoEmergencia);
    }
    
    public boolean tuvoAlta() {
        return "ALTA".equalsIgnoreCase(estadoEmergencia);
    }
    
    // Métodos de gestión de la emergencia
    public void asignarDoctor(Doctor doctor) {
        this.doctorAsignado = doctor;
        System.out.println("Doctor asignado a emergencia #" + idEmergencia + ": " + doctor.getNombreCompleto());
        
        // Calcular tiempo de espera hasta la asignación
        if (fechaIngreso != null) {
            this.tiempoEsperaMinutos = (int) Duration.between(fechaIngreso, LocalDateTime.now()).toMinutes();
        }
    }
    
    public void actualizarEstado(String nuevoEstado, String observacion) {
        String estadoAnterior = this.estadoEmergencia;
        this.estadoEmergencia = nuevoEstado;
        
        if (observacion != null && !observacion.trim().isEmpty()) {
            this.observaciones = (this.observaciones != null ? this.observaciones + " | " : "") + 
                               LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + 
                               ": " + observacion;
        }
        
        System.out.println("Estado de emergencia #" + idEmergencia + " actualizado: " + 
                          estadoAnterior + " → " + nuevoEstado);
        
        // Si es alta, registrar fecha
        if ("ALTA".equalsIgnoreCase(nuevoEstado)) {
            this.fechaAlta = LocalDateTime.now();
        }
    }
    
    public void aplicarTratamiento(String tratamiento) {
        if (tratamiento != null && !tratamiento.trim().isEmpty()) {
            this.tratamientoAplicado = (this.tratamientoAplicado != null ? 
                                      this.tratamientoAplicado + " | " : "") + 
                                     LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + 
                                     ": " + tratamiento;
            
            System.out.println("Tratamiento aplicado en emergencia #" + idEmergencia + ": " + tratamiento);
        }
    }
    
    public void darAlta(String motivoAlta) {
        if (!tuvoAlta()) {
            this.estadoEmergencia = "ALTA";
            this.fechaAlta = LocalDateTime.now();
            
            String observacionAlta = "ALTA: " + motivoAlta;
            this.observaciones = (this.observaciones != null ? this.observaciones + " | " : "") + 
                               LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + 
                               ": " + observacionAlta;
            
            System.out.println("Alta médica para emergencia #" + idEmergencia + 
                             " - Paciente: " + paciente.getNombreCompleto() + 
                             " - Motivo: " + motivoAlta);
        }
    }
    
    // Métodos de tiempo y métricas
    public long getMinutosEnEmergencia() {
        LocalDateTime fechaFin = fechaAlta != null ? fechaAlta : LocalDateTime.now();
        return Duration.between(fechaIngreso, fechaFin).toMinutes();
    }
    
    public long getHorasEnEmergencia() {
        return getMinutosEnEmergencia() / 60;
    }
    
    public String getTiempoTranscurrido() {
        long minutos = getMinutosEnEmergencia();
        long horas = minutos / 60;
        long mins = minutos % 60;
        
        if (horas > 0) {
            return horas + "h " + mins + "m";
        }
        return mins + "m";
    }
    
    public boolean excedeTimepoEspera() {
        int tiempoMaximo = getTiempoMaximoEsperaSegunPrioridad();
        return tiempoEsperaMinutos > tiempoMaximo;
    }
    
    private int getTiempoMaximoEsperaSegunPrioridad() {
        switch (nivelPrioridad.toUpperCase()) {
            case "CRITICO": return 0;    // Inmediato
            case "URGENTE": return 15;   // 15 minutos máximo
            case "MODERADO": return 60;  // 1 hora máximo
            case "LEVE": return 240;     // 4 horas máximo
            default: return 120;
        }
    }
    
    public String getColorPrioridad() {
        switch (nivelPrioridad.toUpperCase()) {
            case "CRITICO": return "🔴 ROJO";
            case "URGENTE": return "🟡 AMARILLO";
            case "MODERADO": return "🟢 VERDE";
            case "LEVE": return "🔵 AZUL";
            default: return "⚪ SIN CLASIFICAR";
        }
    }
    
    // Métodos informativos
    public boolean arriboEnAmbulancia() {
        return ambulancia != null;
    }
    
    public String getResumenEmergencia() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        return "=== EMERGENCIA #" + idEmergencia + " ===\n" +
               "Paciente: " + (paciente != null ? paciente.getNombreCompleto() : "No identificado") + "\n" +
               "Ingreso: " + fechaIngreso.format(formatter) + "\n" +
               "Prioridad: " + nivelPrioridad + " " + getColorPrioridad() + "\n" +
               "Estado: " + estadoEmergencia + "\n" +
               "Síntomas: " + (sintomasPrincipales != null ? sintomasPrincipales : "No especificados") + "\n" +
               "Doctor asignado: " + (doctorAsignado != null ? doctorAsignado.getNombreCompleto() : "Sin asignar") + "\n" +
               "Tiempo en emergencia: " + getTiempoTranscurrido() + "\n" +
               "Tiempo espera inicial: " + tiempoEsperaMinutos + " minutos\n" +
               (arriboEnAmbulancia() ? "Arribó en ambulancia: " + ambulancia.getPlaca() + "\n" : "") +
               (excedeTimepoEspera() ? "⚠️ EXCEDE TIEMPO DE ESPERA RECOMENDADO\n" : "") +
               (fechaAlta != null ? "Fecha alta: " + fechaAlta.format(formatter) + "\n" : "") +
               (tratamientoAplicado != null ? "Tratamientos: " + tratamientoAplicado + "\n" : "") +
               (observaciones != null ? "Observaciones: " + observaciones : "");
    }
    
    public String getAlertasEmergencia() {
        StringBuilder alertas = new StringBuilder();
        
        if (esCritico() && tiempoEsperaMinutos > 5) {
            alertas.append("🚨 CRÍTICO SIN ATENCIÓN INMEDIATA\n");
        }
        
        if (excedeTimepoEspera()) {
            alertas.append("⏰ EXCEDE TIEMPO DE ESPERA RECOMENDADO\n");
        }
        
        if (estaEnAtencion() && getHorasEnEmergencia() > 12) {
            alertas.append("📅 MÁS DE 12 HORAS EN EMERGENCIA\n");
        }
        
        if (doctorAsignado == null && !esCritico()) {
            alertas.append("👨‍⚕️ SIN DOCTOR ASIGNADO\n");
        }
        
        if (esCritico() || esUrgente()) {
            alertas.append("🔥 PRIORIDAD ALTA - Seguimiento continuo\n");
        }
        
        return alertas.length() > 0 ? alertas.toString().trim() : "✅ Sin alertas críticas";
    }
    
    public int getScoreTriage() {
        // Sistema de puntuación para triage (0-10, donde 10 es más crítico)
        int score = 0;
        
        switch (nivelPrioridad.toUpperCase()) {
            case "CRITICO": score += 8; break;
            case "URGENTE": score += 6; break;
            case "MODERADO": score += 4; break;
            case "LEVE": score += 2; break;
        }
        
        // Ajustar por tiempo de espera
        if (tiempoEsperaMinutos > getTiempoMaximoEsperaSegunPrioridad()) {
            score += 2;
        }
        
        // Ajustar por edad del paciente (si es muy joven o muy mayor)
        if (paciente != null) {
            int edad = paciente.getEdad();
            if (edad < 5 || edad > 80) {
                score += 1;
            }
        }
        
        return Math.min(10, score);
    }
    
    @Override
    public String toString() {
        return "Emergencia #" + idEmergencia + " - " +
               (paciente != null ? paciente.getNombreCompleto() : "Sin identificar") +
               " - " + nivelPrioridad + " (" + getColorPrioridad() + ") - " +
               estadoEmergencia + " - " + getTiempoTranscurrido();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Emergencia that = (Emergencia) obj;
        return idEmergencia == that.idEmergencia;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idEmergencia);
    }
}