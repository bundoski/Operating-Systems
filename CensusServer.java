package Networking.tcp.censusIspitna.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CensusServer extends Thread implements CensusWriter {

    private int serverPort;
    private ServerSocket server = null;
    private PrintWriter fileWriter = null;
    private CensusWriter writer;

    public CensusServer(int serverPort, String filePath) throws FileNotFoundException {   // mora da inicijalizirame na koja porta slusa

        this.serverPort = serverPort;   // sekogas step1.
        fileWriter = new PrintWriter(new File(filePath));
    }

    @Override
    public void run() {

        System.out.println("Server is starting...."); // koga e vo run sostojba pecatime deka startnuva
        // pocnuvame so gradenje na server da slusa na odredena porta..
        try{
            server = new ServerSocket(serverPort);
        } catch (IOException exception) {
            exception.printStackTrace();
            System.out.println("Server failed to start");
        }

        // ako startnal uspesno posle ova pogore, pecatime uspesnost.
        System.out.println("Server started successfully");

        while(true){   // ceka konekcii i prima informacii..

            try {
                // duri ne dobija baranje od klient, ne prodolzuva so rabota.
                Socket socket = server.accept();
                // go obsluzuvame dobieniot klient so to sho startnuvame nov working thread, ova go prajme za
                // paralelizacija
                new CensusWorkerThread(socket, this).start(); // prajme paralelizacija, otvorame novi threadovi kolku so e potrebno.

            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }
    }

    // bitno da se zapamti kaj ovaj metod e deka e kriticen blok na kod
    // i tuka morame da implementirame nekakva sinhronizacija.
    // samo klavame synchronized posle public deklaracijata..
    // isto mozi i so synchronized(this) { ovde kodo so treba da se izvrsi }
   public  void addRecordToFile(String indeks, String nameSurname){  // ZA ZAPISUVANJE KON NEKOJ FAJL. gore pram private PrintWriter fileWriter;
        // zapisuvame podatoci vo format 111111 ime prezime 00:00:00
       synchronized (this) {
           fileWriter.append(String.format("%s:%s:%s", indeks, nameSurname, LocalDateTime.now().format(DateTimeFormatter.ISO_TIME)));
           fileWriter.flush();
       }
    }

    public static void main(String[] args) throws FileNotFoundException {

        CensusServer server = new CensusServer(9999, "census.txt");
        server.start();
    }
}
