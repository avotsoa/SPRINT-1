package test.java;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Employee pour tester le Sprint 8-bis.
 * Permet de recevoir un objet directement dans le controleur.
 */
public class Emp {
    private String name;
    private String email;
    private Integer age;
    private List<Departement> departement;
    
    public Emp() {
        this.departement = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    public List<Departement> getDepartement() {
        return departement;
    }
    
    public void setDepartement(List<Departement> departement) {
        this.departement = departement;
    }
    
    @Override
    public String toString() {
        return "Emp{name='" + name + "', email='" + email + "', age=" + age + 
               ", departement=" + departement + "}";
    }
}

