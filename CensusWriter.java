package Networking.tcp.censusIspitna.server;

public interface CensusWriter {

    void addRecordToFile(String mailFrom, String mailTo, String data);
}
