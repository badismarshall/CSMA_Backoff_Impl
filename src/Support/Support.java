package Support;

import sample.Station;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Support{
    Logger my_log = Logger.getLogger(Station.class.getName());
    public synchronized static Support getInstance(){
        Semaphore semaphore = new Semaphore(1);
        return SupportHolder.INSTANCE;  // l'instance de support
    }

    private static class SupportHolder {   // pour le moment un seule support
        private static final Support INSTANCE = new Support();
    }


    SupportState state;                                 // l'état de support (libre ou occupé)
    List<Subscriber> subscriber = new LinkedList<>();  // liste des station qui sont dans la zone de support


    private Support(){
        my_log.log(Level.INFO,"Support creé");
        //System.out.println("Support creé");
        this.state = new Libre(this);
    }

    public synchronized void changeStateSupport(SupportState state){
        this.state = state;
    }

    public void notifyStation(){  // le suport notify qu'il a changer l'etat
        for (Subscriber sb :subscriber){  // le support notify les station qu'il a changer son état
            sb.update();
        }
    }

    public void subscribe(Subscriber sb){
        subscriber.add(sb);
    }

    public void unsubscribe(Subscriber sb){
        subscriber.remove(sb);
    }

    public SupportState getState(){
        return state;
    }

    public synchronized void envoieDuMesaage(Station st, String type, Station dest) { // st = src ou dest
        if (type == "données") { // envoi une trame de données st = src
            try {
                Thread.sleep(500); // wait some millis;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            my_log.log(Level.INFO,st.id +"la station envoi la donnée");
            //System.out.println(st.id +"la station envoi la donnée");
            for (Subscriber sb : Zone.getInstance().stations) {
                if (sb != st) {  // cherche a une destination random  (default)
                    sb.recvoirTrame(st,type); // sb c'est la station destinataire  et st c'est la src
                    break;
                }
            }
        }
        else {      // envoi une trame d'ack  st = dest
            my_log.log(Level.INFO,st.id + "la station envoi une trame d'ack to " + dest.id);
            //System.out.println(st.id + "la station envoi une trame d'ack to " + dest.id);
            dest.recvoirTrame(st,type); // envoi une notification au staion destinataire dest
        }
    }
}
