package Networking.tcp.censusIspitna.client;

import Networking.tcp.censusIspitna.server.CensusServer;
import Networking.tcp.client.TCPclient;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class CensusClient extends Thread{

    // gi implementira obratnite funkcionalnosti fakticki... treba da nosi informacii do nekoj server / do toj fajl.
    private Socket socket;
    private String serverName; // ili ip.
    private int serverPort;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private Scanner scanner = null;
    public CensusClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run() {

        try {
            this.socket = new Socket(serverName, serverPort); // mu prajcame servername ( ip ), i server porta kaj so ce slusa.
            this.dis = new DataInputStream(this.socket.getInputStream());
            this.dos = new DataOutputStream(this.socket.getOutputStream());

            // ovde dobivame Vnesi index isprateno od strana na serverot, to se pecati kaj ekrano na kliento..
            String indexCmd = dis.readUTF();
            System.out.println(indexCmd);
            // vnesuvame nash index
            String index = scanner.nextLine();
            this.dos.writeUTF(index); // mu go prajchame vnesenio index..
            this.dos.flush();
            // dobivame poraka vnesi ime i prezime;
            String nameSurnameCmd = dis.readUTF();
            System.out.println(nameSurnameCmd);
            // ovde gi citame imeto i prezimeto
            String nameSurname = scanner.nextLine();
            this.dos.writeUTF(nameSurname);  // go prajchame ...
            this.dos.flush();


        } catch (IOException exception) {
            exception.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // prajme test na kliento..

        CensusClient client = new CensusClient("localhost", 9999);
        client.start();
    }
}
