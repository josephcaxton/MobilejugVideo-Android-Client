package co.uk.mobilejug.kicc;

import java.util.Date;

public class VideoItem  extends Object {
	private int ID;
	private String Title;
	private String Description;
    private Date DateUploaded;
    private String URN;
    private String ImageLocation;
	private String Commercial;

	
	
	public int getID() {
		return ID;
	}
    public void setID(int id) {
        ID = id;
    }
    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
    }
    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }

	public Date getDateUploaded() {
		return DateUploaded;
	}
	public void setDateUploaded(Date dateuploaded) {
        DateUploaded = dateuploaded;
	}
    public String getURN() {
        return URN;
    }
    public void setURN(String urn) {
        URN = urn;
    }


	public String getImageLocation() {
		return ImageLocation;
	}
	public void setImageLocation(String imagelocation) {
		ImageLocation = imagelocation;
	}

    public String getCommercial() {
        return Commercial;
    }
    public void setCommercial(String commercial) {
        Commercial = commercial;
    }
	
	

}
