/**
 * Universidad Autonoma Nacional de Mexico
 * FACULTAD DE CONTADURÍA Y ADMINISTRACION
 * INFORMATICA A DISTANCIA
 * Programa creado por Daniel Hernandez Hernandez el 05/10/2025
 * Asignatura: Programación orientada a objetos-2430
 * Profesor: Diana Karen Herrera Carrillo
 * 
 * Programa: Jerarquía de productos para supermercado/tienda departamental.
 * 
 * Este programa permite crear y visualizar productos de diferentes categorías usando herencia.
 * Incluye un menú interactivo en consola y 16 productos precargados como ejemplo.
 */

import java.util.ArrayList;
import java.util.Scanner;

// Clase base para todos los productos
abstract class Producto {
    protected String nombre;
    protected double precio;
    protected String codigoBarras;
    protected double descuento;
    protected String marca;
    protected int stock;

    public Producto(String nombre, double precio, String codigoBarras, double descuento, String marca, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.codigoBarras = codigoBarras;
        this.descuento = descuento;
        this.marca = marca;
        this.stock = stock;
    }

    // Calcula el precio con descuento aplicado
    public double getPrecioFinal() {
        return precio * (1 - descuento / 100);
    }

    // Método abstracto: cada producto muestra sus detalles propios
    public abstract void mostrarDetalles();

    // Muestra los datos generales de cualquier producto
    public void mostrarDatosGenerales() {
        System.out.println("Nombre: " + nombre);
        System.out.println("Marca: " + marca);
        System.out.println("Código de Barras: " + codigoBarras);
        System.out.println("Precio base: $" + precio);
        System.out.println("Descuento: " + descuento + "%");
        System.out.println("Precio final: $" + getPrecioFinal());
        System.out.println("Stock: " + stock);
    }
}

// Carnes frescas
class Carne extends Producto {
    private String tipoCorte;
    private String fechaCaducidad;

    public Carne(String nombre, double precio, String codigoBarras, double descuento, String marca, int stock, String tipoCorte, String fechaCaducidad) {
        super(nombre, precio, codigoBarras, descuento, marca, stock);
        this.tipoCorte = tipoCorte;
        this.fechaCaducidad = fechaCaducidad;
    }

    @Override
    public void mostrarDetalles() {
        mostrarDatosGenerales();
        System.out.println("Tipo de corte: " + tipoCorte);
        System.out.println("Fecha de caducidad: " + fechaCaducidad);
    }
}

// Panadería
class Pan extends Producto {
    private boolean integral;

    public Pan(String nombre, double precio, String codigoBarras, double descuento, String marca, int stock, boolean integral) {
        super(nombre, precio, codigoBarras, descuento, marca, stock);
        this.integral = integral;
    }

    @Override
    public void mostrarDetalles() {
        mostrarDatosGenerales();
        System.out.println("¿Es integral?: " + (integral ? "Sí" : "No"));
    }
}

// Lácteos
class Lacteo extends Producto {
    private String tipo;
    private String fechaCaducidad;

    public Lacteo(String nombre, double precio, String codigoBarras, double descuento, String marca, int stock, String tipo, String fechaCaducidad) {
        super(nombre, precio, codigoBarras, descuento, marca, stock);
        this.tipo = tipo;
        this.fechaCaducidad = fechaCaducidad;
    }

    @Override
    public void mostrarDetalles() {
        mostrarDatosGenerales();
        System.out.println("Tipo de lácteo: " + tipo);
        System.out.println("Fecha de caducidad: " + fechaCaducidad);
    }
}

// Refrescos
class Refresco extends Producto {
    private double litros;
    private boolean esLight;

    public Refresco(String nombre, double precio, String codigoBarras, double descuento, String marca, int stock, double litros, boolean esLight) {
        super(nombre, precio, codigoBarras, descuento, marca, stock);
        this.litros = litros;
        this.esLight = esLight;
    }

    @Override
    public void mostrarDetalles() {
        mostrarDatosGenerales();
        System.out.println("Contenido: " + litros + " L");
        System.out.println("¿Es light?: " + (esLight ? "Sí" : "No"));
    }
}

// Salchichonería
class Salchichoneria extends Producto {
    private String tipo;

    public Salchichoneria(String nombre, double precio, String codigoBarras, double descuento, String marca, int stock, String tipo) {
        super(nombre, precio, codigoBarras, descuento, marca, stock);
        this.tipo = tipo;
    }

