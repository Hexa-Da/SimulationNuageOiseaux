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
	
    double x, y, z;
    double vx, vy, vz;
    int NumCase; 
    Color espece;
   
    public Oiseaux(double x, double y, double z, int NumCase,  Color espece) {
    	
        this.x = x;
        this.y = y;
        this.z = z;
        this.NumCase = NumCase;
        this.espece = espece;
        
        // Choix de la direction : (random.nextBoolean() ? 1 : -1)
        // Choix de la norm entre 0 et 1 inclus : random.nextInt(101)/100
        Random random = new Random();
        this.vx = (random.nextBoolean() ? 1 : -1) * (double) random.nextInt(101)/100;
        this.vy = (random.nextBoolean() ? 1 : -1) * (double) random.nextInt(101)/100;
        this.vz = 1; //(random.nextBoolean() ? 1 : -1) * (double) random.nextInt(101)/100 * 0.002;
    }
    
    public void Deplacer(int vitesse, int largeur, int hauteur, int plafond) {

    	x += vx * vitesse;
        y += vy * vitesse;
        //z += vz * vitesse;
        
        // // mouvement avec variation
        // Random random = new Random();
        // double variation = (random.nextBoolean() ? 1 : -1) * random.nextDouble() * 5;
        // angleOiseau += variation;

        // double newVx = Math.cos(Math.toRadians(angleOiseau));
        // double newVy = Math.sin(Math.toRadians(angleOiseau));

        // vx = newVx;
        // vy = newVy;

        // // Les oiseaux rebondissent à l'intérieur de la fenêtre
        // if (x < 0 || x >= largeur) {
        // 	vx = -vx;
        // }
        // if (y < 0 || y >= hauteur) {
        // 	vy = -vy;
        // }
        // if (z <= 1 || z >= plafond) {
        // 	vz = -vz;
        // }

        // Les oiseaux traverse la fenêtre
        if (x < 0) {
            x = largeur;
        }
        if (y < 0) {
            y = hauteur;
        }
        if (x > largeur) {
            x = 0;
        }
        if (y > hauteur) {
        	y = 0;
        }

        if (z <= 1 || z >= plafond) {
        	vz = -vz;
        }
    }

    public double[] Repulsion(double angleOiseau1, double angleOiseau2, double coeffRepulsion) {
		
		  double[] newV = new double[2];
		  
		  double diff = angleOiseau1 - angleOiseau2;
		  angleOiseau1 += coeffRepulsion * diff;
		
		  double newVx1 = Math.cos(Math.toRadians(angleOiseau1));
		  double newVy1 = Math.sin(Math.toRadians(angleOiseau1));
		
		  newV[0] = newVx1;
		  newV[1] = newVy1;
		
		  return newV;
		}
		
	public double[] Alignement(double angleOiseau1, double angleOiseau2, double coeffAlignement) {
		
		  double[] newV = new double[2];
		  
		  double diff = angleOiseau1 - angleOiseau2;
		  angleOiseau1 -= coeffAlignement * diff;
		
		  double newVx1 = Math.cos(Math.toRadians(angleOiseau1));
		  double newVy1 = Math.sin(Math.toRadians(angleOiseau1));
		
		  newV[0] = newVx1;
		  newV[1] = newVy1;
		
		  return newV;
		}
			
	public double[] Attraction(double angleOiseau1, double angleOiseau2, double coeffAttraction) {
			
		  double[] newV = new double[2];
		  
		  double diff = angleOiseau1 - angleOiseau2;
		  angleOiseau1 -= coeffAttraction * diff;
		
		  double newVx1 = Math.cos(Math.toRadians(angleOiseau1));
		  double newVy1 = Math.sin(Math.toRadians(angleOiseau1));
		
		  newV[0] = newVx1;
		  newV[1] = newVy1;
		
		  return newV;
			
		}

	public void dessiner(Graphics g) {
	    Graphics2D g2d = (Graphics2D) g;
	
	    // Dessiner un curseur sous forme de flèche pour indiquer la direction de l'oiseau
	    double cursorLength = 10*z; // Longueur du curseur
	    double cursorAngle = Math.atan2(vy, vx); // Angle de la direction de l'oiseau
	
	    // Coordonnées des points de la flèche
	    int[] xPoints = {(int) x, (int) (x - cursorLength * Math.cos(cursorAngle - Math.PI / 8)), 
	                     (int) (x - cursorLength * Math.cos(cursorAngle + Math.PI / 8))};
	    int[] yPoints = {(int) y, (int) (y - cursorLength * Math.sin(cursorAngle - Math.PI / 8)), 
	                     (int) (y - cursorLength * Math.sin(cursorAngle + Math.PI / 8))};
	
	    // Dessiner la flèche
	    Polygon arrow = new Polygon(xPoints, yPoints, 3);
	    g2d.setColor(espece); // Couleur de la flèche (noir)
	    g2d.fill(arrow);
	    }
	}




