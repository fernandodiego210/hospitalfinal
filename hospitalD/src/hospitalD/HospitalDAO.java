package hospitalD;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * CLASE DAO COMPLETA - ACCESO A DATOS DEL SISTEMA HOSPITALARIO
 * Versión: 2.0 Completa
 * Funcionalidades: CRUD completo para todas las entidades del hospital
 */
public class HospitalDAO {
    
    // ============= MÉTODOS DE CONEXIÓN Y UTILIDADES =============
    
    private Connection getConnection() {
        return ConexionBD.getConnection();
    }
    
    public boolean probarConexion() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Error al probar conexión: " + e.getMessage());
            return false;
        }
    }
    
    // ============= MÉTODOS PARA PERSONAS (CLASE BASE) =============
    
    public int insertarPersona(Persona persona) {
        String sql = "INSERT INTO Persona (nombre, apellido, dni, telefono, email, direccion, fecha_nacimiento) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, persona.getNombre());
            pstmt.setString(2, persona.getApellido());
            pstmt.setString(3, persona.getDni());
            pstmt.setString(4, persona.getTelefono());
            pstmt.setString(5, persona.getEmail());
            pstmt.setString(6, persona.getDireccion());
            pstmt.setDate(7, persona.getFechaNacimiento() != null ? 
                              Date.valueOf(persona.getFechaNacimiento()) : null);
            
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    persona.setIdPersona(idGenerado);
                    return idGenerado;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar persona: " + e.getMessage());
        }
        return -1;
    }
    
    // ============= MÉTODOS PARA ESPECIALIDADES =============
    
    public List<Especialidad> obtenerEspecialidades() {
        List<Especialidad> especialidades = new ArrayList<>();
        String sql = "SELECT id_especialidad, nombre_especialidad, descripcion FROM Especialidad";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Especialidad esp = new Especialidad(
                    rs.getInt("id_especialidad"),
                    rs.getString("nombre_especialidad"),
                    rs.getString("descripcion")
                );
                especialidades.add(esp);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener especialidades: " + e.getMessage());
        }
        return especialidades;
    }
    
    public Especialidad obtenerEspecialidadPorId(int idEspecialidad) {
        String sql = "SELECT id_especialidad, nombre_especialidad, descripcion " +
                    "FROM Especialidad WHERE id_especialidad = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idEspecialidad);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Especialidad(
                    rs.getInt("id_especialidad"),
                    rs.getString("nombre_especialidad"),
                    rs.getString("descripcion")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener especialidad: " + e.getMessage());
        }
        return null;
    }
    
    // ============= MÉTODOS PARA DOCTORES =============
    
    public boolean insertarDoctor(Doctor doctor) {
        int idPersona = insertarPersona(doctor);
        if (idPersona == -1) return false;
        
        String sql = "INSERT INTO Doctor (id_persona, numero_licencia, id_especialidad, fecha_ingreso, " +
                    "salario, horario_inicio, horario_fin, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, idPersona);
            pstmt.setString(2, doctor.getNumeroLicencia());
            pstmt.setInt(3, doctor.getEspecialidad() != null ? doctor.getEspecialidad().getIdEspecialidad() : null);
            pstmt.setDate(4, doctor.getFechaIngreso() != null ? Date.valueOf(doctor.getFechaIngreso()) : null);
            pstmt.setBigDecimal(5, doctor.getSalario());
            pstmt.setTime(6, doctor.getHorarioInicio() != null ? Time.valueOf(doctor.getHorarioInicio()) : null);
            pstmt.setTime(7, doctor.getHorarioFin() != null ? Time.valueOf(doctor.getHorarioFin()) : null);
            pstmt.setString(8, doctor.getEstado());
            
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    doctor.setIdDoctor(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar doctor: " + e.getMessage());
        }
        return false;
    }
    
    public List<Doctor> obtenerDoctores() {
        List<Doctor> doctores = new ArrayList<>();
        String sql = "SELECT d.id_doctor, d.numero_licencia, d.fecha_ingreso, d.salario, " +
                    "d.horario_inicio, d.horario_fin, d.estado, " +
                    "p.id_persona, p.nombre, p.apellido, p.dni, p.telefono, p.email, p.direccion, p.fecha_nacimiento, " +
                    "e.id_especialidad, e.nombre_especialidad, e.descripcion " +
                    "FROM Doctor d " +
                    "INNER JOIN Persona p ON d.id_persona = p.id_persona " +
                    "LEFT JOIN Especialidad e ON d.id_especialidad = e.id_especialidad";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Doctor doctor = crearDoctorDesdeResultSet(rs);
                doctores.add(doctor);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener doctores: " + e.getMessage());
        }
        return doctores;
    }
    
    public Doctor buscarDoctorPorLicencia(String numeroLicencia) {
        String sql = "SELECT d.id_doctor, d.numero_licencia, d.estado, " +
                    "p.nombre, p.apellido, p.dni, " +
                    "e.id_especialidad, e.nombre_especialidad " +
                    "FROM Doctor d " +
                    "INNER JOIN Persona p ON d.id_persona = p.id_persona " +
                    "LEFT JOIN Especialidad e ON d.id_especialidad = e.id_especialidad " +
                    "WHERE d.numero_licencia = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroLicencia);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return crearDoctorSimpleDesdeResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar doctor: " + e.getMessage());
        }
        return null;
    }
    
    // ============= MÉTODOS PARA PACIENTES =============
    
    public boolean insertarPaciente(Paciente paciente) {
        int idPersona = insertarPersona(paciente);
        if (idPersona == -1) return false;
        
        String sql = "INSERT INTO Paciente (id_persona, numero_historia, tipo_sangre, alergias, " +
                    "enfermedades_cronicas, contacto_emergencia, telefono_emergencia, seguro_medico) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, idPersona);
            pstmt.setString(2, paciente.getNumeroHistoria());
            pstmt.setString(3, paciente.getTipoSangre());
            pstmt.setString(4, paciente.getAlergias());
            pstmt.setString(5, paciente.getEnfermedadesCronicas());
            pstmt.setString(6, paciente.getContactoEmergencia());
            pstmt.setString(7, paciente.getTelefonoEmergencia());
            pstmt.setString(8, paciente.getSeguroMedico());
            
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    paciente.setIdPaciente(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar paciente: " + e.getMessage());
        }
        return false;
    }
    
    public List<Paciente> obtenerPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT pac.id_paciente, pac.numero_historia, pac.tipo_sangre, pac.alergias, " +
                    "pac.enfermedades_cronicas, pac.contacto_emergencia, pac.telefono_emergencia, pac.seguro_medico, " +
                    "p.id_persona, p.nombre, p.apellido, p.dni, p.telefono, p.email, p.direccion, p.fecha_nacimiento " +
                    "FROM Paciente pac " +
                    "INNER JOIN Persona p ON pac.id_persona = p.id_persona";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Paciente paciente = crearPacienteDesdeResultSet(rs);
                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener pacientes: " + e.getMessage());
        }
        return pacientes;
    }
    
    public Paciente buscarPacientePorHistoria(String numeroHistoria) {
        String sql = "SELECT pac.id_paciente, pac.numero_historia, pac.tipo_sangre, " +
                    "p.nombre, p.apellido, p.dni, p.telefono " +
                    "FROM Paciente pac " +
                    "INNER JOIN Persona p ON pac.id_persona = p.id_persona " +
                    "WHERE pac.numero_historia = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroHistoria);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return crearPacienteSimpleDesdeResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar paciente: " + e.getMessage());
        }
        return null;
    }
    
    // ============= MÉTODOS PARA CITAS =============
    
    public boolean insertarCita(Cita cita) {
        String sql = "INSERT INTO Cita (id_paciente, id_doctor, fecha_cita, motivo_consulta, " +
                    "estado_cita, observaciones, costo_consulta) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, cita.getPaciente().getIdPaciente());
            pstmt.setInt(2, cita.getDoctor().getIdDoctor());
            pstmt.setTimestamp(3, cita.getFechaCita() != null ? 
                                  Timestamp.valueOf(cita.getFechaCita()) : null);
            pstmt.setString(4, cita.getMotivoConsulta());
            pstmt.setString(5, cita.getEstadoCita());
            pstmt.setString(6, cita.getObservaciones());
            pstmt.setBigDecimal(7, cita.getCostoConsulta());
            
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    cita.setIdCita(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar cita: " + e.getMessage());
        }
        return false;
    }
    
    public List<Cita> obtenerCitas() {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.id_cita, c.fecha_cita, c.motivo_consulta, c.estado_cita, " +
                    "c.observaciones, c.costo_consulta, " +
                    "p.id_paciente, pp.nombre as paciente_nombre, pp.apellido as paciente_apellido, " +
                    "d.id_doctor, pd.nombre as doctor_nombre, pd.apellido as doctor_apellido " +
                    "FROM Cita c " +
                    "INNER JOIN Paciente p ON c.id_paciente = p.id_paciente " +
                    "INNER JOIN Persona pp ON p.id_persona = pp.id_persona " +
                    "INNER JOIN Doctor d ON c.id_doctor = d.id_doctor " +
                    "INNER JOIN Persona pd ON d.id_persona = pd.id_persona " +
                    "ORDER BY c.fecha_cita DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Cita cita = crearCitaDesdeResultSet(rs);
                citas.add(cita);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener citas: " + e.getMessage());
        }
        return citas;
    }
    
    // ============= MÉTODOS PARA HABITACIONES =============
    
    public List<Habitacion> obtenerHabitaciones() {
        List<Habitacion> habitaciones = new ArrayList<>();
        String sql = "SELECT h.id_habitacion, h.numero_habitacion, h.tipo_habitacion, " +
                    "h.capacidad, h.precio_dia, h.estado, " +
                    "d.id_departamento, d.nombre_departamento " +
                    "FROM Habitacion h " +
                    "LEFT JOIN Departamento d ON h.id_departamento = d.id_departamento";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Habitacion habitacion = crearHabitacionDesdeResultSet(rs);
                habitaciones.add(habitacion);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener habitaciones: " + e.getMessage());
        }
        return habitaciones;
    }
    
    // ============= MÉTODOS PARA SISTEMA DE FARMACIA =============
    
    public List<Proveedor> obtenerProveedores() {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT id_proveedor, nombre_proveedor, ruc, direccion, telefono, email, " +
                    "contacto_representante, estado FROM Proveedor";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setIdProveedor(rs.getInt("id_proveedor"));
                proveedor.setNombreProveedor(rs.getString("nombre_proveedor"));
                proveedor.setRuc(rs.getString("ruc"));
                proveedor.setDireccion(rs.getString("direccion"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setEmail(rs.getString("email"));
                proveedor.setContactoRepresentante(rs.getString("contacto_representante"));
                proveedor.setEstado(rs.getString("estado"));
                
                proveedores.add(proveedor);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener proveedores: " + e.getMessage());
        }
        return proveedores;
    }
    
    public List<Farmacia> obtenerFarmacias() {
        List<Farmacia> farmacias = new ArrayList<>();
        String sql = "SELECT id_farmacia, nombre_farmacia, ubicacion, telefono_interno, " +
                    "horario_inicio, horario_fin FROM Farmacia";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Farmacia farmacia = new Farmacia();
                farmacia.setIdFarmacia(rs.getInt("id_farmacia"));
                farmacia.setNombreFarmacia(rs.getString("nombre_farmacia"));
                farmacia.setUbicacion(rs.getString("ubicacion"));
                farmacia.setTelefonoInterno(rs.getString("telefono_interno"));
                
                Time horarioInicio = rs.getTime("horario_inicio");
                if (horarioInicio != null) {
                    farmacia.setHorarioInicio(horarioInicio.toLocalTime());
                }
                
                Time horarioFin = rs.getTime("horario_fin");
                if (horarioFin != null) {
                    farmacia.setHorarioFin(horarioFin.toLocalTime());
                }
                
                farmacias.add(farmacia);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener farmacias: " + e.getMessage());
        }
        return farmacias;
    }
    
    public List<MedicamentoAmpliado> obtenerMedicamentos() {
        List<MedicamentoAmpliado> medicamentos = new ArrayList<>();
        String sql = "SELECT m.id_medicamento, m.nombre_medicamento, m.principio_activo, " +
                    "m.presentacion, m.concentracion, m.stock_actual, m.stock_minimo, " +
                    "m.precio_unitario, m.fecha_vencimiento, m.lote, m.temperatura_almacenamiento, " +
                    "p.id_proveedor, p.nombre_proveedor " +
                    "FROM Medicamento m " +
                    "LEFT JOIN Proveedor p ON m.id_proveedor = p.id_proveedor";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                MedicamentoAmpliado medicamento = crearMedicamentoDesdeResultSet(rs);
                medicamentos.add(medicamento);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener medicamentos: " + e.getMessage());
        }
        return medicamentos;
    }
    
    // ============= MÉTODOS PARA SISTEMA DE LABORATORIO =============
    
    public List<Laboratorio> obtenerLaboratorios() {
        List<Laboratorio> laboratorios = new ArrayList<>();
        String sql = "SELECT id_laboratorio, nombre_laboratorio, ubicacion, telefono_interno, " +
                    "equipos_disponibles, horario_inicio, horario_fin FROM Laboratorio";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Laboratorio laboratorio = crearLaboratorioDesdeResultSet(rs);
                laboratorios.add(laboratorio);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener laboratorios: " + e.getMessage());
        }
        return laboratorios;
    }
    
    public List<TipoExamen> obtenerTiposExamen() {
        List<TipoExamen> tiposExamen = new ArrayList<>();
        String sql = "SELECT te.id_tipo_examen, te.nombre_examen, te.descripcion, te.precio, " +
                    "te.tiempo_resultado_horas, te.requiere_ayuno, " +
                    "l.id_laboratorio, l.nombre_laboratorio " +
                    "FROM TipoExamen te " +
                    "LEFT JOIN Laboratorio l ON te.id_laboratorio = l.id_laboratorio";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                TipoExamen tipoExamen = crearTipoExamenDesdeResultSet(rs);
                tiposExamen.add(tipoExamen);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener tipos de examen: " + e.getMessage());
        }
        return tiposExamen;
    }
    
    // ============= MÉTODOS PARA SISTEMA DE EMERGENCIAS =============
    
    public List<Emergencia> obtenerEmergenciasActivas() {
        List<Emergencia> emergencias = new ArrayList<>();
        String sql = "SELECT e.id_emergencia, e.fecha_ingreso, e.sintomas_principales, " +
                    "e.nivel_prioridad, e.estado_emergencia, e.tiempo_espera_minutos, " +
                    "p.id_paciente, pe.nombre, pe.apellido " +
                    "FROM Emergencia e " +
                    "INNER JOIN Paciente p ON e.id_paciente = p.id_paciente " +
                    "INNER JOIN Persona pe ON p.id_persona = pe.id_persona " +
                    "WHERE e.estado_emergencia IN ('EN_ATENCION', 'ESTABLE') " +
                    "ORDER BY e.fecha_ingreso DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Emergencia emergencia = crearEmergenciaDesdeResultSet(rs);
                emergencias.add(emergencia);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener emergencias: " + e.getMessage());
        }
        return emergencias;
    }
    
    public List<Ambulancia> obtenerAmbulancias() {
        List<Ambulancia> ambulancias = new ArrayList<>();
        String sql = "SELECT id_ambulancia, placa, modelo, tipo_ambulancia, " +
                    "capacidad_pacientes, estado, ubicacion_actual FROM Ambulancia";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Ambulancia ambulancia = new Ambulancia();
                ambulancia.setIdAmbulancia(rs.getInt("id_ambulancia"));
                ambulancia.setPlaca(rs.getString("placa"));
                ambulancia.setModelo(rs.getString("modelo"));
                ambulancia.setTipoAmbulancia(rs.getString("tipo_ambulancia"));
                ambulancia.setCapacidadPacientes(rs.getInt("capacidad_pacientes"));
                ambulancia.setEstado(rs.getString("estado"));
                ambulancia.setUbicacionActual(rs.getString("ubicacion_actual"));
                
                ambulancias.add(ambulancia);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ambulancias: " + e.getMessage());
        }
        return ambulancias;
    }
    
    // ============= MÉTODOS PARA SEGUROS MÉDICOS =============
    
    public List<SeguroMedico> obtenerSegurosMedicos() {
        List<SeguroMedico> seguros = new ArrayList<>();
        String sql = "SELECT id_seguro, nombre_seguro, tipo_seguro, cobertura_porcentaje, " +
                    "telefono_seguro, email_seguro, estado FROM SeguroMedico";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                SeguroMedico seguro = new SeguroMedico();
                seguro.setIdSeguro(rs.getInt("id_seguro"));
                seguro.setNombreSeguro(rs.getString("nombre_seguro"));
                seguro.setTipoSeguro(rs.getString("tipo_seguro"));
                seguro.setCoberturaPorcentaje(rs.getBigDecimal("cobertura_porcentaje"));
                seguro.setTelefonoSeguro(rs.getString("telefono_seguro"));
                seguro.setEmailSeguro(rs.getString("email_seguro"));
                seguro.setEstado(rs.getString("estado"));
                
                // Configurar coberturas automáticamente
                seguro.configurarCoberturasPorTipo();
                
                seguros.add(seguro);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener seguros médicos: " + e.getMessage());
        }
        return seguros;
    }
    
    // ============= MÉTODOS DE INSERCIÓN AVANZADOS =============
    
    public boolean insertarEmergencia(Emergencia emergencia) {
        String sql = "INSERT INTO Emergencia (id_paciente, sintomas_principales, nivel_prioridad, " +
                    "estado_emergencia, tiempo_espera_minutos, observaciones) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, emergencia.getPaciente().getIdPaciente());
            pstmt.setString(2, emergencia.getSintomasPrincipales());
            pstmt.setString(3, emergencia.getNivelPrioridad());
            pstmt.setString(4, emergencia.getEstadoEmergencia());
            pstmt.setInt(5, emergencia.getTiempoEsperaMinutos());
            pstmt.setString(6, emergencia.getObservaciones());
            
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    emergencia.setIdEmergencia(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar emergencia: " + e.getMessage());
        }
        return false;
    }
    
    public boolean insertarSignosVitales(SignosVitales signos) {
        String sql = "INSERT INTO SignosVitales (id_paciente, id_enfermero, presion_sistolica, " +
                    "presion_diastolica, frecuencia_cardiaca, frecuencia_respiratoria, temperatura, " +
                    "saturacion_oxigeno, peso, altura, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, signos.getPaciente().getIdPaciente());
            pstmt.setInt(2, signos.getEnfermero().getIdEnfermero());
            pstmt.setObject(3, signos.getPresionSistolica());
            pstmt.setObject(4, signos.getPresionDiastolica());
            pstmt.setObject(5, signos.getFrecuenciaCardiaca());
            pstmt.setObject(6, signos.getFrecuenciaRespiratoria());
            pstmt.setBigDecimal(7, signos.getTemperatura());
            pstmt.setObject(8, signos.getSaturacionOxigeno());
            pstmt.setBigDecimal(9, signos.getPeso());
            pstmt.setBigDecimal(10, signos.getAltura());
            pstmt.setString(11, signos.getObservaciones());
            
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    signos.setIdSignosVitales(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar signos vitales: " + e.getMessage());
        }
        return false;
    }
    
    // ============= MÉTODOS DE ESTADÍSTICAS RÁPIDAS =============
    
    public int contarEmergenciasActivas() {
        return contarRegistros("SELECT COUNT(*) FROM Emergencia WHERE estado_emergencia IN ('EN_ATENCION', 'ESTABLE')");
    }
    
    public int contarEmergenciasCriticas() {
        return contarRegistros("SELECT COUNT(*) FROM Emergencia WHERE nivel_prioridad = 'CRITICO' AND estado_emergencia = 'EN_ATENCION'");
    }
    
    public int contarAmbulanciasDisponibles() {
        return contarRegistros("SELECT COUNT(*) FROM Ambulancia WHERE estado = 'DISPONIBLE'");
    }
    
    public int contarHabitacionesDisponibles() {
        return contarRegistros("SELECT COUNT(*) FROM Habitacion WHERE estado = 'DISPONIBLE'");
    }
    
    public int contarCitasDeHoy() {
        return contarRegistros("SELECT COUNT(*) FROM Cita WHERE CAST(fecha_cita AS DATE) = CAST(GETDATE() AS DATE) AND estado_cita = 'PROGRAMADA'");
    }
    
    public int contarMedicamentosStockBajo() {
        return contarRegistros("SELECT COUNT(*) FROM Medicamento WHERE stock_actual <= stock_minimo");
    }
    
    public int contarMedicamentosVencidos() {
        return contarRegistros("SELECT COUNT(*) FROM Medicamento WHERE fecha_vencimiento < GETDATE()");
    }
    
    public int contarLaboratoriosOperativos() {
        return contarRegistros("SELECT COUNT(*) FROM Laboratorio WHERE CAST(GETDATE() AS TIME) BETWEEN horario_inicio AND horario_fin");
    }
    
    public int contarOrdenesExamenPendientes() {
        return contarRegistros("SELECT COUNT(*) FROM OrdenExamen WHERE estado_orden IN ('PENDIENTE', 'PROCESANDO')");
    }
    
    // ============= MÉTODOS DE ACTUALIZACIÓN =============
    
    public boolean actualizarEstadoCita(int idCita, String nuevoEstado, String observaciones) {
        String sql = "UPDATE Cita SET estado_cita = ?, observaciones = ? WHERE id_cita = ?";
        return ejecutarActualizacion(sql, nuevoEstado, observaciones, idCita);
    }
    
    public boolean actualizarEstadoHabitacion(int idHabitacion, String nuevoEstado) {
        String sql = "UPDATE Habitacion SET estado = ? WHERE id_habitacion = ?";
        return ejecutarActualizacion(sql, nuevoEstado, idHabitacion);
    }
    
    public boolean actualizarEstadoEmergencia(int idEmergencia, String nuevoEstado, String observaciones) {
        String sql = "UPDATE Emergencia SET estado_emergencia = ?, observaciones = ? WHERE id_emergencia = ?";
        return ejecutarActualizacion(sql, nuevoEstado, observaciones, idEmergencia);
    }
    
    public boolean actualizarStockMedicamento(int idMedicamento, int nuevoStock) {
        String sql = "UPDATE Medicamento SET stock_actual = ? WHERE id_medicamento = ?";
        return ejecutarActualizacion(sql, nuevoStock, idMedicamento);
    }
    
    // ============= MÉTODOS DE REPORTE Y ANÁLISIS =============
    
    public void generarReporteBaseDatos() {
        System.out.println("📊 === REPORTE INTEGRAL DE BASE DE DATOS ===");
        
        try (Connection conn = getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            System.out.println("🔗 Conexión: " + metaData.getURL());
            System.out.println("👤 Usuario: " + metaData.getUserName());
            System.out.println("🖥️ Driver: " + metaData.getDriverName() + " v" + metaData.getDriverVersion());
            
            System.out.println("\n📈 ESTADÍSTICAS POR TABLA:");
            String[] tablas = {
                "Persona", "Doctor", "Paciente", "Enfermero", "Especialidad", 
                "Departamento", "Habitacion", "Cita", "Proveedor", "Farmacia", 
                "Medicamento", "Laboratorio", "TipoExamen", "OrdenExamen",
                "Emergencia", "Ambulancia", "Cirugia", "SignosVitales", "SeguroMedico"
            };
            
            int totalRegistros = 0;
            for (String tabla : tablas) {
                int count = contarRegistrosTabla(tabla);
                totalRegistros += count;
                String status = count > 0 ? "✅" : "⭕";
                System.out.println(String.format("   %s %-20s: %,6d registros", status, tabla, count));
            }
            
            System.out.println("\n📊 RESUMEN GLOBAL:");
            System.out.println("   📋 Total de tablas: " + tablas.length);
            System.out.println("   📄 Total de registros: " + String.format("%,d", totalRegistros));
            System.out.println("   💾 Estado BD: " + (totalRegistros > 0 ? "ACTIVA ✅" : "VACÍA ⚠️"));
            
        } catch (SQLException e) {
            System.err.println("❌ Error al generar reporte: " + e.getMessage());
        }
    }
    
    public boolean verificarIntegridadDatos() {
        System.out.println("🔍 === VERIFICACIÓN DE INTEGRIDAD ===");
        boolean integridadOK = true;
        
        try {
            // Verificar doctores sin especialidad
            int doctoresSinEspec = contarRegistros(
                "SELECT COUNT(*) FROM Doctor d LEFT JOIN Especialidad e ON d.id_especialidad = e.id_especialidad WHERE e.id_especialidad IS NULL");
            
            if (doctoresSinEspec > 0) {
                System.out.println("⚠️ " + doctoresSinEspec + " doctores sin especialidad asignada");
                integridadOK = false;
            }
            
            // Verificar pacientes sin historia
            int pacientesSinHistoria = contarRegistros(
                "SELECT COUNT(*) FROM Paciente WHERE numero_historia IS NULL OR numero_historia = ''");
            
            if (pacientesSinHistoria > 0) {
                System.out.println("⚠️ " + pacientesSinHistoria + " pacientes sin número de historia");
                integridadOK = false;
            }
            
            // Verificar citas futuras con doctores inactivos
            int citasDocInactivos = contarRegistros(
                "SELECT COUNT(*) FROM Cita c INNER JOIN Doctor d ON c.id_doctor = d.id_doctor " +
                "WHERE c.fecha_cita > GETDATE() AND d.estado != 'ACTIVO'");
            
            if (citasDocInactivos > 0) {
                System.out.println("⚠️ " + citasDocInactivos + " citas futuras con doctores inactivos");
                integridadOK = false;
            }
            
            if (integridadOK) {
                System.out.println("✅ Integridad de datos verificada correctamente");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error en verificación: " + e.getMessage());
            integridadOK = false;
        }
        
        return integridadOK;
    }
    
    // ============= MÉTODOS AUXILIARES PRIVADOS =============
    
    private int contarRegistros(String sql) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            // Silencioso para conteos que pueden fallar
        }
        return 0;
    }
    
    private int contarRegistrosTabla(String tabla) {
        return contarRegistros("SELECT COUNT(*) FROM " + tabla);
    }
    
    private boolean ejecutarActualizacion(String sql, Object... parametros) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < parametros.length; i++) {
                pstmt.setObject(i + 1, parametros[i]);
            }
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error en actualización: " + e.getMessage());
            return false;
        }
    }
    
    // ============= MÉTODOS PARA CREAR OBJETOS DESDE RESULTSET =============
    
    private Doctor crearDoctorDesdeResultSet(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        
        // Datos de Persona
        doctor.setIdPersona(rs.getInt("id_persona"));
        doctor.setNombre(rs.getString("nombre"));
        doctor.setApellido(rs.getString("apellido"));
        doctor.setDni(rs.getString("dni"));
        doctor.setTelefono(rs.getString("telefono"));
        doctor.setEmail(rs.getString("email"));
        doctor.setDireccion(rs.getString("direccion"));
        
        Date fechaNac = rs.getDate("fecha_nacimiento");
        if (fechaNac != null) {
            doctor.setFechaNacimiento(fechaNac.toLocalDate());
        }
        
        // Datos de Doctor
        doctor.setIdDoctor(rs.getInt("id_doctor"));
        doctor.setNumeroLicencia(rs.getString("numero_licencia"));
        
        Date fechaIng = rs.getDate("fecha_ingreso");
        if (fechaIng != null) {
            doctor.setFechaIngreso(fechaIng.toLocalDate());
        }
        
        doctor.setSalario(rs.getBigDecimal("salario"));
        
        Time horarioIni = rs.getTime("horario_inicio");
        if (horarioIni != null) {
            doctor.setHorarioInicio(horarioIni.toLocalTime());
        }
        
        Time horarioFin = rs.getTime("horario_fin");
        if (horarioFin != null) {
            doctor.setHorarioFin(horarioFin.toLocalTime());
        }
        
        doctor.setEstado(rs.getString("estado"));
        
        // Especialidad
        int idEsp = rs.getInt("id_especialidad");
        if (idEsp > 0) {
            Especialidad esp = new Especialidad(
                idEsp,
                rs.getString("nombre_especialidad"),
                rs.getString("descripcion")
            );
            doctor.setEspecialidad(esp);
        }
        
        return doctor;
    }
    
    private Doctor crearDoctorSimpleDesdeResultSet(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setIdDoctor(rs.getInt("id_doctor"));
        doctor.setNumeroLicencia(rs.getString("numero_licencia"));
        doctor.setNombre(rs.getString("nombre"));
        doctor.setApellido(rs.getString("apellido"));
        doctor.setDni(rs.getString("dni"));
        doctor.setEstado(rs.getString("estado"));
        
        // Especialidad simple
        String nombreEsp = rs.getString("nombre_especialidad");
        if (nombreEsp != null) {
            Especialidad esp = new Especialidad();
            esp.setIdEspecialidad(rs.getInt("id_especialidad"));
            esp.setNombreEspecialidad(nombreEsp);
            doctor.setEspecialidad(esp);
        }
        
        return doctor;
    }
    
    private Paciente crearPacienteDesdeResultSet(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();
        
        // Datos de Persona
        paciente.setIdPersona(rs.getInt("id_persona"));
        paciente.setNombre(rs.getString("nombre"));
        paciente.setApellido(rs.getString("apellido"));
        paciente.setDni(rs.getString("dni"));
        paciente.setTelefono(rs.getString("telefono"));
        paciente.setEmail(rs.getString("email"));
        paciente.setDireccion(rs.getString("direccion"));
        
        Date fechaNac = rs.getDate("fecha_nacimiento");
        if (fechaNac != null) {
            paciente.setFechaNacimiento(fechaNac.toLocalDate());
        }
        
        // Datos de Paciente
        paciente.setIdPaciente(rs.getInt("id_paciente"));
        paciente.setNumeroHistoria(rs.getString("numero_historia"));
        paciente.setTipoSangre(rs.getString("tipo_sangre"));
        paciente.setAlergias(rs.getString("alergias"));
        paciente.setEnfermedadesCronicas(rs.getString("enfermedades_cronicas"));
        paciente.setContactoEmergencia(rs.getString("contacto_emergencia"));
        paciente.setTelefonoEmergencia(rs.getString("telefono_emergencia"));
        paciente.setSeguroMedico(rs.getString("seguro_medico"));
        
        return paciente;
    }
    
    private Paciente crearPacienteSimpleDesdeResultSet(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(rs.getInt("id_paciente"));
        paciente.setNumeroHistoria(rs.getString("numero_historia"));
        paciente.setTipoSangre(rs.getString("tipo_sangre"));
        paciente.setNombre(rs.getString("nombre"));
        paciente.setApellido(rs.getString("apellido"));
        paciente.setDni(rs.getString("dni"));
        paciente.setTelefono(rs.getString("telefono"));
        
        return paciente;
    }
    
    private Cita crearCitaDesdeResultSet(ResultSet rs) throws SQLException {
        Cita cita = new Cita();
        cita.setIdCita(rs.getInt("id_cita"));
        
        Timestamp fechaCita = rs.getTimestamp("fecha_cita");
        if (fechaCita != null) {
            cita.setFechaCita(fechaCita.toLocalDateTime());
        }
        
        cita.setMotivoConsulta(rs.getString("motivo_consulta"));
        cita.setEstadoCita(rs.getString("estado_cita"));
        cita.setObservaciones(rs.getString("observaciones"));
        cita.setCostoConsulta(rs.getBigDecimal("costo_consulta"));
        
        // Paciente simple
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(rs.getInt("id_paciente"));
        paciente.setNombre(rs.getString("paciente_nombre"));
        paciente.setApellido(rs.getString("paciente_apellido"));
        cita.setPaciente(paciente);
        
        // Doctor simple
        Doctor doctor = new Doctor();
        doctor.setIdDoctor(rs.getInt("id_doctor"));
        doctor.setNombre(rs.getString("doctor_nombre"));
        doctor.setApellido(rs.getString("doctor_apellido"));
        cita.setDoctor(doctor);
        
        return cita;
    }
    
    private Habitacion crearHabitacionDesdeResultSet(ResultSet rs) throws SQLException {
        Habitacion habitacion = new Habitacion();
        habitacion.setIdHabitacion(rs.getInt("id_habitacion"));
        habitacion.setNumeroHabitacion(rs.getString("numero_habitacion"));
        habitacion.setTipoHabitacion(rs.getString("tipo_habitacion"));
        habitacion.setCapacidad(rs.getInt("capacidad"));
        habitacion.setPrecioDia(rs.getBigDecimal("precio_dia"));
        habitacion.setEstado(rs.getString("estado"));
        
        // Departamento simple
        int idDept = rs.getInt("id_departamento");
        if (idDept > 0) {
            Departamento dept = new Departamento();
            dept.setIdDepartamento(idDept);
            dept.setNombreDepartamento(rs.getString("nombre_departamento"));
            habitacion.setDepartamento(dept);
        }
        
        return habitacion;
    }
    
    private MedicamentoAmpliado crearMedicamentoDesdeResultSet(ResultSet rs) throws SQLException {
        MedicamentoAmpliado medicamento = new MedicamentoAmpliado();
        medicamento.setIdMedicamento(rs.getInt("id_medicamento"));
        medicamento.setNombreMedicamento(rs.getString("nombre_medicamento"));
        medicamento.setPrincipioActivo(rs.getString("principio_activo"));
        medicamento.setPresentacion(rs.getString("presentacion"));
        medicamento.setConcentracion(rs.getString("concentracion"));
        medicamento.setStockActual(rs.getInt("stock_actual"));
        medicamento.setStockMinimo(rs.getInt("stock_minimo"));
        medicamento.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        medicamento.setLote(rs.getString("lote"));
        medicamento.setTemperaturaAlmacenamiento(rs.getString("temperatura_almacenamiento"));
        
        Date fechaVenc = rs.getDate("fecha_vencimiento");
        if (fechaVenc != null) {
            medicamento.setFechaVencimiento(fechaVenc.toLocalDate());
        }
        
        // Proveedor simple
        int idProveedor = rs.getInt("id_proveedor");
        if (idProveedor > 0) {
            Proveedor proveedor = new Proveedor();
            proveedor.setIdProveedor(idProveedor);
            proveedor.setNombreProveedor(rs.getString("nombre_proveedor"));
            medicamento.setProveedor(proveedor);
        }
        
        return medicamento;
    }
    
    private Laboratorio crearLaboratorioDesdeResultSet(ResultSet rs) throws SQLException {
        Laboratorio laboratorio = new Laboratorio();
        laboratorio.setIdLaboratorio(rs.getInt("id_laboratorio"));
        laboratorio.setNombreLaboratorio(rs.getString("nombre_laboratorio"));
        laboratorio.setUbicacion(rs.getString("ubicacion"));
        laboratorio.setTelefonoInterno(rs.getString("telefono_interno"));
        laboratorio.setEquiposDisponibles(rs.getString("equipos_disponibles"));
        
        Time horarioInicio = rs.getTime("horario_inicio");
        if (horarioInicio != null) {
            laboratorio.setHorarioInicio(horarioInicio.toLocalTime());
        }
        
        Time horarioFin = rs.getTime("horario_fin");
        if (horarioFin != null) {
            laboratorio.setHorarioFin(horarioFin.toLocalTime());
        }
        
        return laboratorio;
    }
    
    private TipoExamen crearTipoExamenDesdeResultSet(ResultSet rs) throws SQLException {
        TipoExamen tipoExamen = new TipoExamen();
        tipoExamen.setIdTipoExamen(rs.getInt("id_tipo_examen"));
        tipoExamen.setNombreExamen(rs.getString("nombre_examen"));
        tipoExamen.setDescripcion(rs.getString("descripcion"));
        tipoExamen.setPrecio(rs.getBigDecimal("precio"));
        tipoExamen.setTiempoResultadoHoras(rs.getInt("tiempo_resultado_horas"));
        tipoExamen.setRequiereAyuno(rs.getBoolean("requiere_ayuno"));
        
        // Laboratorio simple
        int idLab = rs.getInt("id_laboratorio");
        if (idLab > 0) {
            Laboratorio lab = new Laboratorio();
            lab.setIdLaboratorio(idLab);
            lab.setNombreLaboratorio(rs.getString("nombre_laboratorio"));
            tipoExamen.setLaboratorio(lab);
        }
        
        return tipoExamen;
    }
    
    private Emergencia crearEmergenciaDesdeResultSet(ResultSet rs) throws SQLException {
        Emergencia emergencia = new Emergencia();
        emergencia.setIdEmergencia(rs.getInt("id_emergencia"));
        emergencia.setSintomasPrincipales(rs.getString("sintomas_principales"));
        emergencia.setNivelPrioridad(rs.getString("nivel_prioridad"));
        emergencia.setEstadoEmergencia(rs.getString("estado_emergencia"));
        emergencia.setTiempoEsperaMinutos(rs.getInt("tiempo_espera_minutos"));
        
        Timestamp fechaIngreso = rs.getTimestamp("fecha_ingreso");
        if (fechaIngreso != null) {
            emergencia.setFechaIngreso(fechaIngreso.toLocalDateTime());
        }
        
        // Paciente simple
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(rs.getInt("id_paciente"));
        paciente.setNombre(rs.getString("nombre"));
        paciente.setApellido(rs.getString("apellido"));
        emergencia.setPaciente(paciente);
        
        return emergencia;
    }
}