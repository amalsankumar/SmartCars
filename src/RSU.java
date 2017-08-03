
public class RSU {

    private String vehicleId;
    private String packetId;
    private Position position;

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getPacketId() {
        return packetId;
    }
    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }
    public Position getPosition() {
        return position;
    }

    public RSU(int x, int y) {
        position = new Position(x, y);
    }

}
