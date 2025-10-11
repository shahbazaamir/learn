import React, { useState } from "react";

const ChatInput = ({ onSend }) => {
  const [content, setContent] = useState("");
  const [from, setFrom] = useState("");
  const [to, setTo] = useState("");

  const send = () => {
    if (content.trim() !== ""
        && from.trim() !== ""  
        && to.trim() !== "") {
      onSend(from, to ,content);
      setContent("");
    }
  };

  return (
    <div>
      <input type="text" value={from} onChange={(e) => setFrom(e.target.value)} placeholder="Your name"/>
      <input type="text" value={to} onChange={(e) => setTo(e.target.value)} placeholder="Receiver"/>
      <input type="text" value={content} onChange={(e) => setContent(e.target.value)} placeholder="Type message"/>
      <button onClick={send}>Send</button>
    </div>
  );
};

export default ChatInput;
