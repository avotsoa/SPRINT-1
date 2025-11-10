package test.java;

import com.annotations.Controller;
import com.annotations.HandleUrl;

/**
 * Controleur pour la gestion des produits.
 */
@Controller("ProduitController")
public class ProduitController {

    /**
     * Liste des produits.
     * URL: /produits
     */
    @HandleUrl("/produits")
    public String liste() {
        return """
            <html>
            <head><title>Liste Produits</title></head>
            <body>
                <h1>Liste des Produits</h1>
                <table border="1">
                    <tr>
                        <th>ID</th>
                        <th>Nom</th>
                        <th>Prix</th>
                    </tr>
                    <tr>
                        <td>1</td>
                        <td>Produit A</td>
                        <td>100 Ar</td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td>Produit B</td>
                        <td>200 Ar</td>
                    </tr>
                    <tr>
                        <td>3</td>
                        <td>Produit C</td>
                        <td>150 Ar</td>
                    </tr>
                </table>
                <p><a href="/home">Retour a l'accueil</a></p>
            </body>
            </html>
            """;
    }

}
