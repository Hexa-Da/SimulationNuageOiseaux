import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.ArrayList;
import java.util.Random;



class Cases {
	
	int NumCase;
    int X, Y;
    int TailleCase;
    ArrayList<Cases> CaseVoisine;
    ArrayList<Oiseaux> Population;   

    public Cases(int NumCase, int X, int Y, int TailleCase) {
        this.NumCase = NumCase;
        this.X = X;
        this.Y = Y;
        this.TailleCase = TailleCase;
        CaseVoisine = new ArrayList<>();
        Population = new ArrayList<>();
    }
    
    public void ajouterVoisin(Cases voisin) {
        CaseVoisine.add(voisin);
    }
    
    public void dessiner(Graphics2D g2d) {
        g2d.setColor(Color.BLACK); 
        g2d.drawRect(X, Y, TailleCase, TailleCase); 
    }
}



class Grille extends JPanel {
	private static final long serialVersionUID = 1L;
	
	ArrayList<Cases> Grille;
	int TailleCase;
	int NombreColonnes, NombreLignes;

	public Grille(int TailleCase, int NombreColonnes, int NombreLignes) {
        Grille = new ArrayList<>();
        this.NombreLignes = NombreLignes;
        this.NombreColonnes = NombreColonnes;
        this.TailleCase = TailleCase;

                
        // Création des cases du cadrillage
        for (int i = 0; i < NombreLignes; i++) {
            for (int j = 0; j < NombreColonnes; j++) {
            	int NumCase = i * NombreColonnes + j; //0 à N-1
            	// Coordonnées du point en haut à gauche de chaque case
            	// X abcisse et Y ordonnée et origine en haut à gauche
                int X = j * TailleCase;
                int Y = i * TailleCase;
                
                

                Cases nouvelleCase = new Cases(NumCase, X, Y, TailleCase);
                Grille.add(nouvelleCase);
            }
        }

        // Création de la liste des voisins (9 cases)
        for (int i = 0; i < NombreLignes; i++) {
            for (int j = 0; j < NombreColonnes; j++) {
            	// Identification de la case avec NumCase
                int NumCase = i * NombreColonnes + j;
                //case est un mot clé donc on utilise c
                Cases c = Grille.get(NumCase);
                calculerVoisins(c, NombreColonnes, NombreLignes, Grille);
            }
        }
	}
	
    public void calculerVoisins(Cases Case, int NombreColonnes, int NombreLignes, ArrayList<Cases> Grille) {
    	// Récupération des infos 
        int X = Case.X;
        int Y = Case.Y;
        int NumCase = Case.NumCase;
        int TailleCase = Case.TailleCase;
       
        // Liste des déplacements possibles pour trouver les voisins
        int[][] deplacements = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};

        // Parcours des déplacements possibles pour trouver les voisins
        for (int[] deplacement : deplacements) {
            int VoisinX = X + deplacement[0] * TailleCase;
            int VoisinY = Y + deplacement[1] * TailleCase;
            
            // Vérification si le voisin est dans les limites du cadrillage
            // A faire si les oiseau "rebondissent" sur les bords
            if (VoisinY >= 0 && VoisinY < NombreLignes * TailleCase &&
            		VoisinX >= 0 && VoisinX < NombreColonnes * TailleCase) {
            	
                // Calcul de l'indice du voisin dans le cadrillage
            	int VoisinNum = NumCase + deplacement[1] * NombreColonnes + deplacement[0]; 
                Cases voisin = Grille.get(VoisinNum); 
                Case.ajouterVoisin(voisin);
            }

            // A faire si les oiseau traversent les bords
            else {
                int VoisinNum = NumCase;
                if (VoisinX < 0) {
                    // Calcul de l'indice du voisin dans le cadrillage
                    VoisinNum += NombreColonnes-1;
                }
                if (VoisinX >= NombreColonnes * TailleCase) {
                    VoisinNum += - (NombreColonnes -1);
                }
                if (VoisinY < 0) {
                    VoisinNum += NombreColonnes*(NombreLignes-1);
                }
                if (VoisinY >= NombreLignes*TailleCase) {
                    VoisinNum -= NombreColonnes*(NombreLignes-1);
                }

                Cases voisin = Grille.get(VoisinNum); 
                Case.ajouterVoisin(voisin);
            
            }
        }
    }
    
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Cases c : Grille) {  
        	c.dessiner(g2d);
        }
	}
}



class Oiseaux {
	
