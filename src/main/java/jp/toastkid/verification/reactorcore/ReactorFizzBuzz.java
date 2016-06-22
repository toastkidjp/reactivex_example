package jp.toastkid.verification.reactorcore;

import reactor.core.publisher.Flux;

/**
 * FizzBuzz powered by Reactor-Core.
 * @author Toast kid
 */
public class ReactorFizzBuzz {

    public static final void main(final String[] args) {

        Flux.range(1, 100)
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
