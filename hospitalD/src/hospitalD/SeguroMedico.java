package hospitalD;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Clase SeguroMedico para gestionar seguros de salud
public class SeguroMedico {
    private int idSeguro;
    private String nombreSeguro;
    private String tipoSeguro; // EPS, PARTICULAR, ESTATAL
    private BigDecimal coberturaPorcentaje;
    private String telefonoSeguro;
    private String emailSeguro;
    private String estado;
    private List<String> coberturas; // Lista de servicios cubiertos
    private List<String> exclusiones; // Lista de servicios no cubiertos
    private BigDecimal limiteAnual; // Límite de cobertura anual
    
    // Constructores
    public SeguroMedico() {
        this.estado = "ACTIVO";
        this.coberturaPorcentaje = BigDecimal.valueOf(70); // 70% por defecto
        this.coberturas = new ArrayList<>();
        this.exclusiones = new ArrayList<>();
    }
    
    public SeguroMedico(String nombreSeguro, String tipoSeguro, BigDecimal coberturaPorcentaje) {
        this();
        this.nombreSeguro = nombreSeguro;
        this.tipoSeguro = tipoSeguro;
        this.coberturaPorcentaje = coberturaPorcentaje;
    }
    
    // Getters y setters
    public int getIdSeguro() { return idSeguro; }
    public void setIdSeguro(int idSeguro) { this.idSeguro = idSeguro; }
    
    public String getNombreSeguro() { return nombreSeguro; }
    public void setNombreSeguro(String nombreSeguro) { this.nombreSeguro = nombreSeguro; }
    
    public String getTipoSeguro() { return tipoSeguro; }
    public void setTipoSeguro(String tipoSeguro) { this.tipoSeguro = tipoSeguro; }
    
    public BigDecimal getCoberturaPorcentaje() { return coberturaPorcentaje; }
    public void setCoberturaPorcentaje(BigDecimal coberturaPorcentaje) { this.coberturaPorcentaje = coberturaPorcentaje; }
    
    public String getTelefonoSeguro() { return telefonoSeguro; }
    public void setTelefonoSeguro(String telefonoSeguro) { this.telefonoSeguro = telefonoSeguro; }
    
