import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

/*
 * This program uses a jNetPcap library which is an open source java library used
 * to capture and decode network packets. It uses native implementations to provide
 * optimum packet decoding performance.
 * 
 * It also uses Nmap which is windows packet capture library that has been installed
 * on this Windows machine. The download is located at: https://nmap.org/npcap/windows-10.html
 */

public class PacketAnalysis {

	public static void main(String[] args) {
		List<PcapIf> devices = new ArrayList<PcapIf>(); //List used for available NIC's
		StringBuilder errors = new StringBuilder();     //Used to hold error messages
		
		int r = Pcap.findAllDevs(devices, errors);		//Populate 'devices' list with NIC's
		if (r != Pcap.OK) {
			System.err.printf("Can't read list of devies, error is %s", errors.toString());
			return;
		}
		
		System.out.println("Network devices found:");
		
		int i = 0;
		for (PcapIf device : devices) {					//Output list of NIC's for user selection
			String description = (device.getDescription() != null) ? device.getDescription() :
				 "No description available";
			System.out.printf("#%d: %s [%s]\n", i++, device.getName(), description);
		}
		
		System.out.println("Choose a deivce from the above list of avilable devices:");
		@SuppressWarnings("resource")
		int ch = new Scanner(System.in).nextInt();		//User input for which NIC to open
		PcapIf device = devices.get(ch);
		
		//Parameters used in the packet capture descriptor to look at packets
		int snaplength = 64 * 1024;
		int flags = Pcap.MODE_PROMISCUOUS;
		int timeout = 1000* 1000;
		//Opens the packet capture descriptor on the selected device
		Pcap pcapTcp = Pcap.openLive(device.getName(), snaplength, flags, timeout, errors);
		
		if (pcapTcp == null) {
            System.err.printf("Error while opening device for capture: "
                    + errors.toString());
            return;
		}
		System.out.println("Device opened.");
		//Packethandler to handle the packets captured. A TCP object is created to check if
		//header contains a TCP header. Prints out required header fields
		PcapPacketHandler<String> jpacketHandlerTcp = new PcapPacketHandler<String>() {
			Tcp tcp = new Tcp();
			
			@Override
			public void nextPacket(PcapPacket packet, String user) {
				if (packet.hasHeader(tcp)) {
					System.out.println("<----------TCP---------->");
					System.out.println("Source Port: " + tcp.source());
					System.out.println("Destination Port: " + tcp.destination());
					System.out.println("Sequence Number: " + tcp.seq());
					System.out.println("Acknowledgement: " + tcp.ack());
					System.out.println("TCP Header Length: " + tcp.getHeaderLength());
					System.out.println();
				}
			}
		};
		//Loops the PacketHandler until no more than 5 packets are captures or before a timeout
		//threshold as been reached
		pcapTcp.loop(5, jpacketHandlerTcp, "jNetPcap");
		pcapTcp.close();
		
		Pcap pcapUdp = Pcap.openLive(device.getName(), snaplength, flags, timeout, errors);

		//Packethandler to handle the packets captured. A UDP object is created to check if
		//header contains a UDP header. Prints out required header fields
		PcapPacketHandler<String> jpacketHandlerUdp = new PcapPacketHandler<String>() {
			Udp udp = new Udp();
			
			@Override
			public void nextPacket(PcapPacket packet, String user) {
				if (packet.hasHeader(udp)) {
					System.out.println("<----------UDP---------->");
					System.out.println("Source Port: " + udp.source());
					System.out.println("Destination Port: " + udp.destination());
					System.out.println("Length: " + udp.length());
					System.out.println("Check Sum: " + udp.checksum());
					System.out.println();
				}
			}
		};
		//Loops the PacketHandler until no more than 5 packets are captures or before a timeout
		//threshold as been reached
		pcapUdp.loop(5, jpacketHandlerUdp, "jNetPcap");
		pcapUdp.close();
		
		Pcap pcapIP = Pcap.openLive(device.getName(), snaplength, flags, timeout, errors);
		
		//Packethandler to handle the packets captured. A IP4 object is created to check if
		//header contains a IP4 header. Prints out required header fields
		PcapPacketHandler<String> jpacketHandlerIp = new PcapPacketHandler<String>() {
			Ip4 ip4 = new Ip4();
			
			@Override
			public void nextPacket(PcapPacket packet, String user) {
				if (packet.hasHeader(ip4)) {
					System.out.println("<----------IP---------->");
					System.out.println("Version: " + ip4.version());
					System.out.println("IP Header Length: " + ip4.getHeaderLength());
					System.out.println("TTL: " + ip4.ttl());
					System.out.println("Protocol: " + ip4.getByte(9));
					System.out.println("Source Address: " + org.jnetpcap.packet.format.FormatUtils.ip(ip4.source()));
					System.out.println("Destination Address :" + org.jnetpcap.packet.format.FormatUtils.ip(ip4.destination()));
					System.out.println();
				}
			}
		};
		//Loops the PacketHandler until no more than 5 packets are captures or before a timeout
		//threshold as been reached
		pcapIP.loop(5, jpacketHandlerIp, "jNetPcap");
		pcapIP.close();
	}

}
