package payara.reproducer;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.Serializable;
import java.util.Collection;
import java.util.logging.Logger;

@Path("timer")
@Stateless
public class TimerUsage {

    private final static Logger LOG = Logger.getLogger(TimerUsage.class.getName());

    @Resource
    private TimerService timerService;

    @GET
    public String getAllTimers() {
        final Collection<Timer> allTimers = timerService.getAllTimers();
        for (final Timer timer : allTimers) {
            LOG.info("before get Info");
            final Serializable info = timer.getInfo();
            LOG.info("after get Info");
        }
        return "OK";
    }


    /**
     If only this schedule is defined call of getAllTimers leads to:
     (https://github.com/payara/Payara/issues/3526 seems not to be fixed)

     Caused by: java.lang.NullPointerException
     at com.sun.ejb.containers.TimerWrapper.getInfo(TimerWrapper.java:139)
     at payara.reproducer.TimerUsage.getAllTimers(TimerUsage.java:27)
     at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
     at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
     at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
     at java.lang.reflect.Method.invoke(Method.java:497)

     */
    @Schedule(minute = "*", hour = "*", persistent = false, info = "mySchedule")
    public void mySchedule() {
        LOG.info("mySchedule called");
    }

    /**
     If we additionally define a persitent timer call to getAllTimers leads to:
     (see already existing new bug: https://github.com/payara/Payara/issues/3884)

     Caused by: javax.ejb.NoSuchObjectLocalException: timer no longer exists
     at com.sun.ejb.containers.TimerWrapper.getInfo(TimerWrapper.java:141)
     at payara.reproducer.TimerUsage.getAllTimers(TimerUsage.java:27)
     at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
     at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
     at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)

     */

    @Schedule(minute = "*", hour = "*", persistent = true, info = "my2nSchedule")
    public void my2nSchedule() {
        LOG.info("my2nSchedule called");
    }
}
