/**
 * Universidad Autonoma Nacional de Mexico
 * FACULTAD DE CONTADURÍA Y ADMINISTRACION
 * INFORMATICA A DISTANCIA
 * Programa creado por Daniel Hernandez Hernandez el 05/01/2026
 * Asignatura: Programación orientada a objetos-2430
 * Profesor: Diana Karen Herrera Carrillo
 *
 * Programa: Persistencia de Análisis con JDBC y Transacciones
 *
 * Este programa analiza un archivo de texto, calcula métricas (líneas, palabras,
 * caracteres) y las almacena en una base de datos usando transacciones JDBC.
 * Implementa control de transacciones con commit y rollback para garantizar
 * la integridad de los datos.
 *
 * Programador: Daniel Hernandez Hernandez
 * Fecha: 05/01/2026
 */

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.*;
import java.text.SimpleDateFormat;

public class PersistirAnalisisJDBC extends JFrame {

    // Configuración de la base de datos H2 (en memoria)
    private static final String DB_URL = "jdbc:h2:~/analisis_db";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private File archivoSeleccionado;
    private JLabel lblArchivo;
    private JTextArea txtResultados;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    private JButton btnSeleccionar;
    private JButton btnAnalizar;
    private JButton btnVerHistorial;
    private JButton btnLimpiar;

    /**
     * Constructor principal que inicializa la interfaz y la base de datos
     */
    public PersistirAnalisisJDBC() {
        super("Persistencia de Analisis - JDBC");
        inicializarBaseDatos();
        initUI();
    }

    /**
     * Inicializa la base de datos y crea la tabla si no existe
     */
    private void inicializarBaseDatos() {
        String createTableSQL =
                "CREATE TABLE IF NOT EXISTS analisis_texto (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "lineas INT NOT NULL, " +
                        "palabras INT NOT NULL, " +
                        "caracteres INT NOT NULL, " +
                        "fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")";

        Connection conn = null;
        Statement stmt = null;

        try {
            // Cargar el driver de H2
            Class.forName("org.h2.Driver");

            // Conectar a la base de datos
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            stmt = conn.createStatement();

            // Crear la tabla
            stmt.execute(createTableSQL);

            System.out.println("Base de datos inicializada correctamente.");

        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: No se encontro el driver de H2.");
            System.err.println("Asegurate de tener h2-*.jar en el classpath.");
            JOptionPane.showMessageDialog(null,
                    "No se encontro el driver de la base de datos.\n" +
                            "Necesitas agregar h2-*.jar al proyecto.\n\n" +
                            "Descargalo de: https://www.h2database.com/",
                    "Error de Driver",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (SQLException e) {
            System.err.println("ERROR al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrarRecursos(conn, stmt, null);
        }
    }

    /**
     * Inicializa la interfaz gráfica
     */
    private void initUI() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel superior: Selección de archivo
        JPanel panelSuperior = crearPanelSeleccion();
        add(panelSuperior, BorderLayout.NORTH);

        // Panel central: Pestañas con resultados e historial
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Resultados del Analisis", crearPanelResultados());
        tabbedPane.addTab("Historial en Base de Datos", crearPanelHistorial());
        add(tabbedPane, BorderLayout.CENTER);

        // Panel inferior: Botones
        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Crea el panel de selección de archivo
     */
    private JPanel crearPanelSeleccion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Archivo a Analizar"));

        btnSeleccionar = new JButton("Seleccionar archivo de texto...");
        btnSeleccionar.addActionListener(e -> seleccionarArchivo());

        lblArchivo = new JLabel("Ningun archivo seleccionado");
        lblArchivo.setForeground(Color.GRAY);
        lblArchivo.setFont(new Font("Arial", Font.ITALIC, 12));

        panel.add(btnSeleccionar, BorderLayout.WEST);
        panel.add(lblArchivo, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea el panel de resultados del análisis
     */
    private JPanel crearPanelResultados() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        txtResultados = new JTextArea();
        txtResultados.setEditable(false);
        txtResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtResultados.setText("Seleccione un archivo para analizar...");

        JScrollPane scroll = new JScrollPane(txtResultados);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea el panel con la tabla de historial
     */
    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Crear tabla con modelo
        String[] columnas = {"ID", "Lineas", "Palabras", "Caracteres", "Fecha"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editable
            }
        };

        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaHistorial.getTableHeader().setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tablaHistorial);
        panel.add(scroll, BorderLayout.CENTER);

        // Botón para actualizar la tabla
        JButton btnActualizar = new JButton("Actualizar Historial");
        btnActualizar.addActionListener(e -> cargarHistorial());
        panel.add(btnActualizar, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Crea el panel de botones de acción
     */
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnAnalizar = new JButton("Analizar y Guardar en BD");
        btnAnalizar.setFont(new Font("Arial", Font.BOLD, 12));
        btnAnalizar.addActionListener(e -> analizarYGuardar());

        btnVerHistorial = new JButton("Ver Historial Completo");
        btnVerHistorial.addActionListener(e -> cargarHistorial());

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiar());

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(e -> {
            cerrarConexiones();
            System.exit(0);
        });

        panel.add(btnAnalizar);
        panel.add(btnVerHistorial);
        panel.add(btnLimpiar);
        panel.add(btnSalir);

        return panel;
    }

