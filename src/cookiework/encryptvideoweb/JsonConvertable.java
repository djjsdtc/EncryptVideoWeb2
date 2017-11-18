package cookiework.encryptvideoweb;

import com.google.gson.JsonElement;

/**
 * Created by Administrator on 2017/01/13.
 */
public interface JsonConvertable {
    String toString();
    JsonElement toJsonElement();
}
