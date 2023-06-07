package mx.ipn.escom.compiladores;

import java.util.ArrayList;
import java.util.List;

public class Arbol {
    private final List<Nodo> raiz;

    public Arbol(Nodo raiz){
        this.raiz = new ArrayList<>();
        this.raiz.add(raiz);
    }

    public Arbol(List<Nodo> raiz){
        this.raiz = raiz;
    }

    public void recorrer() 
    {
        if (raiz == null || raiz.isEmpty()) {
            return;
        }
        
        for (Nodo n : raiz) {
            Token t = n.getValue();
            switch (t.tipo) {
                case COMA:
                case PUNTO:
                case ASTERISCO:
                    System.out.println(t);
                    break;
            }
        }
    }
}

