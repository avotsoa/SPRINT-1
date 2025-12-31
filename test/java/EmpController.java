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
}

