package hospitalD;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Clase DAO (Data Access Object) para manejar operaciones de base de datos
public class HospitalDAO {
    
    // ============= MÉTODOS PARA PERSONA =============
    
    public int insertarPersona(Persona persona) {
        String sql = "INSERT INTO Persona (nombre, apellido, dni, telefono, email, direccion, fecha_nacimiento) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
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
            e.printStackTrace();
        }
        return -1;
    }
    
    // ============= MÉTODOS PARA ESPECIALIDAD =============
    
    public List<Especialidad> obtenerEspecialidades() {
        List<Especialidad> especialidades = new ArrayList<>();
        String sql = "SELECT id_especialidad, nombre_especialidad, descripcion FROM Especialidad";
        
        try (Connection conn = ConexionBD.getConnection();
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
        
        try (Connection conn = ConexionBD.getConnection();
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
    
    // ============= MÉTODOS PARA DOCTOR =============
    
    public boolean insertarDoctor(Doctor doctor) {
        // Primero insertar en la tabla Persona
        int idPersona = insertarPersona(doctor);
        if (idPersona == -1) return false;
        
        // Luego insertar en la tabla Doctor
        String sql = "INSERT INTO Doctor (id_persona, numero_licencia, id_especialidad, fecha_ingreso, " +
                    "salario, horario_inicio, horario_fin, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
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
            e.printStackTrace();
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
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
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
                
                doctores.add(doctor);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener doctores: " + e.getMessage());
        }
        return doctores;
    }
    
    // ============= MÉTODOS PARA PACIENTE =============
    
    public boolean insertarPaciente(Paciente paciente) {
        // Primero insertar en la tabla Persona
        int idPersona = insertarPersona(paciente);
        if (idPersona == -1) return false;
        
        // Luego insertar en la tabla Paciente
        String sql = "INSERT INTO Paciente (id_persona, numero_historia, tipo_sangre, alergias, " +
                    "enfermedades_cronicas, contacto_emergencia, telefono_emergencia, seguro_medico) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
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
            e.printStackTrace();
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
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
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
                
                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener pacientes: " + e.getMessage());
        }
        return pacientes;
    }
    
    // ============= MÉTODOS PARA CITA =============
    
    public boolean insertarCita(Cita cita) {
        String sql = "INSERT INTO Cita (id_paciente, id_doctor, fecha_cita, motivo_consulta, " +
                    "estado_cita, observaciones, costo_consulta) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
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
            e.printStackTrace();
        }
        return false;
    }
    
    // Método para obtener citas con información completa
    public List<Cita> obtenerCitas() {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.id_cita, c.fecha_cita, c.motivo_consulta, c.estado_cita, " +
                    "c.observaciones, c.costo_consulta, c.fecha_creacion, " +
                    // Datos del paciente
                    "p.id_paciente, p.numero_historia, pp.nombre as paciente_nombre, pp.apellido as paciente_apellido, " +
                    // Datos del doctor  
                    "d.id_doctor, d.numero_licencia, pd.nombre as doctor_nombre, pd.apellido as doctor_apellido, " +
                    "e.nombre_especialidad " +
                    "FROM Cita c " +
                    "INNER JOIN Paciente p ON c.id_paciente = p.id_paciente " +
                    "INNER JOIN Persona pp ON p.id_persona = pp.id_persona " +
                    "INNER JOIN Doctor d ON c.id_doctor = d.id_doctor " +
                    "INNER JOIN Persona pd ON d.id_persona = pd.id_persona " +
                    "LEFT JOIN Especialidad e ON d.id_especialidad = e.id_especialidad " +
                    "ORDER BY c.fecha_cita DESC";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
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
                
                Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
                if (fechaCreacion != null) {
                    cita.setFechaCreacion(fechaCreacion.toLocalDateTime());
                }
                
                // Crear objetos Paciente y Doctor simplificados
                Paciente paciente = new Paciente();
                paciente.setIdPaciente(rs.getInt("id_paciente"));
                paciente.setNumeroHistoria(rs.getString("numero_historia"));
                paciente.setNombre(rs.getString("paciente_nombre"));
                paciente.setApellido(rs.getString("paciente_apellido"));
                cita.setPaciente(paciente);
                
                Doctor doctor = new Doctor();
                doctor.setIdDoctor(rs.getInt("id_doctor"));
                doctor.setNumeroLicencia(rs.getString("numero_licencia"));
                doctor.setNombre(rs.getString("doctor_nombre"));
                doctor.setApellido(rs.getString("doctor_apellido"));
                
                // Especialidad del doctor
                String nombreEsp = rs.getString("nombre_especialidad");
                if (nombreEsp != null) {
                    Especialidad esp = new Especialidad();
                    esp.setNombreEspecialidad(nombreEsp);
                    doctor.setEspecialidad(esp);
                }
                cita.setDoctor(doctor);
                
                citas.add(cita);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener citas: " + e.getMessage());
        }
        return citas;
    }
    
    // Método para actualizar el estado de una cita
    public boolean actualizarEstadoCita(int idCita, String nuevoEstado, String observaciones) {
        String sql = "UPDATE Cita SET estado_cita = ?, observaciones = ? WHERE id_cita = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nuevoEstado);
            pstmt.setString(2, observaciones);
            pstmt.setInt(3, idCita);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar cita: " + e.getMessage());
            return false;
        }
    }
    
    // ============= MÉTODOS PARA HABITACIÓN =============
    
    public List<Habitacion> obtenerHabitaciones() {
        List<Habitacion> habitaciones = new ArrayList<>();
        String sql = "SELECT h.id_habitacion, h.numero_habitacion, h.tipo_habitacion, " +
                    "h.capacidad, h.precio_dia, h.estado, " +
                    "d.id_departamento, d.nombre_departamento " +
                    "FROM Habitacion h " +
                    "LEFT JOIN Departamento d ON h.id_departamento = d.id_departamento";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Habitacion habitacion = new Habitacion();
                habitacion.setIdHabitacion(rs.getInt("id_habitacion"));
                habitacion.setNumeroHabitacion(rs.getString("numero_habitacion"));
                habitacion.setTipoHabitacion(rs.getString("tipo_habitacion"));
                habitacion.setCapacidad(rs.getInt("capacidad"));
                habitacion.setPrecioDia(rs.getBigDecimal("precio_dia"));
                habitacion.setEstado(rs.getString("estado"));
                
                // Departamento simplificado
                int idDept = rs.getInt("id_departamento");
                if (idDept > 0) {
                    Departamento dept = new Departamento();
                    dept.setIdDepartamento(idDept);
                    dept.setNombreDepartamento(rs.getString("nombre_departamento"));
                    habitacion.setDepartamento(dept);
                }
                
                habitaciones.add(habitacion);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener habitaciones: " + e.getMessage());
        }
        return habitaciones;
    }
    
    public boolean actualizarEstadoHabitacion(int idHabitacion, String nuevoEstado) {
        String sql = "UPDATE Habitacion SET estado = ? WHERE id_habitacion = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idHabitacion);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de habitación: " + e.getMessage());
            return false;
        }
    }
    
    // ============= MÉTODOS PARA DEPARTAMENTO =============
    
    public List<Departamento> obtenerDepartamentos() {
        List<Departamento> departamentos = new ArrayList<>();
        String sql = "SELECT d.id_departamento, d.nombre_departamento, d.ubicacion, " +
                    "d.telefono_interno, d.presupuesto, " +
                    "doc.id_doctor, p.nombre as jefe_nombre, p.apellido as jefe_apellido " +
                    "FROM Departamento d " +
                    "LEFT JOIN Doctor doc ON d.jefe_departamento = doc.id_doctor " +
                    "LEFT JOIN Persona p ON doc.id_persona = p.id_persona";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Departamento dept = new Departamento();
                dept.setIdDepartamento(rs.getInt("id_departamento"));
                dept.setNombreDepartamento(rs.getString("nombre_departamento"));
                dept.setUbicacion(rs.getString("ubicacion"));
                dept.setTelefonoInterno(rs.getString("telefono_interno"));
                dept.setPresupuesto(rs.getBigDecimal("presupuesto"));
                
                // Jefe de departamento simplificado
                int idJefe = rs.getInt("id_doctor");
                if (idJefe > 0) {
                    Doctor jefe = new Doctor();
                    jefe.setIdDoctor(idJefe);
                    jefe.setNombre(rs.getString("jefe_nombre"));
                    jefe.setApellido(rs.getString("jefe_apellido"));
                    dept.setJefeDepartamento(jefe);
                }
                
                departamentos.add(dept);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener departamentos: " + e.getMessage());
        }
        return departamentos;
    }
    
    // ============= MÉTODOS DE BÚSQUEDA ESPECÍFICOS =============
    
    public Doctor buscarDoctorPorLicencia(String numeroLicencia) {
        String sql = "SELECT d.id_doctor, d.numero_licencia, d.estado, " +
                    "p.nombre, p.apellido, p.dni, " +
                    "e.id_especialidad, e.nombre_especialidad " +
                    "FROM Doctor d " +
                    "INNER JOIN Persona p ON d.id_persona = p.id_persona " +
                    "LEFT JOIN Especialidad e ON d.id_especialidad = e.id_especialidad " +
                    "WHERE d.numero_licencia = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroLicencia);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setIdDoctor(rs.getInt("id_doctor"));
                doctor.setNumeroLicencia(rs.getString("numero_licencia"));
                doctor.setNombre(rs.getString("nombre"));
                doctor.setApellido(rs.getString("apellido"));
                doctor.setDni(rs.getString("dni"));
                doctor.setEstado(rs.getString("estado"));
                
                int idEsp = rs.getInt("id_especialidad");
                if (idEsp > 0) {
                    Especialidad esp = new Especialidad();
                    esp.setIdEspecialidad(idEsp);
                    esp.setNombreEspecialidad(rs.getString("nombre_especialidad"));
                    doctor.setEspecialidad(esp);
                }
                
                return doctor;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar doctor por licencia: " + e.getMessage());
        }
        return null;
    }
    
    public Paciente buscarPacientePorHistoria(String numeroHistoria) {
        String sql = "SELECT pac.id_paciente, pac.numero_historia, pac.tipo_sangre, " +
                    "p.nombre, p.apellido, p.dni, p.telefono " +
                    "FROM Paciente pac " +
                    "INNER JOIN Persona p ON pac.id_persona = p.id_persona " +
                    "WHERE pac.numero_historia = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroHistoria);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
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
        } catch (SQLException e) {
            System.err.println("Error al buscar paciente por historia: " + e.getMessage());
        }
        return null;
    }
    
    // ============= MÉTODOS DE ESTADÍSTICAS =============
    
    public int contarDoctoresPorEspecialidad(int idEspecialidad) {
        String sql = "SELECT COUNT(*) as total FROM Doctor WHERE id_especialidad = ? AND estado = 'ACTIVO'";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idEspecialidad);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar doctores por especialidad: " + e.getMessage());
        }
        return 0;
    }
    
    public int contarHabitacionesPorEstado(String estado) {
        String sql = "SELECT COUNT(*) as total FROM Habitacion WHERE estado = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, estado);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar habitaciones por estado: " + e.getMessage());
        }
        return 0;
    }
    
    public int contarCitasDeHoy() {
        String sql = "SELECT COUNT(*) as total FROM Cita WHERE " +
                    "CAST(fecha_cita AS DATE) = CAST(GETDATE() AS DATE) AND estado_cita = 'PROGRAMADA'";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar citas de hoy: " + e.getMessage());
        }
        return 0;
    }
    
    // ============= MÉTODO PARA PROBAR CONEXIÓN =============
    
    public boolean probarConexion() {
        try (Connection conn = ConexionBD.getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Error al probar conexión: " + e.getMessage());
            return false;
        }
    }
}