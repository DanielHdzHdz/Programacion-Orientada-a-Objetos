/**
 *Universidad Autonoma Nacional de Mexico
 *FACULTAD DE CONTADURÍA Y ADMINISTRACION
 *INFORMATICA A DISTANCIA
 *Programa creado por Daniel Hernandez Hernandez el 05/09/2025
 *Asignatura: Programación orientada a objetos-2430
 *Profesor: Diana Karen Herrera Carrillo
 * Programa que demuestra varios métodos de la clase String en Java
 */
public class prueba_metodos_string {
    public static void main(String[] args) {
        // Declaramos una cadena principal
        String frase = "Hola soy Daniel";

        // length(): Devuelve la longitud de la cadena
        System.out.println("Cadena original: " + frase);
        System.out.println("1. Longitud de la cadena (length): " + frase.length());

        // charAt(): Devuelve el carácter en una posición específica
        System.out.println("2. Carácter en la posición 4 (charAt): '" + frase.charAt(4) + "'");

        // substring(): Devuelve una subcadena desde el índice dado
        System.out.println("3. Subcadena desde la posición 5 (substring): \"" + frase.substring(5) + "\"");
        System.out.println("   Subcadena entre posiciones 0 y 4: \"" + frase.substring(0, 4) + "\"");

        // toUpperCase(): Convierte la cadena a mayúsculas
        System.out.println("4. Cadena en mayúsculas (toUpperCase): " + frase.toUpperCase());

        // toLowerCase(): Convierte la cadena a minúsculas
        System.out.println("5. Cadena en minúsculas (toLowerCase): " + frase.toLowerCase());

        // equals(): Compara si dos cadenas son iguales (sensitivo a mayúsculas)
        System.out.println("6. ¿'Hola soy Daniel' es igual a 'hola soy daniel'? (equals): " + frase.equals("hola soy daniel"));

        // equalsIgnoreCase(): Compara si dos cadenas son iguales (ignorando mayúsculas/minúsculas)
        System.out.println("7. ¿'Hola Soy Daniel' es igual a 'hola soy daniel'? (equalsIgnoreCase): " + frase.equalsIgnoreCase("hola soy daniel"));

        // contains(): Verifica si la cadena contiene otra cadena
        System.out.println("8. ¿La cadena contiene 'Dan'? (contains): " + frase.contains("Dan"));

        // indexOf(): Devuelve la posición de la primera aparición de un carácter o subcadena
        System.out.println("9. Posición de 'D' en la cadena (indexOf): " + frase.indexOf('D'));

        // replace(): Reemplaza caracteres o subcadenas por otros
        System.out.println("10. Reemplazar 'Daniel' por 'Paco' (replace): " + frase.replace("Daniel", "Paco"));

        // trim(): Elimina espacios en blanco al inicio y al final de la cadena
        String espaciada = "   Hola Daniel   ";
        System.out.println("11. Cadena con espacios: '" + espaciada + "'");
        System.out.println("    Cadena sin espacios al inicio/final (trim): '" + espaciada.trim() + "'");

        // split(): Divide la cadena en partes usando un delimitador
        String lista = "manzana,pera,plátano";
        String[] frutas = lista.split(",");
        System.out.print("12. Frutas separadas (split): ");
        for (String fruta : frutas) {
            System.out.print(fruta + " ");
        }
        System.out.println();

        // startsWith() y endsWith(): Verifica si la cadena comienza o termina con una subcadena
        System.out.println("13. ¿La cadena comienza con 'Hola'? (startsWith): " + frase.startsWith("Hola"));
        System.out.println("14. ¿La cadena termina con 'do'? (endsWith): " + frase.endsWith("do"));
    }
}
// Programador: Daniel Hernandez Hernandez
// Programa para demostrar varios métodos de la clase String en Java
// Fecha: 20 de septiembre de 2025
