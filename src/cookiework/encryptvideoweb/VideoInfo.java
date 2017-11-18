package cookiework.encryptvideoweb;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Created by Administrator on 2017/01/12.
 */
public class VideoInfo implements JsonConvertable{
    private int id;
    private String cipherTitle;
    private String cipherIntro;
    private String cipherAddr;
    private String status;
    private String encKey;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEncKey() {
        return encKey;
    }

    public void setEncKey(String encKey) {
        this.encKey = encKey;
    }

    public String getCipherTitle() {
        return cipherTitle;
    }

    public void setCipherTitle(String cipherTitle) {
        this.cipherTitle = cipherTitle;
    }

    public String getCipherIntro() {
        return cipherIntro;
    }

    public void setCipherIntro(String cipherIntro) {
        this.cipherIntro = cipherIntro;
    }

    public String getCipherAddr() {
        return cipherAddr;
    }

    public void setCipherAddr(String cipherAddr) {
        this.cipherAddr = cipherAddr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public JsonElement toJsonElement() {
        Gson gson = new Gson();
        return gson.toJsonTree(this);
    }
}
