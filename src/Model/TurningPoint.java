package Model;

/**
 * @author Marcel V. Kohls
 */
public class TurningPoint {
    private int contaDesvio=0;
    private int[] pontoColuna=new int[1000];
    private int[] pontoLinha=new int[1000];
    private Boolean[] pontoAtivo=new Boolean[1000];

    public void TurningPoint(){
        contaDesvio = 0;
    }

    public void addTurningPoint(int posColuna, int posLinha){
        Boolean encontrou = false;

        for (int todPontos=0; todPontos < contaDesvio; todPontos++){
            if (pontoColuna[todPontos] == posColuna && pontoLinha[todPontos] == posLinha){
                encontrou = true;
            }
        }

        //se nao existe, entao inclui
        if (encontrou == false){
            pontoColuna[contaDesvio] = posColuna;
            pontoLinha[contaDesvio] = posLinha;
            pontoAtivo[contaDesvio] = true;

            contaDesvio++;
        }
    }

    public void disableTurningPoint(int posColuna, int posLinha){
        for(int todPontos=0; todPontos < contaDesvio; todPontos++){
            if (pontoColuna[todPontos] == posColuna && pontoLinha[todPontos] == posLinha){
                pontoAtivo[todPontos] = false;
            }
        }
    }

    public int[] getNextPoint(){
        int[] retorno = new int[2];

        //procura o proximo desvio ativo
        retorno[0] = -1;
        retorno[1] = -1;

        int todDesvio = 0;

        while (todDesvio < contaDesvio && retorno[1]==-1){
            if (pontoAtivo[todDesvio] == true){
                retorno[0] = pontoColuna[todDesvio];
                retorno[1] = pontoLinha[todDesvio];
            }

            todDesvio++;
        }

        return retorno;
    }
}
