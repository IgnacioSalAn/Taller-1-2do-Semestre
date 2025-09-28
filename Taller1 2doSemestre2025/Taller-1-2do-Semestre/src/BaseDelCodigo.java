import edu.princeton.cs.stdlib.StdDraw;
import java.awt.event.KeyEvent;
import java.awt.Font;

public class BaseDelCodigo { //(si le ponia taller 1 se rompia)

    /*/
    Pilares basicos del codigo
     */

    //tamanioPantalla
    static int ancho = 1000;
    static int alto = 800;

    //galleta
    static double jugadorAncho = ancho / 2.0; //aparicion en punto medio horizontal [mantener]
    static double jugadorAlto = alto / 2.0; //aparicion punto medio vertical [mantenr]
    static double tamanioJugador = 50; //tamaño del jugador [mantener]
    static double velocidadJugador = 8; //velocidad de movimiento del jugador [eliminar]

    //bonificaciones
    static double[] enemigoAncho = {10, 10, 10, 10, 10}; //aparicion esquina inferior izquierda [modificar]
    static double[] enemigoAlto = {300, 250, 200, 150, 100};  //aparicion y direccion multiple de bonificaciones [modificar]
    static double tamanioEnemigo = 30; //tamaño de los bonus [mantener]
    static double[] velocidadEnemigoHorizontal = {6, -6, 4, -4, 4}; //velocidad horizontal de los bonus [modificar]
    static double[] velocidadEnemigoVertical = {6, -6, 10, -4, 4}; //velocidad vertical de los bonus [modificar]

    //preparandoTemporizador
    static long tiempoInicio; // inicio del contador cuando se ejecuta el programa [mantener]
    static int tiempoLimite = 30; //(Se describe a sí mismo) [modificar]


