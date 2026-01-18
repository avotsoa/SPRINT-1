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
            <head>
                <title>Page d'accueil</title>
                <style>
                    body { font-family: Arial, sans-serif; max-width: 900px; margin: 50px auto; padding: 20px; background-color: #f5f5f5; }
                    .container { background-color: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
                    h1 { color: #333; border-bottom: 2px solid #4CAF50; padding-bottom: 10px; }
                    h2 { color: #555; margin-top: 30px; }
                    .sprint-section { background-color: #f9f9f9; padding: 15px; margin: 15px 0; border-radius: 4px; border-left: 4px solid #4CAF50; }
                    a { color: #4CAF50; text-decoration: none; margin-right: 15px; }
                    a:hover { text-decoration: underline; }
                    .sprint-title { font-weight: bold; color: #333; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>Bienvenue sur le Framework!</h1>
                    <p>Ceci est la page d'accueil generee par le controleur TestController.</p>
                    
                    <h2>Tests des Sprints</h2>
                    
                    <div class="sprint-section">
                        <p class="sprint-title">Sprint 8-bis: Passage d'objets en parametre</p>
                        <p><a href="/FrameworkResume/emp/save">Formulaire Employe (HTML)</a></p>
                    </div>
                    
                    <div class="sprint-section">
                        <p class="sprint-title">Sprint 9: Retour JSON</p>
                        <p><a href="/FrameworkResume/emp/json">Formulaire Employe (JSON)</a></p>
                    </div>
                    
                    <div class="sprint-section">
                        <p class="sprint-title">Sprint 11: Gestion de Session</p>
                        <p><a href="/FrameworkResume/emp/session">Gestion de Session HTTP</a></p>
                    </div>
                    
                    <h2>Navigation</h2>
                    <p>
                        <a href="/FrameworkResume/home">Accueil</a> | 
                        <a href="/FrameworkResume/utilisateurs">Utilisateurs</a> | 
                        <a href="/FrameworkResume/produits">Produits</a>
                    </p>
                </div>
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
        return new ModelView("/test.jsp")
                .addString("message", "Bonjour depuis ModelView.data (Sprint5)");
    }

}
