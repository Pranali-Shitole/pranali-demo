import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;

import java.util.List;

public class PacketSniffer {
    public static void main(String[] args) {
        try {
            List<PcapNetworkInterface> interfaces = Pcaps.findAllDevs();
            if (interfaces == null || interfaces.isEmpty()) {
                System.out.println("No network interfaces found.");
                return;
            }
            System.out.println("Available Interfaces:");
            for (int i = 0; i < interfaces.size(); i++) {
                System.out.println(i + ": " + interfaces.get(i).getName() + " (" + interfaces.get(i).getDescription() + ")");
            }
            int index = 0;
            PcapNetworkInterface nif = interfaces.get(index);

        
            int snapLen = 65536;           
            int timeout = 10;              
            PcapHandle handle = nif.openLive(snapLen, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, timeout);

            System.out.println("Listening on " + nif.getName() + "...");
            PacketListener listener = new PacketListener() {
                public void gotPacket(Packet packet) {
                    System.out.println("Packet received at " + System.currentTimeMillis());
                    System.out.println(packet);
                    System.out.println("--------------------------------------------------");
                }
            };
            int maxPackets = 10;
            handle.loop(maxPackets, listener);
            handle.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

