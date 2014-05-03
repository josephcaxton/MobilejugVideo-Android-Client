package co.uk.mobilejug.kicc;

import org.json.JSONObject;

public class VideoItem  extends Object {

    private String error;
	private int ID;
	private String Title;
	private String Description;
    private String DateUploaded;
    private String URN;
    private String ImageLocation;
	private String Commercial;
    private String _errorMessage;


    public VideoItem(JSONObject videoitem){

        initFromJson(videoitem);
    }

    protected void initFromJson(JSONObject videoitem){

        _errorMessage = null;
        try{

            ID = videoitem.getInt("ID");
            Title = videoitem.getString("Title");
            Description = videoitem.getString("Description");
            DateUploaded = videoitem.getString("DateUploaded");
            URN = videoitem.getString("URN");
            ImageLocation = videoitem.getString("ImageLocation");

        }
        catch (Exception ex){

            _errorMessage = ex.getMessage();
        }


    }

    public String getError() {
        return error;
    }
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
    public void setError(String Error) {
        error = Error;
    }
    public void setDescription(String description) {
        Description = description;
    }

	public String getDateUploaded() {
		return DateUploaded;
	}
	public void setDateUploaded(String dateuploaded) {
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
