package DTO;

public class ChoixDTO {
	
	private String palier;
    private String specialite;
    private String module;
    private String nature; // "Cours", "TD", "TP"
	public String getPalier() {
		return palier;
	}
	public void setPalier(String palier) {
		this.palier = palier;
	}
	public String getSpecialite() {
		return specialite;
	}
	public void setSpecialite(String specialite) {
		this.specialite = specialite;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getNature() {
		return nature;
	}
	public void setNature(String nature) {
		this.nature = nature;
	}
    
    

}
