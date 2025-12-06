/*
 Universidad Autonoma Nacional de Mexico
 FACULTAD DE CONTADURÍA Y ADMINISTRACION
 INFORMATICA A DISTANCIA
 Programa creado por Daniel Hernandez Hernandez el 05/12/2025
 Asignatura: Programación orientada a objetos-2430
 Profesor: Diana Karen Herrera Carrillo

 Programa: Exportación de resultados con DataStreams (Interfaz GUI)
 Descripción:
   Interfaz gráfica que permite seleccionar un archivo de texto con JFileChooser,
   calcular métricas (líneas, palabras y caracteres), guardar esos valores en un
   archivo .bin usando DataOutputStream y luego leerlos con DataInputStream para
   confirmar la exportación.

 Programa requiere interacción mediante botones y diálogos.
 Programador: Daniel Hernandez Hernandez
 Fecha: 05/12/2025
*/

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ExportarResultadosDataStreams extends JFrame {

    private File archivoSeleccionado;
    private File archivoBinSeleccionado;
    private JLabel lblArchivo;
    private JLabel lblBin;
    private JTextArea txtResultados;
    private JButton btnSeleccionar;
    private JButton btnSeleccionarBin;
    private JButton btnExportar;
    private JButton btnLeerBin;
    private JButton btnLimpiar;
    private JButton btnSalir;

    /**
     * Constructor principal que inicializa la interfaz
     */
    public ExportarResultadosDataStreams() {
        super("Exportación de Resultados - DataStreams");
        initUI();
    }

    /**
     * Inicializa la interfaz gráfica y todos sus componentes
     */
    private void initUI() {
        setSize(700, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Panel superior: selección de archivos
        JPanel panelArriba = new JPanel(new GridLayout(2, 1, 5, 5));
        panelArriba.setBorder(BorderFactory.createTitledBorder("Archivos"));

        JPanel p1 = new JPanel(new BorderLayout(5, 5));
        btnSeleccionar = new JButton("Seleccionar archivo de texto...");
        btnSeleccionar.addActionListener(e -> seleccionarArchivoTexto());
        lblArchivo = new JLabel("Ningun archivo de texto seleccionado");
        lblArchivo.setForeground(Color.GRAY);
        p1.add(btnSeleccionar, BorderLayout.WEST);
        p1.add(lblArchivo, BorderLayout.CENTER);

        JPanel p2 = new JPanel(new BorderLayout(5, 5));
        btnSeleccionarBin = new JButton("Seleccionar archivo .bin (guardar como)...");
        btnSeleccionarBin.addActionListener(e -> seleccionarArchivoBin());
        lblBin = new JLabel("Se usara 'resultados.bin' si no se selecciona uno");
        lblBin.setForeground(Color.GRAY);
        p2.add(btnSeleccionarBin, BorderLayout.WEST);
        p2.add(lblBin, BorderLayout.CENTER);

        panelArriba.add(p1);
        panelArriba.add(p2);

        add(panelArriba, BorderLayout.NORTH);

        // Área central: resultados
        txtResultados = new JTextArea();
        txtResultados.setEditable(false);
        txtResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtResultados.setText("Seleccione un archivo de texto para comenzar...");
        JScrollPane scroll = new JScrollPane(txtResultados);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultados y Registro"));
        add(scroll, BorderLayout.CENTER);

        // Panel inferior: botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnExportar = new JButton("Exportar y Leer (.bin)");
        btnExportar.setToolTipText("Analiza el archivo de texto y guarda las metricas en formato binario");
        btnExportar.addActionListener(e -> exportarYLeer());

        btnLeerBin = new JButton("Leer archivo .bin existente");
        btnLeerBin.setToolTipText("Lee un archivo .bin previamente guardado");
        btnLeerBin.addActionListener(e -> soloLeerBinario());

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setToolTipText("Limpia todos los campos y reinicia la interfaz");
        btnLimpiar.addActionListener(e -> limpiar());

        btnSalir = new JButton("Salir");
        btnSalir.setToolTipText("Cierra la aplicacion");
        btnSalir.addActionListener(e -> System.exit(0));

        panelBotones.add(btnExportar);
        panelBotones.add(btnLeerBin);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Abre el selector para elegir un archivo de texto
     */
    private void seleccionarArchivoTexto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar archivo de texto");
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Archivos de texto (*.txt, *.md, *.csv, *.log)", "txt", "md", "csv", "log"));
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = chooser.getSelectedFile();
            lblArchivo.setText("Archivo: " + archivoSeleccionado.getName());
            lblArchivo.setForeground(new Color(0, 128, 0));
            txtResultados.setText("Archivo seleccionado: " + archivoSeleccionado.getAbsolutePath() + "\n");
        }
    }

    /**
     * Abre el selector para elegir dónde guardar el archivo .bin
     */
    private void seleccionarArchivoBin() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar resultados en (.bin)");
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos binarios (*.bin)", "bin"));
        chooser.setSelectedFile(new File("resultados.bin"));
        int res = chooser.showSaveDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            archivoBinSeleccionado = chooser.getSelectedFile();
            // Asegurar que tenga extensión .bin
            if (!archivoBinSeleccionado.getName().toLowerCase().endsWith(".bin")) {
                archivoBinSeleccionado = new File(archivoBinSeleccionado.getAbsolutePath() + ".bin");
            }
            lblBin.setText("Salida binaria: " + archivoBinSeleccionado.getName());
            lblBin.setForeground(new Color(0, 128, 0));
            appendResultado("Archivo binario seleccionado: " + archivoBinSeleccionado.getAbsolutePath());
        }
    }

    /**
     * Exporta las métricas a un archivo .bin y luego las lee para confirmar
     */
    private void exportarYLeer() {
        if (archivoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona primero un archivo de texto.",
                    "Archivo no seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        File bin = (archivoBinSeleccionado != null) ? archivoBinSeleccionado : new File("resultados.bin");

        int[] metrics;
        try {
            metrics = analizarArchivo(archivoSeleccionado);
        } catch (IOException e) {
            mostrarError("Error leyendo el archivo de texto: " + e.getMessage());
            return;
        }

        int lineas = metrics[0];
        int palabras = metrics[1];
        int caracteres = metrics[2];

        appendResultado("\n" + "=".repeat(60));
        appendResultado("METRICAS CALCULADAS");
        appendResultado("=".repeat(60));
        appendResultado(" - Lineas: " + lineas);
        appendResultado(" - Palabras: " + palabras);
        appendResultado(" - Caracteres (incluye saltos de linea): " + caracteres);

        // Escribir con DataOutputStream
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(bin))) {
            dos.writeInt(lineas);
            dos.writeInt(palabras);
            dos.writeInt(caracteres);

            // Mostrar información del archivo binario generado
            long tamano = bin.length();
            appendResultado("\n" + "-".repeat(60));
            appendResultado("ESCRITURA EXITOSA");
            appendResultado("-".repeat(60));
            appendResultado("Archivo: " + bin.getAbsolutePath());
            appendResultado("Tamaño: " + tamano + " bytes (esperado: 12 bytes)");
            appendResultado("Formato: 3 enteros de 4 bytes cada uno");

        } catch (IOException e) {
            mostrarError("Error al escribir el archivo binario: " + e.getMessage());
            return;
        }

        // Leer con DataInputStream y confirmar
        try (DataInputStream dis = new DataInputStream(new FileInputStream(bin))) {
            int l = dis.readInt();
            int p = dis.readInt();
            int c = dis.readInt();

            appendResultado("\n" + "-".repeat(60));
            appendResultado("METRICAS LEIDAS DESDE ARCHIVO BINARIO");
            appendResultado("-".repeat(60));
            appendResultado(" - Lineas: " + l);
            appendResultado(" - Palabras: " + p);
            appendResultado(" - Caracteres: " + c);

            boolean ok = (l == lineas) && (p == palabras) && (c == caracteres);
            appendResultado("\n" + "=".repeat(60));
            appendResultado("CONFIRMACION: " + (ok ? "VALORES COINCIDEN CORRECTAMENTE" : "ADVERTENCIA: VALORES DISTINTOS"));
            appendResultado("=".repeat(60));

            // Mostrar detalles de la comparación
            if (ok) {
                appendResultado("✓ Lineas: " + l + " == " + lineas);
                appendResultado("✓ Palabras: " + p + " == " + palabras);
                appendResultado("✓ Caracteres: " + c + " == " + caracteres);
            } else {
                appendResultado("✗ Lineas: " + l + " vs " + lineas);
                appendResultado("✗ Palabras: " + p + " vs " + palabras);
                appendResultado("✗ Caracteres: " + c + " vs " + caracteres);
            }

            JOptionPane.showMessageDialog(this,
                    "Exportacion completada exitosamente.\n\n" +
                            "Archivo: " + bin.getName() + "\n" +
                            "Tamaño: " + bin.length() + " bytes\n\n" +
                            (ok ? "Los valores coinciden correctamente." : "Los valores leidos difieren."),
                    "Exportacion finalizada",
                    ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) {
            mostrarError("Error al leer el archivo binario: " + e.getMessage());
        }
    }

    /**
     * Lee un archivo .bin existente sin necesidad de analizar un texto primero
     * NUEVA FUNCIONALIDAD: Permite inspeccionar archivos .bin previamente guardados
     */
    private void soloLeerBinario() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar archivo .bin para leer");
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos binarios (*.bin)", "bin"));

        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File bin = chooser.getSelectedFile();

            // Verificar que el archivo existe
            if (!bin.exists()) {
                JOptionPane.showMessageDialog(this,
                        "El archivo seleccionado no existe.",
                        "Archivo no encontrado",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar que el tamaño es correcto (debe ser 12 bytes: 3 enteros de 4 bytes)
            long tamano = bin.length();
            if (tamano != 12) {
                int opcion = JOptionPane.showConfirmDialog(this,
                        "Advertencia: El archivo tiene " + tamano + " bytes.\n" +
                                "Se esperaban 12 bytes (3 enteros).\n\n" +
                                "¿Desea intentar leerlo de todas formas?",
                        "Tamaño inesperado",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (opcion != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            try (DataInputStream dis = new DataInputStream(new FileInputStream(bin))) {
                int lineas = dis.readInt();
                int palabras = dis.readInt();
                int caracteres = dis.readInt();

                appendResultado("\n" + "=".repeat(60));
                appendResultado("LECTURA DE ARCHIVO BINARIO EXISTENTE");
                appendResultado("=".repeat(60));
                appendResultado("Archivo: " + bin.getAbsolutePath());
                appendResultado("Tamaño: " + tamano + " bytes");
                appendResultado("\n" + "-".repeat(60));
                appendResultado("METRICAS RECUPERADAS");
                appendResultado("-".repeat(60));
                appendResultado(" - Lineas: " + lineas);
                appendResultado(" - Palabras: " + palabras);
                appendResultado(" - Caracteres: " + caracteres);
                appendResultado("=".repeat(60));

                JOptionPane.showMessageDialog(this,
                        "Archivo binario leido correctamente:\n\n" +
                                "Lineas: " + lineas + "\n" +
                                "Palabras: " + palabras + "\n" +
                                "Caracteres: " + caracteres + "\n\n" +
                                "Tamaño del archivo: " + tamano + " bytes",
                        "Lectura exitosa",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (EOFException e) {
                mostrarError("El archivo no contiene suficientes datos.\n" +
                        "Se esperaban 3 enteros (12 bytes).");
            } catch (IOException e) {
                mostrarError("Error al leer el archivo binario:\n" + e.getMessage());
            }
        }
    }

    /**
     * Analiza un archivo de texto y calcula las métricas
     *
     * @param archivo Archivo a analizar
     * @return Array con [líneas, palabras, caracteres]
     * @throws IOException Si hay error al leer el archivo
     */
    private int[] analizarArchivo(File archivo) throws IOException {
        int lineas = 0;
        int palabras = 0;
        int caracteresSinSaltos = 0;

        // Intentar con UTF-8; fallback a FileReader si falla
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
            // Fallback a charset por defecto
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
     * Limpia todos los campos y reinicia la interfaz
     */
    private void limpiar() {
        archivoSeleccionado = null;
        archivoBinSeleccionado = null;
        lblArchivo.setText("Ningun archivo de texto seleccionado");
        lblArchivo.setForeground(Color.GRAY);
        lblBin.setText("Se usara 'resultados.bin' si no se selecciona uno");
        lblBin.setForeground(Color.GRAY);
        txtResultados.setText("Seleccione un archivo de texto para comenzar...");
    }

    /**
     * Agrega texto al área de resultados
     */
    private void appendResultado(String texto) {
        txtResultados.append(texto + "\n");
        txtResultados.setCaretPosition(txtResultados.getDocument().getLength());
    }

    /**
     * Muestra un mensaje de error en el área de resultados y en un diálogo
     */
    private void mostrarError(String mensaje) {
        appendResultado("\nERROR: " + mensaje);
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Método principal que inicia la aplicación
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            ExportarResultadosDataStreams app = new ExportarResultadosDataStreams();
            app.setVisible(true);
        });
    }
}

// Programador: Daniel Hernandez Hernandez
// Programa: Exportación de resultados con DataStreams
// Fecha: 05 de diciembre de 2025
