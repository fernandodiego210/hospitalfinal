package hospitalD;

import java.math.BigDecimal;
import java.time.LocalDate;

// Clase Medicamento ampliada con más funcionalidades
public class MedicamentoAmpliado {
    private int idMedicamento;
    private String nombreMedicamento;
    private String principioActivo;
    private String presentacion;
    private String concentracion;
    private int stockActual;
    private int stockMinimo;
    private BigDecimal precioUnitario;
    private LocalDate fechaVencimiento;
    private Proveedor proveedor; // Relación con Proveedor
    private String lote;
    private String temperaturaAlmacenamiento;
    private String contraindicaciones;
    private String efectosSecundarios;
    
    // Constructores
    public MedicamentoAmpliado() {
        this.stockMinimo = 10;
    }
    
    public MedicamentoAmpliado(String nombreMedicamento, String principioActivo, 
                              String presentacion, String concentracion,
                              int stockActual, BigDecimal precioUnitario) {
        this();
        this.nombreMedicamento = nombreMedicamento;
        this.principioActivo = principioActivo;
        this.presentacion = presentacion;
        this.concentracion = concentracion;
        this.stockActual = stockActual;
        this.precioUnitario = precioUnitario;
    }
    
    // Getters y setters
    public int getIdMedicamento() { return idMedicamento; }
    public void setIdMedicamento(int idMedicamento) { this.idMedicamento = idMedicamento; }
    
    public String getNombreMedicamento() { return nombreMedicamento; }
    public void setNombreMedicamento(String nombreMedicamento) { this.nombreMedicamento = nombreMedicamento; }
    
