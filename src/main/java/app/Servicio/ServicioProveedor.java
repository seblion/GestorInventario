package app.Servicio;

import app.DAO.ProveedorDAO;
import app.Modelo.Proveedor;

import java.util.List;

/**
 * Servicio para manejar la lógica de negocio de Proveedores
 */
public class ServicioProveedor {
    
    private ProveedorDAO proveedorDAO;
    
    public ServicioProveedor() {
        this.proveedorDAO = new ProveedorDAO();
    }
    
    /**
     * Registra un nuevo proveedor
     */
    public boolean registrarProveedor(Proveedor proveedor) throws Exception {
        if (proveedor == null) {
            System.err.println("El proveedor no puede ser nulo");
            return false;
        }
        
        if (proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()) {
            System.err.println("El nombre del proveedor es obligatorio");
            return false;
        }
        
        // Verificar que no exista un proveedor con el mismo nombre
        Proveedor existente = proveedorDAO.consultarPorNombre(proveedor.getNombre());
        if (existente != null) {
            System.err.println("Ya existe un proveedor con ese nombre");
            return false;
        }
        
        return proveedorDAO.registrarProveedor(proveedor);
    }
    
    /**
     * Actualiza un proveedor existente
     */
    public boolean actualizarProveedor(Proveedor proveedor) throws Exception {
        if (proveedor == null || proveedor.getId() <= 0) {
            System.err.println("Proveedor inválido");
            return false;
        }
        
        if (proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()) {
            System.err.println("El nombre del proveedor es obligatorio");
            return false;
        }
        
        return proveedorDAO.actualizarProveedor(proveedor);
    }
    
    /**
     * Elimina un proveedor por nombre
     */
    public boolean eliminarProveedor(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("El nombre es obligatorio");
            return false;
        }
        
        Proveedor proveedor = proveedorDAO.consultarPorNombre(nombre);
        if (proveedor == null) {
            System.err.println("Proveedor no encontrado");
            return false;
        }
        
        return proveedorDAO.eliminarProveedor(nombre);
    }
    
    /**
     * Consulta un proveedor por nombre
     */
    public Proveedor consultarPorNombre(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("El nombre es obligatorio");
            return null;
        }
        
        return proveedorDAO.consultarPorNombre(nombre);
    }
    
    /**
     * Consulta un proveedor por ID
     */
    public Proveedor consultarPorId(int id) throws Exception {
        if (id <= 0) {
            System.err.println("ID inválido");
            return null;
        }
        
        return proveedorDAO.consultarPorId(id);
    }
    
    /**
     * Lista todos los proveedores
     */
    public List<Proveedor> listarProveedores() throws Exception {
        return proveedorDAO.listarTodos();
    }
    
    /**
     * Verifica si existe un proveedor con el nombre especificado
     */
    public boolean existeProveedor(String nombre) throws Exception {
        return proveedorDAO.consultarPorNombre(nombre) != null;
    }
}