class NueeOiseaux extends JPanel { 
	
	// Valeur par défaut
    boolean isPaused = false;
    int Vitesse = 3; 
	int NombreOiseaux;
	double coeffRepulsion = 1; 
	double coeffAlignement = 1/8; 
	double coeffAttraction = 1; 
    
	int Largeur;
	int Hauteur; 
	int Plafond;
	int TailleCase;
	int NombreColonnes, NombreLignes;

    ArrayList<Oiseaux> NueeOiseaux;
	ArrayList<Cases> grille;
    
    public NueeOiseaux(int Largeur, int Hauteur, int Plafond, Grille grille) {
        this.NueeOiseaux = new ArrayList<>();  
    	
    	// Récupération des infos
    	this.Largeur = Largeur;
    	this.Hauteur = Hauteur;
    	this.Plafond = Plafond;
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
            double vx = oiseau.vx;
			double vy = oiseau.vy;

            // double moduleOiseau = Math.sqrt(vx * vx + vy * vy);
	        // double angleOiseau = Math.toDegrees(Math.acos(vx / moduleOiseau));
	        // if (vy < 0) {
	        //     angleOiseau = 360 - angleOiseau;
	        // }        	
        	
	        oiseau.Deplacer(Vitesse,Largeur,Hauteur,Plafond);
	        
	        double x = oiseau.x;
	        double y = oiseau.y;
	        int NumCase = oiseau.NumCase;
	        
	        // si l'oiseau change de case on màj les population
	        if (Coord_to_Num(x,y) != NumCase) {
	        	oiseau.NumCase = Coord_to_Num(x,y);
	        	for (Cases c : grille) {
	            	if (c.NumCase == oiseau.NumCase) {
	            		c.Population.add(oiseau);
	            	}
	        	}
	            // supprime l'oiseau de son ancienne case	
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
            double z = 2; //Math.random() * (Plafond-1) + 1;
            int NumCase = Coord_to_Num(x, y);
            ArrayList<Color> ListeColor = new ArrayList<>();
            ListeColor.add(Color.BLACK);
            // ListeColor.add(Color.RED);
            // ListeColor.add(Color.BLUE);
            // ListeColor.add(Color.GREEN);
            Color espece = ListeColor.get(new Random().nextInt(ListeColor.size()));
			Oiseaux oiseau = new Oiseaux(x, y, z, NumCase, espece);
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
        this.coeffRepulsion = coeffRepulsion/100;
    }
    
    public void setAlignement(int coeffAlignement) {
        this.coeffAlignement = coeffAlignement/800;
    }
    
    public void setAttraction(int coeffAttraction) {
        this.coeffAttraction = coeffAttraction/100;
    }
    
    public void Boid() {
    	
		for (Oiseaux oiseau1 : NueeOiseaux) {		
			for (Cases c : grille) {
				if (c.NumCase == oiseau1.NumCase) {
                    // on initialise les oiseaux voisins avec la population de la case c
					ArrayList<Oiseaux> OiseauVoisin = new ArrayList<>(c.Population);
					for (Cases caseVoisine : c.CaseVoisine) {
						OiseauVoisin.addAll(caseVoisine.Population);
					
						for (Oiseaux oiseau2 : OiseauVoisin) {						
							if (oiseau1 != oiseau2) {
								
								double x1 = oiseau1.x;
								double y1 = oiseau1.y;
								double z1 = oiseau1.z;
								double x2 = oiseau2.x;
								double y2 = oiseau2.y;
								double z2 = oiseau2.z;
								
								double diffAltitude = Math.abs(z2-z1);
								double distance = Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2));
									
								double vx1 = oiseau1.vx;
								double vy1 = oiseau1.vy;
								double vx2 = oiseau2.vx;
								double vy2 = oiseau2.vy;
								
								Color espece1 = oiseau1.espece;
								Color espece2 = oiseau2.espece;
								
								double moduleOiseau1 = Math.sqrt(vx1 * vx1 + vy1 * vy1);
						        double angleOiseau1 = Math.toDegrees(Math.acos(vx1 / moduleOiseau1));
						        if (vy1 < 0) {
						            angleOiseau1 = 360 - angleOiseau1;
						        }
						        double moduleOiseau2 = Math.sqrt(vx2 * vx2 + vy2 * vy2);
						        double angleOiseau2 = Math.toDegrees(Math.acos(vx2 / moduleOiseau2));
						        if (vy2 < 0) {
						            angleOiseau2 = 360 - angleOiseau2;
						        }
						        
						        if (diffAltitude < 1 && espece1 == espece2){
									if (distance < TailleCase/3) {
										double[] newV = oiseau1.Repulsion(angleOiseau1, angleOiseau2, coeffRepulsion);
										oiseau1.vx = newV[0];
										oiseau1.vy = newV[1];
										oiseau1.vz = oiseau2.vz;
									}
									
									else if (distance < 2*TailleCase/3) {
										double[] newV = oiseau1.Alignement(angleOiseau1, angleOiseau2, coeffAlignement);
										oiseau1.vx = newV[0];
										oiseau1.vy = newV[1];
										oiseau1.vz = oiseau2.vz;
									}
									
									else if (distance < TailleCase) {
										double[] newV = oiseau1.Attraction(angleOiseau1, angleOiseau2, coeffAttraction);
										oiseau1.vx = newV[0];
										oiseau1.vy = newV[1];
										oiseau1.vz = oiseau2.vz;
									}
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
    	
    	// Réglage grand écran
    	// int LargeurEcran = 1730; 
        // int HauteurEcran = 940;
    	// Réglage petit écran 
        int LargeurEcran = 1250; 
        int HauteurEcran = 750; 
        
        // espace pour le pannel
        int PannelSpace = 180;
        
        // coté d'une case
        int TailleCase = 60; 
        
        // hauteur plafond
        int Plafond = 3;
        
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
        NueeOiseaux nueeOiseaux = new NueeOiseaux(Largeur, Hauteur, Plafond, grille); 
        frame.add(nueeOiseaux);
        


        // gestion du pannel de controle
      
        // Ajout un slider pour le rayons de répulsion entre 10 et 30, avec une valeur initiale de 20
        JSlider repulsionSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 3);
        repulsionSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int coeffRepulsion = repulsionSlider.getValue();
                nueeOiseaux.setRepulsion(coeffRepulsion);
            }
        });
        
        // Ajout un slider pour le rayon d'alignement entre 30 et 50, avec une valeur initiale de 50
        JSlider alignementSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 3);
        alignementSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int coeffAlignement = alignementSlider.getValue();
                nueeOiseaux.setAlignement(coeffAlignement);
            }
        });
        
        // Ajout un slider pour le rayon d'attraction entre 50 et 80, avec une valeur initiale de 60
        JSlider attractionSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 3);
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




