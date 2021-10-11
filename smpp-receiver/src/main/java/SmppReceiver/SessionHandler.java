package SmppReceiver;


import org.jsmpp.PDUStringException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SessionHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(SessionHandler.class);
    private AppConfiguration appConfiguration;
    private MessageListener messageListener;
    private String process_id;
    private final String system_id="test";
    private final String system_pasword="test";
    private MessageBuilder mb;


    public SessionHandler(AppConfiguration appConfiguration, MessageListener messageListener, String process_id) {
        this.appConfiguration = appConfiguration;
        this.messageListener = messageListener;
        this.process_id = process_id;
        this.mb = new MessageBuilder(process_id);

        connect();
    }

    private void connect(){
        try {
            SMPPServerSessionListener sessionListener = new SMPPServerSessionListener(appConfiguration.getSystem_port());

            sessionListener.setSessionStateListener(new SessionStateListener() {
                @Override
                public void onStateChange(SessionState newState, SessionState oldState, Session source) {

                    if(newState.equals(SessionState.CLOSED)){
                        System.exit(0);
                    };
                }
            });

            LOGGER.info(mb.build("RECEIVER.LISTENING", Integer.toString(appConfiguration.getSystem_port())));
            sessionListener.setMessageReceiverListener(messageListener.getListener());
            SMPPServerSession session = sessionListener.accept();
            BindRequest request = session.waitForBind(5000);

            if (request.getSystemId().equals(system_id) && request.getPassword().equals(system_pasword)){

                try {
                    request.accept(system_id, request.getInterfaceVersion());
                } catch (PDUStringException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LOGGER.info(mb.build("RECEIVER.BIND.ACCEPTED", request.getSystemId() + " " + request.getSystemType()));

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
            } else {
                LOGGER.error(mb.build("RECEIVER.BIND.REJECTED", request.getSystemId() + " " + request.getSystemType()));
                //request.reject(SMPPConstant.STAT_ESME_RINVPASWD);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
