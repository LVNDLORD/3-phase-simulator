
public class Main {

    public static void main(String[] args) {
        System.out.println("Simulator started\n");

        Engine engine = new MyEngine();
        engine.setSimulationTime(1000);
        engine.run();
    }
}
