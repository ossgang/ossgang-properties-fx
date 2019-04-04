package io.github.ossgang.properties.fx.util;

import javafx.application.Platform;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public final class JavaFxFlux {

    private static final Scheduler FX_THREAD = Schedulers.fromExecutor(Platform::runLater);

    public static Scheduler fxScheduler() {
        return FX_THREAD;
    }

    private JavaFxFlux() {
        /* static things */
    }
}
