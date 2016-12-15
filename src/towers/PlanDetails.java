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
		
		public PlanDetails getDetails(){
			return details;
		}
	}
		
	private int hue, figureValue;
	private double positionValue;
	
	public PlanDetails(int hue, int figureValue,
			double positionValue){
		this.hue = hue;
		this.figureValue = figureValue;
		this.positionValue = positionValue;
	}
	
	public int getHue(){
		return hue;
	}
	
	public int getFigureValue(){
		return figureValue;
	}
	
	public double getPositionValue(){
		return positionValue;
	}
		
	
}
