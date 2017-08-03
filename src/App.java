import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class App implements Runnable {
    
    //When all to find the positions of the vehicles and update them ?
    //Implement multi hop
    //Implement the copy of packets in the neighbouring cars

    private static List< RSU[] > towers = new ArrayList< RSU[]>();
    private static int noOfCars = 100;
    private static List<Vehicle> cars = new ArrayList<Vehicle>();
    private volatile static boolean exit = false;

    double distanceCT(Vehicle car, RSU tower) {
        int carX = car.getPosition().getX(), carY = car.getPosition().getY();
        int towerX = tower.getPosition().getX(), towerY = tower.getPosition().getY();
        return Math.sqrt(Math.pow(carX - towerX, 2) + Math.pow(carY - towerY, 2));
    }

    double distanceCC(Vehicle car1, Vehicle car2) {
        int car1X = car1.getPosition().getX(), car1Y = car1.getPosition().getY();
        int car2X = car2.getPosition().getX(), car2Y = car2.getPosition().getY();
        return Math.sqrt(Math.pow(car1X - car2X, 2) + Math.pow(car1Y - car2Y, 2));
    }

    boolean isWithinRangeCT(Vehicle car, RSU tower) {
        return distanceCT(car, tower) <= 900;
    }

    boolean isWithinRangeCC(Vehicle car1, Vehicle car2) {
        return distanceCC(car1, car2) <= 300;
    }

    boolean isSelectedCar(int carId) {
        Lock lock = new ReentrantLock();
        if(lock.tryLock()) {
            return false;
        }
        else
            return true;

    }

    int neighborDirection(int id, Vehicle car, RSU tower) {
        int temp = 0;
        if(id == 0) {
            if(car.getPosition().getX() < tower.getPosition().getX())
                temp =  -1;
            else
                temp =  1;
        }
        if(id == 1) {
            if(car.getPosition().getY() < tower.getPosition().getY())
                temp =  -1;
            else
                temp =  1;

        }
        if(id == 2) {
            if(car.getPosition().getX() < tower.getPosition().getX())
                temp = 1;
            else
                temp = -1;
        }
        if(id == 3) {
            if(car.getPosition().getY() < tower.getPosition().getY())
                temp = 1;
            else
                temp = -1;
        }
        return temp;
    }

    public void run() {
        // 1. Select the car.
        // 2. Find the time to finish everything.
        // 3. Sleep for that much time.
        // 4. Go to 1.
        Thread t = Thread.currentThread();
        String[] s = t.getName().split(",");
        int roadId = Integer.parseInt(s[0]), towerId = Integer.parseInt(s[1]);
        RSU tower = towers.get(roadId)[towerId];
        List<Vehicle> carsWithinRange = new ArrayList<Vehicle>();
        List<Vehicle> broadcastCars = new ArrayList<Vehicle>();

        while(!exit) {
            carsWithinRange.clear();
            for (int i = 0; i < noOfCars; i++) {
                try {
                    Vehicle car = cars.get(i);
                    if (car.getRoadId() == Integer.parseInt(s[0]) && isWithinRangeCT(car, tower)) {
                        carsWithinRange.add(car);
                    }
                } catch(Exception ex) {
                    return;
                }
            }
            int totalCarsWithinRange = carsWithinRange.size();
            if (totalCarsWithinRange == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
            } else {
                Random rand = new Random();
                int carId = rand.nextInt(totalCarsWithinRange);
                Vehicle car = carsWithinRange.get(carId);
                Lock lock = car.getLock();
                if (lock.tryLock()) {
                    try {
                        if(distanceCT(car, tower) > 300) {
                            for (int i = 0; i < noOfCars; i++) {
                                Vehicle candidate = cars.get(i);
                                if (isWithinRangeCC(car, candidate)) {
                                    if (distanceCT(candidate, tower) < distanceCT(car, tower)) {
                                        //choose cars which go towards the tower
                                        int dir = neighborDirection(Integer.parseInt(s[0]), car, tower);
                                        if(candidate.getDirection() == dir) {
                                            broadcastCars.add(candidate);
                                        }
                                    }
                                }
                            }

                            if (car.ifPacketQueueNotEmpty()) {
                                Packet selectedPacket = car.getNextPacket();
                                car.popPacket(car.getNextPacket());
                                selectedPacket.setSurvivalTime(1.33);
                                Packet copyPack = selectedPacket;
                                for(int i = 0; i < broadcastCars.size(); i++) {
                                    Vehicle neighbor = broadcastCars.get(i);
                                    neighbor.pushPacket(copyPack);
                                }
                            }
                        }
                        else {
                            if (car.ifPacketQueueNotEmpty()) {
                                Packet selectedPacket = car.getNextPacket();
                                car.popPacket(car.getNextPacket());
                                long deathTime = System.currentTimeMillis();
                                selectedPacket.setSurvivalTime((deathTime - selectedPacket.getpacketBirthTime()) + 1.33);
                            }
                        }

                    } catch(Exception e) {

                    }
                    finally {
                        lock.unlock();
                    }
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                }
            }


        }
    }

    public static void stop() {
        exit = true;
    }


    public static void main(String[] args) {
        Packet packet = new Packet();
        Vehicle vehicle = new Vehicle();
        System.out.println(packet.getId() + "\n" + vehicle.getId());
        vehicle.pushPacket(packet);
        packet.setVehicleId(vehicle.getId());

        int p1 = 900, p2 = 900;
        for(int i = 0; i < 4; i++) {
            if(i % 2 == 0) {
                towers.add(new RSU[12]);
            } else {
                towers.add(new RSU[4]);
            }
            for(int j = 0 ; j < towers.get(i).length ; j++) {
                if(i == 0) {
                    towers.get(i)[j] = new RSU(p1, 0);
                    p1 += 1200;
                }
                else if(i == 1) {
                    towers.get(i)[j] = new RSU(15000, p2);
                    p2 += 1200;
                }
                else if(i == 2) {
                    p1 = 14100;
                    towers.get(i)[j] = new RSU(p1, 5000);
                    p1 -= 1200;
                }
                else if(i == 3) {
                    p2 = 4500;
                    towers.get(i)[j] = new RSU(0, p2);
                    p2 -= 1200;
                }
            }
        }

        for(int k = 0; k < 5; k++, noOfCars += 100) {
            cars.clear();
            for(int i = 0; i < noOfCars; i++) {
                cars.add(new Vehicle());
            }
            for(Integer i = 0; i < 4; i++) {
                for(Integer j = 0 ; j < towers.get(i).length ; j++) {
                    Thread t = (new Thread(new App()));
                    t.setName(i.toString() + "," + j.toString());
                    t.start();
                }
            }
            try {
                Thread.sleep(10000);
                // Notify all threads that its done
                stop();

            }
            catch(InterruptedException e) {

            }

        }


    }
}