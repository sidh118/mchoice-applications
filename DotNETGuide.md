# Getting Started: SMS Application > .NET #

---


  * Software Requirements:
    * **[mChoice-Simulator-LATEST](http://code.google.com/p/mchoice-applications/downloads/detail?name=mchoice-simulator-LATEST.zip&can=2&q=)**
    * **Microsoft Visual Studio**

This tutorial describes how to develop and deploy a simple SMS Application on the Aventura System using .Net as the environment. The example application, will demonstrate how to receive users SMS and to respond them back with a message of your own.

## Creating a Project from Scratch ##

Go to **‘File’** -> **‘New Web Site’** and under **_‘Visual Studio installed templates’_** choose **‘ASP.NET Web Site’** , select Location as **‘File System’**,  language as **‘Visual C#’**  and select project path. Then click OK. This is important, as you will be sending and receiving messages through HTTP.
Project Name will be taken as selected folder name for the project. For this tutorial we will be naming the project as **‘MchoiceSMSSampleApp’**.

## Sample Code for Receive, Process and Send Messages ##

> For receive, process and send message by your application you can reuse and enhance the classes which are coming with the sample application. Those classes are
    * **_MchoiceAventuraSmsMessage_**
    * **_MchoiceAventuraSmsMoHttpHandler_**
    * **_MchoiceAventuraSmsSender_**
> Note that you can do your own implementation for message receiving, processing and sending. Or else you can enhance and use given sample code.

> Now you can start implementing your idea.

## Implementing Your Idea ##

> For this tutorial, we will first create a C# class named **_SmsReceiver_**. Then you will need to extend this class with **_MchoiceAventuraSmsHttpHandler_**.

> Afterwards you will have to implement an abstract method called onMessage. Since all the messages that your application is going to receive will be passed onto this method, it is important to note that your logic should also come inside this method.

> This **_onMessage_** method takes an instance of     **_MchoiceAventuraSmsMessage_** as the paramater. **_MchoiceAventuraSmsMessage_** has methods such as;
    * **getMessage** – returns the user's message
    * **getAddress** – returns the encrypted address of the user's mobile number
    * **getCorrelator** – returns a unique id that uniquely identifies every message
    * **getVersion** – returns mChoice aventura http rest API version that is currently used
> Once you get the users message using the above mentioned methods, you can proceed with your logic. For this tutorial, we have implemented BMI calculator which will take two arguments weight (kg) and height (cm) and send the BMI value as a response message,  back to the user.

> To send a response back to the user, the API has provided another class named MchoiceAventuraSmsSender, which can send a SMS’s to a mobile phone. While instantiating this class would take the following 3 arguments.
    1. **URL** (for the application server)
    1. **Application ID**
    1. **Application Password**

> For testing purposes, for this tutorial, we will use the following values for these arguments.
    1. URL  :  **http://127.0.0.1:8000/sms/**
    1. Application ID :  **“appid”**
    1. Application Password  :  **“password”**

```
MchoiceAventuraSmsSender mchoiceAventuraSmsSender = new MchoiceAventuraSmsSender("http://127.0.0.1:8000/sms/", "appid", "password");
mchoiceAventuraSmsSender.sendMessage(responceMsg, new String[] { mchoiceAventuraSmsMessage.getAddress() });
```

> Using the newly instantiated **_MchoiceAventuraSmsSender_** instance’s sendMessage method, you can send the message that you desire, back to the user. This method will take 2 parameters.
    1. String **_message_** (the response you want to send back)
    1. String **_userAddress_** (the user’s mobile phone number)

> Now you have completed the **_MessageReceiver_** Class.

> The following is how the **_MessageReceiver_** class would look like after completion.

```
using System;
using System.Data;
using System.Configuration;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Globalization;

/// <summary>
/// Summary description for SmsReceiver
/// </summary>
public class SmsReceiver : MchoiceAventuraSmsMoHttpHandler
{
    protected override void onMessage(MchoiceAventuraSmsMessage mchoiceAventuraSmsMessage)
    {
        String content = mchoiceAventuraSmsMessage.getMessage(); // get the message content
        String address = mchoiceAventuraSmsMessage.getAddress(); // get the sender's address
        String correlationId = mchoiceAventuraSmsMessage.getCorrelator(); // get the correlation id of the messgae

        String responceMsg = "";

        //your logic goes here......
        String[] split = content.Split(' ');
        responceMsg = bmiLogicHere(split);

        MchoiceAventuraSmsSender mchoiceAventuraSmsSender = new MchoiceAventuraSmsSender("http://127.0.0.1:8000/sms/", "appid", "password");
        mchoiceAventuraSmsSender.sendMessage(responceMsg, new String[] { mchoiceAventuraSmsMessage.getAddress() });
    }

    private String bmiLogicHere(String[] split)
    {
        String responceMsg;
        if (split.Length < 2)
        {
            responceMsg = "Invalid message content";
        }
        else
        {
            double weight = Double.Parse(split[0]);
            double height = Double.Parse(split[1]);

            double bmi = getBMIValue(weight, (height / 100));

            NumberFormatInfo nfi = new NumberFormatInfo();
            nfi.NumberDecimalSeparator = ".";

            String category = getCategory(bmi);
            responceMsg = "Your BMI :" + bmi.ToString("#.##", nfi) + ", Category :" + category;
        }
        return responceMsg;
    }

    private double getBMIValue(double weigh, double height)
    {
        return (weigh / Math.Pow(height, 2));
    }

    private String getCategory(double bmiValue)
    {
        if (bmiValue < 18.5)
        {
            return "Underweight";
        }
        else if (bmiValue >= 18.5 && bmiValue < 24.9)
        {
            return "Normal Weight";
        }
        else if (bmiValue >= 25 && bmiValue < 29.9)
        {
            return "Overweight";
        }
        else
        {
            return "Obesity";
        }
    }
}
```

## Defining and Mapping the Http Handler Class ##

In order to direct all the messages that comes to your application, into the **_SmsReceiver_** class’s implemented onMessage method, you will need to define the mapping in the **_web.config_** file.
For this you will need to go inside the  web.config and make some modifications.
In the following example this will map all the request to **_SmsReceiver_** class.

```
<?xml version="1.0"?>
<configuration>
  <appSettings/>
  <connectionStrings/>
  <system.web>
    <authentication mode="Forms" />
    <httpHandlers>
      <add verb="*" path="*" type="SmsReceiver"/>
    </httpHandlers>
    <compilation debug="true"/>
  </system.web>
</configuration>
```

## Building the Application ##

> Now press _F5_ or click on run button to build and run your application.

> Now you can deploy this on an application server of your choice. Refer [mChoice-Simulator guide](http://code.google.com/p/mchoice-applications/wiki/SimulatorGuide#mChoice-Simulator_Guide) to test the application.