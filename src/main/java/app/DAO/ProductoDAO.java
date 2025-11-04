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
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            db.conectarBase();
            conn = db.conexion;
            
            String sql = "INSERT INTO producto (codigo, nombre, categoria, precio, cantidad, id_prov) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getCategoria());
            ps.setDouble(4, producto.getPrecio());
            ps.setInt(5, producto.getCantidad());
            
            if (producto.getProveedor() != null) {
                ps.setInt(6, producto.getProveedor().getId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            
            ps.executeUpdate();
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            if (ps != null) ps.close();
            db.desconectarBase();
        }
    }

    public boolean actualizarProducto(Producto producto) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            db.conectarBase();
            conn = db.conexion;
            
            String sql = "UPDATE producto SET codigo=?, nombre=?, categoria=?, precio=?, cantidad=?, id_prov=? " +
                        "WHERE id_prod=?";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getCategoria());
            ps.setDouble(4, producto.getPrecio());
            ps.setInt(5, producto.getCantidad());
            
            if (producto.getProveedor() != null) {
                ps.setInt(6, producto.getProveedor().getId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            
            ps.setInt(7, producto.getId());
            
            ps.executeUpdate();
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            if (ps != null) ps.close();
            db.desconectarBase();
        }
    }

    public boolean eliminarProducto(String codigo) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            db.conectarBase();
            conn = db.conexion;
            
            String sql = "DELETE FROM producto WHERE codigo=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, codigo);
            
            ps.executeUpdate();
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            if (ps != null) ps.close();
            db.desconectarBase();
        }
    }

    /* ======================= CONSULTAS ======================= */

    public Producto consultarPorId(int id) throws Exception {
        PreparedStatement ps = null;
        try {
            db.conectarBase();
            String sql = "SELECT * FROM producto WHERE id=?";
            ps = db.conexion.prepareStatement(sql);
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            return rs.next() ? mapearProducto(rs) : null;
        } finally {
            if (ps != null) ps.close();
            db.desconectarBase();
        }
    }

    public Producto consultarPorCodigo(String codigo) throws Exception {
        PreparedStatement ps = null;
        try {
            db.conectarBase();
            String sql = "SELECT * FROM producto WHERE codigo=?";
            ps = db.conexion.prepareStatement(sql);
            ps.setString(1, codigo);
            
            ResultSet rs = ps.executeQuery();
            return rs.next() ? mapearProducto(rs) : null;
        } finally {
            if (ps != null) ps.close();
            db.desconectarBase();
        }
    }

    public List<Producto> listarTodos() throws Exception {
        try {
            String sql = "SELECT * FROM producto ORDER BY nombre";
            db.consultarBase(sql);
            return mapearLista(db.getResultado());
        } finally {
            db.desconectarBase();
        }
    }

    public List<Producto> consultarPorCategoria(String categoria) throws Exception {
        PreparedStatement ps = null;
        try {
            db.conectarBase();
            String sql = "SELECT * FROM producto WHERE categoria=? ORDER BY nombre";
            ps = db.conexion.prepareStatement(sql);
            ps.setString(1, categoria);
            
            ResultSet rs = ps.executeQuery();
            return mapearLista(rs);
        } finally {
            if (ps != null) ps.close();
            db.desconectarBase();
        }
    }

    public boolean actualizarStock(int productoId, int nuevaCantidad) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            db.conectarBase();
            conn = db.conexion;
            
            String sql = "UPDATE producto SET cantidad=? WHERE id_prod=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, productoId);
            
            ps.executeUpdate();
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            if (ps != null) ps.close();
            db.desconectarBase();
        }
    }

    /* ======================= UTIL ======================= */

    private List<Producto> mapearLista(ResultSet rs) throws Exception {
        List<Producto> lista = new ArrayList<>();
        while (rs.next()) lista.add(mapearProducto(rs));
        return lista;
    }

    private Producto mapearProducto(ResultSet rs) throws Exception {
        Proveedor proveedor = null;
        int proveedorId = rs.getInt("id_prod");
        if (!rs.wasNull()) {
            proveedor = proveedorDAO.consultarPorId(proveedorId);
        }

        return new Producto(
                rs.getInt("id_prod"),
                rs.getString("codigo"),
                rs.getString("nombre"),
                rs.getString("categoria"),
                rs.getDouble("precio"),
                rs.getInt("cantidad"),
                proveedor
        );
    }
}