package com.athena.init;

import com.athena.log.RecordLog;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Load registered init functions and execute in order.
 * @author mukong
 */
public final class InitExecutor {

    private static AtomicBoolean initialized = new AtomicBoolean(false);

    /**
     * If one {@link InitFunc} throws an exception, the init process
     * will immediately be interrupted and the application will exit.
     *
     * The initialization will be executed only once.
     */
    public static void doInit() {
        if (!initialized.compareAndSet(false, true)) {
            return;
        }
        try {

            ServiceLoader<InitFunc> loader = ServiceLoader.load(InitFunc.class);
            List<OrderWrapper> initList = new ArrayList<OrderWrapper>();
            for (InitFunc initFunc : loader) {
                RecordLog.info("[InitExecutor] Found init func: " + initFunc.getClass().getCanonicalName());
                insertSorted(initList, initFunc);
            }
            for (OrderWrapper w : initList) {
                w.func.init();
                RecordLog.info(String.format("[InitExecutor] Executing %s with order %d",
                    w.func.getClass().getCanonicalName(), w.order));
            }
        } catch (Exception ex) {
            RecordLog.warn("[InitExecutor] WARN: Initialization failed", ex);
            ex.printStackTrace();
        } catch (Error error) {
            RecordLog.warn("[InitExecutor] ERROR: Initialization failed with fatal error", error);
            error.printStackTrace();
        }
    }

    private static void insertSorted(List<OrderWrapper> list, InitFunc func) {
        int order = resolveOrder(func);
        int idx = 0;
        for (; idx < list.size(); idx++) {
            if (list.get(idx).getOrder() > order) {
                break;
            }
        }
        list.add(idx, new OrderWrapper(order, func));
    }

    private static int resolveOrder(InitFunc func) {
        if (!func.getClass().isAnnotationPresent(InitOrder.class)) {
            return InitOrder.LOWEST_PRECEDENCE;
        } else {
            return func.getClass().getAnnotation(InitOrder.class).value();
        }
    }

    private InitExecutor() {}

    private static class OrderWrapper {
        private final int order;
        private final InitFunc func;

        OrderWrapper(int order, InitFunc func) {
            this.order = order;
            this.func = func;
        }

        int getOrder() {
            return order;
        }

        InitFunc getFunc() {
            return func;
        }
    }
}
