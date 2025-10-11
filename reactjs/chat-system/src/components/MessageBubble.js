import React from "react";

const MessageBubble = ({ message }) => {
  return (
    <div style={{ margin: "5px", padding: "5px", border: "1px solid #ccc", borderRadius: "5px" }}>
      <b>{message.from}:</b> {message.content}
    </div>
  );
};

export default MessageBubble;
