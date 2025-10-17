/**
 * Universidad Autonoma Nacional de Mexico
 * FACULTAD DE CONTADURÍA Y ADMINISTRACION
 * INFORMATICA A DISTANCIA
 * Programa creado por Daniel Hernandez Hernandez el 17/10/2025
 * Asignatura: Programación orientada a objetos-2430
 * Profesor: Diana Karen Herrera Carrillo
 *
 * Programa: Analizador de Texto con JTabbedPane
 *
 * Este programa crea una aplicación GUI en Java utilizando Swing que permite al usuario
 * que pueden ocurrir al trabajar con esta estructura de datos.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class AnalizadorTexto_JTabbedPane extends JFrame {
    private final JLabel lblLineas = new JLabel("Total de líneas: —");
    private final JLabel lblPalabras = new JLabel("Total de palabras: —");
    private final JLabel lblCaracteres = new JLabel("Total de caracteres: —");

    public AnalizadorTexto_JTabbedPane() {
        super("Mi analizador de Texto - JTabbedPane");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        setSize(600,300);
        setLocationRelativeTo(null);
    }

    private void initUI() {
        // Usa el aspecto del sistema (opcional)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ignored) {}

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Estadísticas generales", createTabPanel(lblLineas, MetricType.LINES));
        tabbedPane.addTab("Contador de palabras", createTabPanel(lblPalabras, MetricType.WORDS));
        tabbedPane.addTab("Contador de caracteres", createTabPanel(lblCaracteres, MetricType.CHARS));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private enum MetricType { LINES, WORDS, CHARS }

    // Crea un panel con un botón "Cargar archivo" y una etiqueta para mostrar la métrica
    private JPanel createTabPanel(JLabel resultLabel, MetricType type) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton btnCargar = new JButton("Seleccionar archivo");
        btnCargar.addActionListener(e -> onCargarArchivo(type, resultLabel));

        top.add(btnCargar);

        // Etiqueta explicativa
        JLabel info = new JLabel(getInfoText(type));
        top.add(info);

        // Centrar la etiqueta del resultado en el centro del panel
        JPanel center = new JPanel(new GridBagLayout());
        resultLabel.setFont(resultLabel.getFont().deriveFont(Font.BOLD, 16f));
        center.add(resultLabel);

        panel.add(top, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return panel;
    }

    private String getInfoText(MetricType type) {
        switch (type) {
            case LINES: return "(Muestra nº de líneas del archivo)";
            case WORDS: return "(Cuenta palabras separadas por espacios/saltos de línea)";
            case CHARS: return "(Cuenta todos los caracteres del archivo)";
            default: return "";
        }
    }

    // Manejador del botón. Abre JFileChooser y lanza SwingWorker para análisis.
    private void onCargarArchivo(MetricType type, JLabel resultLabel) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccione archivo de texto");
        int sel = chooser.showOpenDialog(this);
        if (sel != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path path = chooser.getSelectedFile().toPath();

        // Deshabilitar la ventana principal mientras se procesa (opcional)
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        resultLabel.setText("Procesando...");

        // Analizar en segundo plano para no bloquear la EDT
        new SwingWorker<AnalysisResult, Void>() {
            @Override
            protected AnalysisResult doInBackground() {
                try {
                    // Lee todo el archivo como texto (UTF-8). Si el archivo tiene otra codificación,
                    // se puede adaptar aquí.
                    byte[] bytes = Files.readAllBytes(path);
                    String content = new String(bytes, StandardCharsets.UTF_8);

                    long lines = 0;
                    if (content.isEmpty()) {
                        lines = 0;
                    } else {
                        // split con \\R para considerar cualquier terminador de línea
                        String[] ln = content.split("\\R", -1);
                        lines = ln.length;
                        // Si el archivo termina con un salto de linea extra, split lo cuenta correctamente.
                    }

                    long words = 0;
                    String trimmed = content.trim();
                    if (!trimmed.isEmpty()) {
                        // Separar por cualquier espacio en blanco
                        String[] tokens = trimmed.split("\\s+");
                        words = tokens.length;
                    }

                    long chars = content.length(); // incluye saltos de línea como caracteres

                    return new AnalysisResult(path.getFileName().toString(), lines, words, chars);
                } catch (IOException ex) {
                    return new AnalysisResult(path.getFileName().toString(), ex);
                }
            }

            @Override
            protected void done() {
                try {
                    AnalysisResult res = get();
                    if (res.error != null) {
                        JOptionPane.showMessageDialog(AnalizadorTexto_JTabbedPane.this,
                                "Error al leer el archivo:\n" + res.error.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                        resultLabel.setText("Error al procesar el archivo");
                    } else {
                        switch (type) {
                            case LINES:
                                resultLabel.setText(String.format("Líneas: %d   (archivo: %s)", res.lines, res.fileName));
                                break;
                            case WORDS:
                                resultLabel.setText(String.format("Palabras: %d   (archivo: %s)", res.words, res.fileName));
                                break;
                            case CHARS:
                                resultLabel.setText(String.format("Caracteres: %d   (archivo: %s)", res.chars, res.fileName));
                                break;
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AnalizadorTexto_JTabbedPane.this,
                            "Error inesperado:\n" + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    resultLabel.setText("Error inesperado");
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        }.execute();
    }

    // Resultado del análisis o error
    private static class AnalysisResult {
        final String fileName;
        final long lines;
        final long words;
        final long chars;
        final Exception error;

        AnalysisResult(String fileName, long lines, long words, long chars) {
            this.fileName = fileName;
            this.lines = lines;
            this.words = words;
            this.chars = chars;
            this.error = null;
        }

        AnalysisResult(String fileName, Exception error) {
            this.fileName = fileName;
            this.lines = 0;
            this.words = 0;
            this.chars = 0;
            this.error = error;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AnalizadorTexto_JTabbedPane app = new AnalizadorTexto_JTabbedPane();
            app.setVisible(true);
        });
    }
}
// Programador: Daniel Hernandez Hernandez
// Programa: Analizador de Texto con JTabbedPane
// Fecha: 17 de octubre de 2025