    /**
     * Abre el selector de archivos
     */
    private void seleccionarArchivo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar archivo de texto");
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Archivos de texto (*.txt, *.md, *.csv, *.log)", "txt", "md", "csv", "log"));

        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = chooser.getSelectedFile();
            lblArchivo.setText("Archivo: " + archivoSeleccionado.getName());
            lblArchivo.setForeground(new Color(0, 128, 0));
            txtResultados.setText("Archivo seleccionado: " + archivoSeleccionado.getAbsolutePath() + "\n" +
                    "Presione 'Analizar y Guardar en BD' para procesar.");
        }
    }

    /**
     * Analiza el archivo y guarda los resultados en la base de datos usando transacciones
     */
    private void analizarYGuardar() {
        if (archivoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un archivo primero.",
                    "Archivo no seleccionado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // 1. Analizar el archivo
            int[] metricas = analizarArchivo(archivoSeleccionado);
            int lineas = metricas[0];
            int palabras = metricas[1];
            int caracteres = metricas[2];

            // Mostrar resultados en el área de texto
            StringBuilder resultado = new StringBuilder();
            resultado.append("=".repeat(60)).append("\n");
            resultado.append("ANALISIS COMPLETADO\n");
            resultado.append("=".repeat(60)).append("\n");
            resultado.append("Archivo: ").append(archivoSeleccionado.getName()).append("\n");
            resultado.append("Ruta: ").append(archivoSeleccionado.getAbsolutePath()).append("\n\n");
            resultado.append("METRICAS CALCULADAS:\n");
            resultado.append("-".repeat(60)).append("\n");
            resultado.append("Lineas: ").append(lineas).append("\n");
            resultado.append("Palabras: ").append(palabras).append("\n");
            resultado.append("Caracteres: ").append(caracteres).append("\n\n");

            // 2. Conectar a la base de datos
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 3. IMPORTANTE: Desactivar auto-commit para usar transacciones
            conn.setAutoCommit(false);
            resultado.append("Conectado a la base de datos.\n");
            resultado.append("Modo de transaccion activado (autoCommit = false)\n\n");

            // 4. Preparar la inserción
            String insertSQL = "INSERT INTO analisis_texto (lineas, palabras, caracteres) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, lineas);
            pstmt.setInt(2, palabras);
            pstmt.setInt(3, caracteres);

            // 5. Ejecutar la inserción
            int filasAfectadas = pstmt.executeUpdate();
            resultado.append("Ejecutando INSERT...\n");

            if (filasAfectadas > 0) {
                // 6. COMMIT: Confirmar la transacción
                conn.commit();
                resultado.append("COMMIT ejecutado exitosamente.\n");
                resultado.append("Registro guardado en la base de datos.\n\n");

                // Obtener el ID generado
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    resultado.append("ID del registro: ").append(idGenerado).append("\n");
                }
                rs.close();

                resultado.append("=".repeat(60)).append("\n");
                resultado.append("TRANSACCION COMPLETADA CON EXITO\n");
                resultado.append("=".repeat(60));

                txtResultados.setText(resultado.toString());

                JOptionPane.showMessageDialog(this,
                        "Analisis guardado correctamente en la base de datos.\n\n" +
                                "Lineas: " + lineas + "\n" +
                                "Palabras: " + palabras + "\n" +
                                "Caracteres: " + caracteres,
                        "Guardado Exitoso",
                        JOptionPane.INFORMATION_MESSAGE);

                // Actualizar la tabla de historial
                cargarHistorial();

            } else {
                throw new SQLException("No se inserto ningun registro.");
            }

        } catch (IOException e) {
            txtResultados.append("\nERROR al leer el archivo: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error al leer el archivo:\n" + e.getMessage(),
                    "Error de Lectura",
                    JOptionPane.ERROR_MESSAGE);

        } catch (SQLException e) {
            // 7. ROLLBACK: Si hay error, deshacer la transacción
            txtResultados.append("\nERROR en la base de datos: " + e.getMessage() + "\n");
            txtResultados.append("Ejecutando ROLLBACK...\n");

            if (conn != null) {
                try {
                    conn.rollback();
                    txtResultados.append("ROLLBACK ejecutado. Transaccion cancelada.\n");
                } catch (SQLException ex) {
                    txtResultados.append("ERROR al hacer ROLLBACK: " + ex.getMessage() + "\n");
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Error al guardar en la base de datos:\n" + e.getMessage() +
                            "\n\nLa transaccion fue cancelada (ROLLBACK).",
                    "Error de Base de Datos",
                    JOptionPane.ERROR_MESSAGE);

        } finally {
            // 8. Restaurar auto-commit y cerrar recursos
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Error al restaurar autoCommit: " + e.getMessage());
                }
            }
            cerrarRecursos(conn, pstmt, null);
        }
    }

    /**
     * Analiza un archivo de texto y calcula las métricas
     */
    private int[] analizarArchivo(File archivo) throws IOException {
        int lineas = 0;
        int palabras = 0;
        int caracteresSinSaltos = 0;

        try (BufferedReader br = Files.newBufferedReader(archivo.toPath(), StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas++;
                String textoTrim = linea.trim();
                if (!textoTrim.isEmpty()) {
                    String[] partes = textoTrim.split("\\s+");
                    palabras += partes.length;
                }
                caracteresSinSaltos += linea.length();
            }
        } catch (IOException e) {
            // Fallback con charset por defecto
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    lineas++;
                    String textoTrim = linea.trim();
                    if (!textoTrim.isEmpty()) {
                        String[] partes = textoTrim.split("\\s+");
                        palabras += partes.length;
                    }
                    caracteresSinSaltos += linea.length();
                }
            }
        }

        int saltos = (lineas > 0) ? (lineas - 1) : 0;
        int caracteres = caracteresSinSaltos + saltos;
        return new int[]{lineas, palabras, caracteres};
    }

    /**
     * Carga el historial completo desde la base de datos
     */
    private void cargarHistorial() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            stmt = conn.createStatement();

            String selectSQL = "SELECT id, lineas, palabras, caracteres, fecha FROM analisis_texto ORDER BY id DESC";
            rs = stmt.executeQuery(selectSQL);

            // Limpiar tabla
            modeloTabla.setRowCount(0);

            // Formato de fecha
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            // Llenar tabla con los resultados
            while (rs.next()) {
                int id = rs.getInt("id");
                int lineas = rs.getInt("lineas");
                int palabras = rs.getInt("palabras");
                int caracteres = rs.getInt("caracteres");
                Timestamp fecha = rs.getTimestamp("fecha");

                modeloTabla.addRow(new Object[]{
                        id,
                        lineas,
                        palabras,
                        caracteres,
                        sdf.format(fecha)
                });
            }

            if (modeloTabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No hay registros en la base de datos.",
                        "Historial Vacio",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar el historial:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            cerrarRecursos(conn, stmt, rs);
        }
    }

    /**
     * Limpia la interfaz
     */
    private void limpiar() {
        archivoSeleccionado = null;
        lblArchivo.setText("Ningun archivo seleccionado");
        lblArchivo.setForeground(Color.GRAY);
        txtResultados.setText("Seleccione un archivo para analizar...");
    }

    /**
     * Cierra las conexiones de base de datos
     */
    private void cerrarConexiones() {
        // Las conexiones se cierran automáticamente en cada método
        System.out.println("Cerrando aplicacion...");
    }

    /**
     * Cierra recursos de JDBC de forma segura
     */
    private void cerrarRecursos(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }

    /**
     * Método principal
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            PersistirAnalisisJDBC app = new PersistirAnalisisJDBC();
            app.setVisible(true);
        });
    }
}

// Programador: Daniel Hernandez Hernandez
// Programa: Persistencia de Analisis con JDBC
// Fecha: 05 de enero de 2026
