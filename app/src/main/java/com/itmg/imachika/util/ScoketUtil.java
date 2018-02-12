package com.itmg.imachika.util;

import android.util.Log;

import com.itmg.imachika.ui.LoginActivity;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;

/**
 * Created by lenovo on 2017/11/20.
 */

public class ScoketUtil {
    private static io.socket.client.Socket socket = null;
    public static Socket connect(String uid){
        try {
            socket = IO.socket(URLInfo.chat);
            final String cookie = "uid="+uid;
            Log.i("cookie",cookie);
            socket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Transport transport = (Transport)args[0];
                    transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Map<String, List<String>> headers = (Map<String, List<String>>)args[0];
                            // modify request headers
                            headers.put("Cookie", Arrays.asList(cookie));
                        }
                    });
                }
            });
            Log.e("cookie","准备连接");
            socket.connect();
            return socket;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
