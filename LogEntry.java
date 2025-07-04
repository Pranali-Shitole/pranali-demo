public class LogEntry {
    public final int term;
    public final String command;

    public LogEntry(int term, String command) {
        this.term = term;
        this.command = command;
    }

    @Override
    public String toString() {
        return "[term=" + term + ", command=" + command + "]";
    }
}

public class RaftNode {
    public enum State { FOLLOWER, CANDIDATE, LEADER }

    public int id;
    public State state = State.FOLLOWER;
    public int currentTerm = 0;
    public Integer votedFor = null;
    public List<LogEntry> log = new ArrayList<>();

    private final RaftCluster cluster;

    public RaftNode(int id, RaftCluster cluster) {
        this.id = id;
        this.cluster = cluster;
    }

    public void startElection() {
        currentTerm++;
        state = State.CANDIDATE;
        votedFor = id;
        int votes = 1;

        System.out.println("Node " + id + " starting election for term " + currentTerm);

        for (RaftNode peer : cluster.nodes) {
            if (peer == this) continue;

            if (peer.requestVote(currentTerm, id)) {
                votes++;
            }
        }

        if (votes > cluster.nodes.size() / 2) {
            state = State.LEADER;
            System.out.println("Node " + id + " becomes leader for term " + currentTerm);
        } else {
            System.out.println("Node " + id + " failed to become leader.");
            state = State.FOLLOWER;
        }
    }

    public boolean requestVote(int term, int candidateId) {
        if (term > currentTerm) {
            currentTerm = term;
            state = State.FOLLOWER;
            votedFor = null;
        }

        if ((votedFor == null || votedFor == candidateId) && term >= currentTerm) {
            votedFor = candidateId;
            return true;
        }

        return false;
    }

    public void appendEntry(String command) {
        if (state != State.LEADER) {
            System.out.println("Node " + id + " is not the leader.");
            return;
        }

        LogEntry entry = new LogEntry(currentTerm, command);
        log.add(entry);

        for (RaftNode peer : cluster.nodes) {
            if (peer != this) {
                peer.receiveEntry(entry);
            }
        }

        System.out.println("Leader " + id + " appended entry: " + command);
    }

    public void receiveEntry(LogEntry entry) {
        log.add(entry);
        System.out.println("Node " + id + " replicated entry: " + entry.command);
    }
}

public class RaftCluster {
    public final List<RaftNode> nodes = new ArrayList<>();

    public void addNode(RaftNode node) {
        nodes.add(node);
    }

    public RaftNode getLeader() {
        for (RaftNode node : nodes) {
            if (node.state == RaftNode.State.LEADER) return node;
        }
        return null;
    }
}
public class Main {
    public static void main(String[] args) {
        RaftCluster cluster = new RaftCluster();
        for (int i = 0; i < 3; i++) {
            cluster.addNode(new RaftNode(i, cluster));
        }
        for (RaftNode node : cluster.nodes) {
            node.startElection();
            if (cluster.getLeader() != null) break;
        }
        RaftNode leader = cluster.getLeader();
        if (leader != null) {
            leader.appendEntry("set x=1");
        }
        System.out.println("\nFinal logs:");
        for (RaftNode node : cluster.nodes) {
            System.out.println("Node " + node.id + ": " + node.log);
        }
    }
}


