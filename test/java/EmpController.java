package test.java;

import com.annotations.Controller;
import com.annotations.GetMapping;
import com.annotations.PostMapping;
import com.framework.ModelView;

/**
 * Controleur pour tester le Sprint 8-bis: passage d'objets en parametre.
 */
@Controller("EmpController")
public class EmpController {
    
    /**
     * Affiche le formulaire pour creer un employe (GET).
     * URL: /emp/save
     */
    @GetMapping("/emp/save")
    public ModelView afficherFormulaire() {
        ModelView mv = new ModelView("/emp-formulaire.jsp");
        mv.addString("titre", "Formulaire Employe - Sprint 8-bis");
        mv.addString("message", "Remplissez le formulaire pour creer un employe");
        return mv;
    }
    
    /**
     * Sprint 8-bis: Traite le formulaire avec un objet Emp directement.
     * URL: /emp/save
     * 
     * @param e L'objet Emp instancie automatiquement avec les parametres du formulaire
     */
    @PostMapping("/emp/save")
    public ModelView save(Emp e) {
        ModelView mv = new ModelView("/emp-resultat.jsp");
        mv.addString("titre", "Employe cree - Sprint 8-bis");
        mv.addString("message", "Employe cree avec succes!");
        mv.addObject("emp", e);
        return mv;
    }
    
    /**
     * Sprint 9: Affiche le formulaire pour creer un employe avec retour JSON (GET).
     * URL: /emp/json
     */
    @GetMapping("/emp/json")
    public ModelView afficherFormulaireJson() {
        ModelView mv = new ModelView("/emp-formulaire-json.jsp");
        mv.addString("titre", "Formulaire Employe - Sprint 9 (JSON)");
        mv.addString("message", "Remplissez le formulaire pour recevoir les donnees en JSON");
        return mv;
    }
    
    /**
     * Sprint 9: Traite le formulaire et retourne les donnees en format JSON.
     * URL: /emp/json
     * 
     * @param e L'objet Emp instancie automatiquement avec les parametres du formulaire
     */
    @PostMapping("/emp/json")
    public String saveJson(Emp e) {
        // Construire manuellement le JSON
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"name\": \"").append(e.getName() != null ? e.getName() : "").append("\",\n");
        json.append("  \"email\": \"").append(e.getEmail() != null ? e.getEmail() : "").append("\",\n");
        json.append("  \"age\": ").append(e.getAge() != null ? e.getAge() : "null").append(",\n");
        json.append("  \"departement\": [\n");
        
        if (e.getDepartement() != null && !e.getDepartement().isEmpty()) {
            for (int i = 0; i < e.getDepartement().size(); i++) {
                Departement dept = e.getDepartement().get(i);
                json.append("    {\n");
                json.append("      \"name\": \"").append(dept.getName() != null ? dept.getName() : "").append("\",\n");
                json.append("      \"code\": \"").append(dept.getCode() != null ? dept.getCode() : "").append("\"\n");
                json.append("    }");
                if (i < e.getDepartement().size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
        }
        
        json.append("  ]\n");
        json.append("}");
        
        return json.toString();
    }
}

