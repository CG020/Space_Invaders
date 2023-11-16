package model;

import java.io.File;
import java.io.Serializable;
import java.net.URI;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundEffect {
	private String path;
	private String fileName;
	
	 public SoundEffect(String fileName) {
       path = "lib/" + fileName;
       this.fileName = fileName;
	 }
	 
	 public URI getSongURI() {
       File file = new File(path);
       return file.toURI();
	 }
	 
	 public void playSound() {
		 Media media = new Media(getSongURI().toString());
		    MediaPlayer mediaPlayer = new MediaPlayer(media);
		    mediaPlayer.play();
	 }

}