    double x, y;
    double vx, vy;
    int NumCase, Matricule;
   
    public Oiseaux(double x, double y, int NumCase, int Matricule) {
    	
        this.x = x;
        this.y = y;
        this.NumCase = NumCase;
        this.Matricule = Matricule;
        // Choix de la direction : (random.nextBoolean() ? 1 : -1)
        // Choix de la norm entre 0 et 1 inclus : random.nextInt(101)/100
        Random random = new Random();
        this.vx = (random.nextBoolean() ? 1 : -1) * (double) random.nextInt(101)/100;
        this.vy = (random.nextBoolean() ? 1 : -1) * (double) random.nextInt(101)/100;
    }
    
    public void Deplacer(int vitesse, int largeur, int hauteur) {
    	// Deplacer(double angleOiseau, int vitesse, int largeur, int hauteur)
    	
    	this.x += this.vx * vitesse;
        this.y += this.vy * vitesse;
        
     // mouvement avec variation
//        Random random = new Random();
//        double variation = (random.nextBoolean() ? 1 : -1) * random.nextDouble() * 10;
//        angleOiseau += variation;
//
//        double newVx = Math.cos(Math.toRadians(angleOiseau));
//        double newVy = Math.sin(Math.toRadians(angleOiseau));
//
//        vx = newVx;
//        vy = newVy;
        
     	  // Rebondir à l'intérieur de la fenêtre
        // if (this.x < 0) {
        // 	this.vx = -this.vx;
        // 	this.x = 0.01;
        // 	}
        // if (this.x >= largeur) {
        // 	this.vx = -this.vx;
        // 	this.x = largeur - 0.01;
        // 	}
        
        // if (y < 0) {
        // 	this.vy = -this.vy;
        // 	this.y = 0.01;
        // 	}      	
        // if (y >= hauteur) {
        // 	this.vy = -this.vy;
        // 	this.y = hauteur -0.01;
        // 	}
        
        
       // Traverser la fenêtre
       if (this.x < 0) {
       	this.x = largeur;
       }
       if (this.y < 0) {
       	this.y = hauteur;
       }
       if (this.x > largeur) {
       	this.x = 0;
       }
       if (this.y > hauteur) {
       	this.y = 0;
       }
    }

    public static double[] findMinimum(ArrayList<Double> numbers) {
        // Vérifiez si la liste est vide ou null
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("La liste est vide ou null.");
        }

        // Initialisez le minimum avec le premier élément de la liste
        double min = numbers.get(0);
        int index = 0;
        int indexmin = 0;
        // Parcourez la liste pour trouver le minimum
        for (double num : numbers) {
            if (num < min) {
                min = num; // Mettez à jour le minimum si un nombre plus petit est trouvé
                indexmin = index;
            }
            index += 1;
        }

