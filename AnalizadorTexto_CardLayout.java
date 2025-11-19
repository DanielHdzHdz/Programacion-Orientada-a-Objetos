/**
 * Universidad Autonoma Nacional de Mexico
 * FACULTAD DE CONTADURÍA Y ADMINISTRACION
 * INFORMATICA A DISTANCIA
 * Programa creado por Daniel Hernandez Hernandez el 01/11/2025
 * Asignatura: Programación orientada a objetos-2430
 * Profesor: Diana Karen Herrera Carrillo
 *
 * Programa: Analizador de Texto con CardLayout
 *
 * Este programa permite cargar un archivo de texto, visualizar estadísticas
 * (líneas, palabras, caracteres) y exportar los resultados. La interfaz usa
 * CardLayout para navegar entre tres pantallas: Carga de archivo, Visualización
 * de estadísticas y Exportación de resultados.
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
import java.util.List;

public class AnalizadorTexto_CardLayout extends JFrame {

    private JPanel cardsPanel;
    private CardLayout cardLayout;

    // Componentes compartidos
    private File archivoSeleccionado;
    private JTextArea previewArea;
    private JLabel lblArchivo;
    private JLabel lblLineas;
    private JLabel lblPalabras;
    private JLabel lblCaracteres;
    private JButton btnExportar;

    /**
     * Constructor principal que inicializa la interfaz
     */
    public AnalizadorTexto_CardLayout() {
        super("Analizador de Texto (CardLayout)");
        initUI();
    }

    /**
     * Inicializa la interfaz gráfica y todos sus componentes
     */
    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        // Barra superior con navegación
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnPrev = new JButton("Anterior");
        JButton btnNext = new JButton("Siguiente");
        JComboBox<String> menuCards = new JComboBox<>(new String[] {
                "Carga de archivo", "Visualizacion", "Exportacion"
        });

        topBar.add(btnPrev);
        topBar.add(btnNext);
        topBar.add(new JLabel(" Ir a: "));
        topBar.add(menuCards);

        add(topBar, BorderLayout.NORTH);

        // Panel principal con CardLayout
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        cardsPanel.add(buildCargaPanel(), "carga");
        cardsPanel.add(buildVisualPanel(), "visual");
        cardsPanel.add(buildExportPanel(), "export");

        add(cardsPanel, BorderLayout.CENTER);

        // Lógica de navegación
        btnNext.addActionListener(e -> cardLayout.next(cardsPanel));
        btnPrev.addActionListener(e -> cardLayout.previous(cardsPanel));
        menuCards.addActionListener(e -> {
            String sel = (String) menuCards.getSelectedItem();
            if ("Carga de archivo".equals(sel)) cardLayout.show(cardsPanel, "carga");
            else if ("Visualizacion".equals(sel)) {
                calcularEstadisticasYMostrar();
                cardLayout.show(cardsPanel, "visual");
            } else if ("Exportacion".equals(sel)) {
                prepararExportacion();
                cardLayout.show(cardsPanel, "export");
            }
        });

        // Mostrar la primera tarjeta
        cardLayout.show(cardsPanel, "carga");
    }

    /**
     * Construye el panel de carga de archivo
     */
    private JPanel buildCargaPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnElegir = new JButton("Elegir archivo...");
        lblArchivo = new JLabel("Ningun archivo seleccionado");

        top.add(btnElegir);
        top.add(lblArchivo);

        previewArea = new JTextArea();
        previewArea.setEditable(false);
        previewArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(previewArea);

        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        btnElegir.addActionListener(e -> abrirSelectorDeArchivo());

        return panel;
    }

    /**
     * Construye el panel de visualización de estadísticas
     */
    private JPanel buildVisualPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel stats = new JPanel(new GridLayout(3, 2, 10, 10));
        stats.setBorder(BorderFactory.createTitledBorder("Estadisticas"));

        lblLineas = new JLabel("0");
        lblPalabras = new JLabel("0");
        lblCaracteres = new JLabel("0");

        stats.add(new JLabel("Lineas: "));
        stats.add(lblLineas);
        stats.add(new JLabel("Palabras: "));
        stats.add(lblPalabras);
        stats.add(new JLabel("Caracteres (incl. espacios): "));
        stats.add(lblCaracteres);

        panel.add(stats, BorderLayout.NORTH);

        // Vista de texto (misma preview)
        JScrollPane scroll = new JScrollPane(previewArea);
        panel.add(scroll, BorderLayout.CENTER);

        // Botón para recalcular
        JButton btnRecalcular = new JButton("Recalcular ahora");
        btnRecalcular.addActionListener(e -> calcularEstadisticasYMostrar());
        panel.add(btnRecalcular, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Construye el panel de exportación de resultados
     */
    private JPanel buildExportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea exportPreview = new JTextArea();
        exportPreview.setEditable(false);
        exportPreview.setLineWrap(true);
        exportPreview.setWrapStyleWord(true);
        exportPreview.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(exportPreview);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnExportar = new JButton("Exportar resultados...");
        JButton btnPreparar = new JButton("Preparar vista de exportacion");

        bottom.add(btnPreparar);
        bottom.add(btnExportar);

        panel.add(bottom, BorderLayout.SOUTH);

        // Preparar contenido de exportación
        btnPreparar.addActionListener(e -> {
            String contenido = generarTextoExportacion();
            exportPreview.setText(contenido);
        });

        // Guardar archivo de exportación
        btnExportar.addActionListener(e -> {
            String contenido = generarTextoExportacion();
            if (contenido.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay datos para exportar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Guardar resultados como");
            chooser.setSelectedFile(new File("resultados_analisis.txt"));
            int option = chooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File destino = chooser.getSelectedFile();
                try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(destino), StandardCharsets.UTF_8)) {
                    writer.write(contenido);
                    JOptionPane.showMessageDialog(this, "Exportado correctamente a:\n" + destino.getAbsolutePath(), "Exito", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al guardar el archivo:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }

    /**
     * Abre el selector de archivo y carga el contenido
     */
    private void abrirSelectorDeArchivo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar archivo de texto");
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt", "text", "md", "csv", "log"));
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = chooser.getSelectedFile();
            lblArchivo.setText(archivoSeleccionado.getName());
            cargarArchivoEnPreview();
            calcularEstadisticasYMostrar();
        }
    }

    /**
     * Carga el contenido del archivo en el área de vista previa
     */
    private void cargarArchivoEnPreview() {
        if (archivoSeleccionado == null) return;
        try {
            // Leer archivo con encoding UTF-8
            List<String> lineas = Files.readAllLines(archivoSeleccionado.toPath(), StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder();
            for (String l : lineas) {
                sb.append(l).append("\n");
            }
            previewArea.setText(sb.toString());
            previewArea.setCaretPosition(0);
        } catch (IOException e) {
            // Intento con charset por defecto
            try {
                List<String> lineas = Files.readAllLines(archivoSeleccionado.toPath());
                StringBuilder sb = new StringBuilder();
                for (String l : lineas) {
                    sb.append(l).append("\n");
                }
                previewArea.setText(sb.toString());
                previewArea.setCaretPosition(0);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "No se pudo leer el archivo:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Calcula las estadísticas del texto y actualiza las etiquetas
     */
    private void calcularEstadisticasYMostrar() {
        String texto = previewArea.getText();
        if (texto == null || texto.isEmpty()) {
            lblLineas.setText("0");
            lblPalabras.setText("0");
            lblCaracteres.setText("0");
            return;
        }

        // Contar líneas
        String[] lineas = texto.split("\r\n|\r|\n", -1);
        int numLineas = lineas.length;

        // Contar palabras
        String trimmed = texto.trim();
        int numPalabras = 0;
        if (!trimmed.isEmpty()) {
            String[] palabras = trimmed.split("\\s+");
            numPalabras = palabras.length;
        }

        // Contar caracteres (incluyendo espacios)
        int numCaracteres = texto.length();

        lblLineas.setText(String.valueOf(numLineas));
        lblPalabras.setText(String.valueOf(numPalabras));
        lblCaracteres.setText(String.valueOf(numCaracteres));
    }

    /**
     * Genera el texto para exportación con todas las estadísticas
     */
    private String generarTextoExportacion() {
        if (archivoSeleccionado == null && (previewArea.getText() == null || previewArea.getText().isEmpty())) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Analizador de Texto - Resultados\n");
        sb.append("-------------------------------\n");
        sb.append("Archivo: ").append(archivoSeleccionado != null ? archivoSeleccionado.getAbsolutePath() : "Sin archivo seleccionado").append("\n\n");
        sb.append("Lineas: ").append(lblLineas.getText()).append("\n");
        sb.append("Palabras: ").append(lblPalabras.getText()).append("\n");
        sb.append("Caracteres: ").append(lblCaracteres.getText()).append("\n\n");
        sb.append("Vista previa:\n");
        sb.append("-------------------------------\n");
        sb.append(previewArea.getText()).append("\n");
        return sb.toString();
    }

    /**
     * Prepara la vista de exportación recalculando estadísticas
     */
    private void prepararExportacion() {
        calcularEstadisticasYMostrar();
    }

    /**
     * Método principal que inicia la aplicación
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            AnalizadorTexto_CardLayout app = new AnalizadorTexto_CardLayout();
            app.setVisible(true);
        });
    }
}

// Programador: Daniel Hernandez Hernandez
// Programa: Analizador de Texto con CardLayout
// Fecha: 19 de noviembre de 2025
