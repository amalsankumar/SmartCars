import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Vehicle {

    private String id;
    private int speed;
    private int direction;
    private int roadId;
    private Position position;
    private long positionSetTime;
    private Queue<Packet> packetIdQueue = new LinkedList<Packet>();
    private Lock lock;

    public Lock getLock() {
        return lock;
    }

    public boolean ifPacketQueueNotEmpty() {
        if(packetIdQueue.size() != 0)
            return true;
        return false;
    }

    public Packet getNextPacket() {

        return packetIdQueue.element();
    }

    public void pushPacket(Packet packet) {
        packetIdQueue.add(packet);
    }

    public void popPacket(Packet packet) {
        packetIdQueue.remove(packet);
    }

    public String getId() {
        return id;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDirection() {
        return direction;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) { this.position = position;}

    public int getRoadId() {
        return roadId;
    }

    public void setRoadId(int roadId) { this.roadId = roadId; }

    public long getPositionSetTime() { return positionSetTime ;}

    public Queue<Packet> getPacketQueue() { return packetIdQueue ;}

    public Vehicle() {
        Random rand = new Random();
        this.id = UUID.randomUUID().toString();
        this.speed = (int)(Math.random() * 60) + 30;
        this.direction = rand.nextInt(2) * 2 - 1;
        this.roadId = rand.nextInt(4);
        this.packetIdQueue.add(new Packet());
        position = new Position(this.roadId);
        positionSetTime = System.currentTimeMillis();
        lock = new ReentrantLock();
    }
}