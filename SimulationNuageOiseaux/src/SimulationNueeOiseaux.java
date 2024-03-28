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
    int NumCase; 
   
    public Oiseaux(double x, double y, int NumCase) {
    	
        this.x = x;
        this.y = y;
        this.NumCase = NumCase;
       
        // Choix de la direction : (random.nextBoolean() ? 1 : -1)
        // Choix de la norm entre 0 et 1 inclus : random.nextInt(101)/100
        Random random = new Random();
        this.vx = (random.nextBoolean() ? 1 : -1) * (double) random.nextInt(101)/100;
        this.vy = (random.nextBoolean() ? 1 : -1) * (double) random.nextInt(101)/100;
    }
    
    public void Deplacer(int vitesse, int largeur, int hauteur) {
    	// Deplacer(double angleOiseau, int vitesse, int largeur, int hauteur)
    	
    	x += vx * vitesse;
        y += vy * vitesse;
        
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
        if (x < 0 || x >= largeur) {
        	vx = -vx;
        }
        if (y < 0 || y >= hauteur) {
        	vy = -vy;
        }
        
//        // Traverser la fenêtre
//        if (x < 0) {
//        	x = largeur;
//        }
//        if (y < 0) {
//        	y = hauteur;
//        }
//        if (x > largeur) {
//        	x = 0;
//        }
//        if (y > hauteur) {
//        	y = 0;
//        }
    }

	public void Repulsion(double Xo, double Yo, double dt, double CoeffRepulsion) { 
	
		double dx = x-Xo;
		double dy = y-Yo;
		
		double r = Math.sqrt(dx*dx+dy*dy); //Distance entre le Boid considéré et le Boid de coordonnées (Xo,Yo)
		 
		//Mise à jour de la vitesse
		this.vx = this.vx+CoeffRepulsion*dt*dx/Math.pow(r,3); 
		this.vy = this.vy+CoeffRepulsion*dt*dy/Math.pow(r,3); 
		
		this.x = this.x+dt*this.vx;
		this.y = this.y+dt*this.vy; //Mise à jour de la position
	}

	public void Alignement(double Vox, double Voy, double coeffAlignement) {
		
	   double kal = coeffAlignement; //Coefficient d'alignement (kal < 1)
	
	   double N = Math.sqrt(this.vx*this.vx+this.vy*this.vy); //Norme de la vitesse du Boid considéré
	   double No = Math.sqrt(Vox*Vox+Voy*Voy); //Norme de la vitesse du Boid de vitesse (Vxo,Vyo)
	
	   this.vx = N*(this.vx/N+kal*Vox/No)/Math.sqrt((this.vx/N+kal*Vox/No)*(this.vx/N+kal*Vox/No)+(this.vy/N+kal*Voy/No)*(this.vy/N+kal*Voy/No)); //Variation de vx
	   this.vy = N*(this.vy/N+kal*Voy/No)/Math.sqrt((this.vx/N+kal*Vox/No)*(this.vx/N+kal*Vox/No)+(this.vy/N+kal*Voy/No)*(this.vy/N+kal*Voy/No)); //Variation de vy
	   //Remarque: la norme de v est inchangée!
	}

	public void Attraction(double Xo, double Yo, double dt, double coeffAttraction) {
	
	   double kat = coeffAttraction; //Coefficient d'attraction
	
	   double dx = this.x-Xo;
	   double dy = this.y-Yo;
	
	   double r = Math.sqrt(dx*dx+ dy*dy); //Distance entre les Boids
	
	   this.vx=this.vx+kat*dt*-dx/Math.pow(r,3);
	   this.vy=this.vy+kat*dt*-dy/Math.pow(r,3); //Mise à jour de la vitesse
	   
	   this.x = this.x+dt*this.vx;
	   this.y = this.y+dt*this.vy; //Mise à jour de la position
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
	double coeffRepulsion = 1; 
	double coeffAlignement = 1/8; 
	double coeffAttraction = 1; 
    
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
        	
        	
	        oiseau.Deplacer(Vitesse,Largeur,Hauteur);
	        
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
         
    public void setNbrOiseaux(int NombreOiseaux) {
        this.NombreOiseaux = NombreOiseaux;
        updateOiseaux();
    }
     
    public void updateOiseaux() {
    	// on créé ou recréé une simulation
        NueeOiseaux.clear();
        for (int i = 0; i < NombreOiseaux; i++) {
            double x = Math.random() * Largeur;
            double y = Math.random() * Hauteur;
            int NumCase = Coord_to_Num((double) x, (double) y);
			Oiseaux oiseau = new Oiseaux((double) x, (double) y, (int) NumCase);
            NueeOiseaux.add(oiseau);
            
            // on initialise la population de chaque case
            for (Cases c : grille) {
            	if (c.NumCase == NumCase) {
            		c.Population.add(oiseau);
            	}
            }
        }
    }
    
    public void setRepulsion(int coeffRepulsion) {
        this.coeffRepulsion = coeffRepulsion;
    }
    
    public void setAlignement(int coeffAlignement) {
        this.coeffAlignement = coeffAlignement/8;
    }
    
    public void setAttraction(int coeffAttraction) {
        this.coeffAttraction = coeffAttraction;
    }
    
    public void Boid() {
    	
		for (Oiseaux oiseau1 : NueeOiseaux) {		
			for (Cases c : grille) {
				if (c.NumCase == oiseau1.NumCase) {
					ArrayList<Oiseaux> Voisin = new ArrayList<>(c.Population);
					for (Cases voisin : c.CaseVoisine) {
						Voisin.addAll(voisin.Population);
					
						for (Oiseaux oiseau2 : Voisin) {						
							if (oiseau1 != oiseau2) {
								
								double x1 = oiseau1.x;
								double y1 = oiseau1.y;
								double x2 = oiseau2.x;
								double y2 = oiseau2.y;
								
								double distance = Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2));
								
								double vx2 = oiseau2.vx;
								double vy2 = oiseau2.vy;
								double dt = 0.1;
								
								if (distance < TailleCase/3) {
									oiseau1.Repulsion(x2,y2,dt,coeffRepulsion);
								}
								
								else if (distance < 2*TailleCase/3) {
									oiseau1.Alignement(vx2,vy2,coeffAlignement);
								}
								
								else if (distance < TailleCase)  {
									oiseau1.Attraction(x2,y2,dt,coeffAttraction);
								}
							}
						}
					}
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



public class SimulationNueeOiseaux {
	
    private static boolean isPaused = false;

    public static void main(String[] args) {
    	
    	// Paramètre de simulation
    	
    	// Réglage grand écran
    	int LargeurEcran = 1730; 
        int HauteurEcran = 940;
    	// Réglage petit écran 
//        int LargeurEcran = 1250; 
//        int HauteurEcran = 750; 
        
        // espace pour le pannel
        int PannelSpace = 180;
        
        // coté d'une case
        int TailleCase = 60; 
        
        
        
        // paramètres par défaults
        JFrame frame = new JFrame("Simulation Nuée d'Oiseaux");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(LargeurEcran+PannelSpace, HauteurEcran);
        
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
        JSlider repulsionSlider = new JSlider(JSlider.HORIZONTAL, 0, 5, 1);
        repulsionSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int coeffRepulsion = repulsionSlider.getValue();
                nueeOiseaux.setRepulsion(coeffRepulsion);
            }
        });
        
        // Ajout un slider pour le rayon d'alignement entre 30 et 50, avec une valeur initiale de 50
        JSlider alignementSlider = new JSlider(JSlider.HORIZONTAL, 0, 5, 1);
        alignementSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int coeffAlignement = alignementSlider.getValue();
                nueeOiseaux.setAlignement(coeffAlignement);
            }
        });
        
        // Ajout un slider pour le rayon d'attraction entre 50 et 80, avec une valeur initiale de 60
        JSlider attractionSlider = new JSlider(JSlider.HORIZONTAL, 0, 5, 1);
        attractionSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int coeffAttraction = attractionSlider.getValue();
                nueeOiseaux.setAttraction(coeffAttraction);
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

        // Ajout d'un slider pour la valeur de la vitesse entre 1 et 10, avec une valeur initiale de 5
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 3);
        speedSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int Vitesse = speedSlider.getValue();
                nueeOiseaux.setSpeed(Vitesse);
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




