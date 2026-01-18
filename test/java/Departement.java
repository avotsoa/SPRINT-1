package test.java;

/**
 * Classe Departement pour tester les objets imbriques dans Sprint 8-bis.
 */
public class Departement {
    private String name;
    private String code;
    
    public Departement() {
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    @Override
    public String toString() {
        return "Departement{name='" + name + "', code='" + code + "'}";
    }
}

