# Remote Access Tool
This project involves creating a remote access tool that allows users to perform the following actions on a remote system:

1. Get the processes of the remote system: This functionality allows users to retrieve a list of the processes currently running on the remote system.
2. Take a screenshot of the remote system: This functionality allows users to capture a screenshot of the current state of the remote system's display.
3. Reboot the remote system: This functionality allows users to remotely restart the remote system.

The tool involves the creation of both a service provider/server and a client component.

The latter will be implemented using the client/server and the service-oriented model.
- [Client/Server model implementation](https://github.com/MohamedMOUMOU/remote-access-tool/tree/master/ClientServer)
- [Service-oriented model implementation](https://github.com/MohamedMOUMOU/remote-access-tool/tree/master/ServiceModel)

Also, and extending the service-oriented model implementation, the client is made asynchronous in the last implementation.
- [Asynchronous client](https://github.com/MohamedMOUMOU/remote-access-tool/tree/master/AsynchronousClient)