package hospitalD;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

// Clase principal del Sistema Hospitalario
public class SistemaHospitalario {
    private static Hospital hospital;
    private static HospitalDAO dao;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE GESTIÓN HOSPITALARIA ===");
        System.out.println("Inicializando sistema...\n");
        
        // Inicializar componentes
        inicializarSistema();
        
        // Probar conexión a la base de datos
        if (!dao.probarConexion()) {
            System.err.println("ERROR: No se pudo conectar a la base de datos.");
            System.err.println("Verifique la configuración en ConexionBD.java");
            return;
        }
        
        System.out.println("✓ Conexión a base de datos establecida correctamente");
        
        // Cargar datos desde la base de datos
        cargarDatosDesdeBD();
        
        // Mostrar menú principal
        mostrarMenuPrincipal();
        
        // Cerrar recursos
        scanner.close();
        ConexionBD.closeConnection();
        System.out.println("\n¡Gracias por usar el Sistema Hospitalario!");
    }
    
    private static void inicializarSistema() {
        hospital = new Hospital("Hospital General San José", 
                               "Av. Salud 1234, Lima", 
                               "+51-1-234-5678", 
                               "contacto@hospitalsanjose.pe");
        
        dao = new HospitalDAO();
        scanner = new Scanner(System.in);
    }
    
    private static void cargarDatosDesdeBD() {
        System.out.println("Cargando datos desde la base de datos...");
        
        // Cargar especialidades
        List<Especialidad> especialidades = dao.obtenerEspecialidades();
        for (Especialidad esp : especialidades) {
            hospital.agregarEspecialidad(esp);
        }
        
        // Cargar doctores
        List<Doctor> doctores = dao.obtenerDoctores();
        for (Doctor doc : doctores) {
            hospital.agregarDoctor(doc);
        }
        
        // Cargar pacientes
        List<Paciente> pacientes = dao.obtenerPacientes();
        for (Paciente pac : pacientes) {
            hospital.agregarPaciente(pac);
        }
        
        // Cargar habitaciones
        List<Habitacion> habitaciones = dao.obtenerHabitaciones();
        for (Habitacion hab : habitaciones) {
            hospital.agregarHabitacion(hab);
        }
        
        // Cargar citas
        List<Cita> citas = dao.obtenerCitas();
        for (Cita cita : citas) {
            hospital.agendarCita(cita);
        }
        
        System.out.println("✓ Datos cargados exitosamente");
        System.out.println("- Especialidades: " + especialidades.size());
        System.out.println("- Doctores: " + doctores.size());
        System.out.println("- Pacientes: " + pacientes.size());
        System.out.println("- Habitaciones: " + habitaciones.size());
        System.out.println("- Citas: " + citas.size());
        System.out.println();
    }
    
    private static void mostrarMenuPrincipal() {
        int opcion;
        
        do {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║     MENÚ PRINCIPAL - HOSPITAL      ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║ 1. Gestión de Doctores             ║");
            System.out.println("║ 2. Gestión de Pacientes            ║");
            System.out.println("║ 3. Gestión de Citas                ║");
            System.out.println("║ 4. Gestión de Habitaciones          ║");
            System.out.println("║ 5. Reportes y Estadísticas         ║");
            System.out.println("║ 6. Información del Hospital        ║");
            System.out.println("║ 0. Salir                           ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print("Seleccione una opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir salto de línea
            
            switch (opcion) {
                case 1: menuDoctores(); break;
                case 2: menuPacientes(); break;
                case 3: menuCitas(); break;
                case 4: menuHabitaciones(); break;
                case 5: menuReportes(); break;
                case 6: mostrarInfoHospital(); break;
                case 0: System.out.println("Cerrando sistema..."); break;
                default: System.out.println("Opción inválida. Intente nuevamente."); break;
            }
        } while (opcion != 0);
    }
    
    private static void menuDoctores() {
        int opcion;
        
        do {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║        GESTIÓN DE DOCTORES         ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║ 1. Registrar nuevo doctor          ║");
            System.out.println("║ 2. Listar todos los doctores       ║");
            System.out.println("║ 3. Buscar doctor por licencia      ║");
            System.out.println("║ 4. Doctores por especialidad       ║");
            System.out.println("║ 0. Volver al menú principal        ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print("Seleccione una opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1: registrarDoctor(); break;
                case 2: listarDoctores(); break;
                case 3: buscarDoctorPorLicencia(); break;
                case 4: listarDoctoresPorEspecialidad(); break;
                case 0: break;
                default: System.out.println("Opción inválida."); break;
            }
        } while (opcion != 0);
    }
    
    private static void registrarDoctor() {
        System.out.println("\n=== REGISTRO DE NUEVO DOCTOR ===");
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        
        System.out.print("DNI: ");
        String dni = scanner.nextLine();
        
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();
        
        System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
        String fechaNacStr = scanner.nextLine();
        LocalDate fechaNacimiento = LocalDate.parse(fechaNacStr);
        
        System.out.print("Número de licencia médica: ");
        String licencia = scanner.nextLine();
        
        // Mostrar especialidades disponibles
        System.out.println("\nEspecialidades disponibles:");
        List<Especialidad> especialidades = hospital.getEspecialidades();
        for (int i = 0; i < especialidades.size(); i++) {
            System.out.println((i + 1) + ". " + especialidades.get(i).getNombreEspecialidad());
        }
        
        System.out.print("Seleccione especialidad (número): ");
        int indiceEsp = scanner.nextInt() - 1;
        scanner.nextLine();
        
        Especialidad especialidad = null;
        if (indiceEsp >= 0 && indiceEsp < especialidades.size()) {
            especialidad = especialidades.get(indiceEsp);
        }
        
        System.out.print("Fecha de ingreso (YYYY-MM-DD): ");
        String fechaIngStr = scanner.nextLine();
        LocalDate fechaIngreso = LocalDate.parse(fechaIngStr);
        
        System.out.print("Salario: ");
        BigDecimal salario = scanner.nextBigDecimal();
        scanner.nextLine();
        
        System.out.print("Horario de inicio (HH:MM): ");
        String horarioIniStr = scanner.nextLine();
        LocalTime horarioInicio = LocalTime.parse(horarioIniStr);
        
        System.out.print("Horario de fin (HH:MM): ");
        String horarioFinStr = scanner.nextLine();
        LocalTime horarioFin = LocalTime.parse(horarioFinStr);
        
        Doctor doctor = new Doctor(nombre, apellido, dni, telefono, email, direccion,
                                  fechaNacimiento, licencia, especialidad, fechaIngreso,
                                  salario, horarioInicio, horarioFin);
        
        if (dao.insertarDoctor(doctor)) {
            hospital.agregarDoctor(doctor);
            System.out.println("✓ Doctor registrado exitosamente con ID: " + doctor.getIdDoctor());
        } else {
            System.out.println("✗ Error al registrar el doctor.");
        }
    }
    
    private static void listarDoctores() {
        System.out.println("\n=== LISTA DE DOCTORES ===");
        List<Doctor> doctores = hospital.getDoctores();
        
        if (doctores.isEmpty()) {
            System.out.println("No hay doctores registrados.");
            return;
        }
        
        System.out.printf("%-5s %-20s %-15s %-20s %-10s\n", 
                         "ID", "NOMBRE COMPLETO", "LICENCIA", "ESPECIALIDAD", "ESTADO");
        System.out.println("─".repeat(80));
        
        for (Doctor doctor : doctores) {
            System.out.printf("%-5d %-20s %-15s %-20s %-10s\n",
                            doctor.getIdDoctor(),
                            doctor.getNombreCompleto(),
                            doctor.getNumeroLicencia(),
                            doctor.getEspecialidad() != null ? doctor.getEspecialidad().getNombreEspecialidad() : "N/A",
                            doctor.getEstado());
        }
    }
    
    private static void buscarDoctorPorLicencia() {
        System.out.print("Ingrese número de licencia: ");
        String licencia = scanner.nextLine();
        
        Doctor doctor = dao.buscarDoctorPorLicencia(licencia);
        
        if (doctor != null) {
            System.out.println("\n=== DOCTOR ENCONTRADO ===");
            System.out.println("ID: " + doctor.getIdDoctor());
            System.out.println("Nombre: " + doctor.getNombreCompleto());
            System.out.println("DNI: " + doctor.getDni());
            System.out.println("Licencia: " + doctor.getNumeroLicencia());
            System.out.println("Especialidad: " + 
                             (doctor.getEspecialidad() != null ? doctor.getEspecialidad().getNombreEspecialidad() : "N/A"));
            System.out.println("Estado: " + doctor.getEstado());
        } else {
            System.out.println("✗ No se encontró doctor con esa licencia.");
        }
    }
    
    private static void listarDoctoresPorEspecialidad() {
        System.out.println("Especialidades disponibles:");
        List<Especialidad> especialidades = hospital.getEspecialidades();
        
        for (int i = 0; i < especialidades.size(); i++) {
            System.out.println((i + 1) + ". " + especialidades.get(i).getNombreEspecialidad());
        }
        
        System.out.print("Seleccione especialidad: ");
        int indice = scanner.nextInt() - 1;
        scanner.nextLine();
        
        if (indice >= 0 && indice < especialidades.size()) {
            String nombreEsp = especialidades.get(indice).getNombreEspecialidad();
            List<Doctor> doctores = hospital.getDoctoresPorEspecialidad(nombreEsp);
            
            System.out.println("\n=== DOCTORES DE " + nombreEsp.toUpperCase() + " ===");
            
            if (doctores.isEmpty()) {
                System.out.println("No hay doctores registrados en esta especialidad.");
            } else {
                for (Doctor doctor : doctores) {
                    System.out.println("- " + doctor.toString());
                }
            }
        }
    }
    
    private static void menuPacientes() {
        int opcion;
        
        do {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║       GESTIÓN DE PACIENTES         ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║ 1. Registrar nuevo paciente        ║");
            System.out.println("║ 2. Listar todos los pacientes      ║");
            System.out.println("║ 3. Buscar paciente por historia    ║");
            System.out.println("║ 0. Volver al menú principal        ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print("Seleccione una opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1: registrarPaciente(); break;
                case 2: listarPacientes(); break;
                case 3: buscarPacientePorHistoria(); break;
                case 0: break;
                default: System.out.println("Opción inválida."); break;
            }
        } while (opcion != 0);
    }
    
    private static void registrarPaciente() {
        System.out.println("\n=== REGISTRO DE NUEVO PACIENTE ===");
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        
        System.out.print("DNI: ");
        String dni = scanner.nextLine();
        
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();
        
        System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
        String fechaNacStr = scanner.nextLine();
        LocalDate fechaNacimiento = LocalDate.parse(fechaNacStr);
        
        System.out.print("Número de historia clínica: ");
        String numeroHistoria = scanner.nextLine();
        
        System.out.print("Tipo de sangre: ");
        String tipoSangre = scanner.nextLine();
        
        System.out.print("Seguro médico: ");
        String seguroMedico = scanner.nextLine();
        
        Paciente paciente = new Paciente(nombre, apellido, dni, telefono, email, direccion,
                                        fechaNacimiento, numeroHistoria, tipoSangre, seguroMedico);
        
        System.out.print("Alergias (opcional): ");
        String alergias = scanner.nextLine();
        if (!alergias.isEmpty()) {
            paciente.setAlergias(alergias);
        }
        
        System.out.print("Contacto de emergencia: ");
        String contactoEmergencia = scanner.nextLine();
        paciente.setContactoEmergencia(contactoEmergencia);
        
        System.out.print("Teléfono de emergencia: ");
        String telefonoEmergencia = scanner.nextLine();
        paciente.setTelefonoEmergencia(telefonoEmergencia);
        
        if (dao.insertarPaciente(paciente)) {
            hospital.agregarPaciente(paciente);
            System.out.println("✓ Paciente registrado exitosamente con ID: " + paciente.getIdPaciente());
        } else {
            System.out.println("✗ Error al registrar el paciente.");
        }
    }
    
    private static void listarPacientes() {
        System.out.println("\n=== LISTA DE PACIENTES ===");
        List<Paciente> pacientes = hospital.getPacientes();
        
        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }
        
        System.out.printf("%-5s %-25s %-15s %-10s %-20s\n", 
                         "ID", "NOMBRE COMPLETO", "HISTORIA", "SANGRE", "SEGURO");
        System.out.println("─".repeat(85));
        
        for (Paciente paciente : pacientes) {
            System.out.printf("%-5d %-25s %-15s %-10s %-20s\n",
                            paciente.getIdPaciente(),
                            paciente.getNombreCompleto(),
                            paciente.getNumeroHistoria(),
                            paciente.getTipoSangre() != null ? paciente.getTipoSangre() : "N/A",
                            paciente.getSeguroMedico() != null ? paciente.getSeguroMedico() : "Sin seguro");
        }
    }
    
    private static void buscarPacientePorHistoria() {
        System.out.print("Ingrese número de historia clínica: ");
        String historia = scanner.nextLine();
        
        Paciente paciente = dao.buscarPacientePorHistoria(historia);
        
        if (paciente != null) {
            System.out.println("\n=== PACIENTE ENCONTRADO ===");
            System.out.println("ID: " + paciente.getIdPaciente());
            System.out.println("Nombre: " + paciente.getNombreCompleto());
            System.out.println("DNI: " + paciente.getDni());
            System.out.println("Historia: " + paciente.getNumeroHistoria());
            System.out.println("Tipo de sangre: " + paciente.getTipoSangre());
            System.out.println("Teléfono: " + paciente.getTelefono());
        } else {
            System.out.println("✗ No se encontró paciente con esa historia clínica.");
        }
    }
    
    private static void menuCitas() {
        int opcion;
        
        do {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║         GESTIÓN DE CITAS           ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║ 1. Agendar nueva cita              ║");
            System.out.println("║ 2. Listar todas las citas          ║");
            System.out.println("║ 3. Citas de hoy                    ║");
            System.out.println("║ 4. Completar cita                  ║");
            System.out.println("║ 5. Cancelar cita                   ║");
            System.out.println("║ 0. Volver al menú principal        ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print("Seleccione una opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1: agendarCita(); break;
                case 2: listarCitas(); break;
                case 3: mostrarCitasDeHoy(); break;
                case 4: completarCita(); break;
                case 5: cancelarCita(); break;
                case 0: break;
                default: System.out.println("Opción inválida."); break;
            }
        } while (opcion != 0);
    }
    
    private static void agendarCita() {
        System.out.println("\n=== AGENDAR NUEVA CITA ===");
        
        // Buscar paciente
        System.out.print("Número de historia del paciente: ");
        String historia = scanner.nextLine();
        Paciente paciente = dao.buscarPacientePorHistoria(historia);
        
        if (paciente == null) {
            System.out.println("✗ Paciente no encontrado.");
            return;
        }
        
        // Buscar doctor
        System.out.print("Número de licencia del doctor: ");
        String licencia = scanner.nextLine();
        Doctor doctor = dao.buscarDoctorPorLicencia(licencia);
        
        if (doctor == null) {
            System.out.println("✗ Doctor no encontrado.");
            return;
        }
        
        System.out.print("Fecha y hora de la cita (YYYY-MM-DD HH:MM): ");
        String fechaHoraStr = scanner.nextLine();
        LocalDateTime fechaCita = LocalDateTime.parse(fechaHoraStr.replace(" ", "T"));
        
        System.out.print("Motivo de la consulta: ");
        String motivo = scanner.nextLine();
        
        System.out.print("Costo de la consulta: ");
        BigDecimal costo = scanner.nextBigDecimal();
        scanner.nextLine();
        
        Cita cita = new Cita(paciente, doctor, fechaCita, motivo, costo);
        
        if (dao.insertarCita(cita)) {
            hospital.agendarCita(cita);
            System.out.println("✓ Cita agendada exitosamente con ID: " + cita.getIdCita());
            System.out.println("Paciente: " + paciente.getNombreCompleto());
            System.out.println("Doctor: " + doctor.getNombreCompleto());
            System.out.println("Fecha: " + fechaCita);
        } else {
            System.out.println("✗ Error al agendar la cita.");
        }
    }
    
    private static void listarCitas() {
        System.out.println("\n=== LISTA DE CITAS ===");
        List<Cita> citas = hospital.getCitas();
        
        if (citas.isEmpty()) {
            System.out.println("No hay citas registradas.");
            return;
        }
        
        System.out.printf("%-5s %-20s %-20s %-17s %-12s\n", 
                         "ID", "PACIENTE", "DOCTOR", "FECHA/HORA", "ESTADO");
        System.out.println("─".repeat(85));
        
        for (Cita cita : citas) {
            System.out.printf("%-5d %-20s %-20s %-17s %-12s\n",
                            cita.getIdCita(),
                            cita.getPaciente().getNombreCompleto(),
                            cita.getDoctor().getNombreCompleto(),
                            cita.getFechaCita().toString().replace("T", " "),
                            cita.getEstadoCita());
        }
    }
    
    private static void mostrarCitasDeHoy() {
        System.out.println("\n=== CITAS DE HOY ===");
        List<Cita> citasHoy = hospital.getCitasDeHoy();
        
        if (citasHoy.isEmpty()) {
            System.out.println("No hay citas programadas para hoy.");
            return;
        }
        
        for (Cita cita : citasHoy) {
            System.out.println("─".repeat(50));
            System.out.println(cita.getResumenCita());
        }
        System.out.println("─".repeat(50));
    }
    
    private static void completarCita() {
        System.out.print("ID de la cita a completar: ");
        int idCita = scanner.nextInt();
        scanner.nextLine();
        
        // Buscar la cita en la lista del hospital
        Cita cita = hospital.getCitas().stream()
                          .filter(c -> c.getIdCita() == idCita)
                          .findFirst()
                          .orElse(null);
        
        if (cita == null || !cita.estaProgramada()) {
            System.out.println("✗ Cita no encontrada o no está programada.");
            return;
        }
        
        System.out.print("Diagnóstico: ");
        String diagnostico = scanner.nextLine();
        
        System.out.print("Tratamiento: ");
        String tratamiento = scanner.nextLine();
        
        cita.completarCita(diagnostico, tratamiento);
        
        // Actualizar en base de datos
        if (dao.actualizarEstadoCita(idCita, "COMPLETADA", 
                                   "Diagnóstico: " + diagnostico + " | Tratamiento: " + tratamiento)) {
            System.out.println("✓ Cita completada exitosamente.");
        } else {
            System.out.println("✗ Error al actualizar la cita en la base de datos.");
        }
    }
    
    private static void cancelarCita() {
        System.out.print("ID de la cita a cancelar: ");
        int idCita = scanner.nextInt();
        scanner.nextLine();
        
        // Buscar la cita
        Cita cita = hospital.getCitas().stream()
                          .filter(c -> c.getIdCita() == idCita)
                          .findFirst()
                          .orElse(null);
        
        if (cita == null || !cita.estaProgramada()) {
            System.out.println("✗ Cita no encontrada o no está programada.");
            return;
        }
        
        System.out.print("Motivo de cancelación: ");
        String motivo = scanner.nextLine();
        
        cita.cancelarCita(motivo);
        
        // Actualizar en base de datos
        if (dao.actualizarEstadoCita(idCita, "CANCELADA", "Cancelada: " + motivo)) {
            System.out.println("✓ Cita cancelada exitosamente.");
        } else {
            System.out.println("✗ Error al actualizar la cita en la base de datos.");
        }
    }
    
    private static void menuHabitaciones() {
        int opcion;
        
        do {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║      GESTIÓN DE HABITACIONES       ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║ 1. Listar todas las habitaciones   ║");
            System.out.println("║ 2. Habitaciones disponibles        ║");
            System.out.println("║ 3. Habitaciones por tipo           ║");
            System.out.println("║ 4. Cambiar estado de habitación    ║");
            System.out.println("║ 0. Volver al menú principal        ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print("Seleccione una opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1: listarHabitaciones(); break;
                case 2: listarHabitacionesDisponibles(); break;
                case 3: listarHabitacionesPorTipo(); break;
                case 4: cambiarEstadoHabitacion(); break;
                case 0: break;
                default: System.out.println("Opción inválida."); break;
            }
        } while (opcion != 0);
    }
    
    private static void listarHabitaciones() {
        System.out.println("\n=== LISTA DE HABITACIONES ===");
        List<Habitacion> habitaciones = hospital.getHabitaciones();
        
        if (habitaciones.isEmpty()) {
            System.out.println("No hay habitaciones registradas.");
            return;
        }
        
        System.out.printf("%-5s %-10s %-15s %-10s %-10s %-15s\n", 
                         "ID", "NÚMERO", "TIPO", "CAPACIDAD", "PRECIO/DÍA", "ESTADO");
        System.out.println("─".repeat(75));
        
        for (Habitacion hab : habitaciones) {
            System.out.printf("%-5d %-10s %-15s %-10d $%-9.2f %-15s\n",
                            hab.getIdHabitacion(),
                            hab.getNumeroHabitacion(),
                            hab.getTipoHabitacion(),
                            hab.getCapacidad(),
                            hab.getPrecioDia(),
                            hab.getEstado());
        }
    }
    
    private static void listarHabitacionesDisponibles() {
        System.out.println("\n=== HABITACIONES DISPONIBLES ===");
        List<Habitacion> disponibles = hospital.getHabitacionesDisponibles();
        
        if (disponibles.isEmpty()) {
            System.out.println("No hay habitaciones disponibles.");
            return;
        }
        
        System.out.printf("%-10s %-15s %-10s %-15s\n", 
                         "NÚMERO", "TIPO", "CAPACIDAD", "PRECIO/DÍA");
        System.out.println("─".repeat(55));
        
        for (Habitacion hab : disponibles) {
            System.out.printf("%-10s %-15s %-10d $%-14.2f\n",
                            hab.getNumeroHabitacion(),
                            hab.getTipoHabitacion(),
                            hab.getCapacidad(),
                            hab.getPrecioDia());
        }
    }
    
    private static void listarHabitacionesPorTipo() {
        System.out.println("Tipos de habitación disponibles:");
        System.out.println("1. INDIVIDUAL");
        System.out.println("2. DOBLE");
        System.out.println("3. UCI");
        System.out.println("4. EMERGENCIA");
        
        System.out.print("Seleccione tipo: ");
        int opcionTipo = scanner.nextInt();
        scanner.nextLine();
        
        String[] tipos = {"INDIVIDUAL", "DOBLE", "UCI", "EMERGENCIA"};
        
        if (opcionTipo >= 1 && opcionTipo <= 4) {
            String tipoSeleccionado = tipos[opcionTipo - 1];
            List<Habitacion> habitaciones = hospital.getHabitacionesPorTipo(tipoSeleccionado);
            
            System.out.println("\n=== HABITACIONES TIPO " + tipoSeleccionado + " ===");
            
            if (habitaciones.isEmpty()) {
                System.out.println("No hay habitaciones de este tipo.");
            } else {
                for (Habitacion hab : habitaciones) {
                    System.out.println("- " + hab.toString());
                }
            }
        } else {
            System.out.println("Opción inválida.");
        }
    }
    
    private static void cambiarEstadoHabitacion() {
        System.out.print("Número de habitación: ");
        String numeroHab = scanner.nextLine();
        
        Habitacion habitacion = hospital.buscarHabitacionPorNumero(numeroHab);
        
        if (habitacion == null) {
            System.out.println("✗ Habitación no encontrada.");
            return;
        }
        
        System.out.println("Estado actual: " + habitacion.getEstado());
        System.out.println("Nuevos estados disponibles:");
        System.out.println("1. DISPONIBLE");
        System.out.println("2. OCUPADA");
        System.out.println("3. MANTENIMIENTO");
        
        System.out.print("Seleccione nuevo estado: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        
        String[] estados = {"DISPONIBLE", "OCUPADA", "MANTENIMIENTO"};
        
        if (opcion >= 1 && opcion <= 3) {
            String nuevoEstado = estados[opcion - 1];
            
            if (dao.actualizarEstadoHabitacion(habitacion.getIdHabitacion(), nuevoEstado)) {
                habitacion.setEstado(nuevoEstado);
                System.out.println("✓ Estado actualizado a: " + nuevoEstado);
            } else {
                System.out.println("✗ Error al actualizar el estado.");
            }
        } else {
            System.out.println("Opción inválida.");
        }
    }
    
    private static void menuReportes() {
        int opcion;
        
        do {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║     REPORTES Y ESTADÍSTICAS        ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║ 1. Estadísticas generales          ║");
            System.out.println("║ 2. Resumen de departamentos        ║");
            System.out.println("║ 3. Citas programadas hoy           ║");
            System.out.println("║ 4. Ocupación de habitaciones       ║");
            System.out.println("║ 5. Doctores por especialidad       ║");
            System.out.println("║ 0. Volver al menú principal        ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.print("Seleccione una opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1: hospital.mostrarEstadisticasGenerales(); break;
                case 2: hospital.mostrarResumenDepartamentos(); break;
                case 3: hospital.mostrarCitasDelDia(); break;
                case 4: mostrarEstadisticasHabitaciones(); break;
                case 5: mostrarDoctoresPorEspecialidad(); break;
                case 0: break;
                default: System.out.println("Opción inválida."); break;
            }
        } while (opcion != 0);
    }
    
    private static void mostrarEstadisticasHabitaciones() {
        System.out.println("\n=== ESTADÍSTICAS DE HABITACIONES ===");
        
        int totalHabitaciones = hospital.getHabitaciones().size();
        int disponibles = dao.contarHabitacionesPorEstado("DISPONIBLE");
        int ocupadas = dao.contarHabitacionesPorEstado("OCUPADA");
        int mantenimiento = dao.contarHabitacionesPorEstado("MANTENIMIENTO");
        
        System.out.println("Total de habitaciones: " + totalHabitaciones);
        System.out.println("Disponibles: " + disponibles + " (" + 
                          String.format("%.1f", (disponibles * 100.0 / totalHabitaciones)) + "%)");
        System.out.println("Ocupadas: " + ocupadas + " (" + 
                          String.format("%.1f", (ocupadas * 100.0 / totalHabitaciones)) + "%)");
        System.out.println("En mantenimiento: " + mantenimiento + " (" + 
                          String.format("%.1f", (mantenimiento * 100.0 / totalHabitaciones)) + "%)");
        
        System.out.println("\nTasa de ocupación: " + 
                          String.format("%.1f", (ocupadas * 100.0 / totalHabitaciones)) + "%");
    }
    
    private static void mostrarDoctoresPorEspecialidad() {
        System.out.println("\n=== DOCTORES POR ESPECIALIDAD ===");
        
        List<Especialidad> especialidades = hospital.getEspecialidades();
        
        for (Especialidad esp : especialidades) {
            int cantidad = dao.contarDoctoresPorEspecialidad(esp.getIdEspecialidad());
            System.out.println(esp.getNombreEspecialidad() + ": " + cantidad + " doctor(es)");
        }
        
        System.out.println("\nTotal de doctores activos: " + hospital.getDoctoresDisponibles().size());
    }
    
    private static void mostrarInfoHospital() {
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║            INFORMACIÓN DEL HOSPITAL           ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        
        System.out.println("Nombre: " + hospital.getNombreHospital());
        System.out.println("Dirección: " + hospital.getDireccion());
        System.out.println("Teléfono: " + hospital.getTelefono());
        System.out.println("Email: " + hospital.getEmail());
        
        System.out.println("\n" + hospital.toString());
        
        System.out.println("\n=== SISTEMA DESARROLLADO CON ===");
        System.out.println("✓ Programación Orientada a Objetos");
        System.out.println("✓ Herencia (Persona → Doctor/Paciente/Enfermero)");
        System.out.println("✓ Relaciones de Agregación y Composición");
        System.out.println("✓ Base de datos SQL Server");
        System.out.println("✓ Patrón DAO para acceso a datos");
        System.out.println("✓ Gestión completa de hospital");
    }
}