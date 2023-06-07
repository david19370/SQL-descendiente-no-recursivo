package mx.ipn.escom.compiladores;

public class Token {

    final TipoToken tipo;
    final String lexema;

    final int posicion;

    public Token(TipoToken tipo, String lexema, int posicion) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.posicion = posicion;
    }

    public Token(TipoToken tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.posicion = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Token)) {
            return false;
        }

        if(this.tipo == ((Token)o).tipo){
            return true;
        }

        return false;
    }

    public String toString(){
        return tipo + " " + lexema + " ";
    }

    public boolean esPalabraReservada(){
        switch (this.tipo){
            case SELECT:
            case FROM:
            case DISTINCT:
                return true;
            
            default:
                return false;
        }
    }

    public boolean esCaracter(){
        switch (this.tipo){
            case COMA:
            case PUNTO:
            case ASTERISCO:
                return true;
            default:
                return false;
        }
    }

    public int aridad(){
        switch (this.tipo) {
            case COMA:
            case PUNTO:
            case ASTERISCO:
                return 2;
        }
        return 0;
    }

    public TipoToken getTipo() {
        return tipo;
    }

}