    @Override
    public void mostrarDetalles() {
        mostrarDatosGenerales();
        System.out.println("Tipo de salchichonería: " + tipo);
    }
}

// Jabones
class Jabon extends Producto {
    private String tipo;
    private boolean esAntibacterial;

    public Jabon(String nombre, double precio, String codigoBarras, double descuento, String marca, int stock, String tipo, boolean esAntibacterial) {
        super(nombre, precio, codigoBarras, descuento, marca, stock);
        this.tipo = tipo;
        this.esAntibacterial = esAntibacterial;
    }

    @Override
    public void mostrarDetalles() {
        mostrarDatosGenerales();
        System.out.println("Tipo de jabón: " + tipo);
        System.out.println("¿Es antibacterial?: " + (esAntibacterial ? "Sí" : "No"));
    }
}

// Ropa
class Ropa extends Producto {
    private String talla;
    private String color;
    private String tipo;

    public Ropa(String nombre, double precio, String codigoBarras, double descuento, String marca, int stock, String tipo, String talla, String color) {
        super(nombre, precio, codigoBarras, descuento, marca, stock);
        this.tipo = tipo;
        this.talla = talla;
        this.color = color;
    }

    @Override
    public void mostrarDetalles() {
        mostrarDatosGenerales();
        System.out.println("Tipo de prenda: " + tipo);
        System.out.println("Talla: " + talla);
        System.out.println("Color: " + color);
    }
}

// Línea blanca (electrodomésticos grandes)
class LineaBlanca extends Producto {
    private String tipo;
    private int garantiaMeses;

    public LineaBlanca(String nombre, double precio, String codigoBarras, double descuento, String marca, int stock, String tipo, int garantiaMeses) {
        super(nombre, precio, codigoBarras, descuento, marca, stock);
        this.tipo = tipo;
        this.garantiaMeses = garantiaMeses;
    }

    @Override
    public void mostrarDetalles() {
        mostrarDatosGenerales();
        System.out.println("Tipo de línea blanca: " + tipo);
        System.out.println("Garantía: " + garantiaMeses + " meses");
    }
}

// Celulares
class Celular extends Producto {
    private String modelo;
    private int memoriaGB;
    private int garantiaMeses;

    public Celular(String nombre, double precio, String codigoBarras, double descuento, String marca, int stock, String modelo, int memoriaGB, int garantiaMeses) {
        super(nombre, precio, codigoBarras, descuento, marca, stock);
        this.modelo = modelo;
        this.memoriaGB = memoriaGB;
        this.garantiaMeses = garantiaMeses;
    }

    @Override
    public void mostrarDetalles() {
        mostrarDatosGenerales();
        System.out.println("Modelo: " + modelo);
        System.out.println("Memoria: " + memoriaGB + " GB");
        System.out.println("Garantía: " + garantiaMeses + " meses");
    }
}

// Videojuegos
class Videojuego extends Producto {
    private String plataforma;
    private String genero;

    public Videojuego(String nombre, double precio, String codigoBarras, double descuento, String marca, int stock, String plataforma, String genero) {
        super(nombre, precio, codigoBarras, descuento, marca, stock);
        this.plataforma = plataforma;
        this.genero = genero;
    }

    @Override
    public void mostrarDetalles() {
        mostrarDatosGenerales();
        System.out.println("Plataforma: " + plataforma);
        System.out.println("Género: " + genero);
    }
}

// Frutas
class Fruta extends Producto {
    private String tipo;
    private String origen;

    public Fruta(String nombre, double precio, String codigoBarras, double descuento, String marca, int stock, String tipo, String origen) {
        super(nombre, precio, codigoBarras, descuento, marca, stock);
        this.tipo = tipo;
        this.origen = origen;
    }

    @Override
    public void mostrarDetalles() {
        mostrarDatosGenerales();
        System.out.println("Tipo de fruta: " + tipo);
        System.out.println("Origen: " + origen);
    }
}

// Productos de limpieza
class Limpieza extends Producto {
    private String uso;

    public Limpieza(String nombre, double precio, String codigoBarras, double descuento, String marca, int stock, String uso) {
        super(nombre, precio, codigoBarras, descuento, marca, stock);
        this.uso = uso;
    }

    @Override
    public void mostrarDetalles() {
        mostrarDatosGenerales();
        System.out.println("Uso recomendado: " + uso);
    }
}

