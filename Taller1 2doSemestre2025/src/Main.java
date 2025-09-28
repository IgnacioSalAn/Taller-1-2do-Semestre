import edu.princeton.cs.stdlib.StdDraw;

public class Main {

    /*
     * Variables iniciales necesarias para el funcionamiento del codigo
     */

    /**
     * Variables de los contadores de puntaje
     */
    static double horasEstudio = 0; // contador de clicks en cookie (ratatin)
    static double libreriaTemporal = 0.0; // aumento temporal aplicado al contador de clicks
    static double libreriaPermanente = 0.0; // aumento permanente al contador de clicks
    static int becas = 0; // contador de monedas clickeadas
    static boolean graduado = false; // variable que comprueba el contador de horas alcanzo el numero 10000


    /**
     * Variables para desplegar los mensajes temporales en pantalla
     */
    static String mensajeBase = ""; // String vacio que almacenará el mensaje de mejora generico
    static String mensajeMejora = ""; // String vacio que almacenará el mensaje de mejora particular de cada boton
    static String mensajeExtra = ""; // String vacio que almacernará parte del mensaje de graduacion
    static long tiempoMensaje = 0; // Tiempo vacio (cero) necesario para el reinicio del contador para los mensajes
    static int duracionMensaje = 5; // Segundos que durara el mensaje en pantalla (se hace obvio)

    /**
     * Variables para el sistema de mejoras (temporales)
     */
    static final int MAX_MEJORAS = 999; // Limitador de mejoras (util para el sistema de admision de mejoras que usa listas para separar cada tipo)
    static double[] mejoras = new double[MAX_MEJORAS]; // Listado de mejoras temporales activas o usadas
    static long[] inicioMejoras = new long[MAX_MEJORAS]; // inicio de la mejora
    static int[] duracionMejoras = new int[MAX_MEJORAS]; // duración de la mejora
    static int mejorasActivas = 0; // Contador de mejoras utilizadas/activas en el momento

    /**
     * Variables del recuadro donde se mueven las monedas
     */
    static double posicionRecuadroEje_X = 495; // posicon de la esquina inferior izquierda del recuadro en Eje X
    static double posicionRecuadroEje_Y = 100; // posicion de la esquina inferior izquierda del recuadro en Eje Y
    static double recuadroAncho = 400; // Ancho del recuadro de las monedas
    static double recuadroAlto = 160; // Alto del recuadro de las monedas
    static double rectX = posicionRecuadroEje_X - recuadroAncho / 2; // posicion para la aparicion de la moneda en Eje X
    static double rectY = posicionRecuadroEje_Y - recuadroAlto / 2; // posicion para la aparicion de la moneda en Eje Y

    /**
     * Variables para el sistema de las monedas (incluida aparicion, desaparicion, conteo de capturas y movimiento)
     */
    static final int numeroBolas = 10; // la cantidad de monedas que apareceran en total
    static double[] posicionBolaX = new double[numeroBolas]; // posición inicial X
    static double[] posicionBolaY = new double[numeroBolas]; // posición inicial Y
    static double bolaRadio = 15;
    static double[] bolaDireccionX = new double[numeroBolas]; // velocidad horizontal
    static double[] bolaDireccionY = new double[numeroBolas]; // velocidad vertical
    static boolean[] bolaVisible = new boolean[numeroBolas]; // monedas que aparereran
    static boolean[] bolaRecogida = new boolean[numeroBolas]; // monedas clickeadas
    static boolean[] bolaAparecida = new boolean[numeroBolas]; // monedas desaparecidas
    static long[] tiemposPara_AparicionBola = {60, 120, 180, 240, 300, 360, 420, 480, 540, 600}; // segundos para la aparicion de las monedas
    static double[] velocidadExtraX = new double[numeroBolas]; // velocidad extra horizontal
    static double[] velocidadExtraY = new double[numeroBolas]; // velocidad extra vertical
    static long[] tiempoInicioMoneda = new long[numeroBolas];
    static int duracionMoneda = 10; // duración de aparicion de moneda (en segundos)


