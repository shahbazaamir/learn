import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

let stompClient = null;

export const connect = (onMessageReceived) => {
  const socket = new SockJS("http://localhost:8246/chat-websocket");
  stompClient = new Client({
    webSocketFactory: () => socket,
    reconnectDelay: 5000,
    onConnect: () => {
      stompClient.subscribe("/topic/messages", (msg) => {
        onMessageReceived(JSON.parse(msg.body));
      });
    },
  });

  stompClient.activate();
};

export const sendMessage = (message) => {
  if (stompClient && stompClient.connected) {
    stompClient.publish({ destination: "/app/send", body: JSON.stringify(message) });
  }
};
