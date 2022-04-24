import org.springframework.context.support.ClassPathXmlApplicationContext;
import rlcp.server.processor.calculate.CalculateProcessor;
import rlcp.server.processor.check.CheckProcessor;
import rlcp.server.processor.factory.DefaultConstructorProcessorFactory;
import rlcp.server.processor.generate.GenerateProcessor;

public abstract class ServerTestBase {
    public static final String APPLICATION_CONFIG_PATH = "test-java-server-config.xml";

    static CheckProcessor checkProcessor;
    static CalculateProcessor calculateProcessor;
    static GenerateProcessor generateProcessor;

    //ugly but optimal way to inject needed beans on startup (only once)
    static {
        try (ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(APPLICATION_CONFIG_PATH)) {
            checkProcessor = ((DefaultConstructorProcessorFactory<CheckProcessor>) context.getBean("checkProcessor")).getInstance();
            calculateProcessor = ((DefaultConstructorProcessorFactory<CalculateProcessor>) context.getBean("calculateProcessor")).getInstance();
            generateProcessor = ((DefaultConstructorProcessorFactory<GenerateProcessor>) context.getBean("generateProcessor")).getInstance();
        }
    }
}
