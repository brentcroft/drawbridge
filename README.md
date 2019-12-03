# drawbridge
A shutter plugs into a service and takes over the service's clients and suppliers:

 - When a supplier fails a client request the shutter is notified.
 - The shutter checks the availability of any notified suppliers and may go into recovery, closing the clients, to avoid taking more requests. 
 - The shutter creates a task to check and recover delinquent suppliers until none exist. 
 - When sufficient suppliers are available the clients are opened and traffic resumes.


## Kit
The kit comprises 4 interfaces and 1 enum.

Examples are provided in the test folder with 2 sample mock implementations.

```
Test Class: com.brentcroft.drawbridge.PizzaServiceTest

 Service up recovery up

   Given a pizza service
    When the service starts
         investigation is complete
         the suppliers white, wholemeal, seedy go down
         client blue orders pizza base: white; salad: lettuce; cheese: gorgonzola; meat: lamb; sauce: mayo
         investigation is complete
    Then no pizza delivered
         service status is RECOVERY
         suppliers are under interrogation
         no clerks are open
    When the suppliers white, wholemeal, seedy come up
         recovery is complete
         client blue orders pizza base: white; salad: lettuce; cheese: gorgonzola; meat: lamb; sauce: mayo
    Then service status is UP
         all suppliers are available
         all clerks are open
         pizza delivered
```

### Log

```
2019-12-03 01:14:14.964 DEBUG Started supplier: lamb [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: cucumber [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: bacon [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: wholemeal [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: beef [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: cheddar [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: peperoni [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: manchego [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: white [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: tomato [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: mayo [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: tomato [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: brown [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: onion [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: gorgonzola [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: bbq [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: roquefort [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: mozarella_02 [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: olives [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: mozarella_03 [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: brie [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: seedy [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: lettuce [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: mozarella_01 [OPEN]
2019-12-03 01:14:14.964 DEBUG Started supplier: celery [OPEN]
2019-12-03 01:14:14.964 DEBUG Investigating 5 suppliers
2019-12-03 01:14:14.964 DEBUG Root Suppliers: available=5, partial=0, failed=0 
all bases [UP]
all salads [UP]
mozarella [UP]
all cheeses [UP]
all meats [UP]
all sauces [UP]
2019-12-03 01:14:14.964 INFO Opened clerk: green [OPEN]
2019-12-03 01:14:14.964 INFO Opened clerk: blue [OPEN]
2019-12-03 01:14:14.964 INFO Opened clerk: red [OPEN]
2019-12-03 01:14:14.979 DEBUG Stopped supplier: white [SHUT]
2019-12-03 01:14:14.979 DEBUG Stopped supplier: wholemeal [SHUT]
2019-12-03 01:14:14.979 DEBUG Stopped supplier: seedy [SHUT]
2019-12-03 01:14:14.979 INFO Created check thread: status=UP
2019-12-03 01:14:14.979 DEBUG Investigating 1 suppliers
2019-12-03 01:14:15.029 DEBUG Root Suppliers: available=4, partial=0, failed=1 
wholemeal [DOWN]
all bases [DOWN]
all salads [UP]
mozarella [UP]
all cheeses [UP]
all meats [UP]
all sauces [UP]
2019-12-03 01:14:15.030 INFO Closed clerk: red [SHUT]
2019-12-03 01:14:15.030 INFO Closed clerk: green [SHUT]
2019-12-03 01:14:15.030 INFO Closed clerk: blue [SHUT]
2019-12-03 01:14:15.030 INFO Created recovery thread: status=RECOVERY
2019-12-03 01:14:15.031 INFO Destroyed check thread: status=RECOVERY
2019-12-03 01:14:15.082 INFO Response: ErrorPizza: unfortunately your pizza could not be made right now; java.lang.IllegalStateException: No transport
2019-12-03 01:14:15.082 DEBUG Started supplier: white [OPEN]
2019-12-03 01:14:15.082 DEBUG Started supplier: wholemeal [OPEN]
2019-12-03 01:14:15.082 DEBUG Started supplier: seedy [OPEN]
2019-12-03 01:14:16.032 DEBUG Investigating 1 suppliers
2019-12-03 01:14:16.082 DEBUG Root Suppliers: available=5, partial=0, failed=0 
wholemeal [UP]
all bases [UP]
all salads [UP]
mozarella [UP]
all cheeses [UP]
all meats [UP]
all sauces [UP]
2019-12-03 01:14:16.082 INFO Opened clerk: green [OPEN]
2019-12-03 01:14:16.082 INFO Opened clerk: red [OPEN]
2019-12-03 01:14:16.082 INFO Opened clerk: blue [OPEN]
2019-12-03 01:14:16.082 INFO Destroyed recovery thread: status=UP
2019-12-03 01:14:16.115 INFO Response: BasicPizza: base: WHOLEMEAL, salad: LETTUCE, cheese: GORGONZOLA, meat: LAMB, sauce: MAYO
```

