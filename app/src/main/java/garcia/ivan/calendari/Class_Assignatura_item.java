package garcia.ivan.calendari;

public class Class_Assignatura_item {

    private String nom;
    private String[] grups;
    private int [] index;
    private int grup_escollit;
    private int n_grups;
    private int numEntero;

    public Class_Assignatura_item(String assignatura) {

        String[] parts = assignatura.split(";");
        // Ivan: Es parteix String amb la informació per construir l'assignatura, la primera part és el nom
        nom=parts[0];
        grup_escollit = 0;
        // Ivan: Es guarda la quantitat de grups que té l'assignatura
        n_grups=(parts.length-1)/2;
        grups = new String[n_grups];
        index = new int[n_grups];
        // Ivan: Depenent de la quantitat de grups, es guarda el nom del grup (part[1]) i l'index(part[2])
        for(int i=0; i<n_grups; i++) {
            grups[i]= parts[(i*2)+1];
            numEntero = Integer.parseInt(parts[(i*2)+2]);
            index[i]= numEntero;
        }
    }

    public String getNom() {
        return nom;
    }

    public String[] getGrups() {
        return grups;
    }

    public int getIndex(int grup_escollit) {
        return index[grup_escollit-1];
    }

    public int getGrup_escollit() {
        return grup_escollit;
    }

    public void setGrup_escollit(int grup_escollit) {
        this.grup_escollit = grup_escollit;
    }

    public int getN_grups() {
        return n_grups;
    }

}

