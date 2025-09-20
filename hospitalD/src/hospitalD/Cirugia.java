package hospitalD;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Clase Cirugia para gestionar procedimientos quirúrgicos
public class Cirugia {
    private int idCirugia;
    private Paciente paciente; // Relación con Paciente
    private Doctor doctorCirujano; // Relación con Doctor (cirujano principal)
    private String nombreCirugia;
    private String descripcionProcedimiento;
    private LocalDateTime fechaProgramada;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Habitacion habitacion; // Relación con Habitacion (quirófano)
    private String estadoCirugia; // PROGRAMADA, EN_CURSO, COMPLETADA, CANCELADA, POSPUESTA
    private String tipoAnestesia;
    private String riesgoQuirurgico; // BAJO, MODERADO, ALTO, MUY_ALTO
    private String observacionesPreoperatorias;
    private String observacionesPostoperatorias;
    private List<PersonalCirugia> equipoQuirurgico; // Composición - personal que participa
    
    // Constructores
    public Cirugia() {
        this.estadoCirugia = "PROGRAMADA";
        this.riesgoQuirurgico = "MODERADO";
        this.equipoQuirurgico = new ArrayList<>();
    }
    
    public Cirugia(Paciente paciente, Doctor doctorCirujano, String nombreCirugia, 
                   LocalDateTime fechaProgramada, String tipoAnestesia) {
        this();
        this.paciente = paciente;
        this.doctorCirujano = doctorCirujano;
        this.nombreCirugia = nombreCirugia;
        this.fechaProgramada = fechaProgramada;
        this.tipoAnestesia = tipoAnestesia;
    }
    
    // Getters y setters
    public int getIdCirugia() { return idCirugia; }
    public void setIdCirugia(int idCirugia) { this.idCirugia = idCirugia; }
    
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    
    public Doctor getDoctorCirujano() { return doctorCirujano; }
    public void setDoctorCirujano(Doctor doctorCirujano) { this.doctorCirujano = doctorCirujano; }
    
    public String getNombreCirugia() { return nombreCirugia; }
    public void setNombreCirugia(String nombreCirugia) { this.nombreCirugia = nombreCirugia; }
    
    public String getDescripcionProcedimiento() { return descripcionProcedimiento; }
    public void setDescripcionProcedimiento(String descripcionProcedimiento) { 
        this.descripcionProcedimiento = descripcionProcedimiento; 
    }
    
    public LocalDateTime getFechaProgramada() { return fechaProgramada; }
    public void setFechaProgramada(LocalDateTime fechaProgramada) { this.fechaProgramada = fechaProgramada; }
    
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    
    public Habitacion getHabitacion() { return habitacion; }
    public void setHabitacion(Habitacion habitacion) { this.habitacion = habitacion; }
    
    public String getEstadoCirugia() { return estadoCirugia; }
    public void setEstadoCirugia(String estadoCirugia) { this.estadoCirugia = estadoCirugia; }
    
    public String getTipoAnestesia() { return tipoAnestesia; }
    public void setTipoAnestesia(String tipoAnestesia) { this.tipoAnestesia = tipoAnestesia; }
    
    public String getRiesgoQuirurgico() { return riesgoQuirurgico; }
    public void setRiesgoQuirurgico(String riesgoQuirurgico) { this.riesgoQuirurgico = riesgoQuirurgico; }
    
    public String getObservacionesPreoperatorias() { return observacionesPreoperatorias; }
    public void setObservacionesPreoperatorias(String observacionesPreoperatorias) { 
        this.observacionesPreoperatorias = observacionesPreoperatorias; 
    }
    
    public String getObservacionesPostoperatorias() { return observacionesPostoperatorias; }
    public void setObservacionesPostoperatorias(String observacionesPostoperatorias) { 
        this.observacionesPostoperatorias = observacionesPostoperatorias; 
    }
    
    public List<PersonalCirugia> getEquipoQuirurgico() { return new ArrayList<>(equipoQuirurgico); }
    public void setEquipoQuirurgico(List<PersonalCirugia> equipoQuirurgico) { 
        this.equipoQuirurgico = equipoQuirurgico; 
    }
    
    // Métodos de estado
    public boolean estaProgramada() {
        return "PROGRAMADA".equalsIgnoreCase(estadoCirugia);
    }
    
