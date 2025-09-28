import edu.princeton.cs.stdlib.StdDraw;
import java.awt.event.KeyEvent;

public class Juego {


    /*/
    Configuracion pantalla
     */

    //Tamanio patalla
    static int ancho = 600;
    static int alto = 600;

    //jugador
    static double jugadorAncho = ancho / 2.0;
    static double jugadorAlto = alto / 2.0;
    static double tamanioJugador = 7;

    //Bonificaciones
    static double[] bonusAncho = {10, 10, 10, 10, 10};
    static double[] bonusAlto = {300, 250, 200, 150, 100};
    static double tamanioBonus = 20;
    static double[] velocidadBonusHorizontal = {6, -6, 4, -4, 4};
    static double[] velocidadBonusVertical = {6, -6, 10, -4, 4};

    //movimientoJugador
    public static void movimientoJugador() {
        if (StdDraw.isKeyPressed(KeyEvent.VK_W)) { //presionarWPara:
            //jugadorAlto += velocidadJugador; //subirJugador
        }
        if (StdDraw.isKeyPressed(KeyEvent.VK_S)) { //presionarSPara:
            //jugadorAlto -= velocidadJugador; //bajarJugador
        }
        if (StdDraw.isKeyPressed(KeyEvent.VK_A)) { //presionarAPara:
            //jugadorAncho -= velocidadJugador; //irIzquierdaJugador
        }
        if (StdDraw.isKeyPressed(KeyEvent.VK_D)) { //presionarDPara:
            //jugadorAncho += velocidadJugador; //irDerechaJugador
        }

    }
}
