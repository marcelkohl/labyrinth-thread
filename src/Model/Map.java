package Model;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Random;

/**
 * @author Marcel Vinicius Kohls
 */
public class Map {
    static int BLOCK_SIZE = 34;
    static int STEP_INTERVAL = 6;

    static int DIR_NONE = 0;
    static int DIR_UP = 1;
    static int DIR_DOWN = 2;
    static int DIR_RIGHT = 3;
    static int DIR_LEFT = 4;

    private int actualDirection = DIR_NONE;
    private int imgSpot = -1;

    private int maxRows;
    private int maxCols;

    private Boolean isFinishSet = false;

    private BufferedImage grama;
    private BufferedImage inicio;
    private BufferedImage chegada1;
    private BufferedImage chegada2;
    private BufferedImage chegada3;
    private BufferedImage chegada4;
    private BufferedImage terra0;
    private BufferedImage terra1;
    private BufferedImage terra2;
    private BufferedImage terra3;
    private BufferedImage terra4;
    private BufferedImage terra6;
    private BufferedImage terra5;
    private BufferedImage terra7;
    private BufferedImage terra8;
    private BufferedImage terra9;
    private BufferedImage terra10;
    private BufferedImage terra11;
    private BufferedImage terra12;
    private BufferedImage terra13;
    private BufferedImage terra14;

    private int[][] globalMap;

    private TurningPoint turningPoints = new TurningPoint();
    private Random rnd = new Random();

    public Map(int rows, int cols){
        try {
            grama = ImageIO.read(new File("Theme/grass.png"));
            inicio = ImageIO.read(new File("Theme/start.png"));
            chegada1= ImageIO.read(new File("Theme/finish1.png"));
            chegada2= ImageIO.read(new File("Theme/finish2.png"));
            chegada3= ImageIO.read(new File("Theme/finish3.png"));
            chegada4= ImageIO.read(new File("Theme/finish4.png"));
            terra0 = ImageIO.read(new File("Theme/floor-lr.png"));
            terra1 = ImageIO.read(new File("Theme/floor-lu.png"));
            terra2 = ImageIO.read(new File("Theme/floor-ud.png"));
            terra3 = ImageIO.read(new File("Theme/floor-ld.png"));
            terra4 = ImageIO.read(new File("Theme/floor-ldr.png"));
            terra5 = ImageIO.read(new File("Theme/floor-dr.png"));
            terra6 = ImageIO.read(new File("Theme/floor-dru.png"));
            terra7 = ImageIO.read(new File("Theme/floor-ru.png"));
            terra8 = ImageIO.read(new File("Theme/floor-lru.png"));
            terra9 = ImageIO.read(new File("Theme/floor-ldru.png"));
            terra10 = ImageIO.read(new File("Theme/floor-ldu.png"));
            terra11 = ImageIO.read(new File("Theme/floor-d.png"));
            terra12 = ImageIO.read(new File("Theme/floor-u.png"));
            terra13 = ImageIO.read(new File("Theme/floor-l.png"));
            terra14 = ImageIO.read(new File("Theme/floor-r.png"));
        } catch (Exception e) {
            // show error msgs
        }

        maxRows  = rows;
        maxCols = cols;

        globalMap = new int[100][100];

        this.generateDepthMap();
    }

    public int getMaxRows(){
        return maxRows;
    }

    public int getBlockSize(){
        return BLOCK_SIZE;
    }

    public int getMaxColumns(){
        return maxCols;
    }

    public int getMapBlock(int colPos, int rowPos){
        if (colPos >= 0 && rowPos >= 0 && colPos <= maxCols && rowPos <= maxRows){
            return globalMap[colPos][rowPos];
        } else {
            return -1;
        }
    }

    private void resetGlobalMap() {
        for (int todcol=0; todcol<=maxCols; todcol++){
            for (int todlin=0; todlin<=maxRows; todlin++){
                globalMap[todcol][todlin] = -1;
            }
        }
    }
    
