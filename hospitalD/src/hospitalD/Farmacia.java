package hospitalD;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Clase Farmacia para gestionar medicamentos
public class Farmacia {
    private int idFarmacia;
    private String nombreFarmacia;
    private String ubicacion;
    private String telefonoInterno;
    private Doctor farmaceuticoJefe; // Relación con Doctor
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private List<MedicamentoAmpliado> inventario; // Composición - CORREGIDO: usar MedicamentoAmpliado
    private List<Enfermero> personalFarmacia; // Agregación
    
    // Constructores
    public Farmacia() {
        this.inventario = new ArrayList<>();
        this.personalFarmacia = new ArrayList<>();
    }
    
    public Farmacia(String nombreFarmacia, String ubicacion, String telefonoInterno,
                    LocalTime horarioInicio, LocalTime horarioFin) {
        this();
        this.nombreFarmacia = nombreFarmacia;
        this.ubicacion = ubicacion;
        this.telefonoInterno = telefonoInterno;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
    }
    
    // Getters y setters
    public int getIdFarmacia() { return idFarmacia; }
    public void setIdFarmacia(int idFarmacia) { this.idFarmacia = idFarmacia; }
    
    public String getNombreFarmacia() { return nombreFarmacia; }
    public void setNombreFarmacia(String nombreFarmacia) { this.nombreFarmacia = nombreFarmacia; }
    
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    
    public String getTelefonoInterno() { return telefonoInterno; }
    public void setTelefonoInterno(String telefonoInterno) { this.telefonoInterno = telefonoInterno; }
    
    public Doctor getFarmaceuticoJefe() { return farmaceuticoJefe; }
    public void setFarmaceuticoJefe(Doctor farmaceuticoJefe) { this.farmaceuticoJefe = farmaceuticoJefe; }
    
    public LocalTime getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(LocalTime horarioInicio) { this.horarioInicio = horarioInicio; }
    
    public LocalTime getHorarioFin() { return horarioFin; }
    public void setHorarioFin(LocalTime horarioFin) { this.horarioFin = horarioFin; }
    
    public List<MedicamentoAmpliado> getInventario() { return new ArrayList<>(inventario); }
    public void setInventario(List<MedicamentoAmpliado> inventario) { this.inventario = inventario; }
    
    public List<Enfermero> getPersonalFarmacia() { return new ArrayList<>(personalFarmacia); }
    public void setPersonalFarmacia(List<Enfermero> personalFarmacia) { this.personalFarmacia = personalFarmacia; }
    
    // Métodos para gestionar inventario
    public void agregarMedicamento(MedicamentoAmpliado medicamento) {
        if (medicamento != null) {
            // Buscar si ya existe el medicamento
            MedicamentoAmpliado existente = buscarMedicamentoPorNombre(medicamento.getNombreMedicamento());
            if (existente != null) {
                // Si existe, actualizar stock
                existente.setStockActual(existente.getStockActual() + medicamento.getStockActual());
                System.out.println("Stock actualizado para " + medicamento.getNombreMedicamento());
            } else {
                // Si no existe, agregar nuevo
                inventario.add(medicamento);
                System.out.println("Medicamento agregado: " + medicamento.getNombreMedicamento());
            }
        }
    }
    
    public boolean dispensarMedicamento(String nombreMedicamento, int cantidad) {
        MedicamentoAmpliado medicamento = buscarMedicamentoPorNombre(nombreMedicamento);
        
        if (medicamento == null) {
            System.out.println("Medicamento no encontrado: " + nombreMedicamento);
            return false;
        }
        
        if (medicamento.getStockActual() < cantidad) {
            System.out.println("Stock insuficiente para " + nombreMedicamento + 
                             ". Disponible: " + medicamento.getStockActual() + 
                             ", Solicitado: " + cantidad);
            return false;
        }
        
        medicamento.setStockActual(medicamento.getStockActual() - cantidad);
        System.out.println("Dispensado: " + cantidad + " unidades de " + nombreMedicamento);
        
        // Verificar si está por debajo del stock mínimo
        if (medicamento.getStockActual() <= medicamento.getStockMinimo()) {
            System.out.println("¡ALERTA! Stock bajo para " + nombreMedicamento + 
                             ". Stock actual: " + medicamento.getStockActual() + 
                             ", Mínimo: " + medicamento.getStockMinimo());
        }
        
        return true;
    }
    
