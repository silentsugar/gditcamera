package com.camera.util;

public abstract class SenderThread extends Thread {

	private boolean stop = false;
	
	@Override
	public void run() {
		while(!stop){
			action();
			if(stop){
				break;
			}
		}
	}
	
	public abstract void action();
}
