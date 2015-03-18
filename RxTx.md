Utilisation de RxTx

http://rxtx.qbang.org/wiki/index.php/Main_Page

put rxtxparallel.dll and rxtxserial.dll into c:/program files/java/jdk1.5.0\_02/jre/bin
put rxtxcomm.jar into c:/program files/java/jdk1.5.0\_02/jre/lib/ext

Version 64 bits :

http://www.cloudhopper.com/opensource/rxtx/


From http://www.kuligowski.pl/java/rs232-in-java-for-windows,1

PREPARING ECLIPSE ENVIRONMENT
Create new Java project
The first thing you need to do is creating new Java project in Eclipse (for example called JavaSerial).

Download and install RXTX library
Go to the RXTX download page and get the newest version of RXTX binaries compressed in ZIP file and destined for Windows operating system.

Go to the home directory of your recently created JavaSerial Project and create new directory called lib. Unzip downlowded RXTX package into temporary directory and copy RXTXcomm.jar, rxtxSerial.dll and rxtxParallel.dll files into lib directory. Dll files are located in Windows/i368-mingw32 directory of RXTX library.

Refresh JavaSerial project by hitting F5 key. Next click the right mouse button on the JavaSerial project and go to the Properties/Java Build Path. Select Libraries tab and click Add JARs button. Select RXTXcomm.jar and just click OK button.

Extend project classpath
If you want to run your JavaSerial project from Eclipse by clicking Run button you should enclose in classpath all copied **.dll files as a native libraries. To do this click the right mouse button on JavaSerial project and go to the Properties/Java Build Path. Select Source tab and expand JavaSerial/src tree element. Click on Native library location: (None) and click Edit button. After the Native Library Folder Configuration dialog window appears click on Workspace button and select JavaSerial/lib folder. After all start clicking OK buttons until you back to the begining.**

Congartulations! Now you are ready to run and test you JavaSerial project in Eclipse.

Test your environment
It's time to make simple test if the created Eclipse environment is properly configured to run applications based on RXTX library for Windows.

Create new class named ListAvailablePorts.java and paste following code.

import gnu.io.CommPortIdentifier;

import java.util.Enumeration;

public class ListAvailablePorts {

> public void list() {
> > Enumeration ports = CommPortIdentifier.getPortIdentifiers();


> while(ports.hasMoreElements())
> > System.out.println(((CommPortIdentifier)ports.nextElement()).getName());

> }

> public static void main(String[.md](.md) args) {
> > new ListAvailablePorts().list();

> }
}
Run your simple application by clicking Run button and if the environment is configured correctly you should see following output in console window:

Stable Library

Native lib Version = RXTX-2.1-7
Java lib Version   = RXTX-2.1-7
COM1
LPT1
Number of listed ports depends on number of available ports in your operating system. If you do not see presented output please repeat configuration process again.

PHYSICAL EQUIPMENT
The choise of physical equipment which you need to use during development of RS232 communication application depends on your needs and capabilities of your avialable equipment. You should ask yourself following questions:

do I have a RS232 specification of communication protocol between end-point device and application which I develop?
is my laptop or PC equipped with serial ports (one or two?)
do I have access to the end-point device or maybe I should write an emulator of end-point device?
Assuming that you have no access to the end-point device and you have only specification of RS232 communication protocol between device and application one of two following solutions should be considered.

Two serial ports wired together
If your laptop or PC is equipped with two serial ports you can simply buy very cheap DB9-DB9 cable and connect two serial ports together. You will be able to connect end-point device software emulator to the one of serial ports available in the operating system and the application to the second of available serial ports.

Virtual Serial Port Driver 6.0
If you don't have any serial port in your laptop or PC you should use one of available serial port emulators or simply buy USB adapter (if you have USB port available).

