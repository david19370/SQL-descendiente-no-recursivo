package mx.ipn.escom.compiladores;

import java.util.List;
import java.util.Stack;

public class Parser 
{
    private final List<Token> tokens;
    private final Token identificador = new Token(TipoToken.IDENTIFICADOR, " ");
    private final Token select = new Token(TipoToken.SELECT, "select");
    private final Token from = new Token(TipoToken.FROM, "from");
    private final Token distinct = new Token(TipoToken.DISTINCT, "distinct");
    private final Token coma = new Token(TipoToken.COMA, ",");
    private final Token punto = new Token(TipoToken.PUNTO, ".");
    private final Token asterisco = new Token(TipoToken.ASTERISCO, "*");
    private final Token finCadena = new Token(TipoToken.EOF, "");

    private int i = 0; 
    private int top = 0;
    private boolean hayErrores = false; 
    private Token preanalisis;
    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    Stack <String> pila = new Stack<>();
    public void parse() {
        i = 0; 
        top = 0;
        preanalisis = tokens.get(i);
        pila.push("");
        pila.push("Q");

        while(true)
        {
            Q(); 
            if(i <= tokens.size()-1) 
            {
                if (pila.get(top).equals(finCadena.lexema))
                {
                    System.out.println("Consulta válida");
                    break;
                }
                else
                {
                    System.out.println("Consulta no válida");
                    break;
                }
            }
        }
    }

