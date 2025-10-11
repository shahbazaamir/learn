import React from "react";
import ChatWindow from "../components/ChatWindow";
import ChatInput from "../components/ChatInput";
import { useChat } from "../hooks/useChat";

const ChatPage = () => {
  const { messages, send } = useChat();

  return (
    <div>
      <h2>One-to-One Chat</h2>
      <ChatWindow messages={messages} />
      <ChatInput onSend={send} />
    </div>
  );
};

export default ChatPage;
