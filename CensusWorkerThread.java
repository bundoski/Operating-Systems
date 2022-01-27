import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
            dos.writeUTF("READY"); // prajchame ready pri konekcija
            dos.flush();
            // send data as MAIL FROM:<emailFrom> ...
            String mailFrom = dis.readUTF(); // na ovaj nacin cekame string sho kliento bi ni go pratil..
            if(mailFrom.contains("@"))
                dos.writeUTF("250");
            else
                throw new IOException();
            System.out.printf("MAIL FROM: %s", mailFrom);
            System.out.println();

            // write MAIL TO:<emailTo> message.
            String mailTo = dis.readUTF();
            if(mailTo.contains("@")){
                dos.writeUTF("THANK YOU");
            }
            else
                throw new IOException();
            System.out.printf("MAIL TO: %s", mailTo);
            System.out.println();

            String subject = dis.readUTF();
            if(subject.length()<=200){
                dos.writeUTF("OK");
            }
            else
                throw new IOException();

            System.out.printf("SUBJECT: %s", subject);
            String totaldata = mailFrom + " " + mailTo + " " + subject;
            dos.writeUTF("RECEIVED: " + totaldata.length());
            // shtom dobijame index, ime i prezime , gi zapisuvame vo fajl.
            censusWriter.addRecordToFile(mailFrom, mailTo, subject); // gi dodavame iminjata i indekso itn

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