    public void generateDepthMap(){
        resetGlobalMap();

        //comeca a trilha
        int contaHorizontal = 0;
        int contaVertical = this.RandomInRange(1, maxRows-1, rnd);
        int novoPasso;
        int[] novoPonto;
        Boolean oportunidade = true;
        Boolean marcouInicio = false;

        actualDirection = DIR_RIGHT;
        imgSpot = 1;

        while (oportunidade == true){
            int proxDesvio = this.RandomInRange(1, 6, rnd);
            int contaDesvio = 0;

            // percorre caminho até a marcação do próximo caminho
            while (contaDesvio <= proxDesvio){

                novoPasso = this.continuarCaminho(contaHorizontal, contaVertical);

                // se minha posicao atual nao me permite andar em mais nenhuma direcao, entao marca este ponto de desvio como invalido
                if (actualDirection == DIR_NONE){
                    // inativa ponto de desvio
                    turningPoints.inativaPontoDesvio(contaHorizontal, contaVertical);

                    //encontra novo ponto de desvio ativo
                    novoPonto = turningPoints.getProximoPonto();

                    //se nao exite outro ponto de desvio, oportunidades acabaram. fim do mapa
                    if (novoPonto[0] == -1 && novoPonto[1] == -1){
                        oportunidade = false;
                        contaDesvio = proxDesvio +1; //para sair do laco de contador de desvio
                    } else {
                        contaHorizontal = novoPonto[0];
                        contaVertical = novoPonto[1];
                        actualDirection = DIR_NONE;
                        actualDirection = this.novaDirecao(contaHorizontal, contaVertical);
                    }

                }
                // se nao puder mais andar para o camimnho que estava andando, entao descobre uma nova direcao]
                else if(novoPasso == 0){
                    actualDirection = this.novaDirecao(contaHorizontal, contaVertical);
                }
                // senao, continua andando
                else {
                    //incrementa o deslocamento para podermos sair do lugar
                    if (actualDirection == DIR_RIGHT || actualDirection == DIR_LEFT){
                        contaHorizontal = contaHorizontal + novoPasso;
                        imgSpot = 0;
                    }
                    else if (actualDirection == DIR_UP || actualDirection == DIR_DOWN){
                        contaVertical = contaVertical + novoPasso;
                        imgSpot = 2;
                    }

                    if (marcouInicio == false){
                        marcouInicio = true;
                        imgSpot = 99;
                    }

                    // marca este ponto que acabamos de entrar na matriz
                    globalMap[contaHorizontal][contaVertical] = imgSpot;

                    contaDesvio++;

                }//fim do marca passo andado

            } // fim do laco do desvio

            // cada vez q chegamos no desvio, decidimos uma nova direçao
            actualDirection = this.novaDirecao(contaHorizontal, contaVertical);

       }// fim do laco que faz o labirinto
        
      this.setPontoFinal();
    }// fim do metodo que desenha o mapa por profundidade

    /**
     * define o ponto final na matriz do mapa
     */
    public void setPontoFinal(){
        int posOpcional = 0;
        int todColuna = maxCols;
        Boolean marcou = false;

        //procura um ponto final que seja um terminador
        while (marcou == false && todColuna > 0){
            for (int todLinha = 0; todLinha <= maxRows; todLinha++){
                if (marcou == false && globalMap[todColuna][todLinha] == 11){
                    System.out.println("fim 11");
                    globalMap[todColuna][todLinha] = 98;
                    marcou = true;
                }
                else if(marcou == false && globalMap[todColuna][todLinha] == 12)
                {
                    System.out.println("fim 12");
                    globalMap[todColuna][todLinha] = 97;
                    marcou = true;
                }
                if (marcou == false && globalMap[todColuna][todLinha] == 13){
                    System.out.println("fim 13");
                    globalMap[todColuna][todLinha] = 96;
                    marcou = true;
                }
                if (marcou == false && globalMap[todColuna][todLinha] == 14){
                    System.out.println("fim 14");
                    globalMap[todColuna][todLinha] = 95;
                    marcou = true;
                }

            }//fim do for das linhas q procuram o final

            todColuna--;
        }// fim do while de colunas q procuram o final

    }// fim do metodo que encontra o ponto final

