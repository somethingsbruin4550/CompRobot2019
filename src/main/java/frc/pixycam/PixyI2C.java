package frc.pixycam;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

/**
 * The I2C Implementation of PixyCam for WpiLib
 * 
 * Copyright 2019: team 4550 Somethings Bruin
 * 
 * Usage: Feel free to use this if you want, Just don't claim that it is yours
 * 
 * PORTING: Use this class as a blueprint for porting to other setups It should
 * be very self explanatory. The only thing that you should need to modify is
 * the ``` public PixyPacket request(PixyPacket packet, int returnDataLength)
 * ``` method
 */

public class PixyI2C extends PixyCam {
	Port port = Port.kOnboard;
	I2C I2CBus = null;

	public PixyI2C(byte address) {
		I2CBus = new I2C(port, address);
	}

	/**
	 * Makes a request over the I2C bus provided by
	 */

	@Override
	public PixyPacket request(PixyPacket packet, int returnDataLength, boolean debugOn) {
		// Extract the packet to send, This will setup all formating etc.
		byte[] packetBuffer = packet.getPacket();
		byte[] rawData = new byte[returnDataLength];

		if (debugOn) {
			// This is a debug printout, Don't worry about it
			System.out.println("Sent Data: " + packetBuffer.length);
			for (int i = 0; i < packetBuffer.length; i++) {
				byte num = packetBuffer[i];
				char[] hexDigits = new char[2];
				hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
				hexDigits[1] = Character.forDigit((num & 0xF), 16);
				System.out.println(new String(hexDigits));

			}
		}

		// Actually send and get data from the PixyCam
		I2CBus.transaction(packetBuffer, packetBuffer.length, rawData, rawData.length);

		if(debugOn){
			// Another Debug Printout
			System.out.println("Recieved Data: " + rawData.length);
			for (int i = 0; i < rawData.length; i++) {
				byte num = rawData[i];
				char[] hexDigits = new char[2];
				hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
				hexDigits[1] = Character.forDigit((num & 0xF), 16);
				System.out.println(new String(hexDigits));
			}
		}

		// Import the array to another packet
		PixyPacket recvPacket = new PixyPacket();
		recvPacket.importRecvPacket(rawData);
		return recvPacket;
	}
}