        double[] resultat = {min, indexmin};
        return resultat;
    }

	public void Repulsion(ArrayList<Oiseaux> voisins, double CoeffRepulsion, double Vitesse_max, int Rayon, int  Largeur, int Hauteur) { 
	    if (voisins.isEmpty()) {
            return;
        }
        
        int[][] deplacements = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0},{0,0}};
        double repelX = 0;
        double repelY = 0;
    
        for (Oiseaux voisin : voisins) {
            // Calculer la distance entre ce boid et le voisin
            ArrayList<Double> Distances_possibles = new ArrayList<>();
            for (int[] dep : deplacements){
                double distance = Math.sqrt(Math.pow(voisin.x+dep[0]*Largeur - this.x, 2) + Math.pow(voisin.y+dep[1]*Largeur - this.y, 2));
                Distances_possibles.add(distance);
            }
            double[] distmin = findMinimum(Distances_possibles);
            double distance = distmin[0];
            int indexmin = (int) distmin[1];

            // Si le voisin est trop proche
            if (distance < Rayon) {
                // Calculer la direction pour s'éloigner du voisin
                double directionX = this.x - (voisin.x+deplacements[indexmin][0]*Largeur);
                double directionY = this.y - (voisin.y+deplacements[indexmin][1]*Largeur);

                // Normaliser la direction
                double norm = Math.sqrt(directionX * directionX + directionY * directionY);
                if (norm > 0) {
                    directionX /= norm;
                    directionY /= norm;
                }

                // Ajouter à la force de répulsion
                repelX += directionX;
                repelY += directionY;
            }
        }
    
        // Appliquer la force de répulsion
        this.vx += repelX * CoeffRepulsion;
        this.vy += repelY * CoeffRepulsion;
	}

	public void Alignement(ArrayList<Oiseaux> voisins, double coeffAlignement, double Vitesse_max, int RayonIn, int RayonOut, int  Largeur, int Hauteur) {
            if (voisins.isEmpty()) {
                return;
            }
            int[][] deplacements = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0},{0,0}};
            double avgVx = 0;
            double avgVy = 0;
            int count = 0;

            for (Oiseaux voisin : voisins) {
                // Calculer la distance entre ce boid et le voisin
                ArrayList<Double> Distances_possibles = new ArrayList<>();
                for (int[] dep : deplacements){
                    double distance = Math.sqrt(Math.pow(voisin.x+dep[0]*Largeur - this.x, 2) + Math.pow(voisin.y+dep[1]*Largeur - this.y, 2));
                    Distances_possibles.add(distance);
                }
                double[] distmin = findMinimum(Distances_possibles);
                double distance = distmin[0];
                // int indexmin = (int) distmin[1];


                // Si le voisin est dans le 1er cercle
                if (distance < RayonIn ){ //& distance >= RayonOut
                    avgVx += voisin.vx;
                    avgVy += voisin.vy;
                    count++;
                }
            }

            if (count > 0) {
                // Calculer la moyenne des vitesses des voisins
                avgVx /= count;
                avgVy /= count;

                // Normaliser la vitesse moyenne
                double avgSpeed = Math.sqrt(avgVx * avgVx + avgVy * avgVy);
                if (avgSpeed > 0) {
                    avgVx = (avgVx / avgSpeed) * Vitesse_max;
                    avgVy = (avgVy / avgSpeed) * Vitesse_max;
                }

                // Calculer la force d'alignement
                double steerVx = avgVx - this.vx;
                double steerVy = avgVy - this.vy;

                // Appliquer la force d'alignement
                this.vx += steerVx * coeffAlignement;
                this.vy += steerVy * coeffAlignement;
            }
        
	}

	public void Attraction(ArrayList<Oiseaux> voisins, double coeffAttraction, double Vitesse_max, int RayonIn, int RayonOut, int  Largeur, int Hauteur) {
        if (voisins.isEmpty()) {
            return;
        }
        int[][] deplacements = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0},{0,0}};
        double avgX = 0;
        double avgY = 0;
        int count = 0;
    
        for (Oiseaux voisin : voisins) {
            ArrayList<Double> Distances_possibles = new ArrayList<>();
            for (int[] dep : deplacements){
                double distance = Math.sqrt(Math.pow(voisin.x+dep[0]*Largeur - this.x, 2) + Math.pow(voisin.y+dep[1]*Largeur - this.y, 2));
                Distances_possibles.add(distance);
            }
            double[] distmin = findMinimum(Distances_possibles);
            double Distance = distmin[0];
            int indexmin = (int) distmin[1];



            if (Distance < RayonIn ){ //& Distance >= RayonOut
                avgX += (voisin.x+deplacements[indexmin][0]*Largeur);
                avgY += (voisin.y+deplacements[indexmin][1]*Largeur);
                count++;
            }
        }
    
        if (count > 0) {
            avgX /= count;
            avgY /= count;
    
            // Calculer la direction vers le centre de masse des voisins
            double directionX = avgX - this.x;
            double directionY = avgY - this.y;
    
            // Normaliser la direction
            double distance = Math.sqrt(directionX * directionX + directionY * directionY);
            if (distance > 0) {
                directionX = (directionX / distance) * Vitesse_max;
                directionY = (directionY / distance) * Vitesse_max;
            }
    
            // Calculer la force d'attraction
            double steerX = directionX - this.vx;
            double steerY = directionY - this.vy;
    
            // Appliquer la force d'attraction
            this.vx += steerX * coeffAttraction;
            this.vy += steerY * coeffAttraction;
        }
	}
        
    public void NormerVitesse(double Vitesse){
        //règle la vitesse de l'oiseau à Vitesse, pour que tous les oiseaux aient la même vitesse.
        double Norme = Math.sqrt(this.vx*this.vx + this.vy*this.vy);
        this.vx = this.vx/Norme * Vitesse;
        this.vy = this.vy/Norme * Vitesse;
    }

    public void dessiner(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Dessiner un curseur sous forme de flèche pour indiquer la direction de l'oiseau
        double cursorLength = 10; // Longueur du curseur
        double cursorAngle = Math.atan2(vy, vx); // Angle de la direction de l'oiseau

        // Coordonnées des points de la flèche
        int[] xPoints = {(int) x, (int) (x - cursorLength * Math.cos(cursorAngle - Math.PI / 8)), 
                         (int) (x - cursorLength * Math.cos(cursorAngle + Math.PI / 8))};
        int[] yPoints = {(int) y, (int) (y - cursorLength * Math.sin(cursorAngle - Math.PI / 8)), 
                         (int) (y - cursorLength * Math.sin(cursorAngle + Math.PI / 8))};

        // Dessiner la flèche
        Polygon arrow = new Polygon(xPoints, yPoints, 3);
        g2d.setColor(Color.BLACK); // Couleur de la flèche (noir)
        g2d.fill(arrow);
        }
}



