package DTO;

import java.util.List;

import com.departement.fichedevoeux.model.Grade;

public class FormulaireRequestDTO {

	  private Long professeurId;
	    private List<ChoixDTO> semestre1;
	    private List<ChoixDTO> semestre2;
	    private boolean wantsExtraCourses;
	    private int extraHoursS1;
	    private int extraHoursS2;
	    private int proposedLicence;
	    private int proposedMaster; // ✅ NOM CORRECT

	    private Grade grade;
	    private int numBureau;
	    private String emailPref;
	    
	    
		public Long getProfesseurId() {
			return professeurId;
		}
		public void setProfesseurId(Long professeurId) {
			this.professeurId = professeurId;
		}
		public List<ChoixDTO> getSemestre1() {
			return semestre1;
		}
		public void setSemestre1(List<ChoixDTO> semestre1) {
			this.semestre1 = semestre1;
		}
		public List<ChoixDTO> getSemestre2() {
			return semestre2;
		}
		public void setSemestre2(List<ChoixDTO> semestre2) {
			this.semestre2 = semestre2;
		}
		public boolean isWantsExtraCourses() {
			return wantsExtraCourses;
		}
		public void setWantsExtraCourses(boolean wantsExtraCourses) {
			this.wantsExtraCourses = wantsExtraCourses;
		}
		public int getExtraHoursS1() {
			return extraHoursS1;
		}
		public void setExtraHoursS1(int extraHoursS1) {
			this.extraHoursS1 = extraHoursS1;
		}
		public int getExtraHoursS2() {
			return extraHoursS2;
		}
		public void setExtraHoursS2(int extraHoursS2) {
			this.extraHoursS2 = extraHoursS2;
		}
		public int getProposedLicence() {
			return proposedLicence;
		}
		public void setProposedLicence(int proposedLicence) {
			this.proposedLicence = proposedLicence;
		}
		public int getProposedMaster() {
		    return proposedMaster;
		}

		public void setProposedMaster(int proposedMaster) {
		    this.proposedMaster = proposedMaster;
		}

		public Grade getGrade() {
			return grade;
		}
		public void setGrade(Grade grade) {
			this.grade = grade;
		}
		public int getNumBureau() {
			return numBureau;
		}
		public void setNumBureau(int numBureau) {
			this.numBureau = numBureau;
		}
		public String getEmailPref() {
			return emailPref;
		}
		public void setEmailPref(String emailPref) {
			this.emailPref = emailPref;
		}
	    
	
}
