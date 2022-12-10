import java.time.LocalDate;

public interface IGremien {
    public void setName(String Name);
    public void setOffiziell(Boolean Offiziell);
    public void setInoffiziell(Boolean Inoffiziell);
    public void setBeginn(LocalDate Beginn);
    public void setEnde(LocalDate Ende);

    public String getName();
    public Boolean getOffiziell();
    public Boolean getInoffiziell();
    public LocalDate getBeginn();
    public LocalDate getEnde();
}