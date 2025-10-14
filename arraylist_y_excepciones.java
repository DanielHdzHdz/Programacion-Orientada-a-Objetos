/**
 * Universidad Autonoma Nacional de Mexico
 * FACULTAD DE CONTADURÍA Y ADMINISTRACION
 * INFORMATICA A DISTANCIA
 * Programa creado por Daniel Hernandez Hernandez el 10/10/2025
 * Asignatura: Programación orientada a objetos-2430
 * Profesor: Diana Karen Herrera Carrillo
 *
 * Programa: ArrayList Y Excepciones
 *
 * Este programa demuestra el uso de ArrayList en Java y maneja varias excepciones comunes
 * que pueden ocurrir al trabajar con esta estructura de datos.
 */

import java.util.*;

public class  arraylist_y_excepciones {
    public static void main(String[] args) {
        // Ejemplo 1: IndexOutOfBoundsException
        System.out.println("Ejemplo 1: IndexOutOfBoundsException");
        try {
            ArrayList<String> lista = new ArrayList<>();
            lista.add("A");
            lista.add("B");
            // Acceso a índice fuera de rango (solo hay 2 elementos, índices 0 y 1)
            System.out.println(lista.get(2)); // Índice 2 no existe
        } catch (IndexOutOfBoundsException e) {
            System.out.println("¡Error! Acceso a índice fuera de rango: " + e.getMessage());
        }

        // Ejemplo 2: set() y remove() también pueden lanzar IndexOutOfBoundsException
        System.out.println("\nEjemplo 2: set() y remove() - IndexOutOfBoundsException");
        try {
            ArrayList<String> lista = new ArrayList<>();
            lista.add("A");
            // Intentar reemplazar elemento en índice inexistente
            lista.set(5, "X");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("set(): Índice inválido: " + e.getMessage());
        }
        try {
            ArrayList<String> lista = new ArrayList<>();
            lista.add("A");
            // Intentar eliminar elemento en índice inexistente
            lista.remove(3);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("remove(): Índice inválido: " + e.getMessage());
        }

        // Ejemplo 3: add(index, element) y addAll(index, colección) - IndexOutOfBoundsException
        System.out.println("\nEjemplo 3: add(index, element) y addAll(index, colección) - IndexOutOfBoundsException");
        try {
            ArrayList<String> lista = new ArrayList<>();
            lista.add(2, "Z"); // Índice 2 no existe aún
        } catch (IndexOutOfBoundsException e) {
            System.out.println("add(): Índice inválido: " + e.getMessage());
        }
        try {
            ArrayList<String> lista = new ArrayList<>();
            List<String> otra = Arrays.asList("uno", "dos");
            lista.addAll(4, otra);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("addAll(): Índice inválido: " + e.getMessage());
        }

        // Ejemplo 4: subList() - IndexOutOfBoundsException
        System.out.println("\nEjemplo 4: subList() - IndexOutOfBoundsException");
        try {
            ArrayList<String> lista = new ArrayList<>();
            lista.add("A");
            lista.subList(0, 5); // El índice final excede el tamaño
        } catch (IndexOutOfBoundsException e) {
            System.out.println("subList(): Índice inválido: " + e.getMessage());
        }

        // Ejemplo 5: ConcurrentModificationException
        System.out.println("\nEjemplo 5: ConcurrentModificationException");
        try {
            ArrayList<String> lista = new ArrayList<>();
            lista.add("A");
            lista.add("B");
            // Modificar la lista mientras la recorres con for-each
            for (String s : lista) {
                lista.remove(s); // Modifica la estructura durante iteración
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("ConcurrentModificationException: " + e.getMessage());
        }

        // Ejemplo 6: NullPointerException
        System.out.println("\nEjemplo 6: NullPointerException");
        try {
            ArrayList<String> lista = null;
            // Intentar usar un método en una referencia null
            lista.add("Elemento");
        } catch (NullPointerException e) {
            System.out.println("NullPointerException: " + e.getMessage());
        }

        // Ejemplo 7: IllegalArgumentException
        System.out.println("\nEjemplo 7: IllegalArgumentException");
        try {
            // Intentar crear un ArrayList con capacidad negativa
            ArrayList<String> lista = new ArrayList<>(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }

        // Resumen final
        System.out.println("\nResumen de Excepciones en ArrayList:");
        System.out.println("- IndexOutOfBoundsException: Acceso/modificación con índice fuera de rango.");
        System.out.println("- ConcurrentModificationException: Modificación durante recorrido con iterador.");
        System.out.println("- NullPointerException: Uso de métodos sobre referencias null.");
        System.out.println("- IllegalArgumentException: Argumentos inválidos, como capacidad negativa.");
    }
}
// Programador: Daniel Hernandez Hernandez
// Programa: ArrayList Y Excepciones
// Fecha: 10 de octubre de 2025
