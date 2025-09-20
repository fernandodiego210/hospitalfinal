package hospitalD;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * CLASE HOSPITAL COMPLETA - SISTEMA DE GESTIÓN HOSPITALARIA INTEGRAL
 * Versión: 2.0 Completa
 * Funcionalidades: Farmacia, Laboratorio, Emergencias, Cirugías, Monitoreo, Seguros
 */
public class Hospital {
    private int idHospital;
    private String nombreHospital;
    private String direccion;
    private String telefono;
    private String email;
    
    // ============= LISTAS DE ENTIDADES =============
    
    // Listas básicas
    private List<Departamento> departamentos;
    private List<Doctor> doctores;
    private List<Paciente> pacientes;
    private List<Enfermero> enfermeros;
    private List<Habitacion> habitaciones;
    private List<Cita> citas;
    private List<Especialidad> especialidades;
    
    // Listas del sistema de farmacia
    private List<Farmacia> farmacias;
    private List<Proveedor> proveedores;
    private List<MedicamentoAmpliado> medicamentos;
    
    // Listas del sistema de laboratorio
    private List<Laboratorio> laboratorios;
    private List<TipoExamen> tiposExamen;
    private List<OrdenExamen> ordenesExamen;
    
    // Listas del sistema de emergencias
    private List<Emergencia> emergencias;
    private List<Ambulancia> ambulancias;
    
    // Listas del sistema quirúrgico
    private List<Cirugia> cirugias;
    private List<PersonalCirugia> personalCirugico;
    
    // Listas del sistema de monitoreo y seguros
    private List<SignosVitales> signosVitales;
    private List<SeguroMedico> segurosMedicos;
    
    // ============= CONSTRUCTORES =============
    
    public Hospital() {
        inicializarListas();
    }
    
    public Hospital(String nombreHospital, String direccion, String telefono, String email) {
        this();
        this.nombreHospital = nombreHospital;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }
    
    private void inicializarListas() {
        // Inicializar listas básicas
        this.departamentos = new ArrayList<>();
        this.doctores = new ArrayList<>();
        this.pacientes = new ArrayList<>();
        this.enfermeros = new ArrayList<>();
        this.habitaciones = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.especialidades = new ArrayList<>();
        
        // Inicializar listas de farmacia
        this.farmacias = new ArrayList<>();
        this.proveedores = new ArrayList<>();
        this.medicamentos = new ArrayList<>();
        
        // Inicializar listas de laboratorio
        this.laboratorios = new ArrayList<>();
        this.tiposExamen = new ArrayList<>();
        this.ordenesExamen = new ArrayList<>();
        
        // Inicializar listas de emergencias
        this.emergencias = new ArrayList<>();
        this.ambulancias = new ArrayList<>();
        
        // Inicializar listas quirúrgicas
        this.cirugias = new ArrayList<>();
        this.personalCirugico = new ArrayList<>();
        
        // Inicializar listas de monitoreo y seguros
        this.signosVitales = new ArrayList<>();
        this.segurosMedicos = new ArrayList<>();
    }
    
    // ============= GETTERS Y SETTERS BÁSICOS =============
    
    public int getIdHospital() { return idHospital; }
    public void setIdHospital(int idHospital) { this.idHospital = idHospital; }
    
