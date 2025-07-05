import java.io.*;
import java.nio.file.*;

public class CustomClassLoader extends ClassLoader {
    private final String classesDir;

    public CustomClassLoader(String classesDir) {
        this.classesDir = classesDir;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            String fileName = classesDir + "/" + name.replace('.', '/') + ".class";
            byte[] classBytes = Files.readAllBytes(Paths.get(fileName));
            return defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Could not load class " + name, e);
        }
    }
}
public class HelloPlugin {
    public void sayHello() {
        System.out.println("Hello from dynamically loaded class!");
    }
}

public class LoaderTest {
    public static void main(String[] args) throws Exception {
        String className = "HelloPlugin";
        String classPath = "plugins"; 

        CustomClassLoader loader = new CustomClassLoader(classPath);
        Class<?> clazz = loader.loadClass(className);

        Object instance = clazz.getDeclaredConstructor().newInstance();

        Method method = clazz.getMethod("sayHello");
        method.invoke(instance);
    }
}

