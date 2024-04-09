import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.ArrayList;
import java.util.Random;

import java.awt.Graphics;
import java.awt.Graphics2D;



class Oiseau {
	
    double x, y;
    double vx, vy;
    int NumCase;
    
    public Oiseau(double x, double y, int NumCase) {
    	
        this.x = x;
        this.y = y;
        this.NumCase = NumCase;
       
        // Choix de la direction : (random.nextBoolean() ? 1 : -1)
        // Choix de la norm entre 0 et 1 inclus : random.nextInt(101)/100
        Random random = new Random();
        this.vx = (random.nextBoolean() ? 1 : -1) * (double) random.nextInt(101)/100;
        this.vy = (random.nextBoolean() ? 1 : -1) * (double) random.nextInt(101)/100;
    }
    
    public void deplacer(int vitesse, int largeur, int hauteur) {

    	x += vx * vitesse;
        y += vy * vitesse;

        // Rebondir à l'intérieur de la fenêtre
        if (x < 0 || x > largeur) {
        	vx = -vx;
        }
        if (y < 0 || y > hauteur) {
        	vy = -vy;
        }
        
        // Traverser la fenêtre
        //if (x < 0) {
        //	x = largeur;
        //}
        //if (y < 0) {
        //	y = hauteur;
        //}
        //if (x > largeur) {
        //	x = 0;
        //}
        //if (y > hauteur) {
        //	y = 0;
        //}
    }

//    public void Repulsion(double Xo, double Yo, double dt) {
//    	   
//        double CoeffRepulsion = 1; 
//
//        double dx = x-Xo;
//        double dy = y-Yo;
//
//        double r = Math.sqrt(dx*dx+dy*dy); //Distance entre le Boid considéré et le Boid de coordonnées (Xo,Yo)
//        
//        //Mise à jour de la vitesse
//        this.vx = this.vx+CoeffRepulsion*dt*dx/Math.pow(r,3); 
//        this.vy = this.vy+CoeffRepulsion*dt*dy/Math.pow(r,3); 
//
//        this.x = this.x+dt*this.vx;
//        this.y = this.y+dt*this.vy; //Mise à jour de la position
//    }
//    
//    
//    public void Alignement(double Vox, double Voy) {
//    	
//	      double kal=1/8; //Coefficient d'alignement (kal < 1)
//	
//	      double N=Math.sqrt(this.vx*this.vx+this.vy*this.vy); //Norme de la vitesse du Boid considéré
//	      double No=Math.sqrt(Vox*Vox+Voy*Voy); //Norme de la vitesse du Boid de vitesse (Vxo,Vyo)
//	
//	      this.vx=N*(this.vx/N+kal*Vox/No)/Math.sqrt((this.vx/N+kal*Vox/No)*(this.vx/N+kal*Vox/No)+(this.vy/N+kal*Voy/No)*(this.vy/N+kal*Voy/No)); //Variation de vx
//	      this.vy=N*(this.vy/N+kal*Voy/No)/Math.sqrt((this.vx/N+kal*Vox/No)*(this.vx/N+kal*Vox/No)+(this.vy/N+kal*Voy/No)*(this.vy/N+kal*Voy/No)); //Variation de vy
//	      //Remarque: la norme de v est inchangée!
//	}
//  
//    
//    public void Attraction(double Xo, double Yo, double dt) {
//	  
//	      double kat=1; //Coefficient d'attraction
//	
//	      double dx = this.x-Xo;
//	      double dy = this.y-Yo;
//	
//	      double r = Math.sqrt(dx*dx+ dy*dy); //Distance entre les Boids
//	
//	      this.vx=this.vx+kat*dt*-dx/Math.pow(r,3);
//	      this.vy=this.vy+kat*dt*-dy/Math.pow(r,3); //Mise à jour de la vitesse
//	      
//	      this.x = this.x+dt*this.vx;
//	      this.y = this.y+dt*this.vy; //Mise à jour de la position
//  }
    
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
  


class Case {
	
	int NumCase;
    int X, Y;
    int CaseLargeur, CaseHauteur;
    ArrayList<Case> CaseVoisine; 

    public Case(int NumCase, int X, int Y, int CaseLargeur, int CaseHauteur) {
        this.NumCase = NumCase;
        this.X = X;
        this.Y = Y;
        this.CaseLargeur = CaseLargeur;
        this.CaseHauteur = CaseHauteur;
        CaseVoisine = new ArrayList<>();
    }
    
    
    public void ajouterVoisin(Case voisin) {
        CaseVoisine.add(voisin);
    }
    

    public void dessiner(Graphics2D g2d) {
        g2d.setColor(Color.BLACK); 
        g2d.drawRect(X, Y, CaseLargeur, CaseHauteur); 
    }
}



class Cadrillage extends JPanel {
	private static final long serialVersionUID = 1L;
	
