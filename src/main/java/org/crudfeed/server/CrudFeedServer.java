package org.crudfeed.server;

import org.crudfeed.core.entity.Post;
import org.crudfeed.core.entity.User;
import org.crudfeed.core.repository.MessageRepository;
import org.crudfeed.core.repository.PostRepository;
import org.crudfeed.core.repository.UserRepository;
import org.crudfeed.core.service.AuthService;
import org.crudfeed.core.service.MessageService;
import org.crudfeed.core.service.PostService;
import org.crudfeed.core.service.UserService;
import org.crudfeed.server.mapper.AccountResponseMapper;
import org.crudfeed.server.mapper.MessageResponseMapper;
import org.crudfeed.server.mapper.PostResponseMapper;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import static java.lang.IO.*;

public class CrudFeedServer {

    private final UserRepository userRepository = new UserRepository();
    private final PostRepository postRepository = new PostRepository();
    private final MessageRepository messageRepository = new MessageRepository();

    private final AuthService authService = new AuthService(userRepository);
    private final UserService userService = new UserService(userRepository);
    private final PostService postService = new PostService(postRepository);
    private final MessageService messageService = new MessageService(messageRepository);

    private final AccountResponseMapper accountMapper = new AccountResponseMapper(userService);
    private final PostResponseMapper postMapper = new PostResponseMapper();
    private final MessageResponseMapper messageMapper = new MessageResponseMapper();

    @SuppressWarnings("InfiniteLoopStatement")
    static void main() {
        new CrudFeedServer().start();
    }

    private void start() {
        SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

        try (SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(7777)) {

            //Type of ASCII art: STANDARD
            println("""
                      ____                _ _____             _   ____                             _        ___ \s
                     / ___|_ __ _   _  __| |  ___|__  ___  __| | / ___|  ___ _ ____   _____ _ __  / |      / _ \\\s
                    | |   | '__| | | |/ _` | |_ / _ \\/ _ \\/ _` | \\___ \\ / _ \\ '__\\ \\ / / _ \\ '__| | |     | | | |
                    | |___| |  | |_| | (_| |  _|  __/  __/ (_| |  ___) |  __/ |   \\ V /  __/ |    | |  _  | |_| |
                     \\____|_|   \\__,_|\\__,_|_|  \\___|\\___|\\__,_| |____/ \\___|_|    \\_/ \\___|_|    |_| (_)  \\___/\s
                    
                    """);

            while (true) {
                Socket socket = serverSocket.accept();
                socket.getInetAddress().getHostName();
                println("Accepted connection from " + socket.getRemoteSocketAddress());

                Thread.startVirtualThread(() -> clientLogic(socket));
            }

        } catch (IOException e) {
            println("Error starting server: " + e.getMessage());
        }
    }