Virtual Serial Port Driver 6.0 is an excellent commercial serial port emulator for Windows operating system family which allows you to create pairs of serial ports. Serial ports within each pair could be connected together through the virutal RS232 wire. Then you can connect to the first emulated serial port an end-point device emulator and to the second serial port the application making a communication between these parts. It is great solution if you don't have access to the device to which you develop RS232 communication software and if you don't have any serial port on your laptop or PC - the only thing you should have is a specification of RS232 communication between device and application.

RS232 APPLICATION EXAMPLE
Now we develop sample application based on RXTX library. Presented example will be very simple to show important communication issues and best practices. You can extend this sample to create more sophisticated applications.

Example communiaction main class
Following steps should be performed to establish serial port connection:

receive CommPortIdentifier from the system with specific name (e.g. COM1) - received identifier could be in use by any other application - check it invoking portIdentifier.isCurrentlyOwned() method
if received CommPortIdentifier is not in use by different application, you can open CommPort (which in our case is instance of SerialPort)
the next step is to pass all setup communication parameters: baudrate, number of data bits, number of stop bits and possible parity bit
the last step is to retrieve InputStream and OutputStream for sending and receiving raw bytes
As shown below for receiving raw bytes it is started new Thread which takes as parameter InputStream from serial port.

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class RS232Example {

> public void connect(String portName) throws Exception {
> > CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);


> if (portIdentifier.isCurrentlyOwned()) {
> > System.out.println("Port in use!");

> } else {
> > // points who owns the port and connection timeout
> > SerialPort serialPort = (SerialPort) portIdentifier.open("RS232Example", 2000);


> // setup connection parameters
> serialPort.setSerialPortParams(
> > 38400, SerialPort.DATABITS\_8, SerialPort.STOPBITS\_1, SerialPort.PARITY\_NONE);


> // setup serial port writer
> CommPortSender.setWriterStream(serialPort.getOutputStream());

> // setup serial port reader
> new CommPortReceiver(serialPort.getInputStream()).start();
> }
> }

> public static void main(String[.md](.md) args) throws Exception {

> // connects to the port which name (e.g. COM1) is in the first argument
> new RS232Example().connect(args[0](0.md));

> // send HELO message through serial port using protocol implementation
> CommPortSender.send(new ProtocolImpl().getMessage("HELO"));
> }
}
Serial port data receiver
As you can see CommPortReceiver is a Thread instance with run() implemented method which is performed in inifite loop until java application is broken by the user.

While there is opened InputStream by the end-point device in.read() method blocks and waits for incoming raw bytes. Each byte is passed to the protocol manager which handles all protocol issues (for example recognizes messages from raw bytes). When end-point device breaks the opened InputStream then in.read() method starts to return -1 value without blocking. Because waiting for external device in infinite loop without in.read() blocking method could be destructive for performence of operating system sleep method is introduced to decrease number of in.read() executions.

import java.io.IOException;
import java.io.InputStream;

public class CommPortReceiver extends Thread {

> InputStream in;
> Protocol protocol = new ProtocolImpl();

> public CommPortReceiver(InputStream in) {
> > this.in = in;

> }

> public void run() {
> > try {
> > > int b;
> > > while(true) {


> // if stream is not bound in.read() method returns -1
> while((b = in.read()) != -1) {
> > protocol.onReceive((byte) b);

> }
> protocol.onStreamClosed();

> // wait 10ms when stream is broken and check again
> sleep(10);
> }
> } catch (IOException e) {
> > e.printStackTrace();

> } catch (InterruptedException e) {
> > e.printStackTrace();

> }
> }
}
Protocol
Protocol is used to handle all incoming bytes, segregate them and create recognized messages. The interface of Protocol implementation allows to inform protocol manager about each incoming byte and about broken stream situation.

public interface Protocol {

> // protocol manager handles each received byte
> void onReceive(byte b);

> // protocol manager handles broken stream
> void onStreamClosed();
}
In our case protocol implementation is simple: message is always ended with new line character. Colleaction of received bytes creates String message which could be recognized.

