package zookeeperInAction;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Random;

public class Master implements Watcher {

    private ZooKeeper zk;
    private String hostPort;


    AsyncCallback.StringCallback sb = new AsyncCallback.StringCallback() {
        @Override
        public void processResult(int i, String s, Object o, String s1) {
            switch (KeeperException.Code.get(i)){
                case CONNECTIONLOSS:
                    createParent(s, (byte[]) o);
                    break;
                case OK:
                    break;
                case NODEEXISTS:
                    System.out.println("node already exist");
                    break;
                    default:
                        System.out.println("Something wrong is here");


            }
        }
    };
    Master(String hostPort){
        this.hostPort = hostPort;
    }


    private void createParent(String path,byte[] data){
        zk.create(path,data,ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT,sb,data);
    }
    public void startZk(){
        try {
            Stat stat = new Stat();
            zk = new ZooKeeper(hostPort,15000,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runWithZk(){
        Random random = new Random();
        String serID = Integer.toHexString(random.nextInt());
        try {
            zk.create("/master", serID.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void process(WatchedEvent watchedEvent) {

        if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
            runWithZk();
        }
        System.out.println(watchedEvent);
    }

    public static void main(String[] args) {
        Master m = new Master("127.0.0.1:2181");
        m.startZk();

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
