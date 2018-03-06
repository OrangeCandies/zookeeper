package demo;

public class Singleton {

    private Singleton(){
    }

    private static class SingletonHondler{
        private static final Singleton SINGLETON = new Singleton();
    }

    public Singleton getInstance(){
        return SingletonHondler.SINGLETON;
    }
}

class Single{
    private Single(){}
    private static volatile Single single;
    public Single getSingle(){
        if(single == null){
            synchronized (Single.class){
                if(single == null){
                    single = new Single();
                }
            }
        }

        return single;
    }
}
