package Support;

import sample.Station;

public interface Subscriber {
    public void update();
    public void recvoirTrame(Station st, String Type);
}
