package jp.toastkid.verification.rxjava;

import rx.Observable;

/**
 * FizzBuzz powered by RxJava.
 * @author Toast kid
 */
public class ObservablelImplementation {
    public static final void main(final String[] args) {
        final Observable<String> observable = makeObservable()
        .map(i -> {
            if (i % 15 == 0) {
                return "FizzBuzz";
            }
            if (i % 3  == 0) {
                return "Fizz";
            }
            if (i % 5  == 0) {
                return "Buzz";
            }
            return Integer.toString(i);
        })
        .map(i -> i + ", ");
        System.out.println("ぬるぽぬるぽぬるぽ");
        observable.subscribe(System.out::println);
    }
    private static Observable<Integer> makeObservable() {
        return Observable.create((sub) -> {
            for (int i = 1; i <= 100; i++) {
                sub.onNext(i);
            }
            sub.onCompleted();
        });
    }

}