class NueeOiseaux extends JPanel { 
	private static final long serialVersionUID = 1L;
	
	// Valeur par défaut
    boolean isPaused = false;
    int Vitesse = 3; 
	int NombreOiseaux;
	double coeffRepulsion = 0.05; 
	double coeffAlignement = 0.1; 
	double coeffAttraction = 0.05; 
	double Vitmax=3;
    int RayonAt = 100;
    int RayonAl = 66;
    int RayonRe = 33;
    
    ArrayList<Oiseaux> NueeOiseaux;
	int Largeur;
	int Hauteur;  
	int TailleCase;
	int NombreColonnes, NombreLignes;
	ArrayList<Cases> grille;
    
    public NueeOiseaux(int Largeur, int Hauteur, Grille grille) {
        this.NueeOiseaux = new ArrayList<>();
    	
    	// Récupération des infos
    	this.Largeur = Largeur;
    	this.Hauteur = Hauteur;
    	this.grille  = grille.Grille;
    	this.TailleCase = grille.TailleCase;
    	this.NombreColonnes = grille.NombreColonnes; 
    	this.NombreLignes = grille.NombreLignes;
        
        // Créer une minuterie pour mettre à jour la simulation toutes les 10 millisecondes
        Timer timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) {
                    DeplacerOiseaux();
                    Boid();
                    repaint(); // Redessiner la fenêtre
                }
            }
        });

        timer.start();
    }
    
    public void DeplacerOiseaux() {
    	//Prend note des changements de cases des oiseaux
        for (Oiseaux oiseau : NueeOiseaux) {
            
        	// mouvement avec variation
//            double vx = oiseau.vx;
//			double vy = oiseau.vy;
//            double moduleOiseau = Math.sqrt(vx * vx + vy * vy);
//	        double angleOiseau = Math.toDegrees(Math.acos(vx / moduleOiseau));
//	        if (vy < 0) {
//	            angleOiseau = 360 - angleOiseau;
//	        }
//	        
//	        oiseau.Deplacer(angleOiseau,Vitesse,Largeur,Hauteur);
        	
        	
	        oiseau.Deplacer(this.Vitesse,this.Largeur,this.Hauteur);
	        
	        double x = oiseau.x;
	        double y = oiseau.y;
	        int NumCase = oiseau.NumCase;
	        
	        if (Coord_to_Num(x,y) != NumCase) {
	        	oiseau.NumCase = Coord_to_Num(x,y);
	        	for (Cases c : grille) {
	            	if (c.NumCase == oiseau.NumCase) {
	            		c.Population.add(oiseau);
	            	}
	        	}
	            		
	            for (Cases c : grille) {
	            	if (c.NumCase == NumCase) {
	            		c.Population.remove(oiseau);
	            	}
	        	}
	        }
        }
    }
     
    public int Coord_to_Num(double x, double y) {
        int X = (int) x / TailleCase;
        int Y = (int) y / TailleCase;
        return Y * NombreColonnes + X;
    }
        
    public void setPause(boolean Pause) {
        isPaused = Pause;
    }
      
    public void setSpeed(int Vitesse) {
        this.Vitesse = Vitesse;
    }
         
    public void setRayonAt(int R) {
        this.RayonAt = R;
    }

    public void setRayonAl(int R) {
        this.RayonAl = R;
    }

    public void setRayonRe(int R) {
        this.RayonRe = R;
    }

    public void setNbrOiseaux(int NombreOiseaux) {
        this.NombreOiseaux = NombreOiseaux;
        updateOiseaux();
    }
    public void setVitmax(double Vitmax) {
    	this.Vitmax = Vitmax;
    }
     
    public void updateOiseaux() {
    	// on créé ou recréé une simulation
        NueeOiseaux.clear();
        for (int i = 0; i < NombreOiseaux; i++) {
            double x = Math.random() * Largeur;
            double y = Math.random() * Hauteur;
            int NumCase = Coord_to_Num((double) x, (double) y);
			Oiseaux oiseau = new Oiseaux((double) x, (double) y, (int) NumCase, (int) i);
            NueeOiseaux.add(oiseau);
            
            // on initialise la population de chaque case
            for (Cases c : grille) {
            	if (c.NumCase == NumCase) {
            		c.Population.add(oiseau);
            	}
            }
        }
    }
    
    public void setRepulsion(double coeffRepulsion) {
        this.coeffRepulsion = coeffRepulsion;
    }
    
    public void setAlignement(double coeffAlignement) {
        this.coeffAlignement = coeffAlignement;
    }
    
    public void setAttraction(double coeffAttraction) {
        this.coeffAttraction = coeffAttraction;
    }
    
    public void Boid() {
    	
		for (Oiseaux oiseau1 : NueeOiseaux) {
			for (Cases c : grille) {
				if (c.NumCase == oiseau1.NumCase) {
					ArrayList<Oiseaux> Voisin = new ArrayList<>(c.Population);

					for (Cases voisin : c.CaseVoisine) {
						Voisin.addAll(voisin.Population);
					}

					oiseau1.Repulsion(Voisin,coeffRepulsion,Vitmax,RayonRe, Largeur, Hauteur);
                    oiseau1.Alignement(Voisin,coeffAlignement,Vitmax,RayonAl,RayonRe, Largeur, Hauteur);
                    oiseau1.Attraction(Voisin,coeffAttraction,Vitmax,RayonAt,RayonAl, Largeur, Hauteur);
                    oiseau1.NormerVitesse(Vitmax);
					
				}
			}
	    }
	}
    
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	Graphics2D g2d = (Graphics2D) g;
        
        for (Oiseaux oiseau : NueeOiseaux) {
        	oiseau.dessiner(g2d);
        }    
    }

}



