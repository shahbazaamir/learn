import React from "react";
import MessageBubble from "./MessageBubble";

const ChatWindow = ({ messages }) => {
  return (
    <div style={{ height: "400px", overflowY: "scroll", border: "1px solid gray" }}>
      {messages.map((msg, index) => (
        <MessageBubble key={index} message={msg} />
      ))}
    </div>
  );
};

export default ChatWindow;
