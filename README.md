# Prerequisites
1. Java jdk 11 
2. Eclipse IDE for Java EE Developers
3. Tomcat 9.0.39 installed in Eclipse correctly

# Environment setting
1. project -> Properties -> Targeted Runtimes -> Apache Tomcat v9 -> Apply and close (this will add tomcat to our project)
2. Run (the green button) -> Run Configuration ->common -> Encoding -> Other & choose UTF-8 (support Chinese input in Eclipse)
3. use your own maven not the one in Eclipse, this will make Eclipse happier (optional)

# Run
1. Maven ->　Update Project Configuration
2. Run as -> maven install (optional)
3. Run as -> Run on Server
4. Sometime you need to restart the Eclipse when you see error in .xml files. It is not my fault, but you treat it too rude and it crash (you make it angry now)

# Test web page 
1. Run our Angular frontend with this backend at the same time
2. go to localhost8080 and play around
3. if you cannot receive any result from backend, you may encounter problem(s) in the Eclipse/maven/tomcat/jdk configuration, see if we can solve together
 
Important: pull before you code and push after it to avoid conflict; Avoid push to master directly..

# Others
1. I can successfully support text encryption and recovery, even with special symbols
2. I can successfully support image encryption and recovery, but I cannot open the encrypted shares...
3. I tried to output real byte array of the encrypted shares and produce something call jpg, but still cannot open >.<
4. leave to disscuss later..
