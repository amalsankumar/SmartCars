import java.util.UUID;

public class Packet {

    private String id;
    private int survivalTime;
    private static final int size = 1;
    private String vehicleId;
    private long packetBirthTime;

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getId() {
        return id;
    }

    public int getSurvivalTime() {
        return survivalTime;
    }

    public void setSurvivalTime(double addTime) { this.survivalTime += addTime; };

    public long getpacketBirthTime() { return packetBirthTime ;}

    public static int getSize() {
        return size;
    }

    public Packet() {
        this.id = UUID.randomUUID().toString();
        this.survivalTime = 0;
        packetBirthTime = System.currentTimeMillis();
    }
}