public class SimuExperienceCopy{
	
    private static boolean isPaused = false;

    public static void main(String[] args) {
    	
    	// Paramètre de simulation
    	
    	// Réglage grand écran
    	int LargeurEcran = 600;//1730; 
        int HauteurEcran = 600;
    	// Réglage petit écran 
//        int LargeurEcran = 1250; 
//        int HauteurEcran = 750; 
        
        // espace pour le pannel
        int PannelSpace = 180;
        
        // coté d'une case
        int TailleCase = 100; 
        
        
        // paramètres par défaults
        JFrame frame = new JFrame("Simulation Nuée d'Oiseaux");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(LargeurEcran+PannelSpace+100, HauteurEcran+100);
        
        int NombreColonnes = LargeurEcran/TailleCase; 
        int NombreLignes = HauteurEcran/TailleCase; 
        
        int Largeur = NombreColonnes*TailleCase;
        int Hauteur = NombreLignes*TailleCase;
        
        // Création du Cadrillage
        Grille grille = new Grille(TailleCase, NombreColonnes, NombreLignes);
        frame.add(grille);
        
        // Créer un nuage d'oiseaux sur le cadrillage
        NueeOiseaux nueeOiseaux = new NueeOiseaux(Largeur, Hauteur, grille); 
        frame.add(nueeOiseaux);
        
        
        
        // gestion du pannel de controle
      
        // Ajout un slider pour le rayons de répulsion entre 10 et 30, avec une valeur initiale de 20
        JSlider repulsionSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 5);
        repulsionSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                double coeffRepulsion = repulsionSlider.getValue() * 0.01;
                nueeOiseaux.setRepulsion(coeffRepulsion);
            }
        });
        
        // Ajout un slider pour le rayon d'alignement entre 30 et 50, avec une valeur initiale de 50
        JSlider alignementSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 10);
        alignementSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                double coeffAlignement = alignementSlider.getValue() * 0.01;
                nueeOiseaux.setAlignement(coeffAlignement);
            }
        });
        
     // Ajout un slider pour le rayon d'attraction entre 50 et 80, avec une valeur initiale de 60
        JSlider attractionSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 5);
        attractionSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                double coeffAttraction = attractionSlider.getValue() * 0.01;
                nueeOiseaux.setAttraction(coeffAttraction);
            }
        });
        
        // Slider pour la vitesse maximale
        JSlider vitmaxSlider = new JSlider(JSlider.HORIZONTAL, 0, 6, 3);
        vitmaxSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                double Vitmax = vitmaxSlider.getValue();
                nueeOiseaux.setVitmax(Vitmax);
            }
        });
        
        // Ajout d'un bouton de pause
        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new ActionListener() { 
        	//Lorsque ce bouton est cliqué
            public void actionPerformed(ActionEvent e) {
                isPaused = !isPaused;
                nueeOiseaux.setPause(isPaused);
            }
        });

        // Ajout d'un slider pour la valeur de la vitesse entre 1 et 10, avec une valeur initiale de 3
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 3);
        speedSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int Vitesse = speedSlider.getValue();
                nueeOiseaux.setSpeed(Vitesse);
            }
        });

        // slider rayon d'attraction
        JSlider RayonAtSlider = new JSlider(JSlider.HORIZONTAL, 1, TailleCase, 100);
        RayonAtSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int R = RayonAtSlider.getValue();
                nueeOiseaux.setRayonAt(R);
            }
        });

        // slider rayon d'alignement
        JSlider RayonAlSlider = new JSlider(JSlider.HORIZONTAL, 1, TailleCase, 66);
        RayonAlSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int R = RayonAlSlider.getValue();
                nueeOiseaux.setRayonAl(R);
            }
        });

        // slider rayon de répulsion
        JSlider RayonReSlider = new JSlider(JSlider.HORIZONTAL, 1, TailleCase, 33);
        RayonReSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int R = RayonReSlider.getValue();
                nueeOiseaux.setRayonRe(R);
            }
        });

        // Ajout d'un champ de texte pour entrer le nombre d'oiseaux
        // La taille du champ de texte est limitée à 5 (unité ?)
        JTextField nbrOiseauxField = new JTextField(5);
        //Définit une valeur par défaut de 5 oiseaux
        nbrOiseauxField.setText("5");  

        // Ajout d'un bouton pour appliquer le changement
        JButton applyButton = new JButton("Appliquer");
        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// Lorsque ce bouton est cliqué
                try { 
                	// On récupère le nombre d'oiseaux entré dans le champ de texte
                	int NombreOiseaux = Integer.parseInt(nbrOiseauxField.getText());
                	if (NombreOiseaux > 10000) {
                        JOptionPane.showMessageDialog(frame, "Veuillez entrer un nombre entre 1 et 10000.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return; }
                	else {
	                    nueeOiseaux.setNbrOiseaux(NombreOiseaux); }
                // Si un nombre non valide est entré
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Veuillez entrer un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        
        
        // affichage du pannel de contrôle
        
        // création du panneau de contrôle avec un BoxLayout
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        
        controlPanel.add(Box.createVerticalStrut(10)); // Espacement vertical
        controlPanel.add(new JLabel("Coeff de répulsion:"));
        controlPanel.add(repulsionSlider);
        controlPanel.add(Box.createVerticalStrut(10)); 
        controlPanel.add(new JLabel("Coeff d'alignement:"));
        controlPanel.add(alignementSlider);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(new JLabel("Coeff d'attraction:"));
        controlPanel.add(attractionSlider);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(new JLabel("Vitesse maximale:"));
        controlPanel.add(vitmaxSlider);
        controlPanel.add(new JLabel("Rayon d'attraction:"));
        controlPanel.add(RayonAtSlider);
        controlPanel.add(new JLabel("Rayon d'alignement:"));
        controlPanel.add(RayonAlSlider);
        controlPanel.add(new JLabel("Rayon de répulsion:"));
        controlPanel.add(RayonReSlider);
        
        controlPanel.add(Box.createVerticalStrut(30)); 
        controlPanel.add(new JLabel("Vitesse:"));
        controlPanel.add(speedSlider);
        
        controlPanel.add(Box.createVerticalStrut(20)); 
        controlPanel.add(new JLabel("Nombre d'oiseaux:"));
        controlPanel.add(nbrOiseauxField);
        controlPanel.add(Box.createVerticalStrut(10)); 
        controlPanel.add(applyButton);
        
        controlPanel.add(Box.createVerticalStrut(20)); 
        controlPanel.add(pauseButton);
        
        frame.add(controlPanel, BorderLayout.EAST);
         
        frame.setVisible(true);
    }
}




