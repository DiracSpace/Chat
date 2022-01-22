package chat;
import java.net.InetAddress;
public class infoCliente {
    
    //Clase para guardar la informacion del cliente. 
    //Cuando el cliente se conecte, crea un objeto y lo guardara en un array 
    //que usaremos para mandar mensajes.
    
    private InetAddress direccion = null;
    private String nombrecliente = " ";
    private int puerto = 0, idcliente = 0; 
    
    //hacemos el setter
    public infoCliente(String nombrecliente, int idcliente, int puerto, InetAddress direccion){
        //constructor para definir las variables ya establecidas 
        //se llamara cuando se cree el objeto donde estara la informacion del cliente
        
        this.nombrecliente = nombrecliente;
        this.idcliente = idcliente;
        this.puerto = puerto;
        this.direccion = direccion;
    }
    
    //hacemos los getters
    public String getnombrecliente(){
        return nombrecliente;
    }
    public int idcliente(){
        return idcliente;
    }
    public int getpuerto(){
        return puerto;
    }
    public InetAddress getdireccion(){
        return direccion;
    }
}