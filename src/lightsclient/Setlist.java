package lightsclient;

import java.util.ArrayList;

public class Setlist {

	private ArrayList<Song> songs;
	
	// initialize variables
	public Setlist() {
		songs = new ArrayList<Song>();
	}
	
	public void addSong(Song s) {
		songs.add(s);
	}
	
	public void removeSong(int index) {
		songs.remove(index);
	}
	
	public void removeSong(String title) {
		String[] titles = getSongTitles();
		
		for (int i = 0; i < titles.length; i++) {
			if (titles[i].equals(title)) {
				songs.remove(i);
			}
		}
	}
	
	public Song getSong(int index) {
		return songs.get(index);
	}
	
	public Song getSong(String title) {
		String[] titles = getSongTitles();
		
		for (int i = 0; i < titles.length; i++) {
			if (titles[i].equals(title)) {
				return songs.get(i);
			}
		}
		
		return null;
	}
	
	public String[] getSongTitles() {
		
		ArrayList<String> songNames = new ArrayList<String>();
		
		for (int i = 0; i < songs.size(); i++) {
			songNames.add(songs.get(i).toString());
		}
		
		String[] ret = new String[songNames.size()];
		ret = songNames.toArray(ret);
		return ret;
	}
	
	public String getSingleSongTitle(int index) {
		return songs.get(index).toString();
	}
	
	public void restart() {
		songs = new ArrayList<Song>();
	}

	public void addSongs(Song[] s) {
		for (int i = 0; i < s.length; i++) {
			songs.add(s[i]);
		}
	}
	
	public void reorder(int[] indecies) {
		ArrayList<Song> newSongs = new ArrayList<Song>();
		
		for (int i = 0; i < indecies.length; i++) {
			newSongs.add(songs.get(i));
		}
		
		songs = newSongs;
	}
	
	public void reorder(String[] titles) {
		ArrayList<Song> newSongs = new ArrayList<Song>();
		
		for (int i = 0; i < titles.length; i++) {
			// find index of current title
			String currentTitle = titles[i];
			for (int j = 0; j < songs.size(); j++) {
				if (currentTitle.equals(songs.get(j).toString())) {
					newSongs.add(songs.get(j));
				}
			}
		}
		
		songs = newSongs;
	}

}
