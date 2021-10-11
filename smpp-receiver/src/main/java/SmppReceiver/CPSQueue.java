package SmppReceiver;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;



public class CPSQueue {
    private final static Logger LOGGER = LoggerFactory.getLogger(CPSQueue.class);
    private static MessageBuilder mb;

    private ConnectionFactory connectionFactory;
    private Channel channel;
    private Connection connection;
    private String quename;
    private String process_id;
    private AppConfiguration appConfiguration;


    public CPSQueue(String host, int port, String queuename, String process_id, AppConfiguration appConfiguration) {

        mb = new MessageBuilder(process_id);

        this.connectionFactory = new ConnectionFactory();
        this.connectionFactory.setHost(host);
        this.connectionFactory.setPort(port);
        this.connectionFactory.setUsername("admin");
        this.connectionFactory.setPassword("admin");
        this.quename = queuename;
        this.process_id = process_id;
        this.appConfiguration = appConfiguration;

        try {
            this.connection = this.connectionFactory.newConnection();
        } catch (IOException e) {
            LOGGER.error(mb.build("CPSQUEUE.EXCEPTION", e.getMessage()));
        } catch (TimeoutException e) {
            LOGGER.error(mb.build("CPSQUEUE.TIMEOUT",e.getMessage()));
        }
        try {
            this.channel = this.connection.createChannel();
            LOGGER.info(mb.build("CPSQUEUE.CONNECT",this.connectionFactory.getHost()+" "+ this.connectionFactory.getPort()));
        } catch (IOException e) {
            LOGGER.error(mb.build("CPSQUEUE.EXCEPTION",e.getMessage()));
        }

        try {
            this.channel.queueDeclare(queuename,false,false,false,null);

        } catch (IOException e) {
            LOGGER.error(mb.build("CPSQUEUE.EXCEPTION",e.getMessage()));
        }

    }

    public void publish(Payload payload) {
        String pl = payload.getJson();
        if(appConfiguration.isMask_sms() == true){
            //Payload payload2 = new Payload("*******", payload.getNumber(), payload.getReceiver_generated_id());
            LOGGER.info(mb.build("CPSQUEUE.PUBLISH",this.quename+" "+" "+payload.getNumber() +" "+ payload.receiver_generated_id) );
        }else{
            LOGGER.info(mb.build("CPSQUEUE.PUBLISH",this.quename+" "+pl) );
        }



        try {
            this.channel.basicPublish("", this.quename, null, pl.getBytes());
        } catch (IOException e) {
            LOGGER.error(mb.build("CPSQUEUE.EXCEPTION",e.getMessage()));
        }

    }
}
