package sample;
import Support.Subscriber;
import Support.Support;
import Support.Zone;
import Support.Libre;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Station implements Subscriber,Runnable {
    Logger my_log = Logger.getLogger(Station.class.getName());
    State state;
    boolean firstEcoute = true;
    int CwMinInitale = 7;       //  la taille initiale de la fentere
    int CwMin = CwMinInitale;   //  la taille min de fenetre
    int Cw = CwMin;             // la taille de fenetre courant
    int CwMax = 255;            // la taile de fenetre max
    int iLimite = 5;            // seuil de transmission
    int i = 1;                  // nombre de tentative
    int TimeSlot = 100;          // timeSlot = 100us
    int SIFS = 10;              // SIFS = 10
    int DIFS = SIFS + 2 * TimeSlot;  // DIFS
    int PIFS = SIFS + TimeSlot;        // PIFS
    int Tomporisateur;                 // BackOff
    boolean ackRecu = false;
    int timeWaitingAck = 25;
    int ecouteDIFS = DIFS;
    public int id;
    public Station(int id){  // le constructeur
    this.id = id;
    }

    public void runThis(){
        state = new dansLereseau(this); // la station ecoute le support   (dansLeReseau)
        my_log.log(Level.INFO,id + " Staion crée");
        //System.out.println(id + " Staion crée");
        Zone.getInstance().addToZone(this); // ajouter la station au zone
        Random rand = new Random(); //instance of random class
        int upperbound = 2;
        int int_random = rand.nextInt(upperbound); // 0 ou 1
        if(int_random == 1){  // si int_random = 1 la satation veut transmetre
            my_log.log(Level.INFO,id + " une station veut transmmtre");
            //System.out.println(id + " une station veut transmmtre");

            changeState(new veutTransmettre(this));  // la station veut transmettre le msg (veutTransmettre)
        }
    }

    public void changeState(State state){   // changer l'etat du support
        this.state = state;
    }

    @Override
    public void update() {
        // fait rien
    }

    @Override
    public synchronized void recvoirTrame(Station st, String Type) {
        if(Type == "données"){  // la station recoi une trame de données
            Semaphore semaphore = new Semaphore(1);
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Support.getInstance().changeStateSupport(new Libre(Support.getInstance()));
            semaphore.release();
            // simulation d'une trame de source vers destination
            my_log.log(Level.INFO,id + " trame du donnée recu from " + st.id);
            //System.out.println(id +" trame du donnée recu from" + st.id);
            // elle doit envoyé un ack
            Random rand = new Random();
            int upperbound = 2;
            int int_random = 1; //rand.nextInt(upperbound); // 0 ou 1
            if(int_random == 1) {  // l'envoi du trame peut ne pas etre envoyer  // !!!! pour le moment elle est envoyé
                //System.out.println(id +" trame d'ack sera envoyée");
                Runnable envoiTrame = new envoyeTrame(this, "ack", st);
                this.changeState(new envoyeTrame(this, "ack", st));
                new Thread(envoiTrame).start();
            }else { // on utilse pas sa
                System.out.println(id +" trame d'ack ne sera pas envoyée");
            }
        }
        else {  // une trame d'ack
            if(timeWaitingAck == 0) {
                ackRecu = true;
                my_log.log(Level.INFO,id + " trame d'ack recu from :" + st.id);
                //System.out.println(id + " trame d'ack recu from :" + st.id);
                Semaphore semaphore = new Semaphore(1);
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Support.getInstance().changeStateSupport(new Libre(Support.getInstance()));
                semaphore.release();
                i += 1;
                CwMin = CwMinInitale;
            }
            else {
                Semaphore semaphore = new Semaphore(1);
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Support.getInstance().changeStateSupport(new Libre(Support.getInstance()));
                semaphore.release();
                my_log.log(Level.INFO,id + " trame d'ack n'pas recu from " + st.id);
                //System.out.println(id + " trame d'ack n'pas recu from " + st.id);
            }
        }
    }

    @Override
    public void run() {
        runThis();
    }
}