    /**
     * Variables para la deteccion de clicks y manejo del tiempo
     */
    static boolean mouseAntes = false; // Para establcer que por defecto no se presiona un click en pantalla hasta
    static long tiempoInicio; // Para establecer el contador del sistema

    /**
     * Variable que cuenta el semestre en que se encuentra el jugador
     */
    static int semestreActual = 1; // controla los semestres alcanzados


    /**
     * Funcion principal del programa.
     * Inicializa los sistemas de la pantalla, las monedas y activa el ciclo infinito que contiene
     * @param args (Esto no se "utiliza" en realidad y esta para sostener la logica del programa)
     */
    public static void main(String[] args) {
        /**
         * LLamado de subprogramas y activacion de los sistemas que conforman todo el programa
         */
        configurarPantalla(); // Muestra ventana en donde se reproducira el programa
        inicializarMonedas(); // Activa el sistema de captura de monedas (incluida aparicion, desaparicion, conteo y movimiento)
        tiempoInicio = System.currentTimeMillis(); // Variable que activa el temporizador global que maneja el metodo

        /**
         * Ciclo infinito para mantener abierto funcionando al programa
         */
        while (true) {
            long tiempoActual = System.currentTimeMillis(); // Activa un temporizador interno dentro del propio programa (contador en milisegundos)
            long segundosTranscurridos = (tiempoActual - tiempoInicio) / 1000; // Comprueba la diferencia de segundos trancurrido de forma global e interna


            /**
             * Pequenios sistemas que utilizan el paso del tiempo en el contador
             */
            aplicarMejoras(tiempoActual); // Aplica las mejoras al contador tanto temporales como permanentes
            aparicionMonedas(segundosTranscurridos, tiempoActual); // Activa la aparicion, movimiento y desaparicion de las monedas

            actualizarMensajesHorasEstudio(tiempoActual); // Muestra en pantalla el semestre actual en que se encuentre el jugador/usuario

            /**
             * Pequenios sistemas para la interfaz grafica del programa
             */
            StdDraw.clear(); // Limpia la pantalla
            dibujarEscenario(); // Muestra todas las imagenes (galleta, botones, cuadro de texto, cuadro de becas)
            dibujarMonedas(); // Muestra las monedas en su momento de activacion
            mostrarMensajes(tiempoActual); // Muestra los mensajes en pantalla

            /**
             * Contador de segundos transcurridos
             */
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.text(140, 500, "Tiempo transcurrido: " + segundosTranscurridos);

            /**
             * Pequenio sistema de deteccion de clicks para activar los eventos del programa (coockie, botones de mejora y captura de monedas)
             */
            boolean mouseAhora = StdDraw.isMousePressed(); // Variable para considerar la deteccion del click en pantalla
            if (!mouseAhora && mouseAntes) { // Si se detecta un click en pantalla, entonces:
                double mx = StdDraw.mouseX(); // Se comprueba su cordenada X
                double my = StdDraw.mouseY(); // Se comprueba su cordenada Y

                //Si detectan clicks dentro del area correspondientes a los eventos de todo el programa, se activa:
                /**
                 * Pequenios sistemas de puntaje y suma de contadores del jugador/usuario
                 */
                detectarClickCookie(mx, my); // Sistema de la coockie
                detectarClickBotones(mx, my, tiempoActual); // Sistema de las mejoras
                detectarClickMonedas(mx, my, tiempoActual); // Sistema dde las monedas
            }
            mouseAntes = mouseAhora; // Variable que reinicia estado del contador para volver a validar un click

            StdDraw.show(); // Muestra en pantalla los sistemas de todo el programa
            StdDraw.pause(30); // Tiempo de espera entre cada accion (clicks del jugador/usuario)
        }
    }

    /**
     * Pequenio sistema que activa la ventana en donde funcionara todo el programa
     */
    static void configurarPantalla() {
        StdDraw.setCanvasSize(760, 520); // Medidas de la pantalla
        StdDraw.setXscale(0, 760); // Ancho de antalla
        StdDraw.setYscale(0, 520); // Altura de pantalla
        StdDraw.enableDoubleBuffering(); // Evitar parpadeos de la pantalla
    }