    public String getPrincipioActivo() { return principioActivo; }
    public void setPrincipioActivo(String principioActivo) { this.principioActivo = principioActivo; }
    
    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }
    
    public String getConcentracion() { return concentracion; }
    public void setConcentracion(String concentracion) { this.concentracion = concentracion; }
    
    public int getStockActual() { return stockActual; }
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }
    
    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }
    
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    
    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }
    
    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }
    
    public String getTemperaturaAlmacenamiento() { return temperaturaAlmacenamiento; }
    public void setTemperaturaAlmacenamiento(String temperaturaAlmacenamiento) { 
        this.temperaturaAlmacenamiento = temperaturaAlmacenamiento; 
    }
    
    public String getContraindicaciones() { return contraindicaciones; }
    public void setContraindicaciones(String contraindicaciones) { this.contraindicaciones = contraindicaciones; }
    
    public String getEfectosSecundarios() { return efectosSecundarios; }
    public void setEfectosSecundarios(String efectosSecundarios) { this.efectosSecundarios = efectosSecundarios; }
    
    // Métodos específicos del medicamento
    public boolean tieneStockDisponible(int cantidadSolicitada) {
        return stockActual >= cantidadSolicitada;
    }
    
    public boolean requiereReposicion() {
        return stockActual <= stockMinimo;
    }
    
    public boolean estaVencido() {
        return fechaVencimiento != null && fechaVencimiento.isBefore(LocalDate.now());
    }
    
    public boolean estaPorVencer(int diasAnticipacion) {
        if (fechaVencimiento == null) return false;
        LocalDate fechaLimite = LocalDate.now().plusDays(diasAnticipacion);
        return fechaVencimiento.isBefore(fechaLimite);
    }
    
    public int getDiasParaVencimiento() {
        if (fechaVencimiento == null) return Integer.MAX_VALUE;
        return (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
    }
    
    public BigDecimal getValorTotalInventario() {
        return precioUnitario.multiply(BigDecimal.valueOf(stockActual));
    }
    
    public String getEstadoStock() {
        if (estaVencido()) return "VENCIDO";
        if (estaPorVencer(30)) return "POR_VENCER";
        if (stockActual == 0) return "AGOTADO";
        if (requiereReposicion()) return "STOCK_BAJO";
        return "DISPONIBLE";
    }
    
    public void actualizarStock(int cantidad, String tipoMovimiento) {
        int stockAnterior = this.stockActual;
        
        switch (tipoMovimiento.toUpperCase()) {
            case "ENTRADA":
                this.stockActual += cantidad;
                System.out.println("Entrada de " + cantidad + " unidades. Stock: " + 
                                 stockAnterior + " → " + this.stockActual);
                break;
            case "SALIDA":
                if (this.stockActual >= cantidad) {
                    this.stockActual -= cantidad;
                    System.out.println("Salida de " + cantidad + " unidades. Stock: " + 
                                     stockAnterior + " → " + this.stockActual);
                } else {
                    System.out.println("Error: Stock insuficiente para la salida solicitada");
                }
                break;
            case "AJUSTE":
                this.stockActual = cantidad;
                System.out.println("Ajuste de stock. Stock: " + stockAnterior + " → " + this.stockActual);
                break;
            default:
                System.out.println("Tipo de movimiento no válido: " + tipoMovimiento);
        }
    }
    
    public boolean requiereRefrigeracion() {
        return temperaturaAlmacenamiento != null && 
               (temperaturaAlmacenamiento.toLowerCase().contains("refriger") || 
                temperaturaAlmacenamiento.toLowerCase().contains("2-8") ||
                temperaturaAlmacenamiento.toLowerCase().contains("frío"));
    }
    
    public String getAlertasSeguridad() {
        StringBuilder alertas = new StringBuilder();
        
        if (estaVencido()) {
            alertas.append("⚠️ MEDICAMENTO VENCIDO - No dispensar\n");
        } else if (estaPorVencer(30)) {
            alertas.append("⚠️ Vence en ").append(getDiasParaVencimiento()).append(" días\n");
        }
        
        if (stockActual == 0) {
            alertas.append("🚫 STOCK AGOTADO\n");
        } else if (requiereReposicion()) {
            alertas.append("📦 STOCK BAJO - Reabastecer\n");
        }
        
        if (requiereRefrigeracion()) {
            alertas.append("❄️ REQUIERE REFRIGERACIÓN: ").append(temperaturaAlmacenamiento).append("\n");
        }
        
        return alertas.length() > 0 ? alertas.toString().trim() : "✅ Sin alertas";
    }
    
    public String getInformacionCompleta() {
        return "=== " + nombreMedicamento.toUpperCase() + " ===\n" +
               "Principio Activo: " + (principioActivo != null ? principioActivo : "N/A") + "\n" +
               "Presentación: " + (presentacion != null ? presentacion : "N/A") + "\n" +
               "Concentración: " + (concentracion != null ? concentracion : "N/A") + "\n" +
               "Stock Actual: " + stockActual + " | Mínimo: " + stockMinimo + "\n" +
               "Precio Unitario: $" + precioUnitario + "\n" +
               "Lote: " + (lote != null ? lote : "N/A") + "\n" +
               "Vencimiento: " + (fechaVencimiento != null ? fechaVencimiento : "N/A") + "\n" +
               "Proveedor: " + (proveedor != null ? proveedor.getNombreProveedor() : "N/A") + "\n" +
               "Almacenamiento: " + (temperaturaAlmacenamiento != null ? temperaturaAlmacenamiento : "Ambiente") + "\n" +
               "Estado: " + getEstadoStock() + "\n" +
               "Valor Total: $" + getValorTotalInventario() + "\n" +
               "Alertas: " + getAlertasSeguridad();
    }
    
    @Override
    public String toString() {
        return nombreMedicamento + " (" + concentracion + ") - " +
               "Stock: " + stockActual + " - " +
               "Estado: " + getEstadoStock() + " - " +
               "$" + precioUnitario;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MedicamentoAmpliado that = (MedicamentoAmpliado) obj;
        return idMedicamento == that.idMedicamento;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idMedicamento);
    }
}