    public String getNombreHospital() { return nombreHospital; }
    public void setNombreHospital(String nombreHospital) { this.nombreHospital = nombreHospital; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    // ============= GETTERS PARA LISTAS (con encapsulación) =============
    
    // Listas básicas
    public List<Departamento> getDepartamentos() { return new ArrayList<>(departamentos); }
    public List<Doctor> getDoctores() { return new ArrayList<>(doctores); }
    public List<Paciente> getPacientes() { return new ArrayList<>(pacientes); }
    public List<Enfermero> getEnfermeros() { return new ArrayList<>(enfermeros); }
    public List<Habitacion> getHabitaciones() { return new ArrayList<>(habitaciones); }
    public List<Cita> getCitas() { return new ArrayList<>(citas); }
    public List<Especialidad> getEspecialidades() { return new ArrayList<>(especialidades); }
    
    // Listas de farmacia
    public List<Farmacia> getFarmacias() { return new ArrayList<>(farmacias); }
    public List<Proveedor> getProveedores() { return new ArrayList<>(proveedores); }
    public List<MedicamentoAmpliado> getMedicamentos() { return new ArrayList<>(medicamentos); }
    
    // Listas de laboratorio
    public List<Laboratorio> getLaboratorios() { return new ArrayList<>(laboratorios); }
    public List<TipoExamen> getTiposExamen() { return new ArrayList<>(tiposExamen); }
    public List<OrdenExamen> getOrdenesExamen() { return new ArrayList<>(ordenesExamen); }
    
    // Listas de emergencias
    public List<Emergencia> getEmergencias() { return new ArrayList<>(emergencias); }
    public List<Ambulancia> getAmbulancias() { return new ArrayList<>(ambulancias); }
    
    // Listas quirúrgicas
    public List<Cirugia> getCirugias() { return new ArrayList<>(cirugias); }
    public List<PersonalCirugia> getPersonalCirugico() { return new ArrayList<>(personalCirugico); }
    
    // Listas de monitoreo y seguros
    public List<SignosVitales> getSignosVitales() { return new ArrayList<>(signosVitales); }
    public List<SeguroMedico> getSegurosMedicos() { return new ArrayList<>(segurosMedicos); }
    
    // ============= MÉTODOS DE GESTIÓN BÁSICA =============
    
    // ESPECIALIDADES
    public void agregarEspecialidad(Especialidad especialidad) {
        if (especialidad != null && !especialidades.contains(especialidad)) {
            especialidades.add(especialidad);
        }
    }
    
    public Especialidad buscarEspecialidadPorNombre(String nombre) {
        return especialidades.stream()
                .filter(e -> e.getNombreEspecialidad().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }
    
    // DOCTORES
    public void agregarDoctor(Doctor doctor) {
        if (doctor != null && !doctores.contains(doctor)) {
            doctores.add(doctor);
        }
    }
    
    public void removerDoctor(Doctor doctor) { doctores.remove(doctor); }
    
    public Doctor buscarDoctorPorLicencia(String numeroLicencia) {
        return doctores.stream()
                .filter(d -> d.getNumeroLicencia().equals(numeroLicencia))
                .findFirst()
                .orElse(null);
    }
    
    public List<Doctor> getDoctoresPorEspecialidad(String especialidad) {
        return doctores.stream()
                .filter(d -> d.getEspecialidad() != null && 
                            d.getEspecialidad().getNombreEspecialidad().equalsIgnoreCase(especialidad))
                .collect(Collectors.toList());
    }
    
    public List<Doctor> getDoctoresDisponibles() {
        return doctores.stream()
                .filter(Doctor::estaDisponible)
                .collect(Collectors.toList());
    }
    
    // PACIENTES
    public void agregarPaciente(Paciente paciente) {
        if (paciente != null && !pacientes.contains(paciente)) {
            pacientes.add(paciente);
        }
    }
    
    public void removerPaciente(Paciente paciente) { pacientes.remove(paciente); }
    
    public Paciente buscarPacientePorHistoria(String numeroHistoria) {
        return pacientes.stream()
                .filter(p -> p.getNumeroHistoria().equals(numeroHistoria))
                .findFirst()
                .orElse(null);
    }
    
    public Paciente buscarPacientePorDni(String dni) {
        return pacientes.stream()
                .filter(p -> p.getDni().equals(dni))
                .findFirst()
                .orElse(null);
    }
    
    // ENFERMEROS
    public void agregarEnfermero(Enfermero enfermero) {
        if (enfermero != null && !enfermeros.contains(enfermero)) {
            enfermeros.add(enfermero);
        }
    }
    
    public List<Enfermero> getEnfermerosPorTurno(String turno) {
        return enfermeros.stream()
                .filter(e -> e.getTurno().equalsIgnoreCase(turno))
                .collect(Collectors.toList());
    }
    
    // HABITACIONES
    public void agregarHabitacion(Habitacion habitacion) {
        if (habitacion != null && !habitaciones.contains(habitacion)) {
            habitaciones.add(habitacion);
        }
    }
    
    public List<Habitacion> getHabitacionesDisponibles() {
        return habitaciones.stream()
                .filter(Habitacion::estaDisponible)
                .collect(Collectors.toList());
    }
    
    public List<Habitacion> getHabitacionesPorTipo(String tipo) {
        return habitaciones.stream()
                .filter(h -> h.getTipoHabitacion().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }
    
    public Habitacion buscarHabitacionPorNumero(String numero) {
        return habitaciones.stream()
                .filter(h -> h.getNumeroHabitacion().equals(numero))
                .findFirst()
                .orElse(null);
    }
    
    // CITAS
    public void agendarCita(Cita cita) {
        if (cita != null && !citas.contains(cita)) {
            citas.add(cita);
        }
    }
    
    public List<Cita> getCitasDeHoy() {
        return citas.stream()
                .filter(Cita::esHoy)
                .collect(Collectors.toList());
    }
    
    public List<Cita> getCitasProgramadas() {
        return citas.stream()
                .filter(Cita::estaProgramada)
                .collect(Collectors.toList());
    }
    
    // ============= MÉTODOS DEL SISTEMA DE FARMACIA =============
    
    public void agregarFarmacia(Farmacia farmacia) {
        if (farmacia != null && !farmacias.contains(farmacia)) {
            farmacias.add(farmacia);
        }
    }
    
    public void agregarProveedor(Proveedor proveedor) {
        if (proveedor != null && !proveedores.contains(proveedor)) {
            proveedores.add(proveedor);
        }
    }
    
    public void agregarMedicamento(MedicamentoAmpliado medicamento) {
        if (medicamento != null && !medicamentos.contains(medicamento)) {
            medicamentos.add(medicamento);
        }
    }
    
    public List<Farmacia> getFarmaciasAbiertas() {
        return farmacias.stream()
                .filter(Farmacia::estaAbierta)
                .collect(Collectors.toList());
    }
    
    public List<MedicamentoAmpliado> getMedicamentosStockBajo() {
        return medicamentos.stream()
                .filter(MedicamentoAmpliado::requiereReposicion)
                .collect(Collectors.toList());
    }
    
    public List<MedicamentoAmpliado> getMedicamentosVencidos() {
        return medicamentos.stream()
                .filter(MedicamentoAmpliado::estaVencido)
                .collect(Collectors.toList());
    }
    
    public List<Proveedor> getProveedoresActivos() {
        return proveedores.stream()
                .filter(Proveedor::estaActivo)
                .collect(Collectors.toList());
    }
    
    // ============= MÉTODOS DEL SISTEMA DE LABORATORIO =============
    
    public void agregarLaboratorio(Laboratorio laboratorio) {
        if (laboratorio != null && !laboratorios.contains(laboratorio)) {
            laboratorios.add(laboratorio);
        }
    }
    
    public void agregarTipoExamen(TipoExamen tipoExamen) {
        if (tipoExamen != null && !tiposExamen.contains(tipoExamen)) {
            tiposExamen.add(tipoExamen);
        }
    }
    
    public void agregarOrdenExamen(OrdenExamen orden) {
        if (orden != null && !ordenesExamen.contains(orden)) {
            ordenesExamen.add(orden);
        }
    }
    
    public List<Laboratorio> getLaboratoriosOperativos() {
        return laboratorios.stream()
                .filter(Laboratorio::estaOperativo)
                .collect(Collectors.toList());
    }
    
    public List<OrdenExamen> getOrdenesExamenPendientes() {
        return ordenesExamen.stream()
                .filter(OrdenExamen::estaPendiente)
                .collect(Collectors.toList());
    }
    
    public List<OrdenExamen> getOrdenesExamenUrgentes() {
        return ordenesExamen.stream()
                .filter(orden -> orden.getTipoExamen() != null && orden.getTipoExamen().esUrgente())
                .collect(Collectors.toList());
    }
    
    // ============= MÉTODOS DEL SISTEMA DE EMERGENCIAS =============
    
    public void agregarEmergencia(Emergencia emergencia) {
        if (emergencia != null && !emergencias.contains(emergencia)) {
            emergencias.add(emergencia);
        }
    }
    
    public void agregarAmbulancia(Ambulancia ambulancia) {
        if (ambulancia != null && !ambulancias.contains(ambulancia)) {
            ambulancias.add(ambulancia);
        }
    }
    
    public List<Emergencia> getEmergenciasActivas() {
        return emergencias.stream()
                .filter(Emergencia::estaEnAtencion)
                .collect(Collectors.toList());
    }
    
    public List<Emergencia> getEmergenciasCriticas() {
        return emergencias.stream()
                .filter(Emergencia::esCritico)
                .collect(Collectors.toList());
    }
    
    public List<Ambulancia> getAmbulanciasDisponibles() {
        return ambulancias.stream()
                .filter(Ambulancia::estaDisponible)
                .collect(Collectors.toList());
    }
    
    public List<Ambulancia> getAmbulanciasEnServicio() {
        return ambulancias.stream()
                .filter(Ambulancia::estaEnServicio)
                .collect(Collectors.toList());
    }
    
    // ============= MÉTODOS DEL SISTEMA QUIRÚRGICO =============
    
    public void agregarCirugia(Cirugia cirugia) {
        if (cirugia != null && !cirugias.contains(cirugia)) {
            cirugias.add(cirugia);
        }
    }
    
    public List<Cirugia> getCirugiasDeHoy() {
        return cirugias.stream()
                .filter(c -> c.getFechaProgramada() != null && 
                            c.getFechaProgramada().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
                .collect(Collectors.toList());
    }
    
    public List<Cirugia> getCirugiasEnCurso() {
        return cirugias.stream()
                .filter(Cirugia::estaEnCurso)
                .collect(Collectors.toList());
    }
    
    public List<Cirugia> getCirugiasAltoRiesgo() {
        return cirugias.stream()
                .filter(c -> c.esRiesgoAlto() || c.esRiesgoMuyAlto())
                .collect(Collectors.toList());
    }
    
    // ============= MÉTODOS DEL SISTEMA DE MONITOREO =============
    
    public void agregarSignosVitales(SignosVitales signos) {
        if (signos != null && !signosVitales.contains(signos)) {
            signosVitales.add(signos);
        }
    }
    
    public void agregarSeguroMedico(SeguroMedico seguro) {
        if (seguro != null && !segurosMedicos.contains(seguro)) {
            segurosMedicos.add(seguro);
        }
    }
    
    public List<SignosVitales> getSignosVitalesAlarma() {
        return signosVitales.stream()
                .filter(SignosVitales::tieneSignosAlarmantes)
                .collect(Collectors.toList());
    }
    
    public List<SignosVitales> getUltimosSignosVitalesPaciente(Paciente paciente) {
        return signosVitales.stream()
                .filter(sv -> sv.getPaciente().equals(paciente))
                .sorted((a, b) -> b.getFechaRegistro().compareTo(a.getFechaRegistro()))
                .limit(5)
                .collect(Collectors.toList());
    }
    
    // ============= MÉTODOS DE ESTADÍSTICAS Y REPORTES =============
    
    public void mostrarEstadisticasGenerales() {
        System.out.println("=== ESTADÍSTICAS DEL " + nombreHospital.toUpperCase() + " ===");
        System.out.println("📍 " + direccion + " | ☎️ " + telefono + " | ✉️ " + email);
        System.out.println();
        
        System.out.println("👥 PERSONAL MÉDICO:");
        System.out.println("   Doctores: " + doctores.size() + " (Disponibles: " + getDoctoresDisponibles().size() + ")");
        System.out.println("   Enfermeros: " + enfermeros.size());
        System.out.println("   Total: " + (doctores.size() + enfermeros.size()) + " profesionales");
        
        System.out.println("\n🏥 INFRAESTRUCTURA:");
        System.out.println("   Departamentos: " + departamentos.size());
        System.out.println("   Habitaciones: " + habitaciones.size() + " (Disponibles: " + getHabitacionesDisponibles().size() + ")");
        System.out.println("   Farmacias: " + farmacias.size() + " (Abiertas: " + getFarmaciasAbiertas().size() + ")");
        System.out.println("   Laboratorios: " + laboratorios.size() + " (Operativos: " + getLaboratoriosOperativos().size() + ")");
        System.out.println("   Ambulancias: " + ambulancias.size() + " (Disponibles: " + getAmbulanciasDisponibles().size() + ")");
        
        System.out.println("\n⚕️ ATENCIÓN MÉDICA:");
        System.out.println("   Pacientes: " + pacientes.size());
        System.out.println("   Citas: " + citas.size() + " (Hoy: " + getCitasDeHoy().size() + ")");
        System.out.println("   Emergencias: " + getEmergenciasActivas().size() + " (Críticas: " + getEmergenciasCriticas().size() + ")");
        System.out.println("   Cirugías: " + cirugias.size() + " (Hoy: " + getCirugiasDeHoy().size() + ")");
        
        System.out.println("\n🔬 SERVICIOS DIAGNÓSTICOS:");
        System.out.println("   Tipos de examen: " + tiposExamen.size());
        System.out.println("   Órdenes pendientes: " + getOrdenesExamenPendientes().size());
        System.out.println("   Medicamentos: " + medicamentos.size() + " (Stock bajo: " + getMedicamentosStockBajo().size() + ")");
        
        System.out.println("\n💼 GESTIÓN:");
        System.out.println("   Seguros médicos: " + segurosMedicos.size());
        System.out.println("   Proveedores: " + getProveedoresActivos().size());
    }
    
    public void mostrarDashboardEjecutivo() {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    DASHBOARD EJECUTIVO                    ║");
        System.out.println("║              " + String.format("%-30s", nombreHospital.toUpperCase()) + "              ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        
        // KPIs principales
        int totalHabitaciones = habitaciones.size();
        int habitacionesOcupadas = totalHabitaciones - getHabitacionesDisponibles().size();
        double ocupacion = totalHabitaciones > 0 ? (habitacionesOcupadas * 100.0 / totalHabitaciones) : 0;
        
        System.out.println("║ 🏥 OCUPACIÓN: " + String.format("%.1f", ocupacion) + "%" +
                          " (" + habitacionesOcupadas + "/" + totalHabitaciones + " camas)                        ║");
        System.out.println("║ 🚨 EMERGENCIAS: " + String.format("%2d", getEmergenciasActivas().size()) + 
                          " activas | " + String.format("%2d", getEmergenciasCriticas().size()) + " críticas                    ║");
        System.out.println("║ 🚑 AMBULANCIAS: " + String.format("%2d", getAmbulanciasDisponibles().size()) + 
                          "/" + ambulancias.size() + " disponibles                           ║");
        System.out.println("║ ⚕️ ACTIVIDAD HOY: " + String.format("%2d", getCitasDeHoy().size()) + 
                          " citas | " + String.format("%2d", getCirugiasDeHoy().size()) + " cirugías                       ║");
        System.out.println("║ 🔬 LABORATORIO: " + String.format("%2d", getOrdenesExamenPendientes().size()) + 
                          " pendientes | " + String.format("%2d", getOrdenesExamenUrgentes().size()) + " urgentes              ║");
        System.out.println("║ 💊 FARMACIA: " + String.format("%3d", getMedicamentosStockBajo().size()) + 
                          " stock bajo | " + String.format("%2d", getMedicamentosVencidos().size()) + " vencidos              ║");
        
        // Alertas críticas
        int alertasCriticas = getEmergenciasCriticas().size() + getMedicamentosVencidos().size() + 
                             getSignosVitalesAlarma().size();
        
        System.out.println("║ ⚠️ ALERTAS CRÍTICAS: " + String.format("%2d", alertasCriticas) + 
                          "                                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        
        // Recomendaciones automáticas
        if (ocupacion > 90 || getEmergenciasCriticas().size() > 3 || 
            getMedicamentosVencidos().size() > 0 || getAmbulanciasDisponibles().size() < 2) {
            System.out.println("\n🎯 RECOMENDACIONES URGENTES:");
            if (ocupacion > 90) System.out.println("   🔴 Ocupación crítica - Activar protocolos de expansión");
            if (getEmergenciasCriticas().size() > 3) System.out.println("   🔴 Múltiples emergencias críticas");
            if (getMedicamentosVencidos().size() > 0) System.out.println("   🟡 Revisar medicamentos vencidos");
            if (getAmbulanciasDisponibles().size() < 2) System.out.println("   🟡 Pocas ambulancias disponibles");
        } else {
            System.out.println("\n✅ Todos los indicadores en parámetros normales");
        }
    }
    
    public void mostrarAlertasCriticas() {
        System.out.println("🚨 ===== SISTEMA DE ALERTAS CRÍTICAS =====");
        
        boolean hayAlertas = false;
        
        // Emergencias críticas
        if (!getEmergenciasCriticas().isEmpty()) {
            hayAlertas = true;
            System.out.println("\n🔴 EMERGENCIAS CRÍTICAS (" + getEmergenciasCriticas().size() + "):");
            for (Emergencia em : getEmergenciasCriticas()) {
                System.out.println("   ⚡ " + em.getPaciente().getNombreCompleto() + 
                                 " - " + em.getSintomasPrincipales());
            }
        }
        
        // Signos vitales críticos
        if (!getSignosVitalesAlarma().isEmpty()) {
            hayAlertas = true;
            System.out.println("\n📊 SIGNOS VITALES CRÍTICOS (" + getSignosVitalesAlarma().size() + "):");
            for (SignosVitales sv : getSignosVitalesAlarma()) {
                System.out.println("   ⚠️ " + sv.getPaciente().getNombreCompleto() + 
                                 " - " + sv.getNivelGravedad());
            }
        }
        
        // Medicamentos críticos
        if (!getMedicamentosVencidos().isEmpty()) {
            hayAlertas = true;
            System.out.println("\n💊 MEDICAMENTOS VENCIDOS (" + getMedicamentosVencidos().size() + "):");
            getMedicamentosVencidos().stream().limit(5).forEach(med -> 
                System.out.println("   ❌ " + med.getNombreMedicamento()));
        }
        
        // Stock crítico
        if (getMedicamentosStockBajo().size() > medicamentos.size() * 0.2) {
            hayAlertas = true;
            System.out.println("\n📦 CRISIS DE INVENTARIO:");
            System.out.println("   🔴 " + getMedicamentosStockBajo().size() + 
                             " medicamentos requieren reposición urgente");
        }
        
        if (!hayAlertas) {
            System.out.println("\n✅ No hay alertas críticas - Sistema funcionando normalmente");
        }
    }
    
    // Búsqueda inteligente unificada
    public void busquedaInteligente(String termino) {
        System.out.println("🔍 BÚSQUEDA INTELIGENTE: \"" + termino + "\"");
        System.out.println("=" + "=".repeat(40));
        
        boolean encontrado = false;
        String terminoLower = termino.toLowerCase();
        
        // Buscar pacientes
        var pacientesEncontrados = pacientes.stream()
                .filter(p -> p.getNombreCompleto().toLowerCase().contains(terminoLower) ||
                           p.getDni().contains(termino) || 
                           (p.getNumeroHistoria() != null && p.getNumeroHistoria().contains(termino)))
                .limit(5)
                .collect(Collectors.toList());
        
        if (!pacientesEncontrados.isEmpty()) {
            encontrado = true;
            System.out.println("\n👤 PACIENTES ENCONTRADOS:");
            pacientesEncontrados.forEach(p -> System.out.println("   • " + p.getNombreCompleto() + 
                                                               " - " + p.getDni()));
        }
        
        // Buscar doctores
        var doctoresEncontrados = doctores.stream()
                .filter(d -> d.getNombreCompleto().toLowerCase().contains(terminoLower) ||
                           (d.getEspecialidad() != null && 
                            d.getEspecialidad().getNombreEspecialidad().toLowerCase().contains(terminoLower)))
                .limit(5)
                .collect(Collectors.toList());
        
        if (!doctoresEncontrados.isEmpty()) {
            encontrado = true;
            System.out.println("\n👨‍⚕️ DOCTORES ENCONTRADOS:");
            doctoresEncontrados.forEach(d -> System.out.println("   • Dr. " + d.getNombreCompleto() + 
                                                               " - " + (d.getEspecialidad() != null ? 
                                                               d.getEspecialidad().getNombreEspecialidad() : "General")));
        }
        
        // Buscar medicamentos
        var medicamentosEncontrados = medicamentos.stream()
                .filter(m -> m.getNombreMedicamento().toLowerCase().contains(terminoLower) ||
                           (m.getPrincipioActivo() != null && 
                            m.getPrincipioActivo().toLowerCase().contains(terminoLower)))
                .limit(5)
                .collect(Collectors.toList());
        
        if (!medicamentosEncontrados.isEmpty()) {
            encontrado = true;
            System.out.println("\n💊 MEDICAMENTOS ENCONTRADOS:");
            medicamentosEncontrados.forEach(m -> System.out.println("   • " + m.getNombreMedicamento() + 
                                                                   " - Stock: " + m.getStockActual()));
        }
        
        if (!encontrado) {
            System.out.println("\n❌ No se encontraron resultados");
            System.out.println("💡 Intente con: nombres, DNI, códigos o términos más generales");
        }
    }
    
    // Reporte ejecutivo integral
    public void generarReporteEjecutivo() {
        System.out.println("📊 ===== REPORTE EJECUTIVO INTEGRAL =====");
        System.out.println("Hospital: " + nombreHospital);
        System.out.println("Fecha: " + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        System.out.println("=" + "=".repeat(50));
        
        System.out.println("\n1️⃣ RESUMEN EJECUTIVO");
        mostrarEstadisticasGenerales();
        
        System.out.println("\n2️⃣ DASHBOARD DE CONTROL");
        mostrarDashboardEjecutivo();
        
        System.out.println("\n3️⃣ ALERTAS Y RIESGOS");
        mostrarAlertasCriticas();
        
        System.out.println("\n4️⃣ RECOMENDACIONES ESTRATÉGICAS");
        generarRecomendacionesEstrategicas();
    }
    
    private void generarRecomendacionesEstrategicas() {
        System.out.println("🎯 ANÁLISIS ESTRATÉGICO:");
        
        // Análisis de capacidad
        double ocupacion = habitaciones.size() > 0 ? 
                          ((habitaciones.size() - getHabitacionesDisponibles().size()) * 100.0 / habitaciones.size()) : 0;
        
        if (ocupacion > 85) {
            System.out.println("   📈 EXPANSIÓN: Considerar aumentar capacidad hospitalaria");
        }
        
        // Análisis de eficiencia
        double ratioPersonalPaciente = pacientes.size() > 0 ? 
                                     (double)(doctores.size() + enfermeros.size()) / pacientes.size() : 1;
        
        if (ratioPersonalPaciente < 0.1) {
            System.out.println("   👥 PERSONAL: Evaluar contratación de más profesionales");
        }
        
        // Análisis de tecnología
        if (getLaboratoriosOperativos().size() < laboratorios.size()) {
            System.out.println("   🔧 TECNOLOGÍA: Optimizar operatividad de laboratorios");
        }
        
        // Análisis financiero
        if (getMedicamentosStockBajo().size() > medicamentos.size() * 0.15) {
            System.out.println("   💰 INVENTARIO: Optimizar gestión de stock y proveedores");
        }
        
        System.out.println("   ✅ FORTALEZAS: Sistema integral con monitoreo en tiempo real");
    }
    
    @Override
    public String toString() {
        return String.format(
            "🏥 %s\n" +
            "📍 %s | ☎️ %s\n" +
            "📊 Personal: %d | Pacientes: %d | Habitaciones: %d/%d disponibles\n" +
            "⚕️ Servicios: %d farmacias, %d laboratorios, %d ambulancias\n" +
            "📈 Estado: %d emergencias activas, %d cirugías programadas",
            nombreHospital, direccion, telefono,
            (doctores.size() + enfermeros.size()), pacientes.size(), 
            getHabitacionesDisponibles().size(), habitaciones.size(),
            farmacias.size(), laboratorios.size(), ambulancias.size(),
            getEmergenciasActivas().size(), getCirugiasDeHoy().size()
        );
    }
}