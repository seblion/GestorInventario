package app.Modelo;

import java.util.Date;

/**
 * Clase modelo que representa un Movimiento de Inventario
 */
public class MovimientoInventario {
    
    public enum TipoMovimiento {
        ENTRADA, SALIDA
    }
    
    private int id;
    private Producto producto;
    private TipoMovimiento tipo;
    private int cantidad;
    private Date fecha;
    private String motivo;
    
    // Constructores
    public MovimientoInventario() {
        this.fecha = new Date();
    }
    
    public MovimientoInventario(Producto producto, TipoMovimiento tipo, 
                               int cantidad, String motivo) {
        this.producto = producto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.fecha = new Date();
        this.motivo = motivo;
    }
    
    public MovimientoInventario(int id, Producto producto, TipoMovimiento tipo,
                               int cantidad, Date fecha, String motivo) {
        this.id = id;
        this.producto = producto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.motivo = motivo;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public TipoMovimiento getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public String getMotivo() {
        return motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    /**
     * Aplica el movimiento al inventario del producto
     */
    public void aplicarMovimiento() {
        if (producto != null) {
            int stockActual = producto.getCantidad();
            if (tipo == TipoMovimiento.ENTRADA) {
                producto.actualizarCantidad(stockActual + cantidad);
            } else if (tipo == TipoMovimiento.SALIDA) {
                if (stockActual >= cantidad) {
                    producto.actualizarCantidad(stockActual - cantidad);
                } else {
                    throw new IllegalStateException("Stock insuficiente para realizar la salida");
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return tipo + " - " + cantidad + " unidades - " + fecha;
    }
}