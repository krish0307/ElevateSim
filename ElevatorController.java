class Elevator {
    public static final int TOTAL_FLOORS = 61;
    public static final int NUM_ELEVATORS = 10;

    public int currentFloor;
    public int destinationFloor;
    public int[] requests;
    public Direction direction;
    public int previousFloor;
    
    public Elevator() {
        this.currentFloor = 1;
        this.requests = new int[TOTAL_FLOORS];
        this.direction = Direction.STATIONARY;
    }

    public enum Direction {
        UP, DOWN, STATIONARY
    }

    public void addRequest(int floor) {
        requests[floor-1] = 1;
        if (currentFloor < floor) {
            destinationFloor = floor;
            direction = Direction.UP;
        } else if (currentFloor > floor) {
            destinationFloor = floor;
            direction = Direction.DOWN;
        } // else stays STATIONARY if same floor.
    }

    public void move() {
        previousFloor = currentFloor;
        if (direction == Direction.UP) {
            currentFloor++;
        } else if (direction == Direction.DOWN) {
            currentFloor--;
        }

        if (requests[currentFloor-1] == 1) {
            requests[currentFloor-1] = 0;
        }

        if (currentFloor == destinationFloor) {
            direction = Direction.STATIONARY;
        }
    }
}


public class ElevatorController {
    public Elevator[] elevators;

    public ElevatorController() {
        elevators = new Elevator[Elevator.NUM_ELEVATORS];
        for (int i = 0; i < Elevator.NUM_ELEVATORS; i++) {
            elevators[i] = new Elevator();
        }
    }

    public synchronized void requestElevator(int floor) {
        Elevator bestElevator = findBestElevator(floor);
        bestElevator.addRequest(floor);
    }

    private Elevator findBestElevator(int floor) {
        Elevator bestElevator = null;
        int minDistance = Elevator.TOTAL_FLOORS + 1;
        int minRequests = Elevator.TOTAL_FLOORS + 1;

        for (Elevator elevator : elevators) {
            if (elevator.direction == Elevator.Direction.STATIONARY && Math.abs(elevator.currentFloor - floor) < minDistance) {
                bestElevator = elevator;
                minDistance = Math.abs(elevator.currentFloor - floor);
            } else if ((floor > elevator.currentFloor && elevator.direction == Elevator.Direction.UP) ||
                       (floor < elevator.currentFloor && elevator.direction == Elevator.Direction.DOWN)) {
                return elevator;
            } else if (bestElevator == null || getPendingRequests(elevator) < minRequests) {
                bestElevator = elevator;
                minRequests = getPendingRequests(elevator);
            }
        }

        return bestElevator;
    }

    private int getPendingRequests(Elevator elevator) {
        int count = 0;
        for (int i = 0; i < Elevator.TOTAL_FLOORS; i++) {
            if (elevator.requests[i] == 1) {
                count++;
            }
        }
        return count;
    }

    public void moveElevators() {
        for (Elevator elevator : elevators) {
            elevator.move();
        }
    }
}

