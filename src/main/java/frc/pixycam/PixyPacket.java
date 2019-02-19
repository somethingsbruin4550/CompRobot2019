package frc.pixycam;

public class PixyPacket {
	byte[] syncBuffer = {(byte) 0xAE,(byte) 0xC1};
	byte type = 0;
	byte[] payload = new byte[9];
	boolean debugOn = false;

	//Default constructor : Not needed right now
	public PixyPacket() {

	}
	public PixyPacket(boolean debugOn) {
		this.debugOn = debugOn;
	}
	
	public byte[] getPacket() {
		//Creates the new array of bytes 
		byte[] packet = new byte[syncBuffer.length + 2 + payload.length];
		for(int i = 0;i < syncBuffer.length;i++) {
			packet[i] = syncBuffer[i];
		}
		packet[syncBuffer.length] = type;
		packet[syncBuffer.length + 1] = (byte) (((byte) payload.length) & (byte) 0xFF);
		for(int i = 0;i < payload.length;i++) {
			packet[syncBuffer.length + 2 + i] = payload[i];
		}
		
		return packet;
	}
	//Seperates all of the feilds of the packet
	//Oppisite of get packet
	public void importRecvPacket(byte[] data) {
		setType(data[2]); // set the type for the imported packet

		for(int i = 6; i < data.length; i++){
			payload[i - 6] = data[i];
		}
		if(debugOn){
			for(int i = 0; i < payload.length; i++){
				System.out.println("Payload " + i + ":" + payload[i]);
			}
		}
	}
	//Gets the type
	public byte getType() {
		return type;
	}
	//Sets the type 
	public void setType(byte type) {
		this.type = type;
	}
	//gets an array of payload 
	public byte[] getPayload(){
		return payload;
	}
	//Gets the byte payload length  
	public int getPayloadLength() {
		return payload.length;
	}
	//Sets the byte payload 
	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	/////////////////////////////////////////////////////////////
	// DATA PARSERS
	/////////////////////////////////////////////////////////////
/*
	public PixyFeature getFeatures() {
		if(type != 0x31){}
		
		byte[] data = new byte[1];
		return data; 
	}*/
}
