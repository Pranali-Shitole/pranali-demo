import java.io.*;
import java.util.*;

public class ETLJob implements Runnable {
    private String sourceFile;
    private String targetFile;
    private String jobName;
    private String status = "PENDING";

    public ETLJob(String jobName, String sourceFile, String targetFile) {
        this.jobName = jobName;
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
    }

    public String getStatus() {
        return status;
    }

    public String getJobName() {
        return jobName;
    }

    @Override
    public void run() {
        try {
            status = "RUNNING";
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line.toUpperCase());
                }
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(targetFile))) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            }

            status = "COMPLETED";
        } catch (Exception e) {
            status = "FAILED";
            e.printStackTrace();
        }
    }
}

public class JobManager {
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final List<ETLJob> jobHistory = new ArrayList<>();

    public void submitJob(ETLJob job) {
        jobHistory.add(job);
        executor.submit(job);
    }

    public List<ETLJob> getJobHistory() {
        return jobHistory;
    }

    public void shutdown() {
        executor.shutdown();
    }
}

public class ETLOrchestrator {
    private final JobManager jobManager = new JobManager();
    private final DefaultListModel<String> jobListModel = new DefaultListModel<>();
    private final JList<String> jobList = new JList<>(jobListModel);
    private final Timer refreshTimer;

    public ETLOrchestrator() {
        JFrame frame = new JFrame("ETL Orchestration UI");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField sourceField = new JTextField("data/input.csv", 20);
        JTextField targetField = new JTextField("data/output.csv", 20);
        JTextField jobNameField = new JTextField("Job1", 10);

        JButton runButton = new JButton("Run ETL Job");

        runButton.addActionListener((ActionEvent e) -> {
            String source = sourceField.getText();
            String target = targetField.getText();
            String jobName = jobNameField.getText();

            ETLJob job = new ETLJob(jobName, source, target);
            jobManager.submitJob(job);
            jobListModel.addElement(job.getJobName() + " - " + job.getStatus());
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Job Name:"));
        inputPanel.add(jobNameField);
        inputPanel.add(new JLabel("Source:"));
        inputPanel.add(sourceField);
        inputPanel.add(new JLabel("Target:"));
        inputPanel.add(targetField);
        inputPanel.add(runButton);

        frame.setLayout(new BorderLayout());
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(jobList), BorderLayout.CENTER);
        refreshTimer = new Timer(1000, (ActionEvent e) -> refreshJobList());
        refreshTimer.start();

        frame.setVisible(true);
    }

    private void refreshJobList() {
        List<ETLJob> jobs = jobManager.getJobHistory();
        jobListModel.clear();
        for (ETLJob job : jobs) {
            jobListModel.addElement(job.getJobName() + " - " + job.getStatus());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ETLOrchestrator::new);
    }
}


