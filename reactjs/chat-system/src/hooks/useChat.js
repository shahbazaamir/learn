import { useState, useEffect } from "react";
import { connect, sendMessage } from "../api/chatService";

export const useChat = () => {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    connect((msg) => setMessages((prev) => [...prev, msg]));
  }, []);

  const send = (from, to,content) => {
    sendMessage({ from,to, content });
  };

  return { messages, send };
};
