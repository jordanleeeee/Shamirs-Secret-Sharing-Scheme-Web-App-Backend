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
 
- ![#f03c15](https://via.placeholder.com/15/f03c15/000000?text=+) `Important: pull before you code and push after it to avoid conflict; Avoid push to master directly..` 

# Others
1. I can successfully support text encryption and recovery, even with special symbols
2. I can successfully support image encryption and recovery, will add it to application frontend later. This need to ask professor as we used different way to do.

# Pseudocode

```
function encrypt(s, n, t)
  int[] coefficient
  int[] shares
  coefficients[0] = s
  for i = 1 to t
    coefficients[i] = randomNum(256)
  for i = 0 to n
    shares[i] = F(i, coefficients)
  return shares

function F(x, coefficients)
  int result = 1
  int val = 1
  for coefficient in coefficients
    result = result + coefficient* val
    val = val* x
  return result

```
```
function recover(shares, t)
  result = 0
  for i = 0 to t
    temp = shares[i].y
    for j = 0 to t
      if i != j
        fraction = shares[j].x / (shares[j].x - shares[i].x)
        temp = temp* fraction
    result = result + temp
  return result
```
```
function addOrMinus(a, b):
  return a ⊕ b
```
```
function multiplication(a, b)
  result = 0
  for i=7 down to 0
    if b[i] = 1
      result = result ⊕ a
    if a[0] = 0
      a = a << 1
    else
      a = (a << 1) ⊕ irreduciblePolynomials
  return result
```
```
function division(a, b)
  bInverse = power(b, 2^8-2)
  return multiplication(a, bInverse)

function power(a, idx)
  result = a
  for i=0 to idx-1
    result = multiplication(result, a)
  return result

// improved division
function division(a, b)
  return divisionTable[a][b]
```
