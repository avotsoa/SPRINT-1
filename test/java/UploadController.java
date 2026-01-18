package test.java;

import com.annotations.Controller;
import com.annotations.GetMapping;
import com.annotations.PostMapping;
import com.framework.ModelView;
import java.util.Map;

/**
 * Controleur pour tester le Sprint 10: Upload de fichiers.
 */
@Controller("UploadController")
public class UploadController {
    
    /**
     * Affiche le formulaire d'upload (GET).
     * URL: /upload
     */
    @GetMapping("/upload")
    public ModelView afficherFormulaire() {
        ModelView mv = new ModelView("/upload-formulaire.jsp");
        mv.addString("titre", "Upload de fichiers - Sprint 10");
        mv.addString("message", "Selectionnez un ou plusieurs fichiers a uploader");
        return mv;
    }
    
    /**
     * Sprint 10: Traite l'upload de fichiers avec Map<String,Byte[]>.
     * URL: /upload
     * 
     * @param files Map contenant les fichiers uploades (nom du champ -> contenu du fichier en Byte[])
     */
    @PostMapping("/upload")
    public ModelView save(Map<String, Byte[]> files) {
        ModelView mv = new ModelView("/upload-resultat.jsp");
        mv.addString("titre", "Fichiers uploades - Sprint 10");
        mv.addString("message", "Fichiers uploades avec succes!");
        mv.addObject("files", files);
        mv.addObject("filesCount", files != null ? files.size() : 0);
        
        // Calculer la taille totale des fichiers
        long totalSize = 0;
        if (files != null) {
            for (Byte[] fileBytes : files.values()) {
                if (fileBytes != null) {
                    totalSize += fileBytes.length;
                }
            }
        }
        mv.addObject("totalSize", totalSize);
        
        return mv;
    }
}

