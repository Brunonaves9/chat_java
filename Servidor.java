import java.net.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class Servidor extends Thread {

    // Atributos
    private static ArrayList<BufferedWriter>clientes;
    private static ServerSocket server;
    private String nome;
    private Socket con;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;
    

    /**
     *  Construtor da classe
     */
    public Servidor(Socket con)
    {
        this.con = con;
        try {
            in  = con.getInputStream();
            inr = new InputStreamReader(in);
            bfr = new BufferedReader(inr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método Run
     */
    public void run()
    {
        try {
            String msg;
            OutputStream ou = this.con.getOutputStream();
            Writer ouw = new OutputStreamWriter(ou);
            BufferedWriter bfw = new BufferedWriter(ouw);
            clientes.add(bfw);
            nome = msg = bfr.readLine();

            while(!"Sair".equalsIgnoreCase(msg) && msg != null) {
                msg= bfr.readLine();
                sendToAll(bfw, msg);
                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    /**
     * Método SendToAll (Envia para todos conectados)
     */
    public void sendToAll(BufferedWriter bwSaida, String msg) throws IOException
    {
        BufferedWriter bwS;

        for(BufferedWriter bw : clientes) {

            bwS = (BufferedWriter)bw;
            
            if ( !(bwSaida == bwS) ) {
                bw.write(nome + "-> " + msg + "\r\n");
                bw.flush();
            }
        }
    }
    
    /**
     * Main
     * @param args
     */
    public static void main(String []args)
    {
        try {
            
            JLabel lblMessage = new JLabel("Servidor: Porta");
            JTextField txtPorta = new JTextField("9001");
            Object[] texts = {lblMessage, txtPorta};
            JOptionPane.showMessageDialog(null, texts);
            server = new ServerSocket(Integer.parseInt(txtPorta.getText()));
            clientes = new ArrayList<BufferedWriter>();
            JOptionPane.showMessageDialog(null,"Servidor ativo na porta: "+
            txtPorta.getText());

            while(true){
                System.out.println("Aguardando conexão...");
                Socket con = server.accept();
                System.out.println("Cliente conectado...");
                Thread t = new Servidor(con);
                t.start();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}