	ArrayList<Case> Cadrillage;
	int CaseLargeur, CaseHauteur;
	int NombreColonnes, NombreLignes;

	public Cadrillage(int CaseLargeur, int CaseHauteur, int NombreColonnes, int NombreLignes) {
        Cadrillage = new ArrayList<>();
        this.NombreLignes = NombreLignes;
        this.NombreColonnes = NombreColonnes;
        this.CaseLargeur = CaseLargeur;
        this.CaseHauteur = CaseHauteur;
                
        // Création des cases du cadrillage
        for (int i = 0; i < NombreLignes; i++) {
            for (int j = 0; j < NombreColonnes; j++) {
            	int NumCase = i * NombreColonnes + j; //0 à N-1
            	// Coordonnées du point en haut à gauche de chaque case
            	// X abcisse et Y ordonnée et origine en haut à gauche
                int X = j * CaseLargeur;
                int Y = i * CaseHauteur;
                
                

                Case nouvelleCase = new Case(NumCase, X, Y, CaseLargeur, CaseHauteur);
                Cadrillage.add(nouvelleCase);
            }
        }

        // Création de la liste des voisins (9 cases)
        for (int i = 0; i < NombreLignes; i++) {
            for (int j = 0; j < NombreColonnes; j++) {
            	// Identification de la case avec NumCase
                int NumCase = i * NombreColonnes + j;
                //case est un mot clé donc on utilise c
                Case c = Cadrillage.get(NumCase);
                calculerVoisins(c, NombreColonnes, NombreLignes, Cadrillage);
            }
        }
	}
	
    public void calculerVoisins(Case Case, int NombreColonnes, int NombreLignes, ArrayList<Case> Cadrillage) {
    	// Récupération des infos 
        int X = Case.X;
        int Y = Case.Y;
        int NumCase = Case.NumCase;
        int CaseLargeur = Case.CaseLargeur;
        int CaseHauteur = Case.CaseHauteur;
       
        // Liste des déplacements possibles pour trouver les voisins
        int[][] deplacements = {{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};

        // Parcours des déplacements possibles pour trouver les voisins
        for (int[] deplacement : deplacements) {
            int VoisinX = X + deplacement[0] * CaseLargeur;
            int VoisinY = Y + deplacement[1] * CaseHauteur;
            
            // Vérification si le voisin est dans les limites du cadrillage
            // A faire si les oiseau "rebondissent" sur les bords
            if (VoisinY >= 0 && VoisinY < NombreLignes * CaseHauteur &&
            		VoisinX >= 0 && VoisinX < NombreColonnes * CaseLargeur) {
            	
                // Calcul de l'indice du voisin dans le cadrillage
            	int VoisinNum = NumCase + deplacement[1] * NombreColonnes + deplacement[0]; 
                Case voisin = Cadrillage.get(VoisinNum); 
                Case.ajouterVoisin(voisin);
            }
        }
    }
    
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Case c : Cadrillage) {  
        	c.dessiner(g2d);
        }
	}
}



class NuageOiseaux extends JPanel { 
	private static final long serialVersionUID = 1L;
	
    boolean isPaused = false;
    int Vitesse = 3; // Vitesse des oiseaux par défaut
	int NombreOiseaux;
    
    ArrayList<Oiseau> NuageOiseaux;
    int CaseLargeur, CaseHauteur;
	int NombreColonnes, NombreLignes;
	int largeur,hauteur;
	public Cadrillage cadrillage;
    
    public NuageOiseaux(Cadrillage cadrillage) {
    	this.cadrillage = cadrillage;
        this.NuageOiseaux = new ArrayList<>();  
    	
    	// Récupération des infos
    	this.CaseHauteur = cadrillage.CaseHauteur; 
    	this.CaseLargeur = cadrillage.CaseLargeur;
    	this.NombreColonnes = cadrillage.NombreColonnes; 
    	this.NombreLignes = cadrillage.NombreLignes;
    	this.largeur = CaseLargeur * NombreColonnes;
    	this.hauteur = CaseHauteur * NombreLignes;
        
        // Créer une minuterie pour mettre à jour la simulation toutes les 10 millisecondes
        Timer timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) {
                    DeplacerOiseaux();
                    //Boid();
                    repaint(); // Redessiner la fenêtre
                }
            }
        });

        timer.start();
    }
	
    public void DeplacerOiseaux() {
        for (Oiseau oiseau : NuageOiseaux) {
            oiseau.deplacer(Vitesse,largeur,hauteur);
            // il faut un màj de la case
            int NumCase = Coord_to_Num((double) oiseau.x, (double) oiseau.y);
            oiseau.NumCase = NumCase;
        }
    }
    
    public int Coord_to_Num(double x, double y) {
        int i = (int) x % CaseHauteur;
        int j = (int) y % CaseLargeur;
        return i * NombreColonnes + j;
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
    	// On Ccréé ou recréé une simulation
        NuageOiseaux.clear();
        for (int i = 0; i < NombreOiseaux; i++) {
            double x = Math.random() * getWidth();
            double y = Math.random() * getHeight();
            int NumCase = Coord_to_Num((double) x, (double) y);
            NuageOiseaux.add(new Oiseau((int) x, (int) y, (int) NumCase));
        }
    }
     
