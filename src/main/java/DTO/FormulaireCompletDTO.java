package DTO;


import java.util.List;

public class FormulaireCompletDTO {
    private List<ChoixDTO> semestre1;
    private List<ChoixDTO> semestre2;

    private boolean wantsExtraCourses;
    private int extraHoursS1;
    private int extraHoursS2;
    private int proposedLicence;
    private int proposedMaster;

    private String grade;
    private String numBureau;
    private String emailPref;

    // Getters et Setters
    public List<ChoixDTO> getSemestre1() { return semestre1; }
    public void setSemestre1(List<ChoixDTO> semestre1) { this.semestre1 = semestre1; }

    public List<ChoixDTO> getSemestre2() { return semestre2; }
    public void setSemestre2(List<ChoixDTO> semestre2) { this.semestre2 = semestre2; }

    public boolean isWantsExtraCourses() { return wantsExtraCourses; }
    public void setWantsExtraCourses(boolean wantsExtraCourses) { this.wantsExtraCourses = wantsExtraCourses; }

    public int getExtraHoursS1() { return extraHoursS1; }
    public void setExtraHoursS1(int extraHoursS1) { this.extraHoursS1 = extraHoursS1; }

    public int getExtraHoursS2() { return extraHoursS2; }
    public void setExtraHoursS2(int extraHoursS2) { this.extraHoursS2 = extraHoursS2; }

    public int getProposedLicence() { return proposedLicence; }
    public void setProposedLicence(int proposedLicence) { this.proposedLicence = proposedLicence; }

    public int getProposedMaster() { return proposedMaster; }
    public void setProposedMaster(int proposedMaster) { this.proposedMaster = proposedMaster; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getNumBureau() { return numBureau; }
    public void setNumBureau(String numBureau) { this.numBureau = numBureau; }

    public String getEmailPref() { return emailPref; }
    public void setEmailPref(String emailPref) { this.emailPref = emailPref; }
}

