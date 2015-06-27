# Getting Started: SMS Application > PHP #

---


  * Software Requirements:
    * **[mChoice-Simulator-LATEST](http://code.google.com/p/mchoice-applications/downloads/detail?name=mchoice-simulator-LATEST.zip&can=2&q=)**
    * **Macromedia Dreamweaver**

> This tutorial describes how to develop and deploy a simple SMS Application on the Aventura System using PHP as the language. The example application, will demonstrate how to receive users SMS and to respond them back with a message of your own.

## Creating a Project from Scratch ##

> First, you have to choose **_‘Web Server Software’_** where you test your application (XAMPP, WAMP, etc.) of your choice. **‘XAMPP’** is used in this guide.

> For this tutorial we will be naming the project as **‘MchoiceSMSSampleApp’**. Create a folder under htdocs folder of your XAMPP installation and name it as **‘MchoiceSMSSampleApp’** .

## Importing API’s ##

> The next step, is to attach the necessary API’s needed to create this PHP application. These can be downloaded by following this link.
> [sms-php-api-client-util-LATEST](http://code.google.com/p/mchoice-applications/downloads/detail?name=sms-php-api-client-util-LATEST.zip&can=2&q=)

> After downloading, you can find two files called **_‘AppZoneReciever.php’_** and **_‘AppZoneSender.php’_** extracted folder. Put them under **‘MchoiceSMSSampleApp’** folder.

> Now that all the necessary API’s are imported and you can start implementing your idea.

## Implementing Your Idea ##

> For this tutorial, we will first create a php file named **_SMSReceiver.php_**. Go to **‘File’** -> **‘New’** and under **_‘Category’_** choose **‘Dynamic Page’** , select language as **‘PHP’**. Then click Create.
> Now go to **‘File’** -> **‘Save’** and input File name as **_SMSReceiver.php_**. Then click Save.

> Then you will need to include **_‘AppZoneReciever.php’_** and **_‘AppZoneSender.php’_** like below.
```
  <?php
   //include the api for sending and receiving
   include_once 'AppZoneSender.php';
   include_once 'AppZoneReciever.php';
  ?>
```

> Afterwards you will have to initiate object **_AppZoneReciever_**.
```
   $reciever=new AppZoneReciever();
```

> Class **_AppZoneReciever_** has methods such as
    * **getMessage** – returns the user's message
    * **getAddress** – returns the encrypted address of the user's mobile number
    * **getCorrelator** – returns a unique id that uniquely identifies every message
Once you get the users message using the above mentioned methods, you can proceed with your logic. For this tutorial, we have implemented BMI calculator which will take two arguments weight (kg) and height (cm) and send the BMI value as a response message,  back to the user.

> To send a response back to the user, the API has provided another class named **_AppZoneSender_**, which can send a SMS’s to a mobile phone. While instantiating this class would take the following 3 arguments.
    1. **URL** (for the application server)
    1. **Application ID**
    1. **Application Password**

> For testing purposes, for this tutorial, we will use the following values for these arguments.
    1. URL  :  **http://127.0.0.1:8000/sms/**
    1. Application ID :  **“appid”**
    1. Application Password  :  **“password”**

```
  /*
   creating the sender object with 
   server url
   appId
   password
  */
  $sender=new AppZoneSender("http://127.0.0.1:8000/sms/", "appid", "password");
```

> Using the newly instantiated **_AppZoneSender_** instance’s sms method, you can send the message that you desire, back to the user. This method will take 2 parameters.
    1. **_$message_** (the response you want to send back)
    1. **_$addresses_** (the user’s mobile phone number)

> Now you have completed the **_SMSReceiver.php_**.

> The following is how the **_SMSReceiver.php_** would look like after completion.
```
   <?php
   //include the api for sending and receiving
   include_once 'AppZoneSender.php';
   include_once 'AppZoneReciever.php';

   try{
	//create the receiver
	$reciever=new AppZoneReciever();
	//getting the message content
	$content = $reciever->getMessage(); // get the message content
    $address = $reciever->getAddress(); // get the sender's address
    $correlationId = $reciever->getCorrelator(); // get the correlation id of the messgae

    $responceMsg;	
	//your logic goes here......
	$split = split(' ', $content);	
	$responceMsg = bmiLogicHere($split);
		
	/*
	creating the sender object with 
		server url
		appId
		password
	*/
	$sender=new AppZoneSender("http://127.0.0.1:8000/sms/", "appid", "password");
	
	//sending a one message
	$res=$sender->sms($responceMsg, $reciever->getAddress());		
   }
   catch(AppZoneException $ex){
	//throws when failed sending or receiving the sms
	logFile("ERROR: {$ex->getStatusCode()} | {$ex->getStatusMessage()}");
   }

   function bmiLogicHere($split){
	$responceMsg;
    if (sizeof($split) < 2)
    {
    	$responceMsg = "Invalid message content";
    }
    else
    {
       $weight = (float) $split[0]."<br/>";
       $height = (float) $split[1]."<br/>";
	   	   
       $bmi = getBMIValue($weight, ($height / 100));
	   $category = getCategory($bmi);
	   	          
       $responceMsg = "Your BMI :". round($bmi,2).", Category :".$category;	   
     }
     return $responceMsg;
   }

   function getBMIValue($weight, $height)
   {		
	return ($weight / pow($height, 2));
   }

   function getCategory($bmiValue)
   {		
	if ($bmiValue < 18.5)
	{
		return "Underweight";
	}
	else if ($bmiValue >= 18.5 && $bmiValue < 24.9)
	{
		return "Normal Weight";
	}
	else if ($bmiValue >= 25 && $bmiValue < 29.9)
	{
		return "Overweight";
	}
	else
	{
		return "Obesity";
	}
     }
  ?>
```

## Building the Application ##

> After you save **_SMSReceiver.php_** file, you can continue testing with simulator. Refer [mChoice-Simulator guide](http://code.google.com/p/mchoice-applications/wiki/SimulatorGuide#mChoice-Simulator_Guide) to test the application.

> Note : App URL for the simulator
```
  http://localhost/appzone/php/SmsReceiver.php
```