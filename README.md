# Chat Warrior Service

**Chat Warrior Service** is a real-time chat application built using Spring Boot and WebSockets, offering messaging features with support for multimedia (images and videos). This project supports user registration, disconnection, and private messaging between users in real time.

## Features

- **User Registration:** Users can join the chatroom by entering a nickname and full name. The system registers the user and keeps track of online users.
- **Real-time Messaging:** Users can send and receive messages in real time through WebSockets. This includes support for text, images, and video.
- **Private Messaging:** Users can select another user to start a private chat.
- **Multimedia Support:** Supports sharing multimedia (images and videos) in messages.
- **Persistent Chat History:** The chat history is stored in the database and retrieved when users communicate.
- **WebSocket Communication:** WebSockets are used for efficient two-way communication between clients and the server.
- **User Management:** The system handles online/offline statuses for users.

## Project Structure

The project consists of three main layers:

1. **Controller Layer (`controller` package):**
    - Handles incoming HTTP and WebSocket requests.
    - Includes the following controllers:
        - **`ChatController`**: Manages chat messaging between users, handles sending and receiving messages.
        - **`UserController`**: Handles user registration, disconnection, and fetching connected users.
   
2. **Service Layer (`service` package):**
    - Business logic implementation. It contains:
        - **`ChatMessageService`**: Manages chat messages, including saving and retrieving messages, and checking multimedia types (image/video).
        - **`ChatRoomService`**: Manages chat rooms between users, ensures each chat session has a unique room ID.
        - **`UserService`**: Manages users, handles user registration and online/offline status updates.
   
3. **Repository Layer (`repository` package):**
    - Data access layer, using Spring Data JPA to interact with the database.
    - **`ChatMessageRepository`**: Manages chat messages persistence.
    - **`ChatRoomRepository`**: Manages chat room data.
    - **`UserRepository`**: Manages user data.

## WebSocket Configuration

The project uses WebSocket for communication, and STOMP is used to relay messages between users in real time. The WebSocket configuration can be found in the `WebSocketConfig` class.

- **Endpoint Registration**: The WebSocket connection is established via the `/ws` endpoint.
- **Message Broker**: The system uses a simple in-memory message broker to handle private messaging and public channels.

## How It Works

1. **User Connects**: A user connects to the application by providing a nickname and full name. A WebSocket connection is established, and the user is registered.
   
2. **Send Messages**: Once connected, a user can send messages to any other connected user. The message is either a plain text, an image, or a video. Messages are delivered in real time.
   
3. **Receive Messages**: When a user receives a message, the message content (text or multimedia) is displayed in the chat window.
   
4. **Multimedia Handling**: When sending a file, the system checks the MIME type using Apache Tika to determine if the file is an image or a video, then appropriately handles it.
   
5. **Disconnect**: When a user disconnects, the system updates their status to offline.

## Installation and Setup

### Prerequisites
- Java 23
- Maven
- MySQL (or any preferred relational database)
- JavaScript, HTML, CSS (for front-end assets)

### Clone the Repository
bash
git clone https://github.com/mohsenfn/chatwarriorservice.git
cd chatwarriorservice

### Database Configuration
spring.application.name=ChatWarriorService
spring.data.mongodb.uri=mongodb://localhost:27017/chatDB
server.port=8081
spring.mvc.view.prefix=/WEB-INF/views/
spring.mvc.view.suffix=.jsp
spring.servlet.multipart.max-file-size=3000MB
spring.servlet.multipart.max-request-size=3000MB

### Run the Application

mvn clean install
mvn spring-boot:run

## API Endpoints

### User Management

- **POST** `/app/user.addUser`: Adds a user to the system.
- **POST** `/app/user.disconnectUser`: Disconnects a user from the chat.

### Messaging

- **POST** `/app/chat`: Sends a chat message between users.
- **GET** `/messages/{senderId}/{recipientId}`: Retrieves chat messages between two users.

## Technologies Used

- **Java 23**
- **Spring Boot**: Core framework
- **Spring WebSockets**: For real-time communication
- **Apache Tika**: For detecting MIME types of uploaded files
- **Maven**: For project build and dependency management
- **MySQL**: For database storage
- **JavaScript, HTML, CSS**: For front-end
- **SockJS and STOMP**: WebSocket fallback and protocol support

## Future Improvements

- Add user authentication and authorization (OAuth2, JWT).
- Enhance front-end with a more modern framework (e.g., React or Angular).
- Improve error handling and logging.
- Add unit and integration tests.


