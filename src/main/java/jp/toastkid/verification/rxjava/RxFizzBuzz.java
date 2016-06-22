package jp.toastkid.verification.rxjava;

import rx.Observable;

/**
 * FizzBuzz powered by RxJava.
 * @author Toast kid
 */
public class RxFizzBuzz {

    public static final void main(final String[] args) {

        Observable.range(1, 100)
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
           .subscribe(
                   (i) -> System.out.print(i + ", "),
                   (e) -> e.printStackTrace(),
                   System.out::println
                   );
    }
}
