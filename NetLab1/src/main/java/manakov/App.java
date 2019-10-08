package manakov;

import javafx.application.Application;
import javafx.stage.Stage;

import java.net.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class App extends Application
{
    public static void main( String[] args )
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            InetAddress address = InetAddress.getByName("255.255.255.255");
            int port = 5544;

            int timeout = 1000;

            DatagramSocket socket = new DatagramSocket(port);
            socket.setSoTimeout(timeout);

            Map<InetAddress, Date> map = new HashMap<>();

            byte buffer[] = new byte[0];

            DatagramPacket sendPacket = new DatagramPacket(buffer, 0, address, port);
            DatagramPacket recvPacket = new DatagramPacket(buffer, 0);

            socket.send(sendPacket);
            System.out.println("send");

            Date date = new Date();

            while (true) {
                if (new Date().getTime() - date.getTime() > 1000) {
                    socket.send(sendPacket);
                    System.out.println("send");
                    date = new Date();
                }

                try {
                    socket.receive(recvPacket);
                    if (map.put(recvPacket.getAddress(), new Date()) == null) {
                        System.out.println(map.size());
                    }
                } catch (SocketTimeoutException e) {
                    System.out.println("timeout");
                }

                Long time = new Date().getTime();
                for (InetAddress mapedAddress : map.keySet()) {
                    if (time - map.get(mapedAddress).getTime() < 5000) {
                        System.out.println(mapedAddress);
                    } else {
                        map.remove(mapedAddress);
                    }
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
