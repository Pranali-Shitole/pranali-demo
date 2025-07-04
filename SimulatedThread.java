public class SimulatedThread {
    public final String id;
    public final int burstTime;
    public final int priority;
    public int remainingTime;

    public SimulatedThread(String id, int burstTime, int priority) {
        this.id = id;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }

    @Override
    public String toString() {
        return String.format("Thread[%s, Burst=%d, Priority=%d]", id, burstTime, priority);
    }
}

public class Scheduler {
    private final List<SimulatedThread> threads = new ArrayList<>();

    public void addThread(SimulatedThread t) {
        threads.add(t);
    }

    public void runFCFS() {
        System.out.println("== FCFS Scheduling ==");
        for (SimulatedThread t : threads) {
            System.out.println("Running " + t.id + " for " + t.burstTime + "ms");
            sleep(t.burstTime);
        }
    }

    public void runPriorityScheduling() {
        System.out.println("== Priority Scheduling ==");
        threads.sort(Comparator.comparingInt(t -> t.priority)); 
        for (SimulatedThread t : threads) {
            System.out.println("Running " + t.id + " (Priority " + t.priority + ") for " + t.burstTime + "ms");
            sleep(t.burstTime);
        }
    }

    public void runRoundRobin(int timeQuantum) {
        System.out.println("== Round Robin Scheduling (Time Quantum = " + timeQuantum + ") ==");
        Queue<SimulatedThread> queue = new LinkedList<>(threads);
        while (!queue.isEmpty()) {
            SimulatedThread t = queue.poll();
            if (t.remainingTime > 0) {
                int runTime = Math.min(timeQuantum, t.remainingTime);
                System.out.println("Running " + t.id + " for " + runTime + "ms (Remaining: " + (t.remainingTime - runTime) + ")");
                sleep(runTime);
                t.remainingTime -= runTime;
                if (t.remainingTime > 0) {
                    queue.offer(t);
                }
            }
        }
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms / 10); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
public class Main {
    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        scheduler.addThread(new SimulatedThread("T1", 100, 2));
        scheduler.addThread(new SimulatedThread("T2", 150, 1));
        scheduler.addThread(new SimulatedThread("T3", 80, 3));

        scheduler.runFCFS();
        System.out.println();

        resetScheduler(scheduler);
        scheduler.runPriorityScheduling();
        System.out.println();

        resetScheduler(scheduler);
        scheduler.runRoundRobin(50);
    }

    private static void resetScheduler(Scheduler scheduler) {
        scheduler.threads.clear();
        scheduler.addThread(new SimulatedThread("T1", 100, 2));
        scheduler.addThread(new SimulatedThread("T2", 150, 1));
        scheduler.addThread(new SimulatedThread("T3", 80, 3));
    }
}