    /*
     * defina uma nova direcao para o gerador do mapa por profundidade. se retornar DIR_NONE, entao e porque nao tem mais caminhos e o mapa deverá finalizar o processo de criação
     */
    public int novaDirecao(int atualColuna, int atualLinha){
        Boolean tentaDir=false;
        Boolean tentaEsq=false;
        Boolean tentaCima=false;
        Boolean tentaBaix=false;
        int retorno = DIR_NONE;
        int direcao;
        int velhaDirecao = actualDirection;


        //define a direcao atual como true. problemas se nao fizer isso
        if (actualDirection == DIR_UP){
            tentaCima = true;
        }
        else if (actualDirection == DIR_DOWN){
            tentaBaix = true;
        }
        else if (actualDirection == DIR_LEFT){
            tentaEsq = true;
        }
        else if (actualDirection == DIR_RIGHT){
            tentaDir = true;
        }

        //agora sim procura a nova direcao, ja desconsiderando a atual direcao
        while (retorno == DIR_NONE && (tentaDir==false || tentaEsq==false || tentaCima==false || tentaBaix==false)){
            direcao = this.RandomInRange(1,4,rnd);

            if (direcao != actualDirection) {
                retorno = direcao;
            }

            // marca as tentativas q nao pode mais ir
            if (direcao == DIR_LEFT && (atualColuna-1 == 0 || globalMap[atualColuna-1][atualLinha] != -1) ){
                tentaEsq=true;
                retorno = DIR_NONE;
            }

            else if (direcao == DIR_RIGHT && (atualColuna+1 == maxCols || globalMap[atualColuna+1][atualLinha] != -1)){
                tentaDir=true;
                retorno = DIR_NONE;
            }

            else if (direcao == DIR_UP && (atualLinha-1 == 0 || globalMap[atualColuna][atualLinha-1] != -1)){
                tentaCima=true;
                retorno = DIR_NONE;
            }

            else if (direcao == DIR_DOWN && (atualLinha+1 == maxRows || globalMap[atualColuna][atualLinha+1] != -1)){
                tentaBaix=true;
                retorno = DIR_NONE;
            }
        }// fim da conferencia da nova possivel direcao

        if (retorno != DIR_NONE){
            turningPoints.addPontoDesvio(atualColuna, atualLinha);
        }

        //marca ponto de onde saimos. Se tiver uma mudanca de rota, remarcamos este ponto.
        if (velhaDirecao == DIR_NONE){
            if (globalMap[atualColuna][atualLinha] == 5 && retorno == DIR_UP){
                globalMap[atualColuna][atualLinha] = 6;
            }
            else if (globalMap[atualColuna][atualLinha] == 5 && retorno == DIR_LEFT){
                globalMap[atualColuna][atualLinha] = 4;
            }
            else if (globalMap[atualColuna][atualLinha] == 7 && retorno == DIR_DOWN){
                globalMap[atualColuna][atualLinha] = 6;
            }
            else if (globalMap[atualColuna][atualLinha] == 7 && retorno == DIR_LEFT){
                globalMap[atualColuna][atualLinha] = 8;
            }
            else if (globalMap[atualColuna][atualLinha] == 1 && retorno == DIR_RIGHT){
                globalMap[atualColuna][atualLinha] = 8;
            }
            else if (globalMap[atualColuna][atualLinha] == 1 && retorno == DIR_DOWN){
                globalMap[atualColuna][atualLinha] = 10;
            }
            else if (globalMap[atualColuna][atualLinha] == 3 && retorno == DIR_UP){
                globalMap[atualColuna][atualLinha] = 10;
            }
            else if (globalMap[atualColuna][atualLinha] == 3 && retorno == DIR_RIGHT){
                globalMap[atualColuna][atualLinha] = 4;
            }
            else if (retorno != DIR_NONE && (globalMap[atualColuna][atualLinha] == 6 || globalMap[atualColuna][atualLinha] == 8 || globalMap[atualColuna][atualLinha] == 10 || globalMap[atualColuna][atualLinha] == 4)){
                globalMap[atualColuna][atualLinha] = 9;
            }

        }

        else if(retorno == DIR_RIGHT){
            if (velhaDirecao == DIR_UP){
                globalMap[atualColuna][atualLinha] = 5;
            }
            else if (velhaDirecao == DIR_DOWN){
                globalMap[atualColuna][atualLinha] = 7;
            }
        }
        else if (retorno == DIR_LEFT){
            if (velhaDirecao == DIR_UP){
                globalMap[atualColuna][atualLinha] = 3;
            }
            else if (velhaDirecao == DIR_DOWN){
                globalMap[atualColuna][atualLinha] = 1;
            }
        }
        else if (retorno == DIR_UP){
            if (velhaDirecao == DIR_RIGHT){
                globalMap[atualColuna][atualLinha] = 1;
            }
            else if (velhaDirecao == DIR_LEFT){
                globalMap[atualColuna][atualLinha] = 7;
            }
        }
        else if (retorno == DIR_DOWN){
            if (velhaDirecao == DIR_RIGHT){
                globalMap[atualColuna][atualLinha] = 3;
            }
            else if (velhaDirecao == DIR_LEFT){
                globalMap[atualColuna][atualLinha] = 5;
            }
        }
        else if (retorno == DIR_NONE){
            if (velhaDirecao == DIR_DOWN){
                globalMap[atualColuna][atualLinha] = 12;
            }
            else if (velhaDirecao == DIR_UP){
                globalMap[atualColuna][atualLinha] = 11;
            }
            else if(velhaDirecao == DIR_RIGHT){
                globalMap[atualColuna][atualLinha] = 13;
            }
            else if(velhaDirecao == DIR_LEFT){
                globalMap[atualColuna][atualLinha] = 14;
            }

        }

       //define nova direcao

        return retorno;
    }// fim do metodo nova direcao

