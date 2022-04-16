package sample;

import java.lang.management.ThreadMXBean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class attendAck extends State implements Runnable{
    Station station ;
    Logger my_log = Logger.getLogger(Station.class.getName());
    attendAck(Station station) {
        super(station);
        //waitAck(station);
        this.station = station;
    }

    private void waitAck(Station station) {
//        System.out.println("la station attendre un ack");
//        // entrain de attandre la trame d'ack
//        while (!station.ackRecu && station.timeWaitingAck != 0) station.timeWaitingAck -= 1; // attndre l'ack
//        if(station.ackRecu) {
//            // l'ack est recu acun probleme montioné
//            station.ackRecu = false;
//            // ile fault une simulation !!!!!!!!!!!!!!!!!!!!!!!!
//        }
//        else {
//            station.timeWaitingAck = 5;
//            // l'ack n'a pas récu, il ya une collision
//            System.out.println("il y a une collision ");
//            station.changeState(new collision(station));
//        }
    }

    @Override
    public void run() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // entrain de attandre la trame d'ack
        try { // attndre l'ack
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (!station.ackRecu && station.timeWaitingAck != 0) station.timeWaitingAck -= 1; // attndre l'ack
        //my_log.log(Level.INFO,station.id + "la station attendre un ack");
        //System.out.println(station.id + "la station attendre un ack");
        if(station.ackRecu) {
            // l'ack est recu acun probleme montioné
            station.ackRecu = false;
            // ile fault une simulation !!!!!!!!!!!!!!!!!!!!!!!!
        }
        else {
            //station.timeWaitingAck = 5;
            // l'ack n'a pas récu, il ya une collision
            my_log.log(Level.INFO,station.id + " il y a une collision ");
            //System.out.println(station.id + "il y a une collision ");
            Runnable colission = new collision(station);
            station.changeState(new collision(station));
            new Thread(colission).start();
        }

    }
}
