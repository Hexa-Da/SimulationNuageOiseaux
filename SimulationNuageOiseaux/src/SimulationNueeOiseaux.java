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



class Oiseaux {
	
    double x, y;
    double vx, vy;
   
    public Oiseaux(double x, double y) {
    	
        this.x = x;
        this.y = y;
       
        // Choix de la direction : (random.nextBoolean() ? 1 : -1)
        // Choix de la norm entre 0 et 1 inclus : random.nextInt(101)/100
        Random random = new Random();
        this.vx = (random.nextBoolean() ? 1 : -1) * (double) random.nextInt(101)/100;
        this.vy = (random.nextBoolean() ? 1 : -1) * (double) random.nextInt(101)/100;
    }
    
    public void Deplacer(double angleOiseau, int vitesse, int largeur, int hauteur) {

    	x += vx * vitesse;
        y += vy * vitesse;
        
        Random random = new Random();
        double variation = (random.nextBoolean() ? 1 : -1) * random.nextDouble() * 10;
        angleOiseau += variation;

        double newVx = Math.cos(Math.toRadians(angleOiseau));
        double newVy = Math.sin(Math.toRadians(angleOiseau));

        vx = newVx;
        vy = newVy;
        
        // Traverser la fenêtre
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
	int rayonRepulsion = 20; 
	int rayonAlignement = 40; 
	int rayonAttraction = 60; 
    
    ArrayList<Oiseaux> NueeOiseaux;
	int Largeur;
	int Hauteur;   
    
    public NueeOiseaux(int Largeur, int Hauteur) {
        this.NueeOiseaux = new ArrayList<>();  
    	
    	// Récupération des infos
    	this.Largeur = Largeur;
    	this.Hauteur = Hauteur;
        
        // Créer une minuterie pour mettre à jour la simulation toutes les 10 millisecondes
        Timer timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isPaused) {
                    DeplacerOiseaux();
                    Boid(rayonRepulsion, rayonAlignement, rayonAttraction);
                    repaint(); // Redessiner la fenêtre
                }
            }
        });

        timer.start();
    }
	
    public void DeplacerOiseaux() {
        for (Oiseaux oiseau : NueeOiseaux) {
            
            double vx = oiseau.vx;
			double vy = oiseau.vy;
			
            double moduleOiseau = Math.sqrt(vx * vx + vy * vy);
	        double angleOiseau = Math.toDegrees(Math.acos(vx / moduleOiseau));
	        if (vy < 0) {
	            angleOiseau = 360 - angleOiseau;
	        }
	        oiseau.Deplacer(angleOiseau,Vitesse,Largeur,Hauteur);
        }
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
        NueeOiseaux.clear();
        for (int i = 0; i < NombreOiseaux; i++) {
            double x = Math.random() * getWidth();
            double y = Math.random() * getHeight();
            NueeOiseaux.add(new Oiseaux((double) x, (double) y));
        }
    }
	
    public void setRepulsion(int rayonRepulsion) {
        this.rayonRepulsion = rayonRepulsion;
    }
    
    public void setAlignement(int rayonAlignement) {
        this.rayonAlignement = rayonAlignement;
    }
    
    public void setAttraction(int rayonAttraction) {
        this.rayonAttraction = rayonAttraction;
    }
    
    public void Boid(int rayonRepulsion, int rayonAlignement, int rayonAttraction) {
    	
		for (Oiseaux oiseau1 : NueeOiseaux) {		
			for (Oiseaux oiseau2 : NueeOiseaux) {
				
				double x1 = oiseau1.x;
				double y1 = oiseau1.y;
				double x2 = oiseau2.x;
				double y2 = oiseau2.y;
				
				double distance = Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2));
				
				if (oiseau1 != oiseau2 && distance < rayonAttraction) {
					
					double vx1 = oiseau1.vx;
					double vy1 = oiseau1.vy;
					double vx2 = oiseau2.vx;
					double vy2 = oiseau2.vy;
					
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
					
					if (distance < rayonRepulsion) {
						double[] newV = oiseau1.Repulsion(angleOiseau1, angleOiseau2);
						oiseau1.vx = newV[0];
						oiseau1.vy = newV[1];
					}
					
					else if (distance < rayonAlignement) {
						double[] newV = oiseau1.Alignement(angleOiseau1, angleOiseau2);
						oiseau1.vx = newV[0];
						oiseau1.vy = newV[1];
					}
					
					else {
						double[] newV = oiseau1.Attraction(angleOiseau1, angleOiseau2);
						oiseau1.vx = newV[0];
						oiseau1.vy = newV[1];
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
    	// Réglage grand écran : 1920x900
    	int LargeurEcran = 1800; 
        int HauteurEcran = 980;  
    	// Réglage petit écran : 1440x700 
//        int LargeurEcran = 1440; 
//        int HauteurEcran = 700;  
        int PannelSpace = 120; 
        
        // Paramètre par défault
        JFrame frame = new JFrame("Simulation Nuage d'Oiseaux");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(LargeurEcran+PannelSpace, HauteurEcran);
        
        // Créer un nuage d'oiseaux sur le cadrillage
        NueeOiseaux nueeOiseaux = new NueeOiseaux(LargeurEcran, HauteurEcran); 
        frame.add(nueeOiseaux);
        
        // Ajout un slider pour le rayons de répulsion entre 10 et 30, avec une valeur initiale de 20
        JSlider repulsionSlider = new JSlider(JSlider.HORIZONTAL, 10, 30, 20);
        repulsionSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int rayonRepulsion = repulsionSlider.getValue();
                nueeOiseaux.setRepulsion(rayonRepulsion);
            }
        });
        
        // Ajout un slider pour le rayon d'alignement entre 30 et 50, avec une valeur initiale de 50
        JSlider alignementSlider = new JSlider(JSlider.HORIZONTAL, 30, 50, 40);
        alignementSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int rayonAlignement = alignementSlider.getValue();
                nueeOiseaux.setAlignement(rayonAlignement);
            }
        });
        
        // Ajout un slider pour le rayon d'attraction entre 50 et 80, avec une valeur initiale de 60
        JSlider attractionSlider = new JSlider(JSlider.HORIZONTAL, 50, 80, 60);
        attractionSlider.addChangeListener(new ChangeListener() {
        	// Lorsque la valeur du curseur change
            public void stateChanged(ChangeEvent e) {
                int rayonAttraction = attractionSlider.getValue();
                nueeOiseaux.setAttraction(rayonAttraction);
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

        
        // Création du panneau de contrôle avec un BoxLayout
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        
        controlPanel.add(Box.createVerticalStrut(10)); // Espacement vertical
        controlPanel.add(new JLabel("Rayon de répulsion:"));
        controlPanel.add(repulsionSlider);
        controlPanel.add(Box.createVerticalStrut(10)); 
        controlPanel.add(new JLabel("Rayon d'alignement:"));
        controlPanel.add(alignementSlider);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(new JLabel("Rayon d'attraction:"));
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




