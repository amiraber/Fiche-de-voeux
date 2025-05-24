package DTO;


import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChoixDTO {
    private ModuleDTO module;
    private String semestre;
    private List<String> nature;
    @JsonProperty(required = true)
    private Integer numChoix;

    public Integer getNumChoix() {
        return numChoix;
    }

    public void setNumChoix(Integer numChoix) {
        this.numChoix = numChoix;
    }


    public ModuleDTO getModule() { return module; }
    public void setModule(ModuleDTO module) { this.module = module; }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public List<String> getNature() { return nature; }
    public void setNature(List<String> nature) { this.nature = nature; }
}
