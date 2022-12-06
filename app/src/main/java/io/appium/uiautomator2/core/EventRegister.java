package io.appium.uiautomator2.core;


import android.app.UiAutomation;
import android.view.accessibility.AccessibilityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import io.appium.uiautomator2.model.AccessibilityScrollData;
import io.appium.uiautomator2.model.AppiumUIA2Driver;
import io.appium.uiautomator2.model.NotificationListener;
import io.appium.uiautomator2.model.Session;
import io.appium.uiautomator2.utils.Logger;

public abstract class EventRegister {

    public static final int EVENT_COOLDOWN_MS = 750;
    private static final String EVENT_COOLDOWN_CAP = "scrollEventTimeout";

    public static Boolean runAndRegisterScrollEvents(ReturningRunnable<Boolean> runnable, long timeout) {
        // turn off listening to notifications since it interferes with us listening for the scroll
        // event here
        NotificationListener listener = NotificationListener.getInstance();

        boolean notificationListenerActive = listener.isListening();
        if (notificationListenerActive) {
            listener.stop();
        }

        try {
            // here we set a callback for the accessibility event stream, keeping track of any scroll
            // events we come across
            AccessibilityEvent event = null;
            ArrayList<AccessibilityEvent> events = new ArrayList<>();
            UiAutomation.AccessibilityEventFilter filter = new EventCollectingPredicate(AccessibilityEvent.TYPE_VIEW_SCROLLED, events);
            UiAutomation automation = UiAutomatorBridge.getInstance().getUiAutomation();
            try {
                automation.executeAndWaitForEvent(runnable, filter, timeout);
            } catch (TimeoutException ign) {
                // ignore
            }

            // if we have caught any events in our net, snatch the last one
            if (events.size() > 0) {
                event = events.get(events.size() - 1);
            }

            Session session = AppiumUIA2Driver.getInstance().getSessionOrThrow();

            if (event == null) {
                Logger.debug("Did not retrieve accessibility event for scroll");
                session.setLastScrollData(null);
            } else {
                AccessibilityScrollData data = new AccessibilityScrollData(event);
                Logger.debug("Retrieved accessibility event for scroll: ", data);
                session.setLastScrollData(data);
            }

            // ensure we recycle all accessibility events once we no longer need them
            for (AccessibilityEvent eventToRecycle : events) {
                eventToRecycle.recycle();
            }

        } finally {
            // turn back on notification listener if it was active
            if (notificationListenerActive) {
                listener.start();
            }
        }

        // finally, return whatever the runnable set as its result
        return runnable.getResult();
    }

    public static Boolean runAndRegisterScrollEvents(ReturningRunnable<Boolean> runnable) {
        Session session = AppiumUIA2Driver.getInstance().getSessionOrThrow();
        int timeout = session.getCapability(EVENT_COOLDOWN_CAP, EVENT_COOLDOWN_MS);

        return runAndRegisterScrollEvents(runnable, timeout);
    }

    // https://android.googlesource.com/platform/frameworks/testing/+/master/uiautomator/library/core-src/com/android/uiautomator/core/InteractionController.java#96
    static class EventCollectingPredicate implements UiAutomation.AccessibilityEventFilter {
        private final int mMask;
        private final List<AccessibilityEvent> mEventsList;

        EventCollectingPredicate(int mask, List<AccessibilityEvent> events) {
            mMask = mask;
            mEventsList = events;
        }

        @Override
        public boolean accept(AccessibilityEvent t) {
            // check current event in the list
            if ((t.getEventType() & mMask) != 0) {
                // For the events you need, always store a copy when returning false from
                // predicates since the original will automatically be recycled after the call.
                mEventsList.add(AccessibilityEvent.obtain(t));
            }
            // get more
            return false;
        }
    }
}