    /*
     * identifica se o proximo ponto do caminho q esta seguindo, pode ser marcado (se nao tem obstaculos ou fim da matriz). retorna +1 ou -1 dependendo de para onde o caminho esta indo. retorna 0 quando puder continuar
     */
    public int continuarCaminho(int atualColuna, int atualLinha){
        int retorno = 0;

        if (actualDirection == DIR_RIGHT && atualColuna < maxCols && globalMap[atualColuna+1][atualLinha] == -1){
            retorno = 1;
        }
        else if(actualDirection == DIR_LEFT && atualColuna > 1 && globalMap[atualColuna-1][atualLinha] == -1)
        {
            retorno = -1;
        }
        else if (actualDirection == DIR_UP && atualLinha > 1 && globalMap[atualColuna][atualLinha-1] == -1){
            retorno = -1;
        }
        else if (actualDirection == DIR_DOWN && atualLinha < (maxRows-1) && globalMap[atualColuna][atualLinha+1] == -1){
            retorno = 1;
        }

        return retorno;

    }

    /**
     * atualiza o desenho do mapa
     */
    public void atualizaMapa(Graphics areaG){
        for(int todcol=0; todcol<=maxCols; todcol++){
            for(int todlin=0; todlin<=maxRows; todlin++){
                drawBlock(areaG, todcol, todlin, globalMap[todcol][todlin]);
            }
        }
    }

    /**
     * desenha o bloco na tela conforme coordenada e identificacao passadas como parametro
     * @param coluna desejada
     * @param linha desejada
     * @param identificacao do bloco (0-14)
     */
    public void drawBlock(Graphics gfx, int posCol, int posLin, int idBloco){
        BufferedImage imgBloco;

        if (idBloco == 0){
            imgBloco = terra0;
        }
        else if (idBloco == 1) {
            imgBloco = terra1;
        }
        else if (idBloco == 2) {
            imgBloco = terra2;
        }
        else if (idBloco == 3) {
            imgBloco = terra3;
        }
        else if (idBloco == 4) {
            imgBloco = terra4;
        }
        else if (idBloco == 5) {
            imgBloco = terra5;
        }
        else if (idBloco == 6) {
            imgBloco = terra6;
        }
        else if (idBloco == 7) {
            imgBloco = terra7;
        }
        else if (idBloco == 8) {
            imgBloco = terra8;
        }
        else if (idBloco == 9) {
            imgBloco = terra9;
        }
        else if (idBloco == 10) {
            imgBloco = terra10;
        }
        else if (idBloco == 11) {
            imgBloco = terra11;
        }
        else if (idBloco == 12) {
            imgBloco = terra12;
        }
        else if (idBloco == 13) {
            imgBloco = terra13;
        }
        else if (idBloco == 14) {
            imgBloco = terra14;
        }
        else if (idBloco == 95) {
            imgBloco = chegada1;
        }
        else if (idBloco == 96) {
            imgBloco = chegada2;
        }
        else if (idBloco == 97) {
            imgBloco = chegada3;
        }
        else if (idBloco == 98) {
            imgBloco = chegada4;
        }
        else if (idBloco == 99) {
            imgBloco = inicio;
        }

        else {
            imgBloco = grama;
        }

        gfx.drawImage(imgBloco, posCol*BLOCK_SIZE, posLin*BLOCK_SIZE, null);
    }

    public int RandomInRange(int aStart, int aEnd, Random aRandom){
        if ( aStart > aEnd ) {
          throw new IllegalArgumentException("Start cannot exceed End.");
        }

        //get the range, casting to long to avoid overflow problems
        long range = (long)aEnd - (long)aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long)(range * aRandom.nextDouble());
        int randomNumber =  (int)(fraction + aStart);
        return randomNumber;

      }


}
