package app.DAO;

import app.Database.DatabaseConnection;
import app.Modelo.Producto;
import app.Modelo.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private final DatabaseConnection db;
    private final ProveedorDAO proveedorDAO;

    public ProductoDAO() {
        this.db = DatabaseConnection.getInstancia();
        this.proveedorDAO = new ProveedorDAO();
    }

    /* ======================= CRUD ======================= */

    public boolean registrarProducto(Producto producto) throws Exception {
        String sql = "INSERT INTO producto (codigo, nombre, categoria, precio, cantidad, proveedor_id) " +
                "VALUES (%d, '%s', '%s', %.2f, %d, %s)".formatted(
                        producto.getId(),
                        producto.getCodigo(),
                        producto.getNombre(),
                        producto.getCategoria(),
                        producto.getPrecio(),
                        producto.getCantidad(),
                        producto.getProveedor() != null ? producto.getProveedor().getId() : "NULL"
                );

        db.insertarModificarEliminar(sql);
        return true;
    }

    public boolean actualizarProducto(Producto producto) throws Exception {
        String sql = "UPDATE producto SET codigo='%s', nombre='%s', categoria='%s', precio=%.2f, cantidad=%d, proveedor_id=%s " +
                "WHERE id=%d".formatted(
                        producto.getCodigo(),
                        producto.getNombre(),
                        producto.getCategoria(),
                        producto.getPrecio(),
                        producto.getCantidad(),
                        producto.getProveedor() != null ? producto.getProveedor().getId() : "NULL",
                        producto.getId()
                );

        db.insertarModificarEliminar(sql);
        return true;
    }

    public boolean eliminarProducto(String codigo) throws Exception {
        String sql = "DELETE FROM producto WHERE codigo='%s'".formatted(codigo);
        db.insertarModificarEliminar(sql);
        return true;
    }

    /* ======================= CONSULTAS ======================= */

    public Producto consultarPorId(int id) throws Exception {
        String sql = "SELECT * FROM producto WHERE id=%d".formatted(id);
        db.consultarBase(sql);
        ResultSet rs = db.getResultado();
        return rs.next() ? mapearProducto(rs) : null;
    }

    public Producto consultarPorCodigo(String codigo) throws Exception {
        String sql = "SELECT * FROM producto WHERE codigo='%s'".formatted(codigo);
        db.consultarBase(sql);
        ResultSet rs = db.getResultado();
        return rs.next() ? mapearProducto(rs) : null;
    }

    public List<Producto> listarTodos() throws Exception {
        String sql = "SELECT * FROM producto ORDER BY nombre";
        db.consultarBase(sql);
        return mapearLista(db.getResultado());
    }

    public List<Producto> consultarPorCategoria(String categoria) throws Exception {
        String sql = "SELECT * FROM producto WHERE categoria='%s' ORDER BY nombre".formatted(categoria);
        db.consultarBase(sql);
        return mapearLista(db.getResultado());
    }

    public boolean actualizarStock(int productoId, int nuevaCantidad) throws Exception {
        String sql = "UPDATE producto SET cantidad=%d WHERE id=%d".formatted(nuevaCantidad, productoId);
        db.insertarModificarEliminar(sql);
        return true;
    }

    /* ======================= UTIL ======================= */

    private List<Producto> mapearLista(ResultSet rs) throws Exception  {
        List<Producto> lista = new ArrayList<>();
        while (rs.next()) lista.add(mapearProducto(rs));
        return lista;
    }

    private Producto mapearProducto(ResultSet rs) throws Exception  {
        Proveedor proveedor = null;
        int proveedorId = rs.getInt("proveedor_id");
        if (!rs.wasNull()) {
            proveedor = proveedorDAO.consultarPorId(proveedorId);
        }

        return new Producto(
                rs.getInt("id"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getString("categoria"),
                rs.getDouble("precio"),
                rs.getInt("cantidad"),
                proveedor
        );
    }
}
