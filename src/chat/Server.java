package chat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Server {
    
    //es un tipo de socket de red que proporciona un punto sin conexión para enviar y recibir paquetes
    private static DatagramSocket socket;
    private static boolean busqueda;
    private static ArrayList<infoCliente> informacioncliente = new ArrayList<infoCliente>();
    private static int idcliente; 
    
    //empieza nuestro servidor, crea los recursos necesarios
    //y los inicializa
    public static void EmpezarServidor(int puerto){
        try{
            //creamos el socket para el envio de mensajes
            socket = new DatagramSocket();
            //true porque si funciono el servidor
            busqueda = true;
            //mandamos llamar el metodo escuchar para mantener el servidor corriendo y buscando
            EscucharMensajes();
            System.out.println("El servidor ha sido establecido en el puerto, " + puerto);
        }catch(Exception e){
            //muestra cualquier mensaje de error
            e.printStackTrace();
        }
    }
    //Tiene un hilo que corre todo el tiempo que esta el servidor
    //escucha los mensajes
    private static void EscucharMensajes(){
        //creamos el hilo 
        //un hilo de ejecucion de tareas es el mas pequeño de las secuencias programadas que pueden ser manejadas
        //de forma separada por el programador y es parte del sistema operativo
        //la razon por la que creamos un hilo es porque el socket.receive detendra el programa actual hasta que reciva un mensaje, y no queremos eso
        //buscamos que el programa siga de pie hasta que reciva un mensaje 
        Thread ping = new Thread("Buscando clientes"){
            //creamss un metodo dentro del hilo para realizar la busqueda de clientes
            public void buscando(){
                try{
                    //creamos un ciclo para que siga buscando siempre y cuando sea cierto que el servidor siga de pie
                    while(busqueda){
                        //usamos un array de tipo byte para almacenar los datos de mensaje que estaran llegando
                        byte[] informacion = new byte[1024];
                        //para obtener la informacion, usamos un datagram
                        //Se utilizan para implementar un servicio de entrega de paquetes sin conexión.
                        DatagramPacket caja = new DatagramPacket(informacion, informacion.length);
                        //creamos un socket para temporalmente guardar el mensaje del cliente y pasarlo al paquete de datagrama
                        socket.receive(caja);
                        //creamos un string para poder interpretar el mensaje como texto y no manejarlo como array tipo byte
                        String mensaje = new String(informacion);
                        //lo que hacemos aqui es darle un identificador \e para definir el final de los mensajes enviados
                        //si hay un \e, entonces sabremos que es el final del mensaje
                        //hacemos esto porque al definir el array informacion, los mensajes tendran un tamaño de 1024, y el definir 
                        //un identificador final de cada mensaje nos ahorraria tiempo en buscar el mensaje entre un monton de digitos
                        //ya que al final la variable mensaje tendra un tamaño de 1024 
                        mensaje = mensaje.substring(0, mensaje.indexOf("\\e"));
                        //mandamos llamar el metodo transmitir 
                        if(!ComandoServidor(mensaje,caja)){
                            Transmision(mensaje);
                        }
                    }
                }catch(Exception e){
                    //muestra cualquier mensaje de error
                    e.printStackTrace();
                }
            }
        };
        //iniciamos el hilo aqui 
        ping.start();;
    }
    //Termina el servidor sin terminar el programa
    public static void TerminarServer(){
        busqueda = false;
    }
    //Es para enviar un mensaje a todos los clientes conectados
    private static void Transmision(String mensaje){
        for(infoCliente llamado: informacioncliente){
            Mandar(mensaje, llamado.getdireccion(), llamado.getpuerto());
        }
    }
    //Es para enviar mensajes individuales a los clientes conectados
    private static void Mandar(String mensaje, InetAddress direccion, int puerto){
        try{
            //agregamos nuestro identificador de finalizacion de mensaje
            mensaje += "\\e";
            //aqui creamos otro array tipo byte para meter nuestro mensaje cuya longitud sera n y obtendremos los bytes
            byte[] informacion2 = mensaje.getBytes();
            //creamos otro datagrampacket para enviar el mensaje pero nos pedira 4 parametros
            //el mensaje, longitud mensaje, direccion y puerto
            DatagramPacket caja2 = new DatagramPacket(informacion2, informacion2.length, direccion, puerto);
            //ahora mandamos nuestro paquete
            socket.send(caja2);
            //mandamos mostrar a consola que se mando el mensaje a la siguiente direccion y su puerto de envio
            System.out.println("Se mando mensaje exitosamente a "+direccion.getHostAddress()+":"+puerto);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /* Comandos del servidor
    * \conectar:[nombrecliente]
    * \disconectar:[idcliente]
    */
    //para saber que mensajes mostrar y cuales no
    private static boolean ComandoServidor(String mensaje, DatagramPacket caja){
        if(mensaje.startsWith("\\conectar: ")){
            //codigo de conexion
            //buscamos obtener el nombre del cliente despues del :
            String nombreclientetemp = mensaje.substring(mensaje.indexOf(":")+1);
            //mandamos la informacion que ocupara el constructor de la clase infocliente
            informacioncliente.add(new infoCliente(nombreclientetemp, idcliente++, caja.getPort(), caja.getAddress()));
            Transmision("¡Usuario: "+nombreclientetemp+" conectado!");
            return true;
        }
        return false;
    }
}