import socket #import socket module
import sys #import the built in system module

def main():

    if len(sys.argv) != 4: #Checks the length of the sys.argv
        print("Usage: python3 client.py server_host server_port filename") #if the arguments are more than 3 error message.
        return

    server_host = sys.argv[1]        #This asigns the first value to server host.
    server_port = int(sys.argv[2])   #This assigns the second value to server port.
    filename = sys.argv[3]           #This assigns the third value to filename.

    #Resolve the server IP address
    serverIP = socket.gethostbyname(server_host)

    #Create a TCP socket
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    try:
        #Connect to the server 
        client_socket.connect((serverIP, server_port))

        #Send a GET request for file
        request = f"GET /{filename} HTTP/1.1\r\nHost: {server_host}\r\n\r\n"
        client_socket.sendall(request.encode())

        #Recieve and display the server response
        server_response= ""
        while True:
            response = client_socket.recv(1024)
            if not response:
                break
            server_response += response.decode()

        #Extract the content of the requested file
        content_index = server_response.find('b\r\n\r\n') + len('b\r\n\r\n')
        content = server_response[content_index:]

        print(content)  # Print the content of the file

    #Error message if the server not running 
    except ConnectionRefusedError:
        print("Connection refused. Make sure the server is running and listening on the specified port.")

    finally:
        client_socket.close()

if __name__ == "__main__":
    main()

