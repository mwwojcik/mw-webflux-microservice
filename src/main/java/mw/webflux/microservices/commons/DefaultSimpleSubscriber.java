package mw.webflux.microservices.commons;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class DefaultSimpleSubscriber implements Subscriber<Object> {

    private String name="" ;

    public DefaultSimpleSubscriber() {
    }

    public DefaultSimpleSubscriber(String name) {
        this.name ="=> "+ name;
    }

    public static DefaultSimpleSubscriber create() {
       return new DefaultSimpleSubscriber(DefaultSimpleSubscriber.class.getSimpleName());
    }

    public static DefaultSimpleSubscriber create(String name) {
       return new DefaultSimpleSubscriber(name);
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Object o) {
        System.out.println(String.format("%s => %s Received : %s",Thread.currentThread().getName(),name,o));
    }

    @Override
    public void onError(Throwable t) {
        System.out.println(String.format("%s => %s Error : %s",Thread.currentThread().getName(),name, t));
    }

    @Override
    public void onComplete() {
        System.out.println(String.format("%s => %s Completed",Thread.currentThread().getName(), name));
    }
}
