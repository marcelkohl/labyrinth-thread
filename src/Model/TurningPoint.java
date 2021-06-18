package Model;

/**
 *
 * @author Proprietário
 */
public class TurningPoint {
    private int contaDesvio=0;
    private int[] pontoColuna=new int[1000];
    private int[] pontoLinha=new int[1000];
    private Boolean[] pontoAtivo=new Boolean[1000];

    //construtor da classe
    public void PontoDesvio(){
        contaDesvio = 0;
    }

    /*
     * adiciona ponto de desvio na lista de desvios
     */
    public void addPontoDesvio(int posColuna, int posLinha){
        Boolean encontrou = false;

        //verifica se ponto ja nao existe
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

    /**
     * marca como inativo um ponto de desvio previamente cadastrado
     */
    public void inativaPontoDesvio(int posColuna, int posLinha){
        for(int todPontos=0; todPontos < contaDesvio; todPontos++){
            if (pontoColuna[todPontos] == posColuna && pontoLinha[todPontos] == posLinha){
                pontoAtivo[todPontos] = false;
            }
        }
    }

    /**
     *
     * @return o proximo ponto de desvio que esteja ativo, sendo o primeiro registro da matriz como a coluna e o segundo como a linha
     */
    public int[] getProximoPonto(){
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
