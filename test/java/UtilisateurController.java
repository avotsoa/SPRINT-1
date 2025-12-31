package test.java;

import com.annotations.Controller;
import com.annotations.HandleUrl;

/**
 * Controleur pour la gestion des utilisateurs.
 */
@Controller("UtilisateurController")
public class UtilisateurController {

    /**
     * Liste des utilisateurs.
     * URL: /utilisateurs
     */
    @HandleUrl("/utilisateurs")
    public String liste() {
        return """
            <html>
            <head><title>Liste Utilisateurs</title></head>
            <body>
                <h1>Liste des Utilisateurs</h1>
                <ul>
                    <li>Utilisateur 1 - admin@test.com</li>
                    <li>Utilisateur 2 - user1@test.com</li>
                    <li>Utilisateur 3 - user2@test.com</li>
                </ul>
                <p><a href="/FrameworkResume">Retour a l'accueil</a></p>
            </body>
            </html>
            """;
    }

}
