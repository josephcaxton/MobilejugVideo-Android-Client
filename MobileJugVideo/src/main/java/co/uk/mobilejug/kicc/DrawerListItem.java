package co.uk.mobilejug.kicc;

/**
 * Created by joseph on 02/12/2013.
 */
public class DrawerListItem extends Object {

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    private String title;
    private int icon;
}
