#Not needed for open java.
export TGT=re4ms
scp $JAVA_HOME/jre/lib/security/local_policy.jar $TGT:/usr/lib/jvm/java-8-oracle/jre/lib/security/
scp $JAVA_HOME/jre/lib/security/US_export_policy.jar $TGT:/usr/lib/jvm/java-8-oracle/jre/lib/security/

