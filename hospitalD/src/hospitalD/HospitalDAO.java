package hospitalD;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Clase DAO actualizada con todas las nuevas funcionalidades
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
		
    
    // ============= NUEVOS MÉTODOS PARA PROVEEDORES =============
    
    public boolean insertarProveedor(Proveedor proveedor) {
        String sql = "INSERT INTO Proveedor (nombre_proveedor, ruc, direccion, telefono, email, contacto_representante) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, proveedor.getNombreProveedor());
            pstmt.setString(2, proveedor.getRuc());
            pstmt.setString(3, proveedor.getDireccion());
            pstmt.setString(4, proveedor.getTelefono());
            pstmt.setString(5, proveedor.getEmail());
            pstmt.setString(6, proveedor.getContactoRepresentante());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    proveedor.setIdProveedor(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar proveedor: " + e.getMessage());
        }
        return false;
    }
    
    public List<Proveedor> obtenerProveedores() {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT id_proveedor, nombre_proveedor, ruc, direccion, telefono, email, " +
                    "contacto_representante, estado FROM Proveedor";
        
        try (Connection conn = ConexionBD.getConnection();
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
    
    // ============= NUEVOS MÉTODOS PARA FARMACIA =============
    
    public List<Farmacia> obtenerFarmacias() {
        List<Farmacia> farmacias = new ArrayList<>();
        String sql = "SELECT id_farmacia, nombre_farmacia, ubicacion, telefono_interno, " +
                    "horario_inicio, horario_fin FROM Farmacia";
        
        try (Connection conn = ConexionBD.getConnection();
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
                    "m.contraindicaciones, m.efectos_secundarios, " +
                    "p.id_proveedor, p.nombre_proveedor " +
                    "FROM Medicamento m " +
                    "LEFT JOIN Proveedor p ON m.id_proveedor = p.id_proveedor";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
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
                medicamento.setContraindicaciones(rs.getString("contraindicaciones"));
                medicamento.setEfectosSecundarios(rs.getString("efectos_secundarios"));
                
                Date fechaVenc = rs.getDate("fecha_vencimiento");
                if (fechaVenc != null) {
                    medicamento.setFechaVencimiento(fechaVenc.toLocalDate());
                }
                
                // Proveedor
                int idProveedor = rs.getInt("id_proveedor");
                if (idProveedor > 0) {
                    Proveedor proveedor = new Proveedor();
                    proveedor.setIdProveedor(idProveedor);
                    proveedor.setNombreProveedor(rs.getString("nombre_proveedor"));
                    medicamento.setProveedor(proveedor);
                }
                
                medicamentos.add(medicamento);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener medicamentos: " + e.getMessage());
        }
        return medicamentos;
    }
    
    // ============= NUEVOS MÉTODOS PARA LABORATORIO =============
    
    public List<Laboratorio> obtenerLaboratorios() {
        List<Laboratorio> laboratorios = new ArrayList<>();
        String sql = "SELECT id_laboratorio, nombre_laboratorio, ubicacion, telefono_interno, " +
                    "equipos_disponibles, horario_inicio, horario_fin FROM Laboratorio";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
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
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                TipoExamen tipoExamen = new TipoExamen();
                tipoExamen.setIdTipoExamen(rs.getInt("id_tipo_examen"));
                tipoExamen.setNombreExamen(rs.getString("nombre_examen"));
                tipoExamen.setDescripcion(rs.getString("descripcion"));
                tipoExamen.setPrecio(rs.getBigDecimal("precio"));
                tipoExamen.setTiempoResultadoHoras(rs.getInt("tiempo_resultado_horas"));
                tipoExamen.setRequiereAyuno(rs.getBoolean("requiere_ayuno"));
                
                // Laboratorio
                int idLab = rs.getInt("id_laboratorio");
                if (idLab > 0) {
                    Laboratorio lab = new Laboratorio();
                    lab.setIdLaboratorio(idLab);
                    lab.setNombreLaboratorio(rs.getString("nombre_laboratorio"));
                    tipoExamen.setLaboratorio(lab);
                }
                
                tiposExamen.add(tipoExamen);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener tipos de examen: " + e.getMessage());
        }
        return tiposExamen;
    }
    
    public boolean insertarOrdenExamen(OrdenExamen orden) {
        String sql = "INSERT INTO OrdenExamen (id_cita, id_paciente, id_doctor, id_tipo_examen, " +
                    "fecha_orden, estado_orden, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, orden.getCita() != null ? orden.getCita().getIdCita() : 0);
            pstmt.setInt(2, orden.getPaciente().getIdPaciente());
            pstmt.setInt(3, orden.getDoctor().getIdDoctor());
            pstmt.setInt(4, orden.getTipoExamen().getIdTipoExamen());
            pstmt.setTimestamp(5, Timestamp.valueOf(orden.getFechaOrden()));
            pstmt.setString(6, orden.getEstadoOrden());
            pstmt.setString(7, orden.getObservaciones());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    orden.setIdOrdenExamen(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar orden de examen: " + e.getMessage());
        }
        return false;
    }
    
    // ============= NUEVOS MÉTODOS PARA EMERGENCIAS =============
    
    public boolean insertarEmergencia(Emergencia emergencia) {
        String sql = "INSERT INTO Emergencia (id_paciente, sintomas_principales, nivel_prioridad, " +
                    "estado_emergencia, tiempo_espera_minutos, observaciones) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
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
    
    public List<Emergencia> obtenerEmergenciasActivas() {
        List<Emergencia> emergencias = new ArrayList<>();
        String sql = "SELECT e.id_emergencia, e.fecha_ingreso, e.sintomas_principales, " +
                    "e.nivel_prioridad, e.estado_emergencia, e.tiempo_espera_minutos, e.observaciones, " +
                    "p.id_paciente, p.numero_historia, pe.nombre, pe.apellido " +
                    "FROM Emergencia e " +
                    "INNER JOIN Paciente p ON e.id_paciente = p.id_paciente " +
                    "INNER JOIN Persona pe ON p.id_persona = pe.id_persona " +
                    "WHERE e.estado_emergencia IN ('EN_ATENCION', 'ESTABLE') " +
                    "ORDER BY e.fecha_ingreso DESC";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Emergencia emergencia = new Emergencia();
                emergencia.setIdEmergencia(rs.getInt("id_emergencia"));
                emergencia.setSintomasPrincipales(rs.getString("sintomas_principales"));
                emergencia.setNivelPrioridad(rs.getString("nivel_prioridad"));
                emergencia.setEstadoEmergencia(rs.getString("estado_emergencia"));
                emergencia.setTiempoEsperaMinutos(rs.getInt("tiempo_espera_minutos"));
                emergencia.setObservaciones(rs.getString("observaciones"));
                
                Timestamp fechaIngreso = rs.getTimestamp("fecha_ingreso");
                if (fechaIngreso != null) {
                    emergencia.setFechaIngreso(fechaIngreso.toLocalDateTime());
                }
                
                // Paciente simplificado
                Paciente paciente = new Paciente();
                paciente.setIdPaciente(rs.getInt("id_paciente"));
                paciente.setNumeroHistoria(rs.getString("numero_historia"));
                paciente.setNombre(rs.getString("nombre"));
                paciente.setApellido(rs.getString("apellido"));
                emergencia.setPaciente(paciente);
                
                emergencias.add(emergencia);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener emergencias activas: " + e.getMessage());
        }
        return emergencias;
    }
    
    public List<Ambulancia> obtenerAmbulancias() {
        List<Ambulancia> ambulancias = new ArrayList<>();
        String sql = "SELECT id_ambulancia, placa, modelo, tipo_ambulancia, " +
                    "capacidad_pacientes, estado, ubicacion_actual FROM Ambulancia";
        
        try (Connection conn = ConexionBD.getConnection();
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
    
    // ============= NUEVOS MÉTODOS PARA CIRUGÍAS =============
    
    public boolean insertarCirugia(Cirugia cirugia) {
        String sql = "INSERT INTO Cirugia (id_paciente, id_doctor_cirujano, nombre_cirugia, " +
                    "descripcion_procedimiento, fecha_programada, estado_cirugia, tipo_anestesia, " +
                    "riesgo_quirurgico, observaciones_preoperatorias) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, cirugia.getPaciente().getIdPaciente());
            pstmt.setInt(2, cirugia.getDoctorCirujano().getIdDoctor());
            pstmt.setString(3, cirugia.getNombreCirugia());
            pstmt.setString(4, cirugia.getDescripcionProcedimiento());
            pstmt.setTimestamp(5, cirugia.getFechaProgramada() != null ? 
                                  Timestamp.valueOf(cirugia.getFechaProgramada()) : null);
            pstmt.setString(6, cirugia.getEstadoCirugia());
            pstmt.setString(7, cirugia.getTipoAnestesia());
            pstmt.setString(8, cirugia.getRiesgoQuirurgico());
            pstmt.setString(9, cirugia.getObservacionesPreoperatorias());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    cirugia.setIdCirugia(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar cirugía: " + e.getMessage());
        }
        return false;
    }
    
    // ============= NUEVOS MÉTODOS PARA SIGNOS VITALES =============
    
    public boolean insertarSignosVitales(SignosVitales signos) {
        String sql = "INSERT INTO SignosVitales (id_paciente, id_enfermero, presion_sistolica, " +
                    "presion_diastolica, frecuencia_cardiaca, frecuencia_respiratoria, temperatura, " +
                    "saturacion_oxigeno, peso, altura, observaciones) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
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
    
    public List<SignosVitales> obtenerSignosVitalesRecientes(int idPaciente, int limite) {
        List<SignosVitales> signos = new ArrayList<>();
        String sql = "SELECT TOP (?) sv.id_signos_vitales, sv.fecha_registro, " +
                    "sv.presion_sistolica, sv.presion_diastolica, sv.frecuencia_cardiaca, " +
                    "sv.frecuencia_respiratoria, sv.temperatura, sv.saturacion_oxigeno, " +
                    "sv.peso, sv.altura, sv.observaciones, " +
                    "p.id_paciente, pe.nombre, pe.apellido, " +
                    "e.id_enfermero, pen.nombre as enfermero_nombre, pen.apellido as enfermero_apellido " +
                    "FROM SignosVitales sv " +
                    "INNER JOIN Paciente p ON sv.id_paciente = p.id_paciente " +
                    "INNER JOIN Persona pe ON p.id_persona = pe.id_persona " +
                    "INNER JOIN Enfermero e ON sv.id_enfermero = e.id_enfermero " +
                    "INNER JOIN Persona pen ON e.id_persona = pen.id_persona " +
                    "WHERE sv.id_paciente = ? " +
                    "ORDER BY sv.fecha_registro DESC";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limite);
            pstmt.setInt(2, idPaciente);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                SignosVitales sv = new SignosVitales();
                sv.setIdSignosVitales(rs.getInt("id_signos_vitales"));
                
                Timestamp fechaRegistro = rs.getTimestamp("fecha_registro");
                if (fechaRegistro != null) {
                    sv.setFechaRegistro(fechaRegistro.toLocalDateTime());
                }
                
                sv.setPresionSistolica((Integer) rs.getObject("presion_sistolica"));
                sv.setPresionDiastolica((Integer) rs.getObject("presion_diastolica"));
                sv.setFrecuenciaCardiaca((Integer) rs.getObject("frecuencia_cardiaca"));
                sv.setFrecuenciaRespiratoria((Integer) rs.getObject("frecuencia_respiratoria"));
                sv.setTemperatura(rs.getBigDecimal("temperatura"));
                sv.setSaturacionOxigeno((Integer) rs.getObject("saturacion_oxigeno"));
                sv.setPeso(rs.getBigDecimal("peso"));
                sv.setAltura(rs.getBigDecimal("altura"));
                sv.setObservaciones(rs.getString("observaciones"));
                
                // Paciente simplificado
                Paciente paciente = new Paciente();
                paciente.setIdPaciente(rs.getInt("id_paciente"));
                paciente.setNombre(rs.getString("nombre"));
                paciente.setApellido(rs.getString("apellido"));
                sv.setPaciente(paciente);
                
                // Enfermero simplificado
                Enfermero enfermero = new Enfermero();
                enfermero.setIdEnfermero(rs.getInt("id_enfermero"));
                enfermero.setNombre(rs.getString("enfermero_nombre"));
                enfermero.setApellido(rs.getString("enfermero_apellido"));
                sv.setEnfermero(enfermero);
                
                signos.add(sv);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener signos vitales: " + e.getMessage());
        }
        return signos;
    }
    
    // ============= NUEVOS MÉTODOS PARA SEGUROS MÉDICOS =============
    
    public List<SeguroMedico> obtenerSegurosMedicos() {
        List<SeguroMedico> seguros = new ArrayList<>();
        String sql = "SELECT id_seguro, nombre_seguro, tipo_seguro, cobertura_porcentaje, " +
                    "telefono_seguro, email_seguro, estado FROM SeguroMedico";
        
        try (Connection conn = ConexionBD.getConnection();
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
                
                // Configurar coberturas por tipo
                seguro.configurarCoberturasPorTipo();
                
                seguros.add(seguro);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener seguros médicos: " + e.getMessage());
        }
        return seguros;
    }
    
    // ============= MÉTODOS DE ESTADÍSTICAS AMPLIADAS =============
    
    public int contarEmergenciasActivas() {
        String sql = "SELECT COUNT(*) as total FROM Emergencia WHERE estado_emergencia IN ('EN_ATENCION', 'ESTABLE')";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar emergencias activas: " + e.getMessage());
        }
        return 0;
    }
    
    public int contarEmergenciasCriticas() {
        String sql = "SELECT COUNT(*) as total FROM Emergencia WHERE nivel_prioridad = 'CRITICO' AND estado_emergencia = 'EN_ATENCION'";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar emergencias críticas: " + e.getMessage());
        }
        return 0;
    }
    
    public int contarAmbulanciasDisponibles() {
        String sql = "SELECT COUNT(*) as total FROM Ambulancia WHERE estado = 'DISPONIBLE'";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar ambulancias disponibles: " + e.getMessage());
        }
        return 0;
    }
    
    public int contarCirugiasDeHoy() {
        String sql = "SELECT COUNT(*) as total FROM Cirugia WHERE " +
                    "CAST(fecha_programada AS DATE) = CAST(GETDATE() AS DATE)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar cirugías de hoy: " + e.getMessage());
        }
        return 0;
    }
    
    public int contarCirugiasEnCurso() {
        String sql = "SELECT COUNT(*) as total FROM Cirugia WHERE estado_cirugia = 'EN_CURSO'";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar cirugías en curso: " + e.getMessage());
        }
        return 0;
    }
    
    public int contarMedicamentosStockBajo() {
        String sql = "SELECT COUNT(*) as total FROM Medicamento WHERE stock_actual <= stock_minimo";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar medicamentos con stock bajo: " + e.getMessage());
        }
        return 0;
    }
    
    public int contarMedicamentosVencidos() {
        String sql = "SELECT COUNT(*) as total FROM Medicamento WHERE fecha_vencimiento < GETDATE()";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar medicamentos vencidos: " + e.getMessage());
        }
        return 0;
    }
    
    public int contarOrdenesExamenPendientes() {
        String sql = "SELECT COUNT(*) as total FROM OrdenExamen WHERE estado_orden IN ('PENDIENTE', 'PROCESANDO')";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar órdenes de examen pendientes: " + e.getMessage());
        }
        return 0;
    }
    
    public int contarFarmaciasAbiertas() {
        String sql = "SELECT COUNT(*) as total FROM Farmacia WHERE " +
                    "CAST(GETDATE() AS TIME) BETWEEN horario_inicio AND horario_fin";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar farmacias abiertas: " + e.getMessage());
        }
        return 0;
    }
    
    public int contarLaboratoriosOperativos() {
        String sql = "SELECT COUNT(*) as total FROM Laboratorio WHERE " +
                    "CAST(GETDATE() AS TIME) BETWEEN horario_inicio AND horario_fin";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error al contar laboratorios operativos: " + e.getMessage());
        }
        return 0;
    }
    
    // ============= MÉTODOS DE ACTUALIZACIÓN =============
    
    public boolean actualizarEstadoEmergencia(int idEmergencia, String nuevoEstado, String observaciones) {
        String sql = "UPDATE Emergencia SET estado_emergencia = ?, observaciones = ? WHERE id_emergencia = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nuevoEstado);
            pstmt.setString(2, observaciones);
            pstmt.setInt(3, idEmergencia);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de emergencia: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizarEstadoAmbulancia(int idAmbulancia, String nuevoEstado, String ubicacion) {
        String sql = "UPDATE Ambulancia SET estado = ?, ubicacion_actual = ? WHERE id_ambulancia = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nuevoEstado);
            pstmt.setString(2, ubicacion);
            pstmt.setInt(3, idAmbulancia);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de ambulancia: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizarStockMedicamento(int idMedicamento, int nuevoStock) {
        String sql = "UPDATE Medicamento SET stock_actual = ? WHERE id_medicamento = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, nuevoStock);
            pstmt.setInt(2, idMedicamento);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar stock de medicamento: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizarEstadoCirugia(int idCirugia, String nuevoEstado) {
        String sql = "UPDATE Cirugia SET estado_cirugia = ? WHERE id_cirugia = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idCirugia);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de cirugía: " + e.getMessage());
            return false;
        }
    }
    
    // ============= MÉTODO DE REPORTE EJECUTIVO =============
    
    public void generarReporteBaseDatos() {
        System.out.println("📊 === REPORTE DE BASE DE DATOS ===");
        
        try (Connection conn = ConexionBD.getConnection()) {
            // Información de conexión
            DatabaseMetaData metaData = conn.getMetaData();
            System.out.println("Servidor: " + metaData.getURL());
            System.out.println("Usuario: " + metaData.getUserName());
            System.out.println("Driver: " + metaData.getDriverName() + " " + metaData.getDriverVersion());
            
            // Estadísticas de tablas
            System.out.println("\n📋 ESTADÍSTICAS POR TABLA:");
            String[] tablas = {
                "Persona", "Doctor", "Paciente", "Enfermero", 
                "Especialidad", "Departamento", "Habitacion", "Cita",
                "Proveedor", "Farmacia", "Medicamento", 
                "Laboratorio", "TipoExamen", "OrdenExamen",
                "Emergencia", "Ambulancia", "Cirugia",
                "SignosVitales", "SeguroMedico"
            };
            
            for (String tabla : tablas) {
                int count = contarRegistrosTabla(tabla);
                System.out.println(String.format("%-20s: %6d registros", tabla, count));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al generar reporte de BD: " + e.getMessage());
        }
    }
    
    private int contarRegistrosTabla(String nombreTabla) {
        String sql = "SELECT COUNT(*) as total FROM " + nombreTabla;
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            // Tabla puede no existir, retornar 0
            return 0;
        }
        return 0;
    }
}