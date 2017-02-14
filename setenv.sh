#***************************************************************************
# To include your server's reCAPTCHA keys, https://www.google.com/recaptcha/admin#list:
#***************************************************************************
#CATALINA_OPTS="${CATALINA_OPTS} -Dre4ms_recaptcha_secret=<REPLACE>"
#CATALINA_OPTS="${CATALINA_OPTS} -Dre4ms_recaptcha_public=<REPLACE>"

#***************************************************************************
# Where to put the re4ms .json files.  
# The default is re4mss in the server home directory.
#***************************************************************************
#CATALINA_OPTS="${CATALINA_OPTS} -Dre4ms_dir=<REPLACE>"

#***************************************************************************
# For debugging, uncomment this line:
#***************************************************************************
#CATALINA_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"

#***************************************************************************
# https://www.digitalocean.com/community/questions/fresh-tomcat-takes-loong-time-to-start-up
# You'll likely want to uncomment this line on digitalocean ubuntu servers:
#***************************************************************************
#JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true -Xmx600m -XX:+UseConcMarkSweepGC"
