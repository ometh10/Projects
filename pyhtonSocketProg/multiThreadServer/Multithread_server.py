import time #import time module
from socket import * #import socket module 
import threading #import threading module 

def main():

    serverSocket = socket(AF_INET,SOCK_STREAM) #Creating TCP Socket
    serverPort = 6789                          #Defining the port number
    servername = gethostname()                 #Initiating a method to get the currrent IP address
    serverIP = gethostbyname(servername)       #Tranfer the host IP address to the servername

    serverSocket.bind((serverIP, serverPort))
    serverSocket.listen(5)                     #Defining the max. number of connections

    print(f"The TCP server is ready to recieve connection on {serverIP}:{serverPort}")

    while True:
        
        client_socket, address = serverSocket.accept() #Initiating the client server connection
        print("\nReady to serve...")

        #Creating threads to each client 
        client_thread = threading.Thread(target= handle_client, args= (client_socket, address))
        client_thread.start() #Client thread starting 
        time.sleep(0.1)       #Make the program sleep for 100ms


def handle_client(client_socket: socket, client_address):
    try:
        message = client_socket.recv(1024)
        filename = message.split()[1] #Extracting the requestes filename
        f = open(filename[1:])
        outputdata = f.read()

        #Send one HTTP header line into socket
        header = "    HTTP/1.1 200 OK\r\n\r\n".encode()
        client_socket.send(header)
        contentType = "Content-Type: text/html\r\n".encode()
        client_socket.send(contentType) #sends the constructed header to the client 
        
        #Send the content of the requested file to the client
        for i in range(0, len(outputdata)):            #iterates over each line of file stored in outputData.
            client_socket.send(outputdata[i].encode()) #it sends each line to the client over the connection socket

        client_socket.send("\r\n".encode())#Sends new message to inform that its the end of the file. 

        print(f"Connection to {client_address} is closed...")
        client_socket.close()

    except IOError:
        #Response message for file not found
        response = response = "   HTTP/1.1 404 Not Found\r\n\r\n"
        client_socket.send(response.encode())

        #Send HTML body as the response
        htmlResponse = "<html><head><body><center><h2> 404 Page Not Found. </h2></center></body></head></html>"
        client_socket.send(htmlResponse.encode())

        client_socket.close()

if __name__ == "__main__":
    main()