    /**
     * Sistema de mejoras
     */

    /**
     * Parte del sistema de mejoras. Causa el aumento de las horas de estudio al momento de que se activa alguna mejora.
     * Su objetivo principal es aplicar las mejoras al contador.
     * Para eso se utiliza:
     * @param tiempoActual Variable que mide el tiempo transcurrido al iniciar el programa
     */
    static void aplicarMejoras(long tiempoActual) {
        libreriaTemporal = 0;
        for (int i = 0; i < mejorasActivas; i++) {
            if ((tiempoActual - inicioMejoras[i]) / 1000 < duracionMejoras[i]) {
                libreriaTemporal += mejoras[i];
            }
        }
        horasEstudio += (libreriaPermanente + libreriaTemporal) * 0.03;
    }

    /**
     * Parte del sistema de mejoras. Causa el aumento de las horas de estudio al momento de que se aplica alguna mejora.
     * Su objetivo principal es guardar en listas las mejoras temporales del contador (se necesita del subprograma anterior).
     * Para eso se utiliza:
     * @param bonus Variable que registra la mejora temporal al contador
     * @param tiempo Variable que mide el tiempo actual transcurrido
     * @param durSegundos Variable que establece el tiempo que durara la mejora
     * Funciona almacenando una mejora, dandosele un tiempo y manteniendola activa ese tiempo, asi cada una es independiente entre si y no causan fallos
     */
    static void registrarMejora(double bonus, long tiempo, int durSegundos) {
        if (mejorasActivas < MAX_MEJORAS) { // Comprueba que no se haya llegado al limite de la mejoras que pueden usarse (mismo que no deberia existir en realidad)
            mejoras[mejorasActivas] = bonus; // Se almacena la mejora temporal dentro de su respectiva posicion en la lista
            inicioMejoras[mejorasActivas] = tiempo; // Se registra el tiempo transcurrido en cada mejora temporal registrada en la lista
            duracionMejoras[mejorasActivas] = durSegundos; // El tiempo justo que durara cada mejora temporal almacenada en la lista
            mejorasActivas++; // Aumenta el contador de mejoras utilizadas/activas en el momento de la activacion de alguna mejora temporal
        }
    }

    /**
     * Parte del sistema de monedas.
     * Pequenio sistema que inicializa la aparicion aleatoria de las monedas en su recuadro
     */
    static void inicializarMonedas() {
        for (int i = 0; i < numeroBolas; i++) { // Ciclo for que buscara y aplicara lo siguiente a cada moneda segun el avance del ciclo:
            posicionBolaX[i] = rectX + bolaRadio + Math.random() * (recuadroAncho - 2 * bolaRadio); // Posicion horizontal aleatoria para la aparicion de la moneda
            posicionBolaY[i] = rectY + bolaRadio + Math.random() * (recuadroAlto - 2 * bolaRadio); // Posicion vertical aleatoria para la aparicion de la moneda
            bolaDireccionX[i] = Math.random() * 6 - 3; // Velocidad vertical de la moneda
            bolaDireccionY[i] = Math.random() * 6 - 3; // Velocidad horizontal de la moneda
            bolaVisible[i] = false; // Estado no visible para las monedas que apareceran
            bolaRecogida[i] = false; // Estado no visible para las monedas recogidas por el jugador/usuario
            bolaAparecida[i] = false; // Estado no visible para las monedas
            velocidadExtraX[i] = 0; // Variable que registra la velocidad horizontal extra correspondiente a cada moneda
            velocidadExtraY[i] = 0; // Variable que registra la velocidad vertical extra correspondiente a cada moneda
            tiempoInicioMoneda[i] = 0; // Variable que establece tiempo inicial y reinicia el contador para cuando aparezca cada moneda
        }
    }

