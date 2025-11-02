package app.Servicio;

import app.DAO.MovimientoInventarioDAO;
import app.DAO.ProductoDAO;
import app.Modelo.Producto;
import app.Modelo.Proveedor;
import app.Modelo.MovimientoInventario;

import java.util.Date;
import java.util.List;

/**
 * Servicio para manejar la lógica de negocio del Inventario
 */
public class ServicioInventario {
    
    private ProductoDAO productoDAO;
    private MovimientoInventarioDAO movimientoDAO;
    
    public ServicioInventario() {
        this.productoDAO = new ProductoDAO();
        this.movimientoDAO = new MovimientoInventarioDAO();
    }
    
    /**
     * Registra una entrada de productos al inventario
     */
    public boolean registrarEntrada(Producto producto, int cantidad, Date fecha, 
                                   Proveedor proveedor, String motivo) throws Exception {
        if (producto == null) {
            System.err.println("El producto no puede ser nulo");
            return false;
        }
        
        if (cantidad <= 0) {
            System.err.println("La cantidad debe ser mayor a cero");
            return false;
        }
        
        // Crear el movimiento de entrada
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setTipo(MovimientoInventario.TipoMovimiento.ENTRADA);
        movimiento.setCantidad(cantidad);
        movimiento.setFecha(fecha != null ? fecha : new Date());
        movimiento.setMotivo(motivo != null ? motivo : "Entrada de inventario");
        movimiento.setProveedor(proveedor);
        
        return movimientoDAO.registrarMovimiento(movimiento);
    }
    
    /**
     * Registra una salida de productos del inventario
     */
    public boolean registrarSalida(Producto producto, int cantidad, Date fecha, String motivo) throws Exception {
        if (producto == null) {
            System.err.println("El producto no puede ser nulo");
            return false;
        }
        
        if (cantidad <= 0) {
            System.err.println("La cantidad debe ser mayor a cero");
            return false;
        }
        
        // Verificar que haya suficiente stock
        if (producto.getCantidad() < cantidad) {
            System.err.println("Stock insuficiente. Disponible: " + producto.getCantidad() + 
                             ", Requerido: " + cantidad);
            return false;
        }
        
        // Crear el movimiento de salida
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setTipo(MovimientoInventario.TipoMovimiento.SALIDA);
        movimiento.setCantidad(cantidad);
        movimiento.setFecha(fecha != null ? fecha : new Date());
        movimiento.setMotivo(motivo != null ? motivo : "Salida de inventario");
        
        return movimientoDAO.registrarMovimiento(movimiento);
    }
    
    /**
     * Consulta el stock actual de productos
     */
    public List<Producto> consultarStock() throws Exception {
        return productoDAO.listarTodos();
    }
    
    /**
     * Consulta productos con stock bajo (menor a cantidad mínima)
     */
    public List<Producto> consultarStockBajo(int cantidadMinima) throws Exception {
        List<Producto> todosLosProductos = productoDAO.listarTodos();
        todosLosProductos.removeIf(p -> p.getCantidad() >= cantidadMinima);
        return todosLosProductos;
    }
    
    /**
     * Registra un nuevo producto
     */
    public boolean registrarProducto(Producto producto) throws Exception {
        if (producto == null) {
            System.err.println("El producto no puede ser nulo");
            return false;
        }
        
        if (producto.getCodigo() == null || producto.getCodigo().trim().isEmpty()) {
            System.err.println("El código del producto es obligatorio");
            return false;
        }
        
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            System.err.println("El nombre del producto es obligatorio");
            return false;
        }
        
        // Verificar que no exista un producto con el mismo código
        Producto existente = productoDAO.consultarPorCodigo(producto.getCodigo());
        if (existente != null) {
            System.err.println("Ya existe un producto con ese código");
            return false;
        }
        
        return productoDAO.registrarProducto(producto);
    }
    
    /**
     * Modifica un producto existente
     */
    public boolean modificarProducto(Producto producto) throws Exception {
        if (producto == null || producto.getId() <= 0) {
            System.err.println("Producto inválido");
            return false;
        }
        
        return productoDAO.actualizarProducto(producto);
    }
    
    /**
     * Elimina un producto del inventario
     */
    public boolean eliminarProducto(String codigo) throws Exception {
        if (codigo == null || codigo.trim().isEmpty()) {
            System.err.println("El código es obligatorio");
            return false;
        }
        
        return productoDAO.eliminarProducto(codigo);
    }
    
    /**
     * Consulta un producto por código
     */
    public Producto consultarProductoPorCodigo(String codigo) throws Exception {
        if (codigo == null || codigo.trim().isEmpty()) {
            System.err.println("El código es obligatorio");
            return null;
        }
        
        return productoDAO.consultarPorCodigo(codigo);
    }
    
    /**
     * Consulta productos por categoría
     */
    public List<Producto> consultarPorCategoria(String categoria) throws Exception {
        return productoDAO.consultarPorCategoria(categoria);
    }
    
    /**
     * Lista todos los movimientos
     */
    public List<MovimientoInventario> listarMovimientos() throws Exception {
        return movimientoDAO.listarTodos();
    }
    
    /**
     * Lista movimientos de un producto específico
     */
    public List<MovimientoInventario> listarMovimientosPorProducto(int productoId) throws Exception {
        return movimientoDAO.listarPorProducto(productoId);
    }
    
    /**
     * Lista movimientos por tipo
     */
    public List<MovimientoInventario> listarMovimientosPorTipo(MovimientoInventario.TipoMovimiento tipo) throws Exception {
        return movimientoDAO.listarPorTipo(tipo);
    }
}