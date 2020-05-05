package Ftp;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import java.awt.*;  
import java.awt.event.*;

public class SocketSwing1 extends Frame
{
	private static final long serialVersionUID = 1L;
	public final static int SOCKET_PORT = 2004;
	public static String FILE_NAME = null; 
	static File selectedFile=null;
	static JFrame f= new JFrame();
	static JTextArea tf2=new JTextArea();
	static JScrollPane jscp=new JScrollPane(tf2);
	
	/* button2 thread */
	class B2aThread extends Thread
	{
		private String FILE_NAME_SIZE = null;
		private int FILE_SIZE = 0;
		private BufferedInputStream input;
		private DataInputStream dIn;
		
		public void run()
		{
			
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			OutputStream os = null;
			ServerSocket servsock = null;
			Socket sock = null;
			int bytesRead2,bytesRead3;			
			try 
			{
				servsock = new ServerSocket(SOCKET_PORT);
					
				while (true) 
				{
					
					tf2.append("Waiting.....\n");
					try 
					{
						sock = servsock.accept();
						tf2.append("\nAccepted connection:"+"\n");
						tf2.append(sock.toString()+"\n");
						
						dIn = new DataInputStream(sock.getInputStream());
						input= new BufferedInputStream(sock.getInputStream());
						
						int length = dIn.read();
						//tf2.append(String.valueOf(length));
						byte[] buffer = new byte[length]; 
						int bytesRead = 0;
						bytesRead=input.read(buffer);
					    String terminal_id_serial_id = new String(buffer,0,bytesRead);
					    tf2.append(terminal_id_serial_id+"\n");
					  /*  int a[]=new int[2];
					    String[] split_terminal_id_serial_id=terminal_id_serial_id.split(",");
					    for(int i=0;i<2;i++)
					    {
					    	a[i]=Integer.parseInt(split_terminal_id_serial_id[i]);
					    }
					    String terminal_id=String.valueOf(a[0]);
					    String serial_id=String.valueOf(a[1]);*/
						byte[] mybytearray = new byte[(int) selectedFile.length()];
						fis = new FileInputStream(selectedFile);
						bis = new BufferedInputStream(fis);
						bis.read(mybytearray, 0, mybytearray.length);
						os = sock.getOutputStream();
						//System.out.println("Sending " + selectedFile + "(" + mybytearray.length + " bytes)");
						tf2.append("Sending " + selectedFile + "(" + mybytearray.length + " bytes)"+"\n");
						tf2.append(selectedFile.toString()+"\n");
						String fileLengthByteArray;
						int fileLengthInt = mybytearray.length;
						fileLengthByteArray = Integer.toString(fileLengthInt);
						byte[] b = fileLengthByteArray.getBytes();
						tf2.append("\n"+fileLengthByteArray+"\n");
						String bn= selectedFile.getName();
						byte[] fileName=bn.getBytes();
						
						os.write(fileName, 0,fileName.length);
					//	TimeUnit.SECONDS.sleep(1);
					//	int respoFilename=dIn.read();
					//	TimeUnit.SECONDS.sleep(1);
					//	tf2.append(String.valueOf(respoFilename)+"\n");
					//	byte[] buffer2 = new byte[respoFilename]; 
					//	int byteRespo =0;
					//= input.read(buffer2);
						String respo_Filename="ok";
						tf2.append(bn+"Sent"+"\n");
					//	tf2.append(respo_Filename+"\n");
						boolean respo=true;
						if(respo_Filename=="ok")
						{
							TimeUnit.SECONDS.sleep(1);
							os.write(b, 0, b.length);
							//int respoFileLength=dIn.read();
							//byte [] buffer3=new byte[2];
							//int byteFileLengthrespo=input.read(buffer3);
							String respo_FileLength="ok";
							tf2.append(fileLengthByteArray+"Sent"+"\n");
							if(respo_FileLength=="ok")
							{
								TimeUnit.SECONDS.sleep(1);
								os.write(mybytearray, 0, mybytearray.length);
								TimeUnit.SECONDS.sleep(1);
							//	String respo_mybytearray=dIn.readLine();
							//	byte [] buffer4=new byte[Integer.parseInt(respo_mybytearray)];
							//	int bytemybytearray=input.read(buffer4);
								String respo_strmybytearray="ok";
								tf2.append(selectedFile+" "+String.valueOf(mybytearray.length)+"Sent"+"\n");
							}
						}
						else
						{
							tf2.append("FileName not sent");
						}
						
						if(respo==true)
						{
							FILE_NAME_SIZE = dIn.readLine();
							byte [] mybytearray2  = new byte [Integer.parseInt(FILE_NAME_SIZE)];
							tf2.append(FILE_NAME_SIZE);
							bytesRead2=input.read(mybytearray2);
							FILE_NAME = new String(mybytearray2,0,bytesRead2);
							FILE_SIZE = dIn.readInt();
							//tf2.append(FILE_NAME +"\n"+ Integer.toString(FILE_SIZE));
							byte [] mybytearray3  = new byte [FILE_SIZE];
							
							bytesRead3=input.read(mybytearray3);
							
							String LOG = new String(mybytearray3,0,bytesRead3);
							File file = new File(FILE_NAME);
							FileWriter fileWriter = new FileWriter(file);
						    PrintWriter printWriter = new PrintWriter(fileWriter);
						    printWriter.print(LOG);
						    printWriter.close();
							os.flush();
							tf2.append("\nDone"+"\n");
							tf2.append("\n");
						}
						else
						{
							tf2.append("Log file is not received\n");
						}
						
					} 
					catch (InterruptedException e1)
					{
						e1.printStackTrace();
						tf2.append(e1.toString());
					} 
					finally 
					{
						if(input != null)
						{
							input.close();
						}
						if (bis != null)
						{
							bis.close();
						}
						if (os != null)
						{
							os.close();
						}
						if (sock != null)
						{
							sock.close();
						}
					}
				}
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
				tf2.append(e1.toString()+"\n");
			} 
			finally 
			{
				if (servsock != null)
				{
					try
					{
						servsock.close();
						run();
					} 
					catch (IOException e1)
					{
						e1.printStackTrace();
						tf2.append(e1.toString()+"\n");
					}
				}
			}
		}
	}
	
