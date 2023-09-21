import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ElevatorVisualizer {
    private JFrame frame;
    private JPanel[] floorPanels;
    private JButton[] elevatorButtons;

    public ElevatorVisualizer(ElevatorController controller) {
        frame = new JFrame("Elevator Visualization");
        frame.setSize(300, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(Elevator.TOTAL_FLOORS, 1));

        floorPanels = new JPanel[Elevator.TOTAL_FLOORS];
        elevatorButtons = new JButton[Elevator.NUM_ELEVATORS];

        for (int i = Elevator.TOTAL_FLOORS - 1; i >= 0; i--) {
            floorPanels[i] = new JPanel(new GridLayout(1, Elevator.NUM_ELEVATORS));
            frame.add(floorPanels[i]);
        }

        for (int i = 0; i < Elevator.NUM_ELEVATORS; i++) {
            elevatorButtons[i] = new JButton("E" + (i + 1));
            elevatorButtons[i].setBackground(Color.GRAY);
            floorPanels[0].add(elevatorButtons[i]);
        }

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateElevators(controller);
            }
        });
        timer.start();

        frame.setVisible(true);
    }

    public void updateVisualRepresentation(ElevatorController controller) {
    for (int i = 0; i < Elevator.NUM_ELEVATORS; i++) {
        // Remove the elevator button from its previous floor
        floorPanels[controller.elevators[i].previousFloor - 1].remove(elevatorButtons[i]);

        // Add the elevator button to its current floor
        floorPanels[controller.elevators[i].currentFloor - 1].add(elevatorButtons[i]);

        frame.revalidate();
        frame.repaint();
    }
}


    private void updateElevators(ElevatorController controller) {
        for (int i = 0; i < Elevator.NUM_ELEVATORS; i++) {
            floorPanels[controller.elevators[i].currentFloor - 1].remove(elevatorButtons[i]);
            controller.elevators[i].move();
            floorPanels[controller.elevators[i].currentFloor - 1].add(elevatorButtons[i]);

            frame.revalidate();
            frame.repaint();
        }
    }

    public static void main(String[] args) {
        ElevatorController controller = new ElevatorController();
        ElevatorVisualizer visualizer = new ElevatorVisualizer(controller);

        controller.requestElevator(10);
        controller.requestElevator(30);
        // Add more requests as needed.
    }
}
