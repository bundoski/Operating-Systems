package Networking.tcp.censusIspitna.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CensusWorkerThread extends Thread {

    private Socket socket = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private CensusWriter censusWriter;

    public CensusWorkerThread(Socket socket, CensusWriter censusWriter){

        this.socket = socket;
        this.censusWriter = censusWriter;
        try {
            this.dis = new DataInputStream(socket.getInputStream()); // za primanje na podatoci vo socket file.
            this.dos = new DataOutputStream(socket.getOutputStream());  // za da pratime podatoci do kliento

        } catch (IOException exception) {
            exception.printStackTrace();
        }

    } // novokreiraniot socket se zema i napraena e paralelizacija,
    // sledno, override na run metod.


    @Override
    public void run() {

        // ispisuva Vnesi index kaj kliento, pa ceka informacija od kliento.
        try {
            dos.writeUTF("Vnesi index: ");
            dos.flush();
            String index = dis.readUTF(); // na ovaj nacin cekame string sho kliento bi ni go pratil..
            dos.writeUTF("Vnesi ime i prezime");
            dos.flush();
            String nameSurname = dis.readUTF();
            // shtom dobijame index, ime i prezime , gi zapisuvame vo fajl.
            censusWriter.addRecordToFile(index, nameSurname); // gi dodavame iminjata i indekso itn

        } catch (IOException exception) {
            exception.printStackTrace();
        }
        try{
            if(dis!=null){
                dis.close();
            }
            if(dos!=null){
                dos.flush();
                dos.close();
            }
            socket.close(); // po zavrsuvanje na konekcija..
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
