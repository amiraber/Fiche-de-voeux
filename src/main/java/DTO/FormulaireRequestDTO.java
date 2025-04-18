package DTO;

import java.util.List;

public class FormulaireRequestDTO {

	  private Long professeurId;
	    private List<ChoixDTO> semestre1;
	    private List<ChoixDTO> semestre2;
	    private boolean wantsExtraCourses;
	    private int extraHours;
	    private int proposedLicences;
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
			return extraHours;
		}
		public void setExtraHours(int extraHours) {
			this.extraHours = extraHours;
		}
		public int getProposedLicences() {
			return proposedLicences;
		}
		public void setProposedLicences(int proposedLicences) {
			this.proposedLicences = proposedLicences;
		}
	    
	    
	
}
