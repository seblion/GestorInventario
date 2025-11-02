package app.Vista;

import app.Modelo.Producto;
import app.Modelo.Proveedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VistaInventario extends JFrame {
    
    // Componentes para productos
    private JTextField txtCodigo, txtNombre, txtCategoria, txtPrecio, txtCantidad;
    private JComboBox<Proveedor> cmbProveedor;
    private JButton btnRegistrarProducto, btnModificarProducto, btnEliminarProducto;
    private JButton btnBuscarProducto, btnListarProductos, btnLimpiar;
    
    // Componentes para movimientos
    private JTextField txtCodigoMovimiento, txtCantidadMovimiento, txtMotivo;
    private JComboBox<Proveedor> cmbProveedorMovimiento;
    private JButton btnRegistrarEntrada, btnRegistrarSalida;
    
    // Tabla
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    
    public VistaInventario() {
        inicializarComponentes();
        configurarVentana();
    }
    
    private void inicializarComponentes() {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Pestaña de Productos
        JPanel panelProductos = crearPanelProductos();
        tabbedPane.addTab("Gestión de Productos", panelProductos);
        
        // Pestaña de Movimientos
        JPanel panelMovimientos = crearPanelMovimientos();
        tabbedPane.addTab("Movimientos de Inventario", panelMovimientos);
        
        add(tabbedPane);
    }
    
    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCodigo = new JTextField(15);
        panelFormulario.add(txtCodigo, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        panelFormulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtNombre = new JTextField(15);
        panelFormulario.add(txtNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        panelFormulario.add(new JLabel("Categoría:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCategoria = new JTextField(15);
        panelFormulario.add(txtCategoria, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        panelFormulario.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtPrecio = new JTextField(15);
        panelFormulario.add(txtPrecio, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        panelFormulario.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCantidad = new JTextField(15);
        panelFormulario.add(txtCantidad, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.0;
        panelFormulario.add(new JLabel("Proveedor:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        cmbProveedor = new JComboBox<>();
        panelFormulario.add(cmbProveedor, gbc);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnRegistrarProducto = new JButton("Registrar");
        btnModificarProducto = new JButton("Modificar");
        btnEliminarProducto = new JButton("Eliminar");
        btnBuscarProducto = new JButton("Buscar");
        btnListarProductos = new JButton("Listar");
        btnLimpiar = new JButton("Limpiar");
        
        panelBotones.add(btnRegistrarProducto);
        panelBotones.add(btnModificarProducto);
        panelBotones.add(btnEliminarProducto);
        panelBotones.add(btnBuscarProducto);
        panelBotones.add(btnListarProductos);
        panelBotones.add(btnLimpiar);
        
        // Tabla
        String[] columnas = {"ID", "Código", "Nombre", "Categoría", "Precio", "Stock", "Proveedor"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaProductos.getSelectedRow() >= 0) {
                cargarDatosSeleccionados();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        
        panel.add(panelFormulario, BorderLayout.NORTH);
        panel.add(panelBotones, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelMovimientos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar Movimiento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Código Producto:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCodigoMovimiento = new JTextField(20);
        panelFormulario.add(txtCodigoMovimiento, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        panelFormulario.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCantidadMovimiento = new JTextField(20);
        panelFormulario.add(txtCantidadMovimiento, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        panelFormulario.add(new JLabel("Motivo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMotivo = new JTextField(20);
        panelFormulario.add(txtMotivo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        panelFormulario.add(new JLabel("Proveedor (Entrada):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbProveedorMovimiento = new JComboBox<>();
        panelFormulario.add(cmbProveedorMovimiento, gbc);
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnRegistrarEntrada = new JButton("Registrar Entrada");
        btnRegistrarSalida = new JButton("Registrar Salida");
        
        panelBotones.add(btnRegistrarEntrada);
        panelBotones.add(btnRegistrarSalida);
        
        panel.add(panelFormulario, BorderLayout.NORTH);
        panel.add(panelBotones, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void cargarDatosSeleccionados() {
        int fila = tablaProductos.getSelectedRow();
        txtCodigo.setText(tablaProductos.getValueAt(fila, 1).toString());
        txtNombre.setText(tablaProductos.getValueAt(fila, 2).toString());
        
        Object categoria = tablaProductos.getValueAt(fila, 3);
        txtCategoria.setText(categoria != null ? categoria.toString() : "");
        
        txtPrecio.setText(tablaProductos.getValueAt(fila, 4).toString());
        txtCantidad.setText(tablaProductos.getValueAt(fila, 5).toString());
    }
    
    public void actualizarTablaProductos(List<Producto> productos) {
        modeloTabla.setRowCount(0);
        for (Producto p : productos) {
            Object[] fila = {
                p.getId(),
                p.getCodigo(),
                p.getNombre(),
                p.getCategoria(),
                String.format("%.2f", p.getPrecio()),
                p.getCantidad(),
                p.getProveedor() != null ? p.getProveedor().getNombre() : ""
            };
            modeloTabla.addRow(fila);
        }
    }
    
    public void cargarProveedores(List<Proveedor> proveedores) {
        cmbProveedor.removeAllItems();
        cmbProveedorMovimiento.removeAllItems();
        
        cmbProveedor.addItem(null);
        cmbProveedorMovimiento.addItem(null);
        
        for (Proveedor p : proveedores) {
            cmbProveedor.addItem(p);
            cmbProveedorMovimiento.addItem(p);
        }
    }
    
    private void configurarVentana() {
        setTitle("Sistema de Gestión de Inventario");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    // Getters
    public JTextField getTxtCodigo() { return txtCodigo; }
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtCategoria() { return txtCategoria; }
    public JTextField getTxtPrecio() { return txtPrecio; }
    public JTextField getTxtCantidad() { return txtCantidad; }
    public JComboBox<Proveedor> getCmbProveedor() { return cmbProveedor; }
    public JButton getBtnRegistrarProducto() { return btnRegistrarProducto; }
    public JButton getBtnModificarProducto() { return btnModificarProducto; }
    public JButton getBtnEliminarProducto() { return btnEliminarProducto; }
    public JButton getBtnBuscarProducto() { return btnBuscarProducto; }
    public JButton getBtnListarProductos() { return btnListarProductos; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JTextField getTxtCodigoMovimiento() { return txtCodigoMovimiento; }
    public JTextField getTxtCantidadMovimiento() { return txtCantidadMovimiento; }
    public JTextField getTxtMotivo() { return txtMotivo; }
    public JComboBox<Proveedor> getCmbProveedorMovimiento() { return cmbProveedorMovimiento; }
    public JButton getBtnRegistrarEntrada() { return btnRegistrarEntrada; }
    public JButton getBtnRegistrarSalida() { return btnRegistrarSalida; }
    public JTable getTablaProductos() { return tablaProductos; }
}