package main;

import controller.PersonController;

public class Main {

	public static void main(String[] args) {
		PersonController controller = PersonController.getInstance();
		controller.init();
	}

}
