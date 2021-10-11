package SmppReceiver;


import org.jsmpp.bean.*;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.*;
import org.jsmpp.util.MessageIDGenerator;
import org.jsmpp.util.MessageId;
import org.jsmpp.util.RandomMessageIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);
    private static MessageBuilder mb;
    private CPSQueue masterqueue;
    private AppConfiguration appConfiguration;


    public MessageListener(String process_id, CPSQueue masterqueue, AppConfiguration appConfiguration) {
        this.mb = new MessageBuilder(process_id);
        this.masterqueue = masterqueue;
        this.appConfiguration = appConfiguration;
    }

    public ServerMessageReceiverListener getListener() {
        final MessageIDGenerator messageIdGenerator = new RandomMessageIDGenerator();

        ServerMessageReceiverListener srml = new ServerMessageReceiverListener() {
            public MessageId onAcceptSubmitSm(SubmitSm submitSm, SMPPServerSession source) throws
                    ProcessRequestException {


                String received_message = new String(submitSm.getShortMessage());

                if (appConfiguration.isMask_sms() == true) {
                    LOGGER.info(mb.build("MESSAGELISTENER.RECEIVED", submitSm.getDestAddress() + " <" + " ********* " + ">"));

                } else {
                    LOGGER.info(mb.build("MESSAGELISTENER.RECEIVED", submitSm.getDestAddress() + " <" + received_message + ">"));

                }


                MessageId receiver_generated_id = messageIdGenerator.newMessageId();


                Payload payload = new Payload(received_message, submitSm.getDestAddress(), receiver_generated_id.toString());

                masterqueue.publish(payload);

                // need message_id to response submit_sm
                return receiver_generated_id;
            }

            public QuerySmResult onAcceptQuerySm(QuerySm querySm,
                                                 SMPPServerSession source)
                    throws ProcessRequestException {
                return null;
            }

            public SubmitMultiResult onAcceptSubmitMulti(
                    SubmitMulti submitMulti, SMPPServerSession source)
                    throws ProcessRequestException {
                return null;
            }

            public DataSmResult onAcceptDataSm(DataSm dataSm, Session source)
                    throws ProcessRequestException {
                return null;
            }

            public void onAcceptCancelSm(CancelSm cancelSm,
                                         SMPPServerSession source)
                    throws ProcessRequestException {
            }

            public void onAcceptReplaceSm(ReplaceSm replaceSm,
                                          SMPPServerSession source)
                    throws ProcessRequestException {
            }
        };
        return srml;
    }

}
