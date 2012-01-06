TOTP authentication demo
=================

This is a simple web application which implement Two-Factor authentication by TOTP and Google Authenticator 

Demo Site
-----------

http://totp-authentication-demo.herokuapp.com/

Usage
-----------
	git clone git@github.com:parkghost/TOTP-authentication-demo.git
	cd TOTP-authentication-demo
	mvn package
	java $JAVA_OPTS -classpath "target/totp-authentication-demo-1.0/WEB-INF/classes:target/totp-authentication-demo-1.0/WEB-INF/lib/*" me.brandonc.security.totp.core.Main target/totp-authentication-demo-1.0

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