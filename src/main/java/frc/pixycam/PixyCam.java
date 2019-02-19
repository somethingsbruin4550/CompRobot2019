package frc.pixycam;

/**

	PixyCam

	This class is used as the basis for all of the pixy cam logic
	see the PixyI2C class for actual transmission of packets

	Pixy Docs : https://docs.pixycam.com/wiki/doku.php?id=wiki:v2:protocol_reference

 */


public abstract class PixyCam {

	boolean debugOn = false;

	public PixyCam(){

	}
	public PixyCam(boolean debugOn) {
		this.debugOn = debugOn;
	}

	/**
		To Be Implemented by an exending class

		It must implement tx and rx
	 */
	public abstract PixyPacket request(PixyPacket packet, int returnDataLength, boolean debug);

	/**
		The exact same thing as request(packet, returnDataLength, debug) but without a debug option, defualts to true
	 */

	public PixyPacket request(PixyPacket packet, int returnDataLength) {
		return request(packet, returnDataLength, true);
	}

	//P

	/**
		Turns on/off the upper or lower lamps
	 */

	public void setLamp(boolean upper, boolean lower) {
		PixyPacket packet = new PixyPacket(true);
		packet.setType((byte) 0x16);
		byte[] data = {(byte) (upper?0x01:0x00), (byte) (lower?0x01:0x00)};
		packet.setPayload(data);
		request(packet, 10);
	}

	/**
	
		Sets the lower (only) RGB led to whatever color you want
	
	 */

	public void setLED(byte R, byte G, byte B) {
		PixyPacket packet = new PixyPacket();
		packet.setType((byte) 0x14);
		byte[] data = {R, G, B};
		packet.setPayload(data);
		request(packet, 0);
	}

	/**

		Gets the grayscale value of a pixel on the camera
		@return int between 0-255
	 */

	public int getGrayscale(int x, int y, boolean saturate){
		PixyPacket packet = new PixyPacket();
		packet.setType((byte) 0x70);
		byte[] data = {(byte) x, (byte) y, (byte) (saturate?0x01:0x00)};
		packet.setPayload(data);
		packet = request(packet, 7);
		int pixelData = ((int) (packet.payload[0] + packet.payload[1] + packet.payload[3]) / 3);

		return pixelData;
	}

	/**
		Get the resolution for the images
		@return int array holding resolution x (int[0]) and y (int[1])
	 */

	public int[] getResolution(){
		PixyPacket packet = new PixyPacket();
		packet.setType((byte) 0x0D);
		byte[] data = {0x00}; //This data has no use and is "reserved for future versions"
		packet.setPayload(data);
		packet = request(packet,2);
		//Parsing Resolution from returned data:
		int[] resolution = {(int) (packet.payload[0] + packet.payload[1]), (int) (packet.payload[2] + packet.payload[3])};
		return resolution;
	}

	/**
		Gets an image from the pixycam using getResolution and getGrayscale
		@return 2D int array holding the grayscale values of each pixel
	 */

	public int[][] getImage(){
		int[] resolution = getResolution();
		int[][] image = new int[resolution[0]][resolution[1]]; //int[0] = x, int [1] = y
		
		for(int x = 0; x < resolution[0]; x++){
			for(int y = 0; y < resolution[1]; y++){
				image[x][y] = getGrayscale(x, y, false);
			}
		}
		return image;
	}

	/*
		Gets all features (Lines, vectors, etc)
	*/

	public void getLines() {
		PixyPacket packet = new PixyPacket();
		packet.setType((byte) 0x30);
		byte[] data = {(byte) 0x00, (byte) 0x04};
		packet.setPayload(data);
		request(packet, 50);
	}

}
