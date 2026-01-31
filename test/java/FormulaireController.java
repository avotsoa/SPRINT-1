package test.java;

import com.annotations.Controller;
import com.annotations.GetMapping;
import com.annotations.PostMapping;
import com.annotations.RequestMapping;
import com.framework.ModelView;
import java.util.Map;

/**
 * Contrôleur pour tester les méthodes HTTP GET et POST (Sprint 7).
 */
@Controller("FormulaireController")
public class FormulaireController {

    /**
     * Affiche le formulaire (GET).
     * URL: /formulaire
     */
    @GetMapping("/formulaire")
    public ModelView afficherFormulaire() {
        ModelView mv = new ModelView("/formulaire.jsp");
        mv.addString("message", "Veuillez remplir le formulaire");
        mv.addString("titre", "Formulaire de test - Sprint 7");
        return mv;
    }

    /**
     * Traite le formulaire soumis (POST).
     * URL: /formulaire
     */
    @PostMapping("/formulaire")
    public ModelView traiterFormulaire() {
        ModelView mv = new ModelView("/formulaire-resultat.jsp");
        mv.addString("message", "Formulaire traite avec succes (POST)");
        mv.addString("titre", "Resultat du formulaire");
        return mv;
    }

    /**
     * Sprint 8: Traite le formulaire avec Map<String,String> qui contient tous les parametres.
     * URL: /formulaire-map
     */
    @PostMapping("/formulaire-map")
    public ModelView traiterFormulaireAvecMap(Map<String, String> params) {
        ModelView mv = new ModelView("/formulaire-map-resultat.jsp");
        mv.addString("message", "Formulaire traite avec Map<String,String> (Sprint 8)");
        mv.addString("titre", "Resultat avec Map - Sprint 8");
        mv.addObject("params", params);
        mv.addObject("paramsCount", params != null ? params.size() : 0);
        return mv;
    }

    /**
     * Affiche le formulaire pour tester Map<String,String> (GET).
     * URL: /formulaire-map
     */
    @GetMapping("/formulaire-map")
    public ModelView afficherFormulaireMap() {
        ModelView mv = new ModelView("/formulaire-map.jsp");
        mv.addString("message", "Formulaire de test pour Map<String,String> (Sprint 8)");
        mv.addString("titre", "Formulaire Map - Sprint 8");
        return mv;
    }

    /**
     * Page de liste (GET uniquement).
     * URL: /liste
     */
    @GetMapping("/liste")
    public String liste() {
        return """
            <html>
            <head><title>Liste - GET uniquement</title></head>
            <body>
                <h1>Liste des elements</h1>
                <p>Cette page n'accepte que les requetes GET.</p>
                <ul>
                    <li>Element 1</li>
                    <li>Element 2</li>
                    <li>Element 3</li>
                </ul>
                <p><a href="/FrameworkResume/formulaire">Aller au formulaire</a></p>
                <p><a href="/FrameworkResume/home">Retour a l'accueil</a></p>
            </body>
            </html>
            """;
    }

    /**
     * Exemple avec RequestMapping pour spécifier la méthode HTTP.
     * URL: /api/data (GET uniquement)
     */
    @RequestMapping(value = "/api/data", method = "GET")
    public String apiData() {
        return """
            <html>
            <head><title>API Data</title></head>
            <body>
                <h1>API Data - GET</h1>
                <p>Cette route utilise @RequestMapping avec method = "GET"</p>
                <pre>{"status": "success", "data": "example"}</pre>
                <p><a href="/FrameworkResume/home">Retour a l'accueil</a></p>
            </body>
            </html>
            """;
    }
}

