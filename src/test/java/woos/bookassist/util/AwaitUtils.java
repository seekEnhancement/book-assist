package woos.bookassist.util;

import org.awaitility.core.ThrowingRunnable;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public abstract class AwaitUtils {
    public static void awaitAndCheck(int pollDelay, int atMost, ThrowingRunnable runnable) {
        await()
                .pollDelay(pollDelay, TimeUnit.MILLISECONDS)
                .atMost(atMost, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> runnable.run());
    }
}
