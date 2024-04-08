
public class BoidMaison {

	public static void main(String[] args) {

	}

	public double[] Repulsion(double angleOiseau1, double angleOiseau2) {
		
	  double coeffRepulsion = 0.3;
	  double[] newV = new double[2];
	  
	  double diff = angleOiseau1 - angleOiseau2;
	  angleOiseau1 += coeffRepulsion * diff;
	
	  double newVx1 = Math.cos(Math.toRadians(angleOiseau1));
	  double newVy1 = Math.sin(Math.toRadians(angleOiseau1));
	
	  newV[0] = newVx1;
	  newV[1] = newVy1;
	
	  return newV;
	}
	
	public double[] Alignement(double angleOiseau1, double angleOiseau2) {
	
	  double coeffAlignement = 0.1;
	  double[] newV = new double[2];
	  
	  double diff = angleOiseau1 - angleOiseau2;
	  angleOiseau1 -= coeffAlignement * diff;
	
	  double newVx1 = Math.cos(Math.toRadians(angleOiseau1));
	  double newVy1 = Math.sin(Math.toRadians(angleOiseau1));
	
	  newV[0] = newVx1;
	  newV[1] = newVy1;
	
	  return newV;
	}
		
	public double[] Attraction(double angleOiseau1, double angleOiseau2) {
		
	  double coeffAttraction = 0.2;
	  double[] newV = new double[2];
	  
	  double diff = angleOiseau1 - angleOiseau2;
	  angleOiseau1 -= coeffAttraction * diff;
	
	  double newVx1 = Math.cos(Math.toRadians(angleOiseau1));
	  double newVy1 = Math.sin(Math.toRadians(angleOiseau1));
	
	  newV[0] = newVx1;
	  newV[1] = newVy1;
	
	  return newV;
		
	}
}
