package zookeeperInAction;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Date;

public class AdminClient implements Watcher {

    private ZooKeeper zooKeeper;
    private String hostPort;

    AdminClient(String hostPort){
        this.hostPort = hostPort;
    }

    public void start(){
        try {
            zooKeeper = new ZooKeeper(hostPort,15000,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listStat() throws KeeperException, InterruptedException {
        Stat stat = new Stat();
        byte[] data = zooKeeper.getData("/master", false, stat);
        Date startTime = new Date(stat.getCtime());
        System.out.println("Master: "+new String(data)+" since "+startTime);

        System.out.println("Works");
        for(String s:zooKeeper.getChildren("/works",false)){
            byte[] data1 = zooKeeper.getData("/works/" + s, false, null);
            String info = new String(data1);
            System.out.println("\t" + s + " :"+stat);
        }
    }
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println(watchedEvent);
    }

    public static void main(String[] args) {
        AdminClient c = new AdminClient("127.0.0.1:2182");
        c.start();
        try {
            c.listStat();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
