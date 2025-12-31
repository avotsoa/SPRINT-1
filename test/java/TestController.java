package test.java;

import com.annotations.Controller;
import com.annotations.HandleUrl;
import com.framework.ModelView;

/**
 * Contrôleur de test avec l'annotation @Controller.
 * Contient des méthodes mappées à des URLs avec @HandleUrl.
 */
@Controller("TestController")
public class TestController {

    /**
     * Page d'accueil du test.
     * URL: /home
     */
    @HandleUrl("/home")
    public String home() {
        return """
            <html>
            <head><title>Page d'accueil</title></head>
            <body>
                <h1>Bienvenue sur le Framework!</h1>
                <p>Ceci est la page d'accueil generee par le controleur TestController.</p>
                <p><a href="/FrameworkResume/home">Accueil</a> | <a href="/utilisateurs">Utilisateurs</a> | <a href="/produits">Produits</a></p>
            </body>
            </html>
            """;
    }

    /**
     * Sprint4-bis: retourne un ModelView pour forward vers test.jsp
     * URL: /mv
     */
    @HandleUrl("/mv")
    public ModelView mv() {
        return new ModelView("/test.jsp");
    }

}
