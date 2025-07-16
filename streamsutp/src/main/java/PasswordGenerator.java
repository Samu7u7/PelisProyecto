
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Scanner; // ¡Importa esto!

public class PasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Scanner scanner = new Scanner(System.in); // Para leer la entrada del usuario

        System.out.println("--- Generador y Verificador de Contraseñas BCrypt ---");

        System.out.print("1. Ingresa la contraseña en texto plano para hashear (ej. admin1234567): ");
        String rawPasswordToEncode = scanner.nextLine();

        String generatedHash = passwordEncoder.encode(rawPasswordToEncode);
        System.out.println("\n----------------------------------------------------");
        System.out.println("Hash generado para '" + rawPasswordToEncode + "':");
        System.out.println(generatedHash);
        System.out.println("----------------------------------------------------");
        System.out.println("\n--- COPIA ESTE HASH Y PÉGALO EN TU BASE DE DATOS AHORA ---");
        System.out.println("   Ejemplo: UPDATE usuarios SET password = '" + generatedHash + "' WHERE username = 'admin';");
        System.out.println("   (Asegúrate de que el 'username' o 'id' sea el correcto para el usuario admin)");


        System.out.print("\n2. Cuando hayas ACTUALIZADO la base de datos con este hash, presiona ENTER para continuar: ");
        scanner.nextLine(); // Esto hará que el programa espere hasta que presiones Enter

        System.out.println("\n3. Verificación final: Vamos a confirmar que el hash en tu BD es el correcto.");
        System.out.print("   Ingresa la contraseña en texto plano ORIGINAL nuevamente (ej. admin1234567): ");
        String rawPasswordToVerify = scanner.nextLine();

        System.out.print("   Ahora, por favor, VUELVE A COPIAR EL HASH EXACTO de la columna 'password' DE TU BASE DE DATOS (después de la actualización) y pégalo aquí: ");
        String hashFromDbAfterUpdate = scanner.nextLine();


        boolean matches = passwordEncoder.matches(rawPasswordToVerify, hashFromDbAfterUpdate);

        System.out.println("\n--- RESULTADO DE LA VERIFICACIÓN ---");
        System.out.println("Contraseña original ingresada: " + rawPasswordToVerify);
        System.out.println("Hash copiado de tu BD (después de actualizar): " + hashFromDbAfterUpdate);
        System.out.println("¿La contraseña original coincide con el hash de la BD? " + (matches ? "¡SÍ, COINCIDE!" : "NO COINCIDE. Hay un problema con el hash en tu BD."));
        System.out.println("------------------------------------");

        scanner.close(); // Cierra el scanner
        System.out.println("\n--- Fin del Generador/Verificador ---");
    }
}