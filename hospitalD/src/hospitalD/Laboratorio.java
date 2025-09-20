package hospitalD;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// Clase Laboratorio para análisis clínicos
public class Laboratorio {
    private int idLaboratorio;
    private String nombreLaboratorio;
    private String ubicacion;
    private String telefonoInterno;
    private Doctor jefeLaboratorio; // Relación con Doctor
    private String equiposDisponibles;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private List<TipoExamen> tiposExamen; // Composición - exámenes que realiza
    private List<OrdenExamen> ordenesEnProceso; // Composición - órdenes actuales
    
    // Constructores
    public Laboratorio() {
        this.tiposExamen = new ArrayList<>();
        this.ordenesEnProceso = new ArrayList<>();
    }
    
    public Laboratorio(String nombreLaboratorio, String ubicacion, String telefonoInterno,
                      String equiposDisponibles, LocalTime horarioInicio, LocalTime horarioFin) {
        this();
        this.nombreLaboratorio = nombreLaboratorio;
        this.ubicacion = ubicacion;
        this.telefonoInterno = telefonoInterno;
        this.equiposDisponibles = equiposDisponibles;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
    }
    
    // Getters y setters
    public int getIdLaboratorio() { return idLaboratorio; }
    public void setIdLaboratorio(int idLaboratorio) { this.idLaboratorio = idLaboratorio; }
    
    public String getNombreLaboratorio() { return nombreLaboratorio; }
    public void setNombreLaboratorio(String nombreLaboratorio) { this.nombreLaboratorio = nombreLaboratorio; }
    
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    
    public String getTelefonoInterno() { return telefonoInterno; }
    public void setTelefonoInterno(String telefonoInterno) { this.telefonoInterno = telefonoInterno; }
    
    public Doctor getJefeLaboratorio() { return jefeLaboratorio; }
    public void setJefeLaboratorio(Doctor jefeLaboratorio) { this.jefeLaboratorio = jefeLaboratorio; }
    
    public String getEquiposDisponibles() { return equiposDisponibles; }
    public void setEquiposDisponibles(String equiposDisponibles) { this.equiposDisponibles = equiposDisponibles; }
    
    public LocalTime getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(LocalTime horarioInicio) { this.horarioInicio = horarioInicio; }
    
    public LocalTime getHorarioFin() { return horarioFin; }
    public void setHorarioFin(LocalTime horarioFin) { this.horarioFin = horarioFin; }
    
    public List<TipoExamen> getTiposExamen() { return new ArrayList<>(tiposExamen); }
    public void setTiposExamen(List<TipoExamen> tiposExamen) { this.tiposExamen = tiposExamen; }
    
    public List<OrdenExamen> getOrdenesEnProceso() { return new ArrayList<>(ordenesEnProceso); }
    public void setOrdenesEnProceso(List<OrdenExamen> ordenesEnProceso) { this.ordenesEnProceso = ordenesEnProceso; }
    
    // Métodos para gestionar tipos de examen
    public void agregarTipoExamen(TipoExamen tipoExamen) {
        if (tipoExamen != null && !tiposExamen.contains(tipoExamen)) {
            tiposExamen.add(tipoExamen);
            System.out.println("Tipo de examen agregado: " + tipoExamen.getNombreExamen());
        }
    }
    
    public void removerTipoExamen(TipoExamen tipoExamen) {
        if (tiposExamen.remove(tipoExamen)) {
            System.out.println("Tipo de examen removido: " + tipoExamen.getNombreExamen());
        }
    }
    
    public TipoExamen buscarTipoExamenPorNombre(String nombreExamen) {
        return tiposExamen.stream()
                .filter(te -> te.getNombreExamen().equalsIgnoreCase(nombreExamen))
                .findFirst()
                .orElse(null);
    }
    
    // Métodos para gestionar órdenes de examen
    public boolean procesarOrdenExamen(OrdenExamen orden) {
        if (orden == null) return false;
        
        // Verificar si el laboratorio puede realizar este tipo de examen
        TipoExamen tipoExamen = orden.getTipoExamen();
        if (tipoExamen != null && tiposExamen.contains(tipoExamen)) {
            ordenesEnProceso.add(orden);
            orden.setEstadoOrden("PROCESANDO");
            System.out.println("Orden de examen recibida para procesamiento: " + 
                             tipoExamen.getNombreExamen() + 
                             " - Paciente: " + orden.getPaciente().getNombreCompleto());
            return true;
        }
        
        System.out.println("Este laboratorio no puede procesar el examen: " + 
                          (tipoExamen != null ? tipoExamen.getNombreExamen() : "Desconocido"));
        return false;
    }
    
    public boolean completarExamen(OrdenExamen orden, String resultados) {
        if (ordenesEnProceso.contains(orden)) {
            orden.completarExamen(resultados);
            ordenesEnProceso.remove(orden);
            System.out.println("Examen completado: " + orden.getTipoExamen().getNombreExamen());
            return true;
        }
        return false;
    }
    