    private void clientLogic(Socket socket) {
        try (socket;
             DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {

            boolean isRunning = true;

            while (isRunning) {
                String request;
                try {
                    request = inputStream.readUTF();
                } catch (IOException e) {
                    isRunning = false;
                    continue;
                }

                String[] parts = request.split("\\|", -1);
                String command = parts[0];

                if ("signIn".equalsIgnoreCase(command) || "signUp".equalsIgnoreCase(command)) {
                    String name = parts.length > 1 ? parts[1] : "unknown";
                    println("Receive request: " + command + "|user: " + name + "|[PROTECTED]");

                } else if ("writeMessage".equalsIgnoreCase(command)) {
                    String senderId = parts.length > 1 ? parts[1] : "unknown";
                    String receiverId = parts.length > 2 ? parts[2] : "unknown";
                    println("Receive request: writeMessage|from_id: " +
                            senderId +
                            "|to_id: " +
                            receiverId +
                            "|[TEXT PROTECTED]");

                } else if ("viewMessages".equalsIgnoreCase(command)) {
                    String senderId = parts.length > 1 ? parts[1] : "unknown";
                    String receiverName = parts.length > 2 ? parts[2] : "unknown";
                    println("Receive request: viewMessages|requested_by_id: " +
                            senderId +
                            "|with_user: " +
                            receiverName);

                } else if ("changeName".equalsIgnoreCase(command)) {
                    String userId = parts.length > 2 ? parts[2] : "unknown";
                    println("Receive request: changeName|user_id: " + userId + "|[NEW_NAME PROTECTED]");

                } else if ("changePass".equalsIgnoreCase(command)) {
                    String userId = parts.length > 2 ? parts[2] : "unknown";
                    println("Receive request: changePass|user_id: " + userId + "|[NEW_PASSWORD PROTECTED]");

                } else {
                    println("Receive request: " + request);
                }

                if ("Exit".equalsIgnoreCase(command)) {
                    isRunning = false;
                    continue;
                }

                switch (command) {
                    case "status" -> outputStream.writeUTF("Server status: ONLINE");
                    case "signUp" -> {
                        String name = parts[1];
                        String pass = parts[2];

                        if (name.isBlank() || pass.isBlank()) {
                            outputStream.writeUTF("Error! Name and/or password required!");
                        } else {
                            User user = authService.signUp(name, pass);

                            if (user != null) {
                                outputStream.writeUTF("SUCCESS|" + user.getNickname() + "||" + user.getId());
                            } else {
                                outputStream.writeUTF("Error! Registration failed!");
                            }
                        }
                    }
                    case "signIn" -> {
                        String name = parts[1];
                        String pass = parts[2];

                        if (name.isBlank() || pass.isBlank()) {
                            outputStream.writeUTF("Error! Name and/or password required!");
                        } else {
                            User user = authService.signIn(name, pass);

                            if (user != null) {
                                outputStream.writeUTF("SUCCESS|" + user.getNickname() + "||" + user.getId());
                            } else {
                                outputStream.writeUTF("Error! User not found!");
                            }
                        }
                    }
                    case "getUserById" -> outputStream.writeUTF(userService.getNicknameById(Long.parseLong(parts[1])));
                    case "subscribe" -> outputStream.writeUTF(
                            userService.subscribe(Long.parseLong(parts[1]), parts[2])
                    );
                    case "unSubscribe" -> outputStream.writeUTF(
                            userService.unsubscribe(Long.parseLong(parts[1]), parts[2])
                    );
                    case "createPost" -> {
                        String[] postParts = request.split("\\|", 3);
                        if (postParts.length < 3 || postParts[2].isBlank()) {
                            outputStream.writeUTF("Error! Post can not be empty!");
                        } else {
                            outputStream.writeUTF(postService.createPost(Long.parseLong(postParts[1]), postParts[2]));
                        }
                    }
                    case "editPost" -> outputStream.writeUTF(postService.updatePost(
                            Long.parseLong(parts[1]),
                            parts[2],
                            Long.parseLong(parts[3]))
                    );
                    case "deletePost" -> outputStream.writeUTF(postService.deletePost(
                            Long.parseLong(parts[1]),
                            Long.parseLong(parts[2]))
                    );
                    case "viewFeed" -> outputStream.writeUTF(postMapper.map(postService.viewFeed()));
                    case "showUserPostsById" -> {
                        outputStream.writeUTF(postMapper.map(postService.showUserPostsById(Long.parseLong(parts[1]))));
                    }
                    case "viewUserLikes" -> {
                        outputStream.writeUTF(postMapper.map(postService.showUserLikes(Long.parseLong(parts[1]))));
                    }
                    case "searchPostById" -> {
                        Post post = postService.searchPostById(Long.parseLong(parts[1]));

                        outputStream.writeUTF(
                                post == null
                                        ? "No posts found!"
                                        : postMapper.map(List.of(post))
                        );
                    }
                    case "searchPostByWords" ->
                            outputStream.writeUTF(postMapper.map(postService.searchPostByWords(parts[1])));
                    case "likePost" -> outputStream.writeUTF(postService.likePost(
                            Long.parseLong(parts[1]),
                            Long.parseLong(parts[2]))
                    );
                    case "disLikePost" -> outputStream.writeUTF(postService.dislikePost(
                            Long.parseLong(parts[1]),
                            Long.parseLong(parts[2]))
                    );
                    case "writeMessage" -> {
                        String[] msgParts = request.split("\\|", 4);
                        if (msgParts.length < 4 || msgParts[3].isBlank()) {
                            outputStream.writeUTF("Error! Message can not be empty!");
                        } else {
                            long senderId = Long.parseLong(msgParts[1]);
                            long receiverId = userService.getUserId(msgParts[2]);
                            outputStream.writeUTF(messageService.sendMessage(senderId, receiverId, msgParts[3]));
                        }
                    }
                    case "viewMessages" -> {
                        long senderId = Long.parseLong(parts[1]);
                        long receiverId = userService.getUserId(parts[2]);
                        outputStream.writeUTF(messageMapper.map(messageService.viewMessages(senderId, receiverId)));
                    }
                    case "editMessage" -> outputStream.writeUTF(
                            messageService.editMessage(Long.parseLong(parts[1]), parts[2], Long.parseLong(parts[3])));
                    case "deleteMessage" -> outputStream.writeUTF(
                            messageService.deleteMessage(Long.parseLong(parts[1]), Long.parseLong(parts[2])));
                    case "viewAccountPage" -> {
                        outputStream.writeUTF(accountMapper.map(userService.viewAccountPage(parts[1])));
                    }
                    case "editAccountPage" -> {
                        String[] bioParts = request.split("\\|", 3);
                        outputStream.writeUTF(userService.editAccountPage(Long.parseLong(bioParts[1]), bioParts[2]));
                    }
                    case "changeName" ->
                            outputStream.writeUTF(userService.changeName(parts[1], Long.parseLong(parts[2])));
                    case "changePass" ->
                            outputStream.writeUTF(userService.changePass(parts[1], Long.parseLong(parts[2])));
                    case "deleteAccount" -> {
                        String answer = userService.deleteAccount(parts[1], Long.parseLong(parts[2]));
                        outputStream.writeUTF(answer);

                        if ("Account successfully deleted!".equals(answer)) {
                            isRunning = false;
                        }
                    }
                }

                outputStream.flush();
            }

        } catch (IOException e) {
            println("Connection error with " + socket.getRemoteSocketAddress() + ": " + e.getMessage());

        } finally {
            println("Closing connection with " + socket.getRemoteSocketAddress() + ".");
        }
    }
}