    /**
     * Parte del sistema de monedas. Causa el cambio en el estado de visibilidad de las monedas, su movimiento y la desaparicion de estas.
     * Busca en la lista donde se almaceno el estado de cada moneda y en caso de que se cumplan sus condiciones, la hace aparecer.
     * Tambien le aplica velocidad a la moneda para poder hacer que se desplace en el recuadro y le aumenta la velocidad a la siguiente.
     * Una vez se cumplan sus condiciones de desaparicion se realiza esa accion.
     * Para eso se utiliza:
     * @param segundosTranscurridos Variable que registra el tiempo transcurrido desde la activacion del programa
     * @param tiempoActual Variable que mide el tiempo transcurrido al iniciar el programa
     */
    static void aparicionMonedas(long segundosTranscurridos, long tiempoActual) {
        for (int i = 0; i < numeroBolas; i++) {
            // Aparecer moneda si no fue recogida y no ha aparecido antes
            if (!bolaVisible[i] && !bolaRecogida[i] && !bolaAparecida[i] && segundosTranscurridos >= tiemposPara_AparicionBola[i]) {
                bolaVisible[i] = true;
                bolaAparecida[i] = true; // marcamos que ya apareció
                tiempoInicioMoneda[i] = tiempoActual;
                bolaDireccionX[i] += velocidadExtraX[i];
                bolaDireccionY[i] += velocidadExtraY[i];
                velocidadExtraX[i] = 0;
                velocidadExtraY[i] = 0;
            }

            // Desaparecer moneda si excede duración
            if (bolaVisible[i] && (tiempoActual - tiempoInicioMoneda[i]) / 1000 > duracionMoneda) {
                bolaVisible[i] = false;
            }

            // Movimiento y rebote
            if (bolaVisible[i]) {
                posicionBolaX[i] += bolaDireccionX[i];
                posicionBolaY[i] += bolaDireccionY[i];

                if (posicionBolaX[i] <= rectX + bolaRadio) {
                    posicionBolaX[i] = rectX + bolaRadio;
                    bolaDireccionX[i] *= -1;
                }
                if (posicionBolaX[i] >= rectX + recuadroAncho - bolaRadio) {
                    posicionBolaX[i] = rectX + recuadroAncho - bolaRadio;
                    bolaDireccionX[i] *= -1;
                }
                if (posicionBolaY[i] <= rectY + bolaRadio) {
                    posicionBolaY[i] = rectY + bolaRadio;
                    bolaDireccionY[i] *= -1;
                }
                if (posicionBolaY[i] >= rectY + recuadroAlto - bolaRadio) {
                    posicionBolaY[i] = rectY + recuadroAlto - bolaRadio;
                    bolaDireccionY[i] *= -1;
                }
            }
        }
    }

    /**
     * Parte del sistema de monedas. Se encarga de dibujar en pantalla las monedas cuando se cumpla su condicion de aparicion
     */
    static void dibujarMonedas() {
        for (int i = 0; i < numeroBolas; i++) {
            if (bolaVisible[i]) {
                StdDraw.picture(posicionBolaX[i], posicionBolaY[i], "Monedas.png", bolaRadio*2, bolaRadio*2);
            }
        }
    }

    /**
     * Parte del sistema de monedas. Se encarga de la deteccion de clicks sobre las monedas en pantalla.
     * Para eso se utiliza:
     * @param mx Variable que considera la posicion en el Eje X de la moneda
     * @param my Variable que considera la posicion en el Eje Y de la moneda
     * @param tiempoActual Variable que mide el tiempo transcurrido al iniciar el programa
     */
    static void detectarClickMonedas(double mx, double my, long tiempoActual) {
        for (int i = 0; i < numeroBolas; i++) {
            if (bolaVisible[i] && !bolaRecogida[i]) {
                double distancia = Math.sqrt(Math.pow(mx - posicionBolaX[i], 2) + Math.pow(my - posicionBolaY[i], 2));
                if (distancia <= bolaRadio) {
                    becas++;

                    // Aumentar velocidad acumulativa
                    bolaDireccionX[i] *= 1.5;
                    bolaDireccionY[i] *= 1.5;

                    // Transferir velocidad a la siguiente moneda
                    if (i < numeroBolas - 1) {
                        velocidadExtraX[i + 1] += bolaDireccionX[i] * 0.5;
                        velocidadExtraY[i + 1] += bolaDireccionY[i] * 0.5;
                    }

                    // Limitar velocidad máxima
                    double maxVel = 99;
                    bolaDireccionX[i] = Math.max(Math.min(bolaDireccionX[i], maxVel), -maxVel);
                    bolaDireccionY[i] = Math.max(Math.min(bolaDireccionY[i], maxVel), -maxVel);

                    // Hacer que la moneda desaparezca
                    bolaVisible[i] = false;
                    bolaRecogida[i] = true;
                }
            }
        }
    }


