package io.lolyay.customevents;

import net.dv8tion.jda.api.events.GenericEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {

    private final Map<Class<?>, List<RegisteredListener>> listeners = new ConcurrentHashMap<>();

    public void register(Object listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventListener.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1) {
                    Class<?> eventType = parameterTypes[0];
                    method.setAccessible(true);
                    listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(new RegisteredListener(listener, method));
                }
            }
        }
    }

    public void unregister(Object listener) {
        listeners.values().forEach(list -> list.removeIf(reg -> reg.listener.equals(listener)));
    }

    public boolean post(Event event) {
        List<RegisteredListener> registered = listeners.get(event.getClass());
        if (registered != null) {
            for (RegisteredListener registration : registered) {
                try {
                    registration.method.invoke(registration.listener, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        if (event instanceof CancellableEvent) {
            return !((CancellableEvent) event).isCancelled();
        }

        return true;
    }

    public void postJda(GenericEvent event) {
        for (Map.Entry<Class<?>, List<RegisteredListener>> entry : listeners.entrySet()) {
            if (entry.getKey().isAssignableFrom(event.getClass())) {
                for (RegisteredListener registration : entry.getValue()) {
                    try {
                        registration.method.invoke(registration.listener, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static class RegisteredListener {
        final Object listener;
        final Method method;

        RegisteredListener(Object listener, Method method) {
            this.listener = listener;
            this.method = method;
        }
    }
}
