# Introduction

[Re4ms](http://panopset.com/re4ms) is an open source private message sharing system.
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

Summary (replace username of course):

    adduser username
    usermod -aG sudo username
    cd /home/username
    mkdir .ssh

Back on your machine...

    cd .ssh
    scp id_rsa.pub re4ms:/home/username/.ssh/authorized_keys
    
On the server...

    cd /home/username
    chown -R username .ssh
    chgrp -R username .ssh
    su - username
    chmod 700 ~/.ssh
    cd .ssh
    chmod 600 authorized_keys
    
On your machine...

    vim .ssh/config
    
... and update the user from root to username.

    Host re4ms
    Hostname 1.2.3.4
    User username
    IdentityFile ~/.ssh/id_rsa

On the server not logged in as root.

    sudo vim /etc/ssh/sshd_config

set

    PermitRootLogin no

and then

    sudo service sshd restart

* Add some swap space, especially if you have a 512MB server.

Copy into a script:

    sudo touch /var/swap.img
    sudo chmod 600 /var/swap.img
    sudo fallocate -l 2G /var/swap.img
    sudo mkswap /var/swap.img
    sudo swapon /var/swap.img
    sudo echo -e "/var/swap.img none swap sw 0 0\n" >> /etc/fstab

* [Install tomcat](https://www.digitalocean.com/community/tutorials/how-to-install-apache-tomcat-8-on-ubuntu-16-04).

Summary:

    sudo apt-get update
    sudo apt-get -y install default-jdk
    sudo groupadd tomcat
    sudo useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat
    mkdir tmp
    cd tmp
    curl -O http://www-us.apache.org/dist/tomcat/tomcat-8/v8.5.11/bin/apache-tomcat-8.5.11.tar.gz
    sudo mkdir /opt/tomcat
    sudo tar xzvf apache-tomcat-8.5.11.tar.gz -C /opt/tomcat --strip-components=1
    cd /opt/tomcat
    sudo chgrp -R tomcat /opt/tomcat
    sudo chmod -R g+r conf
    sudo chmod g+x conf
    sudo chown -R tomcat webapps/ work/ temp/ logs/
    
... continuing with Step 5, to set up tomcat as a service, first get the JAVA_HOME value:

    sudo update-java-alternatives -l

    sudo vim /etc/systemd/system/tomcat.service
    
    [Unit]
    Description=Apache Tomcat Web Application Container
    After=network.target

    [Service]
    Type=forking

    Environment=JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre
    Environment=CATALINA_PID=/opt/tomcat/temp/tomcat.pid
    Environment=CATALINA_HOME=/opt/tomcat
    Environment=CATALINA_BASE=/opt/tomcat
    Environment='CATALINA_OPTS=-Xms512M -Xmx1024M -server -XX:+UseParallelGC'
    Environment='JAVA_OPTS=-Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom'

    ExecStart=/opt/tomcat/bin/startup.sh
    ExecStop=/opt/tomcat/bin/shutdown.sh

    User=tomcat
    Group=tomcat
    UMask=0007
    RestartSec=10
    Restart=always

    [Install]
    WantedBy=multi-user.target

wq, Reload and start

    sudo systemctl daemon-reload
    sudo systemctl start tomcat
    
* [Set up HTTPS](https://www.digitalocean.com/community/tutorials/how-to-secure-nginx-with-let-s-encrypt-on-ubuntu-14-04)

    certbot-auto certonly -a webroot --webroot-path=/usr/share/nginx/html -d re4ms.com -d www.re4ms.com
    
* [Verify](https://www.ssllabs.com) HTTPS is good.
* [Reverse proxy](https://www.digitalocean.com/community/tutorials/how-to-configure-nginx-with-ssl-as-a-reverse-proxy-for-jenkins)
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

    
