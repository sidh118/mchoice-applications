# Quick Steps To publish your applications. #

---

Once you are done with your application and tested it successfully with the simulator, then you are ready to deploy your application.

**Note:**
  1. For run your application on live, it's should be hosted in a hosting place. You can use your own hosting place or currently we provide you free hosting facilities on our hosting place, but only for the web applications which runs on tomcat (with tomcat server in linux platform) and only MySql as DBMS. Other web applications such as .NET applications should be hosted in your own hosting place which supporting to the .NET

  * **Step 1**: Fill up the following form with the correct details.[Form](https://spreadsheets.google.com/a/9696.lk/viewform?hl=en&pli=1&formkey=dExQeWhiQlluQnIta0xVUlBCelpTTmc6MQ#gid=0)

> Drop an email to [support@appzone.lk] if you didn’t get application details within 7 working days.

  * **Step 2**: You will receive an email, it will contain your user log in  details and database details (if you requested). And also with an
  1. **application name**
  1. **application ID**
  1. **reserved keyword**
  1. **your application password**
  1. **4 ports**(To establish communication with your application)
which are assigned to your application.

  * **Step 3**: after that, set the **App URL**, **Application ID**, and **Password** when instantiating the sender class (MchoiceAventuraSmsMoSender or MchoiceUssdSender)

  * **Step 4**: Once it is done, change the name of your build to the given application name. Now build your project.You will get a **YOUR\_KEYWORD.WAR**

  * **Step 5**: Now connect to your reserved space.
For this you can use ssh command (in linux) or SSH Secure Client (in Windows you can [download](http://ce.uml.edu/SSH3.2.9.exe) from this link).
> > Check this [link](http://compudoc.princeton.edu/compudocwiki/index.php/HowTos:Connect_to_login_servers_via_ssh) and it will help you.

![http://img193.imageshack.us/img193/2677/impl1.png](http://img193.imageshack.us/img193/2677/impl1.png)
![http://img69.imageshack.us/img69/222/impl2.png](http://img69.imageshack.us/img69/222/impl2.png)
![http://img217.imageshack.us/img217/2684/impl3.png](http://img217.imageshack.us/img217/2684/impl3.png)
![http://img585.imageshack.us/img585/7594/impl4.png](http://img585.imageshack.us/img585/7594/impl4.png)
![http://img32.imageshack.us/img32/8532/impl5.png](http://img32.imageshack.us/img32/8532/impl5.png)


  * **Step 6**: Once you are connected you will be in the home directory.
```
            Ex: - cd /home/<username>/
```

  * **Step 7**: The first thing you have to do is copy the Apache tomcat from the /opt/shared location.
```
            Ex: - cp /opt/shared/apache-tomcat-6.0.29.tar.gz /home/<username>
```
> > And extract it.
```
            tar -xvzf apache-tomcat-6.0.29.tar.gz
```

  * **Step 8**: Go in to this folder and you will find a sub folder called **webapps**.
```
            Ex: - cd /home/<username>/apache-tomcat-6.0.29/webapps
```

  * **Step 9**: Upload your WAR file to this folder. (Refer to the above tutorial)

  * **Step 10**: Go to the **conf** folder in tomcat directory.
```
            Ex: - cd /home/<username>/apache-tomcat-6.0.29/conf
```

  * **Step 11**: Edit the **server.xml** file
```
            Ex: - vi server.xml
```
Here you need to configure with the given ports to you.

> Ex:- If you have assigned **9123**, **9124**, **9125**, and **9126** ports in **server.xml** change the ports for following elements.
```
            <Server port=“9123” shutdown="SHUTDOWN"> 
      <Connector port="9124" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="9125" />
```
> > Here, in this file, you will see that there are two **Connectors**. As in the example above your HTTP Connector port is now **9124**. Therefore your Application URL is **apps.appzone.lk:9124/warfile\_name**

![http://img525.imageshack.us/img525/8240/implconfigurations.jpg](http://img525.imageshack.us/img525/8240/implconfigurations.jpg)

  * **Step 12**: If you are **not** requested for MySQL database, then you can skip to **Step 14**.

  * **Step 13**: In your home directory upload you’re [Data Definition Language (DDL)](http://en.wikipedia.org/wiki/Data_Definition_Language) and [Data Manipulation Language (DML)](http://en.wikipedia.org/wiki/Data_Manipulation_Language) files to create tables and to insert new records. If you request for MySQL database, we will create an empty database for you and we will provide the following information.
```
            database name: *my_database*
 	    User: *my_username* 
 	    password: *my_password* 
```
> > Let's assume your DDL and DML file names are respectively **create.sql** and **insert.sql**.


> Note that you don't have to create the database; it will be already created for you. Give the following 2 commands on the shell

```
            mysql -u *my_username* -p*my_password* my_database < create.sql 
            mysql -u *my_username* -p*my_password* my_database < insert.sql 
```

> Now you have successfully configured your database.

  * **Step 14**: Email your application URL to support@appzone.lk (This is referred as MO url)

  * **Step 15**: Go to the bin folder in tomcat directory.
```
            Ex: - cd /home/<username>/apache-tomcat-6.0.29/bin
```

> Now give the following command
```
       ./startup.sh or sh startup.sh
```
Now your application is UP and running. You can test it live.