# Developing USSD Applications using Java USSD API #

---



  * **Software Requirements:**
    * JDK 1.6+
    * [mChoice-Simulator-LATEST](http://code.google.com/p/mchoice-applications/downloads/detail?name=mchoice-simulator-LATEST.zip&can=2&q=)
    * Tomcat (or any server of your choice)
    * IDE of your choice


  * **Step 1:** Download the Java USSD API

  * **Step 2:** Create a new Java Application

  * **Step 3:** Import the USSD API libraries which you have downloaded

  * **Step 4:** Create a class (i.e. demo.MessageReceiver) and extend this class to **MchoiceUssdReceiver** and implement _onMessage_ and _onSessionTerminate_ methods. This class will be a servlet class.

  * **Step 5:** _onMessage_ will be called when a USSD request comes from a mobile. **MchoiceUssdMessage** is the only parameter passed to this _onMessage_ method. **MchoiceUssdMessage** has public methods; _getAddress_, _getConversationId_, _getCorrelationId_, _getMessage_,
    * getAddress – returns the address of the mobile (encrypted)
    * getMessage – returns the user message (and menu selection)
    * getConversationId – returns the session ID for the conversation between the mobile and this application. This is unique for each mobile user.
    * getCorrelationId – returns a unique ID which is assigned for an instance of the session. For example, each time user selects a menu option, a unique correlation id is assigned.

  * **Step 6:** _onSessionTerminate_ method will be called when a conversation ends. This method has one argument, **MchoiceUssdTerminateMessage**, which is mostly similar to **MchoiceUssdMessage**.

  * **Step 7:** Once you receiver user message, write a logic class to process it.

  * **Step 8:** To reply back to the user, instantiate **MchoiceUssdSender** class. The constructor takes 3 string arguments; those are Server URI, Application ID, and Password. When testing with the simulator, this Server URI must be http://127.0.0.1:8000/ussd/.

  * **Step 9:** Call _sendMessage_ to reply back to the user. See the following code extract. The lass parameter is a Boolean. True means that from this message, the session will be terminated.

```
@Override
    public void onMessage(MchoiceUssdMessage ussd) {
        try {
            MchoiceUssdSender ussdSender = new MchoiceUssdSender("http://127.0.0.1:8000/ussd/", "appid", "password");
            ussdSender.sendMessage("Welcome to USSD menu", ussd.getAddress(), ussd.getConversationId(), true);
        } catch (MchoiceUssdException ex) {
            ex.printStackTrace();
        }
    }
```


  * **Step 10:** Define this servlet in web.xml file. Add the following to xml elements.

```
    <servlet>
        <servlet-name>receiver</servlet-name>
        <servlet-class>demo.MessageReceiver</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>receiver</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>
```

  * **Step 11:** Build the application and create the WAR file and deploy it on the server of your choice.

  * **Step 12:** Refer [mChoice-Simulator guide](http://code.google.com/p/mchoice-applications/wiki/SimulatorGuide#mChoice-Simulator_Guide) to test the application.



---


  * Hint – When developing complex USSD application with many levels of submenus, have a java.util.Map with the "Address" as the key. When a user request for service, check whether this "Address" exist on the map. If not, send the user the main menu and add the "Address" into the Map. In _onSessionTerminate_ method, remove the "Address" from this map.