    public List<OrdenExamen> getOrdenesPendientes() {
        return ordenesEnProceso.stream()
                .filter(orden -> "PENDIENTE".equals(orden.getEstadoOrden()) || 
                               "PROCESANDO".equals(orden.getEstadoOrden()))
                .collect(java.util.stream.Collectors.toList());
    }
    
    public List<OrdenExamen> getOrdenesUrgentes() {
        return ordenesEnProceso.stream()
                .filter(orden -> orden.getTipoExamen().getTiempoResultadoHoras() <= 2)
                .collect(java.util.stream.Collectors.toList());
    }
    
    // Métodos de operación del laboratorio
    public boolean estaOperativo() {
        LocalTime ahora = LocalTime.now();
        return ahora.isAfter(horarioInicio) && ahora.isBefore(horarioFin);
    }
    
    public boolean esOperativo24Horas() {
        return horarioInicio.equals(LocalTime.MIDNIGHT) && 
               horarioFin.equals(LocalTime.of(23, 59));
    }
    
    public boolean puedeRealizarExamen(String nombreExamen) {
        return tiposExamen.stream()
                .anyMatch(te -> te.getNombreExamen().equalsIgnoreCase(nombreExamen));
    }
    
    public int getCapacidadDiariaEstimada() {
        // Estimar capacidad basada en horario de trabajo y tipos de examen
        int horasTrabajo = horarioFin.getHour() - horarioInicio.getHour();
        if (horasTrabajo <= 0) horasTrabajo = 24; // Laboratorio 24 horas
        
        int tiempoPromedioExamen = tiposExamen.stream()
                .mapToInt(TipoExamen::getTiempoResultadoHoras)
                .average()
                .orElse(4.0).intValue();
        
        return (horasTrabajo * 60) / Math.max(tiempoPromedioExamen * 10, 30); // Estimación conservadora
    }
    
    public void mostrarEstadisticasLaboratorio() {
        System.out.println("=== ESTADÍSTICAS DEL LABORATORIO ===");
        System.out.println("Laboratorio: " + nombreLaboratorio);
        System.out.println("Ubicación: " + ubicacion);
        System.out.println("Jefe: " + (jefeLaboratorio != null ? jefeLaboratorio.getNombreCompleto() : "No asignado"));
        System.out.println("Horario: " + horarioInicio + " - " + horarioFin);
        System.out.println("Estado: " + (estaOperativo() ? "OPERATIVO" : "FUERA DE HORARIO"));
        System.out.println("Tipos de examen disponibles: " + tiposExamen.size());
        System.out.println("Órdenes en proceso: " + ordenesEnProceso.size());
        System.out.println("Órdenes pendientes: " + getOrdenesPendientes().size());
        System.out.println("Órdenes urgentes: " + getOrdenesUrgentes().size());
        System.out.println("Capacidad diaria estimada: " + getCapacidadDiariaEstimada() + " exámenes");
        
        if (!tiposExamen.isEmpty()) {
            System.out.println("\n--- TIPOS DE EXAMEN DISPONIBLES ---");
            for (TipoExamen te : tiposExamen) {
                System.out.println("• " + te.getNombreExamen() + 
                                 " - $" + te.getPrecio() + 
                                 " - " + te.getTiempoResultadoHoras() + "h");
            }
        }
        
        if (!ordenesEnProceso.isEmpty()) {
            System.out.println("\n--- ÓRDENES EN PROCESO ---");
            for (OrdenExamen orden : ordenesEnProceso) {
                System.out.println("• " + orden.getTipoExamen().getNombreExamen() + 
                                 " - Paciente: " + orden.getPaciente().getNombreCompleto() + 
                                 " - Estado: " + orden.getEstadoOrden());
            }
        }
    }
    
    public void generarReporteDiario() {
        System.out.println("=== REPORTE DIARIO - " + nombreLaboratorio.toUpperCase() + " ===");
        System.out.println("Fecha: " + java.time.LocalDate.now());
        
        int totalOrdenes = ordenesEnProceso.size();
        int ordenesPendientes = getOrdenesPendientes().size();
        int ordenesUrgentes = getOrdenesUrgentes().size();
        
        System.out.println("Total órdenes del día: " + totalOrdenes);
        System.out.println("Órdenes pendientes: " + ordenesPendientes);
        System.out.println("Órdenes urgentes: " + ordenesUrgentes);
        System.out.println("Órdenes completadas: " + (totalOrdenes - ordenesPendientes));
        
        if (totalOrdenes > 0) {
            double eficiencia = ((double)(totalOrdenes - ordenesPendientes) / totalOrdenes) * 100;
            System.out.println("Eficiencia del día: " + String.format("%.1f", eficiencia) + "%");
        }
    }
    
    @Override
    public String toString() {
        return nombreLaboratorio + " - " + ubicacion + 
               " | Horario: " + horarioInicio + "-" + horarioFin + 
               " | Exámenes: " + tiposExamen.size() + 
               " | En proceso: " + ordenesEnProceso.size();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Laboratorio that = (Laboratorio) obj;
        return idLaboratorio == that.idLaboratorio;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idLaboratorio);
    }
}