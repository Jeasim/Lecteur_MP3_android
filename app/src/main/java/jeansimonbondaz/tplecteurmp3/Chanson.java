package jeansimonbondaz.tplecteurmp3;

public class Chanson
{
    private int id;
    private int index;
    private String titre;
    private String nomArtiste;
    private String album;
    private int duree;
    private String pochette;

    public Chanson(int id, int index, String titre, String nomArtiste, String album, int duree)
    {
        this.id = id;
        this.index = index;
        this.titre = titre;
        this.nomArtiste = nomArtiste;
        this.album = album;
        this.duree = duree;
    }

    public void setPochette(String pochette)
    {
        this.pochette = pochette;
    }

    public String getNomArtiste()
    {
        return nomArtiste;
    }
    public String getTitre(){ return titre;}
    public String getAlbum(){ return album;}
    public String getPochette(){ return pochette;}
    public int getDuree(){return duree;}
    public int getId(){return id;}
    public int getIndex(){return index;}


}
