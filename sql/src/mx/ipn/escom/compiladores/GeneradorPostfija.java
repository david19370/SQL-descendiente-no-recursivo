package mx.ipn.escom.compiladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GeneradorPostfija {

    private final List<Token> infija;
    private final Stack<Token> pila;
    private final List<Token> postfija;

    public GeneradorPostfija(List<Token> infija) {
        this.infija = infija;
        this.pila = new Stack<>();
        this.postfija = new ArrayList<>();
    }

    public List<Token> convertir(){
        for(Token t : infija){
            if(t.tipo == TipoToken.EOF){
                continue;
            }

            if(t.esPalabraReservada()){
                postfija.add(t);
            }
            else if(t.esPalabraReservada()){
                while(!pila.isEmpty()){
                    Token temp = pila.pop();
                    postfija.add(temp);
                }
                pila.push(t);
            }
        }
        while(!pila.isEmpty()){
            Token temp = pila.pop();
            postfija.add(temp);
        }

        return postfija;
    }

}
