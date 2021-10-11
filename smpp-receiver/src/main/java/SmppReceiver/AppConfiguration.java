package SmppReceiver;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.bean.NumberingPlanIndicator;

public class AppConfiguration {

    private int system_port;
    private String system_id;
    private String system_password;
    private String rabbit_host;
    private int rabbit_port;
    private String quename;
    private String logpath;
    private boolean mask_sms;




    public AppConfiguration(String path) {
        /**
         * Sample Configuration
         * {
         * "system_port":8059,
         * "system_id":"test",
         * "system_password":"test",
         * "rabbit_host":"localhost",
         * "rabbit_port":5672,
         * "queuename":"cps",
         * "logpath":"/home/abir/logs/smpp_receiver.log"
         * }
         */


        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JsonObject obj = new JsonParser().parse(reader).getAsJsonObject();

        this.system_port = obj.get("system_port").getAsInt();
        this.system_id = obj.get("system_id").getAsString();
        this.system_password = obj.get("system_password").getAsString();
        this.rabbit_host = obj.get("rabbit_host").getAsString();
        this.rabbit_port = obj.get("rabbit_port").getAsInt();
        this.quename = obj.get("queuename").getAsString();
        this.logpath = obj.get("logpath").getAsString();
        this.mask_sms = obj.get("sms_masking").getAsBoolean();

    }

    public int getSystem_port() {
        return system_port;
    }

    public String getSystem_id() {
        return system_id;
    }

    public String getSystem_password() {
        return system_password;
    }

    public String getRabbit_host() {
        return rabbit_host;
    }

    public int getRabbit_port() {
        return rabbit_port;
    }

    public String getQuename() {
        return quename;
    }

    public String getLogpath() {
        return logpath;
    }

    public boolean isMask_sms() {
        return mask_sms;
    }
}

