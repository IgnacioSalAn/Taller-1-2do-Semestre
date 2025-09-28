/*
import edu.princeton.cs.stdlib.StdDraw;

public class CookieClickerStdDraw {

    static double cookies = 0;
    static double cps = 0.0;  // cookies por segundo
    static double lastTime;

    public static void main(String[] args) {
        // Configurar ventana
        StdDraw.setCanvasSize(600, 600);
        StdDraw.setXscale(0, 600);
        StdDraw.setYscale(0, 600);
        lastTime = System.currentTimeMillis() / 1000.0;

        // Bucle del juego
        while (true) {
            double now = System.currentTimeMillis() / 1000.0;
            double delta = now - lastTime;
            lastTime = now;

            // Ganar cookies pasivas
            cookies += cps * delta;

            // Dibujo
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.text(300, 550, "Cookies: " + (int)cookies);
            StdDraw.text(300, 520, "CPS: " + cps);

            // Dibuja la cookie (círculo)
            StdDraw.setPenColor(200, 150, 100);
            StdDraw.filledCircle(300, 300, 100);

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
                    cookies++;
                }

                // Click en el botón de mejora
                if (mx >= 200 && mx <= 400 && my >= 60 && my <= 140) {
                    if (cookies >= 50) {
                        cookies -= 50;
                        cps += 1;
                    }
                }
                StdDraw.pause(200); // debounce para no registrar clics múltiples
            }

            StdDraw.show();
            StdDraw.pause(30);
        }
    }

    static double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }
}


 */