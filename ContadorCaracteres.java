/**
 * Universidad Autonoma Nacional de Mexico
 * FACULTAD DE CONTADURÍA Y ADMINISTRACION
 * INFORMATICA A DISTANCIA
 * Programa creado por Daniel Hernandez Hernandez el 19/11/2025
 * Asignatura: Programación orientada a objetos-2430
 * Profesor: Diana Karen Herrera Carrillo
 *
 * Programa: Contador de Caracteres
 *
 * Este programa lee un archivo de texto, solicita al usuario un carácter
 * específico y cuenta cuántas veces aparece ese carácter en el archivo.
 * Maneja excepciones de I/O y muestra los resultados en interfaz gráfica.
 *
 * Programador: Daniel Hernandez Hernandez
 * Fecha: 19/11/2025
 */

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ContadorCaracteres extends JFrame {

    private File archivoSeleccionado;
    private JLabel lblArchivo;
    private JTextField txtCaracter;
    private JTextArea txtResultados;
    private JButton btnSeleccionarArchivo;
    private JButton btnContar;
    private JButton btnLimpiar;

    /**
     * Constructor principal que inicializa la interfaz
     */
    public ContadorCaracteres() {
        super("Contador de Caracteres");
        initUI();
    }

    /**
     * Inicializa la interfaz gráfica y todos sus componentes
     */
    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel superior: Selección de archivo
        JPanel panelArchivo = crearPanelArchivo();
        add(panelArchivo, BorderLayout.NORTH);

        // Panel central: Entrada de carácter y resultados
        JPanel panelCentral = crearPanelCentral();
        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior: Botones de acción
        JPanel panelBotones = crearPanelBotones();
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Crea el panel de selección de archivo
     */
    private JPanel crearPanelArchivo() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Seleccion de Archivo"));

        lblArchivo = new JLabel("Ningun archivo seleccionado");
        lblArchivo.setForeground(Color.GRAY);
        lblArchivo.setFont(new Font("Arial", Font.ITALIC, 12));

        btnSeleccionarArchivo = new JButton("Seleccionar Archivo de Texto");
        btnSeleccionarArchivo.setFont(new Font("Arial", Font.BOLD, 12));
        btnSeleccionarArchivo.addActionListener(e -> seleccionarArchivo());

        panel.add(btnSeleccionarArchivo, BorderLayout.WEST);
        panel.add(lblArchivo, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea el panel central con entrada de carácter y área de resultados
     */
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Panel de entrada de carácter
        JPanel panelEntrada = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEntrada.setBorder(BorderFactory.createTitledBorder("Caracter a Buscar"));

        JLabel lblCaracter = new JLabel("Ingrese un caracter: ");
        txtCaracter = new JTextField(5);
        txtCaracter.setFont(new Font("Monospaced", Font.BOLD, 14));

        panelEntrada.add(lblCaracter);
        panelEntrada.add(txtCaracter);

        // Área de resultados
        txtResultados = new JTextArea();
        txtResultados.setEditable(false);
        txtResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtResultados.setLineWrap(true);
        txtResultados.setWrapStyleWord(true);
        txtResultados.setText("Seleccione un archivo y un caracter para comenzar...");

        JScrollPane scroll = new JScrollPane(txtResultados);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados"));

        panel.add(panelEntrada, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea el panel de botones de acción
     */
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnContar = new JButton("Contar Apariciones");
        btnContar.setFont(new Font("Arial", Font.BOLD, 12));
        btnContar.addActionListener(e -> contarCaracteres());

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnLimpiar.addActionListener(e -> limpiarTodo());

        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Arial", Font.PLAIN, 12));
        btnSalir.addActionListener(e -> System.exit(0));

        panel.add(btnContar);
        panel.add(btnLimpiar);
        panel.add(btnSalir);

        return panel;
    }

    /**
     * Abre el selector de archivo y carga el archivo seleccionado
     */
    private void seleccionarArchivo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar archivo de texto");
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Archivos de texto (*.txt, *.md, *.csv, *.log)", "txt", "text", "md", "csv", "log"));

        int resultado = chooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = chooser.getSelectedFile();
            lblArchivo.setText("Archivo: " + archivoSeleccionado.getName());
            lblArchivo.setForeground(new Color(0, 128, 0));
            txtResultados.setText("Archivo cargado correctamente.\nIngrese un caracter y presione 'Contar Apariciones'.");
        }
    }

    /**
     * Cuenta las apariciones del carácter especificado en el archivo
     */
    private void contarCaracteres() {
        // Validar que se haya seleccionado un archivo
        if (archivoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un archivo primero.",
                    "Archivo no seleccionado",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar que se haya ingresado un carácter
        String input = txtCaracter.getText();
        if (input == null || input.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, ingrese un caracter a buscar.",
                    "Caracter vacio",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tomar solo el primer carácter si se ingresaron varios
        char caracterBuscado = input.charAt(0);

        try {
            // Leer el contenido del archivo
            String contenido = leerArchivo(archivoSeleccionado);

            // Contar las apariciones del carácter
            int contador = contarApariciones(contenido, caracterBuscado);

            // Mostrar resultados detallados
            mostrarResultados(caracterBuscado, contador, contenido.length());

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this,
                    "El archivo no fue encontrado:\n" + archivoSeleccionado.getAbsolutePath(),
                    "Archivo no encontrado",
                    JOptionPane.ERROR_MESSAGE);
            txtResultados.setText("ERROR: Archivo no encontrado.");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al leer el archivo:\n" + e.getMessage(),
                    "Error de lectura",
                    JOptionPane.ERROR_MESSAGE);
            txtResultados.setText("ERROR: No se pudo leer el archivo.\n" + e.getMessage());
        }
    }

    /**
     * Lee el contenido completo del archivo
     *
     * @param archivo Archivo a leer
     * @return Contenido del archivo como String
     * @throws IOException Si ocurre un error de lectura
     */
    private String leerArchivo(File archivo) throws IOException {
        StringBuilder contenido = new StringBuilder();

        try {
            // Intentar leer con UTF-8
            byte[] bytes = Files.readAllBytes(archivo.toPath());
            contenido.append(new String(bytes, StandardCharsets.UTF_8));
        } catch (IOException e) {
            // Si falla, intentar con charset por defecto
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    contenido.append(linea).append("\n");
                }
            }
        }

        return contenido.toString();
    }

    /**
     * Cuenta las apariciones de un carácter en un texto
     *
     * @param texto Texto donde buscar
     * @param caracter Carácter a buscar
     * @return Número de apariciones
     */
    private int contarApariciones(String texto, char caracter) {
        int contador = 0;
        for (int i = 0; i < texto.length(); i++) {
            if (texto.charAt(i) == caracter) {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Muestra los resultados del conteo en el área de texto
     *
     * @param caracter Carácter buscado
     * @param apariciones Número de apariciones encontradas
     * @param totalCaracteres Total de caracteres en el archivo
     */
    private void mostrarResultados(char caracterBuscado, int apariciones, int totalCaracteres) {
        StringBuilder resultado = new StringBuilder();
        resultado.append("=".repeat(50)).append("\n");
        resultado.append("RESULTADOS DEL CONTEO\n");
        resultado.append("=".repeat(50)).append("\n\n");

        resultado.append("Archivo analizado: ").append(archivoSeleccionado.getName()).append("\n");
        resultado.append("Ruta completa: ").append(archivoSeleccionado.getAbsolutePath()).append("\n\n");

        resultado.append("Caracter buscado: '").append(caracterBuscado).append("'\n");
        resultado.append("Codigo ASCII/Unicode: ").append((int) caracterBuscado).append("\n\n");

        resultado.append("-".repeat(50)).append("\n");
        resultado.append("ESTADISTICAS\n");
        resultado.append("-".repeat(50)).append("\n");
        resultado.append("Total de apariciones: ").append(apariciones).append("\n");
        resultado.append("Total de caracteres en archivo: ").append(totalCaracteres).append("\n");

        if (totalCaracteres > 0) {
            double porcentaje = (apariciones * 100.0) / totalCaracteres;
            resultado.append("Porcentaje de aparicion: ").append(String.format("%.2f", porcentaje)).append("%\n");
        }

        resultado.append("-".repeat(50)).append("\n\n");

        if (apariciones == 0) {
            resultado.append("El caracter '").append(caracterBuscado).append("' no fue encontrado en el archivo.");
        } else if (apariciones == 1) {
            resultado.append("El caracter '").append(caracterBuscado).append("' aparece 1 vez en el archivo.");
        } else {
            resultado.append("El caracter '").append(caracterBuscado).append("' aparece ").append(apariciones).append(" veces en el archivo.");
        }

        txtResultados.setText(resultado.toString());
        txtResultados.setCaretPosition(0);

        // Mostrar también en un diálogo
        String mensaje = "El caracter '" + caracterBuscado + "' aparece " + apariciones +
                " vez/veces en el archivo.\n\nTotal de caracteres: " + totalCaracteres;
        JOptionPane.showMessageDialog(this, mensaje, "Resultado del Conteo", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Limpia todos los campos y reinicia la interfaz
     */
    private void limpiarTodo() {
        archivoSeleccionado = null;
        lblArchivo.setText("Ningun archivo seleccionado");
        lblArchivo.setForeground(Color.GRAY);
        txtCaracter.setText("");
        txtResultados.setText("Seleccione un archivo y un caracter para comenzar...");
    }

    /**
     * Método principal que inicia la aplicación
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            ContadorCaracteres app = new ContadorCaracteres();
            app.setVisible(true);
        });
    }
}

// Programador: Daniel Hernandez Hernandez
// Programa: Contador de Caracteres
// Fecha: 19 de noviembre de 2025