//    int DistRepulsion = 10;
//	  int DistAlignement = 15;
//	  int DistAttraction = 20;
//	
//    public void Boid() {
//    	//System.out.println("\n");
//		for (Oiseau oiseau1 : NuageOiseaux) {		
//			for (Oiseau oiseau2 : NuageOiseaux) {
//				int NumCase1 = oiseau1.NumCase;
//				int NumCase2 = oiseau2.NumCase;
//				
//				if (NumCase1 == NumCase2 && oiseau1 != oiseau2) {
//					System.out.println("meme case");
//					
//					double x1 = oiseau1.x;
//					double y1 = oiseau1.y;
//					double x2 = oiseau2.x;
//					double y2 = oiseau2.y;
//					
//					double distance = Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2));
//					
//					if (distance < DistRepulsion) {
//						oiseau1.Repulsion((double) x1, (double) y1, (double) x2, (double) y2, (double) distance);
//					}
//					
//					else if (distance < DistAlignement) {
//						oiseau1.Alignement();
//					}
//					
//					else if (distance < DistAttraction) {
//						oiseau1.Attraction();
//					}
//				}
//			}
//	    }
//	}
    
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	Graphics2D g2d = (Graphics2D) g;
        
        for (Oiseau oiseau : NuageOiseaux) {
        	oiseau.dessiner(g2d);
        }    
    }
}



public class SimulationNuageOiseaux {
	
    private static boolean isPaused = false;

    public static void main(String[] args) {
    	// Réglage grand écran : 1920x900
//    	int LargeurEcran = 1900; 
//        int HauteurEcran = 900;  
    	// Réglage petit écran : 1440x700 
        int LargeurEcran = 1440; 
        int HauteurEcran = 700;  
        int PannelSpace = 80; 
        
        // Paramètre par défault
        JFrame frame = new JFrame("Simulation Nuage d'Oiseaux");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(LargeurEcran, HauteurEcran+PannelSpace);
        
        int CaseLargeur = 60; 
        int CaseHauteur = 60; 
        
        int NombreColonnes = LargeurEcran/CaseLargeur; 
        int NombreLignes = HauteurEcran/CaseHauteur; 
        
        // Création du Cadrillage
        // Rappel : Cadrillage(int LargeurEcran, int HauteurEcran, int NombreColonnes, int NombreLignes)
        Cadrillage cadrillage = new Cadrillage(CaseLargeur, CaseHauteur, NombreColonnes, NombreLignes);
        // Ajouter l'affichage
        frame.add(cadrillage);
        
        // Créer un nuage d'oiseaux sur le cadrillage
        // Rappel : NuageOiseaux(Cadrillage cadrillage)
        NuageOiseaux nuageOiseaux = new NuageOiseaux(cadrillage); // Nombre d'oiseaux initial (à revoir)
        frame.add(nuageOiseaux);
        
        
        // Ajout d'un bouton de pause
        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new ActionListener() { 
        	//Lorsque ce bouton est cliqué
            public void actionPerformed(ActionEvent e) {
                isPaused = !isPaused;
                nuageOiseaux.setPause(isPaused);
            }
        });

        // Ajout d'un slider pour la valeur de la vitesse entre 1 et 10, avec une valeur initiale de 5
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 3);
        // Définit l'espacement principal des graduations du curseur à 1
        speedSlider.setMajorTickSpacing(1);
        // Active le dessin des graduations sur le curseur
        speedSlider.setPaintTicks(true);
        speedSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int Vitesse = speedSlider.getValue();
                nuageOiseaux.setSpeed(Vitesse);
            }
        });

        // Ajout d'un champ de texte pour entrer le nombre d'oiseaux
        // La taille du champ de texte est limitée à 5 (unité ?)
        JTextField nbrOiseauxField = new JTextField(5);
        //Définit une valeur par défaut de 2 oiseaux
        nbrOiseauxField.setText("2");  

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
	                    nuageOiseaux.setNbrOiseaux(NombreOiseaux); }
                // Si un nombre non valide est entré
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Veuillez entrer un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Création du panneau de contrôle
        JPanel controlPanel = new JPanel();
        controlPanel.add(pauseButton);
        controlPanel.add(new JLabel("Vitesse:"));
        controlPanel.add(speedSlider);
        controlPanel.add(new JLabel("Nombre d'oiseaux:"));
        controlPanel.add(nbrOiseauxField);
        controlPanel.add(applyButton);

        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}