    /**
     * Sistema para el dibujado de la interfaz grafica del programa
     */

    /**
     * Parte del sistema grafico del programa. Se encarga de la aparicion de imagenes, textos del programa en pantalla
     */
    static void dibujarEscenario() {
        if (graduado) {
            StdDraw.picture(130, 360, "Ratatin graduado.jpeg", 200, 200); // cookie graduado
        } else {
            StdDraw.picture(130, 360, "RatatinUCN.jpeg", 200, 200); // cookie normal
        }
        StdDraw.picture(385, 395, "BibliotecaUCN.jpeg", 215, 95); // mejora (permanente y temporal)
        StdDraw.picture(615, 395, "Tutoria.jpeg", 215, 95); // mejora (permanente y temporal)
        StdDraw.picture(385, 275, "BusquedaInternet.jpg", 215, 95); // mejora (permanente y temporal)
        StdDraw.picture(615, 275, "DesayunoCampeones.jpg", 215, 95); // mejora (solo temporal)
        StdDraw.picture(posicionRecuadroEje_X, posicionRecuadroEje_Y, "CerditoBecas.jpg", recuadroAncho, recuadroAlto); // posicion para recuadro de mensajes
        StdDraw.picture(140, 130, "Cuadro de texto.png", 280, 315); // recuadro de mensajes
        StdDraw.setPenColor(StdDraw.BLACK); // color de mensajes
        StdDraw.text(135, 210, "Horas de estudio: " + (int) horasEstudio); // mensaje contador
        StdDraw.text(135, 185, "Mejoras estudio: " + (libreriaPermanente + libreriaTemporal)); // mensaje contador
        StdDraw.text(135, 160, "Becas conseguidas: " + (becas)); // mensaje contador
        StdDraw.text(135, 135, "Becas no conseguidas: " + ((10) - becas)); // mensaje contador
        StdDraw.text(135, 110, "Cursando semestre: " + semestreActual); // mensaje contador
        StdDraw.text(130, 480, "Cookie: "); // mensaje explicacion
        StdDraw.text(505, 490, "Mejoras (gastan horas de estudio):" ); // mensaje explicacion
        StdDraw.text(385, 455, "Lectura de libros (coste: 50)" ); // mensaje explicacion
        StdDraw.text(615, 455, "Clase extra (coste: 100)" ); // mensaje explicacion
        StdDraw.text(385, 333, "Usar internet (coste: 30)" ); // mensaje explicacion
        StdDraw.text(615, 333, "Gran idea (coste: 15)" ); // mensaje explicacion
        StdDraw.text(380, 195, "Becas a conseguir: "); // mensaje explicacion
    }

    /**
     * Parte del sistema grafico del programa. Se encarga de la aparicion de los mensajes temporales que apareceran en el cuadro de texto.
     * Estos mensajes seran sobre la mejora que hayamos seleccionado y su respectivo tiempo de aparicion.
     * Para eso se utiliza:
     * @param tiempoActual Variable que mide el tiempo transcurrido al iniciar el programa
     */
    static void mostrarMensajes(long tiempoActual) {
        if ((tiempoActual - tiempoMensaje) / 1000 < duracionMensaje) {
            if (!mensajeBase.isEmpty())
                StdDraw.text(135, 85, mensajeBase);
            if (!mensajeMejora.isEmpty())
                StdDraw.text(135, 62, mensajeMejora);
            if (!mensajeExtra.isEmpty())
                StdDraw.text(135, 42, mensajeExtra);
        }
    }

    /**
     * Sistema de deteccion de clicks
     */

