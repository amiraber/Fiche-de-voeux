package DTO;


import java.util.List;

public class ChoixDTO {
    private ModuleDTO module;
    private String semestre;
    private List<String> nature;

    public ModuleDTO getModule() { return module; }
    public void setModule(ModuleDTO module) { this.module = module; }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public List<String> getNature() { return nature; }
    public void setNature(List<String> nature) { this.nature = nature; }
}
