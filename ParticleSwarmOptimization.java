import java.util.Arrays;
import java.util.Random;

public class ParticleSwarmOptimization {

    static final int DIMENSIONS = 2;
    static final int PARTICLE_COUNT = 30;
    static final int MAX_ITERATIONS = 100;
    static final double W = 0.5;   
    static final double C1 = 1.5;  
    static final double C2 = 1.5;  

    static final double MIN_POS = -10;
    static final double MAX_POS = 10;
    static final double MIN_VEL = -1;
    static final double MAX_VEL = 1;

    Random rand = new Random();

    class Particle {
        double[] position = new double[DIMENSIONS];
        double[] velocity = new double[DIMENSIONS];
        double[] bestPosition = new double[DIMENSIONS];
        double bestFitness = Double.MAX_VALUE;

        void initialize() {
            for (int i = 0; i < DIMENSIONS; i++) {
                position[i] = MIN_POS + rand.nextDouble() * (MAX_POS - MIN_POS);
                velocity[i] = MIN_VEL + rand.nextDouble() * (MAX_VEL - MIN_VEL);
                bestPosition[i] = position[i];
            }
            bestFitness = evaluate(position);
        }

        void updateVelocity(double[] globalBest) {
            for (int i = 0; i < DIMENSIONS; i++) {
                double r1 = rand.nextDouble();
                double r2 = rand.nextDouble();
                velocity[i] = W * velocity[i]
                            + C1 * r1 * (bestPosition[i] - position[i])
                            + C2 * r2 * (globalBest[i] - position[i]);
                velocity[i] = Math.max(MIN_VEL, Math.min(MAX_VEL, velocity[i]));
            }
        }

        void updatePosition() {
            for (int i = 0; i < DIMENSIONS; i++) {
                position[i] += velocity[i];
                position[i] = Math.max(MIN_POS, Math.min(MAX_POS, position[i]));
            }

            double fitness = evaluate(position);
            if (fitness < bestFitness) {
                bestFitness = fitness;
                bestPosition = Arrays.copyOf(position, DIMENSIONS);
            }
        }
    }

    double evaluate(double[] pos) {
        double sum = 0.0;
        for (double x : pos) {
            sum += x * x;
        }
        return sum;
    }

    public void run() {
        Particle[] swarm = new Particle[PARTICLE_COUNT];
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            swarm[i] = new Particle();
            swarm[i].initialize();
        }

        double[] globalBest = new double[DIMENSIONS];
        double globalBestFitness = Double.MAX_VALUE;
        for (Particle p : swarm) {
            if (p.bestFitness < globalBestFitness) {
                globalBestFitness = p.bestFitness;
                globalBest = Arrays.copyOf(p.bestPosition, DIMENSIONS);
            }
        }
        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            for (Particle p : swarm) {
                p.updateVelocity(globalBest);
                p.updatePosition();

                if (p.bestFitness < globalBestFitness) {
                    globalBestFitness = p.bestFitness;
                    globalBest = Arrays.copyOf(p.bestPosition, DIMENSIONS);
                }
            }

            System.out.printf("Iteration %d | Best Fitness: %.6f | Best Position: %s%n",
                    iter, globalBestFitness, Arrays.toString(globalBest));
        }

        System.out.println("Optimization Complete.");
        System.out.printf("Global Best Position: %s%n", Arrays.toString(globalBest));
        System.out.printf("Global Best Fitness: %.6f%n", globalBestFitness);
    }

    public static void main(String[] args) {
        new ParticleSwarmOptimization().run();
    }
}