    public boolean estaEnCurso() {
        return "EN_CURSO".equalsIgnoreCase(estadoCirugia);
    }
    
    public boolean estaCompletada() {
        return "COMPLETADA".equalsIgnoreCase(estadoCirugia);
    }
    
    public boolean estaCancelada() {
        return "CANCELADA".equalsIgnoreCase(estadoCirugia);
    }
    
    public boolean estaPospuesta() {
        return "POSPUESTA".equalsIgnoreCase(estadoCirugia);
    }
    
    // Métodos de clasificación de riesgo
    public boolean esRiesgoBajo() {
        return "BAJO".equalsIgnoreCase(riesgoQuirurgico);
    }
    
    public boolean esRiesgoModerado() {
        return "MODERADO".equalsIgnoreCase(riesgoQuirurgico);
    }
    
    public boolean esRiesgoAlto() {
        return "ALTO".equalsIgnoreCase(riesgoQuirurgico);
    }
    
    public boolean esRiesgoMuyAlto() {
        return "MUY_ALTO".equalsIgnoreCase(riesgoQuirurgico);
    }
    
    // Métodos para gestionar el equipo quirúrgico
    public void agregarPersonalCirugia(PersonalCirugia personal) {
        if (personal != null && !equipoQuirurgico.contains(personal)) {
            equipoQuirurgico.add(personal);
            System.out.println("Personal agregado a cirugía: " + personal.getRolEnCirugia() + 
                             " - " + personal.getNombreCompleto());
        }
    }
    
    public void removerPersonalCirugia(PersonalCirugia personal) {
        if (equipoQuirurgico.remove(personal)) {
            System.out.println("Personal removido de cirugía: " + personal.getRolEnCirugia());
        }
    }
    
    public List<PersonalCirugia> getPersonalPorRol(String rol) {
        return equipoQuirurgico.stream()
                .filter(p -> p.getRolEnCirugia().equalsIgnoreCase(rol))
                .collect(java.util.stream.Collectors.toList());
    }
    
    public boolean tieneAnestesiologo() {
        return equipoQuirurgico.stream()
                .anyMatch(p -> "ANESTESIOLOGO".equalsIgnoreCase(p.getRolEnCirugia()));
    }
    
    public boolean tieneInstrumentista() {
        return equipoQuirurgico.stream()
                .anyMatch(p -> "INSTRUMENTISTA".equalsIgnoreCase(p.getRolEnCirugia()));
    }
    
    // Métodos de operación de la cirugía
    public boolean iniciarCirugia() {
        if (!estaProgramada()) {
            System.out.println("No se puede iniciar cirugía. Estado actual: " + estadoCirugia);
            return false;
        }
        
        // Verificar que tenga personal mínimo requerido
        if (!tienePersonalMinimo()) {
            System.out.println("No se puede iniciar. Falta personal mínimo requerido");
            return false;
        }
        
        this.fechaInicio = LocalDateTime.now();
        this.estadoCirugia = "EN_CURSO";
        
        System.out.println("🏥 Cirugía iniciada: " + nombreCirugia + 
                          " - Paciente: " + paciente.getNombreCompleto() + 
                          " - Cirujano: " + doctorCirujano.getNombreCompleto());
        
        return true;
    }
    
    public void completarCirugia(String observacionesPost) {
        if (estaEnCurso()) {
            this.fechaFin = LocalDateTime.now();
            this.estadoCirugia = "COMPLETADA";
            this.observacionesPostoperatorias = observacionesPost;
            
            System.out.println("✅ Cirugía completada: " + nombreCirugia + 
                             " - Duración: " + getDuracionCirugia() + 
                             " - Paciente: " + paciente.getNombreCompleto());
        } else {
            System.out.println("No se puede completar cirugía. Estado actual: " + estadoCirugia);
        }
    }
    
    public void cancelarCirugia(String motivo) {
        if (estaProgramada()) {
            this.estadoCirugia = "CANCELADA";
            this.observacionesPostoperatorias = "CANCELADA: " + motivo;
            
            System.out.println("❌ Cirugía cancelada: " + nombreCirugia + 
                             " - Motivo: " + motivo + 
                             " - Paciente: " + paciente.getNombreCompleto());
        } else {
            System.out.println("No se puede cancelar. Estado actual: " + estadoCirugia);
        }
    }
    
