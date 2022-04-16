package sample;

public class collision extends State implements Runnable{
    Station station;
    collision(Station station) {
        super(station);
        //collisionSup();
        this.station = station;
    }

    private void collisionSup() {
        // l'ack n'a pas récu, il ya une collision
//        station.i += 1; // tentative de retransmission
//        if(station.CwMin < station.CwMax) {
//            station.CwMin = (int) Math.pow(2, 2 + station.i) - 1;
//            station.Cw = station.CwMin;
//            if (station.CwMin >= station.CwMax){
//                if(station.i == station.iLimite){
//                    // paquet rejeter !!!!!!!!!
//                    System.out.println(station.id +"paquet rejeter");
//                }
//                else {
//                    station.changeState(new ecouteSupport(station)); // en reecoute le support
//                }
//            }
//        }
    }

    @Override
    public void run() {
        // l'ack n'a pas récu, il ya une collision
        station.i += 1; // tentative de retransmission
        if(station.CwMin < station.CwMax) {
            station.CwMin = (int) Math.pow(2, 2 + station.i) - 1;
            station.Cw = station.CwMin;
            if (station.CwMin >= station.CwMax){
                if(station.i == station.iLimite){
                    // paquet rejeter !!!!!!!!!

                    System.out.println(station.id +" paquet rejeter");
                }
                else {
                    station.changeState(new ecouteSupport(station)); // en reecoute le support
                }
            }
        }
    }
}
