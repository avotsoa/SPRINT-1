package test.java;

/**
 * Classe SANS annotation @Controller pour demonstration.
 * Cette classe ne sera PAS traitee par le FrontServlet car elle n'a pas @Controller.
 */
public class SimpleService {
    
    /**
     * Methode qui ne sera jamais appelee via URL car la classe n'a pas @Controller
     */
    public String getInfo() {
        return "Cette methode ne sera jamais accessible via URL";
    }
    
    /**
     * Autre methode sans annotation
     */
    public void doSomething() {
        System.out.println("Cette classe n'a pas @Controller");
    }
}

