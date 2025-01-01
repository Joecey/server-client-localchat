# Server Client Localchat (Java 21)
This project is a demonstration of a TCP Socket connection between
server and client connections. The server and its connected clients 
can chat to each other with their messages including things such as 
their username (`SERVER-ADMIN` in the case of SocketServer), and the time
the message was sent. This is similar to how apps such as WhatsApp 
and Messenger work 

Additionally, if other clients join the chat - all historical messages
will be shown too.

## How to build
With IntelliJ, you can just build the application with `Build > Build Project`.
From there, navigate to `out/production/server-client-localchat`. Then
proceed with the below instructions

## How to run...
Once navigated to the output directory, you can run both the Server and
Client as follows...

Also note that everything runs on localhost / 127.0.0.1

### SocketServer
1. Run `java localchat.server.SocketServer <port_number> <max_messages>`
2. `port_number` is replaced with the number port you want to run on e.g. `8080`
3. `max_messages` is the number of messages you want to store as the chat history.
If nothing is added here, the default of 20 messages will be used.
4. An example of the final command is `java localchat.server.SocketServer 8080 50`

Once started, the server can send messages to any connected clients by typing in the terminal.
Furthermore, the server session can be ended by typing `.q`

### SocketClient
1. Run `java localchat.client.SocketClient`
2. Follow the instructions as shown below...

```commandline
What ip would you like to connect to? (Default 127.0.0.1)
(here, you can just press enter to continue)

What port would you like to connect to?
8080

What name would you like to give your client (No special characters are allowed)?
Joe
```
From here, the client attempts to connect to the active SocketServer with the same
port given. When successful, clients will receive the chat history and can add 
more messages by typing in the terminal. Once finished, they can end their session
with `.q`

## Examples
### Server
```commandline
> java localchat.server.SocketServer 8080 100
[INFO]:  Starting new chat server on localhost at port 8080
[INFO]:  Currently running chat room at port: 8080
[INFO]: New client has connected!
[JoeBloggs]: Hi everyone! (19:45 1 January 2025)
Hi Joe!
[SERVER-ADMIN]: Hi Joe! (19:45 01 January 2025)
[JoeBloggs]: this is so cool  (19:46 1 January 2025)
[JoeBloggs]: I am going to disconnect now  (19:46 1 January 2025)
[JoeBloggs]: see you! (19:46 1 January 2025)
See you!
[SERVER-ADMIN]: See you! (19:46 01 January 2025)
[INFO]: Disconnecting client...Goodbye!
[INFO]: New client has connected!
[Billyman]: Hey everyone! (19:46 1 January 2025)
[Billyman]: Just testing this (19:46 1 January 2025)
[Billyman]: okay i will disconnect now (19:46 1 January 2025)
goodbye!
[SERVER-ADMIN]: goodbye! (19:46 01 January 2025)
[INFO]: Disconnecting client...Goodbye!
```

### Client 
```commandline
> java localchat.client.SocketClient
What ip would you like to connect to? (Default 127.0.0.1)

What port would you like to connect to?
8080

What name would you like to give your client (No special characters are allowed)?
Billy man
[INFO]: Attempting to connect to server 127.0.0.1:8080
[INFO]: Connected!

============ CHAT ROOM ===========
[JoeBloggs]: Hi everyone! (19:45 1 January 2025)
[SERVER-ADMIN]: Hi Joe! (19:45 01 January 2025)
[JoeBloggs]: this is so cool  (19:46 1 January 2025)
[JoeBloggs]: I am going to disconnect now  (19:46 1 January 2025)
[JoeBloggs]: see you! (19:46 1 January 2025)
[SERVER-ADMIN]: See you! (19:46 01 January 2025)
Hey everyone!
[Billyman]: Hey everyone! (19:46 1 January 2025)
Just testing this
[Billyman]: Just testing this (19:46 1 January 2025)
okay i will disconnect now
[Billyman]: okay i will disconnect now (19:46 1 January 2025)
[SERVER-ADMIN]: goodbye! (19:46 01 January 2025)
.q
Thanks for chatting. Goodbye! You are free to press Ctrl+C to end session...
```

## Resources used
https://www.baeldung.com/a-guide-to-java-sockets - A Guide to Java Sockets by baeldung
https://docs.oracle.com/javase/8/docs/api/java/net/Socket.html - Java socket documentation
https://www.geeksforgeeks.org/queue-interface-java/ - GeekForGeeks guide on Queues in Java