	/* Main Program */
	public static void main(String[] args) throws IOException 
	{	
		SocketSwing1 ss1=new SocketSwing1();
		SocketSwing1.B2aThread b2athread=ss1.new B2aThread();
		
		f.setFont(new Font("Cutive Mono", Font.PLAIN, 20));
		JTextField tf=new JTextField(); 
		tf.setBounds(160,50, 500,30);
		JButton b1=new JButton("Select File");
		JButton b2=new JButton("Transfer File");
		JButton b3=new JButton("End Command");
		b1.setBounds(50,50,100,30);

		b1.addActionListener(new ActionListener()
		{  
			public void actionPerformed(ActionEvent e)
			{
				
			    JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int returnValue = jfc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) 
				{
					selectedFile = jfc.getSelectedFile();
				}
			    tf.setText(selectedFile.getAbsolutePath());  
			}  
		});
		
		jscp.setBounds(160,120, 500, 200);
		tf2.setBounds(160,150, 500, 200);	
		b2.setBounds(50,150, 100, 30);
		b2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				b2athread.start();
			}
		});
		
		b3.setBounds(50, 200, 100, 30);
		b3.addActionListener(new ActionListener()
		{
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e)
			{
				try 
				{
					TimeUnit.SECONDS.sleep(1);
					b2athread.stop();
					tf2.append("Temination Closed........... \n");
				} 
				catch (InterruptedException e1) 
				{
					e1.printStackTrace();
				}
			}
		});
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(b1);
		f.add(tf);
		f.add(b2);
		f.add(b3);
		f.add(jscp);
		
		f.setSize(700, 700);
		f.setLayout(null);  
	    f.setVisible(true);  
	}
}