    public void posponerCirugia(LocalDateTime nuevaFecha, String motivo) {
        if (estaProgramada()) {
            this.fechaProgramada = nuevaFecha;
            this.estadoCirugia = "POSPUESTA";
            
            String observacion = "POSPUESTA: " + motivo + " - Nueva fecha: " + 
                               nuevaFecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            this.observacionesPreoperatorias = (this.observacionesPreoperatorias != null ? 
                                              this.observacionesPreoperatorias + " | " : "") + observacion;
            
            System.out.println("⏰ Cirugía pospuesta: " + nombreCirugia + 
                             " - Nueva fecha: " + nuevaFecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + 
                             " - Motivo: " + motivo);
            
            // Cambiar estado de vuelta a programada para la nueva fecha
            this.estadoCirugia = "PROGRAMADA";
        }
    }
    
    // Métodos informativos
    private boolean tienePersonalMinimo() {
        // Verificar personal mínimo según tipo de cirugía y riesgo
        boolean tieneCirujano = doctorCirujano != null;
        boolean tieneAnestesia = tieneAnestesiologo() || "LOCAL".equalsIgnoreCase(tipoAnestesia);
        boolean tieneInstrumental = tieneInstrumentista();
        
        if (esRiesgoAlto() || esRiesgoMuyAlto()) {
            return tieneCirujano && tieneAnestesia && tieneInstrumental && equipoQuirurgico.size() >= 4;
        } else {
            return tieneCirujano && tieneAnestesia;
        }
    }
    
    public String getDuracionCirugia() {
        if (fechaInicio == null) return "No iniciada";
        
        LocalDateTime fechaFinal = fechaFin != null ? fechaFin : LocalDateTime.now();
        Duration duracion = Duration.between(fechaInicio, fechaFinal);
        
        long horas = duracion.toHours();
        long minutos = duracion.toMinutes() % 60;
        
        if (horas > 0) {
            return horas + "h " + minutos + "m";
        } else {
            return minutos + "m";
        }
    }
    
    public boolean esLarga() {
        if (fechaInicio == null || fechaFin == null) return false;
        Duration duracion = Duration.between(fechaInicio, fechaFin);
        return duracion.toHours() >= 4; // Más de 4 horas se considera larga
    }
    
    public boolean esDeEmergencia() {
        if (fechaProgramada == null) return true; // Si no tiene fecha programada, es emergencia
        Duration diferencia = Duration.between(LocalDateTime.now(), fechaProgramada);
        return diferencia.toHours() <= 24; // Programada en menos de 24 horas
    }
    
    public String getNivelComplejidad() {
        int puntuacion = 0;
        
        // Puntuación por riesgo
        switch (riesgoQuirurgico.toUpperCase()) {
            case "BAJO": puntuacion += 1; break;
            case "MODERADO": puntuacion += 2; break;
            case "ALTO": puntuacion += 3; break;
            case "MUY_ALTO": puntuacion += 4; break;
        }
        
        // Puntuación por tipo de anestesia
        if ("GENERAL".equalsIgnoreCase(tipoAnestesia)) puntuacion += 2;
        else if ("REGIONAL".equalsIgnoreCase(tipoAnestesia)) puntuacion += 1;
        
        // Puntuación por personal requerido
        if (equipoQuirurgico.size() >= 6) puntuacion += 2;
        else if (equipoQuirurgico.size() >= 4) puntuacion += 1;
        
        if (puntuacion <= 2) return "SIMPLE";
        else if (puntuacion <= 4) return "MODERADA";
        else if (puntuacion <= 6) return "COMPLEJA";
        else return "MUY_COMPLEJA";
    }
    
