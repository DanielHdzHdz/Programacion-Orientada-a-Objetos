import java.util.Scanner;

/**
 * Programa: Descubre el Mes
 * Tema: Condicionales (Tema 1.4.2)
 * 
 * Este programa solicita al usuario un número entre 1 y 12,
 * valida que esté dentro del rango indicado y muestra el nombre 
 * del mes correspondiente al número ingresado.
 * 
 * Ejemplo de salida:
 * "El mes 3 es el mes de marzo."
 * 
 * Si el número está fuera del rango, se muestra un mensaje de error.
 * 
 * Guardar el archivo como: descubre_el_mes.java
 * 
 * @author DanielHdzHdz
 */
public class descubre_el_mes {
    /**
     * Método principal que ejecuta el programa.
     * Solicita la entrada del usuario, valida el rango,
     * identifica el mes y muestra el resultado.
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Solicitar al usuario un número entre 1 y 12
        System.out.print("Ingrese un número entre 1 y 12: ");
        int numeroMes = scanner.nextInt();

        String nombreMes = "";

        // Validar que el número esté en el rango permitido
        if (numeroMes >= 1 && numeroMes <= 12) {
            // Identificar el nombre del mes usando switch
            switch (numeroMes) {
                case 1:
                    nombreMes = "enero";
                    break;
                case 2:
                    nombreMes = "febrero";
                    break;
                case 3:
                    nombreMes = "marzo";
                    break;
                case 4:
                    nombreMes = "abril";
                    break;
                case 5:
                    nombreMes = "mayo";
                    break;
                case 6:
                    nombreMes = "junio";
                    break;
                case 7:
                    nombreMes = "julio";
                    break;
                case 8:
                    nombreMes = "agosto";
                    break;
                case 9:
                    nombreMes = "septiembre";
                    break;
                case 10:
                    nombreMes = "octubre";
                    break;
                case 11:
                    nombreMes = "noviembre";
                    break;
                case 12:
                    nombreMes = "diciembre";
                    break;
            }
            // Mostrar el resultado
            System.out.println("El mes " + numeroMes + " es el mes de " + nombreMes + ".");
        } else {
            // Mensaje de error si el número está fuera de rango
            System.out.println("Error: El número debe estar entre 1 y 12.");
        }
        
        scanner.close();
    }
}