    public String getEmailSeguro() { return emailSeguro; }
    public void setEmailSeguro(String emailSeguro) { this.emailSeguro = emailSeguro; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public List<String> getCoberturas() { return new ArrayList<>(coberturas); }
    public void setCoberturas(List<String> coberturas) { this.coberturas = coberturas; }
    
    public List<String> getExclusiones() { return new ArrayList<>(exclusiones); }
    public void setExclusiones(List<String> exclusiones) { this.exclusiones = exclusiones; }
    
    public BigDecimal getLimiteAnual() { return limiteAnual; }
    public void setLimiteAnual(BigDecimal limiteAnual) { this.limiteAnual = limiteAnual; }
    
    // Métodos de clasificación
    public boolean esEPS() {
        return "EPS".equalsIgnoreCase(tipoSeguro);
    }
    
    public boolean esParticular() {
        return "PARTICULAR".equalsIgnoreCase(tipoSeguro);
    }
    
    public boolean esEstatal() {
        return "ESTATAL".equalsIgnoreCase(tipoSeguro);
    }
    
    public boolean estaActivo() {
        return "ACTIVO".equalsIgnoreCase(estado);
    }
    
    // Métodos de cobertura
    public void agregarCobertura(String servicio) {
        if (servicio != null && !servicio.trim().isEmpty() && !coberturas.contains(servicio)) {
            coberturas.add(servicio.trim());
        }
    }
    
    public void removerCobertura(String servicio) {
        coberturas.remove(servicio);
    }
    
    public void agregarExclusion(String servicio) {
        if (servicio != null && !servicio.trim().isEmpty() && !exclusiones.contains(servicio)) {
            exclusiones.add(servicio.trim());
        }
    }
    
    public void removerExclusion(String servicio) {
        exclusiones.remove(servicio);
    }
    
    public boolean cubreServicio(String servicio) {
        if (!estaActivo()) return false;
        if (exclusiones.contains(servicio)) return false;
        
        // Si no hay coberturas específicas definidas, asume cobertura general
        if (coberturas.isEmpty()) return true;
        
        return coberturas.contains(servicio) || 
               coberturas.stream().anyMatch(c -> c.toLowerCase().contains(servicio.toLowerCase()));
    }
    
    public BigDecimal calcularMontoCobertura(BigDecimal montoTotal, String tipoServicio) {
        if (!cubreServicio(tipoServicio)) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal montoCobertura = montoTotal.multiply(coberturaPorcentaje.divide(BigDecimal.valueOf(100)));
        
        // Aplicar límite anual si existe
        if (limiteAnual != null && montoCobertura.compareTo(limiteAnual) > 0) {
            montoCobertura = limiteAnual;
        }
        
        return montoCobertura.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    public BigDecimal calcularCopago(BigDecimal montoTotal, String tipoServicio) {
        BigDecimal montoCobertura = calcularMontoCobertura(montoTotal, tipoServicio);
        return montoTotal.subtract(montoCobertura);
    }
    
    // Métodos de información
    public String getNivelCobertura() {
        double porcentaje = coberturaPorcentaje.doubleValue();
        
        if (porcentaje >= 90) return "PREMIUM";
        else if (porcentaje >= 80) return "ALTO";
        else if (porcentaje >= 70) return "MEDIO";
        else if (porcentaje >= 50) return "BASICO";
        else return "LIMITADO";
    }
    
    public void configurarCoberturasPorTipo() {
        coberturas.clear();
        
        switch (tipoSeguro.toUpperCase()) {
            case "ESTATAL":
                // Cobertura amplia del seguro estatal
                coberturas.add("Consulta médica general");
                coberturas.add("Emergencias");
                coberturas.add("Hospitalización");
                coberturas.add("Cirugías");
                coberturas.add("Medicamentos esenciales");
                coberturas.add("Exámenes de laboratorio");
                coberturas.add("Radiografías");
                coberturas.add("Partos");
                coberturas.add("Pediatría");
                
                exclusiones.add("Cirugía estética");
                exclusiones.add("Tratamientos experimentales");
                exclusiones.add("Medicina alternativa");
                break;
                
            case "EPS":
                // Cobertura típica de EPS
                coberturas.add("Consulta médica general");
                coberturas.add("Consulta especializada");
                coberturas.add("Emergencias");
                coberturas.add("Hospitalización");
                coberturas.add("Cirugías programadas");
                coberturas.add("Medicamentos formulados");
                coberturas.add("Exámenes diagnósticos");
                coberturas.add("Fisioterapia");
                
                exclusiones.add("Cirugía estética");
                exclusiones.add("Odontología estética");
                exclusiones.add("Lentes de contacto");
                break;
                
            case "PARTICULAR":
                // Cobertura amplia de seguro particular
                coberturas.add("Consulta médica general");
                coberturas.add("Consulta especializada");
                coberturas.add("Emergencias 24/7");
                coberturas.add("Hospitalización privada");
                coberturas.add("Cirugías complejas");
                coberturas.add("Medicamentos de alto costo");
                coberturas.add("Exámenes especializados");
                coberturas.add("Tratamientos oncológicos");
                coberturas.add("Medicina preventiva");
                
                // Menos exclusiones en seguros particulares
                exclusiones.add("Tratamientos experimentales sin aprobación");
                break;
        }
    }
    
    public String getInformacionCompleta() {
        return "=== SEGURO MÉDICO ===\n" +
               "Nombre: " + nombreSeguro + "\n" +
               "Tipo: " + tipoSeguro + "\n" +
               "Cobertura: " + coberturaPorcentaje + "% (" + getNivelCobertura() + ")\n" +
               "Estado: " + estado + "\n" +
               "Límite anual: " + (limiteAnual != null ? "$" + limiteAnual : "Sin límite") + "\n" +
               "Teléfono: " + (telefonoSeguro != null ? telefonoSeguro : "No registrado") + "\n" +
               "Email: " + (emailSeguro != null ? emailSeguro : "No registrado") + "\n\n" +
               "SERVICIOS CUBIERTOS (" + coberturas.size() + "):\n" +
               (coberturas.isEmpty() ? "• Cobertura general según porcentaje" : 
                coberturas.stream().map(c -> "• " + c).reduce("", (a, b) -> a + b + "\n")) + "\n" +
               (exclusiones.isEmpty() ? "" : 
                "EXCLUSIONES (" + exclusiones.size() + "):\n" +
                exclusiones.stream().map(e -> "• " + e).reduce("", (a, b) -> a + b + "\n"));
    }
    
    public String simularCobertura(BigDecimal monto, String servicio) {
        BigDecimal cobertura = calcularMontoCobertura(monto, servicio);
        BigDecimal copago = calcularCopago(monto, servicio);
        
        return "=== SIMULACIÓN DE COBERTURA ===\n" +
               "Servicio: " + servicio + "\n" +
               "Monto total: $" + monto + "\n" +
               "Cobertura del seguro: $" + cobertura + " (" + coberturaPorcentaje + "%)\n" +
               "Copago del paciente: $" + copago + "\n" +
               "¿Servicio cubierto?: " + (cubreServicio(servicio) ? "SÍ ✅" : "NO ❌");
    }
    
    @Override
    public String toString() {
        return nombreSeguro + " (" + tipoSeguro + ") - " + 
               coberturaPorcentaje + "% - " + estado + 
               " - Nivel: " + getNivelCobertura();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SeguroMedico that = (SeguroMedico) obj;
        return idSeguro == that.idSeguro;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idSeguro);
    }
}

// Clase PacienteSeguro para la relación entre Paciente y SeguroMedico
class PacienteSeguro {
    private int idPacienteSeguro;
    private Paciente paciente;
    private SeguroMedico seguro;
    private String numeroPoliza;
    private LocalDate fechaInicio;
    private LocalDate fechaVencimiento;
    private String estado;
    private BigDecimal montoUtilizadoAño;
    
