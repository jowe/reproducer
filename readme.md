# Reproducer for getAllTimers Issue in Payara

## Description
```javax.ejb.TimerService#getAllTimers``` throws NPE or NoSuchObjectLocalException: timer no longer exists

Tested against 
 * payara412.191-p03-00 (fail)
 * payara412.191-p04-00 (fail)
 * payara412.183-p00-00 (OK)
 
Call http://localhost:8080/reproducer/rest/timer to trigger getAllTimers
 
### Contact
joachim.wenzel@capgemini.com 


