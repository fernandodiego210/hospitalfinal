package hospitalD;

//Clase PersonalCirugia para gestionar el equipo quirúrgico
public class PersonalCirugia {
 private int idPersonalCirugia;
 private Cirugia cirugia; // Relación con Cirugia
 private Doctor doctor; // Relación con Doctor (puede ser null)
 private Enfermero enfermero; // Relación con Enfermero (puede ser null)
 private String rolEnCirugia; // CIRUJANO_PRINCIPAL, ASISTENTE, ANESTESIOLOGO, INSTRUMENTISTA, CIRCULANTE
 
 // Constructores
 public PersonalCirugia() {}
 
 public PersonalCirugia(Cirugia cirugia, String rolEnCirugia) {
     this.cirugia = cirugia;
     this.rolEnCirugia = rolEnCirugia;
 }
 
 public PersonalCirugia(Cirugia cirugia, Doctor doctor, String rolEnCirugia) {
     this(cirugia, rolEnCirugia);
     this.doctor = doctor;
 }
 
 public PersonalCirugia(Cirugia cirugia, Enfermero enfermero, String rolEnCirugia) {
     this(cirugia, rolEnCirugia);
     this.enfermero = enfermero;
 }
 
 // Getters y setters
 public int getIdPersonalCirugia() { return idPersonalCirugia; }
 public void setIdPersonalCirugia(int idPersonalCirugia) { this.idPersonalCirugia = idPersonalCirugia; }
 
 public Cirugia getCirugia() { return cirugia; }
 public void setCirugia(Cirugia cirugia) { this.cirugia = cirugia; }
 
 public Doctor getDoctor() { return doctor; }
 public void setDoctor(Doctor doctor) { this.doctor = doctor; }
 
 public Enfermero getEnfermero() { return enfermero; }
 public void setEnfermero(Enfermero enfermero) { this.enfermero = enfermero; }
 
 public String getRolEnCirugia() { return rolEnCirugia; }
 public void setRolEnCirugia(String rolEnCirugia) { this.rolEnCirugia = rolEnCirugia; }
 
 // Métodos de clasificación de roles
 public boolean esCirujano() {
     return "CIRUJANO_PRINCIPAL".equalsIgnoreCase(rolEnCirugia);
 }
 
 public boolean esAsistente() {
     return "ASISTENTE".equalsIgnoreCase(rolEnCirugia);
 }
 
 public boolean esAnestesiologo() {
     return "ANESTESIOLOGO".equalsIgnoreCase(rolEnCirugia);
 }
 
 public boolean esInstrumentista() {
     return "INSTRUMENTISTA".equalsIgnoreCase(rolEnCirugia);
 }
 
 public boolean esCirculante() {
     return "CIRCULANTE".equalsIgnoreCase(rolEnCirugia);
 }
 
 // Métodos informativos
 public boolean esDoctor() {
     return doctor != null;
 }
 
 public boolean esEnfermero() {
     return enfermero != null;
 }
 
 public String getNombreCompleto() {
     if (doctor != null) {
         return "Dr. " + doctor.getNombreCompleto();
     } else if (enfermero != null) {
         return "Enf. " + enfermero.getNombreCompleto();
     } else {
         return "Personal no asignado";
     }
 }
 
 public String getTipoPersonal() {
     if (doctor != null) return "DOCTOR";
     if (enfermero != null) return "ENFERMERO";
     return "NO_ASIGNADO";
 }
 
 public String getEspecialidadOArea() {
     if (doctor != null && doctor.getEspecialidad() != null) {
         return doctor.getEspecialidad().getNombreEspecialidad();
     } else if (enfermero != null && enfermero.getAreaTrabajo() != null) {
         return enfermero.getAreaTrabajo();
     } else {
         return "No especificado";
     }
 }
 
 // Métodos de validación
 public boolean esRolValidoParaDoctor() {
     return doctor != null && (esCirujano() || esAsistente() || esAnestesiologo());
 }
 