When new message is recognized (new line character appears or stream closes) then onMessage() method is called to handle recognized message. If you develop more complicated example (and I am sure you will do) logic of triggered actions depending on recognized message should be placed in external class (some kind of interpreter).

In our case when HELO message comes then OK message should be sent as a response. If OK message is received then acknowledge should be sent as a response (OK ACK).

public class ProtocolImpl implements Protocol {

> byte[.md](.md) buffer = new byte[1024](1024.md);
> int tail = 0;

> public void onReceive(byte b) {
> > // simple protocol: each message ends with new line
> > if (b=='\n') {
> > > onMessage();

> > } else {
> > > buffer[tail](tail.md) = b;
> > > tail++;

> > }

> }

> public void onStreamClosed() {
> > onMessage();

> }

> /*** When message is recognized onMessage is invoked
    * 
> private void onMessage() {
> > if (tail!=0) {
> > > // constructing message
> > > String message = getMessage(buffer, tail);
> > > System.out.println("RECEIVED MESSAGE: " + message);**


> // this logic should be placed in some kind of
> // message interpreter class not here
> if ("HELO".equals(message)) {
> > CommPortSender.send(getMessage("OK"));

> } else if ("OK".equals(message)) {
> > CommPortSender.send(getMessage("OK ACK"));

> }
> tail = 0;
> }
> }

> // helper methods
> public byte[.md](.md) getMessage(String message) {
> > return (message+"\n").getBytes();

> }

> public String getMessage(byte[.md](.md) buffer, int len) {
> > return new String(buffer, 0, tail);

> }
}
Serial port data sender
import java.io.IOException;
import java.io.OutputStream;

public class CommPortSender {

> static OutputStream out;

> public static void setWriterStream(OutputStream out) {
> > CommPortSender.out = out;

> }

> public static void send(byte[.md](.md) bytes) {
> > try {
> > > System.out.println("SENDING: " + new String(bytes, 0, bytes.length));


> // sending through serial port is simply writing into OutputStream
> out.write(bytes);
> out.flush();
> } catch (IOException e) {
> > e.printStackTrace();

> }
> }
}
Running RS232 example
In order to run serial port communication example you need to run two instances of developed application:

the first as an end-point device which is connected to the port COM1
the second as the application connected to the port COM2
Moreover you have to have connected COM1 and COM2 port together (using DB9-DB9 cable or using virtual wire from emulating serial port software).

Follow steps below to run two instance of application in Eclipse environment with different command line parameters:

Click right mouse button on RS232Example class and select Run As/Open Run Dialog
Choose Arguments tab and type in Program arguments text area the first parameter COM1
Click Apply button and then Run button
You should see following output in output console which means that the first instance has been strated and sent HELO command to the not bound serial port.

Stable Library

Native lib Version = RXTX-2.1-7
Java lib Version   = RXTX-2.1-7
SENDING: HELO
Next repeat above steps but type COM2 in Program arguments text area. Switch between output consoles using button with monitor icon and compare results. You should have two output consolse with results as presented below.

The first console (COM1 - 1 instance)
After the second instance had been started HELO message was received. Then OK message was sent as a response. Then acknowledge of OK message was received.

Stable Library

Native lib Version = RXTX-2.1-7
Java lib Version   = RXTX-2.1-7
SENDING: HELO

RECEIVED MESSAGE: HELO
SENDING: OK

RECEIVED MESSAGE: OK ACK
The second console (COM2 - 2 instance)
The second instance sent HELO message receiving OK message. Then acknowledge of OK message was sent to the first instance.

Stable Library

Native lib Version = RXTX-2.1-7
Java lib Version   = RXTX-2.1-7
SENDING: HELO

RECEIVED MESSAGE: OK
SENDING: OK ACK
CONCLUSIONS
When you receive information about that Sun doesn't support serial communication for Windows system environment you starts desperately searching alternative. In consequence there is very good free alternative which is used in several projects (and works fine) - RXTX library. Moreover you can in simple way prepare development environment to work on your project effectively not having end-point device or even serial ports installed in your laptop or PC.