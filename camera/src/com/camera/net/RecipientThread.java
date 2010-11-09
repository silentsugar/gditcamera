package com.camera.net;

public abstract class RecipientThread extends Thread {

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
