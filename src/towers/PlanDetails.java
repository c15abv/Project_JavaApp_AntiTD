package towers;

public class PlanDetails{
	
	public class TowerBuildPlan{
		
		private PlanDetails details;
		private long time;
		
		public TowerBuildPlan(long time, PlanDetails details){
			this.time = time;
			this.details = details;
		}
		
		public long getTime(){
			return time;
		}
		
		protected void setNewTime(long time){
			this.time = time;
		}
		
		public PlanDetails getDetails(){
			return details;
		}
	}
		
	private int numFigures;
	private double positionValue;
	
	public PlanDetails(int numFigures, double positionValue){
		this.numFigures = numFigures;
		this.positionValue = positionValue;
	}
	
	public int getNumFigures(){
		return numFigures;
	}

	public double getPositionValue(){
		return positionValue;
	}
}
