package demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DistributedLock implements Watcher {
    private int threadId;
    private ZooKeeper zk;
    private String selfPath;
    private String waitPath;

    private static final int SESSION_TIMEOUT = 10000;
    private static final String GROUP_PATH  = "/disLocks";
    private static final String SUB_PATH  = "/disLocks/sub";
    private static final String CONNECTION_STRING  = "127.0.0.1:2181";

    private static final int THREAD_NUM = 10;

    private static final CountDownLatch beginSemaphore = new CountDownLatch(THREAD_NUM);
    private CountDownLatch connection = new CountDownLatch(1);

    public DistributedLock(int threadId) {
        this.threadId = threadId;
    }

    public static void main(String[] args) {
        for(int i=0; i<THREAD_NUM;i++){
            final int id = i+1;
            new Thread(()->{
                try {
                    DistributedLock lock = new DistributedLock(id);
                    lock.createConnection(CONNECTION_STRING,SESSION_TIMEOUT);
                    synchronized (beginSemaphore){
                        try {
                            lock.createPath(GROUP_PATH,"该节点由"+lock.threadId+" 创建",true);
                        } catch (KeeperException e) {
                            e.printStackTrace();
                        }
                    }
                    lock.getLock();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (KeeperException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        try {
            beginSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public boolean createPath(String path,String data,boolean needWatcher) throws KeeperException, InterruptedException {
        if(zk.exists(path,needWatcher) == null){
            String s = zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println(threadId+" 节点创建成功，path = "+s);
        }
        return true;
    }
    public void createConnection(String connectionPath, int timeout) throws IOException, InterruptedException {
        zk = new ZooKeeper(connectionPath,timeout,this);
        connection.await();
    }
    public void getLock() throws KeeperException, InterruptedException {
        selfPath = zk.create(SUB_PATH,null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(selfPath+" 创建成功");
        if(checkMinPath()){
            getLockSuccess();
        }

    }

    private void getLockSuccess() throws KeeperException, InterruptedException {
        if(zk.exists(this.selfPath,false) == null){
            return;
        }
        System.out.println(threadId+" 获取锁成功 ");
        Thread.sleep(2000);
        System.out.println(threadId+" 释放锁 ");
        zk.delete(selfPath,-1);
        releaseConnection();
        beginSemaphore.countDown();;
    }

    private void releaseConnection() {
        if(zk != null){
            try {
                zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(selfPath+" 释放成功 ");
    }

    public boolean checkMinPath() throws KeeperException, InterruptedException {
        List<String> children = zk.getChildren(GROUP_PATH, false);
        Collections.sort(children);
        int index = children.indexOf(selfPath.substring(GROUP_PATH.length()+1));
        switch (index){
            case -1 :{
                System.out.println("本节点已不在"+selfPath);
                return false;
            }
            case 0:{
                System.out.println(" 本节点最大"+selfPath);
                return true;
            }
            default:{
                this.waitPath = GROUP_PATH+"/"+children.get(index-1);
                System.out.println(waitPath+" 排在我前面 "+selfPath);
                try {
                    zk.getData(waitPath,true,new Stat());
                }catch (KeeperException e){
                    if(zk.exists(waitPath,false) == null){
                        System.out.println(" 前级节点已经删除");
                        return checkMinPath();
                    }else{
                        throw e;
                    }
                }
                return false;
            }
        }
    }
    public void process(WatchedEvent watchedEvent) {
        if(watchedEvent == null){
            return;
        }
        Event.KeeperState state = watchedEvent.getState();
        Event.EventType type = watchedEvent.getType();
        if(Event.KeeperState.SyncConnected == state){
            if(Event.EventType.None == type){
                System.out.println(threadId+" 连接成功 ");
                connection.countDown();
            }else if(type  == Event.EventType.NodeDeleted&&watchedEvent.getPath().equals(waitPath)){
                System.out.println(" 前级节点已经退出 ");
                try {
                    if(checkMinPath()){
                        getLockSuccess();
                    }
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }else if(state == Event.KeeperState.Disconnected){
            System.out.println(threadId+" 断开链接");
        }
    }
}