    void Q() {
        if (hayErrores) return;
        if (pila.get(top + 1).equals("Q")) {
            pila.pop();
            pila.push("T");
            top++;
            pila.push("FROM");
            top++;
            pila.push("D");
            top++;
            pila.push("SELECT");
            top++;
            String lex = "";
            lex = String.valueOf(preanalisis.tipo); 
            if (lex.equals(pila.get(top)))
            {
                pila.pop(); 
                top--; 
                coincidir(select);
                D();
            }
            else
            {
               hayErrores = true;
               System.out.println("Error en la posición " + preanalisis.posicion + "("+ preanalisis.lexema +")" + "Se esperaba un select");
            }
        }
    }
    void D()
    {
        if (hayErrores) return;
        if (preanalisis.equals(distinct))
        {
            pila.pop(); 
            pila.push("P");
            pila.push("DISTINCT");
            top++;
            String lex = "";
            lex = String.valueOf(preanalisis.tipo); 

            if (lex.equals(pila.get(top)))
            {
                coincidir(distinct);
                pila.pop(); 
                top--;
                P();
                if (hayErrores) return;
            }
        }
        else if (preanalisis.equals(asterisco)) 
        {
            pila.pop(); 
            pila.push("P"); 
            P();
            if (hayErrores) return;
        }
        else if (preanalisis.equals(identificador)) 
        {
            pila.pop(); 
            pila.push("P"); 
            P();
            if (hayErrores) return;
        }
        else
        {
            hayErrores = true;
            System.out.println("Error en la posición: " + preanalisis.posicion + "("+ preanalisis.lexema +")" + ". Se esperaba un distinct, * o un identificador ");
            if (hayErrores) return;
        }

        String lex = "";
        lex = String.valueOf(preanalisis.tipo); 

        if (lex.equals(pila.get(top)))
        {
            pila.pop(); 
            coincidir(from);
            top--;
            T();
        }
        else
        {
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + "("+ preanalisis.lexema +")" + ". Se esperaba un from");
        }
    }
    void P()
    {
        if (hayErrores) return;
        if (preanalisis.equals(asterisco))
        {
            pila.pop(); 
            pila.push("ASTERISCO");
            String lex = "";
            lex = String.valueOf(preanalisis.tipo); 
            if (lex.equals(pila.get(top)))
            {
                coincidir(asterisco);
                pila.pop();
                top--;
            }
        }
        else if (preanalisis.equals(identificador)) 
        {
            pila.pop(); 
            pila.push("A");
            A();
        }
        else
        {
            hayErrores = true;
            System.out.println("Error en la posicion " + preanalisis.posicion + "(" + preanalisis.lexema + ")" + ". Se esperaba un * o un identificador");
        }
    }
    void A()
    {
        pila.pop();
        pila.push("A1");
        pila.push("A2");
        top++;
        A2();
        if (hayErrores) return; 
        A1();
    }
    void A1()
    {
        if (tokens.get(i+1).equals(identificador) && preanalisis.equals(coma))
        {
            pila.pop();
            pila.push("A");
            pila.push("COMA");
            top++;
            String lex = "";
            lex = String.valueOf(preanalisis.tipo);
            if (lex.equals(pila.get(top)))
            {
                coincidir(coma);
                pila.pop();
                top--;
                A();
            }
        } 
        else if (preanalisis.equals(from)) 
        {
            pila.pop(); 
            top--;
        }
    }
    void A2()
    {
        if(hayErrores) return;
        pila.pop(); 
        pila.push("A3");
        pila.push("IDENTIFICADOR");
        top++;
        String lex = "";
        lex = String.valueOf(preanalisis.tipo); 
        if (lex.equals(pila.get(top))) 
        {
            coincidir(identificador);
            pila.pop(); 
            top--;
            A3();
        }
        else
        {
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + "(" + preanalisis.lexema + ")" + ". Se esperaba un identificador");
        }
    }
    void A3()
    {
        if (hayErrores) return;
        if (preanalisis.equals(punto)) 
        {
            pila.pop();
            pila.push("IDENTIFICADOR");
            pila.push("PUNTO");
            top++;
            String lex = "";
            lex = String.valueOf(preanalisis.tipo);
            if (lex.equals(pila.get(top)))
            {
                coincidir(punto);
                pila.pop(); 
                top--;

                lex = String.valueOf(preanalisis.tipo);
                if (lex.equals(pila.get(top))) 
                {
                    coincidir(identificador);
                    pila.pop();
                    top--;
                }
                else
                {
                    hayErrores = true;
                    System.out.println("Error en la posición: " + preanalisis.posicion + "(" + preanalisis.lexema + ")" + ". Se esperaba un identificador");
                }
            }
        }
        else if (preanalisis.equals(from) || preanalisis.equals(coma))
        {
            pila.pop();
            top--;
        }
    }
    void T()
    {
        pila.pop(); 
        pila.push("T1");
        pila.push("T2");
        top++;
        T2();
        if (hayErrores) return; 
        T1();
        if (hayErrores) return; 

        if (preanalisis.equals(finCadena)) 
            top = 0;
    }
    void T1()
    {
        if (hayErrores) return;

        if (preanalisis.equals(coma))
        {
            pila.pop(); 
            pila.push("T");
            pila.push("COMA");
            top++;
            String lex = "";
            lex = String.valueOf(preanalisis.tipo); 

            if (lex.equals(pila.get(top)))
            {
                coincidir(coma);
                top--;
                pila.pop(); 
                T();
            }
        }
        else if (preanalisis.equals(finCadena))
        {
            pila.pop();
            top--;
        }
        else if ((tokens.get(i-1).equals(identificador) && tokens.get(i-2).equals(identificador)) || (tokens.get(i-1).equals(identificador)&&tokens.get(i+1).equals(identificador)))
        {
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + "("+ preanalisis.lexema +")" + ". Se esperaba una coma");
        }
    }
    void T2()
    {
        if(hayErrores) return;
        pila.pop(); 
        pila.push("T3");
        pila.push("IDENTIFICADOR");
        top++;
        String lex = "";
        lex = String.valueOf(preanalisis.tipo); 

        if (lex.equals(pila.get(top)))
        {
            coincidir(identificador);
            top--;
            pila.pop(); 
                T3();
        }
        else
        {
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + "("+ preanalisis.lexema +")" + ". Se esperaba un identificador");
        }
    }

    void T3()
    {
        if (preanalisis.equals(identificador))
        {
            pila.pop();
            pila.push("IDENTIFICADOR");

            String lex = "";
            lex = String.valueOf(preanalisis.tipo); 

            if (lex.equals(pila.get(top)))
            {
                coincidir(identificador);
                top--;
                pila.pop(); 
            }
        } else if (preanalisis.equals(coma)) {
            pila.pop(); 
            top--;
        }
    }
    void coincidir(Token t)
    {
        if(hayErrores) return;

        if(preanalisis.tipo == t.tipo){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + "(" + preanalisis.lexema + ")" + ". Se esperaba un  " + t.tipo);
        }
    }

}