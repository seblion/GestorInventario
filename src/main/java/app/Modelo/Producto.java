package app.Modelo;

/**
 * Clase modelo que representa un Producto
 */
public class Producto {
    
    private int id;
    private String codigo;
    private String nombre;
    private String categoria;
    private double precio;
    private int cantidad;
    private Proveedor proveedor;
    
    // Constructores
    public Producto() {
    }
    
    public Producto(String codigo, String nombre, String categoria, double precio, int cantidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.cantidad = cantidad;
    }
    
    public Producto(int id, String codigo, String nombre, String categoria, 
                   double precio, int cantidad, Proveedor proveedor) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.cantidad = cantidad;
        this.proveedor = proveedor;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public double getPrecio() {
        return precio;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public Proveedor getProveedor() {
        return proveedor;
    }
    
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }
    
    /**
     * Actualiza la cantidad del producto
     */
    public void actualizarCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    /**
     * Actualiza el precio del producto
     */
    public void actualizarPrecio(double precio) {
        this.precio = precio;
    }
    
    /**
     * Verifica si hay stock disponible
     */
    public boolean hayStock(int cantidadRequerida) {
        return this.cantidad >= cantidadRequerida;
    }
    
    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Producto producto = (Producto) obj;
        return id == producto.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}