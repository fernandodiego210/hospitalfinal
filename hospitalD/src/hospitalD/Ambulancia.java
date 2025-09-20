package hospitalD;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Clase Ambulancia para gestionar servicios de emergencia móvil
public class Ambulancia {
    private int idAmbulancia;
    private String placa;
    private String modelo;
    private int año;
    private String tipoAmbulancia; // BASICA, INTERMEDIA, AVANZADA
    private String equiposMedicos;
    private int capacidadPacientes;
    private String estado; // DISPONIBLE, EN_SERVICIO, MANTENIMIENTO, FUERA_DE_SERVICIO
    private String ubicacionActual;
    private List<String> historialServicios; // Historial de servicios realizados
    private LocalDateTime ultimoServicio;
    private int kilometraje;
    
    // Constructores
    public Ambulancia() {
        this.capacidadPacientes = 2;
        this.estado = "DISPONIBLE";
        this.historialServicios = new ArrayList<>();
        this.kilometraje = 0;
    }
    
    public Ambulancia(String placa, String modelo, String tipoAmbulancia, int capacidadPacientes) {
        this();
        this.placa = placa;
        this.modelo = modelo;
        this.tipoAmbulancia = tipoAmbulancia;
        this.capacidadPacientes = capacidadPacientes;
    }
    
    // Getters y setters
    public int getIdAmbulancia() { return idAmbulancia; }
    public void setIdAmbulancia(int idAmbulancia) { this.idAmbulancia = idAmbulancia; }
    
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    
    public int getAño() { return año; }
    public void setAño(int año) { this.año = año; }
    
    public String getTipoAmbulancia() { return tipoAmbulancia; }
    public void setTipoAmbulancia(String tipoAmbulancia) { this.tipoAmbulancia = tipoAmbulancia; }
    
    public String getEquiposMedicos() { return equiposMedicos; }
    public void setEquiposMedicos(String equiposMedicos) { this.equiposMedicos = equiposMedicos; }
    
