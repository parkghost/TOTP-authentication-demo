TOTP Authentication Demo
=================

This is a simple web application which implement Two-Factor authentication by TOTP and Google Authenticator 

Demo Site
-----------

http://totp-authentication-demo.herokuapp.com/

Installation
-----------
1.Redis

    wget http://redis.googlecode.com/files/redis-2.4.5.tar.gz
    tar xzf redis-2.4.5.tar.gz
    cd redis-2.4.5
    make
    src/redis-server

2.TOTP Authentication Demo

    git clone git@github.com:parkghost/TOTP-authentication-demo.git
    cd TOTP-authentication-demo
    mvn package
    java $JAVA_OPTS -jar target/dependency/jetty-runner.jar target/*.war

Bug tracker
-----------

Have a bug? Please create an issue here on GitHub!

https://github.com/parkghost/TOTP-authentication-demo/issues


Authors
-------

**Brandon Chen**

+ http://brandonc.me
+ http://github.com/parkghost

License
---------------------

Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0