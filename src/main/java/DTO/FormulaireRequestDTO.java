package DTO;

import java.util.List;

public class FormulaireRequestDTO {

	  private Long professeurId;
	    private List<ChoixDTO> semestre1;
	    private List<ChoixDTO> semestre2;
	    private boolean wantsExtraCourses;
	    private int extraHoursS1;
	    private int extraHoursS2;
	    private int proposedLicence;
	    private int prposedMaster;
	    
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
		public int getExtraHours() {
			return extraHoursS1;
		}
		public void setExtraHours(int extraHoursS1) {
			this.extraHoursS1 = extraHoursS1;
		}
		public int getExtraHoursS2() {
			return extraHoursS2;
		}
		public void setExtraHoursS2(int extraHoursS2) {
			this.extraHoursS2 = extraHoursS2;
		}
		public int getProposedLicences() {
			return proposedLicence;
		}
		public void setProposedLicences(int proposedLicence) {
			this.proposedLicence = proposedLicence;
		}
		public int getPrposedMaster() {
			return prposedMaster;
		}
		public void setPrposedMaster(int prposedMaster) {
			this.prposedMaster = prposedMaster;
		}
	    
	    
	
}
