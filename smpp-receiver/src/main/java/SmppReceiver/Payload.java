package SmppReceiver;

import com.google.gson.Gson;

public class Payload {
    String message;
    String number;
    String receiver_generated_id;


    public Payload(String message, String number, String receiver_generated_id) {
        this.message = message;
        this.number = number;
        this.receiver_generated_id = receiver_generated_id;
    }

    public String getJson(){
        Gson gson = new Gson();
        return  gson.toJson(this);

    }

    public String getNumber() {
        return number;
    }

    public String getReceiver_generated_id() {
        return receiver_generated_id;
    }
}
