package Support;


import java.util.LinkedList;
import java.util.List;

public class Zone {
    protected List<Subscriber> stations = new LinkedList<>();  // liste des station dans la zone

    public static Zone getInstance(){
        return ZoneHolder.INSTANCE;  // l'instance du zone
    }

    private static class ZoneHolder {   // pour le moment une seule zone
        private static final Zone INSTANCE = new Zone();
    }

    Zone(){
    }
    public void addToZone(Subscriber sb){
        stations.add(sb);
    }
    public void removeFromZone(Subscriber sb){
        stations.remove(sb);
    }
    public List<Subscriber> getStations(){
        return stations;
    }
}