    public MedicamentoAmpliado buscarMedicamentoPorNombre(String nombre) {
        return inventario.stream()
                .filter(m -> m.getNombreMedicamento().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }
    
    public List<MedicamentoAmpliado> getMedicamentosStockBajo() {
        return inventario.stream()
                .filter(m -> m.getStockActual() <= m.getStockMinimo())
                .collect(Collectors.toList());
    }
    
    public List<MedicamentoAmpliado> getMedicamentosPorVencer(int diasAnticipacion) {
        LocalDate fechaLimite = LocalDate.now().plusDays(diasAnticipacion);
        return inventario.stream()
                .filter(m -> m.getFechaVencimiento() != null && 
                           m.getFechaVencimiento().isBefore(fechaLimite))
                .collect(Collectors.toList());
    }
    
    public List<MedicamentoAmpliado> getMedicamentosVencidos() {
        return inventario.stream()
                .filter(m -> m.estaVencido())
                .collect(Collectors.toList());
    }
    
    public List<MedicamentoAmpliado> getMedicamentosQueRequierenReposicion() {
        return inventario.stream()
                .filter(m -> m.requiereReposicion())
                .collect(Collectors.toList());
    }
    
    // Métodos para gestionar personal
    public void agregarPersonal(Enfermero enfermero) {
        if (enfermero != null && !personalFarmacia.contains(enfermero)) {
            personalFarmacia.add(enfermero);
            System.out.println("Personal agregado a farmacia: " + enfermero.getNombreCompleto());
        }
    }
    
    public void removerPersonal(Enfermero enfermero) {
        if (personalFarmacia.remove(enfermero)) {
            System.out.println("Personal removido de farmacia: " + enfermero.getNombreCompleto());
        }
    }
    
    // Métodos de operación
    public boolean estaAbierta() {
        LocalTime ahora = LocalTime.now();
        return ahora.isAfter(horarioInicio) && ahora.isBefore(horarioFin);
    }
    
    public boolean estaAbierta24Horas() {
        return horarioInicio.equals(LocalTime.MIDNIGHT) && horarioFin.equals(LocalTime.of(23, 59));
    }
    
    public void mostrarReporteInventario() {
        System.out.println("=== REPORTE DE INVENTARIO - " + nombreFarmacia.toUpperCase() + " ===");
        System.out.println("Ubicación: " + ubicacion);
        System.out.println("Total de medicamentos: " + inventario.size());
        
        int stockBajo = getMedicamentosStockBajo().size();
        int porVencer = getMedicamentosPorVencer(30).size();
        int vencidos = getMedicamentosVencidos().size();
        
        System.out.println("Medicamentos con stock bajo: " + stockBajo);
        System.out.println("Medicamentos por vencer (30 días): " + porVencer);
        System.out.println("Medicamentos vencidos: " + vencidos);
        
        if (stockBajo > 0) {
            System.out.println("\n--- MEDICAMENTOS CON STOCK BAJO ---");
            for (MedicamentoAmpliado med : getMedicamentosStockBajo()) {
                System.out.println("- " + med.getNombreMedicamento() + 
                                 " | Stock: " + med.getStockActual() + 
                                 " | Mínimo: " + med.getStockMinimo());
            }
        }
        
        if (porVencer > 0) {
            System.out.println("\n--- MEDICAMENTOS POR VENCER ---");
            for (MedicamentoAmpliado med : getMedicamentosPorVencer(30)) {
                System.out.println("- " + med.getNombreMedicamento() + 
                                 " | Vence: " + med.getFechaVencimiento());
            }
        }
        
        if (vencidos > 0) {
            System.out.println("\n--- MEDICAMENTOS VENCIDOS ---");
            for (MedicamentoAmpliado med : getMedicamentosVencidos()) {
                System.out.println("- " + med.getNombreMedicamento() + 
                                 " | Vencido: " + med.getFechaVencimiento());
            }
        }
    }
    
    public int getTotalMedicamentosEnStock() {
        return inventario.stream()
                .mapToInt(MedicamentoAmpliado::getStockActual)
                .sum();
    }
    
    public void mostrarEstadoFarmacia() {
        System.out.println("=== ESTADO DE FARMACIA ===");
        System.out.println("Nombre: " + nombreFarmacia);
        System.out.println("Ubicación: " + ubicacion);
        System.out.println("Teléfono interno: " + telefonoInterno);
        System.out.println("Farmacéutico jefe: " + 
                          (farmaceuticoJefe != null ? farmaceuticoJefe.getNombreCompleto() : "No asignado"));
        System.out.println("Horario: " + horarioInicio + " - " + horarioFin);
        System.out.println("Estado: " + (estaAbierta() ? "ABIERTA" : "CERRADA"));
        System.out.println("Personal: " + personalFarmacia.size() + " empleados");
        System.out.println("Inventario: " + inventario.size() + " medicamentos diferentes");
        System.out.println("Stock total: " + getTotalMedicamentosEnStock() + " unidades");
    }
    
    public boolean tieneStockDisponible(String nombreMedicamento, int cantidadRequerida) {
        MedicamentoAmpliado medicamento = buscarMedicamentoPorNombre(nombreMedicamento);
        return medicamento != null && medicamento.tieneStockDisponible(cantidadRequerida);
    }
    
    public void verificarInventario() {
        System.out.println("\n=== VERIFICACIÓN DE INVENTARIO ===");
        
        List<MedicamentoAmpliado> stockBajo = getMedicamentosStockBajo();
        List<MedicamentoAmpliado> vencidos = getMedicamentosVencidos();
        List<MedicamentoAmpliado> porVencer = getMedicamentosPorVencer(30);
        
        if (stockBajo.isEmpty() && vencidos.isEmpty() && porVencer.isEmpty()) {
            System.out.println("✅ El inventario está en óptimas condiciones");
        } else {
            if (!stockBajo.isEmpty()) {
                System.out.println("⚠️ " + stockBajo.size() + " medicamentos requieren reposición");
            }
            if (!vencidos.isEmpty()) {
                System.out.println("❌ " + vencidos.size() + " medicamentos están vencidos");
            }
            if (!porVencer.isEmpty()) {
                System.out.println("⏰ " + porVencer.size() + " medicamentos vencen en los próximos 30 días");
            }
        }
    }
    
    @Override
    public String toString() {
        return nombreFarmacia + " - " + ubicacion + 
               " | Horario: " + horarioInicio + "-" + horarioFin + 
               " | Medicamentos: " + inventario.size() + 
               " | Personal: " + personalFarmacia.size() +
               " | Estado: " + (estaAbierta() ? "ABIERTA" : "CERRADA");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Farmacia farmacia = (Farmacia) obj;
        return idFarmacia == farmacia.idFarmacia;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idFarmacia);
    }
}