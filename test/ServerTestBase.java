import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rlcp.server.processor.Processor;
import rlcp.server.processor.calculate.CalculateProcessor;
import rlcp.server.processor.check.CheckProcessor;
import rlcp.server.processor.factory.DefaultConstructorProcessorFactory;
import rlcp.server.processor.generate.GenerateProcessor;

import javax.annotation.Nonnull;

public abstract class ServerTestBase {
    private static final String APPLICATION_CONXTEXT_CONFIG_PATH = "test-java-server-config.xml";

    static CheckProcessor checkProcessor;
    static CalculateProcessor calculateProcessor;
    static GenerateProcessor generateProcessor;

    //ugly but optimal way to inject needed beans for tests on startup (only once)
    static {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(APPLICATION_CONXTEXT_CONFIG_PATH);

            checkProcessor = getProcessor(context, "checkProcessor");
            calculateProcessor = getProcessor(context, "calculateProcessor");
            generateProcessor = getProcessor(context, "generateProcessor");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T extends Processor> T getProcessor(@Nonnull ApplicationContext context,
                                                        @Nonnull String beanName) {
        //noinspection unchecked
        DefaultConstructorProcessorFactory<T> processorFactoryBean = (DefaultConstructorProcessorFactory<T>) context.getBean(beanName);
        return processorFactoryBean.getInstance();
    }
}