// Clase principal con menú y productos de ejemplo
public class jerarquia_productos_supermercado {
    private static ArrayList<Producto> inventario = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        agregarProductosEjemplo(); // Agrega 16 productos de ejemplo al iniciar
        int opcion;
        do {
            mostrarMenu();
            opcion = leerInt("Selecciona una opción: ");
            switch (opcion) {
                case 1:
                    crearProducto();
                    break;
                case 2:
                    mostrarInventario();
                    break;
                case 3:
                    System.out.println("¡Gracias por usar el sistema!");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 3);
    }

    /**
     * Agrega productos de distintos tipos al inventario para mostrar ejemplos
     */
    private static void agregarProductosEjemplo() {
        inventario.add(new Ropa("Camiseta deportiva", 299.99, "7501234567890", 5, "Nike", 10, "Camiseta", "M", "Negro"));
        inventario.add(new Jabon("Jabón Zote", 20.0, "7509876543211", 0, "Zote", 100, "Barra", false));
        inventario.add(new Carne("Bistec de res", 180.0, "7701231231231", 10, "CarneMart", 30, "Bistec", "2025-10-12"));
        inventario.add(new Pan("Pan integral Bimbo", 45.5, "7501112233445", 0, "Bimbo", 50, true));
        inventario.add(new Refresco("Coca-Cola", 28.0, "7500000012345", 2, "Coca-Cola", 200, 2.5, false));
        inventario.add(new Lacteo("Leche entera", 26.5, "7509998887776", 0, "Lala", 60, "Leche", "2025-10-15"));
        inventario.add(new Salchichoneria("Jamón de pavo", 110.0, "7505566778899", 5, "FUD", 18, "Jamón"));
        inventario.add(new LineaBlanca("Refrigerador LG", 12500, "7512345678901", 8, "LG", 4, "Refrigerador", 24));
        inventario.add(new Celular("Smartphone Galaxy S23", 18999.0, "7599999999999", 10, "Samsung", 3, "Galaxy S23", 256, 12));
        inventario.add(new Videojuego("The Legend of Zelda", 1399.0, "7598765432101", 0, "Nintendo", 15, "Nintendo Switch", "Aventura"));
        inventario.add(new Fruta("Manzana roja", 45.0, "7501122334455", 0, "CampoFresco", 80, "Manzana", "México"));
        inventario.add(new Limpieza("Limpiador multiusos", 50.0, "7502233445566", 3, "Fabuloso", 30, "Multiusos"));
        inventario.add(new Ropa("Pantalón de mezclilla", 499.0, "7504455667788", 7, "Levi's", 8, "Pantalón", "32", "Azul"));
        inventario.add(new Jabon("Jabón líquido antibacterial", 65.0, "7503332221110", 2, "Escudo", 25, "Líquido", true));
        inventario.add(new Pan("Pan dulce", 15.0, "7506665554443", 0, "Panadería La Flor", 25, false));
        inventario.add(new Salchichoneria("Chorizo español", 150.0, "7507778889990", 12, "La Europea", 5, "Chorizo"));
    }

