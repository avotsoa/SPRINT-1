package test.java;

import com.annotations.Controller;
import com.annotations.HandleUrl;
import com.framework.ModelView;

/**
 * Contrôleur pour la gestion des étudiants.
 * Sprint 6: Gestion des paramètres d'URL dynamiques {id}
 */
@Controller("EtudiantController")
public class EtudiantController {

    /**
     * Récupère un étudiant par son ID.
     * URL: /etudiant/{id}
     * Exemple: /etudiant/123 -> id = 123
     * 
     * @param id L'identifiant de l'étudiant
     * @return Une vue avec les informations de l'étudiant
     */
    @HandleUrl("/etudiant/{id}")
    public ModelView get(int id) {
        ModelView mv = new ModelView("/etudiant.jsp");
        
        // Simuler la recuperation d'un etudiant
        String nom = "Etudiant " + id;
        String email = "etudiant" + id + "@example.com";
        
        mv.addObject("id", id);
        mv.addString("nom", nom);
        mv.addString("email", email);
        mv.addString("message", "Etudiant recupere avec succes (Sprint 6)");
        
        return mv;
    }

    /**
     * Liste tous les étudiants.
     * URL: /etudiants
     */
    @HandleUrl("/etudiants")
    public String liste() {
        return """
            <html>
            <head><title>Liste des Etudiants</title></head>
            <body>
                <h1>Liste des Etudiants</h1>
                <ul>
                    <li><a href="/FrameworkResume/etudiant/1">Etudiant 1</a></li>
                    <li><a href="/FrameworkResume/etudiant/2">Etudiant 2</a></li>
                    <li><a href="/FrameworkResume/etudiant/3">Etudiant 3</a></li>
                    <li><a href="/FrameworkResume/etudiant/42">Etudiant 42</a></li>
                </ul>
                <p><a href="/FrameworkResume/home">Retour a l'accueil</a></p>
            </body>
            </html>
            """;
    }
}