    public String getResumenCirugia() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        return "=== CIRUGÍA #" + idCirugia + " ===\n" +
               "Procedimiento: " + nombreCirugia + "\n" +
               "Paciente: " + (paciente != null ? paciente.getNombreCompleto() : "No asignado") + "\n" +
               "Cirujano: " + (doctorCirujano != null ? doctorCirujano.getNombreCompleto() : "No asignado") + "\n" +
               "Fecha programada: " + (fechaProgramada != null ? fechaProgramada.format(formatter) : "No programada") + "\n" +
               "Estado: " + estadoCirugia + "\n" +
               "Riesgo quirúrgico: " + riesgoQuirurgico + "\n" +
               "Complejidad: " + getNivelComplejidad() + "\n" +
               "Tipo anestesia: " + (tipoAnestesia != null ? tipoAnestesia : "No especificado") + "\n" +
               "Quirófano: " + (habitacion != null ? habitacion.getNumeroHabitacion() : "No asignado") + "\n" +
               "Personal quirúrgico: " + equipoQuirurgico.size() + " personas\n" +
               (fechaInicio != null ? "Inicio real: " + fechaInicio.format(formatter) + "\n" : "") +
               (fechaFin != null ? "Fin: " + fechaFin.format(formatter) + "\n" : "") +
               (estaEnCurso() || estaCompletada() ? "Duración: " + getDuracionCirugia() + "\n" : "") +
               (esDeEmergencia() ? "🚨 CIRUGÍA DE EMERGENCIA\n" : "") +
               (esLarga() ? "⏳ CIRUGÍA PROLONGADA\n" : "") +
               (observacionesPreoperatorias != null ? "Observaciones Pre-op: " + observacionesPreoperatorias + "\n" : "") +
               (observacionesPostoperatorias != null ? "Observaciones Post-op: " + observacionesPostoperatorias : "");
    }
    
    public String getAlertasCirugia() {
        StringBuilder alertas = new StringBuilder();
        
        if (estaProgramada() && !tienePersonalMinimo()) {
            alertas.append("👥 FALTA PERSONAL MÍNIMO REQUERIDO\n");
        }
        
        if (esRiesgoAlto() || esRiesgoMuyAlto()) {
            alertas.append("⚠️ CIRUGÍA DE ALTO RIESGO - Monitoreo especial\n");
        }
        
        if (esDeEmergencia()) {
            alertas.append("🚨 CIRUGÍA DE EMERGENCIA - Prioridad máxima\n");
        }
        
        if (estaEnCurso() && fechaInicio != null) {
            long horasTranscurridas = Duration.between(fechaInicio, LocalDateTime.now()).toHours();
            if (horasTranscurridas >= 6) {
                alertas.append("⏰ CIRUGÍA MUY PROLONGADA - Revisar estado\n");
            }
        }
        
        if (habitacion == null) {
            alertas.append("🏥 SIN QUIRÓFANO ASIGNADO\n");
        }
        
        if (!tieneAnestesiologo() && !"LOCAL".equalsIgnoreCase(tipoAnestesia)) {
            alertas.append("💉 FALTA ANESTESIÓLOGO\n");
        }
        
        return alertas.length() > 0 ? alertas.toString().trim() : "✅ Sin alertas críticas";
    }
    
    public void mostrarEquipoQuirurgico() {
        System.out.println("=== EQUIPO QUIRÚRGICO - CIRUGÍA #" + idCirugia + " ===");
        System.out.println("Cirujano Principal: " + 
                          (doctorCirujano != null ? doctorCirujano.getNombreCompleto() : "No asignado"));
        
        if (equipoQuirurgico.isEmpty()) {
            System.out.println("No hay personal adicional asignado");
        } else {
            System.out.println("\nPersonal adicional:");
            for (PersonalCirugia personal : equipoQuirurgico) {
                System.out.println("• " + personal.getRolEnCirugia() + ": " + personal.getNombreCompleto());
            }
        }
        
        System.out.println("\nVerificación de personal mínimo: " + 
                          (tienePersonalMinimo() ? "✅ COMPLETO" : "❌ INCOMPLETO"));
    }
    
    @Override
    public String toString() {
        return "Cirugía #" + idCirugia + " - " + nombreCirugia + 
               " - " + (paciente != null ? paciente.getNombreCompleto() : "Sin paciente") + 
               " - " + estadoCirugia + " (" + riesgoQuirurgico + " riesgo)" + 
               " - " + (fechaProgramada != null ? 
                       fechaProgramada.format(DateTimeFormatter.ofPattern("dd/MM HH:mm")) : "Sin fecha");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cirugia cirugia = (Cirugia) obj;
        return idCirugia == cirugia.idCirugia;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idCirugia);
    }
}