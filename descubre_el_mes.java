import java.util.Scanner;

/**
 *Universidad Autonoma Nacional de Mexico
 *FACULTAD DE CONTADURÍA Y ADMINISTRACION
 *INFORMATICA A DISTANCIA
 *Programa creado por Daniel Hernandez Hernandez el 05/09/2025
 *Asignatura: Programación orientada a objetos-2430
 *Profesor: Diana Karen Herrera Carrillo
 * Solicita un número entre 1 y 12 y muestra el nombre del mes
 */
public class descubre_el_mes {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Leer entrada del usuario

        System.out.print("Ingrese un numero entre 1 y 12: "); // Solicitud al usuario
        int numeroMes = scanner.nextInt(); // Guardar número ingresado

        String nombreMes = "";

        // Validar rango del número
        if (numeroMes >= 1 && numeroMes <= 12) {
            // Seleccionar el nombre del mes
            switch (numeroMes) {
                case 1:  nombreMes = "enero"; break;
                case 2:  nombreMes = "febrero"; break;
                case 3:  nombreMes = "marzo"; break;
                case 4:  nombreMes = "abril"; break;
                case 5:  nombreMes = "mayo"; break;
                case 6:  nombreMes = "junio"; break;
                case 7:  nombreMes = "julio"; break;
                case 8:  nombreMes = "agosto"; break;
                case 9:  nombreMes = "septiembre"; break;
                case 10: nombreMes = "octubre"; break;
                case 11: nombreMes = "noviembre"; break;
                case 12: nombreMes = "diciembre"; break;
            }
            System.out.println("El mes " + numeroMes + " es el mes de " + nombreMes + "."); // Mostrar resultado
        } else {
            System.out.println("Error: El número debe estar entre 1 y 12."); // Mensaje de error
        }

        scanner.close(); // Cerrar el escáner
    }
}

// Programador: Daniel Hernandez Hernandez
// Programa para Solicitar un número entre 1 y 12 y muestra el nombre del mes
// Fecha: 05 de septiembre de 2025
