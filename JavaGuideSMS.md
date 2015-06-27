# Getting Started: SMS Application > Java #

---


  * Software Requirements:
    * **JDK 1.6+**
    * **[mChoice-Simulator-LATEST](http://code.google.com/p/mchoice-applications/downloads/detail?name=mchoice-simulator-LATEST.zip&can=2&q=)**
    * **Tomcat** (or any server of your choice)
    * **IDE of your choice** (For this guide we will be using NetBeans)

> This tutorial describes how to develop and deploy a simple SMS Application on the Aventura System using Java as the environment. The example application, will demonstrate how to receive users SMS and to respond them back with a message of your own.

## Creating a Project from Scratch ##

> If you’re using NetBeans as your IDE, go to **File -> New Project** and under **_‘Categories’_** choose **‘Java Web’** and under **_‘Projects’_** choose **‘Web Application’**. This is important, as you will be sending and receiving messages through HTTP.
![http://img231.imageshack.us/img231/7900/javasms1.png](http://img231.imageshack.us/img231/7900/javasms1.png)

> Under **_‘Name and Locations’_** you can name your project to your liking, as well as the **_‘Project Location and Folder’_**. For this tutorial we will be naming the project as ‘demo\_app’.

> Next step is choosing the **_‘Server and Settings’_** where you can choose an application server (Tomcat, Glassfish, etc.) of your choice. Afterwards, you can select the frameworks(Spring Web MVC, Struts, Hibernate, etc.)  you want to use on your application, if none, you can proceed by pressing **‘Finish’**.

## Importing API’s ##

> The next step, is to attach the necessary API’s needed to create this java application. These can be downloaded by following this link.
> [sms-java-api-client-util-LATEST](http://code.google.com/p/mchoice-applications/downloads/detail?name=sms-java-api-client-util-LATEST.zip&can=2&q=)

> After downloading, you can right-click on **‘Libraries’** and select **_‘Add JAR/Folder’_** and select all the .jar files inside the downloaded folder.

![http://img194.imageshack.us/img194/524/javasms2.png](http://img194.imageshack.us/img194/524/javasms2.png)

![http://img402.imageshack.us/img402/992/javasms3.png](http://img402.imageshack.us/img402/992/javasms3.png)

> Now that all the necessary API’s are imported you can start implementing your idea.

## Implementing Your Idea ##

> For this tutorial, we will first create a java class named **_MessageReceiver_**. Then you will need to extend this class with **_MchoiceAventuraSmsMoServlet_**. (You will need to import the following line in order to do this.)
```
  import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraSmsMoServlet;
```

> Afterwards you will have to implement an abstract method called **_onMessage_** . Since all the messages that your application is going to receive will be passed onto this method, it is important to note that your logic should also come inside this method.

> This **_onMessage_** method takes an instance of **_MchoiceAventuraSmsMessage_** as the paramater. **_MchoiceAventuraSmsMessage_** has methods such as;

  * **getMessage** – returns the user's message
  * **getAddress** – returns the encrypted address of the user's mobile number
  * **getCorrelator** – returns a unique id that uniquely identifies every message
  * **getVersion** – returns mChoice aventura http rest API version that is currently used

> Once you get the users message using the above mentioned methods, you can proceed with your logic. For this tutorial, we will simply send a response message “Message Received. Thank You.” back to the user.

> To send a response back to the user, the API has provided another class named **_MchoiceAventuraSmsSender_**, which can send a SMS’s to a mobile phone. While instantiating this class would take the following 3 arguments.
    1. **URL** (for the application server)
    1. **Application ID**
    1. **Application Password**

> For testing purposes, for this tutorial, we will use the following values for these arguments.
    1. URL  :  **http://127.0.0.1:8000/sms/**
    1. Application ID :  **“appid”**
    1. Application Password  :  **“password”**

> You will need to wrap this **_MchoiceAventuraSmsSender_** instance with a **try-catch** block, since an exception will be thrown if there is any error on the given URL.

> The try-catch block would be as following.
```
  try {
            MchoiceAventuraSmsSender smsSender = new MchoiceAventuraSmsSender(new URL("http://127.0.0.1:8000/sms/"), "appid", "password");
      
  } catch (MalformedURLException ex) {
        	ex.printStackTrace();
  }
```

> Using the newly instantiated **_MchoiceAventuraSmsSender_** instance’s sendMessage method, you can send the message that you desire, back to the user. This method will take 2 parameters.
    1. String **_message_** (the response you want to send back)
    1. String **_userAddress_** (the user’s mobile phone number)

> Now you have completed the **_MessageReceiver_** Class.

> The following is how the **_MessageReceiver_** class would look like after completion.

```
package demo;

import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraMessagingException;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraSmsMessage;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraSmsMoServlet;
import hsenidmobile.sdp.rest.servletbase.MchoiceAventuraSmsSender;
import java.net.MalformedURLException;
import java.net.URL;

public class MessageReceiver extends MchoiceAventuraSmsMoServlet {

    @Override
    protected void onMessage(MchoiceAventuraSmsMessage msg) {
        System.out.println("Message Received: "+ msg.getMessage());
        System.out.println("Mobile Number: "+ msg.getAddress());
        
        try {
            MchoiceAventuraSmsSender smsSender = new MchoiceAventuraSmsSender(new URL("http://127.0.0.1:8000/sms/"), "appid", "password");
            smsSender.sendMessage("Message Received. Thank You", msg.getAddress());
        } catch (MchoiceAventuraMessagingException ex) {
                ex.printStackTrace();
        } catch (MalformedURLException ex) {
                ex.printStackTrace();
        }
    }
```

## Defining and Mapping the Servlet Class ##

> In order to direct all the messages that comes to your application,into the **_MessageReceiver_** class’s implemented **_onMessage_** method, you will need to define it as a Servlet Class and map it with a URL pattern.

> For this you will need to go inside the **_WEB-INF -> web.xml_** and make some modifications.
![http://img835.imageshack.us/img835/2308/javasms4.png](http://img835.imageshack.us/img835/2308/javasms4.png)

> You will need to add the following inside the **`<`web-app`>`** tag of the web.xml file.

```
<servlet>
        <servlet-name>MessageReceiver</servlet-name>
        <servlet-class>demo.MessageRReceiver</servlet-class>
</servlet>

<servlet-mapping>
        <servlet-name>MessageReceiver</servlet-name>
        <url-pattern>/*</url-pattern>
</servlet-mapping>
```

> The final output of the **web.xml** file would be as following.

```
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="2.5" xmlns="http://java.sun.com/xml/ns/javaee"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">
    <display-name>Sample SMS Sending app - App Zone Developer</display-name>
    <servlet>
        <servlet-name>MessageReceiver</servlet-name>
        <servlet-class>demo.MessageReceiver</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>MessageReceiver</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>
    
</web-app>
```

## Building the Application ##

> _Right-Click_ the project and select Clean and Build to create the _WAR_ file.
![http://img842.imageshack.us/img842/2593/javasms5.png](http://img842.imageshack.us/img842/2593/javasms5.png)

> Now you can deploy this on an application server of your choice. Refer [mChoice-Simulator guide](http://code.google.com/p/mchoice-applications/wiki/SimulatorGuide#mChoice-Simulator_Guide) to test the application.


---

