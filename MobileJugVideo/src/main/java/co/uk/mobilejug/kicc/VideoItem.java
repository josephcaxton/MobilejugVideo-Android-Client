package co.uk.mobilejug.kicc;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class VideoItem  extends Object implements Parcelable {

    private String error;
	private int ID;
	private String Title;
	private String Description;
    private String DateUploaded;
    private String URN;
    private String ImageLocation;
	private String Commercial;
    private String _errorMessage;


    public VideoItem(String error,int ID,String Title, String Description,String DateUploaded, String URN, String ImageLocation, String Commercial){

       this.error = error;
       this.ID = ID;
       this.Title = Title;
       this.Description = Description;
       this.DateUploaded = DateUploaded;
       this.URN = URN;
       this.ImageLocation = ImageLocation;
       this.Commercial = Commercial;
    }
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

 @Override
    public int describeContents(){
               return 0;
          }

@Override
    public void writeToParcel(Parcel dest, int flags){
     dest.writeString(error);
     dest.writeInt(ID);
     dest.writeString(Title);
     dest.writeString(Description);
     dest.writeString(DateUploaded);
     dest.writeString(URN);
     dest.writeString(ImageLocation);
     dest.writeString(Commercial);



    }
    public static final Parcelable.Creator< VideoItem> CREATOR = new Parcelable.Creator< VideoItem>() {

@Override
        public  VideoItem createFromParcel(Parcel in) {
            String error = in.readString();
            int ID = in.readInt();
            String Title = in.readString();
            String Description = in.readString();
            String DateUploaded = in.readString();
            String URN = in.readString();
            String ImageLocation = in.readString();
            String Commercial = in.readString();
            return new  VideoItem(error, ID, Title, Description,DateUploaded,URN, ImageLocation,Commercial);
        }

@Override
        public  VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };


}
