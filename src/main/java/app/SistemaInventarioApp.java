package app;

import app.Controlador.ControladorInventario;
import app.Controlador.ControladorProveedor;
import app.Vista.VistaInventario;
import app.Vista.VistaProveedor;

import javax.swing.*;
import java.awt.*;

/**
 * Clase principal del sistema de inventario
 */
public class SistemaInventarioApp extends JFrame {
    
    public SistemaInventarioApp() {
        inicializarMenuPrincipal();
        configurarVentana();
    }
    
    private void inicializarMenuPrincipal() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título
        JLabel lblTitulo = new JLabel("Sistema de Gestión de Inventario");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        
        // Botones del menú
        JButton btnInventario = new JButton("Gestión de Inventario");
        btnInventario.setPreferredSize(new Dimension(250, 50));
        btnInventario.setFont(new Font("Arial", Font.PLAIN, 16));
        btnInventario.addActionListener(e -> abrirGestionInventario());
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(btnInventario, gbc);
        
        JButton btnProveedores = new JButton("Gestión de Proveedores");
        btnProveedores.setPreferredSize(new Dimension(250, 50));
        btnProveedores.setFont(new Font("Arial", Font.PLAIN, 16));
        btnProveedores.addActionListener(e -> abrirGestionProveedores());
        gbc.gridy = 3;
        panel.add(btnProveedores, gbc);
        
        JButton btnSalir = new JButton("Salir");
        btnSalir.setPreferredSize(new Dimension(250, 50));
        btnSalir.setFont(new Font("Arial", Font.PLAIN, 16));
        btnSalir.addActionListener(e -> salir());
        gbc.gridy = 4;
        panel.add(btnSalir, gbc);
        
        add(panel);
    }
    
    private void abrirGestionInventario() {
        VistaInventario vista = new VistaInventario();
        ControladorInventario controlador = new ControladorInventario(vista);
        controlador.cargarProveedores();
        controlador.listarProductos();
        vista.setVisible(true);
    }
    
    private void abrirGestionProveedores() {
        VistaProveedor vista = new VistaProveedor();
        ControladorProveedor controlador = new ControladorProveedor(vista);
        controlador.listarProveedores();
        vista.setVisible(true);
    }
    
    private void salir() {
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de salir del sistema?",
            "Confirmar salida",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    private void configurarVentana() {
        setTitle("Sistema de Inventario - Menú Principal");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        // Configurar el Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Ejecutar en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            SistemaInventarioApp app = new SistemaInventarioApp();
            app.setVisible(true);
        });
    }
}