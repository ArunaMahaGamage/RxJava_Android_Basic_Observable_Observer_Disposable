package com.yamuko.rxjava2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Disposable disposable;

    TextView tv_rx;
    String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_rx = (TextView) findViewById(R.id.tv_rx);

        // observable
        Observable<String> animalsObservable = getAnimalsObservable();

        // observer
        Observer<String> animalsObserver = getAnimalsObserver();

        // observer subscribing to observable
        animalsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(animalsObserver);
    }

    private Observer<String> getAnimalsObserver() {
        return new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
                disposable = d;

                text += "onSubscribe\n";
                tv_rx.setText(text);
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "Name: " + s);

                text += "Name : " + s + "\n";
                tv_rx.setText(text);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());

                text += "onError : " + e.getMessage() + "\n";
                tv_rx.setText(text);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "All items are emitted!");

                text += "All items are emitted!";
                tv_rx.setText(text);
            }
        };
    }

    private Observable<String> getAnimalsObservable() {
        return Observable.just("Ant", "Bee", "Cat", "Dog", "Fox");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // don't send events once the activity is destroyed
        disposable.dispose();

        Toast.makeText(getBaseContext(),"disposable",
                Toast.LENGTH_SHORT).show();
    }
}
