package hospitalD;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;

/**
* SISTEMA HOSPITALARIO COMPLETO - VERSIÓN INTEGRAL CORREGIDA
* Autor: Sistema de Gestión Hospitalaria
* Versión: 2.0 COMPLETA
* Fecha: 2024
*/
public class SistemaHospitalario_COMPLETO {

    // Instancias del sistema
    private static Hospital hospital;
    private static HospitalDAO hospitalDAO;
    private static Scanner scanner;

    // Colores para la interfaz de consola (códigos ANSI)
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BOLD = "\u001B[1m";

    public static void main(String[] args) {
        // Inicializar sistema
        inicializarSistema();
        
        // Ejecutar menú principal
        ejecutarMenuPrincipal();
        
        // Cerrar sistema
        cerrarSistema();
    }

    // ============= MÉTODOS UTILITARIOS FALTANTES =============
    
    private static void pausar() {
        System.out.println("\nPresione Enter para continuar...");
        try {
            scanner.nextLine();
        } catch (Exception e) {
            // Manejo silencioso del error
        }
    }

    private static void limpiarPantalla() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[2J\033[H");
                System.out.flush();
            }
        } catch (Exception e) {
            // Fallback - imprimir líneas vacías
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    private static int leerOpcion(int min, int max) {
        int opcion = -1;
        while (opcion < min || opcion > max) {
            try {
                String input = scanner.nextLine().trim();
                opcion = Integer.parseInt(input);
                if (opcion < min || opcion > max) {
                    System.out.print("Opción inválida. Ingrese un número entre " + min + " y " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
        return opcion;
    }

    private static int leerEntero() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }

    private static void mostrarHeader(String titulo) {
        limpiarPantalla();
        System.out.println("\n" + "=".repeat(80));
        System.out.println("  " + titulo);
        System.out.println("=".repeat(80));
    }

    private static void mostrarError(String mensaje) {
        System.out.println(RED + " X ERROR: " + mensaje + RESET);
    }

    private static void mostrarExito(String mensaje) {
        System.out.println(GREEN + "/" + mensaje + RESET);
    }

    private static boolean confirmar(String mensaje) {
        System.out.print(mensaje + " (s/n): ");
        String respuesta = scanner.nextLine().trim();
        return "s".equalsIgnoreCase(respuesta) || "si".equalsIgnoreCase(respuesta) || "y".equalsIgnoreCase(respuesta);
    }

    private static boolean confirmarSalida() {
        return confirmar("¿Está seguro que desea salir del sistema?");
    }

    // ============= INICIALIZACIÓN DEL SISTEMA =============

    private static void inicializarSistema() {
        scanner = new Scanner(System.in);
        hospitalDAO = new HospitalDAO();

        mostrarPantallaInicio();

        // Crear hospital principal
        hospital = new Hospital(
            "HOSPITAL GENERAL SANTA MARÍA",
            "Av. Principal 123, Ciudad Central",
            "+51-987654321",
            "contacto@hospitalsantamaria.pe"
        );
        hospital.setIdHospital(1);

        // Cargar datos de prueba
        cargarDatosDePrueba();

        // Verificar conexión a BD
        verificarConexionBD();

        System.out.println(GREEN + " Sistema inicializado correctamente" + RESET);
        pausar();
    }

    private static void cerrarSistema() {
        System.out.println(CYAN + "\n Cerrando Sistema Hospitalario..." + RESET);
        System.out.println("Gracias por usar el sistema de gestión hospitalaria.");
        if (scanner != null) {
            scanner.close();
        }
    }

    private static void mostrarPantallaInicio() {
        limpiarPantalla();
        System.out.println(CYAN + "╔═══════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                                   ║");
        System.out.println("║            SISTEMA INTEGRAL DE GESTIÓN HOSPITALARIA v2.0                      ║");
        System.out.println("║                                                                                   ║");
        System.out.println("║                        Hospital General Santa María                               ║");
        System.out.println("║                                                                                   ║");
        System.out.println("║     Gestión Completa      Emergencias        Laboratorio      Farmacia      ║");
        System.out.println("║     Personal Médico       Citas Médicas      Quirófanos       Reportes      ║");
        System.out.println("║                                                                                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        System.out.println(YELLOW + " Inicializando sistema..." + RESET);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void cargarDatosDePrueba() {
        System.out.println(YELLOW + "📋 Cargando datos de prueba..." + RESET);

        // Crear especialidades
        crearEspecialidades();

        // Crear personal médico
        crearPersonalMedico();

        // Crear pacientes
        crearPacientes();

        // Crear infraestructura
        crearInfraestructura();

        // Crear seguros médicos
        crearSegurosMedicos();

        System.out.println(GREEN + "✅ Datos de prueba cargados" + RESET);
    }

    private static void crearEspecialidades() {
        Especialidad cardiologia = new Especialidad("Cardiología", "Especialidad médica que se encarga del estudio del corazón");
        cardiologia.setIdEspecialidad(1);
        hospital.agregarEspecialidad(cardiologia);

        Especialidad pediatria = new Especialidad("Pediatría", "Medicina especializada en la salud de bebés, niños y adolescentes");
        pediatria.setIdEspecialidad(2);
        hospital.agregarEspecialidad(pediatria);

        Especialidad neurologia = new Especialidad("Neurología", "Especialidad médica que estudia el sistema nervioso");
        neurologia.setIdEspecialidad(3);
        hospital.agregarEspecialidad(neurologia);

        Especialidad cirugia = new Especialidad("Cirugía General", "Especialidad quirúrgica general");
        cirugia.setIdEspecialidad(4);
        hospital.agregarEspecialidad(cirugia);
    }

    private static void crearPersonalMedico() {
        // Crear doctores
        Doctor doctor1 = new Doctor(
            "Carlos", "Mendoza", "12345678", "987654321", "cmendoza@hospital.pe",
            "Av. Los Médicos 123", LocalDate.of(1980, 5, 15),
            "LIC-001", hospital.buscarEspecialidadPorNombre("Cardiología"),
            LocalDate.of(2010, 3, 1), BigDecimal.valueOf(8000),
            LocalTime.of(8, 0), LocalTime.of(17, 0)
        );
        doctor1.setIdDoctor(1);
        hospital.agregarDoctor(doctor1);

        Doctor doctor2 = new Doctor(
            "María", "González", "87654321", "123456789", "mgonzalez@hospital.pe",
            "Jr. Salud 456", LocalDate.of(1985, 8, 22),
            "LIC-002", hospital.buscarEspecialidadPorNombre("Pediatría"),
            LocalDate.of(2012, 6, 15), BigDecimal.valueOf(7500),
            LocalTime.of(9, 0), LocalTime.of(18, 0)
        );
        doctor2.setIdDoctor(2);
        hospital.agregarDoctor(doctor2);

        // Crear enfermeros
        Enfermero enfermero1 = new Enfermero(
            "Ana", "López", "11111111", "999888777", "alopez@hospital.pe",
            "Calle Enfermería 789", LocalDate.of(1990, 12, 10),
            "ENF-001", "MAÑANA", "UCI", LocalDate.of(2015, 1, 10)
        );
        enfermero1.setIdEnfermero(1);
        hospital.agregarEnfermero(enfermero1);

        Enfermero enfermero2 = new Enfermero(
            "Luis", "Ramírez", "22222222", "888777666", "lramirez@hospital.pe",
            "Av. Cuidados 321", LocalDate.of(1988, 4, 8),
            "ENF-002", "NOCHE", "Emergencias", LocalDate.of(2013, 9, 20)
        );
        enfermero2.setIdEnfermero(2);
        hospital.agregarEnfermero(enfermero2);
    }

    private static void crearPacientes() {
        Paciente paciente1 = new Paciente(
            "Juan", "Pérez", "33333333", "555444333", "jperez@email.com",
            "Calle Los Pacientes 111", LocalDate.of(1975, 3, 20),
            "HIST-001", "O+", "EPS Salud"
        );
        paciente1.setIdPaciente(1);
        paciente1.setAlergias("Penicilina");
        paciente1.setEnfermedadesCronicas("Diabetes Tipo 2");
        paciente1.setContactoEmergencia("María Pérez");
        paciente1.setTelefonoEmergencia("999111222");
        hospital.agregarPaciente(paciente1);

        Paciente paciente2 = new Paciente(
            "Carmen", "Silva", "44444444", "666555444", "csilva@email.com",
            "Jr. Bienestar 222", LocalDate.of(1990, 7, 15),
            "HIST-002", "A-", "Seguro Integral"
        );
        paciente2.setIdPaciente(2);
        paciente2.setAlergias("Ninguna");
        paciente2.setContactoEmergencia("Roberto Silva");
        paciente2.setTelefonoEmergencia("888999000");
        hospital.agregarPaciente(paciente2);
    }

    private static void crearInfraestructura() {
        // Crear departamentos
        Departamento emergencias = new Departamento(
            "Emergencias", "Planta Baja - Ala Norte", "2001", BigDecimal.valueOf(50000)
        );
        emergencias.setIdDepartamento(1);
        hospital.getDepartamentos().add(emergencias);

        // Crear habitaciones
        Habitacion hab1 = new Habitacion(
            "101", "INDIVIDUAL", emergencias, 1, BigDecimal.valueOf(150)
        );
        hab1.setIdHabitacion(1);
        hospital.agregarHabitacion(hab1);

        Habitacion hab2 = new Habitacion(
            "201", "UCI", emergencias, 1, BigDecimal.valueOf(300)
        );
        hab2.setIdHabitacion(2);
        hospital.agregarHabitacion(hab2);

        // Crear ambulancias
        Ambulancia ambulancia1 = new Ambulancia("AMB-001", "Mercedes Benz", "AVANZADA", 2);
        ambulancia1.setIdAmbulancia(1);
        ambulancia1.setAño(2020);
        ambulancia1.setEquiposMedicos("Desfibrilador, Ventilador, Monitor de signos vitales");
        ambulancia1.setUbicacionActual("Base Central");
        hospital.agregarAmbulancia(ambulancia1);

        // Crear farmacia
        Farmacia farmacia1 = new Farmacia(
            "Farmacia Central", "Planta Baja - Hall Principal", "2050",
            LocalTime.of(6, 0), LocalTime.of(22, 0)
        );
        farmacia1.setIdFarmacia(1);
        hospital.agregarFarmacia(farmacia1);

        // Crear laboratorio
        Laboratorio lab1 = new Laboratorio(
            "Laboratorio Central", "Segundo Piso - Ala Este", "2100",
            "Analizador automático, Microscopios, Centrífugas",
            LocalTime.of(6, 0), LocalTime.of(20, 0)
        );
        lab1.setIdLaboratorio(1);
        hospital.agregarLaboratorio(lab1);

        // Crear tipos de examen
        TipoExamen hemograma = new TipoExamen(
            "Hemograma Completo", "Análisis completo de la sangre", 
            BigDecimal.valueOf(35), 4, false, "No requiere preparación especial"
        );
        hemograma.setIdTipoExamen(1);
        hemograma.setLaboratorio(lab1);
        hospital.agregarTipoExamen(hemograma);

        lab1.agregarTipoExamen(hemograma);

        // Crear medicamentos
        crearMedicamentos();
    }

    private static void crearMedicamentos() {
        // Crear proveedor
        Proveedor proveedor1 = new Proveedor(
            "FarmaDistribuidora S.A.", "20123456789", "Av. Industrial 500",
            "01-234-5678", "ventas@farmadist.pe", "Carlos Distribuidor"
        );
        proveedor1.setIdProveedor(1);
        hospital.agregarProveedor(proveedor1);

        // Crear medicamentos
        MedicamentoAmpliado paracetamol = new MedicamentoAmpliado(
            "Paracetamol", "Acetaminofén", "Tabletas", "500mg",
            100, BigDecimal.valueOf(0.50)
        );
        paracetamol.setIdMedicamento(1);
        paracetamol.setStockMinimo(20);
        paracetamol.setFechaVencimiento(LocalDate.now().plusMonths(24));
        paracetamol.setProveedor(proveedor1);
        paracetamol.setLote("LOT2024001");
        paracetamol.setTemperaturaAlmacenamiento("Temperatura ambiente (15-25°C)");
        hospital.agregarMedicamento(paracetamol);

        MedicamentoAmpliado amoxicilina = new MedicamentoAmpliado(
            "Amoxicilina", "Amoxicilina", "Cápsulas", "250mg",
            50, BigDecimal.valueOf(1.20)
        );
        amoxicilina.setIdMedicamento(2);
        amoxicilina.setStockMinimo(15);
        amoxicilina.setFechaVencimiento(LocalDate.now().plusMonths(18));
        amoxicilina.setProveedor(proveedor1);
        amoxicilina.setLote("LOT2024002");
        amoxicilina.setTemperaturaAlmacenamiento("Refrigeración (2-8°C)");
        hospital.agregarMedicamento(amoxicilina);
    }

    private static void crearSegurosMedicos() {
        SeguroMedico eps = new SeguroMedico("EPS Salud Total", "EPS", BigDecimal.valueOf(80));
        eps.setIdSeguro(1);
        eps.setTelefonoSeguro("01-800-1234");
        eps.setEmailSeguro("atencion@epssalud.pe");
        eps.configurarCoberturasPorTipo();
        hospital.agregarSeguroMedico(eps);

        SeguroMedico particular = new SeguroMedico("Seguro Premium", "PARTICULAR", BigDecimal.valueOf(95));
        particular.setIdSeguro(2);
        particular.setTelefonoSeguro("01-800-5678");
        particular.setEmailSeguro("premium@seguros.pe");
        particular.configurarCoberturasPorTipo();
        hospital.agregarSeguroMedico(particular);
    }

    private static void verificarConexionBD() {
        System.out.print(YELLOW + "🔗 Verificando conexión a base de datos... " + RESET);

        if (hospitalDAO.probarConexion()) {
            System.out.println(GREEN + "✅ Conectado" + RESET);
        } else {
            System.out.println(RED + "❌ Sin conexión (funcionando en modo local)" + RESET);
        }
    }

    // ============= MENÚ PRINCIPAL =============

    private static void ejecutarMenuPrincipal() {
        boolean continuar = true;

        while (continuar) {
            mostrarMenuPrincipal();
            int opcion = leerOpcion(0, 12);
            
            switch (opcion) {
                case 1:
                    gestionPersonalMedico();
                    break;
                case 2:
                    gestionPacientes();
                    break;
                case 3:
                    gestionCitas();
                    break;
                case 4:
                    gestionEmergencias();
                    break;
                case 5:
                    gestionFarmacia();
                    break;
                case 6:
                    gestionLaboratorio();
                    break;
                case 7:
                    gestionQuirofanos();
                    break;
                case 8:
                    monitoreoSignosVitales();
                    break;
                case 9:
                    gestionSegurosMedicos();
                    break;
                case 10:
                    reportesEstadisticas();
                    break;
                case 11:
                    configuracionSistema();
                    break;
                case 12:
                    ayudaSoporte();
                    break;
                case 0:
                    if (confirmarSalida()) {
                        continuar = false;
                    }
                    break;
                default:
                    mostrarError("Opción no válida");
                    break;
            }
        }
    }

    private static void mostrarMenuPrincipal() {
        limpiarPantalla();
        mostrarHeader("MENÚ PRINCIPAL - SISTEMA HOSPITALARIO");

        System.out.println(CYAN + "┌─────────────────────────────────────────────────────────────────┐");
        System.out.println("│                    🏥 OPCIONES PRINCIPALES                      │");
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.println("│  1️  Gestión de Personal Médico                                │");
        System.out.println("│  2️  Gestión de Pacientes                                      │");
        System.out.println("│  3️  Gestión de Citas Médicas                                 │");
        System.out.println("│  4️  Gestión de Emergencias                                   │");
        System.out.println("│  5️  Gestión de Farmacia                                      │");
        System.out.println("│  6️  Gestión de Laboratorio                                   │");
        System.out.println("│  7️  Gestión de Quirófanos                                    │");
        System.out.println("│  8️  Monitoreo de Signos Vitales                             │");
        System.out.println("│  9  Gestión de Seguros Médicos                              │");
        System.out.println("│  10 Reportes y Estadísticas                                   │");
        System.out.println("│  1️1 Configuración del Sistema                               │");
        System.out.println("│  1️2 Ayuda y Soporte                                         │");
        System.out.println("│  0️  Salir del Sistema                                        │");
        System.out.println("└─────────────────────────────────────────────────────────────────┘" + RESET);

        mostrarStatusBar();
        System.out.print(BOLD + "Seleccione una opción: " + RESET);
    }

    private static void mostrarStatusBar() {
        System.out.println();
        System.out.println(BLUE + " Hospital: " + hospital.getNombreHospital() + 
                          " |  Personal: " + (hospital.getDoctores().size() + hospital.getEnfermeros().size()) +
                          " |  Pacientes: " + hospital.getPacientes().size() +
                          " |  Emergencias: " + hospital.getEmergenciasActivas().size() + RESET);
        System.out.println();
    }

    // ============= MÉTODOS STUB PARA FUNCIONALIDADES =============
    
 // Reemplaza el método gestionPersonalMedico() en tu SistemaHospitalario_COMPLETO.java

    private static void gestionPersonalMedico() {
        boolean continuar = true;

        while (continuar) {
            limpiarPantalla();
            mostrarHeader("GESTIÓN DE PERSONAL MÉDICO");
            
            System.out.println(GREEN + "┌─────────────────────────────────────────────────────────┐");
            System.out.println("│                👨‍⚕️ PERSONAL MÉDICO                          │");
            System.out.println("├─────────────────────────────────────────────────────────┤");
            System.out.println("│  1️   Registrar Doctor                                     │");
            System.out.println("│  2️   Registrar Enfermero                                 │");
            System.out.println("│  3️   Listar Todo el Personal                             │");
            System.out.println("│  4️   Buscar Personal                                     │");
            System.out.println("│  5️   Gestionar Especialidades                            │");
            System.out.println("│  6️   Horarios del Personal                               │");
            System.out.println("│  7️   Estadísticas del Personal                           │");
            System.out.println("│  0️   Volver al Menú Principal                            │");
            System.out.println("└─────────────────────────────────────────────────────────┘" + RESET);
            
            System.out.print(BOLD + "Seleccione una opción: " + RESET);
            int opcion = leerOpcion(0, 7);
            
            switch (opcion) {
                case 1:
                    registrarDoctor();
                    break;
                case 2:
                    registrarEnfermero();
                    break;
                case 3:
                    listarPersonalMedico();
                    break;
                case 4:
                    buscarPersonal();
                    break;
                case 5:
                    gestionarEspecialidades();
                    break;
                case 6:
                    gestionarHorarios();
                    break;
                case 7:
                    estadisticasPersonal();
                    break;
                case 0:
                    continuar = false;
                    break;
            }
        }
    }

    private static void registrarDoctor() {
        limpiarPantalla();
        mostrarHeader("REGISTRO DE DOCTOR");

        try {
            System.out.println(CYAN + " Ingrese los datos del doctor:" + RESET);
            System.out.println();
            
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
            LocalDate fechaNac = LocalDate.parse(scanner.nextLine());
            
            System.out.print("Número de licencia médica: ");
            String licencia = scanner.nextLine();
            
            // Mostrar especialidades disponibles
            System.out.println("\n Especialidades disponibles:");
            List<Especialidad> especialidades = hospital.getEspecialidades();
            for (int i = 0; i < especialidades.size(); i++) {
                System.out.println((i + 1) + ". " + especialidades.get(i).getNombreEspecialidad());
            }
            System.out.print("Seleccione especialidad (número): ");
            int indiceEsp = leerEntero() - 1;
            
            if (indiceEsp < 0 || indiceEsp >= especialidades.size()) {
                mostrarError("Especialidad no válida");
                return;
            }
            
            System.out.print("Fecha de ingreso (YYYY-MM-DD): ");
            LocalDate fechaIngreso = LocalDate.parse(scanner.nextLine());
            
            System.out.print("Salario mensual: ");
            BigDecimal salario = new BigDecimal(scanner.nextLine());
            
            System.out.print("Horario inicio (HH:MM): ");
            LocalTime horaInicio = LocalTime.parse(scanner.nextLine());
            
            System.out.print("Horario fin (HH:MM): ");
            LocalTime horaFin = LocalTime.parse(scanner.nextLine());
            
            // Crear doctor
            Doctor doctor = new Doctor(nombre, apellido, dni, telefono, email, direccion,
                                     fechaNac, licencia, especialidades.get(indiceEsp),
                                     fechaIngreso, salario, horaInicio, horaFin);
            
            doctor.setIdDoctor(hospital.getDoctores().size() + 1);
            hospital.agregarDoctor(doctor);
            
            // Intentar guardar en BD
            boolean guardadoDB = hospitalDAO.insertarDoctor(doctor);
            
            mostrarExito("Doctor registrado exitosamente" + 
                        (guardadoDB ? " (guardado en BD)" : " (solo en memoria)"));
            
        } catch (Exception e) {
            mostrarError("Error al registrar doctor: " + e.getMessage());
        }

        pausar();
    }

    private static void registrarEnfermero() {
        limpiarPantalla();
        mostrarHeader("REGISTRO DE ENFERMERO");

        try {
            System.out.println(CYAN + " Ingrese los datos del enfermero:" + RESET);
            System.out.println();
            
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
            LocalDate fechaNac = LocalDate.parse(scanner.nextLine());
            
            System.out.print("Número de colegiatura: ");
            String colegiatura = scanner.nextLine();
            
            System.out.println("Turnos disponibles: 1) MAÑANA  2) TARDE  3) NOCHE");
            System.out.print("Seleccione turno: ");
            int turnoOp = leerEntero();
            String turno = turnoOp == 1 ? "MAÑANA" : turnoOp == 2 ? "TARDE" : "NOCHE";
            
            System.out.print("Área de trabajo: ");
            String area = scanner.nextLine();
            
            System.out.print("Fecha de ingreso (YYYY-MM-DD): ");
            LocalDate fechaIngreso = LocalDate.parse(scanner.nextLine());
            
            // Crear enfermero
            Enfermero enfermero = new Enfermero(nombre, apellido, dni, telefono, email,
                                              direccion, fechaNac, colegiatura, turno,
                                              area, fechaIngreso);
            
            enfermero.setIdEnfermero(hospital.getEnfermeros().size() + 1);
            hospital.agregarEnfermero(enfermero);
            
            mostrarExito("Enfermero registrado exitosamente");
            
        } catch (Exception e) {
            mostrarError("Error al registrar enfermero: " + e.getMessage());
        }

        pausar();
    }

    private static void listarPersonalMedico() {
        limpiarPantalla();
        mostrarHeader("PERSONAL MÉDICO DEL HOSPITAL");

        System.out.println(GREEN + " DOCTORES (" + hospital.getDoctores().size() + "):" + RESET);
        System.out.println("═".repeat(80));

        if (hospital.getDoctores().isEmpty()) {
            System.out.println("No hay doctores registrados");
        } else {
            for (Doctor doctor : hospital.getDoctores()) {
                System.out.println("   Dr. " + doctor.getNombreCompleto());
                System.out.println("   Licencia: " + doctor.getNumeroLicencia());
                System.out.println("   Especialidad: " + 
                    (doctor.getEspecialidad() != null ? doctor.getEspecialidad().getNombreEspecialidad() : "Sin especialidad"));
                System.out.println("   Horario: " + doctor.getHorarioInicio() + " - " + doctor.getHorarioFin());
                System.out.println("   Estado: " + doctor.getEstado() + " | Experiencia: " + doctor.getAnosExperiencia() + " años");
                System.out.println();
            }
        }

        System.out.println(BLUE + " ENFERMEROS (" + hospital.getEnfermeros().size() + "):" + RESET);
        System.out.println("═".repeat(80));

        if (hospital.getEnfermeros().isEmpty()) {
            System.out.println("No hay enfermeros registrados");
        } else {
            for (Enfermero enfermero : hospital.getEnfermeros()) {
                System.out.println("👩‍⚕️ Enf. " + enfermero.getNombreCompleto());
                System.out.println("   Colegiatura: " + enfermero.getNumeroColegiatura());
                System.out.println("   Turno: " + enfermero.getTurno());
                System.out.println("   Área: " + enfermero.getAreaTrabajo());
                System.out.println("   Experiencia: " + enfermero.getAnosExperiencia() + " años");
                System.out.println();
            }
        }

        pausar();
    }

    private static void buscarPersonal() {
        limpiarPantalla();
        mostrarHeader("BUSCAR PERSONAL");

        System.out.print("Ingrese término de búsqueda (nombre, DNI, licencia): ");
        String termino = scanner.nextLine().toLowerCase();

        boolean encontrado = false;

        System.out.println(GREEN + "\n  RESULTADOS DE BÚSQUEDA:" + RESET);
        System.out.println("═".repeat(60));

        // Buscar doctores
        for (Doctor doctor : hospital.getDoctores()) {
            if (doctor.getNombreCompleto().toLowerCase().contains(termino) ||
                doctor.getDni().contains(termino) ||
                doctor.getNumeroLicencia().toLowerCase().contains(termino)) {
                
                System.out.println("   Dr. " + doctor.getNombreCompleto());
                System.out.println("   DNI: " + doctor.getDni());
                System.out.println("   Licencia: " + doctor.getNumeroLicencia());
                System.out.println("   Especialidad: " + 
                    (doctor.getEspecialidad() != null ? doctor.getEspecialidad().getNombreEspecialidad() : "General"));
                System.out.println();
                encontrado = true;
            }
        }

        // Buscar enfermeros
        for (Enfermero enfermero : hospital.getEnfermeros()) {
            if (enfermero.getNombreCompleto().toLowerCase().contains(termino) ||
                enfermero.getDni().contains(termino) ||
                enfermero.getNumeroColegiatura().toLowerCase().contains(termino)) {
                
                System.out.println("   Enf. " + enfermero.getNombreCompleto());
                System.out.println("   DNI: " + enfermero.getDni());
                System.out.println("   Colegiatura: " + enfermero.getNumeroColegiatura());
                System.out.println("   Área: " + enfermero.getAreaTrabajo());
                System.out.println();
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontraron resultados para: " + termino);
        }

        pausar();
    }

    private static void gestionarEspecialidades() {
        limpiarPantalla();
        mostrarHeader("GESTIÓN DE ESPECIALIDADES");

        System.out.println(CYAN + " ESPECIALIDADES DISPONIBLES:" + RESET);
        System.out.println("═".repeat(60));

        List<Especialidad> especialidades = hospital.getEspecialidades();
        if (especialidades.isEmpty()) {
            System.out.println("No hay especialidades registradas");
        } else {
            for (Especialidad esp : especialidades) {
                System.out.println("• " + esp.getNombreEspecialidad());
                if (esp.getDescripcion() != null) {
                    System.out.println("  " + esp.getDescripcion());
                }
                System.out.println();
            }
        }

        pausar();
    }

    private static void gestionarHorarios() {
        limpiarPantalla();
        mostrarHeader("HORARIOS DEL PERSONAL");

        System.out.println(GREEN + " HORARIOS DE DOCTORES:" + RESET);
        System.out.println("═".repeat(60));

        for (Doctor doctor : hospital.getDoctores()) {
            System.out.println("Dr. " + doctor.getNombreCompleto());
            System.out.println("   Horario: " + doctor.getHorarioInicio() + " - " + doctor.getHorarioFin());
            System.out.println("   Estado: " + (doctor.estaDisponible() ? GREEN + "Disponible" + RESET : RED + "No disponible" + RESET));
            System.out.println();
        }

        System.out.println(BLUE + " TURNOS DE ENFERMEROS:" + RESET);
        System.out.println("═".repeat(60));

        for (Enfermero enfermero : hospital.getEnfermeros()) {
            System.out.println("Enf. " + enfermero.getNombreCompleto());
            System.out.println("   Turno: " + enfermero.getTurno());
            System.out.println("   Área: " + enfermero.getAreaTrabajo());
            System.out.println();
        }

        pausar();
    }

    private static void estadisticasPersonal() {
        limpiarPantalla();
        mostrarHeader("ESTADÍSTICAS DEL PERSONAL");

        int totalDoctores = hospital.getDoctores().size();
        int totalEnfermeros = hospital.getEnfermeros().size();
        int totalPersonal = totalDoctores + totalEnfermeros;

        System.out.println(BOLD + " RESUMEN GENERAL:" + RESET);
        System.out.println("Total de personal médico: " + totalPersonal);
        System.out.println("Doctores: " + totalDoctores);
        System.out.println("Enfermeros: " + totalEnfermeros);
        System.out.println();

        // Estadísticas por especialidad
        if (!hospital.getDoctores().isEmpty()) {
            System.out.println(GREEN + " DOCTORES POR ESPECIALIDAD:" + RESET);
            Map<String, Long> porEspecialidad = hospital.getDoctores().stream()
                    .collect(Collectors.groupingBy(
                        d -> d.getEspecialidad() != null ? d.getEspecialidad().getNombreEspecialidad() : "General",
                        Collectors.counting()));

            porEspecialidad.forEach((especialidad, cantidad) -> 
                    System.out.println("• " + especialidad + ": " + cantidad + " doctor(es)"));
            System.out.println();
        }

        // Estadísticas por turno
        if (!hospital.getEnfermeros().isEmpty()) {
            System.out.println(BLUE + " ENFERMEROS POR TURNO:" + RESET);
            Map<String, Long> porTurno = hospital.getEnfermeros().stream()
                    .collect(Collectors.groupingBy(
                        Enfermero::getTurno,
                        Collectors.counting()));

            porTurno.forEach((turno, cantidad) -> 
                    System.out.println("• " + turno + ": " + cantidad + " enfermero(s)"));
        }

        pausar();
    }

    private static void gestionPacientes() {
        mostrarHeader("GESTIÓN DE PACIENTES");
        System.out.println("Funcionalidad en desarrollo...");
        pausar();
    }

    private static void gestionCitas() {
        mostrarHeader("GESTIÓN DE CITAS MÉDICAS");
        System.out.println("Funcionalidad en desarrollo...");
        pausar();
    }

    private static void gestionEmergencias() {
        mostrarHeader("GESTIÓN DE EMERGENCIAS");
        System.out.println("Funcionalidad en desarrollo...");
        pausar();
    }

    private static void gestionFarmacia() {
        mostrarHeader("GESTIÓN DE FARMACIA");
        System.out.println("Funcionalidad en desarrollo...");
        pausar();
    }

    private static void gestionLaboratorio() {
        mostrarHeader("GESTIÓN DE LABORATORIO");
        System.out.println("Funcionalidad en desarrollo...");
        pausar();
    }

    private static void gestionQuirofanos() {
        mostrarHeader("GESTIÓN DE QUIRÓFANOS");
        System.out.println("Funcionalidad en desarrollo...");
        pausar();
    }

    private static void monitoreoSignosVitales() {
        mostrarHeader("MONITOREO DE SIGNOS VITALES");
        System.out.println("Funcionalidad en desarrollo...");
        pausar();
    }

    private static void gestionSegurosMedicos() {
        mostrarHeader("GESTIÓN DE SEGUROS MÉDICOS");
        System.out.println("Funcionalidad en desarrollo...");
        pausar();
    }

    private static void reportesEstadisticas() {
        mostrarHeader("REPORTES Y ESTADÍSTICAS");
        hospital.mostrarDashboardEjecutivo();
        pausar();
    }

    private static void configuracionSistema() {
        mostrarHeader("CONFIGURACIÓN DEL SISTEMA");
        System.out.println("Configuración del sistema en desarrollo...");
        pausar();
    }

    private static void ayudaSoporte() {
        mostrarHeader("AYUDA Y SOPORTE");
        System.out.println("Sistema de Gestión Hospitalaria v2.0");
        System.out.println("Para soporte técnico, contacte al administrador del sistema.");
        pausar();
    }
}