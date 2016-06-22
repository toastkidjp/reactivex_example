package jp.toastkid.verification.reactorcore;

import reactor.core.publisher.Flux;

/**
 * FizzBuzz powered by Reactor-Core.
 *
 * @author Toast kid
 */
public class FluxImplementation {

    /**
     * main method.
     * @param args
     */
    public static final void main(final String[] args) {
        final Flux<String> observable = makeFlux()
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

    /**
     * Make and return simple ranged Flux.
     * @return Flux object
     */
    private static Flux<Integer> makeFlux() {
        return Flux.create((sub) -> {
            for (int i = 1; i <= 100; i++) {
                sub.next(i);
            }
            sub.complete();
        });
    }

}