 public boolean esRolValidoParaEnfermero() {
     return enfermero != null && (esInstrumentista() || esCirculante());
 }
 
 public boolean esAsignacionValida() {
     if (doctor == null && enfermero == null) return false;
     if (doctor != null && enfermero != null) return false; // No puede ser ambos
     
     return esRolValidoParaDoctor() || esRolValidoParaEnfermero();
 }
 
 public String getResponsabilidades() {
     switch (rolEnCirugia.toUpperCase()) {
         case "CIRUJANO_PRINCIPAL":
             return "• Liderar el procedimiento quirúrgico\n" +
                    "• Tomar decisiones médicas críticas\n" +
                    "• Supervisar el equipo quirúrgico\n" +
                    "• Realizar las incisiones y procedimientos principales";
                    
         case "ASISTENTE":
             return "• Asistir al cirujano principal\n" +
                    "• Mantener la hemostasia\n" +
                    "• Retraer tejidos\n" +
                    "• Suturar cuando sea necesario";
                    
         case "ANESTESIOLOGO":
             return "• Administrar anestesia\n" +
                    "• Monitorear signos vitales del paciente\n" +
                    "• Controlar el dolor durante y después de la cirugía\n" +
                    "• Manejar emergencias anestésicas";
                    
         case "INSTRUMENTISTA":
             return "• Preparar instrumentos quirúrgicos\n" +
                    "• Pasar instrumentos al cirujano\n" +
                    "• Mantener campo estéril\n" +
                    "• Contar instrumental y gasas";
                    
         case "CIRCULANTE":
             return "• Coordinar actividades fuera del campo estéril\n" +
                    "• Traer suministros adicionales\n" +
                    "• Comunicarse con personal externo\n" +
                    "• Documentar el procedimiento";
                    
         default:
             return "Responsabilidades no definidas para este rol";
     }
 }
 
 public int getNivelExperiencia() {
     if (doctor != null) {
         return doctor.getAnosExperiencia();
     } else if (enfermero != null) {
         return enfermero.getAnosExperiencia();
     }
     return 0;
 }
 
 public boolean esExperimentado() {
     return getNivelExperiencia() >= 5;
 }
 
 public boolean estaDisponible() {
     if (doctor != null) {
         return doctor.estaDisponible();
     } else if (enfermero != null) {
         // Los enfermeros no tienen método estaDisponible, asumimos que sí
         return true;
     }
     return false;
 }
 
 public String getInformacionCompleta() {
     return "=== PERSONAL QUIRÚRGICO ===\n" +
            "Rol: " + rolEnCirugia + "\n" +
            "Personal: " + getNombreCompleto() + "\n" +
            "Tipo: " + getTipoPersonal() + "\n" +
            "Especialidad/Área: " + getEspecialidadOArea() + "\n" +
            "Experiencia: " + getNivelExperiencia() + " años " + 
            (esExperimentado() ? "(Experimentado)" : "(Junior)") + "\n" +
            "Disponible: " + (estaDisponible() ? "SÍ" : "NO") + "\n" +
            "Asignación válida: " + (esAsignacionValida() ? "SÍ" : "NO") + "\n\n" +
            "RESPONSABILIDADES:\n" + getResponsabilidades();
 }
 
 @Override
 public String toString() {
     return rolEnCirugia + ": " + getNombreCompleto() + 
            " (" + getTipoPersonal() + ") - " + 
            getNivelExperiencia() + " años exp." +
            (esAsignacionValida() ? " ✅" : " ⚠️");
 }
 
 @Override
 public boolean equals(Object obj) {
     if (this == obj) return true;
     if (obj == null || getClass() != obj.getClass()) return false;
     PersonalCirugia that = (PersonalCirugia) obj;
     return idPersonalCirugia == that.idPersonalCirugia;
 }
 
 @Override
 public int hashCode() {
     return Integer.hashCode(idPersonalCirugia);
 }
}
