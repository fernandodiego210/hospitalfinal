package hospitalD;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;

// Clase SignosVitales para registro de constantes vitales de pacientes
public class SignosVitales {
    private int idSignosVitales;
    private Paciente paciente; // Relación con Paciente
    private Enfermero enfermero; // Relación con Enfermero que toma los signos
    private LocalDateTime fechaRegistro;
    private Integer presionSistolica;
    private Integer presionDiastolica;
    private Integer frecuenciaCardiaca;
    private Integer frecuenciaRespiratoria;
    private BigDecimal temperatura;
    private Integer saturacionOxigeno;
    private BigDecimal peso;
    private BigDecimal altura;
    private String observaciones;
    
    // Constructores
    public SignosVitales() {
        this.fechaRegistro = LocalDateTime.now();
    }
    
    public SignosVitales(Paciente paciente, Enfermero enfermero) {
        this();
        this.paciente = paciente;
        this.enfermero = enfermero;
    }
    
    // Getters y setters
    public int getIdSignosVitales() { return idSignosVitales; }
    public void setIdSignosVitales(int idSignosVitales) { this.idSignosVitales = idSignosVitales; }
    
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    
    public Enfermero getEnfermero() { return enfermero; }
    public void setEnfermero(Enfermero enfermero) { this.enfermero = enfermero; }
    
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    public Integer getPresionSistolica() { return presionSistolica; }
    public void setPresionSistolica(Integer presionSistolica) { this.presionSistolica = presionSistolica; }
    
    public Integer getPresionDiastolica() { return presionDiastolica; }
    public void setPresionDiastolica(Integer presionDiastolica) { this.presionDiastolica = presionDiastolica; }
    