    public int getCapacidadPacientes() { return capacidadPacientes; }
    public void setCapacidadPacientes(int capacidadPacientes) { this.capacidadPacientes = capacidadPacientes; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getUbicacionActual() { return ubicacionActual; }
    public void setUbicacionActual(String ubicacionActual) { this.ubicacionActual = ubicacionActual; }
    
    public List<String> getHistorialServicios() { return new ArrayList<>(historialServicios); }
    public void setHistorialServicios(List<String> historialServicios) { this.historialServicios = historialServicios; }
    
    public LocalDateTime getUltimoServicio() { return ultimoServicio; }
    public void setUltimoServicio(LocalDateTime ultimoServicio) { this.ultimoServicio = ultimoServicio; }
    
    public int getKilometraje() { return kilometraje; }
    public void setKilometraje(int kilometraje) { this.kilometraje = kilometraje; }
    
    // Métodos de estado
    public boolean estaDisponible() {
        return "DISPONIBLE".equalsIgnoreCase(estado);
    }
    
    public boolean estaEnServicio() {
        return "EN_SERVICIO".equalsIgnoreCase(estado);
    }
    
    public boolean estaEnMantenimiento() {
        return "MANTENIMIENTO".equalsIgnoreCase(estado);
    }
    
    public boolean estaFueraDeServicio() {
        return "FUERA_DE_SERVICIO".equalsIgnoreCase(estado);
    }
    
    // Métodos de clasificación
    public boolean esBasica() {
        return "BASICA".equalsIgnoreCase(tipoAmbulancia);
    }
    
    public boolean esIntermedia() {
        return "INTERMEDIA".equalsIgnoreCase(tipoAmbulancia);
    }
    
    public boolean esAvanzada() {
        return "AVANZADA".equalsIgnoreCase(tipoAmbulancia);
    }
    
    // Métodos de operación
    public boolean despachar(String destino, String tipoEmergencia) {
        if (!estaDisponible()) {
            System.out.println("Ambulancia " + placa + " no está disponible. Estado: " + estado);
            return false;
        }
        
        this.estado = "EN_SERVICIO";
        this.ubicacionActual = "En ruta a: " + destino;
        
        String servicio = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + 
                         " - Despachada a: " + destino + " - Tipo: " + tipoEmergencia;
        historialServicios.add(servicio);
        
        System.out.println("🚑 Ambulancia " + placa + " despachada a: " + destino + 
                          " - Emergencia: " + tipoEmergencia);
        return true;
    }
    
    public void completarServicio(String ubicacionFinal, int kmRecorridos) {
        if (estaEnServicio()) {
            this.estado = "DISPONIBLE";
            this.ubicacionActual = ubicacionFinal;
            this.ultimoServicio = LocalDateTime.now();
            this.kilometraje += kmRecorridos;
            
            // Actualizar el último registro del historial
            if (!historialServicios.isEmpty()) {
                int ultimoIndex = historialServicios.size() - 1;
                String ultimoServicio = historialServicios.get(ultimoIndex);
                historialServicios.set(ultimoIndex, ultimoServicio + " - COMPLETADO: " + 
                                     LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + 
                                     " - KM: " + kmRecorridos);
            }
            
            System.out.println("✅ Servicio completado - Ambulancia " + placa + 
                             " regresó a: " + ubicacionFinal + " - KM recorridos: " + kmRecorridos);
        }
    }
    
    public void ponerEnMantenimiento(String motivoMantenimiento) {
        if (estaDisponible()) {
            this.estado = "MANTENIMIENTO";
            String registro = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + 
                            " - MANTENIMIENTO: " + motivoMantenimiento;
            historialServicios.add(registro);
            
            System.out.println("🔧 Ambulancia " + placa + " en mantenimiento: " + motivoMantenimiento);
        } else {
            System.out.println("No se puede poner en mantenimiento. Estado actual: " + estado);
        }
    }
    
    public void completarMantenimiento() {
        if (estaEnMantenimiento()) {
            this.estado = "DISPONIBLE";
            
            // Actualizar el último registro de mantenimiento
            if (!historialServicios.isEmpty()) {
                int ultimoIndex = historialServicios.size() - 1;
                String ultimoRegistro = historialServicios.get(ultimoIndex);
                if (ultimoRegistro.contains("MANTENIMIENTO:")) {
                    historialServicios.set(ultimoIndex, ultimoRegistro + " - COMPLETADO: " + 
                                         LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
                }
            }
            
            System.out.println("✅ Mantenimiento completado - Ambulancia " + placa + " disponible");
        }
    }
    
    // Métodos informativos
    public boolean puedeAtenderEmergencia(String tipoEmergencia) {
        if (!estaDisponible()) return false;
        
        // Verificar capacidades según tipo de ambulancia
        switch (tipoEmergencia.toUpperCase()) {
            case "CRITICO":
            case "TRAUMA_MAYOR":
            case "PARO_CARDIACO":
                return esAvanzada();
            
            case "URGENTE":
            case "FRACTURA":
            case "QUEMADURA":
                return esIntermedia() || esAvanzada();
            
            case "MODERADO":
            case "TRASLADO":
            case "LEVE":
                return true; // Cualquier tipo puede atender
            
            default:
                return true;
        }
    }
    
    public String getCapacidadesDetalladas() {
        StringBuilder capacidades = new StringBuilder();
        capacidades.append("Tipo: ").append(tipoAmbulancia).append("\n");
        capacidades.append("Capacidad: ").append(capacidadPacientes).append(" paciente(s)\n");
        
        switch (tipoAmbulancia.toUpperCase()) {
            case "BASICA":
                capacidades.append("- Primeros auxilios básicos\n");
                capacidades.append("- Transporte de pacientes estables\n");
                capacidades.append("- Oxígeno básico\n");
                break;
            case "INTERMEDIA":
                capacidades.append("- Soporte vital básico\n");
                capacidades.append("- Desfibrilador semiautomático\n");
                capacidades.append("- Medicamentos básicos\n");
                capacidades.append("- Ventilación asistida\n");
                break;
            case "AVANZADA":
                capacidades.append("- Soporte vital avanzado\n");
                capacidades.append("- Desfibrilador/Monitor\n");
                capacidades.append("- Medicamentos de emergencia\n");
                capacidades.append("- Intubación y ventilación mecánica\n");
                capacidades.append("- Acceso vascular avanzado\n");
                break;
        }
        
        if (equiposMedicos != null && !equiposMedicos.trim().isEmpty()) {
            capacidades.append("Equipos adicionales: ").append(equiposMedicos);
        }
        
        return capacidades.toString();
    }
    
    public int getServiciosRealizadosHoy() {
        String hoy = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return (int) historialServicios.stream()
                .filter(servicio -> servicio.startsWith(hoy))
                .count();
    }
    
    public boolean requiereMantenimiento() {
        // Verificar si requiere mantenimiento por kilometraje
        return kilometraje > 50000 || // Cada 50,000 km
               (ultimoServicio != null && 
                ultimoServicio.isBefore(LocalDateTime.now().minusMonths(3))); // O cada 3 meses
    }
    
    public String getEstadoDetallado() {
        StringBuilder estadoDetalle = new StringBuilder();
        estadoDetalle.append("Estado: ").append(estado);
        
        if (estaDisponible()) {
            estadoDetalle.append(" ✅");
        } else if (estaEnServicio()) {
            estadoDetalle.append(" 🚑");
        } else if (estaEnMantenimiento()) {
            estadoDetalle.append(" 🔧");
        } else {
            estadoDetalle.append(" ⚠️");
        }
        
        if (requiereMantenimiento() && estaDisponible()) {
            estadoDetalle.append(" (Requiere mantenimiento)");
        }
        
        return estadoDetalle.toString();
    }
    
    public void mostrarReporteAmbulancia() {
        System.out.println("=== REPORTE AMBULANCIA " + placa + " ===");
        System.out.println("Modelo: " + modelo + (año > 0 ? " (" + año + ")" : ""));
        System.out.println("Tipo: " + tipoAmbulancia);
        System.out.println("Estado: " + getEstadoDetallado());
        System.out.println("Ubicación actual: " + (ubicacionActual != null ? ubicacionActual : "No especificada"));
        System.out.println("Kilometraje: " + kilometraje + " km");
        System.out.println("Servicios hoy: " + getServiciosRealizadosHoy());
        System.out.println("Total servicios registrados: " + historialServicios.size());
        
        if (ultimoServicio != null) {
            System.out.println("Último servicio: " + ultimoServicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        }
        
        System.out.println("\nCapacidades:");
        System.out.println(getCapacidadesDetalladas());
        
        if (!historialServicios.isEmpty()) {
            System.out.println("\n--- ÚLTIMOS 5 SERVICIOS ---");
            int inicio = Math.max(0, historialServicios.size() - 5);
            for (int i = inicio; i < historialServicios.size(); i++) {
                System.out.println("• " + historialServicios.get(i));
            }
        }
    }
    
    @Override
    public String toString() {
        return "Ambulancia " + placa + " (" + tipoAmbulancia + ") - " + 
               getEstadoDetallado() + " - " + 
               (ubicacionActual != null ? ubicacionActual : "Ubicación no especificada");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ambulancia that = (Ambulancia) obj;
        return placa.equals(that.placa);
    }
    
    @Override
    public int hashCode() {
        return placa.hashCode();
    }
}