    /**
     * Parte del sistema de deteccion de clicks.
     * Se encarga de la deteccion de clicks sobre la cookie y suma de uno en uno segun los clicks que hayamos hecho.
     * Para eso se utiliza:
     * @param mx Variable que considera la posicion en el Eje X de la moneda
     * @param my Variable que considera la posicion en el Eje Y de la moneda
     */
    static void detectarClickCookie(double mx, double my) {
        if (mx >= 30 && mx <= 230 && my >= 260 && my <= 460) {
            horasEstudio++;
        }
    }

    /**
     * Parte del sistema de deteccion de clicks.
     * Se encarga de la deteccion de clicks sobre los cuatro botones de mejora.
     * Al momento de detectar un click resta la cantidad de horas de estudio, aplica una mejora temporal y/o permanente a ese contador y desplieza en pantalla un mensaje segun el boton presionado
     * Para eso se utiliza:
     * @param mx Variable que considera la posicion en el Eje X de la moneda
     * @param my Variable que considera la posicion en el Eje Y de la moneda
     * @param tiempoActual Variable que mide el tiempo transcurrido al iniciar el programa
     */
    static void detectarClickBotones(double mx, double my, long tiempoActual) {
        // Biblioteca
        if (mx >= 277.5 && mx <= 492.5 && my >= 347.5 && my <= 442.5) {
            if (horasEstudio >= 50) {
                horasEstudio -= 25;
                registrarMejora(2.0, tiempoActual, 6);
                libreriaPermanente += 1.5;
                mensajeBase = "Mejora elegida:";
                mensajeMejora = "Lectura de libros (6s)";
                tiempoMensaje = tiempoActual;
            }
        }

        // Tutoria
        if (mx >= 507.5 && mx <= 722.5 && my >= 347.5 && my <= 442.5) {
            if (horasEstudio >= 100) {
                horasEstudio -= 50;
                registrarMejora(4, tiempoActual, 7);
                libreriaPermanente += 2.5;
                mensajeBase = "Mejora elegida:";
                mensajeMejora = "Clase extra (7s)";
                tiempoMensaje = tiempoActual;
            }
        }
        // Busqueda internet
        if (mx >= 277.5 && mx <= 492.5 && my >= 227.5 && my <= 322.5) {
            if (horasEstudio >= 30) {
                horasEstudio -= 15;
                registrarMejora(2.5, tiempoActual, 4);
                libreriaPermanente += 1.0;
                mensajeBase = "Mejora elegida:";
                mensajeMejora = "Busqueda por internet (4s)";
                tiempoMensaje = tiempoActual;
            }
        }
        // Desayuno campeones
        if (mx >= 507.5 && mx <= 722.5 && my >= 227.5 && my <= 322.5) {
            if (horasEstudio >= 15) {
                horasEstudio -= 10;
                registrarMejora(0.5, tiempoActual, 5);
                mensajeBase = "Mejora elegida:";
                mensajeMejora = "Desayuno de ganadores (5s)";
                tiempoMensaje = tiempoActual;
            }
        }
    }

    /**
     * Sistema de mensajes de progreso
     */

    /**
     * Pequenio sistema para el despliegue de mensajes temporales que
     * Para eso se utiliza:
     * @param tiempoActual Variable que mide el tiempo transcurrido al iniciar el programa
     */
    static void actualizarMensajesHorasEstudio(long tiempoActual) {
        // Mensaje de graduación
        if (horasEstudio >= 10000 && !graduado) {
            graduado = true;
            semestreActual = 20;
            mensajeBase = "¡Graduación!";
            mensajeMejora = "Felicidades";
            mensajeExtra = "Has terminado la carrera!";
            tiempoMensaje = tiempoActual;
            return;
        }

        int nuevoSemestre = (int) (horasEstudio / 500) + 1;
        // Mensaje de semestre
        if (nuevoSemestre > semestreActual && horasEstudio < 10000) {
            semestreActual = nuevoSemestre;
            mensajeBase = "¡Nuevo semestre alcanzado!";
            tiempoMensaje = tiempoActual;
        }
    }
}