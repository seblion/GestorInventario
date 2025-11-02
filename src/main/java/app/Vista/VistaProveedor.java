package app.Vista;

import app.Modelo.Proveedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vista para la gestión de proveedores
 */
public class VistaProveedor extends JFrame {
    
    private JTextField txtNombre, txtContacto, txtCodigoProducto;
    private JButton btnRegistrar, btnActualizar, btnEliminar, btnBuscar, btnLimpiar, btnListar;
    private JTable tablaProveedores;
    private DefaultTableModel modeloTabla;
    
    public VistaProveedor() {
        inicializarComponentes();
        configurarVentana();
    }
    
    private void inicializarComponentes() {
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de formulario
        JPanel panelFormulario = crearPanelFormulario();
        
        // Panel de botones
        JPanel panelBotones = crearPanelBotones();
        
        // Panel de tabla
        JPanel panelTabla = crearPanelTabla();
        
        // Agregar componentes al panel principal
        panelPrincipal.add(panelFormulario, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        panelPrincipal.add(panelTabla, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Proveedor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 1.0;
        txtNombre = new JTextField(20);
        panel.add(txtNombre, gbc);
        
        // Contacto
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Contacto:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.weightx = 1.0;
        txtContacto = new JTextField(20);
        panel.add(txtContacto, gbc);
        
        // Código de Producto
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Código Producto:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.weightx = 1.0;
        txtCodigoProducto = new JTextField(20);
        panel.add(txtCodigoProducto, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnRegistrar = new JButton("Registrar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnBuscar = new JButton("Buscar");
        btnLimpiar = new JButton("Limpiar");
        btnListar = new JButton("Listar Todos");
        
        panel.add(btnRegistrar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        panel.add(btnBuscar);
        panel.add(btnLimpiar);
        panel.add(btnListar);
        
        return panel;
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Proveedores"));
        
        String[] columnas = {"ID", "Nombre", "Contacto", "Código Producto"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaProveedores = new JTable(modeloTabla);
        tablaProveedores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Listener para cargar datos en el formulario al seleccionar una fila
        tablaProveedores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaProveedores.getSelectedRow() >= 0) {
                cargarDatosSeleccionados();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaProveedores);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void cargarDatosSeleccionados() {
        int fila = tablaProveedores.getSelectedRow();
        txtNombre.setText(tablaProveedores.getValueAt(fila, 1).toString());
        
        Object contacto = tablaProveedores.getValueAt(fila, 2);
        txtContacto.setText(contacto != null ? contacto.toString() : "");
        
        Object codigoProducto = tablaProveedores.getValueAt(fila, 3);
        txtCodigoProducto.setText(codigoProducto != null ? codigoProducto.toString() : "");
    }
    
    public void actualizarTabla(List<Proveedor> proveedores) {
        modeloTabla.setRowCount(0);
        for (Proveedor p : proveedores) {
            Object[] fila = {
                p.getId(),
                p.getNombre(),
                p.getContacto(),
                p.getCodigoProducto()
            };
            modeloTabla.addRow(fila);
        }
    }
    
    private void configurarVentana() {
        setTitle("Gestión de Proveedores");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    // Getters
    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtContacto() { return txtContacto; }
    public JTextField getTxtCodigoProducto() { return txtCodigoProducto; }
    public JButton getBtnRegistrar() { return btnRegistrar; }
    public JButton getBtnActualizar() { return btnActualizar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }
    public JButton getBtnListar() { return btnListar; }
    public JTable getTablaProveedores() { return tablaProveedores; }
}