import java.util.*;

public class PaxosDemo {
    public static void main(String[] args) {
        List<Acceptor> acceptors = Arrays.asList(new Acceptor(), new Acceptor(), new Acceptor());
        Learner learner = new Learner();
        Proposer proposer = new Proposer(1, "VALUE_X", acceptors, learner);

        proposer.propose();
    }
}
import java.util.*;

public class Proposer {
    private final int proposalNumber;
    private final String proposedValue;
    private final List<Acceptor> acceptors;
    private final Learner learner;

    public Proposer(int proposalNumber, String proposedValue, List<Acceptor> acceptors, Learner learner) {
        this.proposalNumber = proposalNumber;
        this.proposedValue = proposedValue;
        this.acceptors = acceptors;
        this.learner = learner;
    }

    public void propose() {
        int majority = (acceptors.size() / 2) + 1;
        int promised = 0;
        for (Acceptor acceptor : acceptors) {
            if (acceptor.prepare(proposalNumber)) {
                promised++;
            }
        }

        if (promised >= majority) {
            int accepted = 0;
            for (Acceptor acceptor : acceptors) {
                if (acceptor.accept(proposalNumber, proposedValue)) {
                    accepted++;
                }
            }

            if (accepted >= majority) {
                learner.learn(proposedValue);
            } else {
                System.out.println("Proposal rejected in accept phase.");
            }
        } else {
            System.out.println("Proposal rejected in prepare phase.");
        }
    }
}
public class Acceptor {
    private int promisedProposal = -1;
    private int acceptedProposal = -1;
    private String acceptedValue = null;

    public boolean prepare(int proposalNumber) {
        if (proposalNumber > promisedProposal) {
            promisedProposal = proposalNumber;
            return true;
        }
        return false;
    }

    public boolean accept(int proposalNumber, String value) {
        if (proposalNumber >= promisedProposal) {
            promisedProposal = proposalNumber;
            acceptedProposal = proposalNumber;
            acceptedValue = value;
            return true;
        }
        return false;
    }
}
public class Learner {
    public void learn(String value) {
        System.out.println("Consensus reached. Learned value: " + value);
    }
}