    // Constructores
    public PacienteSeguro() {
        this.estado = "ACTIVO";
        this.montoUtilizadoAño = BigDecimal.ZERO;
    }
    
    public PacienteSeguro(Paciente paciente, SeguroMedico seguro, String numeroPoliza) {
        this();
        this.paciente = paciente;
        this.seguro = seguro;
        this.numeroPoliza = numeroPoliza;
    }
    
    // Getters y setters
    public int getIdPacienteSeguro() { return idPacienteSeguro; }
    public void setIdPacienteSeguro(int idPacienteSeguro) { this.idPacienteSeguro = idPacienteSeguro; }
    
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    
    public SeguroMedico getSeguro() { return seguro; }
    public void setSeguro(SeguroMedico seguro) { this.seguro = seguro; }
    
    public String getNumeroPoliza() { return numeroPoliza; }
    public void setNumeroPoliza(String numeroPoliza) { this.numeroPoliza = numeroPoliza; }
    
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public BigDecimal getMontoUtilizadoAño() { return montoUtilizadoAño; }
    public void setMontoUtilizadoAño(BigDecimal montoUtilizadoAño) { this.montoUtilizadoAño = montoUtilizadoAño; }
    
    // Métodos de validación
    public boolean estaVigente() {
        return "ACTIVO".equals(estado) && 
               (fechaVencimiento == null || fechaVencimiento.isAfter(LocalDate.now()));
    }
    
    public boolean estaVencido() {
        return fechaVencimiento != null && fechaVencimiento.isBefore(LocalDate.now());
    }
    
    public boolean tieneCobertura(String servicio) {
        return estaVigente() && seguro.cubreServicio(servicio);
    }
    
    @Override
    public String toString() {
        return numeroPoliza + " - " + 
               (seguro != null ? seguro.getNombreSeguro() : "Sin seguro") + 
               " - " + estado + 
               (estaVencido() ? " (VENCIDO)" : "");
    }
}