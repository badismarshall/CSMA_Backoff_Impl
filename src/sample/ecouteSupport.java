package sample;

import Support.Support;
import Support.Libre;
import Support.occupe;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ecouteSupport extends State implements  Runnable{
    Station station;
    Logger my_log = Logger.getLogger(Station.class.getName());
    ecouteSupport(Station station) {
        super(station);
        this.station = station;
          // appel a la fonction d'ecoute
    }

    public void  ecoute(Station station){
        if (station.firstEcoute){  // la station veut transmmetre au début
            my_log.log(Level.INFO,station.id + "la station ecoute le support pour la premiere fois");
            //System.out.println(station.id + "la station ecoute le support pour la premiere fois");
            station.firstEcoute = false;
            if(Support.getInstance().getState().getClass() == Libre.class){ // le support est initialement libre
                my_log.log(Level.INFO,station.id + "le support est initialement libre");
                //System.out.println(station.id + "le support est initialement libre");
                // attendre un DIFS
                // while (station.ecouteDIFS != 0)  station.ecouteDIFS -= 1; // attendre un DIFS

                // station.ecouteDIFS = station.DIFS;
                // Transmettre la trame
                Semaphore semaphore = new Semaphore(1);
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Support.getInstance().changeStateSupport(new occupe(Support.getInstance()));
                semaphore.release();

                try {
                    Thread.sleep(station.DIFS * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Runnable envoiTrame = new envoyeTrame(station, "données",null);  // pas de destinataire
                station.changeState(new envoyeTrame(station, "données", null));

                // attend un ack
                Runnable attebdAck = new attendAck(station);
                station.changeState(new attendAck(station));
                // le support devinet occupé


                new Thread(envoiTrame).start();
                new Thread(attebdAck).start();
            }
            else { // le support est initialement occupé
                my_log.log(Level.INFO,station.id + "le support est intialement occupé");
                //System.out.println(station.id + "le support est intialement occupé");
                Random rand = new Random(); //instance of random class
                int int_random = rand.nextInt(station.Cw + 1); // 0 ou 7
                station.Tomporisateur = int_random * station.TimeSlot;
                ecoute(station);   // la station reste en ecoute
            }
        }
        else {  // une tentative de transsmision pas la premiere!
            st:
            if (Support.getInstance().getState().getClass() == Libre.class) { // le suppport est libre
                my_log.log(Level.INFO,station.id + "le support est libre");
                //System.out.println(station.id + "le support est libre");
                // while (station.ecouteDIFS != 0)  station.ecouteDIFS -= 1; // attendre un DIFS
                // station.ecouteDIFS = station.DIFS;
                try {
                    Thread.sleep(station.DIFS * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (Support.getInstance().getState().getClass() == Libre.class && station.Tomporisateur != 0) {
                    station.Tomporisateur -= 1; // décrenmenter le Tomporisateur
                }
                if (Support.getInstance().getState().getClass() == occupe.class) { // le support est de nouveau occupé
                    while (Support.getInstance().getState().getClass() == occupe.class) ;
                    break st;  // goto st
                }
                if (station.Tomporisateur == 0) {
                    my_log.log(Level.INFO,station.id + "station donc peut envoyee tompo = 0");
                    //System.out.println(station.id + "station donc peut envoyee tompo = 0");
                    Semaphore semaphore = new Semaphore(1);
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Support.getInstance().changeStateSupport(new occupe(Support.getInstance()));
                    semaphore.release();
                    Runnable envoiTrame = new envoyeTrame(station, "données", null); // elle conait pas le destinataire :: puf hhh
                    station.changeState(new envoyeTrame(station, "données", null)); // la station émettre une trame de donnée // pas destinataire
                    // attend d'ack
                    Runnable attebdAck = new attendAck(station);
                    station.changeState(new attendAck(station));

                    new Thread(envoiTrame).start();
                    new Thread(attebdAck).start();
                }
            }
            else { // le support est occupé
                my_log.log(Level.INFO,station.id + " les support reste occupé");
                //System.out.println(station.id + " les support reste occupé");
                Runnable ecouteSupport = new ecouteSupport(station);
                new Thread(ecouteSupport).start();
                //ecoute(station);  // la station reste en écoute
            }
        }
    }

    @Override
    public void run() {
       ecoute(this.station);
    }
}
