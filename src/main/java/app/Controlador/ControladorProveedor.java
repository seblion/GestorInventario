package app.Controlador;

import app.Modelo.Proveedor;
import app.Servicio.ServicioProveedor;
import app.Vista.VistaProveedor;

import javax.swing.*;
import java.util.List;

/**
 * Controlador para la gestión de proveedores
 */
public class ControladorProveedor {
    
    private ServicioProveedor servicioProveedor;
    private VistaProveedor vista;
    
    public ControladorProveedor(VistaProveedor vista) {
        this.vista = vista;
        this.servicioProveedor = new ServicioProveedor();
        inicializarEventos();
    }
    
    /**
     * Inicializa los eventos de la vista
     */
    private void inicializarEventos() {
        vista.getBtnRegistrar().addActionListener(e -> registrarProveedor());
        vista.getBtnActualizar().addActionListener(e -> actualizarProveedor());
        vista.getBtnEliminar().addActionListener(e -> eliminarProveedor());
        vista.getBtnBuscar().addActionListener(e -> buscarProveedor());
        vista.getBtnLimpiar().addActionListener(e -> limpiarCampos());
        vista.getBtnListar().addActionListener(e -> listarProveedores());
    }
    
    /**
     * Registra un nuevo proveedor
     */
    private void registrarProveedor() {
        try {
            String nombre = vista.getTxtNombre().getText().trim();
            String contacto = vista.getTxtContacto().getText().trim();
            String codigoProducto = vista.getTxtCodigoProducto().getText().trim();
            
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "El nombre es obligatorio", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Proveedor proveedor = new Proveedor(nombre, contacto, codigoProducto);
            
            if (servicioProveedor.registrarProveedor(proveedor)) {
                JOptionPane.showMessageDialog(vista, "Proveedor registrado exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                listarProveedores();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar el proveedor", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Actualiza un proveedor existente
     */
    private void actualizarProveedor() {
        try {
            int filaSeleccionada = vista.getTablaProveedores().getSelectedRow();
            if (filaSeleccionada < 0) {
                JOptionPane.showMessageDialog(vista, "Seleccione un proveedor de la tabla", 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int id = (int) vista.getTablaProveedores().getValueAt(filaSeleccionada, 0);
            String nombre = vista.getTxtNombre().getText().trim();
            String contacto = vista.getTxtContacto().getText().trim();
            String codigoProducto = vista.getTxtCodigoProducto().getText().trim();
            
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "El nombre es obligatorio", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Proveedor proveedor = new Proveedor(id, nombre, contacto, codigoProducto);
            
            if (servicioProveedor.actualizarProveedor(proveedor)) {
                JOptionPane.showMessageDialog(vista, "Proveedor actualizado exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                listarProveedores();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al actualizar el proveedor", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Elimina un proveedor
     */
    private void eliminarProveedor() {
        try {
            int filaSeleccionada = vista.getTablaProveedores().getSelectedRow();
            if (filaSeleccionada < 0) {
                JOptionPane.showMessageDialog(vista, "Seleccione un proveedor de la tabla", 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String nombre = (String) vista.getTablaProveedores().getValueAt(filaSeleccionada, 1);
            
            int confirmacion = JOptionPane.showConfirmDialog(vista, 
                "¿Está seguro de eliminar el proveedor " + nombre + "?", 
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (servicioProveedor.eliminarProveedor(nombre)) {
                    JOptionPane.showMessageDialog(vista, "Proveedor eliminado exitosamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos();
                    listarProveedores();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al eliminar el proveedor", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Busca un proveedor por nombre
     */
    private void buscarProveedor() {
        try {
            String nombre = vista.getTxtNombre().getText().trim();
            
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Ingrese el nombre del proveedor", 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Proveedor proveedor = servicioProveedor.consultarPorNombre(nombre);
            
            if (proveedor != null) {
                vista.getTxtNombre().setText(proveedor.getNombre());
                vista.getTxtContacto().setText(proveedor.getContacto());
                vista.getTxtCodigoProducto().setText(proveedor.getCodigoProducto());
            } else {
                JOptionPane.showMessageDialog(vista, "Proveedor no encontrado", 
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Lista todos los proveedores en la tabla
     */
    public void listarProveedores() {
        try {
            List<Proveedor> proveedores = servicioProveedor.listarProveedores();
            vista.actualizarTabla(proveedores);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al listar proveedores: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpia los campos del formulario
     */
    private void limpiarCampos() {
        vista.getTxtNombre().setText("");
        vista.getTxtContacto().setText("");
        vista.getTxtCodigoProducto().setText("");
        vista.getTablaProveedores().clearSelection();
    }
}