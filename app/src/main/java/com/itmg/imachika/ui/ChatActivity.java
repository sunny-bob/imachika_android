package com.itmg.imachika.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itmg.imachika.R;
import com.itmg.imachika.adapter.ChatAdapter;
import com.itmg.imachika.application.APP;
import com.itmg.imachika.contans.Contans;
import com.itmg.imachika.model.ChatMessage;
import com.itmg.imachika.model.NearPerson;
import com.itmg.imachika.util.GsonUtil;
import com.itmg.imachika.util.Mytoast;
import com.itmg.imachika.util.URLInfo;

import org.java_websocket.WebSocketFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import io.socket.engineio.client.transports.WebSocket;

public class ChatActivity extends Activity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.listView)
    ListView listView;
    Intent intent;
    String targId = "longren",msg;
    NearPerson.Data data;
    @BindView(R.id.et_msg)
    EditText etMsg;
    ChatAdapter adapter;
    List<ChatMessage> list = new ArrayList<>();
    SharedPreferences sp;
    String uid;
    Socket socket;
    WebSocketClient client;
    URI uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        intent = getIntent();
        data = (NearPerson.Data) intent.getSerializableExtra("data");
        tvTitle.setText(data.getUser_name());
        sp = getSharedPreferences("login",MODE_PRIVATE);
        uid = sp.getString("id","");
//        initWebSocket();

//        send();
      /*  list.add(new ChatMessage("fddsjglkfjhgfhl",false));
        list.add(new ChatMessage("dsfdksgjlfhglf",true));
        list.add(new ChatMessage("asssdgrfhreeddsg ",true));
        list.add(new ChatMessage("的考试了都看过豆腐干恢复的快乐就好东方航空个地方环境规划的房间看更好地发挥刚看到看和发动机开关个",false));*/
        adapter = new ChatAdapter(this,list);
        listView.setAdapter(adapter);
//        targId = data.get_id();
    }
    //初始化webSocket
/*    private void initWebSocket() {
        try {
            uri = new URI(URLInfo.webSocket);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Draft_17 draft = new Draft_17();
        String cookie = "uid="+uid;
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Cookie", cookie);
        client = new WebSocketClient(uri,draft,hashMap,5000) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i("msg","123213421");
            }

            @Override
            public void onMessage(String message) {
                Log.i("msg",message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i("close",reason);
            }

            @Override
            public void onError(Exception ex) {
                Log.i("error",ex.toString());
            }
        };
        client.connect();
    }*/
    @Override
    protected void onResume() {
        super.onResume();
        connectSocket();
        //使用 onNewMessage 来监听服务器发来的 "new message" 事件
    }

    private void connectSocket() {
        socket = APP.getMyApplication().getSocket();
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
//        socket.on("c_connected",connect);
        socket.on(Socket.EVENT_CONNECT,connect);
        socket.on(Socket.EVENT_CONNECTING,connecting);
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError );
        socket.connect();
        if (socket.connected()){
            Mytoast.show(getApplicationContext(),"连接成功");
        }else{
            Mytoast.show(getApplicationContext(),"连接失败");
        }
    }

    Emitter.Listener connect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            JSONObject jsonObject = (JSONObject) args[0];
            String s = GsonUtil.objectToJson(args[0]);
            Log.i("connect",s);
        }
    };
    Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            String s = GsonUtil.objectToJson(args[0]);
            Log.i("connecting","error");
        }
    };
    Emitter.Listener connecting = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            String s = GsonUtil.objectToJson(args[0]);
            Log.i("connecting","cfdsgfd");
        }
    };
    @OnClick({R.id.img_back, R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_send:
                msg = etMsg.getText().toString();
                if (!msg.isEmpty()){
                    send();
                }else{
                    Mytoast.show(getApplicationContext(),"发送的消息不能为空");
                }
                break;
        }
    }
    //发送消息的方法
    private void send() {
        socket.send("s_realtime-msg=wqewqwrewr");
        socket.emit("s_realtime-msg", "dsadfdsgfd", new Ack() {
            @Override
            public void call(Object... args) {
                Log.i("return",args.toString());
            }
        });
        etMsg.setText("");
//        ScoketUtil.getInstance(uid).sendMsg(msg);
       /* ChatMessage chatMessage = new ChatMessage();
        chatMessage.setData(msg);
        chatMessage.setFromu("123");
        chatMessage.setFromuid("123");
        chatMessage.setTouid("longren");
        String json = GsonUtil.objectToJson(chatMessage);
        ScoketUtil.sendMsg(json);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket!=null)
        socket.disconnect();
    }
}
