# Sviazeń
Звязваем палітвязняў - connecting political prisoners.  
__ATTENTION:__ Using the app is currently insecure. Please, wait until HTTPS support is added.
  
    
### About
Repressions and ideological pressure have been an integral part of Belarusian life
since a dictator gained power in 1994.
Politically motivated arrests, murdercases, regression of economy and public life
are what makes the life of over 9 million people.
In summer 2020 the entire world observed so many Belarusians
looking forward to democratic changes in our country - and we still keep up
with terrifying numbers of arrested, beaten and murdered people.  
On the other hand, the increasing repressions raised up people's solidarity to unbelievably high. 
From couples getting engaged on a Sunday march,
to close neighbours who've never talked to each other getting united,
the great problem creates great relationships all over the country.  
We're creating Sviazeń to help great people stay connected.

##### How it works
1. Create your account. It's quick and free!
1. Give as much information about yourself as you want to share. Note that only what you specify as your name will be visible to not connected users.
1. Specify where and when you've been during your arrests.
1. That's it! The app will find people you've been inprisoned together with.
1. Connect with your co-prisoners and see how you may contact them.

The app is currently focused specificly on administrative arrests (no more than 30 days)
and allows editing your schedule on day-to-day basis.
In future we may add support for criminal (long-term) arrests.

##### Compatibility
Sviazeń runs on Android 6+ phones and tablets. You need internet connection, for sure. Don't worry: all the internet traffic is encrypted.  
Sviazeń is a *fully voluntary*  project and has and will ever have *absolutely no* monetization (no subscriptions, no purchases, no ads).


### Technical overview

##### Project  structure
Sviazeń is a set of Gradle projects.
- 'server' project is the backend implementation.
- 'android' is the Android client implementation.
- 'contract' project contains a single 'domain' module shared between the client and the server.

Each project has 1 or more modules as appropriate.


##### Security
The following features make Sviazeń secure:
- SHA-512 hash for passwords, which is considered the standard de-facto.
- HTTPS connection between the client and the server, with a custom certificate. The certificate key is obviously *not* published to this repository. - __PENDING, BUT NOT SUPPORTED YET__
- The client app does cache some information, but it *never* persists any user-sensitive data.
- Raw password is not stored even in RAM. The app operates with password hash.


### Run locally
As an ordinary user, you would just install the latest APK (see `releases`), connect to network and enjoy the app.
Besides, if you urge to have a deeper look at Sviazeń,
you may run our server app on your own machine and connect your Android device over a local network.

##### Create a certificate
1. Go to CSR generator: https://decoder.link/csr_generator
1. Enter the required fields (what you enter in them doesn't matter for local testing).
1. Copy the certificate into `android/remote/src/main/asset/crt/sviazen.crt` (existing content of the file is to be replaced).
1. Copy the key into `server/protocol/src/main/resources/sviazen.key`.
1. Copy the CSR into `server/protocol/src/main/resources/sviazen.csr`.

##### Prepare the database
`Sviazen` server app relies on a MySQL database which you have to create and make available before you can run the app on your machine.
1. Install MySQL server, if haven't yet, and run it.
1. Create the database schema. You may find an SQL script creating the correct schema at `server/sql/create.sql`.
1. (Optional): You may prepopulate the database with test data by running `server/sql/test.sql` on the created schema. It creates a sample account; you will then be able to authorize to that account with credentials found in the script file.
1. (Optional): Same way, you may add other sample prisoners by running `server/sql/test2.sql`.
1. Create a file `server/protocol/src/main/resources/confidential.properties` and fill it as follows:
```
spring.datasource.username = <Your MySQL username>
spring.datasource.password = <Your MySQL password>
```
6. In the same directory (`server/protocol/src/main/resources/`) but another file (`public.properties`) replace the value of `spring.datasource.url` property with valid JDBC URL to your MySQL schema according to the following pattern:  
`jdbc:mysql://<Host name>/<Schema name>`  
Note: your `<Host name>` will most likely be `localhost:3306`.

##### Run the server
1. Install Gradle 6.*, if you haven't yet.
1. From the `server` folder run:
`gradle bootRun`

##### Run the client
1. In code find `RetrofitHolder.IP` constant and replace it with the (private) IP address of your machine. This is the IPv4 address and can be obtained via `ifconfig` (Unix) or `ipconfig` (Windows)
1. Build APK using Android Studio.

Please note that you cannot use our pre-built APK since your certificate is (99.9% that) different from ours.  
Also note that locally running server is only available on local network,
so your Android device must be connected to the same access point as your computer is.
  
    

*Appreciate each other!*  
*Cheers,*  
*the Sviazeń team.*