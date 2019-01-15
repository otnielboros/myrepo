package yubackend.controller;

import rx.Observable;
import yubackend.model.NotifyObject;

public class MyObservable extends Observable<NotifyObject> {
    protected MyObservable(OnSubscribe<NotifyObject> f) {
        super(f);
    }
}
