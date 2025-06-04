#import socket module
from socket import *
import sys # In order to terminate the program

serverSocket = socket(AF_INET, SOCK_STREAM)#creating TCP objects for socket

#Prepare a sever socket
serverPort = 6789
serverName = gethostname() #This method returns the current IP address of the computer. 
serverIP= gethostbyname(serverName)

#Binding the server socket
serverSocket.bind((serverIP,serverPort))
#Listening for incoming connection 
serverSocket.listen(1) #defining the max. num of connections can be in the queue
#display message
print (f"The TCP server is ready to recieve connections on  {serverIP}:{serverPort}")

while True:
    #Establish the connection
    print('Ready to serve...')
    connectionSocket, address = serverSocket.accept()

    try:
        message = connectionSocket.recv(1024)
        filename = message.split()[1]
        f = open(filename[1:])
        outputdata = f.read()

        #Send one HTTP header line into socket
        header = "   HTTP/1.1 200 OK\r\n\r\n".encode()
        connectionSocket.send(header)
        contentType = "Content-Type: text/html\r\n".encode()
        connectionSocket.send(contentType) #sends the constructed header to the client 
        
        #Send the content of the requested file to the client
        for i in range(0, len(outputdata)): #iterates over each line of file stored in outputData.
            connectionSocket.send(outputdata[i].encode()) #it sends each line to the client over the connection socket

        connectionSocket.send("\r\n".encode())#Sends new message to inform that its the end of the file. 

        connectionSocket.close()

    except IOError:
        #Send response message for file not found
        response = "   HTTP/1.1 404 Not Found\r\n\r\n"
        connectionSocket.send(response.encode())

        #Send HTML body as the response
        htmlResponse = "<html><head><body><center><h2> 404 Page Not Found. </h2></center></body></head></html>"
        connectionSocket.send(htmlResponse.encode())

        #Close client socket
        connectionSocket.close()
     

    serverSocket.close()
    sys.exit()#Terminate the program after sending the corresponding data