    //juego
    public static void main(String[] args) {

        tiempoInicio = System.currentTimeMillis(); //contadorSegundosTemporizador [mantener] (debo buscar una explicacion)

        //mostrarPantalla
        StdDraw.setCanvasSize(ancho, alto); //tamanioMaximoPantalla [mantener]
        StdDraw.setXscale(0, ancho); //anchoPantalla [mantener]
        StdDraw.setYscale(0, alto); //alturaPantalla [mantener]
        StdDraw.enableDoubleBuffering(); //evitar parpadeo [mantener]

        //mantenerJuegoAbierto
        while (true) {

            //aplicandoTemporizador
            long tiempoActual = System.currentTimeMillis(); //contadorSegundosTemporizador
            long segundosTranscurridos = (tiempoActual - tiempoInicio) / 1000; //comprobarTiempoTranscurrido

            if (segundosTranscurridos >= tiempoLimite) { //condicionTermino
                StdDraw.clear(StdDraw.WHITE); //pintadoNegro
                mensajeCierre("Ganaste"); //mensajeFinal
                break;
            }

            //movimientoJugador
            movimientoJugador(); //[eliminar o modificar]

            //limitePantallaJugador [eliminar o modificar]
            if (jugadorAncho < tamanioJugador) { //limiteIzquierdo
                jugadorAncho = tamanioJugador; //jugadorNoAvanzaMas
            }
            if (jugadorAncho > ancho - tamanioJugador) { //limiteDerecho
                jugadorAncho = ancho - tamanioJugador;//jugadorNoAvanzaMas
            }
            if (jugadorAlto < tamanioJugador) { //limiteInferior
                jugadorAlto = tamanioJugador; //jugadorNoAvanzaMas
            }
            if (jugadorAlto > alto - tamanioJugador) { //ladoSuperior
                jugadorAlto = alto - tamanioJugador; //jugadorNoAvanzaMas
            }

            //movimientoEnemigo [mantener y moddificar para el bonus]
            for (int i = 0; i < enemigoAncho.length; i++) { //seleccionarEnemigosPara:
                enemigoAncho[i] += velocidadEnemigoHorizontal[i]; //agregarVelocidadHorizontal
                enemigoAlto[i] += velocidadEnemigoVertical[i]; //agregarVelocidadVerttical

                //limitePantallaEnemigo
                if (enemigoAncho[i] < tamanioEnemigo || enemigoAncho[i] > ancho - tamanioEnemigo) { //limiteHorizontal
                    velocidadEnemigoHorizontal[i] = -velocidadEnemigoHorizontal[i]; //rebote
                    enemigoAncho[i] = Math.max(tamanioEnemigo, Math.min(enemigoAncho[i], ancho - tamanioEnemigo)); //evitarParpadeo
                }
                if (enemigoAlto[i] < tamanioEnemigo || enemigoAlto[i] > alto - tamanioEnemigo) { //limiteVertical
                    velocidadEnemigoVertical[i] = -velocidadEnemigoVertical[i]; //rebote
                    enemigoAlto[i] = Math.max(tamanioEnemigo, Math.min(enemigoAlto[i], alto - tamanioEnemigo)); //evitarParpadeo
                }
            }

            //choqueJugadorEnemigo [mantener y modificar] (esta chistoso)
            for (int i = 0; i < enemigoAncho.length; i++) { //seleccionarEnemigosPara:
                if (Math.abs(jugadorAncho - enemigoAncho[i]) <= tamanioJugador + tamanioEnemigo && //comprobarChoqueHorizontal
                        Math.abs(jugadorAlto - enemigoAlto[i]) <= tamanioJugador + tamanioEnemigo) { //comprobarChoqueVertical
                    StdDraw.clear(StdDraw.WHITE); //pintadoNegro
                    mensajeCierre("Perdiste"); //mensajeFinal
                    return;
                }
            }
            StdDraw.clear(StdDraw.WHITE); //evitaDejarRastrosJugadorBloqueTemporizador [mantener]

            //mostrarJugador [mantener y modificar]
            StdDraw.setPenColor(StdDraw.BLUE); //jugadorColor
            StdDraw.filledSquare(jugadorAncho, jugadorAlto, tamanioJugador); //jugadorTamanio

            //mostrarEnemigos [mantener y modificar]
            StdDraw.setPenColor(StdDraw.RED); //enemigoColor
            for (int i = 0; i < enemigoAncho.length; i++) { //seleccionarEnemigosPara:
                StdDraw.filledSquare(enemigoAncho[i], enemigoAlto[i], tamanioEnemigo); //enemigoTamanio
            }

            //mostrarTemporizador [mantener] (ver como cmabiar)
            StdDraw.setPenColor(StdDraw.BLACK); //temporizadorColor
            StdDraw.setFont(new Font("Arial", Font.PLAIN, 15)); //tipoTexto
            StdDraw.textLeft(10, alto - 20, "Tiempo: " + (tiempoLimite - segundosTranscurridos)); //posicion&texto
            StdDraw.show(); //mostrarTexto

            //mostrarPantalla
            StdDraw.show(); //mostrarPantalla

            StdDraw.pause(15); //evitaDescontrolEnemigos {es un pilar}
        }
    }

    //terminoJuego [mantener y modficar]
    public static void mensajeCierre(String mensaje) {
        StdDraw.clear(StdDraw.WHITE);
        StdDraw.setPenColor(StdDraw.BLACK); //colorMensaje
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 25)); //tipoMensaje
        StdDraw.text(ancho / 2.0, alto / 2.0, mensaje);
        StdDraw.pause(200);
        StdDraw.show();
    }

    //movimientoJugador [eliminar]
    public static void movimientoJugador(){
        if (StdDraw.isKeyPressed(KeyEvent.VK_W)) { //presionarWPara:
            jugadorAlto += velocidadJugador; //subirJugador
        }
        if (StdDraw.isKeyPressed(KeyEvent.VK_S)){ //presionarSPara:
            jugadorAlto -= velocidadJugador; //bajarJugador
        }
        if (StdDraw.isKeyPressed(KeyEvent.VK_A)){ //presionarAPara:
            jugadorAncho -= velocidadJugador; //irIzquierdaJugador
        }
        if (StdDraw.isKeyPressed(KeyEvent.VK_D)){ //presionarDPara:
            jugadorAncho += velocidadJugador; //irDerechaJugador
        }


    }
}
