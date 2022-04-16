package sample;

import Support.Subscriber;
import Support.Support;

public class envoyeTrame extends State implements Runnable{
    String type;
    Station src;
    Station dest;
    envoyeTrame(Station station, String type, Station dest) {
        super(station);
        //SimulateTrame(station,type);
        this.src = station;
        this.type = type;
        this.dest = dest;
    }

    public void SimulateTrame(Station station,String type){
        if (type == "données") {
            Support.getInstance().envoieDuMesaage(station, type, null); // le support c'est lui qui va émetre la trame
        }
        else { // une trame d'ack
            Support.getInstance().envoieDuMesaage(null, type, station);  // le support envoi la trame d'ack
        }
    }

    @Override
    public void run() {
            Support.getInstance().envoieDuMesaage(src, type, dest); // le support c'est lui qui va émetre la trame
    }
}
