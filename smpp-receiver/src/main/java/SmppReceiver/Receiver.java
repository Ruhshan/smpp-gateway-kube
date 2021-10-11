package SmppReceiver;

import java.text.SimpleDateFormat;
import java.util.Date;


import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Receiver {
    /**
     * This class is the entrypoint of smpp receiver service. It receives smpp request from cps then saves the message
     * to Rabbitmq.
     */

    private static CPSQueue masterqueue;
    private static MessageBuilder mb;
    private static MessageListener messageReceivedListener;




    /**
     * This is the main method for Receiver class. This takes following parameters from stdin
     *
     */


    public static void main(String[] args) {

        System.setProperty("LOG_DIR","/tmp/logs");
        AppConfiguration appconf = new AppConfiguration(args[0]);

        String quehost = appconf.getRabbit_host();
        System.out.println(quehost);
        int queport = appconf.getRabbit_port();
        String queuename = appconf.getQuename();


        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        ContextInitializer ci = new ContextInitializer(lc);
        lc.reset();
        System.setProperty("LOG_DIR",appconf.getLogpath());
        try {
            //I prefer autoConfig() over JoranConfigurator.doConfigure() so I wouldn't need to find the file myself.
            ci.autoConfig();
        } catch (JoranException e) {
            // StatusPrinter will try to log this
            e.printStackTrace();
        }

        String process_id = "SPR_"+args[1];

        masterqueue = new CPSQueue(quehost, queport, queuename, process_id, appconf);
        mb = new MessageBuilder(process_id);
        messageReceivedListener = new MessageListener(process_id, masterqueue, appconf);

        SessionHandler sessionHandler = new SessionHandler(appconf,messageReceivedListener, process_id);


    }

}
