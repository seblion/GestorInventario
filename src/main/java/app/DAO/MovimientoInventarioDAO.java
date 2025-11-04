package app.DAO;

import app.Database.DatabaseConnection;
import app.Modelo.MovimientoInventario;
import app.Modelo.Producto;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MovimientoInventarioDAO {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final DatabaseConnection db;

    public MovimientoInventarioDAO() {
        this.db = DatabaseConnection.getInstancia();
    }

    /**
     * Registra un movimiento de inventario
     */
    public boolean registrarMovimiento(MovimientoInventario movimiento) throws Exception {
        // Calcular nuevo stock antes de insertar para evitar movimientos inválidos
        Producto producto = movimiento.getProducto();
        int nuevoStock = producto.getCantidad();

        if (movimiento.getTipo() == MovimientoInventario.TipoMovimiento.ENTRADA) {
            nuevoStock += movimiento.getCantidad();
        } else {
            nuevoStock -= movimiento.getCantidad();
        }

        if (nuevoStock < 0) {
            // No permitir registrar salidas que dejen stock negativo
            throw new IllegalStateException("Stock insuficiente para registrar el movimiento");
        }

        // Inserción
        String sql = String.format(
                "INSERT INTO movimientoinventario (id_prod, tipo, cantidad, fecha, motivo) " +
                        "VALUES (%d, '%s', %d, '%s', '%s')",
                producto.getId(),
                movimiento.getTipo().name(),
                movimiento.getCantidad(),
                new Timestamp(movimiento.getFecha().getTime()),
                movimiento.getMotivo()
        );

        // Ejecutar inserción
        db.insertarModificarEliminar(sql);

        // Actualizar stock del producto en la base
        if (productoDAO.actualizarStock(producto.getId(), nuevoStock)) {
            producto.setCantidad(nuevoStock);
            return true;
        }

        return false;
    }

    /**
     * Lista todos los movimientos
     */
    public List<MovimientoInventario> listarTodos() throws Exception {
        String sql = "SELECT * FROM movimientoinventario ORDER BY fecha DESC";
        db.consultarBase(sql);
        ResultSet rs = db.getResultado();

        List<MovimientoInventario> movimientos = new ArrayList<>();
        while (rs.next()) {
            movimientos.add(mapearMovimiento(rs));
        }
        return movimientos;
    }

    /**
     * Lista movimientos por producto
     */
    public List<MovimientoInventario> listarPorProducto(int productoId) throws Exception {
        String sql = String.format(
                "SELECT * FROM movimientoinventario WHERE id_prod=%d ORDER BY fecha DESC", productoId
        );

        db.consultarBase(sql);
        ResultSet rs = db.getResultado();
        List<MovimientoInventario> movimientos = new ArrayList<>();
        while (rs.next()) {
            movimientos.add(mapearMovimiento(rs));
        }
        return movimientos;
    }

    /**
     * Lista movimientos por tipo
     */
    public List<MovimientoInventario> listarPorTipo(MovimientoInventario.TipoMovimiento tipo) throws Exception {
        String sql = String.format(
                "SELECT * FROM movimientoinventario WHERE tipo='%s' ORDER BY fecha DESC", tipo.name()
        );

        db.consultarBase(sql);
        ResultSet rs = db.getResultado();
        List<MovimientoInventario> movimientos = new ArrayList<>();
        while (rs.next()) {
            movimientos.add(mapearMovimiento(rs));
        }
        return movimientos;
    }

    /**
     * Lista movimientos por rango de fechas
     */
    public List<MovimientoInventario> listarPorFechas(java.util.Date fechaInicio, java.util.Date fechaFin) throws Exception {
        String sql = String.format(
                "SELECT * FROM movimientoinventario WHERE fecha BETWEEN '%s' AND '%s' ORDER BY fecha DESC",
                new Timestamp(fechaInicio.getTime()),
                new Timestamp(fechaFin.getTime())
        );

        db.consultarBase(sql);
        ResultSet rs = db.getResultado();
        List<MovimientoInventario> movimientos = new ArrayList<>();
        while (rs.next()) {
            movimientos.add(mapearMovimiento(rs));
        }
        return movimientos;
    }

    /**
     * Mapea un ResultSet a un objeto MovimientoInventario
     */
    private MovimientoInventario mapearMovimiento(ResultSet rs) throws Exception {
        Producto producto = productoDAO.consultarPorId(rs.getInt("id_prod"));

        MovimientoInventario.TipoMovimiento tipo =
                MovimientoInventario.TipoMovimiento.valueOf(rs.getString("tipo"));

        return new MovimientoInventario(
                rs.getInt("id_mov"),
                producto,
                tipo,
                rs.getInt("cantidad"),
                rs.getTimestamp("fecha"),
                rs.getString("motivo")
        );
    }
}
