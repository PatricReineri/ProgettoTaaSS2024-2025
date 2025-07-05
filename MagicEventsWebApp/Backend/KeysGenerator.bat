@echo off
REM === Customizable parameters ===
set KEYSTORE_FILE=keystore.p12
set ALIAS=springboot
set STOREPASS=password
set DNAME="CN=localhost, OU=Dev, O=MyCompany, L=MyCity, S=MyState, C=IT"
set VALIDITY=365

REM === Generate the keystore in PKCS12 format ===
echo Generate the keystore %KEYSTORE_FILE%...
keytool -genkeypair ^
 -alias %ALIAS% ^
 -keyalg RSA ^
 -keysize 2048 ^
 -storetype PKCS12 ^
 -keystore %KEYSTORE_FILE% ^
 -validity %VALIDITY% ^
 -storepass %STOREPASS% ^
 -keypass %STOREPASS% ^
 -dname %DNAME%

echo.
echo ✅ Keystore generated: %KEYSTORE_FILE%
echo 📌 Password: %STOREPASS%
pause