    /**
     * Muestra el menú principal en consola
     */
    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ SUPERMERCADO/DEPARTAMENTAL ---");
        System.out.println("1. Agregar producto al inventario");
        System.out.println("2. Mostrar todos los productos");
        System.out.println("3. Salir");
    }

    /**
     * Permite capturar los datos de un producto desde teclado y lo agrega al inventario
     */
    private static void crearProducto() {
        System.out.println("\nElige el tipo de producto:");
        System.out.println("1. Ropa\n2. Jabón\n3. Carne\n4. Pan\n5. Refresco\n6. Lácteo\n7. Salchichonería\n8. Línea Blanca\n9. Celular\n10. Videojuego\n11. Fruta\n12. Limpieza");
        int tipo = leerInt("Opción: ");
        Producto p = null;

        // Datos generales de cualquier producto
        String nombre = leerString("Nombre: ");
        String marca = leerString("Marca: ");
        String codigoBarras = leerString("Código de barras: ");
        double precio = leerDouble("Precio: ");
        double descuento = leerDouble("Descuento (%): ");
        int stock = leerInt("Stock: ");

        // Captura de datos específicos según el tipo de producto
        switch (tipo) {
            case 1: // Ropa
                String tipoPrenda = leerString("Tipo de prenda: ");
                String talla = leerString("Talla: ");
                String color = leerString("Color: ");
                p = new Ropa(nombre, precio, codigoBarras, descuento, marca, stock, tipoPrenda, talla, color);
                break;
            case 2: // Jabón
                String tipoJabon = leerString("Tipo de jabón (líquido, barra, polvo): ");
                boolean antibac = leerBoolean("¿Es antibacterial? (s/n): ");
                p = new Jabon(nombre, precio, codigoBarras, descuento, marca, stock, tipoJabon, antibac);
                break;
            case 3: // Carne
                String tipoCorte = leerString("Tipo de corte: ");
                String cadCarne = leerString("Fecha de caducidad: ");
                p = new Carne(nombre, precio, codigoBarras, descuento, marca, stock, tipoCorte, cadCarne);
                break;
            case 4: // Pan
                boolean integral = leerBoolean("¿Es integral? (s/n): ");
                p = new Pan(nombre, precio, codigoBarras, descuento, marca, stock, integral);
                break;
            case 5: // Refresco
                double litros = leerDouble("Litros: ");
                boolean light = leerBoolean("¿Es light? (s/n): ");
                p = new Refresco(nombre, precio, codigoBarras, descuento, marca, stock, litros, light);
                break;
            case 6: // Lácteo
                String tipoLacteo = leerString("Tipo de lácteo: ");
                String cadLacteo = leerString("Fecha de caducidad: ");
                p = new Lacteo(nombre, precio, codigoBarras, descuento, marca, stock, tipoLacteo, cadLacteo);
                break;
            case 7: // Salchichonería
                String tipoSalch = leerString("Tipo de salchichonería: ");
                p = new Salchichoneria(nombre, precio, codigoBarras, descuento, marca, stock, tipoSalch);
                break;
            case 8: // Línea Blanca
                String tipoLB = leerString("Tipo de línea blanca: ");
                int garantiaLB = leerInt("Garantía (meses): ");
                p = new LineaBlanca(nombre, precio, codigoBarras, descuento, marca, stock, tipoLB, garantiaLB);
                break;
            case 9: // Celular
                String modelo = leerString("Modelo: ");
                int memoriaGB = leerInt("Memoria (GB): ");
                int garantiaCel = leerInt("Garantía (meses): ");
                p = new Celular(nombre, precio, codigoBarras, descuento, marca, stock, modelo, memoriaGB, garantiaCel);
                break;
            case 10: // Videojuego
                String plataforma = leerString("Plataforma: ");
                String genero = leerString("Género: ");
                p = new Videojuego(nombre, precio, codigoBarras, descuento, marca, stock, plataforma, genero);
                break;
            case 11: // Fruta
                String tipoFruta = leerString("Tipo de fruta: ");
                String origen = leerString("Origen: ");
                p = new Fruta(nombre, precio, codigoBarras, descuento, marca, stock, tipoFruta, origen);
                break;
            case 12: // Limpieza
                String uso = leerString("Uso recomendado: ");
                p = new Limpieza(nombre, precio, codigoBarras, descuento, marca, stock, uso);
                break;
            default:
                System.out.println("Tipo de producto inválido.");
                return;
        }

        inventario.add(p);
        System.out.println("Producto agregado correctamente.");
    }

    /**
     * Muestra la lista de todos los productos existentes en el inventario
     */
    private static void mostrarInventario() {
        if (inventario.isEmpty()) {
            System.out.println("\nNo hay productos en el inventario.");
            return;
        }
        System.out.println("\n--- INVENTARIO DE PRODUCTOS ---");
        int i = 1;
        for (Producto p : inventario) {
            System.out.println("\nProducto #" + i++);
            p.mostrarDetalles();
        }
    }

    // Métodos para leer datos del usuario desde consola
    private static String leerString(String msg) {
        System.out.print(msg);
        return sc.nextLine();
    }

    private static int leerInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número entero válido.");
            }
        }
    }

    private static double leerDouble(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número válido.");
            }
        }
    }

    private static boolean leerBoolean(String msg) {
        System.out.print(msg);
        String val = sc.nextLine();
        return (val.equalsIgnoreCase("s") || val.equalsIgnoreCase("si") || val.equalsIgnoreCase("sí"));
    }
}

// Programador: Daniel Hernandez Hernandez
// Programa: Jerarquía de productos para supermercado/tienda departamental
// Fecha: 06 de octubre de 2025
