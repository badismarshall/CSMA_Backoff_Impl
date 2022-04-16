package sample;

import Support.Support;

public class veutTransmettre extends State{
    veutTransmettre(Station station) {
        super(station);
        Support.getInstance().subscribe(station);
        Runnable ecouteSupport = new ecouteSupport(station);
        station.changeState(new ecouteSupport(station));
        new Thread(ecouteSupport).start();
    }
}
