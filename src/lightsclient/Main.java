package lightsclient;

import lightsclient.*;

public class Main {

	public static void main(String[] args) {
		
		MyReader r = new MyReader();
		String[] dirs = r.checkPrefs();
		
		System.out.println(dirs[0] + " " + dirs[1]);
		//MainWindow.main(args);
	}

}
