
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.api.listener.MessageListener;
import org.redisson.config.Config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class App
{
    static RedissonClient redissonClient;
    static String channel = new String();
    static String username=new String();
    static RTopic subTopic;
    static RTopic pubTopic;
    static Scanner scanner;
    static RListMultimapCache<String,ChatObject> map;

    public static void main( String[] args ) {
        inputInfo();

        connectServer();

        printAllMessageCached();

        sub();

        pub();

        chatAndPubToChannel();
    }

    private static void inputInfo() {
        System.out.println("Enter your channel:");
        Scanner scanner = new Scanner(System.in);
        channel = scanner.nextLine();
        System.out.println("Enter your name:");
        username=scanner.nextLine();
    }

    private static void connectServer() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379");
        redissonClient = Redisson.create(config);
    }

    private static void printAllMessageCached() {
        map = redissonClient.getListMultimapCache(channel);

        Set<String> keys = map.readAllKeySet();
        for (String key : keys) {
            List<ChatObject> values = map.getAll(key);
            for (ChatObject value : values) {
                System.out.println(value.toString());

            }
        }
    }

    static void sub(){
        subTopic = redissonClient.getTopic(channel);
        subTopic.addListener(ChatObject.class, new MessageListener<ChatObject>() {

            public void onMessage(CharSequence charSequence, ChatObject chatObject) {
                System.out.println(chatObject.toString());

            }
        });
    }

    static void pub(){
        pubTopic = redissonClient.getTopic(channel);

        ChatObject chatObject=new ChatObject(channel,username, "joined the channel!", getTime());

        pubTopic.publish(chatObject);
        putMessageToCache(channel,chatObject);
    }

    private static void chatAndPubToChannel() {
        while (true) {
            scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            if (message.equals(":wq")) {
                System.out.println("You have left the channel! Bye bye my babe!!!");
                pub("has left the channel!!!");
                System.exit(0);
            }
            else
                pub(message);
        }
    }


    public static String getTime(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    static void pub(String message){
        System.out.println("--------------------");
        System.out.println("If you want to quit, type: ':wq'\nPUB channel message: ");

        pubTopic = redissonClient.getTopic(channel);
        pubTopic.publish(new ChatObject(channel,username,message, getTime()));
        putMessageToCache(channel,new ChatObject(channel,username,message, getTime()));
    }

    public static void putMessageToCache(String channel, ChatObject chatObject){
        map.put(channel,chatObject);
        map.expireKey(channel,1, TimeUnit.DAYS);
    }
}

//https://github.com/redisson/redisson/wiki/6.-Distributed-objects