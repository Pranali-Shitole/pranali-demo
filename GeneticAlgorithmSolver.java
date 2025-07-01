
import java.util.*;

public class GeneticAlgorithmSolver {

    static final int GENE_LENGTH = 20;
    static final int POP_SIZE = 50;
    static final int MAX_GENERATIONS = 100;
    static final double MUTATION_RATE = 0.01;
    static final double CROSSOVER_RATE = 0.7;

    Random rand = new Random();

    class Individual {
        int[] genes = new int[GENE_LENGTH];
        int fitness;

        public Individual() {
            for (int i = 0; i < GENE_LENGTH; i++) {
                genes[i] = rand.nextBoolean() ? 1 : 0;
            }
            calculateFitness();
        }

        void calculateFitness() {
            fitness = 0;
            for (int gene : genes) {
                if (gene == 1) fitness++;
            }
        }

        Individual crossover(Individual partner) {
            Individual child = new Individual();
            int crossoverPoint = rand.nextInt(GENE_LENGTH);
            for (int i = 0; i < GENE_LENGTH; i++) {
                if (i < crossoverPoint) {
                    child.genes[i] = this.genes[i];
                } else {
                    child.genes[i] = partner.genes[i];
                }
            }
            child.calculateFitness();
            return child;
        }

        void mutate() {
            for (int i = 0; i < GENE_LENGTH; i++) {
                if (rand.nextDouble() < MUTATION_RATE) {
                    genes[i] = 1 - genes[i]; 
                }
            }
            calculateFitness();
        }

        @Override
        public String toString() {
            return Arrays.toString(genes) + " Fitness: " + fitness;
        }
    }

    Individual[] population = new Individual[POP_SIZE];

    public GeneticAlgorithmSolver() {
        for (int i = 0; i < POP_SIZE; i++) {
            population[i] = new Individual();
        }
    }

    public Individual selectParent() {
        int totalFitness = Arrays.stream(population).mapToInt(ind -> ind.fitness).sum();
        int pick = rand.nextInt(totalFitness);
        int sum = 0;
        for (Individual ind : population) {
            sum += ind.fitness;
            if (sum > pick) return ind;
        }
        return population[rand.nextInt(POP_SIZE)];
    }

    public void evolve() {
        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            Individual[] newPopulation = new Individual[POP_SIZE];

            for (int i = 0; i < POP_SIZE; i++) {
                Individual parent1 = selectParent();
                Individual parent2 = selectParent();

                Individual child;
                if (rand.nextDouble() < CROSSOVER_RATE) {
                    child = parent1.crossover(parent2);
                } else {
                    child = new Individual(); 
                }

                child.mutate();
                newPopulation[i] = child;
            }

            population = newPopulation;

            
            Individual best = Arrays.stream(population)
                                    .max(Comparator.comparingInt(ind -> ind.fitness))
                                    .orElse(null);
            System.out.printf("Generation %d | Best: %s%n", generation, best);

            if (best != null && best.fitness == GENE_LENGTH) {
                System.out.println("Optimal solution found!");
                break;
            }
        }
    }

    public static void main(String[] args) {
        GeneticAlgorithmSolver ga = new GeneticAlgorithmSolver();
        ga.evolve();
    }
}
