
<%@page import="javax.jms.TextMessage"%>
<%@page import="javax.jms.QueueSender"%>
<%@page import="javax.jms.Destination"%>
<%@page import="javax.jms.Session"%>
<%@page import="javax.jms.QueueSession"%>
<%@page import="javax.jms.QueueConnection"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.jms.QueueConnectionFactory"%>
<%@page import="javax.jms.Queue"%>
<%
InitialContext context = new InitialContext();
QueueConnectionFactory factory = (QueueConnectionFactory)context.lookup("java:/JmsXA");

final String QUEUE_LOOKUP = "queue/MyQueue";

Queue queue = (Queue)context.lookup(QUEUE_LOOKUP);
out.println("queue is present");


QueueConnection connect = factory.createQueueConnection();
out.println("queue connection created ");
QueueSession session1 = connect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
out.println("queue session   is applicable ");
//Destination destination = (Destination)getRemoteContext().lookup("/queue/nsQueue");
QueueSender objQS = session1.createSender(queue);
out.println("queue sender created ");
//MessageProducer sender = session.createProducer(destination);
out.println( " created produces for destination ");
TextMessage  message = session1.createTextMessage("bla");
out.println( " created a jms message ");
		objQS.send(message);
		out.println( " sent a jms message ");
		connect.close();
		out.println( " closing the connection  ");
%>