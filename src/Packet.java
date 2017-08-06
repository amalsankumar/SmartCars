import java.util.UUID;

public class Packet {

    private String id;
    private double transTime;
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

    public double getTransTime() {
        return transTime;
    }

    public void setTransTime(double addTime) { this.transTime += addTime; };

    public long getpacketBirthTime() { return packetBirthTime ;}

    public static int getSize() {
        return size;
    }

    public Packet() {
        this.id = UUID.randomUUID().toString();
        this.transTime = 0;
        packetBirthTime = System.currentTimeMillis();
    }
}