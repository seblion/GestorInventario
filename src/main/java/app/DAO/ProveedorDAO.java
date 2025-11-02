package app.DAO;

import app.Database.DatabaseConnection;
import app.Modelo.Proveedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD de Proveedores usando Singleton
 */
public class ProveedorDAO {

    private final DatabaseConnection db;

    public ProveedorDAO() {
        this.db = DatabaseConnection.getInstancia();
    }

    /**
     * Registra un nuevo proveedor
     */
    public boolean registrarProveedor(Proveedor proveedor) throws Exception {
        String sql = String.format(
                "INSERT INTO proveedor (nombre, contacto, codigo_producto) VALUES ('%s', '%s', '%s')",
                proveedor.getNombre(), proveedor.getContacto(), proveedor.getCodigoProducto()
        );

        db.insertarModificarEliminar(sql);

        // Se puede obtener el ID si la BD lo devuelve con RETURNING o similar
        return true;
    }

    /**
     * Actualiza un proveedor existente
     */
    public boolean actualizarProveedor(Proveedor proveedor) throws Exception {
        String sql = String.format(
                "UPDATE proveedor SET nombre='%s', contacto='%s', codigo_producto='%s' WHERE id=%d",
                proveedor.getNombre(), proveedor.getContacto(), proveedor.getCodigoProducto(), proveedor.getId()
        );

        db.insertarModificarEliminar(sql);
        return true;
    }

    /**
     * Elimina un proveedor por nombre
     */
    public boolean eliminarProveedor(String nombre) throws Exception {
        String sql = String.format("DELETE FROM proveedor WHERE nombre='%s'", nombre);
        db.insertarModificarEliminar(sql);
        return true;
    }

    /**
     * Consulta un proveedor por nombre
     */
    public Proveedor consultarPorNombre(String nombre) throws Exception {
        String sql = String.format("SELECT * FROM proveedor WHERE nombre='%s'", nombre);

        db.consultarBase(sql);
        ResultSet rs = db.getResultado();
        return rs.next() ? mapearProveedor(rs) : null;
    }

    /**
     * Consulta un proveedor por ID
     */
    public Proveedor consultarPorId(int id) throws Exception {
        String sql = "SELECT * FROM proveedor WHERE id=" + id;

        db.consultarBase(sql);
        ResultSet rs = db.getResultado();
        return rs.next() ? mapearProveedor(rs) : null;
    }

    /**
     * Lista todos los proveedores
     */
    public List<Proveedor> listarTodos() throws Exception {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM proveedor ORDER BY nombre";

        db.consultarBase(sql);
        ResultSet rs = db.getResultado();
        while (rs.next()) {
            proveedores.add(mapearProveedor(rs));
        }

        return proveedores;
    }

    /**
     * Mapea un ResultSet a un objeto Proveedor
     */
    private Proveedor mapearProveedor(ResultSet rs) throws SQLException {
        return new Proveedor(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("contacto"),
                rs.getString("codigo_producto")
        );
    }
}
