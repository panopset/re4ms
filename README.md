# Introduction

[Re4ms](http://re4ms.com) is an open source private message sharing system.
A reasonable Java developer level understanding of Tomcat
is all that is needed to set up your own Re4ms server, giving
you complete control of all security aspects of this little
"secret decoder ring" sharing system.

# Disclaimer

**FOR ENTERTAINMENT ONLY!**

Because Re4ms is free open source software, you are entirely responsible 
for any security breaches resulting from its use.  Whether a breach is the result 
of implementation on an untrusted server, configuration error, Re4ms bug, 
or any other reason:  you can not hold Panopset liable, because nobody 
sold you anything, or made any claims regarding Re4ms security standards.

# Build

* Minimum Java version is 8.

* Ubuntu 16.04.1 x64 server.
* [Disable root access.](https://www.digitalocean.com/community/tutorials/initial-server-setup-with-ubuntu-16-04)
* Add some swap space, especially if you have a 512MB server.

    sudo touch /var/swap.img
    sudo chmod 600 /var/swap.img
    sudo fallocate -l 2G /var/swap.img
    sudo mkswap /var/swap.img
    sudo swapon /var/swap.img
    sudo echo -e "/var/swap.img none swap sw 0 0\n" >> /etc/fstab

* Install [Java 8](http://tipsonubuntu.com/2016/07/31/install-oracle-java-8-9-ubuntu-16-04-linux-mint-18/).

Exit and verify

    java -version
    echo $JAVA_HOME

* [Install tomcat](https://www.digitalocean.com/community/tutorials/how-to-install-apache-tomcat-8-on-ubuntu-16-04).
* [Set up HTTPS](https://www.digitalocean.com/community/tutorials/how-to-secure-nginx-with-let-s-encrypt-on-ubuntu-14-04)

    certbot-auto certonly -a webroot --webroot-path=/usr/share/nginx/html -d re4ms.com -d www.re4ms.com
    
* [Verify](https://www.ssllabs.com) HTTPS is good.
* Copy setenv.sh to $CATALINA_HOME/bin, and make any available customizations, which are commented in the setenv.sh file.
* Make whatever customizations to the re4ms project that you like.
* Create re4ms.war and deploy it locally:

    mvn clean install

* Deploy to your server, something like:

    scp $CATALINA_HOME/webapps/re4ms.war yourUserName@yourServerName:/opt/tomcat/webapps/ROOT.war

# Health checks

    top
    sudo ufw status
    df

    
