package mx.ipn.escom.compiladores;

import java.util.ArrayList;
import java.util.List;

public class Nodo {
    private final Token value;
    private List<Nodo> hijos;

    public Nodo(Token value){
        this.value = value;
    }

    public void insertarHijo(Nodo n){
        if(hijos == null){
            hijos = new ArrayList<>();
            hijos.add(n);
        }
        else{
            hijos.add(0, n);
        }
    }

    public Token getValue(){
        return value;
    }

    public List<Nodo> getHijos(){
        return hijos;
    }
}
