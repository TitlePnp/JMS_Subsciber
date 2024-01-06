/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package subscriber;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

/**
 *
 * @author tleku
 */
public class Main {

    @Resource(mappedName = "jms/SimpleJMSTopic")
    private static Topic simpleJMSTopic;

    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Connection connection = null;
        Session session = null;
        Destination dest = simpleJMSTopic;
        MessageConsumer consumer = null;
        TextMessage message = null;
        
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(
                    false,
                    Session.AUTO_ACKNOWLEDGE
            );
            consumer = session.createConsumer(dest);
            connection.start();
            System.out.println("Ready to receive topic message");
            while (true) {
                Message m = consumer.receive();
                
                if (m != null) {
                    if (m instanceof TextMessage) {
                        message = (TextMessage) m;
                        System.out.println("Updated!: " + message.getText());
                    }
                    else {
                        break;
                    }
                }
                else {
                    break;
                }
            }
            
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }
        
    }
    
}
