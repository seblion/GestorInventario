package app.Modelo;

/**
 * Clase modelo que representa un Proveedor
 */
public class Proveedor {
    
    private int id;
    private String nombre;
    private String contacto;

    // Constructores
    public Proveedor() {
    }
    
    public Proveedor(String nombre, String contacto) {
        this.nombre = nombre;
        this.contacto = contacto;
    }
    
    public Proveedor(int id, String nombre, String contacto) {
        this.id = id;
        this.nombre = nombre;
        this.contacto = contacto;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getContacto() {
        return contacto;
    }
    
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
    
    /**
     * Actualiza el contacto del proveedor
     */
    public void actualizarContacto(String contacto) {
        this.contacto = contacto;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Proveedor proveedor = (Proveedor) obj;
        return id == proveedor.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}