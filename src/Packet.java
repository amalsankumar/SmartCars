import java.util.UUID;
import java.util.Random;

public class Packet {

    private String id;
    private double transTime;
    private static int size;
    private String vehicleId;
    private long packetBirthTime;
    private int checkTower;     //which tower has selected the packet
    private int computationReq;


    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getId() {
        return id;
    }

    public double getTransTime() {
        return transTime;
    }

    public void setTransTime(double addTime) { this.transTime += addTime; }

    public long getpacketBirthTime() { return packetBirthTime ;}

    public int getSize() {
        return size;
    }

    public int getCheckTower() { return checkTower;}

    public void setCheckTower(int checkTower) { this.checkTower = checkTower; }

    public int getComputationReq() { return computationReq;}

    public Packet() {
        Random rand = new Random();
        this.id = UUID.randomUUID().toString();
        this.transTime = 0;
        packetBirthTime = System.currentTimeMillis();
        size = rand.nextInt(10000 - 100) + 100;            //setting packet size between 100kb and 10mb
        computationReq = rand.nextInt(90 - 40) + 40;
    }
}