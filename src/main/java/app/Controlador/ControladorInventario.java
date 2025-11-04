package app.Controlador;

import app.Modelo.Producto;
import app.Modelo.Proveedor;
import app.Servicio.ServicioInventario;
import app.Servicio.ServicioProveedor;
import app.Vista.VistaInventario;

import javax.swing.*;
import java.util.Date;
import java.util.List;

/**
 * Controlador para la gestión del inventario
 */
public class ControladorInventario {
    
    private ServicioInventario servicioInventario;
    private ServicioProveedor servicioProveedor;
    private VistaInventario vista;
    
    public ControladorInventario(VistaInventario vista) {
        this.vista = vista;
        this.servicioInventario = new ServicioInventario();
        this.servicioProveedor = new ServicioProveedor();
        inicializarEventos();
    }
    
    private void inicializarEventos() {
        vista.getBtnRegistrarProducto().addActionListener(e -> registrarProducto());
        vista.getBtnModificarProducto().addActionListener(e -> modificarProducto());
        vista.getBtnEliminarProducto().addActionListener(e -> eliminarProducto());
        vista.getBtnRegistrarEntrada().addActionListener(e -> registrarEntrada());
        vista.getBtnRegistrarSalida().addActionListener(e -> registrarSalida());
        vista.getBtnBuscarProducto().addActionListener(e -> buscarProducto());
        vista.getBtnListarProductos().addActionListener(e -> listarProductos());
        vista.getBtnLimpiar().addActionListener(e -> limpiarCampos());
        
        // Cuando se selecciona un producto en la tabla, mostrar sus movimientos
        vista.getTablaProductos().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && vista.getTablaProductos().getSelectedRow() >= 0) {
                int fila = vista.getTablaProductos().getSelectedRow();
                int id = (int) vista.getTablaProductos().getValueAt(fila, 0);
                try {
                    List<app.Modelo.MovimientoInventario> movimientos = servicioInventario.listarMovimientosPorProducto(id);
                    vista.actualizarTablaMovimientos(movimientos);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vista, "Error al cargar movimientos: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private void registrarProducto() {
        try {
            String codigo = vista.getTxtCodigo().getText().trim();
            String nombre = vista.getTxtNombre().getText().trim();
            String categoria = vista.getTxtCategoria().getText().trim();
            double precio = parsePrecio(vista.getTxtPrecio().getText());
            int cantidad = Integer.parseInt(vista.getTxtCantidad().getText());
            
            if (codigo.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Código y nombre son obligatorios", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

                      
            Proveedor proveedor = null;
            if (vista.getCmbProveedor().getSelectedItem() != null) {
                proveedor = (Proveedor) vista.getCmbProveedor().getSelectedItem();
            }
            
            Producto producto = new Producto(codigo, nombre, categoria, precio, cantidad);
            producto.setProveedor(proveedor);
            
            if (servicioInventario.registrarProducto(producto)) {
                JOptionPane.showMessageDialog(vista, "Producto registrado exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                listarProductos();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar el producto", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "Error en los valores numéricos", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void modificarProducto() {
        try {
            int filaSeleccionada = vista.getTablaProductos().getSelectedRow();
            if (filaSeleccionada < 0) {
                JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla", 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int id = (int) vista.getTablaProductos().getValueAt(filaSeleccionada, 0);
            String codigo = vista.getTxtCodigo().getText().trim();
            String nombre = vista.getTxtNombre().getText().trim();
            String categoria = vista.getTxtCategoria().getText().trim();
            double precio = parsePrecio(vista.getTxtPrecio().getText());
            int cantidad = Integer.parseInt(vista.getTxtCantidad().getText());
            
            Proveedor proveedor = null;
            if (vista.getCmbProveedor().getSelectedItem() != null) {
                proveedor = (Proveedor) vista.getCmbProveedor().getSelectedItem();
            }
            
            Producto producto = new Producto(id, codigo, nombre, categoria, precio, cantidad, proveedor);
            
            if (servicioInventario.modificarProducto(producto)) {
                JOptionPane.showMessageDialog(vista, "Producto actualizado exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                listarProductos();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al actualizar el producto", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarProducto() {
        try {
            int filaSeleccionada = vista.getTablaProductos().getSelectedRow();
            if (filaSeleccionada < 0) {
                JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla", 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String codigo = (String) vista.getTablaProductos().getValueAt(filaSeleccionada, 1);
            
            int confirmacion = JOptionPane.showConfirmDialog(vista, 
                "¿Está seguro de eliminar el producto " + codigo + "?", 
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (servicioInventario.eliminarProducto(codigo)) {
                    JOptionPane.showMessageDialog(vista, "Producto eliminado exitosamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos();
                    listarProductos();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al eliminar el producto", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void registrarEntrada() {
        try {
            String codigo = vista.getTxtCodigoMovimiento().getText().trim();
            int cantidad = Integer.parseInt(vista.getTxtCantidadMovimiento().getText());
            String motivo = vista.getTxtMotivo().getText().trim();
            
            Producto producto = servicioInventario.consultarProductoPorCodigo(codigo);
            if (producto == null) {
                JOptionPane.showMessageDialog(vista, "Producto no encontrado", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (servicioInventario.registrarEntrada(producto, cantidad, new Date(), motivo)) {
                JOptionPane.showMessageDialog(vista, "Entrada registrada exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                listarProductos();
                listarMovimientos();
                limpiarCamposMovimiento();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar la entrada", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void registrarSalida() {
        try {
            String codigo = vista.getTxtCodigoMovimiento().getText().trim();
            int cantidad = Integer.parseInt(vista.getTxtCantidadMovimiento().getText());
            String motivo = vista.getTxtMotivo().getText().trim();
            
            Producto producto = servicioInventario.consultarProductoPorCodigo(codigo);
            if (producto == null) {
                JOptionPane.showMessageDialog(vista, "Producto no encontrado", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (servicioInventario.registrarSalida(producto, cantidad, new Date(), motivo)) {
                JOptionPane.showMessageDialog(vista, "Salida registrada exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                listarProductos();
                listarMovimientos();
                limpiarCamposMovimiento();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar la salida. Verifique el stock", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Lista todos los movimientos y actualiza la vista
     */
    public void listarMovimientos() {
        try {
            List<app.Modelo.MovimientoInventario> movimientos = servicioInventario.listarMovimientos();
            vista.actualizarTablaMovimientos(movimientos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al listar movimientos: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarProducto() {
        try {
            String codigo = vista.getTxtCodigo().getText().trim();
            
            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Ingrese el código del producto", 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Producto producto = servicioInventario.consultarProductoPorCodigo(codigo);
            
            if (producto != null) {
                vista.getTxtCodigo().setText(producto.getCodigo());
                vista.getTxtNombre().setText(producto.getNombre());
                vista.getTxtCategoria().setText(producto.getCategoria());
                vista.getTxtPrecio().setText(formatearPrecio(producto.getPrecio()));
                vista.getTxtCantidad().setText(String.valueOf(producto.getCantidad()));
                
                if (producto.getProveedor() != null) {
                    vista.getCmbProveedor().setSelectedItem(producto.getProveedor());
                }
            } else {
                JOptionPane.showMessageDialog(vista, "Producto no encontrado", 
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void listarProductos() {
        try {
            List<Producto> productos = servicioInventario.consultarStock();
            vista.actualizarTablaProductos(productos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al listar productos: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void cargarProveedores() {
        try {
            List<Proveedor> proveedores = servicioProveedor.listarProveedores();
            vista.cargarProveedores(proveedores);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar proveedores: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarCampos() {
        vista.getTxtCodigo().setText("");
        vista.getTxtNombre().setText("");
        vista.getTxtCategoria().setText("");
        vista.getTxtPrecio().setText("");
        vista.getTxtCantidad().setText("");
        vista.getCmbProveedor().setSelectedIndex(0);
        vista.getTablaProductos().clearSelection();
    }
    
    private void limpiarCamposMovimiento() {
        vista.getTxtCodigoMovimiento().setText("");
        vista.getTxtCantidadMovimiento().setText("");
        vista.getTxtMotivo().setText("");
    }

    private String formatearPrecio(double precio) {
    return String.format("%.2f", precio).replace(',', '.');
}

    /**
     * Convierte un string con precio a double, aceptando tanto punto como coma
     */
    private double parsePrecio(String precioStr) throws NumberFormatException {
        if (precioStr == null || precioStr.trim().isEmpty()) {
            throw new NumberFormatException("El precio no puede estar vacío");
        }
        String precioNormalizado = precioStr.trim().replace(',', '.');
        return Double.parseDouble(precioNormalizado);
    }
}