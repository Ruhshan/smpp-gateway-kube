package SmppReceiver;

public class MessageBuilder {
    private String process_id;
    private String delim = "\t";


    public MessageBuilder(String process_id) {
        this.process_id = process_id;
    }

    public String build(String code, String message){
        return String.format("%s\t%s\t%s", this.process_id, code,message);
    }

}
