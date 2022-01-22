package chat;

public class Chat {

    public static void main(String[] args) {
        
        //el servidor inicia, pero el proceso termina debido a que no hemos establecido, o creado, nuestro
        //metodo escucharmensajes, el cual es el que mantiene un servidor en constante busqueda del cliente
        Server.EmpezarServidor(6056);
    }
    
}
