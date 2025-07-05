import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class LoggingAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("[Agent] Starting...");
        inst.addTransformer(new ClassLoggerTransformer());
    }
}

class ClassLoggerTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        System.out.println("[Agent] Loading class: " + className);
        return null; 
    }
}
public class App {
    public static void main(String[] args) {
        System.out.println("Hello from App");
    }
}

