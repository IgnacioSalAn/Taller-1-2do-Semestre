import edu.princeton.cs.stdlib.StdDraw;
import java.awt.event.KeyEvent;
import java.awt.Font;


public class Main {

    /*?
    Variables iniciales
     */
    static double horasEstudio = 0;
    static double librerias = 0.0;
    static long ultimoSegundo; // Registra conteo de segundos transcurridos


    public static void main(String[] args) {

        /*/
        Configuracion de pantalla
        */
        StdDraw.setCanvasSize(1000, 800); //tamanioMaximoPantalla [mantener]
        StdDraw.setXscale(0, 1000); //anchoPantalla [mantener]
        StdDraw.setYscale(0, 800); //alturaPantalla [mantener]
        StdDraw.enableDoubleBuffering(); //evitar parpadeo [mantener]
        ultimoSegundo = System.currentTimeMillis() / 1000; //iniciará el conteo


        /*/
        Configuracion de becas (bonus perdibles) // [considerarlo]
         */


        /*/
        Bucle principal del que se sostiene el codigo
         */
        while (true) {
            long tiempoActual = System.currentTimeMillis() / 1000;
            ; //contadorSegundosActualesTemporizador
            long tiempoFrames = (tiempoActual - ultimoSegundo); //comprobarTiempoTranscurrido
            ultimoSegundo = tiempoActual; //se actualiza para:

            // estudio extra
            horasEstudio += librerias * tiempoFrames; //aplicar multiplicadores

            // Dibujo
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.text(300, 550, "Horas de estudio: " + (int) horasEstudio);
            StdDraw.text(300, 520, "Librerias: " + librerias);

            // Dibuja la cookie (círculo) [Debe ser modificado para incluir imagen de la universidad]
            StdDraw.setPenColor(200, 150, 100);
            StdDraw.filledCircle(300, 300, 100);
            /*/
            Considerar esto:
            StdDraw.picture(0,0, "UCN_Logo.svg.png");
             */

            // Dibuja botón de mejora (rectángulo)
            StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
            StdDraw.filledRectangle(300, 100, 100, 40);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.text(300, 100, "Comprar +1 CPS (coste 50)");


            // Detectar clic
            if (StdDraw.isMousePressed()) {
                double mx = StdDraw.mouseX();
                double my = StdDraw.mouseY();

                // Click en la cookie
                if (dist(mx, my, 300, 300) <= 100) {
                    horasEstudio++;
                }

                // Click en el botón de mejora
                if (mx >= 200 && mx <= 400 && my >= 60 && my <= 140) {
                    if (horasEstudio >= 50) {
                        horasEstudio -= 50;
                        librerias += 1;
                    }
                }
                StdDraw.pause(200); // debounce para no registrar clics múltiples
            }

            StdDraw.show();
            StdDraw.pause(30);


        }
    }

    static double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    
}