    public Integer getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) { this.frecuenciaCardiaca = frecuenciaCardiaca; }
    
    public Integer getFrecuenciaRespiratoria() { return frecuenciaRespiratoria; }
    public void setFrecuenciaRespiratoria(Integer frecuenciaRespiratoria) { this.frecuenciaRespiratoria = frecuenciaRespiratoria; }
    
    public BigDecimal getTemperatura() { return temperatura; }
    public void setTemperatura(BigDecimal temperatura) { this.temperatura = temperatura; }
    
    public Integer getSaturacionOxigeno() { return saturacionOxigeno; }
    public void setSaturacionOxigeno(Integer saturacionOxigeno) { this.saturacionOxigeno = saturacionOxigeno; }
    
    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }
    
    public BigDecimal getAltura() { return altura; }
    public void setAltura(BigDecimal altura) { this.altura = altura; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    
    // Métodos de evaluación clínica
    public String evaluarPresionArterial() {
        if (presionSistolica == null || presionDiastolica == null) {
            return "NO_REGISTRADO";
        }
        
        if (presionSistolica < 90 || presionDiastolica < 60) {
            return "HIPOTENSION"; // Presión baja
        } else if (presionSistolica >= 180 || presionDiastolica >= 110) {
            return "HIPERTENSION_SEVERA"; // Crisis hipertensiva
        } else if (presionSistolica >= 140 || presionDiastolica >= 90) {
            return "HIPERTENSION"; // Presión alta
        } else if (presionSistolica >= 120 || presionDiastolica >= 80) {
            return "PREHIPERTENSION"; // Pre-hipertensión
        } else {
            return "NORMAL"; // Presión normal
        }
    }
    
    public String evaluarFrecuenciaCardiaca() {
        if (frecuenciaCardiaca == null) return "NO_REGISTRADO";
        
        if (frecuenciaCardiaca < 60) {
            return "BRADICARDIA"; // Frecuencia cardíaca baja
        } else if (frecuenciaCardiaca > 100) {
            return "TAQUICARDIA"; // Frecuencia cardíaca alta
        } else {
            return "NORMAL"; // Frecuencia normal (60-100 lpm)
        }
    }
    
    public String evaluarTemperatura() {
        if (temperatura == null) return "NO_REGISTRADO";
        
        double temp = temperatura.doubleValue();
        
        if (temp < 36.0) {
            return "HIPOTERMIA"; // Temperatura baja
        } else if (temp >= 38.0) {
            return "FIEBRE"; // Fiebre
        } else if (temp >= 37.5) {
            return "FEBRICULА"; // Febrícula
        } else {
            return "NORMAL"; // Temperatura normal (36.0-37.4°C)
        }
    }
    
    public String evaluarSaturacionOxigeno() {
        if (saturacionOxigeno == null) return "NO_REGISTRADO";
        
        if (saturacionOxigeno < 90) {
            return "HIPOXEMIA_SEVERA"; // Saturación muy baja
        } else if (saturacionOxigeno < 95) {
            return "HIPOXEMIA_LEVE"; // Saturación baja
        } else {
            return "NORMAL"; // Saturación normal (≥95%)
        }
    }
    
    public String evaluarFrecuenciaRespiratoria() {
        if (frecuenciaRespiratoria == null) return "NO_REGISTRADO";
        
        if (frecuenciaRespiratoria < 12) {
            return "BRADIPNEA"; // Respiración lenta
        } else if (frecuenciaRespiratoria > 20) {
            return "TAQUIPNEA"; // Respiración rápida
        } else {
            return "NORMAL"; // Frecuencia normal (12-20 rpm)
        }
    }
    
    // Métodos de cálculo
    public BigDecimal calcularIMC() {
        if (peso == null || altura == null || 
            altura.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        
        // IMC = peso (kg) / altura² (m²)
        BigDecimal alturaMetros = altura.divide(BigDecimal.valueOf(100)); // cm a metros
        BigDecimal alturaAlCuadrado = alturaMetros.multiply(alturaMetros);
        
        return peso.divide(alturaAlCuadrado, 2, BigDecimal.ROUND_HALF_UP);
    }
    
    public String clasificarIMC() {
        BigDecimal imc = calcularIMC();
        if (imc == null) return "NO_CALCULABLE";
        
        double imcValor = imc.doubleValue();
        
        if (imcValor < 18.5) {
            return "BAJO_PESO";
        } else if (imcValor < 25.0) {
            return "PESO_NORMAL";
        } else if (imcValor < 30.0) {
            return "SOBREPESO";
        } else if (imcValor < 35.0) {
            return "OBESIDAD_I";
        } else if (imcValor < 40.0) {
            return "OBESIDAD_II";
        } else {
            return "OBESIDAD_III";
        }
    }
    
    public String getPresionArterial() {
        if (presionSistolica == null || presionDiastolica == null) {
            return "No registrado";
        }
        return presionSistolica + "/" + presionDiastolica + " mmHg";
    }
    
    // Métodos de evaluación general
    public boolean tieneSignosAlarmantes() {
        return "HIPERTENSION_SEVERA".equals(evaluarPresionArterial()) ||
               "HIPOTENSION".equals(evaluarPresionArterial()) ||
               "FIEBRE".equals(evaluarTemperatura()) ||
               "HIPOXEMIA_SEVERA".equals(evaluarSaturacionOxigeno()) ||
               "HIPOXEMIA_LEVE".equals(evaluarSaturacionOxigeno()) ||
               (frecuenciaCardiaca != null && (frecuenciaCardiaca < 50 || frecuenciaCardiaca > 120)) ||
               (frecuenciaRespiratoria != null && (frecuenciaRespiratoria < 10 || frecuenciaRespiratoria > 25));
    }
    
    public String getNivelGravedad() {
        int puntajeRiesgo = 0;
        
        // Evaluar cada signo vital
        String evalPA = evaluarPresionArterial();
        if ("HIPERTENSION_SEVERA".equals(evalPA) || "HIPOTENSION".equals(evalPA)) puntajeRiesgo += 3;
        else if ("HIPERTENSION".equals(evalPA)) puntajeRiesgo += 2;
        else if ("PREHIPERTENSION".equals(evalPA)) puntajeRiesgo += 1;
        
        String evalTemp = evaluarTemperatura();
        if ("FIEBRE".equals(evalTemp)) puntajeRiesgo += 2;
        else if ("FEBRICULА".equals(evalTemp)) puntajeRiesgo += 1;
        else if ("HIPOTERMIA".equals(evalTemp)) puntajeRiesgo += 3;
        
        String evalSat = evaluarSaturacionOxigeno();
        if ("HIPOXEMIA_SEVERA".equals(evalSat)) puntajeRiesgo += 3;
        else if ("HIPOXEMIA_LEVE".equals(evalSat)) puntajeRiesgo += 2;
        
        if (frecuenciaCardiaca != null) {
            if (frecuenciaCardiaca < 50 || frecuenciaCardiaca > 120) puntajeRiesgo += 2;
            else if (frecuenciaCardiaca < 60 || frecuenciaCardiaca > 100) puntajeRiesgo += 1;
        }
        
        if (puntajeRiesgo >= 6) return "CRITICO";
        else if (puntajeRiesgo >= 4) return "ALTO_RIESGO";
        else if (puntajeRiesgo >= 2) return "MODERADO";
        else return "ESTABLE";
    }
    
    public String getResumenSignosVitales() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        return "=== SIGNOS VITALES ===\n" +
               "Paciente: " + (paciente != null ? paciente.getNombreCompleto() : "No asignado") + "\n" +
               "Registrado por: " + (enfermero != null ? enfermero.getNombreCompleto() : "No registrado") + "\n" +
               "Fecha/Hora: " + fechaRegistro.format(formatter) + "\n\n" +
               "SIGNOS VITALES:\n" +
               "• Presión Arterial: " + getPresionArterial() + " - " + evaluarPresionArterial() + "\n" +
               "• Frecuencia Cardíaca: " + (frecuenciaCardiaca != null ? frecuenciaCardiaca + " lpm" : "No registrado") + 
               " - " + evaluarFrecuenciaCardiaca() + "\n" +
               "• Frecuencia Respiratoria: " + (frecuenciaRespiratoria != null ? frecuenciaRespiratoria + " rpm" : "No registrado") + 
               " - " + evaluarFrecuenciaRespiratoria() + "\n" +
               "• Temperatura: " + (temperatura != null ? temperatura + "°C" : "No registrado") + 
               " - " + evaluarTemperatura() + "\n" +
               "• Saturación O2: " + (saturacionOxigeno != null ? saturacionOxigeno + "%" : "No registrado") + 
               " - " + evaluarSaturacionOxigeno() + "\n\n" +
               "MEDIDAS ANTROPOMÉTRICAS:\n" +
               "• Peso: " + (peso != null ? peso + " kg" : "No registrado") + "\n" +
               "• Altura: " + (altura != null ? altura + " cm" : "No registrado") + "\n" +
               "• IMC: " + (calcularIMC() != null ? calcularIMC() + " (" + clasificarIMC() + ")" : "No calculable") + "\n\n" +
               "EVALUACIÓN GENERAL: " + getNivelGravedad() + 
               (tieneSignosAlarmantes() ? " ⚠️ REQUIERE ATENCIÓN" : " ✅") + "\n" +
               (observaciones != null ? "\nObservaciones: " + observaciones : "");
    }
    
    public String getAlertasSignosVitales() {
        StringBuilder alertas = new StringBuilder();
        
        if (tieneSignosAlarmantes()) {
            alertas.append("🚨 SIGNOS VITALES ALTERADOS\n");
        }
        
        String evalPA = evaluarPresionArterial();
        if ("HIPERTENSION_SEVERA".equals(evalPA)) {
            alertas.append("🩸 CRISIS HIPERTENSIVA - Atención inmediata\n");
        } else if ("HIPOTENSION".equals(evalPA)) {
            alertas.append("📉 HIPOTENSIÓN - Vigilar estado hemodinámico\n");
        }
        
        String evalSat = evaluarSaturacionOxigeno();
        if ("HIPOXEMIA_SEVERA".equals(evalSat)) {
            alertas.append("🫁 HIPOXEMIA SEVERA - Oxígeno suplementario urgente\n");
        } else if ("HIPOXEMIA_LEVE".equals(evalSat)) {
            alertas.append("💨 SATURACIÓN BAJA - Monitorear respiración\n");
        }
        
        String evalTemp = evaluarTemperatura();
        if ("FIEBRE".equals(evalTemp)) {
            alertas.append("🌡️ FIEBRE - Investigar causa, antipiréticos\n");
        } else if ("HIPOTERMIA".equals(evalTemp)) {
            alertas.append("🧊 HIPOTERMIA - Medidas de calentamiento\n");
        }
        
        if (frecuenciaCardiaca != null) {
            if (frecuenciaCardiaca < 50) {
                alertas.append("💓 BRADICARDIA SEVERA - ECG urgente\n");
            } else if (frecuenciaCardiaca > 120) {
                alertas.append("⚡ TAQUICARDIA SEVERA - Evaluar causas\n");
            }
        }
        
        String nivelGravedad = getNivelGravedad();
        if ("CRITICO".equals(nivelGravedad)) {
            alertas.append("🚨 ESTADO CRÍTICO - Notificar médico inmediatamente\n");
        } else if ("ALTO_RIESGO".equals(nivelGravedad)) {
            alertas.append("⚠️ ALTO RIESGO - Monitoreo continuo\n");
        }
        
        return alertas.length() > 0 ? alertas.toString().trim() : "✅ Signos vitales dentro de parámetros normales";
    }
    
    @Override
    public String toString() {
        return "Signos Vitales - " + 
               (paciente != null ? paciente.getNombreCompleto() : "Sin paciente") + 
               " - " + fechaRegistro.format(DateTimeFormatter.ofPattern("dd/MM HH:mm")) + 
               " - PA: " + getPresionArterial() + 
               ", FC: " + (frecuenciaCardiaca != null ? frecuenciaCardiaca + "lpm" : "N/R") + 
               ", T°: " + (temperatura != null ? temperatura + "°C" : "N/R") + 
               " - " + getNivelGravedad();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SignosVitales that = (SignosVitales) obj;
        return idSignosVitales == that.idSignosVitales;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idSignosVitales);
    }
}