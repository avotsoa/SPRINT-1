package test.java;

import com.annotations.HandleUrl;

/**
 * Classe simple SANS annotation @Controller.
 * Les méthodes avec @HandleUrl fonctionnent quand même.
 */
public class SimpleClass {

    /**
     * Test API endpoint.
     * URL: /api/test
     */
    @HandleUrl("/api/test")
    public String apiTest() {
        return """
            <html>
            <head><title>API Test</title></head>
            <body>
                <h1>Test API</h1>
                <p>Cette methode est dans une classe SANS @Controller.</p>
                <pre>
{
  "status": "success",
  "message": "API endpoint fonctionne!",
  "timestamp": "2025-01-XX"
}
                </pre>
            </body>
            </html>
            """;
    }

    /**
     * Méthode normale sans annotation.
     */
    public void normalMethod() {
        System.out.println("Méthode normale non mappée.");
    }
}
